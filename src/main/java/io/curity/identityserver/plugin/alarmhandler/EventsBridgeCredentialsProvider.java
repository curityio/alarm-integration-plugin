package io.curity.identityserver.plugin.alarmhandler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

public class EventsBridgeCredentialsProvider {

    private final EventsBridgeAlarmConfiguration _configuration;
    private final Logger _logger;

    public EventsBridgeCredentialsProvider(EventsBridgeAlarmConfiguration configuration) {
        this._configuration = configuration;
        this._logger = LoggerFactory.getLogger(EventsBridgeCredentialsProvider.class);
    }

    /*
     * Deal with the different ways of supplying AWS credentials
     */
    public AwsCredentialsProvider get() {

        AwsCredentialsProvider creds = null;
        EventsBridgeAlarmConfiguration.AWSAccessMethod accessMethod = this._configuration.getEventsBridgeAccessMethod();

        // Use Instance Profile from IAM Role applied to EC2 instance
        Optional<Boolean> isEC2InstanceProfile = accessMethod.isEC2InstanceProfile();
        if (isEC2InstanceProfile.isPresent() && isEC2InstanceProfile.get()) {
            creds = InstanceProfileCredentialsProvider.builder().build();
        }

        // Use AccessKey and Secret from config
        else if (accessMethod.getAccessKeyIdAndSecret().isPresent()) {

            EventsBridgeAlarmConfiguration.AWSAccessMethod.AccessKeyIdAndSecret key =
                    accessMethod.getAccessKeyIdAndSecret().get();

            creds = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(key.getAccessKeyId().get(), key.getAccessKeySecret().get()));

            // If roleARN is present, get temporary credentials through AssumeRole
            Optional<String> roleARN = key.getAwsRoleARN();
            if (roleARN.isPresent()) {
                creds = getNewCredentialsFromAssumeRole(creds, roleARN.get());
            }
        }

        // If a profile name is defined get credentials from configured profile from ~/.aws/credentials
        else if (accessMethod.getAWSProfile().get().getAwsProfileName().isPresent())
        {
            EventsBridgeAlarmConfiguration.AWSAccessMethod.AWSProfile profile = accessMethod.getAWSProfile().get();

            creds = ProfileCredentialsProvider.builder()
                    .profileName(profile.getAwsProfileName().get())
                    .build();

            // If roleARN is present, get temporary credentials through AssumeRole
            Optional<String> roleARN = profile.getAwsRoleARN();
            if (roleARN.isPresent()) {
                creds = getNewCredentialsFromAssumeRole(creds, roleARN.get());
            }
        }

        return creds;
    }

    /*
     * Create temporary credentials for an assumed role
     */
    private AwsCredentialsProvider getNewCredentialsFromAssumeRole(AwsCredentialsProvider creds, String roleARN)
    {
        StsClient stsClient = StsClient.builder()
                .region(Region.of(this._configuration.getRegionName()))
                .credentialsProvider(creds)
                .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .durationSeconds(3600)
                .roleArn(roleARN)
                .roleSessionName("curity-alarm-handler-session")
                .build();

        this._logger.info("AssumeRole Request for Events Bridge Alarm Handler");
        AssumeRoleResponse assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);
        if (!assumeRoleResult.sdkHttpResponse().isSuccessful())
        {
            this._logger.warn("AssumeRole Request sent but was not successful: {}",
                    assumeRoleResult.sdkHttpResponse().statusText().get() );
            return creds;
        }
        else
        {
            Credentials credentials = assumeRoleResult.credentials();
            AwsSessionCredentials asc = AwsSessionCredentials.create(
                    credentials.accessKeyId(),
                    credentials.secretAccessKey(),
                    credentials.sessionToken());

            _logger.info("AssumeRole Request successful: {}", assumeRoleResult.sdkHttpResponse().statusText());
            return StaticCredentialsProvider.create(asc);
        }
    }
}

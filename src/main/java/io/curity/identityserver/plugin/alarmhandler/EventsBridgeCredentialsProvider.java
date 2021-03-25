package io.curity.identityserver.plugin.alarmhandler;

import java.util.Optional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class EventsBridgeCredentialsProvider {

    /*
     * Deal with the different ways of supplying AWS credentials
     */
    public static AwsCredentialsProvider get(EventsBridgeAlarmConfiguration.AWSAccessMethod accessMethod) {

        AwsCredentialsProvider creds = null;

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
                // creds = getNewCredentialsFromAssumeRole(creds, roleARN.get());
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
                // creds = getNewCredentialsFromAssumeRole(creds, roleARN.get());
            }
        }

        return creds;
    }

    /*
    private AwsCredentialsProvider getNewCredentialsFromAssumeRole(AwsCredentialsProvider creds, String roleARN)
    {
        StsClient stsClient = StsClient.builder()
                .region(_awsRegion)
                .credentialsProvider(creds)
                .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .durationSeconds(3600)
                .roleArn(roleARN)
                .roleSessionName("curity-alarm-handler-session")
                .build();

        try
        {
            AssumeRoleResponse assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);

            if (!assumeRoleResult.sdkHttpResponse().isSuccessful())
            {
                _logger.warn("AssumeRole Request sent but was not successful: {}",
                        assumeRoleResult.sdkHttpResponse().statusText().get() );
                return creds; //Returning the original credentials
            }
            else
            {
                Credentials credentials = assumeRoleResult.credentials();

                AwsSessionCredentials asc = AwsSessionCredentials.create(credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());

                _logger.debug("AssumeRole Request successful: {}", assumeRoleResult.sdkHttpResponse().statusText());

                return StaticCredentialsProvider.create(asc); //returning temp credentials from the assumed role
            }
        }
        catch (Exception e)
        {
            _logger.debug("AssumeRole Request failed: {}", e.getMessage());
            throw _exceptionFactory.internalServerException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
    }*/
}

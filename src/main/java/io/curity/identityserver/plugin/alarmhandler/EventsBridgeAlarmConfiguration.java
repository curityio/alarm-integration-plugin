/*
 *  Copyright 2021 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.alarmhandler;

import java.util.Optional;
import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.OneOf;
import se.curity.identityserver.sdk.config.annotation.DefaultString;
import se.curity.identityserver.sdk.config.annotation.Description;

/*
 * These properties will be configured in the Curity Identity Server under Alarm Handlers
 */
public interface EventsBridgeAlarmConfiguration extends Configuration {

    @Description("The AWS region name")
    @DefaultString("us-east-1")
    String getRegionName();

    @Description("The AWS event bus that will receive the alarm data")
    @DefaultString("curity.events")
    String getEventBusName();

    @Description("The AWS event rule's pattern will use this value")
    @DefaultString("curity.identityserver")
    String getDataSourceName();

    @Description("Choose how to access AWS Events Bridge")
    AWSAccessMethod getEventsBridgeAccessMethod();

    interface AWSAccessMethod extends OneOf
    {
        Optional<AccessKeyIdAndSecret> getAccessKeyIdAndSecret();
        Optional<AWSProfile> getAWSProfile();

        interface AccessKeyIdAndSecret
        {
            Optional<String> getAccessKeyId();

            Optional<String> getAccessKeySecret();

            @Description("Optional role ARN used when requesting temporary credentials, ex. arn:aws:iam::123456789012:role/events-bridge-role")
            Optional<String> getAwsRoleARN();
        }

        interface AWSProfile
        {
            @Description("AWS Profile name. Retrieves credentials from the system (~/.aws/credentials)")
            Optional<String> getAwsProfileName();

            @Description("Optional role ARN used when requesting temporary credentials, ex. arn:aws:iam::123456789012:role/events-bridge-role")
            Optional<String> getAwsRoleARN();
        }

        @Description("EC2 instance that the Curity Identity Server is running on has been assigned an IAM Role with permissions to Events Bridge.")
        Optional<Boolean> isEC2InstanceProfile();
    }
}

# Alarm Integration Plug-in

[![Quality](https://img.shields.io/badge/quality-experiment-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

An alarm handler plugin that shows how to integrate alarm data from the Curity Identity Server with a Cloud Monitoring system.

## Documentation

This repository is described in the [Alarm Handler Plugin Tutorial](https://curity.io/resources/learn/cloud-alarm-integration), which shows one possible monitoring solution, using the AWS cloud:

- A small plugin sends Curity Alarm Data to the AWS Events Bridge
- The data is saved to Cloudwatch Logs, then queried using Cloudwatch Insights
- A summary of each distinct alarm is then displayed on a Monitoring Dashboard

## More Information

Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.

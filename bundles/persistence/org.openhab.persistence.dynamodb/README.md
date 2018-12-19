# Amazon DynamoDB Persistence

This service allows you to persist state updates using the [Amazon DynamoDB](https://aws.amazon.com/dynamodb/) database. Query functionality is also fully supported.

Features:

* Writing/reading information to relational database systems
* Configurable database table names
* Automatic table creation

## Disclaimer

This service is provided "AS IS", and the user takes full responsibility of any charges or damage to Amazon data.

## Table of Contents

<!-- Using MarkdownTOC plugin for Sublime Text to update the table of contents (TOC) -->
<!-- MarkdownTOC depth=3 autolink=true bracket=round -->

- [Prerequisites](#prerequisites)
	- [Setting Up an Amazon Account](#setting-up-an-amazon-account)
- [Configuration](#configuration)
	- [Basic configuration](#basic-configuration)
	- [Configuration Using Credentials File](#configuration-using-credentials-file)
	- [Advanced Configuration](#advanced-configuration)
- [Details](#details)
	- [Tables Creation](#tables-creation)
	- [Caveats](#caveats)
- [Developer Notes](#developer-notes)
	- [Updating Amazon SDK](#updating-amazon-sdk)

<!-- /MarkdownTOC -->

## Prerequisites

You must first set up an Amazon account as described below.  

Users are recommended to familiarize themselves with AWS pricing before using this service.  Please note that there might be charges from Amazon when using this service to query/store data to DynamoDB. See [Amazon DynamoDB pricing pages](https://aws.amazon.com/dynamodb/pricing/) for more details. Please also note possible [Free Tier](https://aws.amazon.com/free/) benefits. 

### Setting Up an Amazon Account

* [Sign up](https://aws.amazon.com/) for Amazon AWS.
* Select the AWS region in the [AWS console](https://console.aws.amazon.com/) using [these instructions](https://docs.aws.amazon.com/awsconsolehelpdocs/latest/gsg/getting-started.html#select-region). Note the region identifier in the URL (e.g. `https://eu-west-1.console.aws.amazon.com/console/home?region=eu-west-1` means that region id is `eu-west-1`).
* **Create user for openHAB with IAM**
  * Open Services -> IAM -> Users -> Create new Users. Enter `openhab` to _User names_, keep _Generate an access key for each user_ checked, and finally click _Create_.
  * _Show User Security Credentials_ and record the keys displayed
* **Configure user policy to have access for dynamodb**
  * Open Services -> IAM -> Policies
  * Check _AmazonDynamoDBFullAccess_ and click _Policy actions_ -> _Attach_
  * Check the user created in step 2 and click _Attach policy_

## Configuration

This service can be configured in the file `services/dynamodb.cfg`.

### Basic configuration

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| accessKey |        |    Yes   | access key as shown in [Setting up Amazon account](#setting-up-amazon-account). |
| secretKey |        |    Yes   | secret key as shown in [Setting up Amazon account](#setting-up-amazon-account). |
| region   |         |    Yes   | AWS region ID as described in [Setting up Amazon account](#setting-up-amazon-account). The region needs to match the region that was used to create the user. |

### Configuration Using Credentials File

Alternatively, instead of specifying `accessKey` and `secretKey`, one can configure a configuration profile file.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| profilesConfigFile | |  Yes   | path to the credentials file.  For example, `/etc/openhab2/aws_creds`. Please note that the user that runs openHAB must have approriate read rights to the credential file. For more details on the Amazon credential file format, see [Amazon documentation](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html). |
| profile  |         |    Yes   | name of the profile to use |
| region   |         |    Yes   | AWS region ID as described in Step 2 in [Setting up Amazon account](#setting-up-amazon-account). The region needs to match the region that was used to create the user. |

Example of service configuration file (`services/dynamodb.cfg`):

```ini
profilesConfigFile=/etc/openhab2/aws_creds
profile=fooprofile
region=eu-west-1
```

Example of credentials file (`/etc/openhab2/aws_creds`):

````ini
[fooprofile]
aws_access_key_id=testAccessKey
aws_secret_access_key=testSecretKey
````

### Advanced Configuration

In addition to the configuration properties above, the following are also available:

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| readCapacityUnits | 1 |  No   | read capacity for the created tables |
| writeCapacityUnits | 1 | No   | write capacity for the created tables |
| tablePrefix | `openhab-` | No | table prefix used in the name of created tables |

Refer to Amazon documentation on [provisioned throughput](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.ProvisionedThroughput.html) for details on read/write capacity.

All item- and event-related configuration is done in the file `persistence/dynamodb.persist`.

## Details

### Tables Creation

When an item is persisted via this service, a table is created (if necessary). Currently, the service will create at most two tables for different item types. The tables will be named `<prefix><item-type>`, where `<prefix>` matches the `tablePrefix` configuration property; while the `<item-type>` is either `bigdecimal` (numeric items) or `string` (string and complex items).

Each table will have three columns: `itemname` (item name), `timeutc` (in ISO 8601 format with millisecond accuracy), and `itemstate` (either a number or string representing item state).

### Caveats

When the tables are created, the read/write capacity is configured according to configuration. However, the service does not modify the capacity of existing tables. As a workaround, you can modify the read/write capacity of existing tables using the [Amazon console](https://aws.amazon.com/console/).

## Developer Notes

### Updating Amazon SDK

1. Clean `lib/*`
2. Update SDK version in `scripts/fetch_sdk_pom.xml`. You can use the [maven online repository browser](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-dynamodb) to find the latest version available online.
3. `scripts/fetch_sdk.sh`
4. Copy `scripts/target/site/dependencies.html` and `scripts/target/dependency/*.jar` to `lib/`
5. Generate `build.properties` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\\\\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
6. Generate `META-INF/MANIFEST.MF` `Bundle-ClassPath` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
7. Generate `.classpath` entries
`ls lib/*.jar | python -c "import sys;pre='<classpathentry exported=\"true\" kind=\"lib\" path=\"';post='\"/>'; print('\\t' + pre + (post + '\\n\\t' + pre).join(map(str.strip, sys.stdin.readlines())) + post)"`

After these changes, it's good practice to run integration tests (against live AWS DynamoDB) in `org.openhab.persistence.dynamodb.test` bundle. See README.md in the test bundle for more information how to execute the tests.
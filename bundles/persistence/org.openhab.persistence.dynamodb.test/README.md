To run integration tests, one needs to provide AWS credentials.

Eclipse instructions
1. Run all tests (in package org.openhab.persistence.dynamodb.internal) as JUnit Tests
2. Configure the run configuration, and open Arguments sheet
3. In VM arguments, provide the credentials for AWS
````
-DDYNAMODBTEST_REGION=REGION-ID
-DDYNAMODBTEST_ACCESS=ACCESS-KEY
-DDYNAMODBTEST_SECRET=SECRET
````

The tests will create tables with prefix `dynamodb-integration-tests-`. Note that when tests are begun, all data is removed from that table!
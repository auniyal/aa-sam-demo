# AA SAM Stack Application

This project contains source code and supporting files for a serverless application as part of coding exercise for AA . It includes the following files and folders.

- AAFunction/src/main - Code for the application's Lambda function.
- events - Invocation events that you can use to invoke the function.
- AAFunction/src/test - Unit tests for the application code. 
- template.yaml - A template that defines the application's AWS resources.

The application uses following AWS resources.
 1.	SQS Queue 
 2.	Lambda Function (triggered by sqs event)
 3.	Dynamo table

These resources are defined in the `template.yaml` file in this project. 


## Deploy the sample application

To build and deploy this application for the first time, run the following in your shell:

```bash
sam build
sam deploy --guided
```
NB: you would need to install [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) and configure [AWS profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html)

The first command will build the source of your application. The second command will package and deploy your application to AWS, with a series of prompts:

* **Stack Name**: The name of the stack to deploy to CloudFormation. This should be unique to your account and region, and a good starting point would be something matching your project name.
* **AWS Region**: The AWS region you want to deploy your app to.
* **Confirm changes before deploy**: If set to yes, any change sets will be shown to you before execution for manual review. If set to no, the AWS SAM CLI will automatically deploy application changes.
* **Allow SAM CLI IAM role creation**: Many AWS SAM templates, including this example, create AWS IAM roles required for the AWS Lambda function(s) included to access AWS services. By default, these are scoped down to minimum required permissions. To deploy an AWS CloudFormation stack which creates or modified IAM roles, the `CAPABILITY_IAM` value for `capabilities` must be provided. If permission isn't provided through this prompt, to deploy this example you must explicitly pass `--capabilities CAPABILITY_IAM` to the `sam deploy` command.
* **Save arguments to samconfig.toml**: If set to yes, your choices will be saved to a configuration file inside the project, so that in the future you can just re-run `sam deploy` without parameters to deploy changes to your application.

You can find your API Gateway Endpoint URL in the output values displayed after deployment.

## Use the SAM CLI to build and test locally

Build your application with the `sam build` command.

```bash
AWS$ sam build
```

The SAM CLI installs dependencies defined in `AAFunction/build.gradle`, creates a deployment package, and saves it in the `.aws-sam/build` folder.

## Fetch, tail, and filter Lambda function logs
use cloudwatch to view the logs from Lambda function
https://console.aws.amazon.com/cloudwatch/home?region=us-east-1

## Unit tests

Tests are defined in the `AAFunction/src/test` folder in this project.

```bash
AWS$ cd AAFunction
AAFunction$ gradle test
```

## Cleanup

To delete the sample application that you created, use the AWS CLI. Assuming you used your project name for the stack name, you can run the following:

```bash
aws cloudformation delete-stack --stack-name AWS
```

## FAQ

_(1) Why are there two queues?_

I have added [Dead Letter Queue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html) for resilience, its for messages which can’t be processed because of a variety of possible issues. 
Tip: consider sending malformed json message to primary queue

_(2)  How are combinations computed in lambda?_

[Guava](https://guava.dev/releases/22.0/api/docs/com/google/common/collect/Sets.html#powerSet-java.util.Set-) is open source and very popular library. For this exercise I choose this over custom algorithm, as it's well tested and is production ready library 

_(3) How can I test this application?_

Go to [SQS dashboard](https://console.aws.amazon.com/sqs/v2/home?region=us-east-1#/queues) and choose AAPrimaryQueue, then go to _Send and receive messages_ section and send message
OR using aws cli command (example below) 

`aws sqs send-message --queue-url https://sqs.us-east-1.amazonaws.com/380964018971/AAPrimaryQueue --message-body "{ 
   \"input\":[ 
      \"A\",
      \"B\",
      \"C\",
      \"D\"
   ]
}
"
`

_(4) What's being written to DB?_

As was required in spec, dynamo would have all the combinations of input along with message id. Beside that I have added few more attributes to the item for convenience i.e. dateCreated and input
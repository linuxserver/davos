package io.linuxserver.davos.schedule.workflow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SNSNotifyAction implements PostDownloadAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNSNotifyAction.class);

    private AmazonSNSClientBuilder snsClientBuilder = AmazonSNSClient.builder();

    private String region;
    private String arn;
    private String accessKey;
    private String secretAccessKey;

    public SNSNotifyAction(String region, String arn, String accessKey, String secretAccessKey) {

        this.region = region;
        this.arn = arn;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
    }

    @Override
    public void execute(PostDownloadExecution execution) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);

        AmazonSNS sns = snsClientBuilder.withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        LOGGER.debug("SNS: Topic Arn               : {}", arn);
        LOGGER.debug("SNS: Topic Region            : {}", region);
        LOGGER.debug("SNS: Topic Access Key        : {}", accessKey);
        LOGGER.debug("SNS: Topic Secret Access Key : {}", secretAccessKey);

        PublishRequest request = new PublishRequest();
        request.setTopicArn(arn);
        request.setMessageStructure("json");
        request.setMessage(formatJsonMessage(execution.fileName));
        request.setSubject("A new file has been downloaded");

        LOGGER.info("Publishing message to SNS");
        PublishResult result = sns.publish(request);
        LOGGER.info("Publish successful!");
        LOGGER.debug("{}", result.getMessageId());
    }
    
    private String formatJsonMessage(String message) {
        return String.format("{\"default\": \"%s\"}", message);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

package aa;

import aa.dao.RequestDao;
import aa.model.Request;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<SQSEvent, Void> {

    @SneakyThrows
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        RequestDao requestDao = new RequestDao();
        ObjectMapper objectMapper = new ObjectMapper();

        //Iterate over messages
        for (SQSMessage msg : event.getRecords()) {

            logger.log(format("Message received: %s", msg.getBody()));

            //Extract body
            String sqsMessageBody = msg.getBody();
            sqsMessageBody = sqsMessageBody.replaceAll("[\\n\\r\\t]+", "");
            Request request = objectMapper.readValue(sqsMessageBody, Request.class);

            //get all combinations and get json representation
            final ObjectWriter writer = objectMapper.writer().withRootName("response");
            final String responseJson = writer.writeValueAsString(Sets.powerSet(request.getInput()));

            //persist combinations in DB
            requestDao.save(sqsMessageBody, responseJson, msg.getMessageId());
            logger.log(format("Message saved to db:  input[%s]  combinations[%s]", sqsMessageBody, responseJson));

        }

        return null;
    }


}


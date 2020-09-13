package com.aa.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import java.time.Instant;
import java.util.HashMap;

/**
 * @author Ashish Uniyal
 * <p>
 * DAO layer
 */
public class RequestDao {
    private final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String TABLE_NAME = "All_COMBINATIONS";

    /**
     * Saves  items to DB
     *
     * @param input:               json input by user
     * @param allCombinationsJson: all the combination of the user input
     * @param messageId:           message id of the sqs message
     * @return
     */
    public PutItemResult save(String input, String allCombinationsJson, String messageId) {

        var itemValues =
                new HashMap<String, AttributeValue>();

        itemValues.put("id", new AttributeValue(messageId));
        itemValues.put("input", new AttributeValue(input));
        itemValues.put("combinations", new AttributeValue(allCombinationsJson));
        itemValues.put("dateCreated", new AttributeValue(Instant.now().toString()));

        return ddb.putItem(TABLE_NAME, itemValues);

    }

}
            
    
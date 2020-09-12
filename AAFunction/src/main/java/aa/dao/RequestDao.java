package aa.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import java.time.Instant;
import java.util.HashMap;

public class RequestDao {
    final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
    static final String TABLE_NAME = "All_COMBINATIONS";

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
            
    
package aa.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.time.Instant;
import java.util.HashMap;


public class RequestDao {
    public void save(String input, String allCombinationsJson, String messageId) {
        String table_name = "All_COMBINATIONS";
        var item_values =
                new HashMap<String, AttributeValue>();

        item_values.put("id", new AttributeValue(messageId));
        item_values.put("input", new AttributeValue(input));
        item_values.put("combinations", new AttributeValue(allCombinationsJson));
        item_values.put("dateCreated", new AttributeValue(Instant.now().toString()));
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        ddb.putItem(table_name, item_values);

    }

}
            
    
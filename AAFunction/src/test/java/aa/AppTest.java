package aa;

import aa.dao.RequestDao;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AppTest {
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);


    @InjectMocks
    App app;
    @Mock
    RequestDao requestDao;

    @Before
    public void setUp() {
        PutItemResult putItemResult = new PutItemResult();
        when(requestDao.save(any(), any(), any())).thenReturn(putItemResult);
    }

    @Test
    public void successfulResponse() {
        logger.info("Invoke TEST");
        SQSEvent sqsEvent = new SQSEvent();
        List<SQSEvent.SQSMessage> records = new ArrayList<>();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody("{ \n" +
                "   \"input\":[ \n" +
                "      \"A\",\n" +
                "      \"B\",\n" +
                "      \"C\",\n" +
                "      \"D\"\n" +
                "   ]\n" +
                "}\n");
        sqsMessage.setMessageId("1298192");

        records.add(sqsMessage);
        sqsEvent.setRecords(records);

        Context context = new TestContext();


        List<PutItemResult> result = app.handleRequest(sqsEvent, context);
        assertEquals(result.size(), 1);
    }
}

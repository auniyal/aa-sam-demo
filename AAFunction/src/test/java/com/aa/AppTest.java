package com.aa;

import com.aa.dao.RequestDao;
import com.aa.exception.InvalidInputException;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AppTest {


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

    @Test(expected = JsonMappingException.class)
    public void invalidJsonInputShouldThrowException() {
        SQSEvent sqsEvent = new SQSEvent();
        List<SQSEvent.SQSMessage> records = new ArrayList<>();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody("{ \n" +
                "   \"input\":[ \n" +
                "      \"A\",\n" +
                "      \"B\",\n" +
                "      \"C\",\n" +
                "      \"D\"\n" +
                //         "   ]\n" + //      Invalid Json
                "}\n");
        sqsMessage.setMessageId("1298192");
        records.add(sqsMessage);
        sqsEvent.setRecords(records);
        Context context = new TestContext();
        List<PutItemResult> result = app.handleRequest(sqsEvent, context);
        assertEquals(result.size(), 1);
    }


    @Test(expected = JsonParseException.class)
    public void missingInputHeaderShouldThrowException() {
        SQSEvent sqsEvent = new SQSEvent();
        List<SQSEvent.SQSMessage> records = new ArrayList<>();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody("{ \n" +
                "   [ \n" +
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


    @Test(expected = InvalidInputException.class)
    public void noBodyInputShouldThrowException() {
        SQSEvent sqsEvent = new SQSEvent();
        List<SQSEvent.SQSMessage> records = new ArrayList<>();
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody(null);
        sqsMessage.setMessageId("1298192");
        records.add(sqsMessage);
        sqsEvent.setRecords(records);
        Context context = new TestContext();
        List<PutItemResult> result = app.handleRequest(sqsEvent, context);
        assertEquals(result.size(), 1);
    }

}

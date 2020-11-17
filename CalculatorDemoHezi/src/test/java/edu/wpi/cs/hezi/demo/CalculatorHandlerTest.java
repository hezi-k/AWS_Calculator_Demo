package edu.wpi.cs.hezi.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.junit.Assert;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class CalculatorHandlerTest {
	
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	
	
    void testInput(String incoming, String outgoing) throws IOException {
    	CalculatorHandler handler = new CalculatorHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("add"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
	
	
    void testFailInput(String incoming, String outgoing) throws IOException {
    	CalculatorHandler handler = new CalculatorHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("add"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("400", outputNode.get("statusCode").asText());
    }
	
    
    @Test
    public void testCalculatorSimple() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"17\", \"arg2\": \"19\"}";
        String RESULT = "36.0";
        
        try {
        	testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testCalculatorConstant() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"pi\", \"arg2\": \"19\"}";
        String RESULT = "22.1415926";
        
        try {
        	testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testFailInput() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"- GARBAGE -\", \"arg2\": \"10\"}";
        String RESULT = "";
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
	
}

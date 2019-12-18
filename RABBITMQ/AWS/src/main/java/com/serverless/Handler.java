package com.serverless;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	private final static String BODY_KEY = "body";

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);
		String message = (String) input.get(BODY_KEY);
		String result = process(message);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(result)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}

	private String process(String message) {
		return message.replaceAll("task", "<h1>$0</h1>");
	}
}

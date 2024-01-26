package org.siever.processors;

import com.google.gson.JsonObject;
//import com.scio.quantum.harvesters.process.HarvestersPreprocess;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.apache.camel.http.common.HttpOperationFailedException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class ExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        HashMap error = new HashMap();
        String errorMessage;
        String errorName;
        UUID errorID = UUID.randomUUID();

        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
        errorMessage =  exception.getMessage();
        errorName = exception.getClass().toString();

        error.put("errorID", errorID);
        error.put("errorName", errorName);
        error.put("errorMessage", errorMessage);
        System.out.println("Exception Handler says:\n\t" + errorMessage);
        exchange.getIn().setHeader("CamelFileName", errorID);
        exchange.getIn().setBody(error, HashMap.class);

    }

}

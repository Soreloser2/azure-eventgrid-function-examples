package org.example.functions;

import java.util.*;

import com.azure.core.implementation.serializer.jsonwrapper.jacksonwrapper.JacksonDeserializer;
import com.microsoft.azure.eventgrid.models.EventGridEvent;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with EventGrid Trigger.
 */
public class ConsumeEvent {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java
     * 2. curl {your host}/api/HttpTrigger-Java?name=HTTP%20Query
     */
    @FunctionName("EventGrid-Consumer")
    public void run(@EventGridTrigger(name = "data") String data, final ExecutionContext context) {
        context.getLogger().info("Recieved trigger");

        try {
            JacksonDeserializer deserializer = new JacksonDeserializer();
            EventGridEvent event = deserializer.readString(data, EventGridEvent.class);

            if (event.eventType().equalsIgnoreCase("CustomSample.Event")) {
                PublishEvents.CustomEventData customEventData = deserializer.readString(event.toString(),
                        PublishEvents.CustomEventData.class);
                context.getLogger().info("Event Data: " + customEventData.name);
            } else {
                context.getLogger().info("Other data type: " + event.eventType());
            }

        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
        }

        context.getLogger().info(data);
    }
}

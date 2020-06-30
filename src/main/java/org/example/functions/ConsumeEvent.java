// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
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
     * This function responds to eventgrid events by logging the data within the event
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
            context.getLogger().info(e.getMessage());
        }
        
    }
}

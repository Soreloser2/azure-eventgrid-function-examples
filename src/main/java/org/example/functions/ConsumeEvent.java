// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package org.example.functions;

import java.util.*;

import com.azure.core.implementation.serializer.jsonwrapper.jacksonwrapper.JacksonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
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
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());

            EventGridEvent event = mapper.readValue(data, EventGridEvent.class);

            context.getLogger().info("EventGrid Event " + event);

            if (event.eventType().equalsIgnoreCase("CustomSample.Event")) {
                JsonNode storageEventNode = mapper.readTree(data);
                JsonNode storageEventDataNode = storageEventNode.get("data");

                PublishEvents.CustomEventData customEventData = mapper.treeToValue(storageEventDataNode,
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

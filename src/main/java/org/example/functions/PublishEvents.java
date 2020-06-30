// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package org.example.functions;

import com.microsoft.azure.eventgrid.EventGridClient;
import com.microsoft.azure.eventgrid.TopicCredentials;
import com.microsoft.azure.eventgrid.implementation.EventGridClientImpl;
import com.microsoft.azure.eventgrid.models.EventGridEvent;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PublishEvents {

    static class CustomEventData {
        String name;

        public CustomEventData(String name) {
            this.name = name;
        }
    }

    public static final String TOPIC_ENDPOINT = System.getenv("EVENTGRID_TOPIC_ENDPOINT");

    public static final String TOPIC_KEY = System.getenv("EVENTGRID_TOPIC_KEY");

    public static void main(String[] args) {
        try {

            TopicCredentials credentials = new TopicCredentials(TOPIC_KEY);
            EventGridClient egClient = new EventGridClientImpl(credentials);

            List<EventGridEvent> events = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                events.add(new EventGridEvent(UUID.randomUUID().toString(),
                        "Example",
                        new CustomEventData("Test " + i),
                        "CustomSample.Event",
                        DateTime.now(),
                        "2.0"));
            }

            String eventGridEndpoint = String.format("https://%s/", new URI(TOPIC_ENDPOINT).getHost());

            egClient.publishEvents(eventGridEndpoint, events);
            System.out.println("events published");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

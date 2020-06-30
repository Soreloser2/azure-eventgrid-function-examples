# azure function examples
This is example code for publishing and consuming event grid custom events

## Running
This small project has 2 parts. The first part, publishing events, is done by 
running the PublishEvents class locally. In order to do this, set the following 
system environment variables:
```
EVENTGRID_TOPIC_KEY = <topic key created through azure CLI or portal>
EVENTGRID_TOPIC_ENDPOINT = <topic endpoint from azure CLI or portal>
```

The second part, consuming events, is an azure function that uses an eventgrid 
event trigger to log the contents of the message. To use this, create an azure 
function and deploy the project to azure functions. After it has been deployed 
(which may take a while), you need to set a subscription from your eventgrid 
topic with the endpoint as the "EventGrid-Consumer" trigger from the azure
function. After this is all set up, you should be able to see the custom data
logged when PublishEvents is run.
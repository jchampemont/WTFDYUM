### Requirements :

- Java 1.8
- Redis server

### Configuration :

You just need to configure WTFDYUM to use your redis instance and twitter application.


Edit application.properties and change the following values accordingly:

- `wtfdyum.redis.server`
- `wtfdyum.redis.port`
- `wtfdyum.redis.database`
- `wtfdyum.twitter.appId`
- `wtfdyum.twitter.appSecret`
- `wtfdyum.server-base-url`
    
You might want to edit other values too, just refer to the comments in the file.

**You're done !**

### Run :

To run the application, just launch the following command :

    java -jar wtfdyum-XXX.jar
    
The application should be running within seconds, and listening on http port 8080.
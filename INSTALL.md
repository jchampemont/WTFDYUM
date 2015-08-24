### Requirements :

- Java 1.8
- Redis server

### Configuration :

You just need to configure WTFDYUM to use your redis instance.


Edit application.properties and change the following values accordingly:
    - wtfdyum.redis.server
    - wtfdyum.redis.port
    - wtfdyum.redis.database
    
You might want to edit other values too, just refer to the comments in the file.

**You're done !**

### Run :

To run the application, just launch the following command :

    java -jar wtfdyum-X.Y.Z-RC.jar
    
The application should be running within seconds, and listening on http port 8080.
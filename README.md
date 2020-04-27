# Rooms with Lamps

Visit the web page (http://localhost:8082/), the user is able to:
  - create a room
> To press the button "Create room" and to fill the form:
> to enter a room name and to select a country from the list
watch the list of created rooms (including rooms created by other users)
  - watch the list of created rooms (including rooms created by other users). 
 >This list is updating after loading the main page, every 30 seconds and after creating a new room.
  - visit one of the rooms and turn on / off the lamp in the selected room.
  
This application limits access to the room by a user IP address. The app recognizes locate website visitors (visitor's country) using "ipdata.co" REST API  by visitor's IP Address. If a user country matches a room country, the user can enter this room and press on the lamp button. In other cases, access to the room with the lamp will be blocked.


# What You Need!

  - Java 8+
  - Maven 3.2+

# INSTRUCTION!

In this application, the main HTTP port defaults to 8082. we can easily configure the app to use a different port to edit the properties file /src/main/resources/application.properties :
```
server.port=8083
```
Launch an application from the command line from the project folder (where locate "pom.xml" file):
```
mvn spring-boot:run
```
Open the page (if you changed the server port, you have to fix this link)
[http://localhost:8082/](http://localhost:8082/)

Read the REST API documenation:
[http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

Run tests :
```
mvn test
```

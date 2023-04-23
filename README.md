# HotelTrivago
## Description
This is a homework project of a hotel guest registration program. The requirements for the program:
- The program has to be written in pure Java (no Spring Boot, etc.)
- The hotel has 5 single rooms numbered from 1 to 5
- There can be only one guest in a room at any time
- Information of the guests: name and surname
- Actions with the program
  - Guest registration. Name and surname are needed. The program picks a free room and informs the guest. If there are no free rooms at the time of registration, the guest is informed of that.
  - Guest check out 
  - Room availability. Occupied rooms are listed with the guest name and surname
  - Room history and status. Each of the guest name and surname is listed together with the room status (available or occupied)

I've chosen to implement a Web API to communicate with the program. Data is stored in H2 database as it is easy to use for a Java project. Because of the requirement to not use Spring Boot, I've chosen to use HttpServer as the starting point for the application.
## Setup
To run the program, the `main` method from `HotelHttpServer` class in `server` package has to be run. This starts the program and makes `http://localhost:8000/` available as the starting point for the endpoints that are provided below.
## Endpoints
For the program I've decided to create these endpoints:
### Create Reservation
`POST /reservations?name=&surname=`\
Creates a new reservation for a guest with the given name and surname.\
Parameters: name (string), surname (string)
### End Reservation
`PUT /reservations?name=&surname=`\
Ends an existing reservation for a guest with the given name and surname.\
Parameters: name (string), surname (string)
### Create Guest
`POST /guests?name=&surname=`\
Creates a new guest with the given name and surname or returns an existing guest with the same name and surname.\
Parameters: name (string), surname (string)
#### Find Guest
`GET /guests?name=&surname=`\
Retrieves a guest with the given name and surname.\
Parameters: name (string), surname (string)
#### Room Details
`GET /rooms/{{room_number}}/details`\
Retrieves the details of a specific room by its ID.\
Path variable: room_id (string)
### Rooms Occupied
`GET /rooms/occupied`\
Retrieves a list of all occupied rooms.
### Rooms Available
`GET /rooms/available`\
Retrieves a list of all available rooms.

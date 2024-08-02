### How to start a mongo db container with some default database

- Create an **init.js** file with the values to be initialized

```
db = db.getSiblingDB('mydatabase');

// Enum values for roles
const rolesEnum = ["ADMIN", "USER"];

// Create and insert documents into the 'users' collection
db.users.insertMany([
    { username: 'anshul@gmail.com', user_id: 'anshul@gmail.com', email: 'anshul@gmail.com', roles: [rolesEnum[0], rolesEnum[1]]},
    { username: 'akhil@gmail.com', user_id: 'akhil@gmail.com', email: 'akhil@gmail.com', roles: [rolesEnum[0], rolesEnum[1]]},
    { username: 'ruhaan@gmail.com', user_id: 'ruhaan@gmail.com', email: 'ruhaan@gmail.com', roles: [rolesEnum[0], rolesEnum[1]]}
]);

```

- Create docker-compose.yml file with reference to above script
- Give the permissions to the file using command '**chmod 644 init.js**'

```
version: '3'
services:
  mongo:
    image: mongo:latest
    container_name: mongodb
    ports:
      - "27017:27017"
    environment:
      - MONGO_INITDB_ROOT_USERNAME=admin
      - MONGO_INITDB_ROOT_PASSWORD=adminpassword
    volumes:
      - mongo-data:/data/db
      - ./init.js:/docker-entrypoint-initdb.d/init.js:ro
    networks:
      - my-network

  mongo-express:
    image: mongo-express:latest
    container_name: mongo-express
    ports:
      - "8081:8081"
    environment:
      - ME_CONFIG_MONGODB_SERVER=mongo
      - ME_CONFIG_MONGODB_PORT=27017
      - ME_CONFIG_MONGODB_ADMINUSERNAME=admin
      - ME_CONFIG_MONGODB_ADMINPASSWORD=adminpassword
      - ME_CONFIG_MONGODB_AUTH_DATABASE=admin
      - ME_CONFIG_BASICAUTH_USERNAME=webadmin
      - ME_CONFIG_BASICAUTH_PASSWORD=webpassword
    networks:
      - my-network

volumes:
  mongo-data:

networks:
  my-network:
    driver: bridge
```
- Stop docker container using command '**docker-compose down -v**'
- Stop docker container using command '**docker-compose up -d --force-recreate**'
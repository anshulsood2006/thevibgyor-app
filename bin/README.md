### POC on implementing Spring Security 6 using Spring Boot 3

- This POC has been implemented using Spring Security 6 and Spring Boot 3
- The user roles are defined in enum class Role.java
- This POC does not user any database layer. The data has been mocked in class UserRepositoryImpl.java
- The end point /auth/get-token can be called only using basic auth credentials in header.
- The end point /auth/get-token can generate jwt token for both User and Admin Roles.
- The time to live for the token can be sent in request body as  
```
{
    "ttl": 3600
}
```
- All the other endpoints can be called only using JWT token in header.
- All the admin endpoints e.g. /admin/page can be called using JWT token generated with admin credentials only
  viz.anshul/sood
- All the user endpoints e.g. /user/page can be called using JWT token generated with both user credentials
  viz.akhil/sood and admin credentials viz.anshul/sood

### Prerequisite Softwares
- gitbash
- maven
- Java 11 or above
- curl / postman

### Steps to run the POC on local

1. Download the project from github.
2. Run command ```mvn clean install```
3. Run command ```mvn spring-boot:run```
4. Admin JWT tokens can be created using curl command
```
curl --location 'http://localhost:8080/auth/get-token' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YW5zaHVsOnNvb2Q=' \
--data '{
    "ttl": 3600
}'

or 

curl --location 'http://localhost:8080/auth/get-token' \
--header 'Content-Type: application/json' \
--user 'anshul:sood' \
--data '{
    "ttl": 3600
}'

```
5. Admin API can be called using curl command
```
curl --location --request POST 'http://localhost:8080/admin/page' \
--header 'Authorization: Bearer {ADMIN JWT TOKEN}' \
--data ''
```
6. User JWT tokens can be created using curl command
```
curl --location 'http://localhost:8080/auth/get-token' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWtoaWw6c29vZA==' \
--data '{
    "ttl": 3600
}'

or

curl --location 'http://localhost:8080/auth/get-token' \
--header 'Content-Type: application/json' \
--user 'akhil:sood' \
--data '{
    "ttl": 3600
}'
```
7. User API can be called using curl command
```
curl --location --request POST 'http://localhost:8080/user/page' \
--header 'Authorization: Bearer {USER JWT TOKEN}' \
--data ''
```

Feel free to drop a mail to [anshulsood2006@gmail.com](mailto:anshulsood2006@gmail.com) in case of any queries or
suggestions


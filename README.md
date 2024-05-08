
# rest-assured-with-basic-web-service

This project includes basic Users webservice (created using **Spring Boot**  for testing purpose only) together with examples of unit, integration,functional tests.

**Webservice** module:
 - has  **Basic Authentication** (login/password);
 - restrict access to public and admin resources  depends on user role (USER OR ADMIN);
 - includes basic **unit and integration** tests, using **Mockito** and **Junit5**
 - contains all needed flyway scripts to auto setup schema for PostgreSQL DB;

Example, POST {base-url}/api/admin/users 
Body:    
```json
{ 
  "userName" : "userTestRole5613",
  "password": "superSecret",
  "isActive": true
}
```
response : 
```json
{
    "id": 42,
    "userName": "userTestRole56",
    "isActive": true,
    "registeredTime": "2024-05-06T07:47:31.263968",
    "role": "USER"
}
```

**FunctionalTesting** module contains tests that have been written on Junit5 + REST Assured, Spring boot (for DB interaction).
For running tests, you need to set correct value in application.yml. 



The tests result will have follow view : 


![image](https://github.com/mbilan/rest-assured-with-basic-web-service/assets/35302729/94ac6632-3a0a-4f83-92bd-a0a0f0768462)



# Schoolproject

Small project for teaching purposes.

* Wildfly
* JEE 8 
* Java 11
* Git
* Maven
* MySQL

## Wildfly configuration

Install any Wildfly release you want. I use 18.

Add a user, and place the school.cli script under the bin folder.<br>
Create database school. The script will need a mysql connector under `C:\Users`
to work. 

The script is predefined for `mysql.connector-java-8.0.18.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.

## Deployment with Maven

Start Wildfly from bin folder with command: `standalone.bat -c standalone-full.xml` <br>
Maven goals: `wildfly:undeploy clean:clean wildfly:deploy`

## Api

Base url: localhost:8080/school/student

| Path | Method | Params/Json body | Responses |
| ---  | --- | --- | --- |
| / | GET |  | 200 - List of students <br> 404 - Not found |
| /find | GET | forename, lastname | 200 - Student model <br>  404 - Not found <br> 406 - Empty field  <br> 400 - Bad request |
| /add | POST | example Json body <br> { "forename":"hans", <br> "lastname":"hansen", <br> "email":"hansen@test.com" } | 200 - Student model <br> 409 - Duplicate email conflict <br> 406 - Empty field <br> 400 - Bad request |
| {email} | DELETE | | 406 - Empty field <br> 404 - Not found <br> 400 - Bad request |
| / | PUT | forename, lastname, email | 406 - Empty field <br> 404 - Not found <br> 400 - Bad request |
| / | PATCH | example Json body <br> { "forename":"hans", <br> "lastname":"hansen", <br> "email":"hansen@test.com" } <br><br> **lastname not mandatory since <br> it wont be changed anyways** | 406 - Empty field <br> 404 - Not found <br> 400 - Bad Request |


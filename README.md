This application manages dependecies and build by maven. 
After cloning this repo, you can navigate to project directory where pom.xml exists. 

Then execute 
**mvn clean compile install

It will fetch dependencies that you do not have in your local repo, 
build the project, creates .jar project package and also run the unit tests under test folder

How to start the application 
When you finish **mvn clean compile install, the target folder be created inside project folder
You can navigate into it, and you should see CustomerLoanService-1.0-SNAPSHOT jar package.

You can start the application by
**java -jar CustomerLoanService-1.0-SNAPSHOT.jar
**

it will start the application on 8080 port.

You can refer to user manuel document about how to call endpoints.
Also there will be a postman collection for the endpoints. 

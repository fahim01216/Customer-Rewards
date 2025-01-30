**Overview**

The Customer Reward Service is a Spring Boot application designed to calculate customer reward points based on their transaction history. It supports filtering transactions by date range and provides a REST API to fetch reward details for a given customer.


**Features**

● Calculate reward points for transactions based on the following rules:

		2 points for every dollar spent over $100.
	
		1 point for every dollar spent over $50 (up to $100).
● Filter transactions by date range.

● Exception handling for invalid inputs and non-existent customers.

● Modular and extensible architecture.


**Technologies Used**

● Backend: Java, Spring Boot, Hibernate

● Database: H2 (in-memory) / MySQL (configurable)

● Testing: JUnit, Mockito

● Build Tool: Maven

● Logging: SLF4J with Logback


**Project Structure**

src/main/java

	└── com.assignment.reward

		├── controller        // REST API controllers

			── RewardController

		├── dto               // Data Transfer Objects (DTOs)
	
		 	── ErrorResponse
		
		 	── MonthwiseRewardResponse
		
		 	── RewardResponse
		
		 	── TransactionResponse

		├── entity            // JPA entities
	
		 	── Customer
		
		 	── MonthwiseReward
		
		 	── Transaction

		├── exception         // Custom exceptions and handlers
	
			── CustomerNotFoundException
	 
			── GlobalExceptionHandler
	 
			── InvalidInputException

		│── images
	
			── rewards-api.postman_collection.json

		├── repository        // JPA repositories
	
			── CustomerRepository
	 
			── TransactionRepository
		
		├── service           // Business logic layer
	
			── calculatePoints
	 
			── RewardPointsCalculator
	 
			── RewardService
	 
src/test/java

	└── com.assignment.reward

		├── controller        // Controller layer test cases
	
   		── RewardControllerTest

		└── service           // Service layer test cases
	
   		── RewardServiceTest

	
**Prerequisites**

● Java 17+

● Maven 3.6+

● MySQL (if using a persistent database)


**Setup Instructions**

	_Clone the Repository:_

		git clone https://github.com/fahim01216/Customer-Rewards.git cd customer-reward-service

	_Update Configuration:_

		spring.datasource.url=jdbc:mysql://localhost:3306/reward_db 
		
		spring.datasource.username= root
		
		spring.datasource.password= root
		
		spring.jpa.hibernate.ddl-auto=update

	_Build the Project:_

		mvn clean install
		
		Run the Application:
		
		mvn spring-boot:run


**API Endpoints**

	_Reward Controller_

	HTTP Method Endpoint Description

	GET /api/rewards/customer/{id} Fetch reward details for a specific customer.

	_Query Parameters:_

	startDate (optional): Start date for filtering transactions.
	
	endDate (optional): End date for filtering transactions.


**Sample Request**

```
GET /api/rewards/customer/1
Content-Type: application/json

{
  "customerId": 1,
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

**Sample Response**

```
{
  "customerId": 1,
  "customerName": "John Doe",
  "transactions": [
    {
      "transactionId": 101,
      "amount": 120,
      "date": "2024-01-10",
      "points": 90
    },
    {
      "transactionId": 102,
      "amount": 80,
      "date": "2024-01-15",
      "points": 30
    }
  ],
  "monthwiseRewards": [
    {
      "month": "2024-01",
      "rewardPoints": 120
    }
  ],
  "totalPoints": 120
}
```

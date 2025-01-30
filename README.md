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
    
    ├── repository        // JPA repositories
       ── CustomerRepository
       ── TransactionRepository
    
    ├── service           // Business logic layer
       ── calculatePoints
       ── RewardPointsCalculator
       ── RewardService
    
    └── util              // Utility classes (e.g., PointsCalculator)
       ── ValidationUtil
    


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

1. Clone the Repository:

    git clone https://github.com/fahim01216/Customer-Rewards.git
    cd customer-reward-service

2. Update Configuration:

    spring.datasource.url=jdbc:mysql://localhost:3306/reward_db
    spring.datasource.username=<your-username>
    spring.datasource.password=<your-password>
    spring.jpa.hibernate.ddl-auto=update

3. Build the Project:
   
    mvn clean install

4. Run the Application:
   
    mvn spring-boot:run



**API Endpoints**

**Reward Controller**

HTTP Method	Endpoint	         Description

GET	/api/rewards/customer/{id}	Fetch reward details for a specific customer.


**Query Parameters:**

startDate (optional): Start date for filtering transactions.

endDate (optional): End date for filtering transactions.


**Sample Request**

GET /api/rewards/customer/1?startDate=2023-10-01&endDate=2023-11-31

**Sample Response**

{
  "id": 1,
  "customerName": "Md Fahim",
  "transactions": [
    {
      "id": 101,
      "amount": 120.0,
      "date": "2024-10-15",
      "pointsEarned": 90
    },
    {
      "id": 102,
      "amount": 80.0,
      "date": "2024-11-10",
      "pointsEarned": 30
    }
  ],
  "monthwiseRewards": [
    {
      "month": "2024-10",
      "rewardPoints": 90
    },
    {
      "month": "2024-11",
      "rewardPoints": 30
    }
  ]
}


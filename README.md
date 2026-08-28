# MoonWalk Order ETA

The MoonWalk Order ETA System is a Spring Boot application designed to calculate and manage the Estimated Time of Arrival (ETA) for restaurant orders. The system implements various dynamic scheduling and estimation algorithms to provide accurate food preparation times based on real-time kitchen workloads.

## Features

- **Order Management:** Create and track customer orders consisting of various dishes.
- **Kitchen State Tracking:** Monitor live kitchen states, including active chefs, cooking stations, and their current workloads.
- **Dynamic ETA Calculation Engine:** Calculate order ETAs using multiple interchangeable strategies:
  - **First Come First Serve (FCFS):** Processes orders strictly based on their arrival time.
  - **Shortest Job First (SJF):** Prioritizes orders with the shortest preparation time.
  - **Priority Strategy:** Considers order priority.
  - **Resource Aware Strategy:** Considers available chefs, cooking stations, and current workloads for advanced estimations.
- **Database Versioning:** Utilizes Flyway for robust and version-controlled database migrations.
- **API Documentation:** Integrated Swagger UI (OpenAPI 3) for easy API exploration and testing.
- **QR Code Generation:** Built-in support for generating QR codes (via ZXing).

## Technology Stack

- **Java 17**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **MySQL Database**
- **Flyway** (Database Migrations)
- **Lombok** (Boilerplate reduction)
- **Springdoc OpenAPI** (Swagger UI)
- **ZXing** (QR Code processing)

## Architecture

The application is structured using a standard n-tier architecture:

1.  **Controllers (REST API):** Handles incoming HTTP requests and serves responses.
2.  **Services:** Contains business logic (OrderService, KitchenService, CountdownService).
3.  **ETA Engine:** A dedicated module containing different algorithm strategies for ETA calculations.
4.  **Data Access (JPA):** Interfaces with the MySQL database using Hibernate and Spring Data JPA.

For a detailed visual overview, please refer to the [Architecture Diagram](docs/architecture-diagram.md) and [ER Diagram](docs/er-diagram.md).

## Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL Server

### Database Setup

1. Create a MySQL database for the application.
2. Update the database connection properties in `src/main/resources/application.properties` (or `application.yml`):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Running the Application

You can run the application directly using the Maven wrapper:

```bash
./mvnw spring-boot:run
```

Once the application starts, Flyway will automatically execute any pending database migrations.

### API Documentation

After starting the application, you can access the Swagger UI to view and interact with the API endpoints at:
`http://localhost:8080/swagger-ui.html`

## API Endpoints

Here is a summary of the available REST API endpoints:

### Restaurants
- `POST /api/restaurants` - Create a new restaurant
- `GET /api/restaurants` - Get all restaurants
- `GET /api/restaurants/{id}` - Get a specific restaurant
- `GET /api/restaurants/{id}/order-link` - Get the ordering link for a restaurant
- `GET /api/restaurants/{id}/qr-code` - Generate a QR code for ordering

### Kitchen & Menu
- `POST /api/restaurants/{restaurantId}/chefs` - Add a chef to a restaurant
- `GET /api/restaurants/{restaurantId}/chefs` - Get all chefs for a restaurant
- `PATCH /api/chefs/{id}/workload` - Update a chef's workload
- `POST /api/restaurants/{restaurantId}/stations` - Add a cooking station
- `GET /api/restaurants/{restaurantId}/stations` - Get all stations for a restaurant
- `POST /api/restaurants/{restaurantId}/dishes` - Add a dish to the menu
- `GET /api/restaurants/{restaurantId}/dishes` - Get all dishes for a restaurant

### Orders & ETA
- `POST /api/restaurants/{restaurantId}/orders` - Create a new order
- `GET /api/restaurants/{restaurantId}/orders` - Get all orders for a restaurant
- `GET /api/orders/{orderId}` - Get details of a specific order
- `PUT /api/orders/{orderId}` - Update an order
- `PUT /api/orders/{orderId}/status` - Update the status of an order
- `GET /api/orders/{orderId}/countdown` - Get the live ETA countdown for an order
- `GET /api/orders/{orderId}/eta-history` - Get the ETA calculation history for an order

### Testing & Data Seeding
- `POST /api/seed/restaurant` - Seed dummy data for testing

## Documentation

- [Entity Relationship Diagram](docs/er-diagram.md)
- [Architecture Diagram](docs/architecture-diagram.md)

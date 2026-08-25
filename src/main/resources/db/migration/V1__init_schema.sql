CREATE TABLE restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    strategy VARCHAR(50) NOT NULL,
    created_at DATETIME(6)
);

CREATE TABLE chefs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_workload INT NOT NULL,
    CONSTRAINT fk_chef_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE cooking_stations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_workload INT NOT NULL,
    CONSTRAINT fk_station_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    preparation_time INT NOT NULL,
    required_station VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT fk_dish_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME(6),
    estimated_time_seconds INT,
    estimated_at DATETIME(6),
    started_at DATETIME(6),
    completed_at DATETIME(6),
    algorithm_used VARCHAR(50)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    preparation_time INT NOT NULL,
    CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE eta_executions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    timestamp DATETIME(6),
    estimated_time_seconds INT,
    algorithm_used VARCHAR(50),
    pending_order_count INT NOT NULL,
    available_chef_count INT NOT NULL,
    busy_station_count INT NOT NULL,
    status VARCHAR(50) NOT NULL
);

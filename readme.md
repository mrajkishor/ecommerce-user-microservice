
# User Microservice

This microservice handles user registration, authentication, and profile management for the e-commerce platform.

## Technologies
- Java 17
- Spring Boot
- Docker
- Kubernetes

## Running Locally

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Build the Docker image:
   ```bash
   docker build -t user-service:latest .
   ```

3. Deploy the microservice to Kubernetes:
   ```bash
   kubectl apply -f user-service.yaml
   ```

## API Endpoints
- `POST /users/register`: Register a new user
- `POST /users/login`: Authenticate a user

## License
This project is licensed under the MIT License.


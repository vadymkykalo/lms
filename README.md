# Pet Project (lms)

# LMS Application

## Description
LMS Application using Spring Boot, PostgreSQL, and Redis. The entire application infrastructure is containerized using Docker.

## Getting Started

### Prerequisites
To work with this project, you will need:
- Docker
- Docker Compose
- OpenSSL (for generating key pairs)

### Project Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd lms

2. **Start Docker containers**
   Launch all containers using Docker Compose:
   ```bash
   docker network create lms_network
   docker-compose up -d
   
3. **Application Configuration**
The application is configured to interact with services running inside containers. Ensure that the application's configuration files 
are set to use the container names as hostnames when running in containers. When running locally, you should use localhost.

PostgreSQL is available on port 5444.
Redis is available as a service within the network, typically not exposed to the host directly.

### Generating New Keys
For security reasons, it's important to generate your own private-public key pair if you are setting up the application for production use.
Run the following commands to generate a new key pair:

Generate a private key:
```bash
openssl genrsa -out keypair.pem 2048
```

Extract the public key:

```bash
openssl rsa -in keypair.pem -pubout -out public.pem
```

Convert private key to PKCS#8 format:
```bash
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```

### Additional Information
Ensure to replace your keys in the relevant sections of the application to avoid security risks associated with
using the sample keys provided in the repository.
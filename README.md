# Note

1. Example of CRUD API using spring-boot framework basic setup.
2. This example use in-memory H2 database for simplicity
3. TLS is not configure
4. API spec is available at http://localhost:8080/swagger-ui/index.html
5. Register your user before using any of /notes/\*\* API

```
curl -X 'POST' \
  'http://localhost:8080/users/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "user@email.com",
  "name": "my name",
  "password": "password"
}'
```

6. Get JWT token. Token will be return as access_token after successful login

```
curl -X 'POST' \
  'http://localhost:8080/users/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "user@email.com",
  "password": "password"
}' | jq -r '.access_token'
```

7. To use the token.

```
export JWT_TOKEN=<token>
curl -X 'POST' \
  'http://localhost:8080/notes' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer '$JWT_TOKEN \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "my note",
  "content": "my note contents"
}'
```

# Build and run jar locally

```
mvn clean package
java -jar target/notes-0.0.1-SNAPSHOT.jar
```

# Build docker image locally

1. Using spring boot image builder

```
mvn clean spring-boot:build-image
docker run -p 8080:8080 -t notes:0.0.1-SNAPSHOT
```

2. Simple docker packaging

```
docker build -t notes:0.0.1-SNAPSHOT .
docker run -p 8080:8080 -t notes:0.0.1-SNAPSHOT
```

3. Multi-stage docker build and packaging. Use the latest docker version with BuildKit (DOCKER_BUILDKIT=1) enabled to cache the maven repo and speed up the build next time.

```
DOCKER_BUILDKIT=1 docker build -t notes:0.0.1-SNAPSHOT -f Dockerfile.build .
docker run -p 8080:8080 -t notes:0.0.1-SNAPSHOT
```

# Sonar scanning

```
docker-compose -f infra/sonarqube-docker-compose.yml up
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=admin
```

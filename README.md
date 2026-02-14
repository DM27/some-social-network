# Some social network
Study project

## Getting Started
Go to the project directory.
Run command:
```shell
docker compose -f ./docker/docker-compose.yaml up -d
```
Api will be available at http://localhost:8080/api/v2

## Endpoints
Specification : https://github.com/OtusTeam/highload/blob/master/homework/openapi.json
- /login 
- /user/register
- /user/get/{id}
- /user/search
- /friend/add 
- /friend/delete
- /post/create
- /post/update
- /post/delete
- /post/get
- /dialog/{user_id}/send
- /dialog/{user_id}/list
 
## List of dependencies
- Spring WEB
- Spring Security
- Mybatis
- Liquibase
- PostgreSQL
- Redis
- RabbitMQ
- Lombok
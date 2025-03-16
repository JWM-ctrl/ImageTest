# ImageTest

Как поднять базу: docker run --name postgres-container -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:latest

Как запустить проект: mvn clean install spring-boot:run

Тип авторизации выбран Base Auth для ускорения выдачи проекта на проверку


# todo-api-gateway

# gateway api
web -> gateway -> auth
                -> users
                -> tod_o
                -> tod_o_graphql

Базовый путь gateway сервиса
- http://localhost:8080/todo-web-api

Всегда доступные url
- http://localhost:8080/todo-web-api/auth-api/login
- http://localhost:8080/todo-web-api/users-api/users/register
- http://localhost:8080/todo-web-api/auth-api/refreshToken

Url, для доступа к которым нужен токен, который будет проверяться
- http://localhost:8080/todo-web-api/notes-api/**
- http://localhost:8080/todo-web-api/auth-api/**
- http://localhost:8080/todo-web-api/graphql/**

Проверка токена на валидность
- http://localhost:8887/auth-api/validateToken


# Запуск приложения

## Запуск всех тестов
    1) Запустить тесты:
      'clean install -Dskip-unit-test=false -Dskip-integration-test=false'

## Запуск юнит тестов
    1) Запустить тесты:
      'clean install -Dskip-unit-test=false'

## Запуск интеграционных тестов
    1) Запустить тесты:
      'clean install -Dskip-integration-test=false'

## Запуск приложения для разработки
    1) Запустить spring приложение через main метод класса
      ru.lappi.gateway.GatewayApplication
    2) Приложение будет принимать запросы по url:
      http://localhost:8080/todo-web-api

## Запуск в докере бд с приложением
    1) Собираем проект:
      'mvn clean install'
    2) Создаём docker image из корня проекта (image с 1 jar файлом):
      'docker build -t gateway-app:v1 .'
    3) Запустить docker приложение
    4) Приложение будет принимать запросы по url:
      http://localhost:8080/todo-web-api

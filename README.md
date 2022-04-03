# todo-api-gateway

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
    2) Запустить spring приложение через main метод класса
      ru.lappi.gateway.GatewayApplication
    3) Приложение будет принимать запросы по url:
      http://localhost:8080/todo-web-api

## Запуск в докере бд с приложением
    1) Собираем проект:
      'mvn clean install'
    2) Создаём docker image из корня проекта (image с 1 jar файлом):
      'docker build -t gateway-app:v1 .'
    3) Запустить docker приложение
    4) Приложение будет принимать запросы по url:
      http://localhost:8080/todo-web-api

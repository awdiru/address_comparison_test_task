# Address Comparison Service

Сервис для сравнения геокоординат, полученных от Yandex Maps API и Dadata API.

## Технологический стек
- Java 17
- Spring Boot 3.5.0
- MySQL 8.0
- Docker
- Docker Compose

## Функциональность
1. Принимает текстовый адрес через REST API
2. Получает координаты через Yandex Maps API и Dadata API
3. Рассчитывает расстояние между координатами
4. Сохраняет результаты в базу данных
5. Возвращает сравнение результатов

## Требования
- Docker 20.10+
- Docker Compose 1.29+
- Maven 3.8+

## Быстрый запуск

### 1. Клонирование репозитория
```bash
  git clone https://github.com/awdiru/address_comparison_test_task.git
  cd address_comparison_test_task
```
### 2. Настройка переменных окружения
Заполните файл .env своими API ключами:
```evn
# Обязательные ключи
YANDEX_API_KEY=ваш_яндекс_ключ
DADATA_API_KEY=ваш_dadata_ключ

# Опционально (если используется Dadata с секретным ключом)
DADATA_SECRET_KEY=ваш_секретный_ключ
```
### 3. Сборка проекта
```bash
  mvn clean package
```
### 4. Запуск системы
```bash
  docker-compose up --build -d
```
### 5. Проверка работы через Postman:
1. Создать новый запрос
2. Ввести в поле url http://localhost:8080/api/address/compare
3. Выбрать метод POST
4. В тело запроса ввести:
```json
{
  "address":"Москва, Красная площадь"
}
```
Пример успешного вывода:
```json
{
  "yandexLat": 55.753544,
  "yandexLon": 37.621202,
  "dadataLat": 55.7535859,
  "dadataLon": 37.6210462,
  "distance": 10.805301070562633
}
```
Поле distance показывает расстояние между координатами от сервиса \
Yandex Maps API и Dadata API в метрах
## Структура проекта
```txt
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ru
│   │   │       └── avdonin
│   │   │           └── address_comparison
│   │   │               ├── controller
│   │   │               ├── config
│   │   │               ├── exception
│   │   │               ├── model
│   │   │               │   ├── entity
│   │   │               │   └── dto
│   │   │               ├── service
│   │   │               ├── repository
│   │   │               └── AddressComparisonApplication.java
│   │   └── resources
│   │       └── application.yml
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```
## Управление сервисом
### Запуск
```bash
  docker-compose up -d
```
### Остановка
```bash
  docker-compose down
```
### Просмотр логов
```bash
  docker-compose logs -f app
```
### Пересборка
```bash
  docker-compose down
  mvn clean package
  docker-compose build
  docker-compose up -d
```
## Доступ к базе данных
- Хост: localhost
- Порт: 3306
- База данных: address_db
- Пользователь: root
- Пароль: password
### Для подключения используйте:
```bash
  mysql -h localhost -P 3306 -u root -ppassword address_db
```
## API Endpoints
### Основной метод
```
POST /api/address/compare  - Сравнение координат адреса
```
### Тело запроса:
```json
{
    "address":"Moscow, Red Square"
}
```

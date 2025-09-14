# API Usage Guide

## Запуск приложения

1. Убедитесь, что у вас установлен Java 17+
2. Запустите приложение: `mvn spring-boot:run`
3. Приложение будет доступно по адресу: `http://localhost:8080`

## Доступные эндпоинты

### Аутентификация

#### Регистрация
```
POST /api/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
}
```

#### Логин
```
POST /api/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "password123"
}
```

#### Получить текущего пользователя
```
GET /api/auth/me
Authorization: Bearer <your-jwt-token>
```

### Тестовые эндпоинты

#### Публичный эндпоинт
```
GET /api/test/public
```

#### Эндпоинт для пользователей
```
GET /api/test/user
Authorization: Bearer <your-jwt-token>
```

#### Эндпоинт для администраторов
```
GET /api/test/admin
Authorization: Bearer <your-jwt-token>
```

## База данных

- Используется PostgreSQL база данных
- База данных: `hr_space`
- Host: `localhost:5432`
- Username: `postgres`
- Password: `postgres`

### Настройка PostgreSQL

1. Убедитесь, что PostgreSQL установлен и запущен
2. Создайте базу данных:
   ```sql
   CREATE DATABASE hr_space;
   ```
3. Приложение автоматически создаст необходимые таблицы при запуске

## Примеры использования с curl

### Регистрация
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Логин
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Доступ к защищенному эндпоинту
```bash
curl -X GET http://localhost:8080/api/test/user \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Структура ответов

### Успешный ответ
```json
{
    "success": true,
    "message": "Operation successful",
    "data": { ... }
}
```

### Ответ с ошибкой
```json
{
    "success": false,
    "message": "Error message"
}
```

### Ответ аутентификации
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "type": "Bearer",
        "username": "testuser",
        "email": "test@example.com",
        "role": "USER"
    }
}
```

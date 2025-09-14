# Email Verification System Guide

## Обзор

Реализована двухэтапная система регистрации с подтверждением email через код верификации.

## Процесс регистрации

### Этап 1: Отправка кода на email
1. Пользователь отправляет данные регистрации
2. Система генерирует 6-значный код
3. Код отправляется на email пользователя
4. Пользователь сохраняется как "неподтвержденный"

### Этап 2: Подтверждение кода
1. Пользователь вводит полученный код
2. Система проверяет код и время его действия
3. При успешной проверке email подтверждается
4. Пользователь получает JWT токен

## Новые поля в User entity

```java
@Column(name = "is_email_verified")
private Boolean isEmailVerified = false;

@Column(name = "verification_code")
private String verificationCode;

@Column(name = "code_expiration_time")
private LocalDateTime codeExpirationTime;
```

## API Эндпоинты

### 1. Регистрация (отправка кода)
```
POST /api/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
}
```

**Ответ:**
```json
{
    "success": true,
    "message": "Verification code sent to your email"
}
```

### 2. Подтверждение email
```
POST /api/auth/verify-email
Content-Type: application/json

{
    "email": "test@example.com",
    "code": "123456"
}
```

**Ответ:**
```json
{
    "success": true,
    "message": "Email verified successfully",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "type": "Bearer",
        "username": "testuser",
        "email": "test@example.com",
        "role": "USER"
    }
}
```

### 3. Повторная отправка кода
```
POST /api/auth/resend-code
Content-Type: application/json

{
    "email": "test@example.com"
}
```

**Ответ:**
```json
{
    "success": true,
    "message": "Verification code resent to your email"
}
```

### 4. Логин (только для подтвержденных пользователей)
```
POST /api/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "password123"
}
```

## Новые исключения

### EmailNotVerifiedException (403)
```json
{
    "success": false,
    "message": "Email is not verified. Please verify your email first.",
    "errorCode": "EMAIL_NOT_VERIFIED",
    "status": 403
}
```

### InvalidVerificationCodeException (400)
```json
{
    "success": false,
    "message": "Invalid or expired verification code",
    "errorCode": "INVALID_VERIFICATION_CODE",
    "status": 400
}
```

## Конфигурация Email

### application.yml
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME:your-email@gmail.com}
    password: ${MAIL_PASSWORD:your-app-password}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

app:
  name: Project
  mail:
    from: ${MAIL_FROM:noreply@example.com}
```

### Переменные окружения
```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
export MAIL_FROM=noreply@example.com
```

## Особенности системы

### Код верификации
- **Формат**: 6 цифр (например: 123456)
- **Время жизни**: 15 минут
- **Генерация**: криптографически стойкий SecureRandom

### Безопасность
- Неподтвержденные пользователи не могут логиниться
- Коды имеют ограниченное время жизни
- Возможность повторной отправки кода

### Email шаблон
```
Hello!

Thank you for registering with Project.

Your verification code is: 123456

This code will expire in 15 minutes.

If you didn't request this code, please ignore this email.

Best regards,
Project Team
```

## Примеры использования

### 1. Полная регистрация
```bash
# Шаг 1: Регистрация
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
  }'

# Шаг 2: Подтверждение email (код придет на email)
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "code": "123456"
  }'

# Шаг 3: Логин
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123"
  }'
```

### 2. Повторная отправка кода
```bash
curl -X POST http://localhost:8080/api/auth/resend-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com"
  }'
```

## Настройка для разработки

### Gmail SMTP
1. Включите двухфакторную аутентификацию
2. Создайте пароль приложения
3. Используйте пароль приложения в `MAIL_PASSWORD`

### Тестирование без реальной отправки
Для разработки можно использовать:
- **MailHog** - локальный SMTP сервер
- **Mailtrap** - сервис для тестирования email
- **FakeSMTP** - простой SMTP сервер

## Миграции базы данных

Добавлены новые поля в таблицу `users`:
- `is_email_verified` - статус подтверждения email
- `verification_code` - код верификации
- `code_expiration_time` - время истечения кода

Миграция выполняется автоматически при запуске приложения.



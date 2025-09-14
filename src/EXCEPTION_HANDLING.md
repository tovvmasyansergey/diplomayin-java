# Exception Handling Guide

## Обзор

В проекте реализована система красивой обработки исключений с использованием глобального обработчика и кастомных исключений.

## Структура исключений

```
src/main/java/com/example/project/exception/
├── AuthException.java                    # Базовое исключение для аутентификации
├── UserNotFoundException.java            # Пользователь не найден
├── UserAlreadyExistsException.java       # Пользователь уже существует
├── InvalidCredentialsException.java      # Неверные учетные данные
├── ValidationException.java              # Ошибки валидации
└── GlobalExceptionHandler.java           # Глобальный обработчик
```

## Кастомные исключения

### 1. AuthException
Базовое исключение для всех ошибок аутентификации:
```java
public class AuthException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
}
```

### 2. UserNotFoundException
Выбрасывается когда пользователь не найден:
```java
// Примеры использования:
throw new UserNotFoundException("User not found with username: john");
throw new UserNotFoundException("username", "john");
```

### 3. UserAlreadyExistsException
Выбрасывается при попытке создать пользователя с существующими данными:
```java
// Примеры использования:
throw new UserAlreadyExistsException("Username is already taken");
throw new UserAlreadyExistsException("username", "john");
```

### 4. InvalidCredentialsException
Выбрасывается при неверных учетных данных:
```java
// Примеры использования:
throw new InvalidCredentialsException();
throw new InvalidCredentialsException("Account is locked");
```

### 5. ValidationException
Выбрасывается при ошибках валидации:
```java
// Примеры использования:
throw new ValidationException("Field is required");
throw new ValidationException(List.of("Field1 is required", "Field2 is invalid"));
```

## Глобальный обработчик исключений

`GlobalExceptionHandler` обрабатывает все исключения и возвращает структурированные ответы:

### Обрабатываемые исключения:
- `AuthException` и его наследники
- `MethodArgumentNotValidException` (валидация @Valid)
- `ConstraintViolationException` (валидация @Validated)
- `AuthenticationException` (Spring Security)
- `AccessDeniedException` (Spring Security)
- `Exception` (общие исключения)

## Структура ответа с ошибкой

```json
{
    "success": false,
    "message": "User not found with username: john",
    "errorCode": "USER_NOT_FOUND",
    "status": 404,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/auth/login",
    "errors": ["username: Username is required", "email: Email should be valid"]
}
```

## Примеры ответов

### 1. Пользователь не найден (404)
```json
{
    "success": false,
    "message": "User not found with username: john",
    "errorCode": "USER_NOT_FOUND",
    "status": 404,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/auth/login"
}
```

### 2. Пользователь уже существует (409)
```json
{
    "success": false,
    "message": "User with username 'john' already exists",
    "errorCode": "USER_ALREADY_EXISTS",
    "status": 409,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/auth/register"
}
```

### 3. Неверные учетные данные (401)
```json
{
    "success": false,
    "message": "Invalid username or password",
    "errorCode": "INVALID_CREDENTIALS",
    "status": 401,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/auth/login"
}
```

### 4. Ошибки валидации (400)
```json
{
    "success": false,
    "message": "Validation failed",
    "errorCode": "VALIDATION_ERROR",
    "status": 400,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/auth/register",
    "errors": [
        "username: Username must be between 3 and 50 characters",
        "email: Email should be valid",
        "password: Password must be at least 6 characters"
    ]
}
```

### 5. Доступ запрещен (403)
```json
{
    "success": false,
    "message": "Access denied",
    "errorCode": "ACCESS_DENIED",
    "status": 403,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/test/admin"
}
```

## Коды ошибок

| Код ошибки | HTTP Status | Описание |
|------------|-------------|----------|
| `USER_NOT_FOUND` | 404 | Пользователь не найден |
| `USER_ALREADY_EXISTS` | 409 | Пользователь уже существует |
| `INVALID_CREDENTIALS` | 401 | Неверные учетные данные |
| `VALIDATION_ERROR` | 400 | Ошибки валидации |
| `CONSTRAINT_VIOLATION` | 400 | Нарушение ограничений |
| `AUTHENTICATION_FAILED` | 401 | Ошибка аутентификации |
| `ACCESS_DENIED` | 403 | Доступ запрещен |
| `INTERNAL_SERVER_ERROR` | 500 | Внутренняя ошибка сервера |

## Преимущества новой системы

1. **Единообразие** - все ошибки имеют одинаковую структуру
2. **Информативность** - детальная информация об ошибке
3. **Логирование** - автоматическое логирование всех исключений
4. **Чистота кода** - контроллеры стали проще
5. **Расширяемость** - легко добавлять новые типы исключений
6. **Безопасность** - правильные HTTP статус коды

## Использование в контроллерах

### До (старый способ):
```java
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    try {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid username or password"));
    }
}
```

### После (новый способ):
```java
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(ApiResponse.success("Login successful", response));
}
```

Теперь контроллеры стали намного чище и проще для понимания!



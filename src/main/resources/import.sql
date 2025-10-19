-- Добавляем тестовых пользователей для чата
INSERT INTO app_user (id, firstname, lastname, email, password, role, verification_code) VALUES 
(2, 'John', 'Doe', 'john@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'USER', true),
(3, 'Jane', 'Smith', 'jane@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'USER', true);

-- Добавляем тестовые сообщения
INSERT INTO message (id, content, sender_id, receiver_id, created_at, is_read, message_type) VALUES 
(1, 'Привет! Как дела?', 1, 2, NOW(), false, 'TEXT'),
(2, 'Привет! Все хорошо, спасибо!', 2, 1, NOW(), false, 'TEXT'),
(3, 'Что планируешь на выходные?', 1, 2, NOW(), false, 'TEXT'),
(4, 'Пойду в кино. А ты?', 2, 1, NOW(), false, 'TEXT'),
(5, 'Отлично! Какой фильм?', 1, 2, NOW(), false, 'TEXT'),
(6, 'Новый фильм Marvel', 2, 1, NOW(), false, 'TEXT'),
(7, 'Круто! Расскажешь потом', 1, 2, NOW(), false, 'TEXT'),
(8, 'Конечно!', 2, 1, NOW(), false, 'TEXT');






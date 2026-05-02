Тестовые аккаунты
admin@mail.com / admin123 — ADMIN
user@mail.com / user123 — USER
test@mail.com / test123 — USER

# 📁 JavaShareHub — Файлообменник

Веб-приложение для обмена файлами с регистрацией,
авторизацией и разграничением доступа.
Разработано в рамках Control work #8.

---

## 🛠️ Технологический стек

| Слой | Технология |
|---|---|
| Backend | Java, Spring Boot|
| Web | Spring MVC |
| Безопасность | Spring Security |
| База данных | H2 (file режим) |
| ORM | Spring Data JPA / Hibernate |
| Миграции БД | Liquibase |
| Шаблоны | FreeMarker |
| Frontend | HTML, CSS, Bootstrap 5 |
| Логирование | SLF4J + Logback |
| Утилиты | Lombok |

---

## 📋 Функциональность

### Файлы
- Загрузка файлов (до 50MB)
- Статус PUBLIC / PRIVATE
- Фильтрация по категориям (Documents, Images, Videos, Archives)
- Пагинация (10 файлов на странице)
- Счётчик скачиваний

### Скачивание
- Публичные файлы — доступны всем без авторизации
- Приватные файлы — по одноразовой ссылке с UUID токеном
- После использования ссылка становится недействительной

### Безопасность
- Регистрация и вход по Email + Пароль
- Пароли хранятся в зашифрованном виде (BCrypt)
- Роли: ROLE_USER и ROLE_ADMIN
- Удаление только своих файлов

### Админ панель
- Просмотр всех файлов и пользователей
- Удаление любых файлов и пользователей
- Статистика (кол-во файлов и пользователей)

---

## 🔗 Эндпоинты

### Доступно всем

| Метод | URL | Описание |
|---|---|---|
| GET | `/files` | Список публичных файлов |
| GET | `/files?category=Documents` | Фильтр по категории |
| GET | `/files?page=1` | Пагинация |
| GET | `/files/download/{id}` | Скачать публичный файл |
| GET | `/files/private/{token}` | Скачать по приватной ссылке (одноразовая) |
| GET | `/login` | Страница входа |
| POST | `/login` | Авторизация |
| GET | `/register` | Страница регистрации |
| POST | `/register` | Регистрация |
| POST | `/logout` | Выход |

### Только авторизованным

| Метод | URL | Описание |
|---|---|---|
| GET | `/profile` | Мои файлы |
| GET | `/files/upload` | Страница загрузки |
| POST | `/files/upload` | Загрузить файл |
| POST | `/files/delete/{id}` | Удалить свой файл |
| POST | `/files/generate-link/{id}` | Получить приватную ссылку |

### Только администратору

| Метод | URL | Описание |
|---|---|---|
| GET | `/admin` | Админ панель |
| POST | `/admin/files/delete/{id}` | Удалить любой файл |
| POST | `/admin/users/delete/{id}` | Удалить пользователя |

---

## 🏗 Архитектура

### Entity слой
- **User** — пользователь (id, email, password, name, role)
- **SharedFile** — файл (id, originalName, storedName, fileType, fileSize, status, category, downloadCount, uploadedAt, user)
- **PrivateLink** — одноразовая ссылка (id, token, file, used, createdAt)

### Repository слой
- **UserRepository** — `findByEmail()` — поиск пользователя по email
- **SharedFileRepository** — `findByStatus()`, `findByStatusAndCategory()`, `findByUserId()`, `findByUserIdAndCategory()` — выборка файлов с фильтрацией и пагинацией
- **PrivateLinkRepository** — `findByTokenAndUsedFalse()` — поиск активной ссылки по токену

### Service слой
- **UserService**
 - `register()` — регистрация нового пользователя с проверкой уникальности email и шифрованием пароля
 - `findByEmail()` — поиск пользователя по email
 - `countUsers()` — количество пользователей для админки

- **FileService**
 - `upload()` — сохранение файла на диск и запись в БД
 - `getPublicFiles()` — список публичных файлов с фильтрацией и пагинацией
 - `getUserFiles()` — список файлов конкретного пользователя
 - `getById()` — получение файла по ID
 - `incrementDownload()` — увеличение счётчика скачиваний
 - `delete()` — удаление файла пользователем (с проверкой владельца)
 - `adminDelete()` — удаление файла администратором (без проверки владельца)
 - `countFiles()` — количество файлов для админки

- **PrivateLinkService**
 - `generateLink()` — генерация UUID токена и сохранение в БД
 - `useLink()` — поиск активной ссылки, пометка как использованной (saveAndFlush) и возврат

### Controller слой
- **AuthController**
 - `GET /login` — страница входа
 - `GET /register` — страница регистрации
 - `POST /register` — обработка регистрации

- **FileController**
 - `GET /files` — список публичных файлов с фильтрацией
 - `GET /profile` — список файлов авторизованного пользователя
 - `GET /files/upload` — страница загрузки
 - `POST /files/upload` — обработка загрузки файла
 - `GET /files/download/{id}` — скачивание публичного файла
 - `GET /files/private/{token}` — скачивание по одноразовой ссылке
 - `POST /files/generate-link/{id}` — генерация приватной ссылки
 - `POST /files/delete/{id}` — удаление своего файла

- **AdminController**
 - `GET /admin` — панель со статистикой, списком файлов и пользователей
 - `POST /admin/files/delete/{id}` — удаление любого файла
 - `POST /admin/users/delete/{id}` — удаление пользователя

- **GlobalExceptionHandler**
 - `RuntimeException` — общая обработка ошибок
 - `AccessDeniedException` — нет доступа
 - `MaxUploadSizeExceededException` — файл слишком большой

### Config слой
- **SecurityConfig** — настройка Spring Security, правила доступа по ролям, страницы входа и выхода, CSRF защита
- **UserDetailsServiceImpl** — загрузка пользователя по email для Spring Security
- **DataInitializer** — автоматическое создание тестовых файлов при первом запуске

---

## ⚙️ Запуск

```bash
git clone https://github.com/your/javasharehub.git
cd javasharehub
mvn spring-boot:run

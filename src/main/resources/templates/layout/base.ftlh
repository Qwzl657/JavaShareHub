<#macro base username="" isAdmin=false>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>JavaShareHub</title>
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    </head>
    <body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="/files">
                <i class="bi bi-share-fill"></i> JavaShareHub
            </a>
            <div class="d-flex align-items-center gap-3">
                <#if username?has_content>
                    <span class="text-light">
                    <i class="bi bi-person-circle"></i> ${username}
                </span>
                    <a href="/profile" class="btn btn-outline-light btn-sm">
                        <i class="bi bi-folder"></i> Мои файлы
                    </a>
                    <a href="/files/upload" class="btn btn-success btn-sm">
                        <i class="bi bi-upload"></i> Загрузить
                    </a>
                    <#if isAdmin>
                        <a href="/admin" class="btn btn-warning btn-sm">
                            <i class="bi bi-shield-check"></i> Админ
                        </a>
                    </#if>
                    <form method="post" action="/logout" class="d-inline">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button class="btn btn-outline-danger btn-sm">Выйти</button>
                    </form>
                <#else>
                    <a href="/login" class="btn btn-outline-light btn-sm">Войти</a>
                    <a href="/register" class="btn btn-warning btn-sm">Регистрация</a>
                </#if>
            </div>
        </div>
    </nav>

    <div class="container pb-5">
        <#nested>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    </html>
</#macro>
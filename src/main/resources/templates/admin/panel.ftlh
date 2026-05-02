<#import "../layout/base.ftlh" as layout>
<@layout.base username=username!"">

    <h2 class="mb-4"><i class="bi bi-shield-check"></i> Админ панель</h2>

    <div class="row g-4 mb-4">
        <div class="col-md-3">
            <div class="card shadow-sm text-center p-3">
                <h2 class="text-primary">${totalFiles}</h2>
                <p class="text-muted mb-0">Всего файлов</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm text-center p-3">
                <h2 class="text-success">${totalUsers}</h2>
                <p class="text-muted mb-0">Всего пользователей</p>
            </div>
        </div>
    </div>

    <div class="alert alert-info">
        Для управления файлами перейдите в
        <a href="/profile">профиль пользователя</a>.
    </div>

</@layout.base>
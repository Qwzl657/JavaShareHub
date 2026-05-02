<#import "layout/base.ftlh" as layout>
<@layout.base username="">
    <div class="alert alert-danger mt-4">
        <h4><i class="bi bi-exclamation-triangle"></i> Ошибка</h4>
        <p>${error!"Что-то пошло не так"}</p>
        <a href="/files" class="btn btn-primary">На главную</a>
    </div>
</@layout.base>
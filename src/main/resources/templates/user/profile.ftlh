<#import "../layout/base.ftlh" as layout>
<@layout.base username=username!"">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-folder"></i> Мои файлы</h2>
        <a href="/files/upload" class="btn btn-success">
            <i class="bi bi-upload"></i> Загрузить файл
        </a>
    </div>

    <form method="get" action="/profile" class="d-flex gap-2 mb-4">
        <select name="category" class="form-select" style="width: 200px;">
            <option value="">Все категории</option>
            <option value="Documents" <#if category=="Documents">selected</#if>>Documents</option>
            <option value="Images" <#if category=="Images">selected</#if>>Images</option>
            <option value="Videos" <#if category=="Videos">selected</#if>>Videos</option>
            <option value="Archives" <#if category=="Archives">selected</#if>>Archives</option>
        </select>
        <button class="btn btn-primary">Фильтр</button>
        <#if category?has_content>
            <a href="/profile" class="btn btn-outline-secondary">Сбросить</a>
        </#if>
    </form>

    <div class="row g-3">
        <#list files as file>
            <div class="col-md-6">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="mb-1">
                                    <i class="bi bi-file-earmark"></i> ${file.originalName}
                                </h6>
                                <small class="text-muted">
                                    ${file.category!""} •
                                    ${(file.fileSize / 1024)?int} KB •
                                    <i class="bi bi-download"></i> ${file.downloadCount}
                                </small>
                            </div>
                            <#if file.status == "PUBLIC">
                                <span class="badge bg-success">PUBLIC</span>
                            <#else>
                                <span class="badge bg-secondary">PRIVATE</span>
                            </#if>
                        </div>
                    </div>
                    <div class="card-footer d-flex gap-2">
                        <#if file.status == "PUBLIC">
                            <a href="/files/download/${file.id}" class="btn btn-sm btn-primary">
                                <i class="bi bi-download"></i> Скачать
                            </a>
                        <#else>
                            <form method="post" action="/files/generate-link/${file.id}">
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button class="btn btn-sm btn-warning">
                                    <i class="bi bi-link-45deg"></i> Получить ссылку
                                </button>
                            </form>
                        </#if>
                        <form method="post" action="/files/delete/${file.id}">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-sm btn-outline-danger">
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        <#else>
            <div class="col-12">
                <div class="alert alert-info">Вы ещё не загружали файлы</div>
            </div>
        </#list>
    </div>

    <div class="d-flex justify-content-center gap-2 mt-4">
        <#if (currentPage > 0)>
            <a href="/profile?page=${currentPage - 1}&category=${category}"
               class="btn btn-outline-secondary">← Назад</a>
        </#if>
        <#if (totalPages > 0)>
            <span class="btn btn-light disabled">${currentPage + 1} / ${totalPages}</span>
        </#if>
        <#if (currentPage + 1 < totalPages)>
            <a href="/profile?page=${currentPage + 1}&category=${category}"
               class="btn btn-outline-primary">Вперёд →</a>
        </#if>
    </div>

</@layout.base>
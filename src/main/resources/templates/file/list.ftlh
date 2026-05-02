<#import "../layout/base.ftlh" as layout>
<@layout.base username=username!"">

    <h2 class="mb-4"><i class="bi bi-files"></i> Публичные файлы</h2>

    <form method="get" action="/files" class="d-flex gap-2 mb-4">
        <select name="category" class="form-select" style="width: 200px;">
            <option value="">Все категории</option>
            <option value="Documents" <#if category=="Documents">selected</#if>>Documents</option>
            <option value="Images" <#if category=="Images">selected</#if>>Images</option>
            <option value="Videos" <#if category=="Videos">selected</#if>>Videos</option>
            <option value="Archives" <#if category=="Archives">selected</#if>>Archives</option>
        </select>
        <button class="btn btn-primary">Фильтр</button>
        <#if category?has_content>
            <a href="/files" class="btn btn-outline-secondary">Сбросить</a>
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
                            <span class="badge bg-success">PUBLIC</span>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/files/download/${file.id}" class="btn btn-sm btn-primary">
                            <i class="bi bi-download"></i> Скачать
                        </a>
                    </div>
                </div>
            </div>
        <#else>
            <div class="col-12">
                <div class="alert alert-info">Файлы не найдены</div>
            </div>
        </#list>
    </div>

    <div class="d-flex justify-content-center gap-2 mt-4">
        <#if (currentPage > 0)>
            <a href="/files?page=${currentPage - 1}&category=${category}"
               class="btn btn-outline-secondary">← Назад</a>
        </#if>
        <#if (totalPages > 0)>
            <span class="btn btn-light disabled">${currentPage + 1} / ${totalPages}</span>
        </#if>
        <#if (currentPage + 1 < totalPages)>
            <a href="/files?page=${currentPage + 1}&category=${category}"
               class="btn btn-outline-primary">Вперёд →</a>
        </#if>
    </div>

</@layout.base>
<#import "../layout/base.ftlh" as layout>
<@layout.base username=username!"">

    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-body p-4">
                    <h4 class="mb-3"><i class="bi bi-upload"></i> Загрузить файл</h4>
                    <form method="post" action="/files/upload" enctype="multipart/form-data">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="mb-3">
                            <label class="form-label">Файл</label>
                            <input type="file" name="file" class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Категория</label>
                            <select name="category" class="form-select">
                                <option value="">Без категории</option>
                                <option value="Documents">Documents</option>
                                <option value="Images">Images</option>
                                <option value="Videos">Videos</option>
                                <option value="Archives">Archives</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Статус</label>
                            <select name="status" class="form-select">
                                <option value="PUBLIC">Публичный</option>
                                <option value="PRIVATE">Приватный</option>
                            </select>
                        </div>
                        <button class="btn btn-success w-100">
                            <i class="bi bi-upload"></i> Загрузить
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</@layout.base>
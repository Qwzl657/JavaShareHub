<#import "../layout/base.ftlh" as layout>
<@layout.base username=username!"">

    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-body p-4">
                    <h4 class="mb-3">
                        <i class="bi bi-link-45deg"></i> Приватная ссылка
                    </h4>
                    <div class="alert alert-warning">
                        <i class="bi bi-exclamation-triangle"></i>
                        Ссылка одноразовая — после скачивания она станет недействительной!
                    </div>
                    <div class="input-group">
                        <input type="text" class="form-control"
                               value="http://localhost:8080${link}" readonly/>
                        <button class="btn btn-outline-secondary"
                                onclick="navigator.clipboard.writeText('http://localhost:8080${link}')">
                            <i class="bi bi-clipboard"></i>
                        </button>
                    </div>
                    <a href="/profile" class="btn btn-primary mt-3">← Назад к файлам</a>
                </div>
            </div>
        </div>
    </div>

</@layout.base>
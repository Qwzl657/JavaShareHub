<#import "../layout/base.ftlh" as layout>
<@layout.base username="" isAdmin=false>
    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body p-4">
                    <h4 class="mb-3">
                        <i class="bi bi-person-plus"></i> Регистрация
                    </h4>
                    <#if error??>
                        <div class="alert alert-danger">${error}</div>
                    </#if>
                    <form method="post" action="/register">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="mb-3">
                            <label class="form-label">Имя</label>
                            <input type="text" name="name"
                                   class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email"
                                   class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password"
                                   class="form-control" required/>
                        </div>
                        <button class="btn btn-success w-100">
                            Зарегистрироваться
                        </button>
                    </form>
                    <div class="text-center mt-3">
                        <a href="/login">Уже есть аккаунт? Войти</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.base>
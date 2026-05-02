<#import "../layout/base.ftlh" as layout>
<@layout.base username="">
    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body p-4">
                    <h4 class="mb-3"><i class="bi bi-box-arrow-in-right"></i> Вход</h4>
                    <#if RequestParameters.error??>
                        <div class="alert alert-danger">Неверный email или пароль</div>
                    </#if>
                    <form method="post" action="/login">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="username" class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password" class="form-control" required/>
                        </div>
                        <button class="btn btn-primary w-100">Войти</button>
                    </form>
                    <div class="text-center mt-3">
                        <a href="/register">Нет аккаунта? Зарегистрироваться</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.base>
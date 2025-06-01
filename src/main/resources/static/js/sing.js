document.addEventListener("DOMContentLoaded", () => {

    const form        = document.getElementById("loginForm");
    const fieldNames  = ["username", "password"];
    const commonError = document.getElementById("common-error");

    form.addEventListener("submit", async e => {
        e.preventDefault();
        clearErrors();

        const dto = Object.fromEntries(new FormData(form).entries());

        let res;
        try {
            res = await fetch("/auth/sign-in", {
                method : "POST",
                headers: {"Content-Type":"application/json"},
                body   : JSON.stringify(dto)
            });
        } catch (err) {
            commonError.textContent = "Сервер недоступен. Попробуйте позже.";
            return;
        }

        const body = await res.json();

        if (res.ok) {                 // 200
            localStorage.setItem("accessToken", body.accessToken);  // пример: сохраняем access
            localStorage.setItem("refreshToken", body.refreshToken);  // пример: сохраняем refresh
            location.href = body.redirectUrl;
            return;
        }

        if (res.status === 401) {                     // неверный логин/пароль
            commonError.textContent = body.message;
            return;
        }

        if (res.status === 422) {                     // Bean-валидация
            showFieldErrors(body);                      // {field: msg}
            return;
        }

        commonError.textContent = "Ошибка " + res.status;
    });

    /* ---------------- helpers ---------------- */

    function clearErrors() {
        commonError.textContent = "";
        fieldNames.forEach(n => {
            const input = form.querySelector(`[name="${n}"]`);
            input.classList.remove("invalid");
            const err = input.nextElementSibling;
            if (err && err.classList.contains("error-message")) err.remove();
        });
    }

    function showFieldErrors(map) {
        Object.entries(map).forEach(([field, msg]) => {
            const input = form.querySelector(`[name="${field}"]`);
            if (!input) return;
            input.classList.add("invalid");

            const span = document.createElement("span");
            span.className = "error-message";
            span.textContent = msg;
            input.after(span);
        });
    }
});

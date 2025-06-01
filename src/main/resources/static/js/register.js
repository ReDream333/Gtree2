document.addEventListener("DOMContentLoaded", () => {
    const form       = document.getElementById("regForm");
    const fieldNames = ["username", "email", "password"];

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        clearErrors();

        /* собираем DTO из формы --------------------------------------------- */
        const dto = Object.fromEntries(new FormData(form).entries());

        /* отправляем JSON ---------------------------------------------------- */
        let res;
        try {
            res = await fetch("/auth/sign-up", {
                method : "POST",
                headers: {"Content-Type": "application/json"},
                body   : JSON.stringify(dto)
            });
        } catch (err) {
            alert("Сервер недоступен. Попробуйте позже.");
            console.error(err);
            return;
        }

        const ctype = res.headers.get("content-type") || "";
        if (!ctype.includes("application/json")) {
            console.error("HTML response:", await res.text());
            alert("Ошибка сервера. Подробности — в консоли.");
            return;
        }
        const body = await res.json();

        /* обработка ---------------------------------------------------------- */
        if (res.ok && body.success) {
            location.href = body.redirectUrl;
            return;
        }

        if (res.status === 422) {
            showErrors(body);
            return;
        }

        alert("Неизвестная ошибка: " + res.status);
    });

    /* -------------------- helpers ---------------------------------------- */

    function clearErrors() {
        fieldNames.forEach(name => {
            const input = form.querySelector(`[name="${name}"]`);
            if (!input) return;
            input.classList.remove("invalid");
            const err = input.nextElementSibling;
            if (err && err.classList.contains("error-message")) err.remove();
        });
    }

    function showErrors(map) {
        Object.entries(map).forEach(([field, msg]) => {
            const input = form.querySelector(`[name="${field}"]`);
            if (!input) return;
            input.classList.add("invalid");

            const span = document.createElement("span");
            span.className   = "error-message";
            span.textContent = msg;
            input.after(span);
        });
    }
});

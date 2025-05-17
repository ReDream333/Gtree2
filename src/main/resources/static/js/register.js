document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const errorMessage = document.createElement("div");
    errorMessage.className = "error-message";
    form.insertBefore(errorMessage, form.firstChild); // Добавляем над формой

    form.addEventListener("submit", (e) => {
        e.preventDefault(); // Предотвращаем стандартное поведение формы

        const formData = new FormData(form);

        fetch("/register", {
            method: "POST",
            body: new URLSearchParams(formData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.href = "profile.jsp"; // Перенаправляем на профиль при успехе
                } else {
                    errorMessage.textContent = data.errorMessage; // Выводим сообщение об ошибке
                    errorMessage.style.color = "red";
                }
            })
            .catch(error => {
                console.error("Ошибка при отправке формы:", error);
                errorMessage.textContent = "Произошла ошибка. Попробуйте позже.";
                errorMessage.style.color = "red";
            });
    });
});


document.addEventListener("DOMContentLoaded", () => {
    const errorContainer = document.querySelector(".error-container");
    const closeButton = document.getElementById("closeErrorButton");

    closeButton.addEventListener("click", () => {
        console.log("Закрытие контейнера...");
        errorContainer.classList.add("hidden"); // Добавляем класс для скрытия
    })
});
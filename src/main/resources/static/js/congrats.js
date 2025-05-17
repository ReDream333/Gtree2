document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("congratsModal");
    const closeModalButton = document.getElementById("closeModalButton");

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("success") === "true") {

        confetti({
            particleCount: 200,
            spread: 100,
            origin: { y: 0.6 },
        });

        setTimeout(() => {
            modal.style.display = "flex";
        }, 1000); // 1 секунда на конфетти
    }

    closeModalButton.addEventListener("click", () => {

        const url = new URL(window.location.href);
        url.searchParams.delete("success"); // Удаляем параметр "success"

        window.history.replaceState(null, "", url.toString());

        modal.style.display = "none";
    });
});
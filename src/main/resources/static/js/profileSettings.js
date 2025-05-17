document.addEventListener("DOMContentLoaded", () => {
  const updateButton = document.getElementById("updateButton");
  const confirmationModal = document.getElementById("confirmationModal");
  const confirmButton = document.getElementById("confirmButton");
  const closeModalButton = document.getElementById("closeModalButton");
  const message = document.getElementById("message");

  const currentNameInput = document.getElementById("currentNameInput");
  const currentPassword = document.getElementById("currentPassword");

  const newNameInput = document.getElementById("newName");
  const newPasswordInput = document.getElementById("newPassword");

  let updateType = ""; // Тип операции: имя или пароль

  // Открыть модальное окно
  updateButton.addEventListener("click", () => {
    confirmationModal.style.display = "flex";
  });

  // Закрыть модальное окно
  closeModalButton.addEventListener("click", () => {
    confirmationModal.style.display = "none";
  });

  // Подтвердить текущие данные
  confirmButton.addEventListener("click", () => {
    const currentName = currentNameInput.value.trim();
    const currentPass = currentPassword.value.trim();

    fetch("/change", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `currentName=${encodeURIComponent(currentName)}&currentPassword=${encodeURIComponent(currentPass)}`
    })
        .then(response => response.text())
        .then(result => {
          if (result === "success") {
            confirmationModal.style.display = "none"; // Закрыть модалку
            submitUpdate(); // Отправить новые данные
          } else {
            message.textContent = "Неверный логин или пароль!";
          }
        })
        .catch(error => {
          console.error("Ошибка:", error);
        });
  });

  function submitUpdate() {
    const newName = newNameInput.value.trim();
    const newPassword = newPasswordInput.value.trim();

    fetch("/change", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `newName=${encodeURIComponent(newName)}&newPassword=${encodeURIComponent(newPassword)}`
    })
        .then(response => response.text())
        .then(result => {
          if (result === "success") {
            message.style.color = "green";
            message.textContent = "Данные успешно обновлены!";
          } else {
            message.style.color = "red";
            message.textContent = "Ошибка при обновлении данных.";
          }
        });
  }
});

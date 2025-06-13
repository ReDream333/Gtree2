const bioInput = document.getElementById("bioInput");
const charCount = document.getElementById("charCount");
const saveStatus = document.getElementById("saveStatus");
const saveButton = document.getElementById("saveButton");
const clearButton = document.getElementById("clearButton");
const autoFillButton = document.getElementById("autoFillButton");

const openScrollButton = document.getElementById("openScrollButton");
const closeScrollButton = document.getElementById("closeScrollButton");
const scrollContainer = document.getElementById("scrollContainer");

charCount.textContent = `${bioInput.value.length} символов`;

// Обновление количества символов
bioInput.addEventListener("input", () => {
    charCount.textContent = `${bioInput.value.length} символов`;
    saveStatus.textContent = "Есть несохранённые изменения";
});

// Сохранение биографии
saveButton.addEventListener("click", () => {
    const bioText = bioInput.value;

    fetch("biography", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `nodeId=${nodeId}&biography=${encodeURIComponent(bioText)}`
    }).catch(error => console.error("Ошибка сохранения:", error));

    saveStatus.textContent = "Текст сохранён";
});

// Очистка биографии
clearButton.addEventListener("click", () => {

    const isConfirmed = confirm("Вы уверены, что хотите удалить текст биографии? Это действие нельзя отменить.");

    if (isConfirmed) {
        // Если пользователь подтвердил, выполняем удаление
        fetch(`biography?nodeId=${nodeId}`, {
            method: "DELETE"
        }).catch(error => console.error("Ошибка удаления:", error));

        // Очищаем поле ввода и обновляем статус
        bioInput.value = "";
        charCount.textContent = "0 символов";
        saveStatus.textContent = "Текст удалён";
    } else {
        // Если пользователь отменил действие
        console.log("Удаление отменено пользователем.");
    }
});


// Автозаполнение биографии из Википедии
let wikiBiographies = [];
let wikiIndex = 0;
autoFillButton.addEventListener("click", async () => {
    if (wikiBiographies.length === 0) {
        try {
            const resp = await fetch(`biography/auto?nodeId=${nodeId}`);
            wikiBiographies = await resp.json();
            wikiIndex = 0;
            autoFillButton.textContent = `Автозаполнение - ${wikiBiographies.length}`;
        } catch (e) {
            console.error(e);
            return;
        }
    }
    if (wikiBiographies.length > 0) {
        bioInput.value = wikiBiographies[wikiIndex];
        charCount.textContent = `${bioInput.value.length} символов`;
        saveStatus.textContent = "Есть несохранённые изменения";
        wikiIndex = (wikiIndex + 1) % wikiBiographies.length;
        let remaining = wikiBiographies.length - wikiIndex;
        if (remaining === 0) remaining = wikiBiographies.length;
        autoFillButton.textContent = `Автозаполнение - ${remaining}`;
    }
});

// Летопись
const overlay = document.createElement("div");
overlay.classList.add("overlay");
document.body.appendChild(overlay);

openScrollButton.addEventListener("click", () => {
    const bioText = bioInput.value.trim();

    const placeholderTexts = [
        "Помни, Плотва подождет, а летопись - нет.",
        "Белый лист ждёт. Возможно, здесь будет написано что-то великое, а возможно, сам Лютик приложит руку к этой рукописи?",
        "Летописец ушёл за вдохновением и скоро вернётся... конечно, если его не затянет лор Ведьмака",
        "Здесь должно было быть что-то о Геральте, Йеннифэр и единорогах… но судьба распорядилась иначе.",
        "Когда летописец доиграет в Ведьмака, он обязательно приступит к работе!",
        "Похоже, Дийкстра купил все чернила в Новиграде. Страница осталась пустой",
        "На свитке лежит пыль Каэр Морхена. Добавьте пару строк о старых ведьмаках!",
        "Даже Ламберт бы что-то написал здесь, если бы был трезв. У вас есть шанс лучше!",
        "Как говорил Весемир: 'Не спеши, ученик. Пустой свиток — это лишь начало пути.",
        "Цири пронеслась мимо и оставила пустую страницу. Попробуйте догнать её словами"
    ];

    const randomPlaceholder = placeholderTexts[Math.floor(Math.random() * placeholderTexts.length)];

    // Если текста нет, показываем заглушку
    const scrollText = document.getElementById("scrollText");
    scrollText.textContent = bioText || randomPlaceholder;

    // Открываем летопись
    overlay.classList.add("show");
    scrollContainer.style.display = "block";
    scrollContainer.style.animation = "scrollOpen 1s ease-out forwards";
});

closeScrollButton.addEventListener("click", () => {
    scrollContainer.style.animation = "scrollClose 1s ease-out forwards";

    setTimeout(() => {
        scrollContainer.style.display = "none";
        overlay.classList.remove("show");
    }, 700);
});

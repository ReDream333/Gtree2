/* ---------- вспомогательные геттеры токенов ---------------- */
const access  = () => localStorage.getItem("accessToken");
const refresh = () => localStorage.getItem("refreshToken");

/* ---------- Cloudinary widget ------------------------------ */
let widget;  // объявим позже

/* ---------- старт ------------------------------------------ */
document.addEventListener("DOMContentLoaded", init);

async function init() {
    await loadMe();                      // пробуем сразу
    initButtons();                       // вешаем события
}

/* ---------- получаем /api/me --------------------------------*/
async function loadMe() {
    try {
        const res  = await fetch("/users/api/me", {
            headers: { "Authorization":"Bearer "+access(),
                "Accept":"application/json" }
        });
        if (res.status === 401 && await tryRefresh()) return loadMe();
        if (!res.ok) throw await res.text();

        const u = await res.json();
        document.getElementById("uname").textContent  = u.username;
        document.getElementById("email").textContent += u.email;
        document.getElementById("photo").src          = u.photoUrl ?? "/images/sunf.jpg";

        const date = new Date(u.createdAt);
        document.getElementById("createdAt").textContent +=
            date.toLocaleDateString("ru-RU", {
                year: "numeric", month: "long", day: "numeric"
            });
    } catch (e) {
        showError("Не удалось загрузить профиль");
        console.error(e);
    }
}

/* ---------- refresh-токен ---------------------------------- */
async function tryRefresh() {
    if (!refresh()) return false;
    const res = await fetch("/auth/refresh", {
        method : "POST",
        headers: {"Content-Type":"application/json"},
        body   : JSON.stringify({refreshToken: refresh()})
    });
    if (!res.ok) return false;
    const b = await res.json();
    localStorage.setItem("accessToken",  b.accessToken);
    localStorage.setItem("refreshToken", b.refreshToken);
    return true;
}

/* ---------- init кнопок + Cloudinary ----------------------- */
function initButtons() {

    /* загрузка фото */
    widget = cloudinary.createUploadWidget(
        { cloudName: 'gtree', uploadPreset: 'usersphoto' },
        (error, result) => {
            if (error || result.event !== "success") return;
            savePhoto(result.info.secure_url);
        });

    document.getElementById("upload_widget")
        .addEventListener("click", () => widget.open());

    /* удалить аккаунт */
    document.getElementById("delete_button")
        .addEventListener("click", async () => {
            if (!confirm("Удалить аккаунт?")) return;
            const res = await fetch("users/api/me", {
                method : "DELETE",
                headers: { "Authorization":"Bearer "+access() }
            });
            if (res.ok) { localStorage.clear(); location.href="/"; }
            else        { showError("Не удалось удалить"); }
        });

    /* редактировать (переход на форму) */
    document.getElementById("edit_button")
        .addEventListener("click", () => location.href="/settings");


    /* выход из аккаунта */
    document.getElementById("logout_button")
        .addEventListener("click", async () => {
            await fetch("/auth/sign-out");
            localStorage.clear();
            location.href = "/";
        });
}

/* ---------- POST /saveProfilePhoto ------------------------- */
async function savePhoto(url) {
    const res = await fetch("/saveProfilePhoto", {
        method : "POST",
        headers: { "Content-Type":"application/json",
            "Authorization":"Bearer "+access() },
        body   : JSON.stringify({imageUrl:url})
    });
    if (res.ok) document.getElementById("photo").src = url;
    else showError("Ошибка при сохранении фото");
}

/* ---------- helper ----------------------------------------- */
const showError = msg => document.getElementById("error").textContent = msg;

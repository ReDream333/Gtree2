// --------------- 1.  вытаскиваем username -------------------------
const path   = window.location.pathname;
const parts  = path.split("/").filter(Boolean);
const username = parts.at(-1);

// --------------- 2.  грузим профиль -------------------------------

fetch(`/users/api/${encodeURIComponent(username)}`)
    .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
    })
    .then(u => fillProfile(u))
    .catch(err => {
        console.error(err);
        alert("Пользователь не найден");
    });

// --------------- 3.  рендер ---------------------------------------
function fillProfile(u){
    document.getElementById("username").textContent = u.username;
    document.getElementById("email")   .textContent = u.email;

    if (u.photoUrl){
        document.getElementById("photo").src = u.photoUrl;
    }

    const d = new Date(u.createdAt);
    document.getElementById("createdAt").textContent =
        d.toLocaleDateString("ru-RU",
            {year:"numeric", month:"long", day:"numeric"});
}

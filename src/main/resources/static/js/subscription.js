document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('subscribeBtn');
    if (!btn) return;

    fetch(`/api/trees/${treeId}/subscribe`)
        .then(r => r.ok ? r.json() : { subscribed: false })
        .then(d => updateBtn(d.subscribed));

    btn.addEventListener('click', async () => {
        const subscribed = btn.dataset.subscribed === 'true';
        const method = subscribed ? 'DELETE' : 'POST';
        const res = await fetch(`/api/trees/${treeId}/subscribe`, { method });
        if (res.ok) {
            updateBtn(!subscribed);
        }
    });

    function updateBtn(state) {
        btn.dataset.subscribed = state;
        btn.textContent = state ? 'Отписаться от напоминаний' : 'Подписаться на напоминания';
    }
});
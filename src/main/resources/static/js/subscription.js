document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('subscribeBtn');
    const modal = document.getElementById('subscriptionModal');
    const messageEl = document.getElementById('subscriptionMessage');
    const closeBtn = document.getElementById('subscriptionClose');

    if (!btn) return;

    fetch(`/api/trees/${treeId}/subscribe`)
        .then(r => r.ok ? r.json() : { subscribed: false })
        .then(d => updateBtn(d.subscribed));

    btn.addEventListener('click', async () => {
        const subscribed = btn.dataset.subscribed === 'true';
        const method = subscribed ? 'DELETE' : 'POST';
        const res = await fetch(`/api/trees/${treeId}/subscribe`, { method });
        if (res.status === 403) {
            showMessage('Для подписки необходимо подтвердить почту');
            return;
        }
        if (res.ok) {
            updateBtn(!subscribed);
            showMessage(subscribed ? 'Вы отписались от дерева' : 'Вы подписаны на дерево');
        }
    });

    closeBtn.addEventListener('click', () => modal.style.display = 'none');

    function updateBtn(state) {
        btn.dataset.subscribed = state;
        btn.textContent = state ? 'Отписаться от напоминаний' : 'Подписаться на напоминания';
    }

    function showMessage(text) {
        messageEl.textContent = text;
        modal.style.display = 'flex';
    }
});
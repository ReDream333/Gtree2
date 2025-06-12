document.addEventListener('DOMContentLoaded', async () => {
    const parts = window.location.pathname.split('/');
    const username = decodeURIComponent(parts.at(-1));
    document.getElementById('dialog-with').textContent = 'Диалог с ' + username;

    const messages = document.getElementById('messages');
    const form = document.getElementById('chat-form');
    const input = document.getElementById('chat-input');

    let current = '';
    try {
        const res = await fetch('/users/api/me');
        if (res.ok) current = (await res.json()).username;
    } catch (e) {}

    try {
        const res = await fetch(`/dialog/api/${username}`);
        if (res.ok) {
            const history = await res.json();
            history.forEach(append);
        }
    } catch (e) {}

    const ws = new WebSocket(`ws://${location.host}/ws/dialog/${encodeURIComponent(username)}`);

    ws.addEventListener('message', e => {
        const msg = JSON.parse(e.data);
        append(msg);
    });

    form.addEventListener('submit', e => {
        e.preventDefault();
        if (input.value.trim() !== '') {
            ws.send(input.value);
            input.value = '';
        }
    });

    function append(msg){
        const div = document.createElement('div');
        div.classList.add('message');
        div.classList.add(msg.sender === current ? 'me' : 'other');
        div.textContent = msg.sender + ': ' + msg.content;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }
});
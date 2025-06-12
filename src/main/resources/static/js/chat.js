document.addEventListener('DOMContentLoaded', async () => {
    const ws = new WebSocket(`ws://${location.host}/ws/chat`);
    const form = document.getElementById('chat-form');
    const input = document.getElementById('chat-input');
    const messages = document.getElementById('messages');

    let current = '';
    try {
        const res = await fetch('/users/api/me');
        if (res.ok) current = (await res.json()).username;
    } catch (e) {}


    ws.addEventListener('message', e => {
        const div = document.createElement('div');
        div.classList.add('message');
        const [sender, text] = e.data.split(': ', 2);
        div.classList.add(sender === current ? 'me' : 'other');
        div.textContent = e.data;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    });

    form.addEventListener('submit', e => {
        e.preventDefault();
        if (input.value.trim() !== '') {
            ws.send(input.value);
            input.value = '';
        }
    });
});
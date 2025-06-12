document.addEventListener('DOMContentLoaded', () => {
    const ws = new WebSocket(`ws://${location.host}/ws/chat`);
    const form = document.getElementById('chat-form');
    const input = document.getElementById('chat-input');
    const messages = document.getElementById('messages');

    ws.addEventListener('message', e => {
        const div = document.createElement('div');
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
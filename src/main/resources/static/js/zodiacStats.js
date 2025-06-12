document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('zodiacStatsBtn');
    if (!btn) return;
    btn.addEventListener('click', async () => {
        const res = await fetch(`/api/trees/${treeId}/nodes/zodiac-stat`);
        if (!res.ok) return;
        const data = await res.json();
        const stats = data.stats || {};
        const labels = Object.keys(stats);
        const values = Object.values(stats);
        const ctx = document.getElementById('zodiacChart');
        ctx.style.display = 'block';
        new Chart(ctx, {
            type: 'pie',
            data: { labels, datasets: [{ data: values, backgroundColor: labels.map((_,i)=>`hsl(${i*360/labels.length},70%,70%)`)}] }
        });
        document.getElementById('zodiacMessage').textContent = data.message;
    });
});
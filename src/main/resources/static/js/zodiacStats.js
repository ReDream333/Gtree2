let zodiacChartInstance;

document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('zodiacStatsBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        const res = await fetch(`/api/trees/${treeId}/nodes/zodiac-stat`);
        console.log(res.data);
        if (!res.ok) {
            console.log(res.data);
            return ;
        }

        const data = await res.json();
        console.log(data);
        const stats = data.stats || {};
        const labels = Object.keys(stats);
        const values = Object.values(stats);
        const canvas = document.getElementById('zodiacChart');
        canvas.style.display = 'block';

        if (zodiacChartInstance) {
            zodiacChartInstance.destroy();
        }

        zodiacChartInstance = new Chart(canvas, {
            type: 'pie',
            data: {
                labels,
                datasets: [{
                    data: values,
                    backgroundColor: labels.map((_, i) => `hsl(${i * 360 / labels.length},70%,70%)`)
                }]
            }
        });

        document.getElementById('zodiacMessage').textContent = data.message;
    });
});
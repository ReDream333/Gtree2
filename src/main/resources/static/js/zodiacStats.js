let zodiacChartInstance;

document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('zodiacStatsBtn');
    const modal = document.getElementById('zodiacModal');
    const closeBtn = document.getElementById('zodiacModalClose');
    if (!btn || !modal || !closeBtn) return;

    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    btn.addEventListener('click', async () => {
        try {
            const res = await fetch(`/api/trees/${treeId}/nodes/zodiac-stat`);
            if (!res.ok) {
                const errorData = await res.json();
                console.error('Error fetching zodiac stats:', errorData);
                return;
            }

            const data = await res.json();
            const stats = data.stats || {};
            const labels = Object.keys(stats);
            const values = Object.values(stats);
            const canvas = document.getElementById('zodiacChart');

            modal.style.display = 'flex';

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
                },
                options: {
                    responsive: false
                }
            });

            document.getElementById('zodiacMessage').textContent = data.message;
        } catch (error) {
            console.error('Не удалось получить статистику знаков зодиака:', error);
        }
    });
});

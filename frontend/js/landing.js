document.addEventListener('DOMContentLoaded', () => {
    const greetingEl = document.getElementById('greeting');
    const startBtn = document.getElementById('start-exploring-btn');

    // 1. Personalized Greeting
    const userJson = localStorage.getItem('user');
    if (userJson) {
        try {
            const user = JSON.parse(userJson);
            if (user && user.name) {
                // Get first name or full name
                const firstName = user.name.split(' ')[0];
                greetingEl.textContent = `Hey ${firstName}`;
            }
        } catch (e) {
            console.error("Error parsing user data from local storage", e);
        }
    }

    // 2. Handle Start Exploring click
    if (startBtn) {
        startBtn.addEventListener('click', () => {
            // Fade out the entire body for a smooth page transition
            document.body.style.transition = 'opacity 0.6s ease';
            document.body.style.opacity = '0';

            // Navigate to destinations page after fade
            setTimeout(() => {
                window.location.href = 'pages/destinations.html';
            }, 600);
        });
    }
});

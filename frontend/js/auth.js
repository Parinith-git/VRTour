const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    const authButtons = document.getElementById('auth-buttons');

    if (authButtons) {
        const token = localStorage.getItem('token');
        if (token) {
            authButtons.innerHTML = `
                <a href="pages/dashboard.html" class="btn btn-outline" style="border: none; margin-right: 5px;">Dashboard</a>
                <a href="pages/profile.html" class="btn btn-primary">Profile</a>
            `;
        }
    }

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const errorMsg = document.getElementById('errorMsg');

            try {
                const res = await fetch(`${API_BASE_URL}/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })
                });

                if (res.ok) {
                    const data = await res.json();
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('user', JSON.stringify(data));
                    window.location.href = 'dashboard.html';
                } else {
                    errorMsg.textContent = 'Invalid email or password';
                    errorMsg.style.display = 'block';
                }
            } catch (error) {
                console.error(error);
                errorMsg.textContent = 'Server error. Please try again later.';
                errorMsg.style.display = 'block';
            }
        });
    }

    if (signupForm) {
        signupForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const errorMsg = document.getElementById('errorMsg');
            const successMsg = document.getElementById('successMsg');

            // Email Validation: Must have a valid domain (e.g. .com)
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
            if (!emailRegex.test(email)) {
                errorMsg.textContent = 'Please enter a valid email address with a domain (e.g. .com).';
                errorMsg.style.display = 'block';
                successMsg.style.display = 'none';
                return;
            }

            // Password Validation: Min 8 chars, 1 uppercase, 1 special char
            const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/;
            if (!passwordRegex.test(password)) {
                errorMsg.textContent = 'Password does not meet the requirements.';
                errorMsg.style.display = 'block';
                successMsg.style.display = 'none';
                return;
            }

            try {
                const res = await fetch(`${API_BASE_URL}/auth/signup`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ name, email, password })
                });

                if (res.ok) {
                    errorMsg.style.display = 'none';
                    successMsg.textContent = 'Registration successful! Please login.';
                    successMsg.style.display = 'block';
                    setTimeout(() => window.location.href = 'login.html', 2000);
                } else {
                    const text = await res.text();
                    errorMsg.textContent = text || 'Registration failed';
                    errorMsg.style.display = 'block';
                    successMsg.style.display = 'none';
                }
            } catch (error) {
                console.error(error);
                errorMsg.textContent = 'Server error. Please try again later.';
                errorMsg.style.display = 'block';
            }
        });
    }
});

function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '../index.html';
}

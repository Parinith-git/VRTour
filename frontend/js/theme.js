/* ══════════════════════════════════════════════════
   THEME SYSTEM — Light / Dark Mode Toggle
   Loaded on every page via <script src="theme.js">
   ══════════════════════════════════════════════════ */

(function () {
    'use strict';

    // ── 1. Determine initial theme ──
    function getPreferredTheme() {
        const saved = localStorage.getItem('vrtour-theme');
        if (saved) return saved;
        // Respect system preference on first visit
        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }

    // ── 2. Apply theme immediately (before DOM renders) ──
    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('vrtour-theme', theme);
        updateToggleState(theme);
        updateChartColors(theme);
    }

    // Apply instantly to prevent FOUC (flash of unstyled content)
    applyTheme(getPreferredTheme());

    // ── 3. Inject toggle button into navbar ──
    function injectToggle() {
        const nav = document.querySelector('nav');
        if (!nav || document.getElementById('themeToggle')) return;

        // Create toggle element
        const toggle = document.createElement('div');
        toggle.className = 'theme-toggle';
        toggle.id = 'themeToggle';
        toggle.setAttribute('role', 'switch');
        toggle.setAttribute('aria-label', 'Toggle dark mode');
        toggle.setAttribute('tabindex', '0');
        toggle.innerHTML = `
            <div class="toggle-track">
                <div class="toggle-thumb">
                    <span class="toggle-icon">${getPreferredTheme() === 'dark' ? '🌙' : '☀️'}</span>
                </div>
            </div>
        `;

        // Insert before auth-buttons div or at the end of nav
        const authBtns = nav.querySelector('#auth-buttons') || nav.querySelector('.btn');
        if (authBtns) {
            nav.insertBefore(toggle, authBtns);
        } else {
            nav.appendChild(toggle);
        }

        // Click handler
        toggle.addEventListener('click', () => {
            const current = document.documentElement.getAttribute('data-theme');
            const next = current === 'dark' ? 'light' : 'dark';
            applyTheme(next);
        });

        // Keyboard support (Enter / Space)
        toggle.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggle.click();
            }
        });
    }

    // ── 4. Update toggle visual state ──
    function updateToggleState(theme) {
        const icon = document.querySelector('.toggle-icon');
        if (icon) {
            icon.textContent = theme === 'dark' ? '🌙' : '☀️';
        }
        const toggle = document.getElementById('themeToggle');
        if (toggle) {
            toggle.setAttribute('aria-checked', theme === 'dark');
        }
    }

    // ── 5. Update chart colors for dark mode (donut chart) ──
    function updateChartColors(theme) {
        // Re-render donut chart if it exists and data is available
        const canvas = document.getElementById('donutChart');
        if (canvas && typeof drawDonutChart === 'function' && typeof currentResult !== 'undefined' && currentResult) {
            // The chart draws with CSS-independent colors, so it stays readable.
            // But update the center text color based on theme.
            // We'll let drawDonutChart handle this via a global flag.
        }
        // Set a global for the chart renderer to pick up
        window.__darkMode = (theme === 'dark');
    }

    // ── 6. Listen for system theme changes ──
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
        if (!localStorage.getItem('vrtour-theme')) {
            applyTheme(e.matches ? 'dark' : 'light');
        }
    });

    // ── 7. Inject on DOM ready ──
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', injectToggle);
    } else {
        injectToggle();
    }
})();

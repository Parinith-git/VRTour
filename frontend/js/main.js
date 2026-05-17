document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('a');
    const navLinks = document.querySelectorAll('.nav-links a');
    const currentPath = window.location.pathname;

    // ── 1. ACTIVE PAGE HIGHLIGHTING ──
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        
        // Match home (index.html or root /)
        if ((currentPath === '/' || currentPath.endsWith('index.html')) && href.includes('index.html')) {
            link.classList.add('active');
        } 
        // Match other pages
        else if (currentPath.includes(href) && href !== 'index.html' && href !== '../index.html') {
            link.classList.add('active');
        }
    });

    // ── 2. SCROLL SECTION HIGHLIGHTING (for multi-section pages) ──
    const sections = document.querySelectorAll('header[id], section[id]');
    if (sections.length > 1) {
        const observerOptions = {
            threshold: 0.6,
            rootMargin: '0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const id = entry.target.getAttribute('id');
                    navLinks.forEach(link => {
                        link.classList.remove('active');
                        if (link.getAttribute('href').includes(id)) {
                            link.classList.add('active');
                        }
                    });
                }
            });
        }, observerOptions);

        sections.forEach(section => observer.observe(section));
    }

    // ── 3. PAGE TRANSITION LOGIC ──
    links.forEach(link => {
        link.addEventListener('click', (e) => {
            const target = link.getAttribute('href');
            
            if (target && target.includes('.html') && !target.startsWith('#') && link.target !== '_blank') {
                e.preventDefault();
                document.body.style.transition = 'opacity 0.4s ease';
                document.body.style.opacity = '0';
                
                setTimeout(() => {
                    window.location.href = target;
                }, 400);
            }
        });
    });
});

// ── AI Travel Budget Estimation Engine (INR - Fallback) ──

const DEST_DATA = {
    1: { name:'Taj Mahal', city:'Agra', country:'India', region:'asia', hotel:6500, food:2000, transport:1200, tickets:800, bestMonths:'Oct–Mar', stayDays:'3–4', nearby:['Agra Fort','Fatehpur Sikri','Mehtab Bagh'], tips:['Visit at sunrise for fewer crowds','Carry cash — many local vendors don\'t accept cards','Dress modestly when visiting religious sites'] },
    2: { name:'Eiffel Tower', city:'Paris', country:'France', region:'europe', hotel:15000, food:5000, transport:1600, tickets:2000, bestMonths:'Apr–Jun, Sep–Oct', stayDays:'4–5', nearby:['Louvre Museum','Champs-Élysées','Sacré-Cœur'], tips:['Buy skip-the-line tickets online','Visit the summit at sunset','The Paris Museum Pass saves money on attractions'] },
    3: { name:'Sydney Opera House', city:'Sydney', country:'Australia', region:'oceania', hotel:16500, food:4500, transport:2000, tickets:1600, bestMonths:'Sep–Nov, Mar–May', stayDays:'4–6', nearby:['Harbour Bridge','Bondi Beach','Royal Botanic Garden'], tips:['Take the ferry for the best Opera House views','Book a backstage tour in advance','Sydney is walkable — save on transport in the CBD'] },
    4: { name:'Colosseum', city:'Rome', country:'Italy', region:'europe', hotel:14000, food:4200, transport:1500, tickets:1800, bestMonths:'Apr–Jun, Sep–Oct', stayDays:'4–5', nearby:['Roman Forum','Vatican City','Pantheon'], tips:['Book combined Colosseum + Forum tickets','Avoid restaurants right next to tourist sites','Tap water from public fountains is safe and free'] },
    5: { name:'Pyramids of Giza', city:'Giza', country:'Egypt', region:'africa', hotel:7500, food:1600, transport:1000, tickets:1200, bestMonths:'Oct–Apr', stayDays:'3–5', nearby:['Sphinx','Egyptian Museum','Khan el-Khalili Bazaar'], tips:['Hire a licensed guide at the gate','Bring sun protection — there\'s no shade','Negotiate prices before accepting any service'] },
    6: { name:'Liberty Island', city:'New York', country:'USA', region:'americas', hotel:20000, food:5500, transport:2500, tickets:2000, bestMonths:'Apr–Jun, Sep–Nov', stayDays:'5–7', nearby:['Ellis Island','Central Park','Times Square'], tips:['Book crown access tickets months ahead','Take the Staten Island Ferry for free Statue views','Get a MetroCard for unlimited subway rides'] },
    7: { name:'Space Loop City', city:'Orbital Station', country:'Space', region:'space', hotel:42000, food:8500, transport:4200, tickets:17000, bestMonths:'Year-round', stayDays:'2–3', nearby:['Zero-G Observatory','Nebula Deck','Star Garden'], tips:['Pack light — luggage fees are astronomical','Book the observation pod at least 6 months ahead','The artificial gravity zones are most comfortable for first-timers'] }
};

const FLIGHT_COSTS = {
    asia:    { asia:8000, europe:45000, oceania:50000, americas:65000, africa:40000, space:500000 },
    europe:  { asia:45000, europe:12000, oceania:75000, americas:42000, africa:28000, space:500000 },
    oceania: { asia:42000, europe:75000, oceania:15000, americas:80000, africa:65000, space:500000 },
    americas:{ asia:65000, europe:42000, oceania:80000, americas:15000, africa:58000, space:500000 },
    africa:  { asia:40000, europe:28000, oceania:65000, americas:58000, africa:15000, space:500000 },
    space:   { asia:500000,europe:500000,oceania:500000,americas:500000,africa:500000,space:100000 }
};

const STYLE_MULT = { budget: 0.55, comfortable: 1.0, luxury: 2.2 };

const ORIGIN_REGIONS = {
    'india':'asia','china':'asia','japan':'asia','thailand':'asia','singapore':'asia','korea':'asia','vietnam':'asia','indonesia':'asia','malaysia':'asia','philippines':'asia','nepal':'asia','sri lanka':'asia','bangladesh':'asia','pakistan':'asia','uae':'asia','dubai':'asia','saudi arabia':'asia',
    'mumbai':'asia','delhi':'asia','bangalore':'asia','chennai':'asia','hyderabad':'asia','kolkata':'asia','pune':'asia','ahmedabad':'asia','jaipur':'asia','lucknow':'asia','kochi':'asia','goa':'asia',
    'usa':'americas','canada':'americas','mexico':'americas','brazil':'americas','argentina':'americas','colombia':'americas','chile':'americas','peru':'americas','new york':'americas','los angeles':'americas','chicago':'americas','miami':'americas','toronto':'americas','vancouver':'americas',
    'uk':'europe','france':'europe','germany':'europe','italy':'europe','spain':'europe','netherlands':'europe','switzerland':'europe','london':'europe','paris':'europe','berlin':'europe','amsterdam':'europe','rome':'europe','portugal':'europe','sweden':'europe','norway':'europe','denmark':'europe','austria':'europe','belgium':'europe','ireland':'europe','poland':'europe','czech republic':'europe','greece':'europe','turkey':'europe','russia':'europe',
    'australia':'oceania','new zealand':'oceania','sydney':'oceania','melbourne':'oceania','fiji':'oceania',
    'egypt':'africa','south africa':'africa','kenya':'africa','morocco':'africa','nigeria':'africa','ethiopia':'africa','tanzania':'africa','ghana':'africa',
    'space':'space','moon':'space','mars':'space'
};

function detectRegion(origin) {
    const lower = origin.toLowerCase().trim();
    for (const [key, region] of Object.entries(ORIGIN_REGIONS)) {
        if (lower.includes(key)) return region;
    }
    return 'asia'; // default for Indian users
}

function calculateBudget(destId, origin, startDate, endDate, style) {
    const dest = DEST_DATA[destId];
    if (!dest) return null;

    const start = new Date(startDate);
    const end = new Date(endDate);
    const nights = Math.max(1, Math.round((end - start) / (1000 * 60 * 60 * 24)));
    const mult = STYLE_MULT[style] || 1.0;
    const originRegion = detectRegion(origin);

    const baseFlight = FLIGHT_COSTS[originRegion]?.[dest.region] || 50000;
    const flight = Math.round(baseFlight * (style === 'luxury' ? 2.5 : style === 'budget' ? 0.7 : 1.0));
    const hotel = Math.round(dest.hotel * nights * mult);
    const food = Math.round(dest.food * nights * mult);
    const transport = Math.round(dest.transport * nights * mult);
    const tickets = Math.round(dest.tickets * Math.min(nights, 5) * mult);
    const misc = Math.round((hotel + food + transport) * 0.08);
    const total = flight + hotel + food + transport + tickets + misc;

    return {
        destination: dest,
        origin,
        nights,
        style,
        flight, hotel, food, transport, tickets, misc, total,
        dailyAvg: Math.round(total / nights),
        breakdown: [
            { label:'Flights', value:flight, icon:'ri-plane-line', color:'#C6E7FF' },
            { label:'Hotel & Stay', value:hotel, icon:'ri-hotel-line', color:'#FFDDAE' },
            { label:'Food & Dining', value:food, icon:'ri-restaurant-line', color:'#D4F6FF' },
            { label:'Local Transport', value:transport, icon:'ri-bus-line', color:'#B8E6C8' },
            { label:'Entry Tickets', value:tickets, icon:'ri-ticket-line', color:'#E8D4FF' },
            { label:'Miscellaneous', value:misc, icon:'ri-more-line', color:'#FFD4D4' }
        ]
    };
}

function drawDonutChart(canvas, breakdown, total) {
    const ctx = canvas.getContext('2d');
    const w = canvas.width = 260;
    const h = canvas.height = 260;
    const cx = w / 2, cy = h / 2, r = 100, inner = 65;
    ctx.clearRect(0, 0, w, h);

    let angle = -Math.PI / 2;
    breakdown.forEach(item => {
        const slice = (item.value / total) * Math.PI * 2;
        ctx.beginPath();
        ctx.arc(cx, cy, r, angle, angle + slice);
        ctx.arc(cx, cy, inner, angle + slice, angle, true);
        ctx.closePath();
        ctx.fillStyle = item.color;
        ctx.fill();
        angle += slice;
    });

    // Center text — theme-aware colors
    const isDark = window.__darkMode;
    ctx.fillStyle = isDark ? '#F8FAFC' : '#2C3E50';
    ctx.textAlign = 'center';
    // Format: show crores for very large numbers
    let displayTotal;
    if (total >= 1_00_00_000) {
        const crores = (total / 1_00_00_000).toFixed(1);
        displayTotal = '₹' + crores + ' Cr';
        ctx.font = '600 19px Outfit';
    } else {
        displayTotal = '₹' + total.toLocaleString('en-IN');
        ctx.font = '600 20px Outfit';
    }
    ctx.fillText(displayTotal, cx, cy + 2);
    ctx.font = '300 13px Outfit';
    ctx.fillStyle = isDark ? '#94A3B8' : '#7B8D9E';
    ctx.fillText('Total Budget', cx, cy + 22);
}

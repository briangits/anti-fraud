// Dark mode detection
if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    document.documentElement.classList.add('dark');
}
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', event => {
    if (event.matches) {
        document.documentElement.classList.add('dark');
    } else {
        document.documentElement.classList.remove('dark');
    }
});

// Global variables
let currentRole = '';
let transactionCount = 0;
let fraudCount = 0;
let blockHeight = 1000000;
let charts = {};

// Login functionality
function login() {
    currentRole = document.getElementById('roleSelect').value;
    document.getElementById('loginModal').classList.add('hidden');
    document.getElementById('dashboard').classList.remove('hidden');

    updateUIForRole();
    initializeCharts();
    startRealTimeUpdates();
}

function logout() {
    document.getElementById('loginModal').classList.remove('hidden');
    document.getElementById('dashboard').classList.add('hidden');
    stopRealTimeUpdates();
}

// Role-based UI updates
function updateUIForRole() {
    const roleDisplayNames = {
        'user': 'User - Basic Monitoring',
        'admin': 'Admin - System Configuration',
        'manager': 'Manager - Fraud Oversight',
        'ceo': 'CEO - Strategic Overview'
    };

    document.getElementById('userRole').textContent = roleDisplayNames[currentRole];

    // Update navigation based on role
    const navTabs = document.getElementById('navTabs');
    const tabs = navTabs.querySelectorAll('.nav-tab');

    if (currentRole === 'user') {
        // Users can only see overview and monitoring
        tabs.forEach((tab, index) => {
            if (index > 1) tab.style.display = 'none';
        });
    } else {
        // All other roles see all tabs
        tabs.forEach(tab => tab.style.display = 'inline-block');
    }
}

// Tab switching
function showTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
    });

    // Show selected tab
    document.getElementById(tabName + '-tab').classList.remove('hidden');

    // Update nav tab styles
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active', 'bg-primary', 'text-white');
        tab.classList.add('text-gray-600', 'dark:text-gray-400');
    });

    event.target.classList.add('active', 'bg-primary', 'text-white');
    event.target.classList.remove('text-gray-600', 'dark:text-gray-400');
}

// Initialize charts
function initializeCharts() {
    // Transaction chart
    const transactionCtx = document.getElementById('transactionChart').getContext('2d');
    charts.transaction = new Chart(transactionCtx, {
        type: 'line',
        data: {
            labels: Array.from({
                length: 12
            }, (_, i) => `${i + 1}:00`),
            datasets: [{
                label: 'Transactions',
                data: Array.from({
                    length: 12
                }, () => Math.floor(Math.random() * 1000) + 500),
                borderColor: '#5D5CDE',
                backgroundColor: 'rgba(93, 92, 222, 0.1)',
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });

    // Fraud detection chart
    const fraudCtx = document.getElementById('fraudChart').getContext('2d');
    charts.fraud = new Chart(fraudCtx, {
        type: 'doughnut',
        data: {
            labels: ['Safe', 'Suspicious', 'Fraud'],
            datasets: [{
                data: [85, 12, 3],
                backgroundColor: ['#4ECDC4', '#FFE66D', '#FF6B6B']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // Risk scoring chart
    const riskCtx = document.getElementById('riskChart').getContext('2d');
    charts.risk = new Chart(riskCtx, {
        type: 'radar',
        data: {
            labels: ['Velocity', 'Location', 'Device', 'Behavior', 'Network', 'Time'],
            datasets: [{
                label: 'Risk Factors',
                data: [65, 59, 90, 81, 56, 55],
                fill: true,
                backgroundColor: 'rgba(255, 107, 107, 0.2)',
                borderColor: '#FF6B6B',
                pointBackgroundColor: '#FF6B6B'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

// Real-time updates
let updateInterval;

function startRealTimeUpdates() {
    updateInterval = setInterval(() => {
        updateMetrics();
        addTransaction();
        addAlert();
        updateBlockchain();
        updateNodes();
    }, 2000);

    // Initial load
    updateMetrics();
    generateInitialData();
}

function stopRealTimeUpdates() {
    if (updateInterval) {
        clearInterval(updateInterval);
    }
}

function updateMetrics() {
    transactionCount += Math.floor(Math.random() * 10) + 1;
    fraudCount += Math.random() > 0.7 ? 1 : 0;

    document.getElementById('totalTransactions').textContent = transactionCount.toLocaleString();
    document.getElementById('fraudDetected').textContent = fraudCount.toLocaleString();
    document.getElementById('riskScore').textContent = Math.floor(Math.random() * 100);
    document.getElementById('activeNodes').textContent = Math.floor(Math.random() * 3) + 8;
}

function addTransaction() {
    const feed = document.getElementById('transactionFeed');
    const transaction = createTransactionElement();

    feed.insertBefore(transaction, feed.firstChild);

    // Remove old transactions
    while (feed.children.length > 10) {
        feed.removeChild(feed.lastChild);
    }
}

function createTransactionElement() {
    const div = document.createElement('div');
    const isHighRisk = Math.random() > 0.8;
    const amount = (Math.random() * 10000).toFixed(2);

    div.className = `p-3 rounded-lg border slide-in ${isHighRisk ? 'border-red-200 bg-red-50 dark:border-red-800 dark:bg-red-900/20' : 'border-gray-200 bg-gray-50 dark:border-gray-700 dark:bg-gray-700'}`;

    div.innerHTML = `
                <div class="flex justify-between items-center ">
                    <div>
                        <div class="font-mono text-sm ">${generateTransactionId()}</div>
                        <div class="text-xs text-gray-600 dark:text-gray-400 ">$${amount}</div>
                    </div>
                    <div class="text-right ">
                        <div class="text-xs text-gray-600 dark:text-gray-400 ">${new Date().toLocaleTimeString()}</div>
                        <div class="${isHighRisk ? 'text-red-600' : 'text-green-600'} text-xs font-medium ">
                            ${isHighRisk ? 'HIGH RISK' : 'NORMAL'}
                        </div>
                    </div>
                </div>
            `;

    return div;
}

function addAlert() {
    if (Math.random() > 0.6) return; // Don't always add alerts

    const feed = document.getElementById('alertsFeed');
    const alert = createAlertElement();

    feed.insertBefore(alert, feed.firstChild);

    while (feed.children.length > 5) {
        feed.removeChild(feed.lastChild);
    }
}

function createAlertElement() {
    const alerts = [
        'Unusual login pattern detected',
        'Multiple failed authentication attempts',
        'Velocity check triggered',
        'Behavioral anomaly identified',
        'Device fingerprint mismatch'
    ];

    const div = document.createElement('div');
    div.className = 'p-3 rounded-lg border border-yellow-200 bg-yellow-50 dark:border-yellow-800 dark:bg-yellow-900/20 slide-in';

    div.innerHTML = `
                <div class="flex items-start space-x-2 ">
                    <div class="w-2 h-2 bg-yellow-500 rounded-full mt-2 "></div>
                    <div>
                        <div class="text-sm font-medium ">${alerts[Math.floor(Math.random() * alerts.length)]}</div>
                        <div class="text-xs text-gray-600 dark:text-gray-400 ">${new Date().toLocaleTimeString()}</div>
                    </div>
                </div>
            `;

    return div;
}

function updateBlockchain() {
    blockHeight++;
    document.getElementById('blockHeight').textContent = blockHeight.toLocaleString();

    const log = document.getElementById('blockchainLog');
    const entry = document.createElement('div');
    entry.className = 'text-green-600 dark:text-green-400 slide-in';
    entry.textContent = `Block ${blockHeight}: ${generateBlockHash()}`;

    log.insertBefore(entry, log.firstChild);

    while (log.children.length > 8) {
        log.removeChild(log.lastChild);
    }
}

function updateNodes() {
    const grid = document.getElementById('nodesGrid');
    grid.innerHTML = '';

    const regions = ['US-East', 'US-West', 'EU-Central', 'Asia-Pacific', 'South America', 'Africa'];

    regions.forEach(region => {
        const nodeDiv = document.createElement('div');
        const isHealthy = Math.random() > 0.1;
        const latency = Math.floor(Math.random() * 100) + 10;

        nodeDiv.className = 'bg-white dark:bg-gray-800 p-6 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700';
        nodeDiv.innerHTML = `
                    <div class="flex items-center justify-between mb-4 ">
                        <h4 class="font-semibold ">${region}</h4>
                        <div class="w-3 h-3 ${isHealthy ? 'bg-green-500' : 'bg-red-500'} rounded-full ${isHealthy ? 'pulse-slow' : ''} "></div>
                    </div>
                    <div class="space-y-2 text-sm ">
                        <div class="flex justify-between ">
                            <span>Status</span>
                            <span class="${isHealthy ? 'text-green-600' : 'text-red-600'} ">${isHealthy ? 'Online' : 'Offline'}</span>
                        </div>
                        <div class="flex justify-between ">
                            <span>Latency</span>
                            <span>${latency}ms</span>
                        </div>
                        <div class="flex justify-between ">
                            <span>Load</span>
                            <span>${Math.floor(Math.random() * 100)}%</span>
                        </div>
                    </div>
                `;

        grid.appendChild(nodeDiv);
    });
}

function generateInitialData() {
    // Generate initial transactions
    for (let i = 0; i < 5; i++) {
        setTimeout(() => addTransaction(), i * 500);
    }

    // Generate initial alerts
    for (let i = 0; i < 3; i++) {
        setTimeout(() => addAlert(), i * 1000);
    }

    // Generate initial blockchain entries
    for (let i = 0; i < 5; i++) {
        setTimeout(() => updateBlockchain(), i * 300);
    }

    updateNodes();
}

function generateTransactionId() {
    return 'TXN-' + Math.random().toString(36).substr(2, 9).toUpperCase();
}

function generateBlockHash() {
    return Array.from({
        length: 64
    }, () => Math.floor(Math.random() * 16).toString(16)).join('');
}

// Initialize nav tab styles
document.addEventListener('DOMContentLoaded', () => {
    const firstTab = document.querySelector('.nav-tab');
    if (firstTab) {
        firstTab.classList.add('active', 'bg-primary', 'text-white');
        firstTab.classList.remove('text-gray-600', 'dark:text-gray-400');
    }
});
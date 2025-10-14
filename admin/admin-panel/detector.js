// === GLOBAL STATE ===
let userRole = null;

// === LOGIN HANDLER ===
function login() {
    const roleSelect = document.getElementById("roleSelect");
    userRole = roleSelect.value;

    document.getElementById("loginModal").classList.add("hidden");
    document.getElementById("dashboard").classList.remove("hidden");

    const roleDisplay = document.getElementById("userRole");
    roleDisplay.textContent = `Logged in as ${userRole.toUpperCase()}`;

    // Customize dashboard by role
    if (userRole === "user") {
        document.getElementById("ai-detection-tab").classList.add("hidden");
        document.getElementById("blockchain-tab").classList.add("hidden");
        document.getElementById("nodes-tab").classList.add("hidden");
    } else if (userRole === "manager") {
        document.getElementById("ai-detection-tab").classList.remove("hidden");
        document.getElementById("blockchain-tab").classList.remove("hidden");
    } else if (userRole === "ceo") {
        document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("hidden"));
    }
}

// === LOGOUT HANDLER ===
function logout() {
    location.reload();
}

// === TAB NAVIGATION ===
function showTab(tabId) {
    document.querySelectorAll(".tab-content").forEach(tab => tab.classList.add("hidden"));
    document.querySelectorAll(".nav-tab").forEach(tab => tab.classList.remove("active"));
    const targetTab = document.getElementById(`${tabId}-tab`);
    if (targetTab) {
        targetTab.classList.remove("hidden");
        document.querySelector(`[onclick="showTab('${tabId}')"]`).classList.add("active");
    }
}

// === DASHBOARD METRIC UPDATES ===
function updateMetrics() {
    document.getElementById("totalTransactions").textContent = Math.floor(Math.random() * 5000);
    document.getElementById("fraudDetected").textContent = Math.floor(Math.random() * 200);
    document.getElementById("riskScore").textContent = (Math.random() * 100).toFixed(1);
    document.getElementById("activeNodes").textContent = 10 + Math.floor(Math.random() * 5);
}

// === BLOCKCHAIN LOG SIMULATION ===
function updateBlockchainLog() {
    const log = document.getElementById("blockchainLog");
    if (!log) return;
    const entries = [
        "Block #15243 verified ✅",
        "Smart contract executed for TX-98213",
        "Node sync completed ⛓️",
        "Block #15244 mined successfully ⛏️"
    ];
    const entry = document.createElement("div");
    entry.textContent = entries[Math.floor(Math.random() * entries.length)];
    log.prepend(entry);
    if (log.childElementCount > 6) log.removeChild(log.lastChild);
}

// === NODE STATUS GENERATION ===
function updateNodes() {
    const nodesGrid = document.getElementById("nodesGrid");
    if (!nodesGrid) return;
    nodesGrid.innerHTML = "";
    const statuses = ["Active", "Syncing", "Idle", "Error"];
    for (let i = 1; i <= 6; i++) {
        const div = document.createElement("div");
        const status = statuses[Math.floor(Math.random() * statuses.length)];
        const color =
            status === "Active" ? "text-green-600" :
            status === "Syncing" ? "text-yellow-600" :
            status === "Idle" ? "text-gray-500" :
            "text-red-600";
        div.className = "p-4 bg-gray-50 dark:bg-gray-700 rounded-lg";
        div.innerHTML = `
            <h4 class="font-medium mb-1">Node-${i}</h4>
            <p class="${color} font-medium">${status}</p>
        `;
        nodesGrid.appendChild(div);
    }
}

// === REAL-TIME TRANSACTION FEED ===
function generateTransactionFeed() {
    const feed = document.getElementById("transactionFeed");
    if (!feed) return;
    const txs = [
        "TX-8452 → $2,345 approved",
        "TX-1203 → $12,430 flagged 🚨",
        "TX-9823 → $640 cleared ✅",
        "TX-5672 → $9,820 under review ⚠️"
    ];
    const div = document.createElement("div");
    div.textContent = txs[Math.floor(Math.random() * txs.length)];
    feed.prepend(div);
    if (feed.childElementCount > 10) feed.removeChild(feed.lastChild);
}

// === SECURITY ALERT FEED ===
function generateAlertFeed() {
    const feed = document.getElementById("alertsFeed");
    if (!feed) return;
    const alerts = [
        "Suspicious IP detected on Node-3 🚨",
        "Unusual transaction velocity by User-128 ⚠️",
        "Admin override logged 🧩",
        "Blockchain re-sync triggered 🔁"
    ];
    const div = document.createElement("div");
    div.textContent = alerts[Math.floor(Math.random() * alerts.length)];
    feed.prepend(div);
    if (feed.childElementCount > 8) feed.removeChild(feed.lastChild);
}

// === AUTO REFRESH ===
setInterval(() => {
    updateMetrics();
    updateBlockchainLog();
    updateNodes();
    generateTransactionFeed();
    generateAlertFeed();
}, 5000);

// Initialize once DOM is ready
document.addEventListener("DOMContentLoaded", () => {
    updateMetrics();
    updateNodes();
});

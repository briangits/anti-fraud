// ====== Dynamic Audit Log System ======

// Sample data for random events
const users = ["Admin", "Manager", "UserA", "UserB", "CEO", "Validator01"];
const actions = [
    "Login Attempt", "Fraud Scan Triggered", "Node Sync",
    "Block Verified", "Alert Issued", "Access Request"
];
const targets = [
    "Transaction#12345", "Node#East-4", "Wallet#XZ92",
    "Dataset#FraudList", "Configuration Panel"
];
const outcomes = ["Success", "Failure", "Verified", "Rejected", "Pending"];
const nodes = ["Node-01", "Node-02", "Validator-Alpha", "Consensus-Beta"];

const tableBody = document.querySelector("#auditTable tbody");

// Add a log row to table
function addLogToTable(log) {
    const row = document.createElement("tr");
    row.innerHTML = `
    <td>${log.timestamp}</td>
    <td>${log.user}</td>
    <td>${log.action}</td>
    <td>${log.target}</td>
    <td>${log.outcome}</td>
    <td>${log.node}</td>
  `;
    tableBody.insertBefore(row, tableBody.firstChild);

    // Limit to last 20 logs
    const rows = tableBody.querySelectorAll("tr");
    if (rows.length > 20) tableBody.removeChild(rows[rows.length - 1]);
}

// Load stored logs (from Settings page actions)
function loadStoredLogs() {
    const storedLogs = JSON.parse(localStorage.getItem("auditLogs")) || [];
    storedLogs.forEach(addLogToTable);
}

// Create simulated random logs
function generateLogEntry() {
    return {
        timestamp: new Date().toLocaleString(),
        user: users[Math.floor(Math.random() * users.length)],
        action: actions[Math.floor(Math.random() * actions.length)],
        target: targets[Math.floor(Math.random() * targets.length)],
        outcome: outcomes[Math.floor(Math.random() * outcomes.length)],
        node: nodes[Math.floor(Math.random() * nodes.length)]
    };
}

// Start automatic log updates
function startAuditLogFeed() {
    setInterval(() => {
        const log = generateLogEntry();
        addLogToTable(log);
    }, Math.floor(Math.random() * 1500) + 1000);
}

// Initialize
document.addEventListener("DOMContentLoaded", () => {
    loadStoredLogs();
    startAuditLogFeed();
});
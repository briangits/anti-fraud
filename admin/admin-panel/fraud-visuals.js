// Fraud Detection Line Graph + Node Grid
// Requires Chart.js for graphs — include in your HTML:
// <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

let fraudChart;
let fraudData = [];
let timeLabels = [];

// === FRAUD DETECTION TREND LINE ===
function initFraudLineGraph() {
    const ctx = document.getElementById("fraudChart");
    if (!ctx) return;

    // Initialize dummy data
    for (let i = 0; i < 10; i++) {
        fraudData.push(Math.floor(Math.random() * 200));
        timeLabels.push(`${i}:00`);
    }

    fraudChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: timeLabels,
            datasets: [{
                label: "Fraudulent Transactions",
                data: fraudData,
                borderColor: "#e74c3c",
                backgroundColor: "rgba(231,76,60,0.1)",
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 4,
                pointBackgroundColor: "#c0392b"
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: { title: { display: true, text: "Time" }},
                y: { title: { display: true, text: "Number of Fraud Detections" }, beginAtZero: true }
            },
            plugins: {
                legend: { display: true },
                tooltip: { mode: "index", intersect: false }
            }
        }
    });
}

// === LIVE GRAPH UPDATES ===
function updateFraudGraph() {
    if (!fraudChart) return;
    const now = new Date();
    const label = `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}`;
    const newValue = Math.floor(Math.random() * 250);

    fraudData.push(newValue);
    timeLabels.push(label);

    if (fraudData.length > 15) {
        fraudData.shift();
        timeLabels.shift();
    }

    fraudChart.update();
}

// === DATA NODES GRID ===
function initDataNodes() {
    const container = document.getElementById("nodesGrid");
    if (!container) return;

    container.innerHTML = "";
    const statuses = ["Active", "Syncing", "Idle", "Error"];

    for (let i = 1; i <= 8; i++) {
        const status = statuses[Math.floor(Math.random() * statuses.length)];
        const color =
            status === "Active" ? "bg-green-100 text-green-800" :
            status === "Syncing" ? "bg-yellow-100 text-yellow-800" :
            status === "Idle" ? "bg-gray-100 text-gray-800" :
            "bg-red-100 text-red-800";

        const node = document.createElement("div");
        node.className = `p-4 rounded-xl shadow-md ${color} flex flex-col items-center justify-center transition-all hover:scale-105`;
        node.innerHTML = `
            <h3 class="font-semibold text-lg">Node-${i}</h3>
            <p class="text-sm">${status}</p>
        `;
        container.appendChild(node);
    }
}

// === AUTO UPDATE NODES ===
function updateDataNodes() {
    const nodes = document.querySelectorAll("#nodesGrid div");
    const statuses = ["Active", "Syncing", "Idle", "Error"];
    nodes.forEach(node => {
        const status = statuses[Math.floor(Math.random() * statuses.length)];
        node.querySelector("p").textContent = status;
    });
}

// === AUTO REFRESH EVERY 6 SECONDS ===
setInterval(() => {
    updateFraudGraph();
    updateDataNodes();
}, 6000);

// === INITIALIZE EVERYTHING ===
document.addEventListener("DOMContentLoaded", () => {
    initFraudLineGraph();
    initDataNodes();
});

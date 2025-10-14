// alerts.js

document.addEventListener("DOMContentLoaded", () => {
    const alertsContainer = document.getElementById("alertsContainer");

    // Show loader
    showLoader();

    // Simulate loading alerts (could be from API in real app)
    setTimeout(() => {
        hideLoader();
        loadAlerts();
    }, 2000); // simulate 2s loading time
});

function showLoader() {
    const loader = document.createElement("div");
    loader.id = "loaderOverlay";
    loader.innerHTML = `
        <div class="loader-content">
            <div class="spinner"></div>
            <p>Loading Security Alerts...</p>
        </div>
    `;
    document.body.appendChild(loader);

    const style = document.createElement("style");
    style.textContent = `
        #loaderOverlay {
            position: fixed;
            top: 0; left: 0;
            width: 100vw; height: 100vh;
            background: rgba(255, 255, 255, 0.9);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }
        .loader-content {
            text-align: center;
        }
        .spinner {
            width: 60px;
            height: 60px;
            border: 6px solid #ddd;
            border-top: 6px solid #5D5CDE;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }
        #loaderOverlay p {
            margin-top: 15px;
            font-size: 16px;
            color: #333;
        }
        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
    `;
    document.head.appendChild(style);
}

function hideLoader() {
    const loader = document.getElementById("loaderOverlay");
    if (loader) loader.remove();
}

// Function to load alerts dynamically
function loadAlerts() {
    const alertsContainer = document.getElementById("alertsContainer");
    if (!alertsContainer) return;

    const alertMessages = [
        "Unusual login pattern detected",
        "Multiple failed authentication attempts",
        "Velocity check triggered",
        "Behavioral anomaly identified",
        "Device fingerprint mismatch",
        "Unauthorized access attempt blocked",
        "Suspicious blockchain node connection"
    ];

    // Simulate random alerts every few seconds
    function addAlert() {
        const randomMessage = alertMessages[Math.floor(Math.random() * alertMessages.length)];
        const alert = document.createElement("div");
        alert.className = "p-3 rounded-lg border border-yellow-200 bg-yellow-50 dark:border-yellow-800 dark:bg-yellow-900/20 slide-in";
        alert.innerHTML = `
            <div class="flex items-start space-x-2">
                <div class="w-2 h-2 bg-yellow-500 rounded-full mt-2"></div>
                <div>
                    <div class="text-sm font-medium">${randomMessage}</div>
                    <div class="text-xs text-gray-600 dark:text-gray-400">${new Date().toLocaleTimeString()}</div>
                </div>
            </div>
        `;

        alertsContainer.insertBefore(alert, alertsContainer.firstChild);

        // Limit visible alerts to 10
        while (alertsContainer.children.length > 10) {
            alertsContainer.removeChild(alertsContainer.lastChild);
        }
    }

    // Add initial alerts
    for (let i = 0; i < 3; i++) {
        setTimeout(() => addAlert(), i * 500);
    }

    // Continuously add new alerts every few seconds
    setInterval(addAlert, 4000);
}
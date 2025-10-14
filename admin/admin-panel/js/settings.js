// Handle login form
document.getElementById("loginForm").addEventListener("submit", (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const role = document.getElementById("role").value;
    const password = document.getElementById("password").value.trim();

    if (!username || !role || !password) {
        alert("Please fill all fields.");
        return;
    }

    // Simulated login
    if (password === "1234") {
        document.querySelector(".login-box").style.display = "none";
        document.querySelector(".container").style.display = "block";

        const allowedRoles = ["Admin", "Manager", "CEO"];
        const accessDenied = document.getElementById("accessDenied");
        const settingsPanel = document.getElementById("settingsPanel");

        if (allowedRoles.includes(role)) {
            accessDenied.classList.add("hidden");
            settingsPanel.classList.remove("hidden");
        } else {
            accessDenied.classList.remove("hidden");
            settingsPanel.classList.add("hidden");
        }

        alert(`Welcome ${username}! You logged in as ${role}.`);
        localStorage.setItem("currentUser", username);
        localStorage.setItem("currentRole", role);
    } else {
        alert("Invalid password!. Make sure you enter the correct credentials!.");
    }
});

// Handle sensitivity range display
const sensitivity = document.getElementById("sensitivity");
const sensitivityValue = document.getElementById("sensitivityValue");
if (sensitivity) {
    sensitivity.addEventListener("input", () => {
        sensitivityValue.textContent = sensitivity.value;
    });
}

// Redirect to Decentralized Dashboard
document.getElementById("dashboardTab").addEventListener("click", (e) => {
    e.preventDefault();
    alert("Redirecting to Decentralized Dashboard...");

    const loader = document.createElement("div");
    loader.id = "loaderOverlay";
    loader.innerHTML = `
    <div class="spinner"></div>
    <p>Loading Decentralized Dashboard...</p>
  `;
    document.body.appendChild(loader);

    setTimeout(() => {
        window.location.href = "http://127.0.0.1:5500/decentralized.html";
    }, 2000);
});

// ===== Add New User Form =====
const addUserForm = document.getElementById("addUserForm");
if (addUserForm) {
    addUserForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const newUser = document.getElementById("newUsername").value;
        const newRole = document.getElementById("newUserRole").value;
        const currentUser = localStorage.getItem("currentUser") || "System";

        alert(`New user added: ${newUser} (${newRole})`);

        // Save an audit log entry
        const newLog = {
            timestamp: new Date().toLocaleString(),
            user: currentUser,
            action: "User Added",
            target: newUser,
            outcome: "Success",
            node: "Admin-Panel"
        };

        // Retrieve existing logs from localStorage
        let logs = JSON.parse(localStorage.getItem("auditLogs")) || [];
        logs.push(newLog);
        localStorage.setItem("auditLogs", JSON.stringify(logs));

        addUserForm.reset();
    });
}

// Handle System Config Form
const configForm = document.getElementById("configForm");
if (configForm) {
    configForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const sens = document.getElementById("sensitivity").value;
        const thresh = document.getElementById("threshold").value;
        alert(`Configuration updated:\nSensitivity: ${sens}\nThreshold: ${thresh}`);

        const currentUser = localStorage.getItem("currentUser") || "System";
        const newLog = {
            timestamp: new Date().toLocaleString(),
            user: currentUser,
            action: "Configuration Updated",
            target: "System Settings",
            outcome: "Applied",
            node: "Settings-Panel"
        };

        let logs = JSON.parse(localStorage.getItem("auditLogs")) || [];
        logs.push(newLog);
        localStorage.setItem("auditLogs", JSON.stringify(logs));
    });
}

function addUser() {
    document.getElementById('userAdded').classList.remove('hidden');
    setTimeout(() => {
        document.getElementById('userAdded').classList.add('hidden');
    }, 3000);
}

function deleteUser() {
    alert("User deleted successfully!");
}

function updateSettings() {
    alert("Fraud detection sensitivity updated.");
}

function viewLogs() {
    window.location.href = "http://127.0.0.1:5500/audit-logs.html";
}
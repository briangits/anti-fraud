document.getElementsByClassName("login-btn").addEventListener("submit", function(e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    // 🔹 Example role assignment logic
    let role = "User";
    if (username === "manager") role = "Manager";
    else if (username === "admin") role = "Admin";
    else if (username === "ceo") role = "CEO";

    // 🔹 Store data in localStorage
    localStorage.setItem("loggedInUser", JSON.stringify({ username, role }));

    // 🔹 Redirect to dashboard
    window.location.href = "decentralized.html";
});

document.addEventListener("DOMContentLoaded", () => {
    const userData = localStorage.getItem("loggedInUser");

    if (userData) {
        const user = JSON.parse(userData);
        document.getElementById("userName").textContent = user.username;
        document.getElementById("userRole").textContent = user.role;

        // Optional: color-code the role
        const roleEl = document.getElementById("userRole");
        switch (user.role.toLowerCase()) {
            case "admin":
                roleEl.style.color = "#e74c3c"; // Red
                break;
            case "manager":
                roleEl.style.color = "#f39c12"; // Orange
                break;
            case "ceo":
                roleEl.style.color = "#2ecc71"; // Green
                break;
            default:
                roleEl.style.color = "#3498db"; // Blue for regular user
        }
    } else {
        // Redirect to login if no user is found
        window.location.href = "login.html";
    }
});
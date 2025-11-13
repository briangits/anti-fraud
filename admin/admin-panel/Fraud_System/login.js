const sampleUsers = [
    { username: 'Admin', password: 'Admin123', role: 'admin' },
    { username: 'Manager', password: 'Manager123', role: 'manager' }
];

// Function to validate credentials
function validateCredentials(username, password, role) {
    return sampleUsers.some(user =>
        user.username === username &&
        user.password === password &&
        user.role === role
    );
}

// Handle login form submission
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const loginContainer = document.getElementById('login-container');
    const errorMessage = document.getElementById('login-error');
    const loader = document.getElementById('loader');

    loginForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const role = document.getElementById('role').value;

        // Hide previous error
        errorMessage.classList.add('hidden');

        // Show loader
        loader.classList.remove('hidden');
        loginContainer.classList.add('fade-out');

        // Simulate validation delay
        setTimeout(() => {
            if (validateCredentials(username, password, role)) {
                // Store in localStorage
                localStorage.setItem('username', username);
                localStorage.setItem('userRole', role);

                // Update loader and redirect
                loader.querySelector('p').textContent = 'Redirecting to Dashboard...';
                setTimeout(() => {
                    window.location.href = '/Admin_panel/admin-panel.htm'; // Change to your dashboard URL
                }, 1500);
            } else {
                // Hide loader, show error
                loader.classList.add('hidden');
                loginContainer.classList.remove('fade-out');
                errorMessage.classList.remove('hidden');
            }
        }, 1000); // Simulate server delay
    });
});
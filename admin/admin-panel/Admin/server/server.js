const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const WebSocket = require('ws'); // For connecting to external WebSockets

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

// Mock external WebSocket URLs (replace with real ones, e.g., ws://your-payment-service.com)
const wsUrls = {
    payment: 'ws://mock-payment-service.com', // Simulates payment data
    auth: 'ws://mock-auth-service.com', // Simulates auth data
    loggedIn: 'ws://mock-loggedin-service.com', // Simulates logged-in peers
    votes: 'ws://mock-votes-service.com', // Simulates voting data
    reputation: 'ws://mock-reputation-service.com' // Simulates reputation data
};

// Store aggregated data
let aggregatedData = {
    payment: { flagged: 15, highRisk: 3, alert: 'Suspicious transaction from UserY' },
    auth: { active: 120, failed: 5, topFailedIP: '192.168.1.1' },
    loggedIn: { total: 25, list: ['UserA (Regular) - Online since 10:00 AM', 'UserB (Manager) - Online since 9:45 AM'] },
    votes: { pending: 8, resolved: 20, topPending: 'Fraud report on UserX (Votes: 7/10)' },
    reputation: {
        totalPeers: 30,
        list: [
            { name: 'UserB', score: 15, change: '+5 (Reported fraud, voted yes)' },
            { name: 'UserX', score: -10, change: '-3 (Accused of fraud, majority vote yes)' }
        ]
    }
};

// Function to connect to a WebSocket and gather data
function connectToWebSocket(service, url) {
    const ws = new WebSocket(url);
    ws.on('open', () => console.log(`Connected to ${service} WebSocket`));
    ws.on('message', (data) => {
        const parsed = JSON.parse(data.toString()); // Assume data is JSON
        // Process and update aggregatedData based on service
        if (service === 'payment') {
            aggregatedData.payment = {...aggregatedData.payment, ...parsed };
        } else if (service === 'auth') {
            aggregatedData.auth = {...aggregatedData.auth, ...parsed };
        } else if (service === 'loggedIn') {
            aggregatedData.loggedIn = {...aggregatedData.loggedIn, ...parsed };
        } else if (service === 'votes') {
            // Apply voting logic: If majority "yes" (e.g., >50% and >=10 votes), adjust reputation
            if (parsed.votes && parsed.votes.length >= 10) {
                const yesVotes = parsed.votes.filter(v => v === 'yes').length;
                if (yesVotes > parsed.votes.length / 2) {
                    // Reporter +1, accused -1
                    aggregatedData.reputation.list.forEach(peer => {
                        if (peer.name === parsed.reporter) peer.score += 1;
                        if (peer.name === parsed.accused) peer.score -= 1;
                    });
                }
            }
            aggregatedData.votes = {...aggregatedData.votes, ...parsed };
        } else if (service === 'reputation') {
            aggregatedData.reputation = {...aggregatedData.reputation, ...parsed };
        }
        // Broadcast updated data to all connected clients
        io.emit('updateData', aggregatedData);
    });
    ws.on('error', (err) => console.error(`WebSocket error for ${service}:`, err));
}

// Connect to all WebSockets on server start
Object.entries(wsUrls).forEach(([service, url]) => {
    connectToWebSocket(service, url);
});

// Serve the dynamic HTML
app.get('/', (req, res) => {
            const html = `
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Panel Overview</title>
        <link rel="stylesheet" href="admin-panel.css"> <!-- Link to your CSS file -->
        <script src="/socket.io/socket.io.js"></script>
    </head>
    <body>
        <div class="container">
            <h1 class="header">Admin Panel Overview</h1>
            <!-- Payment Sub-Socket -->
            <div class="card">
                <h3>Payment</h3>
                <p>Flagged Payments: <span id="flagged-payments">${aggregatedData.payment.flagged}</span></p>
                <p>High-Risk Transactions: <span id="high-risk">${aggregatedData.payment.highRisk}</span></p>
                <p class="alert">${aggregatedData.payment.alert}</p>
            </div>
            <!-- Peers Authentication Sub-Socket -->
            <div class="card">
                <h3>Peers Authentication</h3>
                <p>Active Authentications: <span id="active-auth">${aggregatedData.auth.active}</span></p>
                <p>Failed Attempts Today: <span id="failed-auth">${aggregatedData.auth.failed}</span></p>
                <p>Top Failed IP: ${aggregatedData.auth.topFailedIP}</p>
            </div>
            <!-- Peers Logged In Sub-Socket -->
            <div class="card">
                <h3>Peers Logged In</h3>
                <p>Total Online: <span id="online-peers">${aggregatedData.loggedIn.total}</span></p>
                <ul id="online-list">
                    ${aggregatedData.loggedIn.list.map(item => `<li>${item}</li>`).join('')}
                </ul>
            </div>
            <!-- View Peers Votes Sub-Socket -->
            <div class="card">
                <h3>View Peers Votes</h3>
                <p>Pending Votes: <span id="pending-votes">${aggregatedData.votes.pending}</span></p>
                <p>Resolved Issues: <span id="resolved-votes">${aggregatedData.votes.resolved}</span></p>
                <p class="alert">${aggregatedData.votes.topPending}</p>
            </div>
            <!-- Peers Reputation Sub-Socket -->
            <div class="card">
                <h3>Peers Reputation</h3>
                <p>Total Peers: <span id="total-peers">${aggregatedData.reputation.totalPeers}</span></p>
                <table class="reputation-table">
                    <thead>
                        <tr>
                            <th>Peer Name</th>
                            <th>Reputation Score</th>
                            <th>Recent Change</th>
                        </tr>
                    </thead>
                    <tbody id="reputation-list">
                        ${aggregatedData.reputation.list.map(peer => `
                            <tr>
                                <td>${peer.name}</td>
                                <td>${peer.score}</td>
                                <td>${peer.change}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        </div>
        <script>
            const socket = io();
            socket.on('updateData', (data) => {
                // Update DOM elements with new data
                document.getElementById('flagged-payments').textContent = data.payment.flagged;
                document.getElementById('high-risk').textContent = data.payment.highRisk;
                document.querySelector('.card:nth-child(1) .alert').textContent = data.payment.alert;
                document.getElementById('active-auth').textContent = data.auth.active;
                document.getElementById('failed-auth').textContent = data.auth.failed;
                document.querySelector('.card:nth-child(2) p:last-child').textContent = 'Top Failed IP: ' + data.auth.topFailedIP;
                document.getElementById('online-peers').textContent = data.loggedIn.total;
                document.getElementById('online-list').innerHTML = data.loggedIn.list.map(item => '<li>' + item + '</li>').join('');
                document.getElementById('pending-votes').textContent = data.votes.pending;
                document.getElementById('resolved-votes').textContent = data.votes.resolved;
                document.querySelector('.card:nth-child(4) .alert').textContent = data.votes.topPending;
                document.getElementById('total-peers').textContent = data.reputation.totalPeers;
                document.getElementById('reputation-list').innerHTML = data.reputation.list.map(peer => 
                    '<tr><td>' + peer.name + '</td><td>' + peer.score + '</td><td>' + peer.change + '</td></tr>'
                ).join('');
            });
        </script>
    </body>
    </html>
  `;
  res.send(html);
});

// Serve CSS file (assuming admin-panel.css is in the same folder)
app.use(express.static(__dirname));

server.listen(3000, () => {
  console.log('Server running on http://localhost:3000');
});



 // Socket.IO listener for real-time updates (integrates with server)
        const socket = io();
        socket.on('updateData', (data) => {
            if (data.reputation) {
                updateReputationTable(data.reputation);
            }
        });

        // On page load, attach listeners to initial buttons
        document.addEventListener('DOMContentLoaded', () => {
            attachDeleteListeners();
        });

        io.on('connection', (socket) => {
            // Existing code...

            // Handle peer deletion (admin rights check assumed server-side)
            socket.on('deletePeer', (data) => {
                const {
                    peerName,
                    updatedData
                } = data;
                // Validate admin rights (e.g., check JWT or session)
                // if (!isAdmin(socket)) return socket.emit('error', 'Unauthorized');

                // Update global aggregatedData
                aggregatedData.reputation = updatedData;

                // Broadcast to all clients
                io.emit('updateData', aggregatedData);

                console.log(`Peer "${peerName}" deleted by admin.`);
            });
        });
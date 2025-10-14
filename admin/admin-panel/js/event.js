// events.js

document.addEventListener("DOMContentLoaded", () => {
  const activityList = document.querySelector(".activity-list");
  const refreshBtn = document.querySelector(".btn.btn-outline");

  const events = [
    {
      icon: "fas fa-exclamation-triangle",
      color: "danger",
      title: "High-risk transaction blocked",
      message: (id) => `Transaction ID: TX-${id} was flagged and blocked`,
      time: "Just now",
    },
    {
      icon: "fas fa-user-clock",
      color: "warning",
      title: "Multi-factor authentication required",
      message: () =>
        "User login from new device requires additional verification",
      time: "2 minutes ago",
    },
    {
      icon: "fas fa-check-circle",
      color: "success",
      title: "Behavioral analysis completed",
      message: () =>
        "AI model has completed analysis of transaction patterns",
      time: "5 minutes ago",
    },
    {
      icon: "fas fa-server",
      color: "warning",
      title: "Node synchronization in progress",
      message: () => "Blockchain nodes are synchronizing recent ledger data",
      time: "10 minutes ago",
    },
    {
      icon: "fas fa-lock",
      color: "info",
      title: "System encryption updated",
      message: () => "Security certificates successfully renewed across network",
      time: "30 minutes ago",
    },
    {
      icon: "fas fa-user-shield",
      color: "success",
      title: "Admin login verified",
      message: () => "Admin login confirmed with 2FA authentication",
      time: "1 hour ago",
    },
    {
      icon: "fas fa-bug",
      color: "danger",
      title: "Suspicious API activity detected",
      message: () => "Abnormal request rate from IP 192.168.1.42 detected",
      time: "3 hours ago",
    },
  ];

  // Generate random ID
  const randomId = () => Math.floor(10000 + Math.random() * 90000);

  // Generate random time label
  const randomTime = () => {
    const mins = Math.floor(Math.random() * 59) + 1;
    const hours = Math.floor(Math.random() * 5) + 1;
    const options = [
      `${mins} minutes ago`,
      `${hours} hours ago`,
      "Just now",
      "Yesterday",
    ];
    return options[Math.floor(Math.random() * options.length)];
  };

  // Function to refresh activity items
  const refreshEvents = () => {
    activityList.innerHTML = ""; // clear current list

    // Select random subset of 3–5 events
    const shuffled = events.sort(() => 0.5 - Math.random());
    const selected = shuffled.slice(0, Math.floor(Math.random() * 3) + 3);

    selected.forEach((event) => {
      const id = randomId();
      const li = document.createElement("li");
      li.classList.add("activity-item");
      li.innerHTML = `
        <div class="activity-icon ${event.color}">
          <i class="${event.icon}"></i>
        </div>
        <div class="activity-content">
          <h4>${event.title}</h4>
          <p>${event.message(id)}</p>
          <div class="activity-time">${randomTime()}</div>
        </div>
      `;
      activityList.appendChild(li);
    });
  };

  // Refresh manually via button
  refreshBtn.addEventListener("click", () => {
    refreshEvents();
    refreshBtn.querySelector("i").classList.add("fa-spin");
    setTimeout(() => refreshBtn.querySelector("i").classList.remove("fa-spin"), 800);
  });

  // Auto-refresh every 15 seconds
  setInterval(refreshEvents, 15000);

  // Initial load
  refreshEvents();
});

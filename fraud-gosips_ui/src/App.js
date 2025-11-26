import React, { useState } from "react";
import { v4 as uuidv4 } from "uuid";

// Simple mock database
const initialPeers = [
  { id: uuidv4(), name: "Alice", reputation: 50, trust: 50, reports: [] },
  { id: uuidv4(), name: "Bob", reputation: 55, trust: 60, reports: [] },
  { id: uuidv4(), name: "Charlie", reputation: 40, trust: 45, reports: [] }
];

export default function App() {
  const [peers, setPeers] = useState(initialPeers);
  const [messages, setMessages] = useState([]);
  const [selectedPeer, setSelectedPeer] = useState(null);
  const [messageText, setMessageText] = useState("");
  const [reportTarget, setReportTarget] = useState(null);
  const [activeReport, setActiveReport] = useState(null);

  // Send message between peers
  const sendMessage = () => {
    if (!selectedPeer || !messageText) return;

    const newMessage = {
      id: uuidv4(),
      from: "You",
      to: selectedPeer.name,
      text: messageText,
      time: new Date().toLocaleTimeString()
    };

    setMessages([...messages, newMessage]);
    setMessageText("");
  };

  // Report a peer
  const reportPeer = (peer) => {
    setReportTarget(peer);
    const report = {
      id: uuidv4(),
      reporter: "You",
      targetId: peer.id,
      votes: []
    };
    setActiveReport(report);
  };

  // Vote on a fraud report
  const voteOnReport = (vote) => {
    const updatedReport = {
      ...activeReport,
      votes: [...activeReport.votes, vote]
    };
    setActiveReport(updatedReport);

    // If 3 votes reached, finalize
    if (updatedReport.votes.length >= 3) finalizeReport(updatedReport);
  };

  // Finalizing Fraud Report
  const finalizeReport = (report) => {
    const fraudVotes = report.votes.filter((v) => v === "fraud").length;
    const isFraud = fraudVotes >= 2;

    const updatedPeers = peers.map((p) => {
      if (p.id === report.targetId) {
        return {
          ...p,
          reputation: p.reputation - (isFraud ? 10 : 0),
          trust: p.trust - (isFraud ? 10 : 0)
        };
      }
      if (p.name === "You") {
        return {
          ...p,
          reputation: p.reputation + (isFraud ? 5 : -5),
          trust: p.trust + (isFraud ? 5 : -5)
        };
      }
      return p;
    });

    setPeers(updatedPeers);
    setActiveReport(null);
    setReportTarget(null);
  };

  return (
    <div className="p-6 max-w-4xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold text-center">Gosips – Peer Interaction System</h1>

      {/* Peer List */}
      <div className="grid grid-cols-3 gap-4">
        {peers.map((peer) => (
          <div
            key={peer.id}
            className="p-4 rounded-xl shadow bg-white hover:shadow-lg transition cursor-pointer"
          >
            <h3 className="text-xl font-semibold">{peer.name}</h3>
            <p>Reputation: {peer.reputation}</p>
            <p>Trust: {peer.trust}</p>

            <button
              className="mt-2 w-full p-2 bg-blue-500 text-white rounded-lg"
              onClick={() => setSelectedPeer(peer)}
            >
              Chat
            </button>

            <button
              className="mt-2 w-full p-2 bg-red-500 text-white rounded-lg"
              onClick={() => reportPeer(peer)}
            >
              Report
            </button>
          </div>
        ))}
      </div>

      {/* Messaging */}
      {selectedPeer && (
        <div className="p-4 bg-gray-100 rounded-xl">
          <h2 className="text-xl font-bold">Chat with {selectedPeer.name}</h2>

          <div className="mt-4 space-y-2 max-h-40 overflow-auto p-2 bg-white rounded-lg shadow">
            {messages.map((msg) => (
              <div key={msg.id}>
                <strong>{msg.from} → {msg.to}:</strong> {msg.text}
                <span className="text-gray-500 text-sm ml-2">{msg.time}</span>
              </div>
            ))}
          </div>

          <div className="mt-4 flex gap-2">
            <input
              className="flex-1 p-2 rounded-lg border"
              placeholder="Type message..."
              value={messageText}
              onChange={(e) => setMessageText(e.target.value)}
            />

            <button
              onClick={sendMessage}
              className="p-2 bg-green-500 text-white rounded-lg"
            >
              Send
            </button>
          </div>
        </div>
      )}

      {/* Fraud Report Voting */}
      {reportTarget && activeReport && (
        <div className="p-4 bg-yellow-100 rounded-xl">
          <h2 className="text-xl font-bold">Report Review</h2>
          <p className="mt-2">You reported <strong>{reportTarget.name}</strong>.</p>

          <p className="mt-4 font-semibold">Other peers voting...</p>
          <div className="mt-2 flex gap-4">
            <button
              className="p-2 bg-red-500 text-white rounded-lg"
              onClick={() => voteOnReport("fraud")}
            >
              Vote Fraud
            </button>
            <button
              className="p-2 bg-gray-500 text-white rounded-lg"
              onClick={() => voteOnReport("not_fraud")}
            >
              Vote Not Fraud
            </button>
          </div>
        </div>
      )}
    </div>
  );
}


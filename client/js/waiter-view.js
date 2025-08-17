document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://127.0.0.1:8080/api/orders";

  const notReadyContainer = document.getElementById("not-ready-items");
  const readyContainer = document.getElementById("ready-items");
  const servedContainer = document.getElementById("served-items");

  async function fetchOrders() {
    try {
      const res = await fetch(API_BASE);
      if (!res.ok) throw new Error("Failed to fetch orders");
      const orders = await res.json();
      renderItems(flattenOrderItems(orders));
    } catch (e) {
      console.error(e);
    }
  }

  // Attach order.createdAt, tableNumber, waiterName to each item
  function flattenOrderItems(orders) {
    return orders.flatMap((order) =>
      (order.items || []).map((item) => ({
        itemId: item.id,
        name: item.menuItem?.name ?? "Unknown item",
        quantity: item.quantity,
        status: item.status, // PENDING | READY | SERVED
        notes: item.notes || "",
        createdAt: order.createdAt,
        tableNumber: order.table?.tableNumber ?? "—",
        waiterName: order.staff?.name ?? "—",
        orderId: order.id,
        orderNumber: order.orderNumber,
      }))
    );
  }

  function minutesAgo(ts) {
    const ms = Date.now() - new Date(ts).getTime();
    const mins = Math.max(0, Math.floor(ms / 60000));
    return mins === 0 ? "just now" : `${mins} min ago`;
  }

  function renderItems(items) {
    notReadyContainer.innerHTML = "";
    readyContainer.innerHTML = "";
    servedContainer.innerHTML = "";

    items.forEach((it) => {
      const card = document.createElement("div");
      card.className = "order-card";

      card.innerHTML = `
        <div class="order-top">
          <div class="badges">
            <span class="badge">Table ${it.tableNumber}</span>
            <span class="badge">${escapeHtml(it.waiterName)}</span>
            <span class="badge">#${shortOrder(it.orderNumber)}</span>
          </div>
          <span class="timeago">${minutesAgo(it.createdAt)}</span>
        </div>

        <div class="order-main">
          <p class="item-name">${escapeHtml(it.name)}</p>
          <p class="qty">${it.quantity}x</p>
        </div>

        ${it.notes ? `<p class="notes">Note: ${escapeHtml(it.notes)}</p>` : ""}

        <div class="order-actions">
          <button class="move-left" ${
            it.status === "PENDING" ? "disabled" : ""
          }>⬅</button>
          <button class="move-right" ${
            it.status === "SERVED" ? "disabled" : ""
          }>➡</button>
          <button class="cancel-btn">✖</button>
        </div>
      `;

      // Wire actions
      card.querySelector(".move-left").addEventListener("click", () => {
        const newStatus = prevStatus(it.status);
        if (newStatus !== it.status) updateStatus(it.itemId, newStatus);
      });

      card.querySelector(".move-right").addEventListener("click", () => {
        const newStatus = nextStatus(it.status);
        if (newStatus !== it.status) updateStatus(it.itemId, newStatus);
      });

      card.querySelector(".cancel-btn").addEventListener("click", async () => {
        const ok = confirm("Cancel this item?");
        if (ok) await cancelItem(it.itemId);
      });

      // Drop into correct lane
      if (it.status === "PENDING") notReadyContainer.appendChild(card);
      else if (it.status === "READY") readyContainer.appendChild(card);
      else if (it.status === "SERVED") servedContainer.appendChild(card);
    });
  }

  // Status flow: PENDING → READY → SERVED
  function prevStatus(s) {
    if (s === "READY") return "PENDING";
    if (s === "SERVED") return "READY";
    return "PENDING";
  }
  function nextStatus(s) {
    if (s === "PENDING") return "READY";
    if (s === "READY") return "SERVED";
    return "SERVED";
  }

  async function updateStatus(itemId, newStatus) {
    try {
      const res = await fetch(`${API_BASE}/items/${itemId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: newStatus }),
      });
      if (!res.ok) throw new Error("Failed to update status");
      await fetchOrders();
    } catch (e) {
      console.error(e);
      alert("Could not update status");
    }
  }

  async function cancelItem(itemId) {
    try {
      const res = await fetch(`${API_BASE}/items/${itemId}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to cancel item");
      await fetchOrders();
    } catch (e) {
      console.error(e);
      alert("Could not cancel item");
    }
  }

  function shortOrder(orderNumber = "") {
    // show last 6 chars for quick ID
    return orderNumber.slice(-6);
  }

  function escapeHtml(str) {
    return String(str)
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }

  function connectWebSocket() {
    const socket = new SockJS("http://127.0.0.1:8080/ws");
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log("Connected to WebSocket!");

      // Subscribe to whole orders
      stompClient.subscribe("/topic/orders", (msg) => {
        const order = JSON.parse(msg.body);
        console.log("Order update:", order);
        fetchOrders(); // simplest: refetch all
      });

      // Subscribe to individual item updates
      stompClient.subscribe("/topic/order-items", (msg) => {
        const item = JSON.parse(msg.body);
        console.log("Item update:", item);
        fetchOrders();
      });
    });
  }

  // Initial load
  fetchOrders();
  connectWebSocket();
});

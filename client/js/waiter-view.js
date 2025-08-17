document.addEventListener("DOMContentLoaded", () => {
  const notReadyContainer = document.getElementById("not-ready-items");
  const readyContainer = document.getElementById("ready-items");
  const servedContainer = document.getElementById("served-items");

  async function fetchOrders() {
    try {
      let response = await fetch("http://localhost:8080/api/orders");
      if (!response.ok) throw new Error("Backend not available");

      const orders = await response.json();
      renderOrders(flattenOrderItems(orders));
    } catch (err) {
      console.error("Error fetching orders:", err);
    }
  }

  function flattenOrderItems(orders) {
    // Flatten: order -> order.items while attaching order.createdAt
    return orders.flatMap(order =>
      order.items.map(item => ({
        id: item.id,
        name: item.menuItem.name,
        quantity: item.quantity,
        createdAt: order.createdAt, // attach from order
        status: item.status
      }))
    );
  }

  function renderOrders(items) {
    notReadyContainer.innerHTML = "";
    readyContainer.innerHTML = "";
    servedContainer.innerHTML = "";

    items.forEach(item => {
      const card = document.createElement("div");
      card.className = "order-card";

      const minutesAgo = Math.floor(
        (Date.now() - new Date(item.createdAt)) / 60000
      );

      card.innerHTML = `
        <div class="order-info">
          <p class="order-name">${item.name} (${item.quantity}x)</p>
          <p class="order-meta">${minutesAgo} min ago</p>
        </div>
        <div class="order-actions">
          <button class="move-left">⬅</button>
          <button class="move-right">➡</button>
          <button class="cancel-btn">✖</button>
        </div>
      `;

      // Button events
      card.querySelector(".move-left").addEventListener("click", () =>
        updateStatus(item.id, prevStatus(item.status))
      );
      card.querySelector(".move-right").addEventListener("click", () =>
        updateStatus(item.id, nextStatus(item.status))
      );
      card.querySelector(".cancel-btn").addEventListener("click", () =>
        cancelItem(item.id)
      );

      if (item.status === "PENDING") notReadyContainer.appendChild(card);
      else if (item.status === "READY") readyContainer.appendChild(card);
      else if (item.status === "SERVED") servedContainer.appendChild(card);
    });
  }

  function prevStatus(status) {
    if (status === "READY") return "PENDING";
    if (status === "SERVED") return "READY";
    return "PENDING";
  }

  function nextStatus(status) {
    if (status === "PENDING") return "READY";
    if (status === "READY") return "SERVED";
    return "SERVED";
  }

  async function updateStatus(itemId, newStatus) {
    try {
      await fetch(`http://localhost:8080/api/order-items/${itemId}/status`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: newStatus })
      });
      fetchOrders();
    } catch (err) {
      console.error("Error updating status:", err);
    }
  }

  async function cancelItem(itemId) {
    try {
      await fetch(`http://localhost:8080/api/order-items/${itemId}`, {
        method: "DELETE"
      });
      fetchOrders();
    } catch (err) {
      console.error("Error cancelling item:", err);
    }
  }

  // Poll every 5s
  fetchOrders();
  setInterval(fetchOrders, 5000);
});

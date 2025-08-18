// table-reservation.js
const apiUrl = "http://localhost:8080/api/tables";

let editingTableId = null;

// Status/Color mapping
const statusColors = {
  "AVAILABLE": "bg-success",
  "OCCUPIED": "bg-danger",
  "RESERVED": "bg-warning text-dark",
  "OUT_OF_SERVICE": "bg-black"
};

// ========== Render Tables ==========
function renderTables(tables) {
  const container = document.getElementById("tables-container");
  container.innerHTML = "";

  tables.sort((a, b) => a.id - b.id);

  tables.forEach(table => {
    const badgeClass = statusColors[table.status] || "bg-secondary";

    container.innerHTML += `
      <div class="col-md-4 mb-3">
        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex justify-content-between">
            <div>
              <h5 class="card-title">Table ${table.tableNumber}</h5>
              <p class="card-text">
                <strong>Seats:</strong> ${table.seats} <br>
                
                <strong>Section:</strong> ${table.section? table.section : "N/A"} <br>
              </p>

              <!-- Dropdown status badge -->
              <div class="dropdown">
                <button class="badge ${badgeClass} dropdown-toggle border-0"
                        type="button" data-bs-toggle="dropdown"
                        aria-expanded="false" id="status-${table.id}">
                  ${table.status}
                </button>
                <ul class="dropdown-menu">
                  <li><button class="dropdown-item status-option"
                              data-id="${table.id}" data-status="AVAILABLE">AVAILABLE</button></li>
                  <li><button class="dropdown-item status-option"
                              data-id="${table.id}" data-status="OCCUPIED">OCCUPIED</button></li>
                  <li><button class="dropdown-item status-option"
                              data-id="${table.id}" data-status="RESERVED">RESERVED</button></li>
                  <li><button class="dropdown-item status-option"
                              data-id="${table.id}" data-status="OUT_OF_SERVICE">OUT OF SERVICE</button></li>
                </ul>
              </div>
            </div>

            <!-- Right Column (buttons) -->
            <div class="d-flex flex-column ms-3">
              <button class="btn btn-sm btn-outline-primary mb-2 edit-btn " data-id="${table.id}">✏️ Edit</button>
              <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${table.id}">🗑️ Delete</button>
            </div>
          </div>
        </div>
      </div>
    `;
  });

  attachTableListeners(); // rebind events
}

// ========== Attach Listeners ==========
function attachTableListeners() {
  // Status dropdown (PATCH)
  document.querySelectorAll(".status-option").forEach(option => {
    option.addEventListener("click", async e => {
      const id = e.target.dataset.id;
      const newStatus = e.target.dataset.status;

      try {
        const response = await fetch(`${apiUrl}/${id}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ status: newStatus })
        });

        if (!response.ok) throw new Error("Failed to update status");

        // update badge
        const badge = document.getElementById("status-" + id);
        badge.textContent = newStatus;
        badge.className = "badge dropdown-toggle border-0 " +
          (statusColors[newStatus] || "bg-secondary");

        // close dropdown
        const dropdown = bootstrap.Dropdown.getInstance(badge);
        if (dropdown) dropdown.hide();

      } catch (err) {
        console.error(err);
      }
    });
  });

  // Edit button (GET + open modal)
  document.querySelectorAll(".edit-btn").forEach(btn => {
    btn.addEventListener("click", async (e) => {
      editingTableId = e.target.dataset.id;
      document.getElementById("tableModalLabel").textContent = "Edit Table";

      try {
        const response = await fetch(`${apiUrl}/${editingTableId}`);
        if (!response.ok) throw new Error("Failed to fetch table");
        const table = await response.json();

        // fill modal form
        document.getElementById("tableNumber").value = table.tableNumber;
        document.getElementById("seats").value = table.seats;
        document.getElementById("section").value = table.section;
        document.getElementById("status").value = table.status;

        new bootstrap.Modal(document.getElementById("tableModal")).show();
      } catch (err) {
        console.error(err);
      }
    });
  });

  // Delete button (DELETE)
  document.querySelectorAll(".delete-btn").forEach(btn => {
    btn.addEventListener("click", async (e) => {
      const id = e.target.dataset.id;
      if (!confirm("Are you sure you want to delete this table?")) return;

      try {
        const response = await fetch(`${apiUrl}/${id}`, { method: "DELETE" });
        if (!response.ok) throw new Error("Failed to delete table");

        console.log(`Table ${id} deleted`);
        fetchTables(); // refresh list
      } catch (err) {
        console.error(err);
      }
    });
  });
}

// ========== Fetch Tables ==========
async function fetchTables(status = "ALL") {
  let url = apiUrl;
  if (status !== "ALL") url += `?status=${status}`;

  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error("Failed to fetch tables");

    const data = await response.json();
    renderTables(data);
  } catch (err) {
    console.error(err);
  }
}

// ========== Add New Table ==========
document.getElementById("addTableBtn").addEventListener("click", () => {
  editingTableId = null;
  document.getElementById("tableModalLabel").textContent = "Add New Table";
  document.getElementById("tableForm").reset();
  new bootstrap.Modal(document.getElementById("tableModal")).show();
});

// ========== Save (Add or Edit) ==========
document.getElementById("saveTableBtn").addEventListener("click", async () => {
  const tableData = {
    tableNumber: document.getElementById("tableNumber").value,
    seats: parseInt(document.getElementById("seats").value, 10),
    section: document.getElementById("section").value,
    status: document.getElementById("status").value
  };

  try {
    let response;
    if (editingTableId) {
      // PUT for edit
      response = await fetch(`${apiUrl}/${editingTableId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tableData)
      });
    } else {
      // POST for new table
      response = await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tableData)
      });
    }

    if (!response.ok) throw new Error("Save failed");

    bootstrap.Modal.getInstance(document.getElementById("tableModal")).hide();
    fetchTables(); // refresh
  } catch (err) {
  }
});

// ========== Filter by Status ==========
document.getElementById("statusFilter").addEventListener("change", e => {
  fetchTables(e.target.value);
});

// ========== WebSocket Setup ==========
function connectSocket() {
  const socket = new SockJS("http://localhost:8080/ws");
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    console.log("Connected to websocket");

    // Subscribe to table updates
    stompClient.subscribe("/topic/tables", (message) => {
      fetchTables(document.getElementById("statusFilter").value);
    });
  });
}



// Initial load
fetchTables();
connectSocket();

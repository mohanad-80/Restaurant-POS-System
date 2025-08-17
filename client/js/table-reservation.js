const apiUrl = "http://localhost:8080/tables";

// Status/Color mapping
const statusColors = {
  "AVAILABLE": "bg-success",
  "OCCUPIED": "bg-danger",
  "RESERVED": "bg-warning text-dark",
  "OUT_OF_SERVICE": "bg-black"
};

// Render tables dynamically
function renderTables(tables) {
  const container = document.getElementById("tables-container");
  container.innerHTML = "";

  tables.forEach(table => {
    const card = document.createElement("div");
    card.className = "col-md-4 mb-3";

    const badgeClass = statusColors[table.status] || "bg-secondary";

    card.innerHTML = `
      <div class="card h-100 shadow-sm">
        <div class="card-body d-flex justify-content-between">
          
          <!-- Left Column (table info + status) -->
          <div>
            <h5 class="card-title">Table ${table.tableNumber}</h5>
            <p class="card-text">
              <strong>Seats:</strong> ${table.seats} <br>
              <strong>Section:</strong> ${table.section}
            </p>

            <!-- Dropdown status badge -->
            <div class="dropdown">
              <button class="badge ${badgeClass} dropdown-toggle border-0" 
                      type="button" data-bs-toggle="dropdown" 
                      aria-expanded="false" id="status-${table.id}">
                ${table.status}
              </button>
              <ul class="dropdown-menu">
                <li><button class="dropdown-item status-option" data-id="${table.id}" data-status="AVAILABLE">AVAILABLE</button></li>
                <li><button class="dropdown-item status-option" data-id="${table.id}" data-status="OCCUPIED">OCCUPIED</button></li>
                <li><button class="dropdown-item status-option" data-id="${table.id}" data-status="RESERVED">RESERVED</button></li>
                <li><button class="dropdown-item status-option" data-id="${table.id}" data-status="OUT_OF_SERVICE">OUT OF SERVICE</button></li>
              </ul>
            </div>
          </div>

          <!-- Right Column (buttons) -->
          <div class="d-flex flex-column ms-3">
            <button class="btn btn-sm btn-outline-primary mb-2 edit-btn" data-id="${table.id}">✏️ Edit</button>
            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${table.id}">🗑️ Delete</button>
          </div>
        </div>
      </div>
    `;

    container.appendChild(card);
  });

  // Handle status change (with PATCH call)
  document.querySelectorAll(".status-option").forEach(option => {
    option.addEventListener("click", async e => {
      const id = e.target.dataset.id;
      const newStatus = e.target.dataset.status;

      try {
        const response = await fetch(`${apiUrl}/${id}`, {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({ status: newStatus })
        });

        if (!response.ok) {
          throw new Error("Failed to update status");
        }

        // update status badge
        const badge = document.getElementById("status-" + id);
        badge.textContent = newStatus;
        badge.className = "badge dropdown-toggle border-0 " + (statusColors[newStatus] || "bg-secondary");

        // close dropdown
        const dropdown = bootstrap.Dropdown.getInstance(badge);
        if (dropdown) dropdown.hide();

        console.log(`Status for table ${id} updated to ${newStatus}`);
      } catch (err) {
        console.error(err);
        alert("Error updating table status!");
      }
    });
  });
}

// Fetch tables from API
async function fetchTables(status = "ALL") {
  let url = apiUrl;
  if (status !== "ALL") {
    url += `?status=${status}`;
  }

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error("Failed to fetch tables");
    }
    const data = await response.json();
    renderTables(data);
  } catch (err) {
    console.error(err);
    alert("Error loading tables from API!");
  }
}

// Initial load
fetchTables();

// Filter handler
document.getElementById("statusFilter").addEventListener("change", e => {
  fetchTables(e.target.value);
});

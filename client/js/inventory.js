const API = "http://localhost:8080/api/v1/inventory";

const form = document.getElementById("form");
const nameEl = document.getElementById("name");
const unitsEl = document.getElementById("available_units");
const unitEl = document.getElementById("unit");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    name: nameEl.value.trim(),
    available_units: parseFloat(unitsEl.value),
    unit: unitEl.value.trim(),
    menuItemIds: []
  };
  const res = await fetch(API, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (res.ok) location.href = "inventory-list.html";
});

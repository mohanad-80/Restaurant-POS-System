const API = (path="") => `http://localhost:8080/api/v1/inventory${path}`;

const tbody = document.getElementById("tbody");
const search = document.getElementById("search");
const downloadBtn = document.getElementById("downloadCsv");
const uploadInput = document.getElementById("uploadCsv");

async function load() {
  const res = await fetch(API());
  const data = await res.json();
  render(data);
  search.addEventListener("input", () => {
    const q = search.value.toLowerCase();
    render(data.filter(r => r.name.toLowerCase().includes(q)));
  });
}

function render(rows) {
  tbody.innerHTML = rows.map(r => `
    <tr>
      <td>${escapeHtml(r.name)}</td>
      <td>${r.available_units}</td>
      <td>${escapeHtml(r.unit)}</td>
    </tr>
  `).join("");
}

downloadBtn.addEventListener("click", async () => {
  const res = await fetch(API("/export"));
  const blob = await res.blob();
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url; a.download = "inventory.csv"; a.click();
  URL.revokeObjectURL(url);
});

uploadInput.addEventListener("change", async () => {
  if (!uploadInput.files.length) return;
  const fd = new FormData();
  fd.append("file", uploadInput.files[0]);
  const res = await fetch(API("/import"), { method: "POST", body: fd });
  if (res.ok) load();
  uploadInput.value = "";
});

function escapeHtml(s){ return (s??"").replace(/[&<>"']/g, m=>({ "&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#039;" }[m])); }

load();


const API_BASE = "http://localhost:8080/api"; 

const CATS_URL = `${API_BASE}/categories`;
const MENU_URL = `${API_BASE}/menu`;


const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

function showToast(msg, type = "success") {
  const el = $("#toast");
  el.textContent = msg;
  el.className = `toast show ${type}`;
  setTimeout(() => { el.className = "toast"; }, 2500);
}
function toQuery(params) {
  const usp = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && `${v}`.length) usp.set(k, v);
  });
  return usp.toString();
}
async function handleJson(res) {
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || `HTTP ${res.status}`);
  }
  // try parse json; fall back to text
  const ct = res.headers.get("content-type") || "";
  if (ct.includes("application/json")) return res.json();
  return res.text();
}


$$(".tab-btn").forEach((btn) => {
  btn.addEventListener("click", () => {
    $$(".tab-btn").forEach((b) => b.classList.remove("active"));
    $$(".tab").forEach((t) => t.classList.remove("active"));
    btn.classList.add("active");
    const target = btn.getAttribute("data-target");
    document.getElementById(target).classList.add("active");
  });
});


async function loadCategories() {
  try {
    const data = await handleJson(await fetch(CATS_URL));
    const tbody = $("#categoryTable");
    const menuCatSelect = $("#menuCategory");
    const filterCatSelect = $("#filterCategory");

    tbody.innerHTML = "";
    menuCatSelect.innerHTML = "";
    filterCatSelect.innerHTML = `<option value="">(All)</option>`;

    data.forEach((c) => {

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${c.id}</td>
        <td>${c.name}</td>
        <td>
          <button class="btn" data-act="edit" data-id="${c.id}" data-name="${c.name}">Edit</button>
          <button class="btn danger" data-act="del" data-id="${c.id}">Delete</button>
        </td>
      `;
      tbody.appendChild(tr);


      const opt1 = document.createElement("option");
      opt1.value = c.id; opt1.textContent = c.name;
      menuCatSelect.appendChild(opt1);

      const opt2 = document.createElement("option");
      opt2.value = c.id; opt2.textContent = c.name;
      filterCatSelect.appendChild(opt2);
    });


    tbody.onclick = async (e) => {
      const btn = e.target.closest("button"); if (!btn) return;
      const id = btn.getAttribute("data-id");
      const act = btn.getAttribute("data-act");
      if (act === "edit") {
        $("#categoryId").value = id;
        $("#categoryName").value = btn.getAttribute("data-name");
        $("#categoryName").focus();
      } else if (act === "del") {
        if (!confirm("Delete this category?")) return;
        try {
          await handleJson(await fetch(`${CATS_URL}/${id}`, { method: "DELETE" }));
          showToast("Category deleted");
          await loadCategories();
          await loadMenu(); 
        } catch (err) {
          showToast(err.message, "error");
        }
      }
    };
  } catch (err) {
    showToast(`Failed to load categories: ${err.message}`, "error");
  }
}

$("#categoryForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = $("#categoryId").value.trim();
  const name = $("#categoryName").value.trim();
  if (!name) return;

  try {
    if (id) {
      await handleJson(
        await fetch(`${CATS_URL}/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name })
        })
      );
      showToast("Category updated");
    } else {
      await handleJson(
        await fetch(CATS_URL, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name })
        })
      );
      showToast("Category added");
    }
    e.target.reset();
    await loadCategories();
  } catch (err) {
    showToast(err.message, "error");
  }
});
$("#categoryResetBtn").addEventListener("click", () => {
  $("#categoryForm").reset();
  $("#categoryId").value = "";
});


async function loadMenu() {
  try {
    const catId = $("#filterCategory").value;
    const status = $("#filterStatus").value;
    const qs = toQuery({ categoryId: catId || undefined, status: status || undefined });
    const url = qs ? `${MENU_URL}?${qs}` : MENU_URL;

    const data = await handleJson(await fetch(url));
    const tbody = $("#menuTable");
    tbody.innerHTML = "";

    data.forEach((m) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${m.id}</td>
        <td>${m.name}</td>
        <td>${m.price}</td>
        <td>${m.preparation_time}</td>
        <td>${m.status}</td>
        <td>${m.category ? m.category.name : ""}</td>
        <td>${m.image_path ? `<img class="menu-thumb" src="${m.image_path}" alt="image">` : "—"}</td>
        <td class="row-actions">
          <button class="btn" data-act="edit" data-id="${m.id}">Edit</button>
          <button class="btn danger" data-act="del" data-id="${m.id}">Delete</button>
          <button class="btn success" data-act="upload" data-id="${m.id}">Upload Image</button>
        </td>
      `;
      tbody.appendChild(tr);
    });


    tbody.onclick = (e) => {
      const btn = e.target.closest("button"); if (!btn) return;
      const id = btn.getAttribute("data-id");
      const act = btn.getAttribute("data-act");
      if (act === "edit") return prefillMenuForm(id);
      if (act === "del") return deleteMenuItem(id);
      if (act === "upload") return uploadImage(id);
    };
  } catch (err) {
    showToast(`Failed to load menu: ${err.message}`, "error");
  }
}


$("#menuForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = $("#menuId").value.trim();
  const body = {
    name: $("#menuName").value.trim(),
    price: $("#menuPrice").value,
    preparation_time: $("#menuPrep").value,
    status: $("#menuStatus").value,
    categoryId: $("#menuCategory").value
  };

  try {
    if (id) {

      const updates = {
        name: body.name,
        price: body.price,
        preparation_time: body.preparation_time,
        status: body.status,
        category_id: body.categoryId
      };
      const qs = toQuery(updates);
      await handleJson(await fetch(`${MENU_URL}/${id}?${qs}`, { method: "PUT" }));
      showToast("Menu item updated");
    } else {

      await handleJson(await fetch(MENU_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      }));
      showToast("Menu item added");
    }
    e.target.reset();
    $("#menuId").value = "";
    await loadMenu();
  } catch (err) {
    showToast(err.message, "error");
  }
});

$("#menuResetBtn").addEventListener("click", () => {
  $("#menuForm").reset();
  $("#menuId").value = "";
});

async function prefillMenuForm(id) {
  try {
    const item = await handleJson(await fetch(`${MENU_URL}/${id}`));
    $("#menuId").value = item.id;
    $("#menuName").value = item.name ?? "";
    $("#menuPrice").value = item.price ?? "";
    $("#menuPrep").value = item.preparation_time ?? "";
    $("#menuStatus").value = item.status ?? "AVAILABLE";
    $("#menuCategory").value = item.category ? item.category.id : "";

    $("#menuName").focus();
  } catch (err) {
    showToast(err.message, "error");
  }
}


async function deleteMenuItem(id) {
  if (!confirm("Delete this menu item?")) return;
  try {
    await handleJson(await fetch(`${MENU_URL}/${id}`, { method: "DELETE" }));
    showToast("Menu item deleted");
    await loadMenu();
  } catch (err) {
    showToast(err.message, "error");
  }
}

async function uploadImage(id) {
  const input = document.createElement("input");
  input.type = "file";
  input.accept = "image/*";
  input.onchange = async () => {
    const file = input.files[0];
    if (!file) return;
    const fd = new FormData();
    fd.append("image", file);
    try {
      await handleJson(await fetch(`${MENU_URL}/uploadImage/${id}`, {
        method: "POST",
        body: fd
      }));
      showToast("Image uploaded");
      await loadMenu();
    } catch (err) {
      showToast(err.message, "error");
    }
  };
  input.click();
}


$("#applyFilters").addEventListener("click", loadMenu);
$("#clearFilters").addEventListener("click", () => {
  $("#filterCategory").value = "";
  $("#filterStatus").value = "";
  loadMenu();
});


(async function init() {
  await loadCategories();
  await loadMenu();
})();

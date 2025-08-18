const form = document.getElementById("loginForm");
const message = document.getElementById("message");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  message.textContent = "";
  message.classList.remove("success");

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const res = await fetch("http://127.0.0.1:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const data = await res.json();

    if (!res.ok) {
      message.textContent = data.message || "Login failed";
      return;
    }

    document.cookie = `token=${data.token}; path=/; max-age=${
      60 * 60 * 24
    }; secure`;
    document.cookie = `role=${data.role}; path=/; max-age=${
      60 * 60 * 24
    }; secure`;
    message.textContent = "Login successful!";
    message.classList.add("success");

    // Example: redirect after login
    setTimeout(() => {
      switch (data.role) {
        case "ADMIN":
          window.location.href = "./../partials/MenuItem.html";
          break;

        case "WAITER":
          window.location.href = "./../partials/waiter-view.html";
          break;
        case "MANAGER":
          window.location.href = "./../partials/Inventory.html";
          break;
        case "KITCHEN":
          window.location.href = "./../partials/inventory-list.html";
          break;
        case "CASHIER":
          window.location.href = "./../partials/order-entry.html";
          break;
        case "CUSTOMER":
          window.location.href = "./../partials/waiter-view.html";
          break;
        default:
          break;
      }
    }, 1000);
  } catch (err) {
    message.textContent = "Network error";
  }
});

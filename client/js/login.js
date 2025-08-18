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
    message.textContent = "Login successful!";
    message.classList.add("success");

    // Example: redirect after login
    setTimeout(() => {
      window.location.href = "dashboard.html";
    }, 1000);
  } catch (err) {
    message.textContent = "Network error";
  }
});

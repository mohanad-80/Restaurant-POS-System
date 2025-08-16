document.addEventListener("DOMContentLoaded", function () {
  const productsContainer = document.querySelector(".products");
  const cartHeader = document.querySelector(".cart-header");
  const cartContent = document.querySelector(".cart-content");
  const cartTotalDetails = document.querySelector(".cart-total-details");
  const carbonNeutral = document.querySelector(".carbon-neutral");
  const emptyCartDiv = document.createElement("div");
  emptyCartDiv.className = "empty-cart";
  emptyCartDiv.innerHTML = `
        <img class="empty-cart-img" src="../assets/images/illustration-empty-cart.svg" alt="empty cart illustration" />
        <p class="empty-cart-text">Your added items will appear here</p>
    `;
  const cartTotalPrice = document.querySelector(".cart-total-price");
  const confirmOrderBtn = document.getElementById("confirm-order-btn");
  const orderMeta = document.querySelector(".order-meta");
  const tableIdInput = document.getElementById("tableId");
  const staffIdInput = document.getElementById("staffId");
  const modal = document.getElementById("modal");
  const orderDetails = document.querySelector(".order-details");
  const startNewOrderBtn = document.querySelector(".start-new-order-btn");

  // State
  let cart = [];
  let products = [];
  let activeCategories = [];

  async function init() {
    try {
      const response = await fetch("../data/data.json");
      products = await response.json();
      initializeCategoryFilter();
      renderProducts();
    } catch (error) {
      console.error("Error loading products:", error);
    }

    renderCart();

    confirmOrderBtn.addEventListener("click", confirmOrder);
    startNewOrderBtn.addEventListener("click", startNewOrder);
  }

  function initializeCategoryFilter() {
    const categories = [...new Set(products.map((p) => p.category))];
    const filterChipsContainer = document.querySelector(".filter-chips");

    if (!filterChipsContainer) return;

    filterChipsContainer.innerHTML = "";

    categories.forEach((category) => {
      const chip = document.createElement("button");

      chip.type = "button";
      chip.classList.add("filter-chip");
      chip.innerText = category;

      if (activeCategories.includes(category)) {
        chip.classList.add("active");
      }

      filterChipsContainer.appendChild(chip);
      chip.addEventListener("click", handleFilterChange);
    });
  }

  function handleFilterChange(event) {
    const clickedCategory = event.target.innerText;

    if (event.target.classList.contains("active")) {
      const idx = activeCategories.indexOf(clickedCategory);
      activeCategories.splice(idx, 1);

      event.target.classList.remove("active");
    } else {
      activeCategories.push(clickedCategory);

      event.target.classList.add("active");
    }

    updateUI();
  }

  function renderProducts() {
    productsContainer.innerHTML = "";

    const filteredProducts = products.filter((product) => {
      if (activeCategories.length === 0) return true;

      return activeCategories.includes(product.category);
    });

    filteredProducts.forEach((product) => {
      const productCard = document.createElement("div");
      productCard.className = "product-card";

      const cartItem = cart.find((item) => item.name === product.name);
      if (cartItem) {
        productCard.classList.add("selected-product");
      }

      productCard.innerHTML = `
                <img class="product-image" src="${product.image}" alt="${
        product.name
      }" />
                ${
                  cartItem
                    ? `
                    <div class="quantity-btn">
                        <button class="quantity-decrease">
                            <img src="../assets/images/icon-decrement-quantity.svg" alt="decrement product quantity" />
                        </button>
                        <span class="quantity-value">${cartItem.quantity}</span>
                        <button class="quantity-increase">
                            <img src="../assets/images/icon-increment-quantity.svg" alt="increment product quantity" />
                        </button>
                    </div>
                `
                    : ` ${
                        product.status === "OUT_OF_STOCK"
                          ? `<div class="add-to-cart-btn">
                              <button disabled style="opacity:0.7; cursor:not-allowed;>
                                  <img src="../assets/images/icon-add-to-cart.svg" alt="add to cart icon" />
                                  <span>Out of stock</span>
                              </button>
                          </div>`
                          : `<div class="add-to-cart-btn">
                            <button>
                                <img src="../assets/images/icon-add-to-cart.svg" alt="add to cart icon" />
                                <span>Add to cart</span>
                            </button>
                          </div>`
                      }
                    
                `
                }
                <h5 class="product-category">${product.category}</h5>
                <h4 class="product-name">${product.name}</h4>
                <p class="product-price">$${product.price.toFixed(2)}</p>
            `;

      productsContainer.appendChild(productCard);

      if (cartItem) {
        const decreaseBtn = productCard.querySelector(".quantity-decrease");
        const increaseBtn = productCard.querySelector(".quantity-increase");

        decreaseBtn.addEventListener("click", () =>
          updateQuantity(product, -1)
        );
        increaseBtn.addEventListener("click", () => updateQuantity(product, 1));
      } else {
        const addToCartBtn = productCard.querySelector(
          ".add-to-cart-btn button"
        );
        addToCartBtn.addEventListener("click", () => addToCart(product));
      }
    });
  }

  function renderCart() {
    const itemCount = cart.reduce((total, item) => total + item.quantity, 0);
    cartHeader.textContent = `Your Cart (${itemCount})`;

    cartContent.innerHTML = "";

    if (cart.length === 0) {
      cartContent.appendChild(emptyCartDiv);

      cartTotalDetails.style.display = "none";
      carbonNeutral.style.display = "none";
      confirmOrderBtn.style.display = "none";
      orderMeta.style.display = "none";
    } else {
      orderMeta.style.display = "flex";
      confirmOrderBtn.style.display = "block";
      cartTotalDetails.style.display = "flex";
      carbonNeutral.style.display = "flex";

      cart.forEach((item) => {
        const cartItemDiv = document.createElement("div");
        cartItemDiv.className = "cart-item";

        cartItemDiv.innerHTML = `
                    <div class="item-details">
                        <h5 class="item-name">${item.name}</h5>
                        <div class="item-price-details">
                            <p class="item-quantity">${item.quantity}x</p>
                            <p class="item-price">@ $${item.price.toFixed(
                              2
                            )}</p>
                            <p class="item-total-price">$${(
                              item.price * item.quantity
                            ).toFixed(2)}</p>
                        </div>
                        <input type="text" placeholder="Notes (optional)" 
                          class="item-note" data-name="${item.name}" 
                          value="${item.notes || ""}">
                    </div>
                    <button aria-label="remove item from cart" class="remove-item-btn">
                        <img src="../assets/images/icon-remove-item.svg" alt="" />
                    </button>
                `;

        const removeBtn = cartItemDiv.querySelector(".remove-item-btn");
        removeBtn.addEventListener("click", () => removeFromCart(item));

        cartContent.appendChild(cartItemDiv);
      });

      const total = cart.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
      );
      cartTotalPrice.textContent = `$${total.toFixed(2)}`;
    }

    cartContent.querySelectorAll(".item-note").forEach((input) => {
      input.addEventListener("input", (e) => {
        const name = e.target.dataset.name;
        const item = cart.find((i) => i.name === name);
        if (item) item.notes = e.target.value;
      });
    });
  }

  function addToCart(product) {
    const existingItem = cart.find((item) => item.name === product.name);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      cart.push({
        ...product,
        quantity: 1,
      });
    }

    updateUI();
  }

  function updateQuantity(product, change) {
    const item = cart.find((item) => item.name === product.name);

    if (item) {
      item.quantity += change;

      if (item.quantity <= 0) {
        cart = cart.filter((i) => i.name !== product.name);
      }
    }

    updateUI();
  }

  function removeFromCart(product) {
    cart = cart.filter((item) => item.name !== product.name);
    updateUI();
  }

  function updateUI() {
    renderProducts();
    renderCart();
  }

  async function confirmOrder() {
    const orderPayload = {
      tableId: tableIdInput.value,
      staffId: staffIdInput.value,
      items: cart.map((item) => ({
        menuItemId: item.id,
        quantity: item.quantity,
        notes: item.notes || "",
      })),
    };

    try {
      const response = await fetch("http://localhost:8080/api/orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(orderPayload),
      });

      if (!response.ok) throw new Error("Failed to create order");

      const createdOrder = await response.json();
      console.log("Order created:", createdOrder);

      showOrderConfirmation();
    } catch (err) {
      alert("Error submitting order: " + err.message);
    }
  }

  function showOrderConfirmation() {
    orderDetails.innerHTML = "";

    cart.forEach((item) => {
      const orderItem = document.createElement("div");
      orderItem.className = "order-item";

      orderItem.innerHTML = `
                <img src="${
                  item.image
                }" alt="order item thumbnail" class="order-item-thumbnail">
                <div class="order-item-details">
                    <h5 class="order-item-name">${item.name}</h5>
                    <div class="order-item-price-details">
                        <p class="order-item-quantity">${item.quantity}x</p>
                        <p class="order-item-price">@ $${item.price.toFixed(
                          2
                        )}</p>
                    </div>
                </div>
                <p class="order-item-total-price">$${(
                  item.price * item.quantity
                ).toFixed(2)}</p>
            `;

      orderDetails.appendChild(orderItem);
    });

    const total = cart.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
    const orderTotalDiv = document.createElement("div");
    orderTotalDiv.className = "order-total-details";
    orderTotalDiv.innerHTML = `
            <p>Order Total</p>
            <h3 class="order-total-price">$${total.toFixed(2)}</h3>
        `;

    orderDetails.appendChild(orderTotalDiv);

    modal.style.display = "block";
  }

  function startNewOrder() {
    printReceipt();

    cart = [];

    updateUI();

    modal.style.display = "none";
  }

  function printReceipt() {
    const receiptWindow = window.open("", "_blank");

    const receiptHTML = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>Order Receipt</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; max-width: 400px; margin: 0 auto; }
                    h1 { text-align: center; margin-bottom: 30px; }
                    .receipt-item { display: flex; justify-content: space-between; margin-bottom: 10px; }
                    .receipt-total { font-weight: bold; margin-top: 20px; border-top: 1px solid #000; padding-top: 10px; }
                    .thank-you { text-align: center; margin-top: 30px; font-style: italic; }
                </style>
            </head>
            <body>
                <h1>Order Receipt</h1>
                <div class="receipt-items">
                    ${cart
                      .map(
                        (item) => `
                        <div class="receipt-item">
                            <span>${item.name} (${
                          item.quantity
                        }x @ $${item.price.toFixed(2)})</span>
                            <span>$${(item.price * item.quantity).toFixed(
                              2
                            )}</span>
                        </div>
                    `
                      )
                      .join("")}
                </div>
                <div class="receipt-total">
                    <span>Total:</span>
                    <span>$${cart
                      .reduce(
                        (sum, item) => sum + item.price * item.quantity,
                        0
                      )
                      .toFixed(2)}</span>
                </div>
                <div class="thank-you">
                    <p>Thank you for your order!</p>
                </div>
            </body>
            </html>
        `;

    receiptWindow.document.write(receiptHTML);
    receiptWindow.document.close();

    // Wait for content to load before printing
    receiptWindow.onload = function () {
      setTimeout(() => {
        receiptWindow.print();
      }, 500);
    };
  }

  init();
});

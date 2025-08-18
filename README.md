# Restaurant-POS-System

<!-- Badges -->

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/en/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-green)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<!-- Description -->

A comprehensive Restaurant Point of Sale (POS) system built with Java and Spring Boot, offering a range of features for efficient restaurant management, from menu and inventory management to order processing, table reservations, user authentication, and payment processing. The system incorporates a modern web interface and real-time updates using WebSockets.

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database ERD](#database-erd)
- [Installation](#installation)
- [Usage](#usage)
- [How to Use](#how-to-use)
- [Project Structure](#project-structure)
- [API Reference](#api-reference)
- [License](#license)
- [Important Links](#important-links)
- [Authors](#authors)

## Features

- **Category Management**: Add, update, and delete menu categories using the Category Management UI.
- **Menu Item Management**: Create, update, delete, and filter menu items with details like price, preparation time, and status. Accessible through the Menu Items UI.
- **Inventory Management**: Track inventory levels, import/export via CSV. Manage inventory using the Inventory Management UI.
- **Order Management**: Create, update, and manage customer orders with items, quantities, and notes. Accessible through the Order Entry UI.
- **Table Management**: Manage dining tables, including status updates and reservations, using the Table Reservation UI.
- **User Authentication**: Secure user registration and login with JWT (JSON Web Tokens).
- **Real-time Updates**: WebSocket integration for real-time order updates.
- **Payment Processing**: Payment recording with receipts.
- **Role-Based Access Control**: User roles like ADMIN, MANAGER, WAITER, and CASHIER.
- **Reporting**: Generate sales receipts in PDF format.

## Tech Stack

- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL
- **Frontend**: JavaScript, HTML, CSS
- **Other**: Maven, Lombok, JSON Web Tokens (JWT), OpenCSV, OpenPDF, SockJS, STOMP

## Database ERD

Below is the Entity Relationship Diagram (ERD) for the Restaurant POS System, showing how entities like orders, menu items, payments, and users are connected:

![Restaurant POS ERD](./Restaurant%20POS%20ERD.svg)

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/mohanad-80/Restaurant-POS-System.git
   cd Restaurant-POS-System
   ```

2. **Backend Setup:**

   - Ensure you have Java 17 installed.
   - Install Maven.
   - Navigate to the `server` directory.
   - Configure the database connection in `server/src/main/resources/application.properties`.
   - Run `mvn spring-boot:run` to start the backend server.

   ```bash
   cd server
   mvn spring-boot:run
   ```

3. **Frontend Setup:**
   - Navigate to the `client` directory.
   - Open the HTML files in your browser.
   - Ensure the API base URL in the JavaScript files (`client/js/*.js`) is correctly set to your backend server address (e.g., `http://localhost:8080/api`).

## Usage

1. **Start the Backend:**

   - Run the Spring Boot application using Maven in the `server` directory:

     ```bash
     cd server
     mvn spring-boot:run
     ```

2. **Access the User Interfaces:**
   - Open the HTML files located in the `client/partials/` directory in your web browser to access different functionalities:
     - Category & Menu Items Management: `client/partials/MenuItem.html`
     - Inventory Management: `client/partials/inventory-list.html`
     - Order Entry: `client/partials/order-entry.html`
     - Table Reservation: `client/partials/table-reservation.html`
     - Waiter View: `client/partials/waiter-view.html`

---

### Use Cases

- **Cashier**: Taking customer orders and processing payments.
- **Waiter**: Managing assigned tables, placing orders, and tracking service.
- **Kitchen Staff**: Viewing and updating order statuses, managing inventory levels.
- **Manager**: Overseeing staff, menu, and inventory management, and generating reports.

---

### Examples

- **Accessing the Menu**

  ```http
  GET /api/menu
  ```

- **Creating an Order**

  ```http
  POST /api/orders
  Content-Type: application/json

  {
    "tableId": 1,
    "staffId": 1,
    "method": "CASH",
    "items": [
      {
        "menuItemId": 1,
        "quantity": 2,
        "notes": "Extra sauce"
      }
    ]
  }
  ```

- **Updating Table Status**

  ```http
  PATCH /api/tables/{id}
  Content-Type: application/json

  {
    "status": "OCCUPIED"
  }
  ```

- **Logging in**

  ```http
  POST /api/auth/login
  Content-Type: application/json

  {
    "email": "user@example.com",
    "password": "password"
  }
  ```

- **Uploading a Menu Item Image**

  ```http
  POST /api/menu/uploadImage/{id}
  Content-Type: multipart/form-data

  --form
  Content-Disposition: form-data; name="image"; filename="item.jpg"
  Content-Type: image/jpeg
  (binary image data)
  ```

---

## How to Use

### Managing Menu Items

1.  Navigate to `client/partials/MenuItem.html` in your browser.
2.  Use the "Categories" tab to add, edit, or delete menu categories.
3.  Use the "Menu Items" tab to create, update, or delete menu items.
4.  Fill in the item details such as name, price, preparation time, status, and category.
5.  Click "Save" to add or update the item.

### Managing Inventory

1.  Navigate to `client/partials/inventory-list.html` in your browser.
2.  Click "+ New Item" to add a new inventory item.
3.  Enter the item's name, available units, and unit type.
4.  Click "Save" to add the item.
5.  Use the "Download CSV" and "Upload CSV" buttons to export and import inventory data.

### Creating Orders

1.  Navigate to `client/partials/order-entry.html` in your browser.
2.  Select menu items to add to the cart.
3.  Enter the table ID, staff ID, and payment method.
4.  Click "Confirm Order" to submit the order.
5.  Review the order details in the modal and click "Start New Order" to complete the order.

### Managing Tables

1.  Navigate to `client/partials/table-reservation.html` in your browser.
2.  Click "Add New Table" to add a new table.
3.  Enter the table number, seats, section, and status.
4.  Click "Save" to add the table.
5.  Use the edit and delete buttons to modify or remove existing tables.

### Waiter View

1.  Navigate to `client/partials/waiter-view.html` in your browser.
2.  View and manage order items in real-time, moving them between "Not Ready", "Ready", and "Served" panels.

## Project Structure

```
/Restaurant-POS-System
├── README.md
├── client # Frontend (HTML, CSS, JS)
│ ├── assets
│ │ └── images # Icons, product images, favicon
│ ├── data # Local JSON seed data for menus/tables
│ ├── index.html # Main entry point
│ ├── index.js # Root JS file
│ ├── js # Page-specific scripts
│ ├── partials # HTML partials loaded dynamically
│ ├── style.css # Global styles
│ └── styles # Page-specific CSS modules
└── server # Spring Boot backend
  ├── pom.xml # Maven configuration
  ├── src/main/java # Main backend source
  │ ├── Category # CRUD for categories
  │ ├── Inventory # Inventory management
  │ ├── MenuItem # Menu item management
  │ ├── User # User + role entities
  │ ├── auth # Authentication & JWT security
  │ ├── orders # Orders + order items
  │ ├── payment # Payment entities & services
  │ ├── shared # Config (CORS, security, websocket, etc.)
  │ └── table_management # Table reservation + status
  ├── src/main/resources
  │ ├── application.properties
  │ ├── data.sql # Initial DB seed
  │ └── static/templates
  └── src/test/java # Unit & integration tests
```

## API Reference

### Categories API

- `GET /api/categories`: Get all categories
- `POST /api/categories`: Add a new category
- `PUT /api/categories/{id}`: Update a category
- `DELETE /api/categories/{id}`: Delete a category

### Menu Items API

- `GET /api/menu`: Get all menu items (with optional categoryId and status filters)
- `GET /api/menu/{id}`: Get a menu item by ID
- `POST /api/menu`: Add a new menu item
- `PUT /api/menu/{id}`: Update a menu item
- `DELETE /api/menu/{id}`: Delete a menu item
- `POST /api/menu/uploadImage/{id}`: Upload an image for a menu item

### Inventory API

- `GET /api/v1/inventory`: Get all inventory items
- `POST /api/v1/inventory`: Add a new inventory item
- `POST /api/v1/inventory/import`: Import inventory items from a CSV file
- `GET /api/v1/inventory/export`: Export inventory items to a CSV file

### Orders API

- `GET /api/orders`: Get all orders
- `GET /api/orders/{id}`: Get an order by ID
- `POST /api/orders`: Create a new order
- `PATCH /api/orders/{id}`: Update an order
- `DELETE /api/orders/{id}`: Delete an order
- `POST /api/orders/{id}/items`: Add an item to an order
- `PATCH /api/orders/items/{itemId}`: Update an order item
- `DELETE /api/orders/items/{itemId}`: Delete an order item

### Table Management API

- `GET /api/tables`: Get all tables
- `POST /api/tables`: Add a new table
- `GET /api/tables/{id}`: Get a table by ID
- `PUT /api/tables/{id}`: Update a table
- `DELETE /api/tables/{id}`: Delete a table
- `PATCH /api/tables/{id}`: Update a table status

### Authentication API

- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Login a user
- `POST /api/auth/logout`: Logout a user

### Payments API

- `POST /api/payments/{id}`: Pay for an order
- `GET /api/payments/{orderId}/receipt.pdf`: Download a receipt for an order

## License

This project is open source and available under the [MIT License](./LICENSE).

## Important Links

- Repository Link: [https://github.com/mohanad-80/Restaurant-POS-System](https://github.com/mohanad-80/Restaurant-POS-System)

## Authors

[Restaurant-POS-System](https://github.com/mohanad-80/Restaurant-POS-System) by:

- [Mohanad Ahmed](https://github.com/mohanad-80)
- [Omar Elshereef](https://github.com/OmarElshereef)
- [Seif Ahmed Salah](https://github.com/Seif-Ahmed-Salah)
- [Omar Sameh](https://github.com/omarse7a)

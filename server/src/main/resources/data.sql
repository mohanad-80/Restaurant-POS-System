INSERT INTO users (id, name, email, password, role, created_at) VALUES
(1,  'Alice Admin',     'alice.admin@example.com',    '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'ADMIN',   NOW()),
(2,  'Bob Manager',     'bob.manager@example.com',    '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'MANAGER', NOW()),
(3,  'Charlie Waiter',  'charlie.waiter@example.com', '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'WAITER',  NOW()),
(4,  'Diana Cashier',   'diana.cashier@example.com',  '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CASHIER', NOW()),
(5,  'Ethan Kitchen',   'ethan.kitchen@example.com',  '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'KITCHEN', NOW()),
(6,  'Fiona Customer',  'fiona.customer@example.com', '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CUSTOMER',NOW()),
(7,  'George Admin',    'george.admin@example.com',   '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'ADMIN',   NOW()),
(8,  'Hannah Manager',  'hannah.manager@example.com', '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'MANAGER', NOW()),
(9,  'Ian Waiter',      'ian.waiter@example.com',     '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'WAITER',  NOW()),
(10, 'Julia Cashier',   'julia.cashier@example.com',  '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CASHIER', NOW()),
(11, 'Kevin Kitchen',   'kevin.kitchen@example.com',  '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'KITCHEN', NOW()),
(12, 'Laura Customer',  'laura.customer@example.com', '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CUSTOMER',NOW()),
(13, 'Mike Admin',      'mike.admin@example.com',     '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'ADMIN',   NOW()),
(14, 'Nina Manager',    'nina.manager@example.com',   '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'MANAGER', NOW()),
(15, 'Oscar Waiter',    'oscar.waiter@example.com',   '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'WAITER',  NOW()),
(16, 'Paula Cashier',   'paula.cashier@example.com',  '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CASHIER', NOW()),
(17, 'Quentin Kitchen', 'quentin.kitchen@example.com','$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'KITCHEN', NOW()),
(18, 'Rachel Customer', 'rachel.customer@example.com','$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CUSTOMER',NOW()),
(19, 'Steve Waiter',    'steve.waiter@example.com',   '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'WAITER',  NOW()),
(20, 'Tina Cashier',    'tina.cashier@example.com',   '$2b$10$7KIbPPMikhilaGMDGLlCAe5gYxbSf/FYIUIjwhxnCvwyd4ZA39/Ay', 'CASHIER', NOW());




-- Categories
INSERT INTO categories (id, name) VALUES
(1, 'Appetizers'),
(2, 'Main Courses'),
(3, 'Desserts'),
(4, 'Beverages'),
(5, 'Salads'),
(6, 'Soups');



-- Menu Items
INSERT INTO menu_items (id, name, category_id, price, preparation_time, image_path, status, created_at)
VALUES
  (1, 'Spring Rolls', 1, 5.99, 10, 'uploads/menu/spring_rolls.jpg', 'AVAILABLE', NOW()),
  (2, 'Grilled Chicken', 2, 12.50, 20, 'uploads/menu/grilled_chicken.jpg', 'AVAILABLE', NOW()),
  (3, 'Cheesecake', 3, 6.75, 15, 'uploads/menu/cheesecake.jpg', 'AVAILABLE', NOW()),
  (4, 'Lemonade', 4, 3.25, 5, 'uploads/menu/lemonade.jpg', 'AVAILABLE', NOW()),
  (5, 'Caesar Salad', 5, 7.40, 12, 'uploads/menu/caesar_salad.jpg', 'OUT_OF_STOCK', NOW()),
  (6, 'Tomato Soup', 6, 4.90, 8, 'uploads/menu/tomato_soup.jpg', 'AVAILABLE', NOW());


-- Dining Tables
INSERT INTO dining_tables (id, table_number, seats, section, status)
VALUES
  (1, 'T1', 2, 'Patio', 'AVAILABLE'),
  (2, 'T2', 4, 'Patio', 'OCCUPIED'),
  (3, 'T3', 6, 'Main Hall', 'AVAILABLE'),
  (4, 'T4', 4, 'Main Hall', 'RESERVED'),
  (5, 'T5', 8, 'VIP', 'OUT_OF_SERVICE'),
  (6, 'T6', 2, 'Balcony', 'AVAILABLE'),
  (7, 'T7', 4, 'Balcony', 'OCCUPIED'),
  (8, 'T8', 6, 'Terrace', 'AVAILABLE'),
  (9, 'T9', 2, 'Terrace', 'RESERVED'),
  (10, 'T10', 10, 'Banquet', 'AVAILABLE');



-- Orders
INSERT INTO orders (id, order_number, total_amount, discount, tax_amount, status, created_at, table_id, staff_id)
VALUES
  (1, 'ORD-1001', 26.78, 0.00, 2.68, 'OPEN', NOW(), 1, 3),       -- Table 1, Charlie Waiter
  (2, 'ORD-1002', 17.40, 0.00, 1.74, 'IN_KITCHEN', NOW(), 2, 9), -- Table 2, Ian Waiter
  (3, 'ORD-1003', 9.75, 0.00, 0.98, 'SERVED', NOW(), 3, 15),     -- Table 3, Oscar Waiter
  (4, 'ORD-1004', 6.75, 0.00, 0.68, 'PAID', NOW(), 4, 19);       -- Table 4, Steve Waiter


-- Order Items
INSERT INTO order_items (id, quantity, unit_price, total_price, status, notes, order_id, menu_item_id)
VALUES
  (1, 2, 5.99, 11.98, 'PENDING', 'Extra crispy', 1, 1),          -- Spring Rolls
  (2, 1, 12.50, 12.50, 'PREPARING', 'No salt', 1, 2),            -- Grilled Chicken
  (3, 3, 3.25, 9.75, 'READY', 'With ice', 2, 4),                 -- Lemonade
  (4, 1, 6.75, 6.75, 'SERVED', 'Birthday special', 3, 3),        -- Cheesecake
  (5, 2, 7.40, 14.80, 'PENDING', 'Dressing on the side', 2, 5),  -- Caesar Salad
  (6, 1, 4.90, 4.90, 'PREPARING', 'Extra hot', 4, 6);            -- Tomato Soup



INSERT INTO payments (id, order_id, amount, method, paid_at)
VALUES
  (1, 4, 6.75, 'CASH', NOW());     -- Paid for order ORD-1004

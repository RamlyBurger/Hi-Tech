insert into category (id, code, name, description, image_path) values
    (1, 'desktop', 'Desktop', 'Performance desktops for gaming, creation, study, and work.', '/images/desktop.jpg'),
    (2, 'laptop', 'Laptop', 'Portable computers from gaming machines to everyday notebooks.', '/images/laptop.jpg'),
    (3, 'smartphone', 'Smartphone', 'Gaming and productivity smartphones with high refresh displays.', '/images/smartphone.jpg'),
    (4, 'accessories', 'Accessories', 'Keyboards, mice, and other daily computing accessories.', '/images/accesories2.jpg');

insert into product (id, slug, name, description, price, discount_percentage, stock, image_path, detail_image_path, active, category_id) values
    (1, 'dell-xps-8950', 'Dell XPS 8950', 'A compact high-performance desktop for gaming, study, and creative workloads.', 3500.00, 25, 15, '/images/desktop1.jpg', '/images/desktop1c.jpg', true, 1),
    (2, 'hp-omen-45l', 'HP Omen 45L', 'A premium gaming desktop with strong cooling and expansion room.', 11000.00, 0, 8, '/images/desktop2.jpg', '/images/desktop2b.jpg', true, 1),
    (3, 'hp-pavilion-gaming-desktop', 'HP Pavilion Gaming Desktop', 'A balanced desktop for everyday gaming and productivity.', 6000.00, 0, 10, '/images/desktop3.jpg', '/images/desktop3b.png', true, 1),
    (4, 'falcon-northwest-tiki', 'Falcon Northwest Tiki', 'A small-form-factor desktop for customers who need power in a compact setup.', 7699.00, 45, 5, '/images/desktop4.jpg', '/images/desktop4b.jpg', true, 1),
    (5, 'rog-strix-g15', 'ROG STRIX G15', 'A gaming laptop with strong graphics performance and a fast display.', 5000.00, 25, 12, '/images/rog1.png', '/images/rog1b.png', true, 2),
    (6, 'tuf-gaming-f15', 'TUF Gaming F15', 'A durable gaming laptop for students and everyday players.', 4199.00, 10, 16, '/images/tuf1.png', '/images/tuf1b.png', true, 2),
    (7, 'apple-macbook1', 'Apple Macbook1', 'A slim notebook for productivity, study, and creative workflows.', 4399.00, 0, 9, '/images/macbook1.jpg', '/images/macbook1b.jpg', true, 2),
    (8, 'hp-victus-16', 'HP Victus 16', 'A capable laptop for gaming, assignments, and multitasking.', 3700.00, 45, 11, '/images/victus2.png', '/images/victus2b.png', true, 2),
    (9, 'red-magic-7', 'Red Magic 7', 'A gaming smartphone built for smooth play and long sessions.', 2500.00, 25, 18, '/images/phone1.png', '/images/phone1b.png', true, 3),
    (10, 'black-shark-5-pro', 'Black Shark 5 Pro', 'A flagship gaming phone with high-end performance and responsive controls.', 3200.00, 18, 14, '/images/phone2.png', '/images/phone2b.png', true, 3),
    (11, 'asus-rog-phone-6-pro', 'Asus ROG Phone 6 Pro', 'A high-performance smartphone for mobile gaming enthusiasts.', 2800.00, 0, 10, '/images/phone3.png', '/images/phone3b.png', true, 3),
    (12, 'poco-f4-gt', 'Poco F4 GT', 'A value-focused gaming smartphone with flagship-class speed.', 1400.00, 45, 20, '/images/phone4.png', '/images/phone4b.png', true, 3),
    (13, 'g502-hero', 'G502 HERO', 'A precise gaming mouse with a familiar ergonomic shape.', 299.00, 45, 30, '/images/logitech1.jpg', '/images/logitech1b.jpg', true, 4),
    (14, 'razer-basilisk-ultimate', 'Razer Basilisk Ultimate', 'A wireless gaming mouse with configurable controls.', 600.00, 0, 22, '/images/razer1.png', '/images/razer1b.png', true, 4),
    (15, 'logitech-signature-k650', 'Logitech Signature K650', 'A comfortable full-size keyboard for daily work and study.', 150.00, 0, 28, '/images/logitech2.jpg', '/images/logitech2b.jpg', true, 4),
    (16, 'razer-pro-type-ultra', 'Razer Pro Type Ultra', 'A clean wireless mechanical keyboard for productivity users.', 240.00, 45, 25, '/images/razer2.png', '/images/razer2-1.png', true, 4);

insert into user_account (id, username, email, full_name, password_hash, enabled) values
    (1, 'customer', 'customer@hitech.local', 'Demo Customer', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', true),
    (2, 'admin', 'admin@hitech.local', 'Demo Admin', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', true);

insert into user_role (user_account_id, role) values
    (1, 'CUSTOMER'),
    (2, 'CUSTOMER'),
    (2, 'ADMIN');

insert into store_event (id, name, venue, event_date, event_time, active) values
    (1, 'Gaming Competition', 'Segamat', '2022-10-01', '08:00:00', true),
    (2, 'Calling Competition', 'KL', '2022-10-03', '17:30:00', true),
    (3, 'Lucky Draw', 'Sarawak', '2022-10-04', '14:00:00', true),
    (4, 'Big-eater Competition', 'Perlis', '2022-10-14', '14:15:00', true),
    (5, 'Scrabble Competition', 'JB', '2022-10-27', '10:30:00', true);

insert into promotion (id, title, description, product_name, highlight_color, starts_on, ends_on, active) values
    (1, 'Iphone 14 Promotion', 'Promotion on selected smartphone products.', 'Iphone 14', 'red', '2022-09-26', '2022-09-30', true),
    (2, 'Lenovo Laptop Promotion', 'Short laptop promotion for early October.', 'Lenovo Laptop', 'purple', '2022-10-01', '2022-10-02', true),
    (3, 'Dell Laptop Promotion', 'Featured promotion for Dell laptop buyers.', 'Dell Laptop', 'gold', '2022-10-03', '2022-10-04', true),
    (4, 'Airpod Promotion', 'One-day accessory promotion.', 'Airpod', 'green', '2022-10-09', '2022-10-09', true),
    (5, 'Intel CPU Promotion', 'Hardware promotion during the October event.', 'Intel CPU', 'blue', '2022-10-14', '2022-10-14', true),
    (6, 'JBL Earphones Promotion', 'Audio accessory promotion for November.', 'JBL Earphones', 'orange', '2022-11-06', '2022-11-07', true),
    (7, 'Poco F3 Promotion', 'Smartphone promotion for Poco fans.', 'Poco F3', 'darkolivegreen', '2022-11-07', '2022-11-19', true),
    (8, 'HP Laptop Promotion', 'Laptop promotion for HP shoppers.', 'Hp Laptop', 'navy', '2022-11-21', '2022-11-21', true),
    (9, 'Dell Monitor Promotion', 'Christmas monitor promotion.', 'Dell Monitor', 'salmon', '2022-12-25', '2022-12-25', true);

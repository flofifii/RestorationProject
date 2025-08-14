USE restoration;

-- =========================
-- 1) SUPPLIER
-- =========================
INSERT INTO supplier (name, address, email, phoneNumber) VALUES
('Pommes de Terre SARL', '123 rue Agricole, Flandre', 'contact@pommesdeterre.com', '0320123456'),
('Surgelés Belges',      '89 avenue du Gel, Wallonie', 'contact@surgelesbelges.be', '0330456789'),
('Boissons du Plat Pays','7 rue des Brasseurs, Bruxelles', 'hello@boissons-pp.be', '025512345');

-- =========================
-- 2) CATEGORY
-- =========================
INSERT INTO category (name, description) VALUES
('Légumes',   'Tous types de légumes frais'),
('Surgelés',  'Produits congelés'),
('Condiments','Sauces et épices'),
('Boissons',  'Boissons froides et chaudes'),
('Plat',      'Plats principaux'),
('Menu',      'Menus combinés');

-- =========================
-- 3) PERSON
-- =========================
INSERT INTO person (firstName, lastName, mail, address, phoneNumber) VALUES
('Alice',  'Dupont', 'alice.dupont@mail.com', '12 rue des Frites, Bruxelles', '0478123456'),
('Bob',    'Martin', 'bob.martin@mail.com',   '45 avenue des Pommes, Liège',  '0487654321'),
('Charlie','Lemoine','charlie@mail.com',      '78 bd des Saveurs, Namur',     '0498123456');

-- =========================
-- 4) CUSTOMER (hérite de person)
-- =========================
INSERT INTO customer (customerId, registrationDate, hasDiscount, rewardPoint) VALUES
(1, '2024-01-15', 1, 120),
(2, '2024-05-20', 0, 50);

-- =========================
-- 5) WORKER (hérite de person)
-- =========================
INSERT INTO worker (workerId, hireDate, `function`, endDate) VALUES
(3, '2023-08-01', 'Chef cuisinier', NULL);

-- =========================
-- 6) SHIFT
-- =========================
INSERT INTO shift (workerId, startTime, endTime) VALUES
(3, '2025-08-10 09:00:00', '2025-08-10 17:00:00'),
(3, '2025-08-11 09:00:00', '2025-08-11 17:00:00');

-- =========================
-- 7) PRODUCT (FK: supplierName, categoryName)
-- =========================
INSERT INTO product (name, supplierName, categoryName, minQuantityDesired, stockQuantity, reorderQuantity, dateOfSale, isFrozen, description) VALUES
('Pommes de terre',   'Pommes de Terre SARL', 'Légumes',   100, 250, 200, '2025-08-10', 0, 'Pommes de terre fraîches pour frites'),
('Frites surgelées',  'Surgelés Belges',      'Surgelés',   50, 100,  80, '2025-08-09', 1, 'Frites congelées prêtes à cuire'),
('Sauce ketchup',     'Pommes de Terre SARL', 'Condiments', 30,  60,  40, '2025-08-08', 0, 'Sauce tomate'),
('Sauce andalouse',   'Pommes de Terre SARL', 'Condiments', 20,  40,  30, '2025-08-08', 0, 'Sauce andalouse'),
('Cola 33cl',         'Boissons du Plat Pays','Boissons',   40, 120,  60, '2025-08-07', 0, 'Boisson gazeuse'),
('Eau plate 50cl',    'Boissons du Plat Pays','Boissons',   40, 150,  60, '2025-08-07', 0, 'Eau minérale');

-- =========================
-- 8) ITEM (FK: category)
-- =========================
INSERT INTO item (name, price, category) VALUES
('Portion de frites',      2.50, 'Plat'),
('Burger classique',       5.00, 'Plat'),
('Menu frites + burger',   7.50, 'Menu'),
('Menu frites + boisson',  4.00, 'Menu');

-- =========================
-- 9) ITEM_DETAIL (composition des items)
-- =========================
-- Portion de frites : 150g pommes de terre OU 150g frites surgelées
INSERT INTO itemDetail (itemName, productName, quantity) VALUES
('Portion de frites', 'Pommes de terre', 150),
('Portion de frites', 'Frites surgelées', 150),

-- Burger classique (exemple simplifié : frites + sauce)
('Burger classique', 'Pommes de terre', 120),
('Burger classique', 'Sauce ketchup', 1),

-- Menu frites + burger
('Menu frites + burger', 'Pommes de terre', 150),
('Menu frites + burger', 'Frites surgelées', 150),
('Menu frites + burger', 'Sauce ketchup', 1),

-- Menu frites + boisson
('Menu frites + boisson', 'Pommes de terre', 150),
('Menu frites + boisson', 'Cola 33cl', 1);

-- =========================
-- 10) ORDER (table réservée → backticks)
-- =========================
INSERT INTO `order` (customerId, paymentMethod, isTakeaway, creationDate, deliveryDate, commentary, specificRequest) VALUES
(1, 'Carte bancaire', 0, '2025-08-10 12:00:00', NULL, 'Merci de préparer rapidement', NULL),
(2, 'Espèces',        1, '2025-08-10 12:15:00', '2025-08-10 12:45:00', NULL, 'Sans sauce');

-- =========================
-- 11) ORDER_DETAIL
-- =========================
INSERT INTO orderDetail (orderNumber, itemName, quantity) VALUES
(1, 'Portion de frites', 2),
(1, 'Burger classique', 1),
(2, 'Portion de frites', 1),
(2, 'Menu frites + boisson', 1);

-- =========================
-- 12) RESTOCK
-- =========================
INSERT INTO restock (date) VALUES
('2025-08-09'),
('2025-08-10');

-- =========================
-- 13) RESTOCK_DETAIL
-- =========================
INSERT INTO restockDetail (restockId, productName, quantity) VALUES
(1, 'Pommes de terre', 500),
(1, 'Sauce ketchup',   100),
(2, 'Frites surgelées', 300),
(2, 'Cola 33cl',        200);

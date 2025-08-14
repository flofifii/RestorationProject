DROP DATABASE IF EXISTS restoration;
CREATE DATABASE restoration CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restoration;

-- =========================
-- 1) Person / Specialisations
-- =========================
CREATE TABLE person (
  personId     INT AUTO_INCREMENT PRIMARY KEY,
  firstName    VARCHAR(100) NOT NULL,
  lastName     VARCHAR(100) NOT NULL,
  mail         VARCHAR(150) NULL,
  address      VARCHAR(255) NULL,
  phoneNumber  VARCHAR(30)  NULL
) ENGINE=InnoDB;

CREATE TABLE customer (
  customerId       INT PRIMARY KEY,
  registrationDate DATE        NOT NULL,
  hasDiscount      TINYINT(1)  NOT NULL DEFAULT 0,
  rewardPoint      INT         NOT NULL DEFAULT 0,
  CONSTRAINT fkCustomerPerson
    FOREIGN KEY (customerId) REFERENCES person(personId)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE worker (
  workerId    INT PRIMARY KEY,
  hireDate    DATE         NOT NULL,
  `function`    VARCHAR(100) NOT NULL,
  endDate     DATE         NULL,
  CONSTRAINT fkWorkerPerson
    FOREIGN KEY (workerId) REFERENCES person(personId)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE shift (
  shiftId    INT AUTO_INCREMENT PRIMARY KEY,
  workerId   INT       NOT NULL,
  startTime  DATETIME  NOT NULL,
  endTime    DATETIME  NOT NULL,
  CONSTRAINT fkShiftWorker
    FOREIGN KEY (workerId) REFERENCES worker(workerId)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 2) Catalogue & Stock
-- =========================
CREATE TABLE supplier (
  name        VARCHAR(120) PRIMARY KEY,
  address     VARCHAR(255) NULL,
  email       VARCHAR(150) NULL,
  phoneNumber VARCHAR(30)  NULL
) ENGINE=InnoDB;

CREATE TABLE category (
  name        VARCHAR(100) PRIMARY KEY,
  description TEXT NULL
) ENGINE=InnoDB;

CREATE TABLE product (
  name               VARCHAR(120) PRIMARY KEY,
  supplierName       VARCHAR(120) NOT NULL,
  categoryName       VARCHAR(100) NOT NULL,
  minQuantityDesired INT          NOT NULL,
  stockQuantity      INT          NOT NULL,
  reorderQuantity    INT          NOT NULL,
  dateOfSale         DATE         NOT NULL,
  isFrozen           TINYINT(1)   NOT NULL DEFAULT 0,
  description        TEXT         NULL,
  CONSTRAINT fkProductSupplier
    FOREIGN KEY (supplierName) REFERENCES supplier(name)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fkProductCategory
    FOREIGN KEY (categoryName) REFERENCES category(name)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE item (
  name      VARCHAR(120) PRIMARY KEY,
  price     DECIMAL(10,2) NOT NULL,
  category  VARCHAR(100)  NOT NULL,
  CONSTRAINT fkItemCategory
    FOREIGN KEY (category) REFERENCES category(name)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE itemDetail (
  itemName     VARCHAR(120) NOT NULL,
  productName  VARCHAR(120) NOT NULL,
  quantity     INT          NOT NULL,
  PRIMARY KEY (itemName, productName),
  CONSTRAINT fkItemDetailItem
    FOREIGN KEY (itemName) REFERENCES item(name)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fkItemDetailProduct
    FOREIGN KEY (productName) REFERENCES product(name)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 3) Commandes
-- =========================
CREATE TABLE `order` (
  number           INT AUTO_INCREMENT PRIMARY KEY,
  customerId       INT         NOT NULL,
  paymentMethod    VARCHAR(30) NOT NULL,
  isTakeaway       TINYINT(1)  NOT NULL DEFAULT 0,
  creationDate     DATETIME    NOT NULL,
  deliveryDate     DATETIME    NULL,
  commentary       TEXT        NULL,
  specificRequest  TEXT        NULL,
  CONSTRAINT fkOrderCustomer
    FOREIGN KEY (customerId) REFERENCES customer(customerId)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE orderDetail (
  orderNumber  INT          NOT NULL,
  itemName     VARCHAR(120) NOT NULL,
  quantity     INT          NOT NULL,
  PRIMARY KEY (orderNumber, itemName),
  CONSTRAINT fkOrderDetailOrder
    FOREIGN KEY (orderNumber) REFERENCES `order`(number)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fkOrderDetailItem
    FOREIGN KEY (itemName) REFERENCES item(name)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 4) Réapprovisionnement
-- =========================
CREATE TABLE restock (
  restockId INT AUTO_INCREMENT PRIMARY KEY,
  date      DATE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE restockDetail (
  restockId    INT          NOT NULL,
  productName  VARCHAR(120) NOT NULL,
  quantity     INT          NOT NULL,
  PRIMARY KEY (restockId, productName),
  CONSTRAINT fkRestockDetailRestock
    FOREIGN KEY (restockId) REFERENCES restock(restockId)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fkRestockDetailProduct
    FOREIGN KEY (productName) REFERENCES product(name)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;
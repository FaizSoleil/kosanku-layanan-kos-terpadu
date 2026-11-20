DROP DATABASE IF EXISTS kosanku;

CREATE DATABASE IF NOT EXISTS kosanku
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kosanku;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    username            VARCHAR(50) PRIMARY KEY,
    password            VARCHAR(255) NOT NULL,
    nama_lengkap        VARCHAR(100) NOT NULL
);

-- 2. Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number         VARCHAR(20) NOT NULL UNIQUE,
    price               DECIMAL(12, 2) NOT NULL,
    status              ENUM('Tersedia','Terisi','Perbaikan') NOT NULL DEFAULT 'Tersedia',
    CONSTRAINT chk_room_price   CHECK (price >= 0)
);

-- 3. Tenants Table
CREATE TABLE IF NOT EXISTS tenants (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    id_number           VARCHAR(20) UNIQUE NOT NULL,  -- nomor id kaya NIK
    name                VARCHAR(100) NOT NULL,
    email               VARCHAR(100) UNIQUE,          -- nullable; send NULL, not '', from Java
    phone_number        VARCHAR(20)  NOT NULL,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE -- soft delete: hide instead of DELETE
    CHECK (is_active IN (0,1))
);

-- 4. Contracts Table
CREATE TABLE IF NOT EXISTS contracts (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id           INT NOT NULL,
    room_id             INT NOT NULL,
    tanggal_masuk       DATE NOT NULL,
    tanggal_keluar      DATE NOT NULL,
    deposit             DECIMAL(12, 2) NOT NULL,
    tanggal_jatuh_tempo DATE NOT NULL,
    status              ENUM('Aktif','Selesai','Dibatalkan') NOT NULL DEFAULT 'Aktif',
    FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE RESTRICT,
    FOREIGN KEY (room_id)   REFERENCES rooms(id) ON DELETE RESTRICT,
    CONSTRAINT chk_contract_dates_tempo_masuk   CHECK (tanggal_jatuh_tempo > tanggal_masuk),
    CONSTRAINT chk_contract_dates_tempo_keluar  CHECK (tanggal_keluar > tanggal_jatuh_tempo),
    CONSTRAINT chk_deposit                      CHECK (deposit >= 0)
);

-- 5. Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    contract_id         INT NOT NULL,
    bulan_tagihan       DATE NOT NULL,
    total_tagihan       DECIMAL(12, 2) NOT NULL,
    status              ENUM('Belum Dibayar','Sebagian','Lunas') NOT NULL DEFAULT 'Belum Dibayar',
    FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE RESTRICT,
    UNIQUE (contract_id, bulan_tagihan),            -- no duplicate bill for the same month
    CONSTRAINT chk_bill_total CHECK (total_tagihan >= 0)
);

-- 6. Payments Table (Added bill_id for proper relation tracking)
CREATE TABLE IF NOT EXISTS payments (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    bill_id             INT NOT NULL,
    payment_date        DATE NOT NULL,
    jumlah              DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE RESTRICT,
    CONSTRAINT chk_payment_amount CHECK (jumlah > 0)
);

-- 7. Expenses Table
CREATE TABLE IF NOT EXISTS expenses (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    expense_date        DATE NOT NULL,
    deskripsi           TEXT NOT NULL,
    jumlah              DECIMAL(12, 2) NOT NULL,
    CONSTRAINT chk_expense_amount CHECK (jumlah > 0)
);
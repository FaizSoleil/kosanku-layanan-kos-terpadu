CREATE DATABASE IF NOT EXISTS kosanku;
USE kosanku;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL
);

-- 2. Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    facilities TEXT,
    status VARCHAR(50) NOT NULL
);

-- 3. Tenants Table
CREATE TABLE IF NOT EXISTS tenants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20) NOT NULL
);

-- 4. Contracts Table
CREATE TABLE IF NOT EXISTS contracts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id INT NOT NULL,
    room_id INT NOT NULL,
    tanggal_masuk DATE NOT NULL,
    tanggal_keluar DATE NOT NULL,
    deposit DECIMAL(10, 2) NOT NULL,
    tanggal_jatuh_tempo DATE NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- 5. Bills Table
CREATE TABLE IF NOT EXISTS bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contract_id INT NOT NULL,
    bulan_tagihan DATE NOT NULL,
    total_tagihan DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE
);

-- 6. Payments Table (Added bill_id for proper relation tracking)
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT NOT NULL,
    payment_date DATE NOT NULL,
    jumlah DECIMAL(10, 2) NOT NULL,
    metode_pembayaran VARCHAR(50) NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE
);

-- 7. Expenses Table
CREATE TABLE IF NOT EXISTS expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expense_date DATE NOT NULL,
    deskripsi TEXT NOT NULL,
    jumlah DECIMAL(10, 2) NOT NULL
);

SET FOREIGN_KEY_CHECKS = 0;

--jalankan manual untuk buat dummy data
-- Disable foreign key checks temporarily for smooth seeding order

-- Clear existing data if re-running seed
TRUNCATE TABLE payments;
TRUNCATE TABLE bills;
TRUNCATE TABLE contracts;
TRUNCATE TABLE rooms;
TRUNCATE TABLE tenants;
TRUNCATE TABLE users;
TRUNCATE TABLE expenses;

-- Enable foreign key checks back
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Seed Users (Admin / Staff)
-- username: admin, pass: admin123
INSERT INTO users (username, password, nama_lengkap) VALUES
('admin', 'a735e89cc43f756daed5a66c21a9f0296f4b75834172eb8f0bf70eef2063f93a', 'Budi Santoso'),
('staff_front', '$2a$10$X7vQj5wD2nB5n4K1qW0Xeu1Jq3g2v5P9W8L8T7K6v5m4n3p2q1s0W', 'Siti Rahma');

-- 2. Seed Rooms
INSERT INTO rooms (room_number, price, status) VALUES
('101', 1500000.00, 'Tersedia'),
('102', 1500000.00, 'Terisi'),
('201', 2200000.00, 'Perbaikan'),
('202', 2200000.00, 'Tersedia'),
('301', 3000000.00, 'Terisi');

-- 3. Seed Tenants
INSERT INTO tenants (id_number, name, email, phone_number, is_active) VALUES
('1234567891234567', 'Andi Pratama', 'andi.pratama@gmail.com', '081234567890', TRUE),
('0987654321098765', 'Rina Melati', 'rina.melati@yahoo.com', '081987654321', FALSE),
('5678904321098543', 'Joko Widodo', 'joko.w@outlook.com', '081122334455', '1');

-- 4. Seed Contracts
-- tenant_id 1 in Room 101, tenant_id 2 in Room 201, tenant_id 3 in Room 301
INSERT INTO contracts (tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo, status) VALUES
(1, 1, '2026-01-01', '2026-12-31', 1500000.00, '2026-12-30', 'Aktif'),
(2, 3, '2026-02-01', '2027-01-31', 2200000.00, '2027-01-30', 'Selesai'),
(3, 5, '2026-03-01', '2027-02-28', 3000000.00, '2027-02-27', 'Dibatalkan');

-- 5. Seed Bills
-- Monthly bills linked to the active contracts
INSERT INTO bills (contract_id, bulan_tagihan, total_tagihan, status) VALUES
(1, '2026-10-01', 1500000.00, 'Belum Dibayar'),
(1, '2026-11-01', 1500000.00, 'Sebagian'),
(2, '2026-10-01', 2200000.00, 'Lunas'),
(2, '2026-11-01', 2200000.00, 'Belum Dibayar'),
(3, '2026-11-01', 3000000.00, 'Lunas');

-- 6. Seed Payments
-- Matching payments for the paid bills
INSERT INTO payments (bill_id, payment_date, jumlah) VALUES
(1, '2026-10-05', 1500000.00),
(3, '2026-10-12', 2200000.00),
(4, '2026-11-10', 2200000.00);

-- 7. Seed Expenses
INSERT INTO expenses (expense_date, deskripsi, jumlah) VALUES
('2026-11-02', 'Maintenance', 350000.00),
('2026-11-05', 'Electricity & Water', 1250000.00),
('2026-11-12', 'Cleaning Supplies', 150000.00);
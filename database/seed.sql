-- =====================================================================
-- Kosanku dummy data seeder (jalankan manual)
-- "Hari ini" untuk data ini = 2026-11-25
--
-- PENTING: ID di bagian contracts/bills/payments dirujuk secara manual,
-- jadi script ini bergantung pada TRUNCATE yang me-reset AUTO_INCREMENT ke 1.
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE payments;
TRUNCATE TABLE bills;
TRUNCATE TABLE contracts;
TRUNCATE TABLE rooms;
TRUNCATE TABLE tenants;
TRUNCATE TABLE users;
TRUNCATE TABLE expenses;

SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- 1. Users (Admin / Staff)  -- hash dipertahankan dari script lama
-- ---------------------------------------------------------------------
INSERT INTO users (username, password, nama_lengkap) VALUES
('admin',       'a735e89cc43f756daed5a66c21a9f0296f4b75834172eb8f0bf70eef2063f93a', 'Budi Santoso'),
('staff_front', '$2a$10$X7vQj5wD2nB5n4K1qW0Xeu1Jq3g2v5P9W8L8T7K6v5m4n3p2q1s0W',     'Siti Rahma');

-- ---------------------------------------------------------------------
-- 2. Rooms  (id 1..8)
-- Aturan: status 'Terisi' <=> punya tepat 1 kontrak 'Aktif'
-- ---------------------------------------------------------------------
INSERT INTO rooms (room_number, price, status) VALUES
('101', 1500000.00, 'Terisi'),     -- id 1: Andi (kontrak diperpanjang)
('102', 1500000.00, 'Terisi'),     -- id 2: Rina (nunggak)
('103', 1500000.00, 'Tersedia'),   -- id 3: bekas Bayu (kontrak selesai)
('104', 1500000.00, 'Tersedia'),   -- id 4: kontrak Sari dibatalkan
('201', 2200000.00, 'Perbaikan'),  -- id 5: sedang diperbaiki
('202', 2200000.00, 'Terisi'),     -- id 6: Dewi (penghuni baru)
('203', 2200000.00, 'Tersedia'),   -- id 7: belum pernah dihuni
('301', 3000000.00, 'Terisi');     -- id 8: Joko (kamar premium)

-- ---------------------------------------------------------------------
-- 3. Tenants  (id 1..7)
-- is_visible = FALSE -> penyewa diarsipkan (soft delete)
-- ---------------------------------------------------------------------
INSERT INTO tenants (id_number, name, email, phone_number, is_visible) VALUES
('3273010101950001', 'Andi Pratama',    'andi.pratama@gmail.com',   '081234567890', TRUE),   -- id 1: kontrak lama selesai + kontrak baru aktif
('3273025505980002', 'Rina Melati',     'rina.melati@yahoo.com',    '081987654321', TRUE),   -- id 2: tagihan sebagian & menunggak
('3171030303900003', 'Joko Widodo',     'joko.w@outlook.com',       '081122334455', TRUE),   -- id 3: selalu bayar (cicil 2x)
('3276044408000004', 'Dewi Lestari',    'dewi.lestari@gmail.com',   '082134567812', TRUE),   -- id 4: penghuni baru, tagihan pertama
('3578051207970005', 'Bayu Setiawan',   'bayu.setiawan@gmail.com',  '085712345678', FALSE),  -- id 5: sudah pindah, diarsipkan
('3374066709020006', 'Sari Wulandari',  NULL,                       '087812345690', TRUE),   -- id 6: kontrak dibatalkan, email kosong
('3273071511960007', 'Hendra Kusuma',   'hendra.k@gmail.com',       '089612345611', TRUE);   -- id 7: belum punya kontrak sama sekali

-- ---------------------------------------------------------------------
-- 4. Contracts  (id 1..7)
-- tanggal_jatuh_tempo = 1 hari sebelum tanggal_keluar
-- deposit = 1x harga kamar
-- ---------------------------------------------------------------------
INSERT INTO contracts (tenant_id, room_id, tanggal_masuk, tanggal_keluar, deposit, tanggal_jatuh_tempo, status) VALUES
(1, 1, '2025-01-01', '2025-12-31', 1500000.00, '2025-12-30', 'Selesai'),     -- id 1: Andi, kontrak lama
(1, 1, '2026-01-01', '2026-12-31', 1500000.00, '2026-12-30', 'Aktif'),       -- id 2: Andi, perpanjangan (hampir habis)
(2, 2, '2026-06-01', '2027-05-31', 1500000.00, '2027-05-30', 'Aktif'),       -- id 3: Rina
(3, 8, '2026-03-01', '2027-02-28', 3000000.00, '2027-02-27', 'Aktif'),       -- id 4: Joko
(4, 6, '2026-11-01', '2027-10-31', 2200000.00, '2027-10-30', 'Aktif'),       -- id 5: Dewi
(5, 3, '2026-01-01', '2026-06-30', 1500000.00, '2026-06-29', 'Selesai'),     -- id 6: Bayu, sudah pindah
(6, 4, '2026-09-01', '2027-08-31', 1500000.00, '2027-08-30', 'Dibatalkan');  -- id 7: Sari, batal sebelum pindah

-- ---------------------------------------------------------------------
-- 5. Bills  (id 1..11)  UNIQUE (contract_id, bulan_tagihan)
-- Konsistensi status vs total pembayaran:
--   Kelebihan     -> SUM(payments) > total_tagihan
--   Lunas         -> SUM(payments) = total_tagihan
--   Sebagian      -> 0 < SUM(payments) < total_tagihan
--   Belum Dibayar -> tidak ada payment
-- ---------------------------------------------------------------------
INSERT INTO bills (contract_id, bulan_tagihan, total_tagihan, status) VALUES
(1, '2025-12-01', 1500000.00, 'Lunas'),          -- id 1:  Andi, kontrak lama
(2, '2026-10-01', 1500000.00, 'Lunas'),          -- id 2:  Andi
(2, '2026-11-01', 1500000.00, 'Kelebihan'),      -- id 3:  Andi, kelebihan bayar 250rb (total bayar 1.750.000)
(3, '2026-09-01', 1500000.00, 'Lunas'),          -- id 4:  Rina
(3, '2026-10-01', 1500000.00, 'Sebagian'),       -- id 5:  Rina, baru bayar 700rb (lewat jatuh tempo)
(3, '2026-11-01', 1500000.00, 'Belum Dibayar'),  -- id 6:  Rina, belum bayar sama sekali
(4, '2026-10-01', 3000000.00, 'Lunas'),          -- id 7:  Joko
(4, '2026-11-01', 3000000.00, 'Lunas'),          -- id 8:  Joko, dibayar 2x cicilan
(5, '2026-11-01', 2200000.00, 'Belum Dibayar'),  -- id 9:  Dewi, tagihan pertama
(6, '2026-06-01', 1500000.00, 'Lunas'),          -- id 10: Bayu, tagihan terakhir
(7, '2026-09-01', 1500000.00, 'Belum Dibayar');  -- id 11: Sari, tagihan menggantung di kontrak batal

-- ---------------------------------------------------------------------
-- 6. Payments
-- ---------------------------------------------------------------------
INSERT INTO payments (bill_id, payment_date, jumlah) VALUES
(1,  '2025-12-04', 1500000.00),
(2,  '2026-10-02', 1500000.00),
(3,  '2026-11-03', 1750000.00),   -- kelebihan bayar Rp 250.000 (tagihan 1.500.000)
(4,  '2026-09-08', 1500000.00),
(5,  '2026-10-15',  700000.00),   -- pembayaran sebagian
(7,  '2026-10-10', 3000000.00),
(8,  '2026-11-05', 1500000.00),   -- cicilan 1
(8,  '2026-11-20', 1500000.00),   -- cicilan 2 (total = 3.000.000)
(10, '2026-06-05', 1500000.00);

-- ---------------------------------------------------------------------
-- 7. Expenses (3 bulan terakhir, bervariasi)
-- ---------------------------------------------------------------------
INSERT INTO expenses (expense_date, deskripsi, jumlah) VALUES
('2026-09-03', 'Electricity & Water',        1180000.00),
('2026-09-18', 'Cleaning Supplies',           135000.00),
('2026-10-02', 'Electricity & Water',        1220000.00),
('2026-10-14', 'Plumbing Repair Room 102',    475000.00),
('2026-10-28', 'Internet (WiFi) Monthly',     450000.00),
('2026-11-02', 'Maintenance',                 350000.00),
('2026-11-05', 'Electricity & Water',        1250000.00),
('2026-11-12', 'Cleaning Supplies',           150000.00),
('2026-11-18', 'AC Repair Room 201',          900000.00);
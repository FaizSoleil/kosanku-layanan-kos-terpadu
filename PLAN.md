# Kosanku: Layanan Kos Terpadu
---
## 1. Deskripsi

Kosanku adalah aplikasi desktop yang membantu pemilik kos mengelola propertinya dalam satu tempat: kamar, penghuni, pencatatan meteran utilitas, tagihan bulanan, dan pembayaran. Aplikasi ini menggantikan buku catatan kertas dengan sistem yang sederhana dan tertata.

**Pengguna:** Pemilik/Admin kos


## 2. Alur dan Fungsionalitas

**Alur keseluruhan:** Login > Dasbor > pilih menu (Kamar, Penghuni, Meteran, Tagihan, Pembayaran, Pengeluaran, Keluhan, Laporan).


**Fitur utama:**
- Manajemen kamar
- Data penghuni dan kontrak sewa
- Pencatatan utilitas (listrik, air, dll.)
- Tagihan bulanan otomatis
- Pencatatan pembayaran dan pengeluaran


## 3. Database

| Tabel | Isinya |
|---|---|
| **Users** | Akun pemilik/admin  |
| **Rooms** | Nomor kamar, tipe, harga, fasilitas, status |
| **Tenants** | Nama, nomor identitas, telepon, alamat asal, kontak darurat |
| **Contracts** | Siapa menyewa kamar mana, tanggal masuk/keluar, deposit, tanggal jatuh tempo |
| **Bills** | Per kontrak per bulan, dengan total dan status |
| **Payments** | Tanggal, jumlah, dan metode untuk setiap pembayaran atas tagihan |
| **Expenses** | Tanggal, kategori, jumlah (perbaikan, kebersihan, dll.) |

\**Ditambahkan lagi sesuai kebutuhan*


## 4. Class


- **UI:** objek-objek framework swing.
- **Controller:** menerima user input dan diteruskan ke logika yang tepat.
- **Service:** business logic-nya, eg. buat tagihan, validasi angka meteran, etc.
- **Akses data:** baca tulis MySQL.
- **Model:** objek data sederhana yang menyimpan data yang sama dengan database.

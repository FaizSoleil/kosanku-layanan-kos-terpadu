package kosanku.service;

import java.util.List;
import java.util.Optional;
import kosanku.dao.RoomsDAO;
import kosanku.model.Rooms;

public class KamarService {

    private final RoomsDAO roomsDAO = new RoomsDAO();

    public List<Rooms> getAll() {
        return roomsDAO.findAll();
    }

    public void create(Rooms newRoom) {
        validate(newRoom);
        Optional<Rooms> existing = roomsDAO.findByRoomNumber(newRoom.getRoomNumber());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                "Nomor kamar " + newRoom.getRoomNumber() + " sudah ada.");
        }
        if (!roomsDAO.insert(newRoom)) {
            throw new IllegalStateException("Gagal menyimpan kamar.");
        }
    }

    public void edit(Rooms updatedRoom) {
        validate(updatedRoom);
        // Another room may already use this number, the same room keeping it is fine.
        // If there's an existing room with the same room number but different id, then that roomNumber is already taken.
        Optional<Rooms> existing = roomsDAO.findByRoomNumber(updatedRoom.getRoomNumber());
        if (!isDifferent(existing, updatedRoom)) {
            throw new IllegalArgumentException("Nomor kamar " + updatedRoom.getRoomNumber() + " sudah dipakai kamar lain.");
        }
        if (!roomsDAO.update(updatedRoom)) {
            throw new IllegalStateException("Gagal mengubah kamar.");
        }
    }

    public void delete(int id) {
        // With ON DELETE RESTRICT, MySQL refuses if a contract points to this room.
        // How that failure shows up depends on your DAO.
        if (!roomsDAO.delete(id)) {
            throw new IllegalStateException("Kamar tidak bisa dihapus. Mungkin masih dipakai kontrak.");
        }
    }

    private boolean isDifferent(Optional<Rooms> existingRoom, Rooms newRoom) {
        return existingRoom.orElse(new Rooms()).getId() != newRoom.getId();
    }

    private void validate(Rooms room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            throw new IllegalArgumentException("Nomor kamar wajib diisi.");
        }
        if (Double.valueOf(room.getPrice()) == null || room.getPrice() <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0.");
        }
        if (room.getStatus() == null) {
            throw new IllegalArgumentException("Status wajib dipilih.");
        }
    }
}
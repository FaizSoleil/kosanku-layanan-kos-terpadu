package kosanku.service;

import java.util.List;
import java.util.Optional;
import kosanku.dao.RoomDAO;
import kosanku.dao.UserDAO;
import kosanku.model.Room;

public class RoomService {
    private final RoomDAO roomDAO;

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public List<Room> getAll() {
        return roomDAO.findAll();
    }

    /* Create doesn't require an id for the object newRoom */
    public void create(Room newRoom) {
        validate(newRoom);
        Optional<Room> existing = roomDAO.findByRoomNumber(newRoom.getRoomNumber());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                "Nomor kamar " + newRoom.getRoomNumber() + " sudah ada.");
        }
        if (!roomDAO.insert(newRoom)) {
            throw new IllegalStateException("Gagal menyimpan kamar.");
        }
    }

    public void edit(Room updatedRoom) {
        validate(updatedRoom);
        // Another room may already use this number, the same room keeping it is fine.
        // If there's an existing room with the same room number but different id, then that roomNumber is already taken.
        Optional<Room> existing = roomDAO.findByRoomNumber(updatedRoom.getRoomNumber());
        if (!isDifferent(existing, updatedRoom)) {
            throw new IllegalArgumentException("Nomor kamar " + updatedRoom.getRoomNumber() + " sudah dipakai kamar lain.");
        }
        if (!roomDAO.update(updatedRoom)) {
            throw new IllegalStateException("Gagal mengubah kamar.");
        }
    }

    public void delete(int id) {
        // With ON DELETE RESTRICT, MySQL refuses if a contract points to this room.
        // How that failure shows up depends on your DAO.
        if (!roomDAO.delete(id)) {
            throw new IllegalStateException("Kamar tidak bisa dihapus. Mungkin masih dipakai kontrak.");
        }
    }

    private boolean isDifferent(Optional<Room> existingRoom, Room newRoom) {
        return existingRoom.orElse(new Room()).getId() != newRoom.getId();
    }

    private void validate(Room room) {
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
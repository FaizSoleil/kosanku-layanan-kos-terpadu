package kosanku.controller;

import java.math.BigDecimal;
import java.util.List;
import kosanku.model.RoomStatus;
import kosanku.model.Rooms;
import kosanku.service.KamarService;

public class KamarController {

    private final KamarService service = new KamarService();

    public List<Rooms> loadAll() {
        return service.getAll();
    }

    public void create(String nomor, String hargaText, RoomStatus status) {
        service.create(buildRoom(0, nomor, hargaText, status));
    }

    public void edit(int id, String nomor, String hargaText, RoomStatus status) {
        service.edit(buildRoom(id, nomor, hargaText, status));
    }

    public void delete(int id) {
        service.delete(id);
    }

    private Rooms buildRoom(int id, String roomNumber, String priceText, RoomStatus status) {
        double price;
        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Harga harus berupa angka");
        }
        Rooms room = new Rooms();
        room.setId(id);
        room.setRoomNumber(roomNumber == null ? null : roomNumber.trim());
        room.setPrice(price);
        room.setStatus(status);
        return room;
    }
}
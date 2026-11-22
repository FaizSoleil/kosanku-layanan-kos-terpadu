package kosanku.controller;

import java.util.List;
import kosanku.model.RoomStatus;
import kosanku.model.Room;
import kosanku.service.RoomService;
import kosanku.ui.panel.RoomPanel;

public class RoomController {
    private final RoomService roomService;
    private RoomPanel roomView;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public void setRoomView(RoomPanel roomView) {
        this.roomView = roomView;
    }

    public List<Room> loadAll() {
        return roomService.getAll();
    }

    public void create(Integer id, String nomor, String hargaText, RoomStatus status) {
        roomService.create(buildRoom(id, nomor, hargaText, status));
    }

    public void create(Integer id, String nomor, String hargaText) {
        roomService.create(buildRoom(id, nomor, hargaText));
    }

    public void create(String nomor, String hargaText, RoomStatus status) {
        roomService.create(buildRoom(null, nomor, hargaText, status));
    }

    public void create(String nomor, String hargaText) {
        roomService.create(buildRoom(null, nomor, hargaText));
    }

    public void edit(int id, String nomor, String hargaText, RoomStatus status) {
        roomService.edit(buildRoom(id, nomor, hargaText, status));
    }

    public void delete(int id) {
        roomService.delete(id);
    }

    //id omitted
    private Room buildRoom(String roomNumber, String priceText) {
        double price;
        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Harga harus berupa angka");
        }
        Room room = new Room();
        room.setId(null);
        room.setRoomNumber(roomNumber == null ? null : roomNumber.trim());
        room.setPrice(price);
        room.setStatus(RoomStatus.TERSEDIA);
        return room;
    }

    private Room buildRoom(String roomNumber, String priceText, RoomStatus status) {
        double price;
        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Harga harus berupa angka");
        }
        Room room = new Room();
        room.setId(null);
        room.setRoomNumber(roomNumber == null ? null : roomNumber.trim());
        room.setPrice(price);
        room.setStatus(status);
        return room;
    }
    
    //Default availability status
    private Room buildRoom(Integer id, String roomNumber, String priceText) {
        double price;
        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Harga harus berupa angka");
        }
        Room room = new Room();
        room.setId(id);
        room.setRoomNumber(roomNumber == null ? null : roomNumber.trim());
        room.setPrice(price);
        room.setStatus(RoomStatus.TERSEDIA);
        return room;
    }

    //Specific availability status
    private Room buildRoom(Integer id, String roomNumber, String priceText, RoomStatus status) {
        double price;
        try {
            price = Double.parseDouble(priceText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Harga harus berupa angka");
        }
        Room room = new Room();
        room.setId(id);
        room.setRoomNumber(roomNumber == null ? null : roomNumber.trim());
        room.setPrice(price);
        room.setStatus(status);
        return room;
    }
}
package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.domain.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponse {
    private Long id;
    private String name;
    private String type;
    private int price;
    private byte[] image;
    private String barcode;
    private int quantity;
    private LocationResponse location;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getItem().getName();
        this.type = item.getItem().getType();
        this.price = item.getItem().getPrice();
        this.image = item.getItem().getImage();
        this.barcode = item.getItem().getBarcode();
        this.quantity = item.getQuantity();
    }

    public ItemResponse(Long id, String name, String type, int price, byte[] image, String barcode, int quantity, LocationResponse location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.image = image;
        this.barcode = barcode;
        this.quantity = quantity;
//        this.location = location;
    }
    //    public ItemResponse(Item item) {
//        id =
//    }


    public ItemResponse(Long id, LocationResponse location) {
        this.id = id;
        this.location = location;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }
}
package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.domain.CenterItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CenterItemResponse {
    private Long id;
    private String name;
    private String type;
    private int price;
    private byte[] image;
    private String barcode;

    public CenterItemResponse(Long id, String name, String type, int price, byte[] image, String barcode) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.image = image;
        this.barcode = barcode;
    }

    public CenterItemResponse(CenterItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.type = item.getType();
        this.price = item.getPrice();
        this.image = item.getImage();
        this.barcode = item.getBarcode();
    }
}
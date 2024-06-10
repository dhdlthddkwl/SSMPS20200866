package capstone_design_1.ssmps_backend.dto;


import capstone_design_1.ssmps_backend.dto.store.AddStoreRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private AddStoreRequest store;
    private String name;
    private String type;
    private int price;
    private int quantity;
    private String image;
    private String barcode;

    public ItemRequest(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public ItemRequest(Long id, AddStoreRequest store, String name, String type, int price, int quantity, String image, String barcode) {
        this.id = id;
        this.store = store;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.barcode = barcode;
    }
}
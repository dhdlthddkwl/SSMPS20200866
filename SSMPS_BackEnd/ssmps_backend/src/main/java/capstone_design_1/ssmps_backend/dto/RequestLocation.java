package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.domain.Item;
import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Store;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.Request;

import java.util.List;


@Getter
@Setter
@Data
public class RequestLocation {
    private Store store;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private List<ItemRequest> itemList;

    public RequestLocation(Store store, float startX, float startY, float endX, float endY, List<ItemRequest> itemList) {
        this.store = store;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.itemList = itemList;
    }

//    public Location toEntity(){
//        Location location = new Location(this.store);
//    }

}

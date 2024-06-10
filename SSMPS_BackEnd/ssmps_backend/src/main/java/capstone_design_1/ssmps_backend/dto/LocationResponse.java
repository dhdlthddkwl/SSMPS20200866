package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.domain.Location;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Data
public class LocationResponse {
    private Long id;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private List<ItemResponse> itemList;

    public LocationResponse(Long id, float startX, float startY, float endX, float endY, List<ItemResponse> itemList) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.itemList = itemList;
    }
    public LocationResponse(Location location){
        this.id = location.getId();
        this.startX = location.getStartX();
        this.startY = location.getStartY();
        this.endX = location.getEndX();
        this.endY = location.getEndY();
        this.itemList = location.getItemList().stream().map(i -> new ItemResponse(i)).collect(Collectors.toList());
    }

    public LocationResponse(Long id, float startX, float startY, float endX, float endY) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
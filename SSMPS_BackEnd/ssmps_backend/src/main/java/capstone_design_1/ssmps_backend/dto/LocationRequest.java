package capstone_design_1.ssmps_backend.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    private Long id;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private List<ItemRequest> itemList;

    private float centerX;
    private float centerY;

    public LocationRequest(Long id, float startX, float startY, float endX, float endY) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public LocationRequest(Long id, float startX, float startY, float endX, float endY, List<ItemRequest> itemList) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.itemList = itemList;
    }
}



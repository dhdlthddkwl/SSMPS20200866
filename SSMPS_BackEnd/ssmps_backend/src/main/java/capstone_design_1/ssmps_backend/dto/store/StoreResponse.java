package capstone_design_1.ssmps_backend.dto.store;


import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.dto.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StoreResponse {
    private Long id;
    private String name;
    private String address;
    private List<LocationResponse> locationList;
}

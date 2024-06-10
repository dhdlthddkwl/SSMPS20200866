package capstone_design_1.ssmps_backend.dto.store;


import capstone_design_1.ssmps_backend.dto.LocationRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Data
@Slf4j
public class StoreRequest {
    private Long id;
    private String name;
    private String address;
    private List<LocationRequest> locationList;
}

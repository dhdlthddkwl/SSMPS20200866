package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.dto.store.StoreRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Data
@AllArgsConstructor
public class AddLocationRequest {
    List<LocationRequest> locationList;
    StoreRequest store;
}

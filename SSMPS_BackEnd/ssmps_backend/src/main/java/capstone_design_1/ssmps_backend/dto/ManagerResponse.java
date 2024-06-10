package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.dto.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ManagerResponse {
    private Long id;
    private String accountId;
    private List<StoreResponse> storeList;

    private String token;
}

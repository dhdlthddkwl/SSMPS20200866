package capstone_design_1.ssmps_backend.dto;

import capstone_design_1.ssmps_backend.dto.store.AddStoreRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Data
public class ManagerRequest {
    private String accountId;
    private String password;
    private List<AddStoreRequest> storeList;

    public ManagerRequest(String id, String password, List<AddStoreRequest> storeList) {
        this.accountId = id;
        this.password = password;
        this.storeList = storeList;
    }
}

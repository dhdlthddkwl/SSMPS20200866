package capstone_design_1.ssmps_backend.dto.store;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AddStoreRequest {
    private Long id;
    private String name;
    private String address;
}

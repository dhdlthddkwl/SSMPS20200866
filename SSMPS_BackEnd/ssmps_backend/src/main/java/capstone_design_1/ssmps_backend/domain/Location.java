package capstone_design_1.ssmps_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "store_id")
//    private Store store;
    @Column(name = "start_x")
    private float startX;
    @Column(name = "start_y")
    private float startY;
    @Column(name = "end_x")
    private float endX;

    @Column(name = "end_y")
    private float endY;

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Item> itemList = new ArrayList<>();
}
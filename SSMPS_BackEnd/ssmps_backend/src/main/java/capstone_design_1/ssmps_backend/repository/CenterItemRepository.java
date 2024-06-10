package capstone_design_1.ssmps_backend.repository;

import capstone_design_1.ssmps_backend.domain.CenterItem;
import capstone_design_1.ssmps_backend.dto.CenterItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class CenterItemRepository {
    private final EntityManager em;
    public List<CenterItem> findAllCenterItem(){
        return em.createQuery("select ci from CenterItem ci")
                .getResultList();
    }

    public List<CenterItem> findCenterItemByName(String itemName) {
        return em.createQuery("select ci from CenterItem ci where ci.name like :name")
                .setParameter("name", "%" + itemName + "%")
                .getResultList();
    }

    public CenterItem findCenterItem(Long id) {
        return em.find(CenterItem.class, id);
    }

    public void insertItem(CenterItem centerItem) {
        em.persist(centerItem);
    }
}

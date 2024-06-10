package capstone_design_1.ssmps_backend.repository;

import capstone_design_1.ssmps_backend.domain.Manager;
import capstone_design_1.ssmps_backend.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoginRepository {
    private final EntityManager em;
    public Manager join(Manager manager, List<Store> list){
        for(Store s : list){
            em.persist(s);
        }
        return manager;
    }

    public Optional<Manager> findManagerByAccountId(String accountId){
        List<Manager> findManagerList = em.createQuery("select m from Manager m where m.accountId = :id")
                .setParameter("id", accountId)
                .getResultList();
        return findManagerList.stream().findFirst();
    }

    public Manager findManagerById(Long id){
        return em.find(Manager.class, id);
    }
}
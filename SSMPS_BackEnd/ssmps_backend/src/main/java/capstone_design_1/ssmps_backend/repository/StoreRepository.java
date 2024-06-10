package capstone_design_1.ssmps_backend.repository;


import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Manager;
import capstone_design_1.ssmps_backend.domain.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StoreRepository {

    private final EntityManager em;
    public List<Location> findLocationList(Long id) {
        return em.createQuery("select l from Location l where store.id = :store")
                .setParameter("store", id)
                .getResultList();
    }

    public List<Store> findAllStoreById(Manager manager) {
        return em.createQuery("select s from Store s where s.manager = :manager")
                .setParameter("manager", manager)
                .getResultList();
    }

    public Store findStore(Long id) {
        return em.find(Store.class, id);
    }

    public List<Store> findAllStore(){
        return em.createQuery("select s from Store s").getResultList();
    }

    public Store registStoreLocation(Store store){
        for(Location l : store.getLocationList()){
            em.persist(l); // Location 저장
        }
        return store;
    }

    public void saveLocation(Location l) {
        em.persist(l);
    }

    public List<Store> findStoreByName(String storeName){
        return em.createQuery("select s from Store s where s.name like :name")
                .setParameter("name", "%" + storeName + "%")
                .getResultList();
    }

    public Optional<Store> findStoreName(String name){
        List<Store> findList = em.createQuery("select s from Store s where s.name = :name")
                .setParameter("name", name)
                .getResultList();
        return findList.stream().findFirst();
    }

    public List<Store> findManagerStoreByName(String storeName, Long managerId){
        return em.createQuery("select s from Store s where s.name = :name and s.manager.id = :managerId")
                .setParameter("name", storeName)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    public Location findLocationById(Long locationId) {
        return em.find(Location.class, locationId);
    }

    public void deleteLocation(Location findLocation) {
        em.remove(findLocation);
    }
}

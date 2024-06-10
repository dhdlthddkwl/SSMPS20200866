package capstone_design_1.ssmps_backend.repository;


import capstone_design_1.ssmps_backend.domain.CenterItem;
import capstone_design_1.ssmps_backend.domain.Item;
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
public class ItemRepository {
    private final EntityManager em;
    public Optional<CenterItem> findItemByBarcode(String barcode){
        List<CenterItem> findItems = em.createQuery("select i from CenterItem i where i.barcode = :barcode", CenterItem.class)
                .setParameter("barcode", barcode)
                .getResultList();
        return findItems.stream().findFirst();
    }
    public Item addNewItem(Item item){
        em.persist(item);
        return item;
    }
    public List<Item> findAllList(Store store) {
        List findItemList = em.createQuery("select i from Item i where i.store = :store")
                .setParameter("store", store)
                .getResultList();
        return findItemList;
    }

    public Item findItemById(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findItemByName(String name, Store store) {
        return em.createQuery("select i from Item i where i.item.name like :name and i.store = :store")
                .setParameter("name", "%" + name + "%")
                .setParameter("store", store)
                .getResultList();
    }
    public List<CenterItem> findStoreItem(String name){
        return em.createQuery("select i from CenterItem i where i.name = :name")
                .setParameter("name", name)
                .getResultList();
    }

    public Item deleteItem(Item deleteItem) {
        em.remove(deleteItem);
        return deleteItem;
    }

    public void updateLocation(Long id, Long locationId) {
    }

    public List<Item> findItemName(String itemName, Store store) {
        return em.createQuery("select i from Item i where i.item.name = :name and i.store = :store")
                .setParameter("name", itemName)
                .setParameter("store", store)
                .getResultList();
    }
}
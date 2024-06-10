package capstone_design_1.ssmps_backend.service;


import capstone_design_1.ssmps_backend.domain.CenterItem;
import capstone_design_1.ssmps_backend.domain.Item;
import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.ItemRequest;
import capstone_design_1.ssmps_backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {
    private final ItemRepository itemRepository;

    // 바코드 인식
    public CenterItem getItemByBarcode(String barcode){
        Optional<CenterItem> findItem = itemRepository.findItemByBarcode(barcode);
        CenterItem centerItem = findItem.orElseThrow(() -> new NoSuchElementException());
        return centerItem;
    }

    // 제품 추가
    @Transactional
    public Item addItem(Item item) {
//        Optional<Item> findItem = itemRepository.findItemByBarcode(item.getBarcode());
//        if(!findItem.isEmpty() && item.checkRedundancy(findItem.get())){
//            findItem.get().addQuantity();
//            return findItem.get();
//        }
        return itemRepository.addNewItem(item);
    }
    // 제품 전체 리스트 가져오기
    public List<Item> getItemList(Store store) {
        return itemRepository.findAllList(store);
    }

    public Item getItemById(Long id){
        return itemRepository.findItemById(id);
    }

    public List<Item> getItemByName(String name, Store store) {
        return itemRepository.findItemByName(name, store);
    }
    public Item getItemName(String itemName, Store store){
        List<Item> findItemList = itemRepository.findItemName(itemName, store);
        Item findItem = findItemList.stream().findFirst().orElseThrow(() -> new IllegalArgumentException());
        return findItem;
    }

    public CenterItem getStoreItem(String name){
        List<CenterItem> storeItem = itemRepository.findStoreItem(name);
        CenterItem item = storeItem.stream().findFirst().orElseThrow(() -> new IllegalArgumentException());
        return item;
    }

    @Transactional
    public Item updateQuantity(Item updateItem, int quantity) {
        updateItem.setQuantity(quantity);
        return updateItem;
    }

    @Transactional
    public Item deleteItem(Item deleteItem) {
        return itemRepository.deleteItem(deleteItem);
    }


    @Transactional
    public Location updateLocation(List<Item> findItem, Location location) {
        location.setItemList(findItem);
        return location;
    }
}
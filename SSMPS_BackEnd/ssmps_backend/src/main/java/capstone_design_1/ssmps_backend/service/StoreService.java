package capstone_design_1.ssmps_backend.service;

import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Manager;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.LocationRequest;
import capstone_design_1.ssmps_backend.repository.LoginRepository;
import capstone_design_1.ssmps_backend.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final LoginRepository loginRepository;
    public List<Location> getLocationList(Long id) {
        return storeRepository.findLocationList(id);
    }

    public List<Store> findAllStoreById(Long managerId) {
        Manager findManager = loginRepository.findManagerById(managerId);
        return storeRepository.findAllStoreById(findManager);
    }

    public Store findStoreById(Long id){
        return storeRepository.findStore(id);
    }

//    @Transactional
//    public Store updateStore(Store store){
//        Store findStore = storeRepository.findStore(store.getId());
//        findStore = store;
//    }

    @Transactional
    public List<Location> registStoreLocation(Long storeId, List<Location> locationList){
        Store changeStore = storeRepository.findStore(storeId);
        for(Location l : locationList){
            storeRepository.saveLocation(l);
        }
        changeStore.setLocationList(locationList);
        return locationList;
    }

    public List<Store> findAllStore(){
        return storeRepository.findAllStore();
    }

    public List<Store> findStoreByName(String storeName){
        return storeRepository.findStoreByName(storeName);
    }

    public Store findStoreName(String name){
        Optional<Store> storeName = storeRepository.findStoreName(name);
        Store store = storeName.orElseThrow(() -> new IllegalArgumentException());
        return store;
    }

    public List<Store> findManagerStoreByName(String storeName, Long managerId){
        return storeRepository.findManagerStoreByName(storeName, managerId);
    }

    public Location findLocationById(Long locationId) {
        return storeRepository.findLocationById(locationId);
    }

    @Transactional
    public Location deleteLocation(Location findLocation) {
        storeRepository.deleteLocation(findLocation);
        return findLocation;
    }

    @Transactional
    public Location updateLocation(Location findLocation, Location location) {
        findLocation.setItemList(location.getItemList());
        findLocation.setStartX(location.getStartX());
        findLocation.setStartY(location.getStartY());
        findLocation.setEndX(location.getEndX());
        findLocation.setEndY(location.getEndY());
        return findLocation;
    }

    @Transactional
    public Store addLocation(Store store, Location location){
        store.getLocationList().add(location);
        return store;
    }
}

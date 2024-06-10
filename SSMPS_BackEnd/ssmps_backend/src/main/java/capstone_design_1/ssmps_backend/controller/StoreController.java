package capstone_design_1.ssmps_backend.controller;

import capstone_design_1.ssmps_backend.domain.CenterItem;
import capstone_design_1.ssmps_backend.domain.Item;
import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.*;
import capstone_design_1.ssmps_backend.dto.store.StoreRequest;
import capstone_design_1.ssmps_backend.dto.store.StoreResponse;
import capstone_design_1.ssmps_backend.service.ItemService;
import capstone_design_1.ssmps_backend.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;
    private final ItemService itemService;

    @GetMapping("/api/location")
    public ResponseEntity<Object> getLocationList(@RequestParam Long storeId){
        List<Location> locationList = storeService.getLocationList(storeId);
//        locationList.stream()
//                .map(l -> new LocationResponse(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getItemList())))
        return ResponseEntity.ok(null);
    }

//    @PostMapping("/api/location")
//    public ResponseEntity<Object> addLocation(@RequestBody AddLocationRequest locationRequest){
////        storeService.addLocation();
//        StoreRequest store = locationRequest.getStore();
////        Store nowStore = new Store(store.getId(), store.getName(), store.getAddress(), store.)
//        List<LocationRequest> locationList = locationRequest.getLocationList();
//
////        locationList.stream()
////                        .map(l -> new Location(null, ))
////        storeService.addLocation();
//        return ResponseEntity.ok(null);
//    }


    // 매니저 스토어 찾기
    @GetMapping("api/storeList/{id}")
    public ResponseEntity<Object> findAllStore(@PathVariable Long id){
        List<Store> allStore = storeService.findAllStoreById(id);
        List<StoreResponse> resultList = allStore.stream()
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList().stream()
                .map(l -> new LocationResponse(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getItemList().stream()
                .map(i -> new ItemResponse(i)).collect(Collectors.toList()))).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }

//     로케이션 찾기
    @GetMapping("api/store/location/{id}")
    public ResponseEntity<Object> findStoreLocation(@PathVariable(name = "id") Long storeId){
        Store store = storeService.findStoreById(storeId);
        log.error("here");
        log.error(store.getName());
        List<LocationResponse> resultList = store.getLocationList().stream()
                .map(l -> new LocationResponse(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getItemList().stream()
                        .map(i -> new ItemResponse(i)).collect(Collectors.toList()))).collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }

    @PostMapping("api/store/location")
    public ResponseEntity<Object> registStoreLocation(@RequestParam(name = "store_id") Long storeId, @RequestBody LocationRequest locationRequest){
        Store findStore = storeService.findStoreById(storeId);
        Location location = new Location(locationRequest.getId(), locationRequest.getStartX(), locationRequest.getStartY(),
                locationRequest.getEndX(), locationRequest.getEndY(), null);
        Store store = storeService.addLocation(findStore, location);
        return ResponseEntity.ok("good");
    }

    @DeleteMapping("api/store/location")
    public String deleteStoreLocation(@RequestParam(name = "location_id") Long locationId){
        Location findLocation = storeService.findLocationById(locationId);
        Location deletedLocation = storeService.deleteLocation(findLocation);
        return "good";
    }

    @PutMapping("api/store/location")
    public String updateStoreLocation(@RequestBody LocationRequest locationRequest){
        Location findLocation = storeService.findLocationById(locationRequest.getId());
        Location location = new Location(locationRequest.getId(), locationRequest.getStartX(),
                locationRequest.getStartY(), locationRequest.getEndX(), locationRequest.getEndY(), null);

        Location updatedLocation = storeService.updateLocation(findLocation, location);
        return "good";
    }

    @GetMapping("api/storeList")
    public List<StoreResponse> findAllStore(){
        List<Store> findAllStore = storeService.findAllStore();
        List<StoreResponse> resultList = findAllStore.stream()
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList().stream()
                .map(l -> new LocationResponse(l)).collect(Collectors.toList()))).collect(Collectors.toList());
        return resultList;
    }

//    @GetMapping("api/manager/store/{name}")
//    public List<StoreResponse> findManagerStoreByName(@PathVariable(name = "name") String storeName, @RequestParam(name = "id") Long managerId){
//        List<Store> findStoreList = storeService.findManagerStoreByName(storeName, managerId);
//        List<StoreResponse> resultList = findStoreList.stream()
//                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList().stream()
//                .map(l -> new LocationResponse(l)).collect(Collectors.toList())))
//                .collect(Collectors.toList());
//        return resultList;
//    }

    @GetMapping("api/store/{name}")
    public List<StoreResponse> findStoreByName(@PathVariable String name){
        log.error("name: {}", name);
        List<Store> findStoreList = storeService.findStoreByName(name);
        log.error("find size: {}", findStoreList.size());
        List<StoreResponse> resultList = findStoreList.stream()
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList().stream()
                .map(l -> new LocationResponse(l)).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return resultList;
    }

    @PutMapping("api/location/item/{id}")
    public LocationResponse modifyItemLocation(@RequestBody List<ItemRequest> itemList, @PathVariable("id") Long locationId){
        List<Item> findItemList = new ArrayList<>();
        for(int i = 0;i < itemList.size();i++){
            findItemList.add(itemService.getItemById(itemList.get(i).getId()));
        }
        Location findLocation = storeService.findLocationById(locationId);
        Location location = itemService.updateLocation(findItemList, findLocation);
        return new LocationResponse(location.getId(), location.getStartX(), location.getStartY(), location.getEndX(), location.getEndY());
    }
}
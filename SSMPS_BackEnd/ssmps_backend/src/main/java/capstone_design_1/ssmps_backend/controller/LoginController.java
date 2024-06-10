package capstone_design_1.ssmps_backend.controller;

import capstone_design_1.ssmps_backend.domain.Manager;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.ManagerRequest;
import capstone_design_1.ssmps_backend.dto.ManagerResponse;
import capstone_design_1.ssmps_backend.dto.store.StoreResponse;
import capstone_design_1.ssmps_backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@Slf4j
public class LoginController {
    private final LoginService managerService;

    @PostMapping("/api/join")
    public ManagerResponse join(@RequestBody ManagerRequest manager){
        // 회원가입
        Manager joinManager = new Manager(null, manager.getAccountId(), manager.getPassword(), null);
        List<Store> storeList = manager.getStoreList().stream()
                .map(s -> new Store(null, s.getName(), s.getAddress(), joinManager, null, null)).collect(Collectors.toList());
        Manager joinedManager = managerService.join(joinManager, storeList);
//        List<StoreResponse> storeListRes = joinedManager.getStores().stream().map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), null)).collect(Collectors.toList());
        ManagerResponse resultManager = new ManagerResponse(joinedManager.getId(), joinedManager.getAccountId(), null, null);
        return resultManager;
    }
    @GetMapping("/api/manager/login")
    public ManagerResponse login(@RequestParam String id, @RequestParam String password){
        // 토큰 로그인
        Manager loginManager = new Manager(null, id, password, null);
//        return ResponseEntity.ok(managerService.login(loginManager));
        return managerService.login(loginManager);
    }

    @GetMapping("/api/loginFirst")
    public ManagerResponse loginFirst(@RequestParam String id, @RequestParam String password){
        // 최초 로그인
        Manager loginManager = new Manager(null, id, password, null);
        return managerService.login(loginManager);
    }

    @GetMapping("/api/loginFirst/test")
    public String loginTest(){
//        Manager loginManager = new Manager(null, id, password, null);
        return "good";
//        String token = managerService.login(loginManager);
//        // 로그인
//        return token;
    }

//    @GetMapping("/api/loginFirst")
//    public String loginFirst(){
////        Manager loginManager = new Manager(null, id, password, null);
////        // 로그인
//////        return ResponseEntity.ok(managerService.login(loginManager));
//        return "hello";
//    }

}

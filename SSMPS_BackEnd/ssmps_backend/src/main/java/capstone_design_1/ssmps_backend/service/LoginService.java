package capstone_design_1.ssmps_backend.service;

import capstone_design_1.ssmps_backend.domain.Manager;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.ItemResponse;
import capstone_design_1.ssmps_backend.dto.LocationResponse;
import capstone_design_1.ssmps_backend.dto.ManagerResponse;
import capstone_design_1.ssmps_backend.dto.store.StoreResponse;
import capstone_design_1.ssmps_backend.repository.LoginRepository;
import capstone_design_1.ssmps_backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginService {
    private final LoginRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Manager join(Manager manager, List<Store> list) {
        String encodedPassword = passwordEncoder.encode(manager.getPassword());
        Manager encodedManager = new Manager(null, manager.getAccountId(), encodedPassword, manager.getStores());
        for(Store s : list){
            s.setManager(encodedManager);
        }
        return managerRepository.join(encodedManager, list);
    }
//
    public ManagerResponse login(Manager manager){
        Manager findManager = managerRepository.findManagerByAccountId(manager.getAccountId())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if(passwordEncoder.matches(manager.getPassword(), findManager.getPassword())){
            String token = jwtTokenProvider.createToken(manager.getAccountId());

            List<StoreResponse> storeRes = findManager.getStores().stream()
                    .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList().stream()
                    .map(l -> new LocationResponse(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getItemList().stream()
                    .map(i -> new ItemResponse(i)).collect(Collectors.toList()))).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
            return new ManagerResponse(findManager.getId(), findManager.getAccountId(), storeRes, token);
        }else{
            throw new BadCredentialsException("로그인 정보가 일치하지 않습니다.");
        }
    }
}
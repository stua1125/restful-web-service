package com.kakaopay.restfulwebservice.controller;

import com.kakaopay.restfulwebservice.RandomGenerator.RandomMaker;
import com.kakaopay.restfulwebservice.models.Receive;
import com.kakaopay.restfulwebservice.repository.ReceiveRepository;
import com.kakaopay.restfulwebservice.repository.SpreadRepository;
import com.kakaopay.restfulwebservice.uesr.Spread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class JpaController {

    static int Sq_no = 0;

    @Autowired
    private SpreadRepository spreadRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @GetMapping("/spread")
    public List<Spread> allSpread(){
        return spreadRepository.findAll();
    }

    @GetMapping("/spread/{token}")
    public Spread spread(
            @PathVariable String token,
            @RequestHeader(value = "X_USER_ID") String X_USER_ID,
            @RequestHeader(value = "X_ROOM_ID") String X_ROOM_ID
    ){
        Optional<Spread> spread = spreadRepository.findById(token);

        return spread.get();
    }

    @Transactional
    @PostMapping("/spread")
    public Map<String, String> spread(
            @Valid @RequestBody Spread spread,
            @RequestHeader(value = "X_USER_ID") String X_USER_ID,
            @RequestHeader(value = "X_ROOM_ID") String X_ROOM_ID){

        spread.setSpreadDate(new Date());
        String ranToken = RandomMaker.getRandomString();
        spread.setToken(ranToken);
        spread.setX_USER_ID(Integer.parseInt(X_USER_ID));
        spread.setX_ROOM_ID(X_ROOM_ID);

        List<Receive> receiveList = new ArrayList<Receive>();
        for(int i = 1; i <= spread.getNumOfPeople(); i++){
            Receive receiveItem = new Receive();
            receiveItem.setToken(spread);
            if(i == spread.getNumOfPeople()){
                receiveItem.setReceiveOfMoney((spread.getAmountOfMoney()/spread.getNumOfPeople()) + spread.getAmountOfMoney()%i);
            } else {
                receiveItem.setReceiveOfMoney(spread.getAmountOfMoney()/spread.getNumOfPeople());
            }
            receiveItem.setReceive_room_id(X_ROOM_ID);
            receiveItem.setTokenValue(ranToken);
            int userSq = i;
            if(userSq == Integer.parseInt(X_USER_ID)){
                userSq++;
            }
            receiveItem.setReceive_id(userSq);

            receiveList.add(receiveItem);
        }
        spread.setTakeMoneyList(receiveList);
        Spread savedSpread = spreadRepository.save(spread);

        return new HashMap<String, String>(){{
            put("Token", spread.getToken());
        }};

    }

    @Transactional
    @PatchMapping("/takeMoney/{token}")
    public String takeMoney(
            @RequestParam(value ="token")String token,
            @RequestHeader(value = "X_USER_ID") String X_USER_ID,
            @RequestHeader(value = "X_ROOM_ID") String X_ROOM_ID){

        Optional<Receive> receive = receiveRepository.findByTokenValue(token);

        receive.ifPresent(selectReceive -> {
                selectReceive.setSq(++Sq_no);
                selectReceive.setTokenValue(token);
                selectReceive.setReceive_id(Integer.parseInt(X_USER_ID));
                selectReceive.setReceive_room_id(X_ROOM_ID);
                receiveRepository.save(selectReceive);
        });
        return "sucess";
    }

}

package com.kakaopay.restfulwebservice.controller;

import com.kakaopay.restfulwebservice.RandomGenerator.RandomMaker;
import com.kakaopay.restfulwebservice.exception.ExceptionStatus;
import com.kakaopay.restfulwebservice.exception.SpreadNotFoundException;
import com.kakaopay.restfulwebservice.models.Receive;
import com.kakaopay.restfulwebservice.repository.ReceiveRepository;
import com.kakaopay.restfulwebservice.repository.SpreadRepository;
import com.kakaopay.restfulwebservice.models.Spread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    ) throws ParseException {
        Optional<Spread> spread = spreadRepository.findByToken(token);
        if(Objects.isNull(spread)){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR508);
        }

        if(spread.get().getX_USER_ID() != (Integer.parseInt(X_USER_ID))){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR505);
        }

        if(isOverSevenDays(spread.get())){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR506);
        }

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
            receiveList.add(receiveItem);
        }
        spread.setTakeMoneyList(receiveList);
        Spread savedSpread = spreadRepository.save(spread);

        return new HashMap<String, String>(){{
            put("Token", spread.getToken());
        }};

    }

    @Transactional
    @PatchMapping("/takeMoney")
    public int[] takeMoney(
            @RequestParam(value ="token")String token,
            @RequestHeader(value = "X_USER_ID") String X_USER_ID,
            @RequestHeader(value = "X_ROOM_ID") String X_ROOM_ID) throws ParseException {

        Optional<Receive> receive = receiveRepository.findByTokenValue(token);
        Optional<Spread> spread = spreadRepository.findByToken(token);

        if(Objects.isNull(spread)){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR508);
        }

        if(isOverTenMin(spread.get())){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR503);
        }

        if(!spread.get().getX_ROOM_ID().equals(X_ROOM_ID)){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR502);
        }

        if(spread.get().getX_USER_ID() == (Integer.parseInt(X_USER_ID))){
            throw new SpreadNotFoundException(ExceptionStatus.ERORR501);
        }

        final int[] money = {0};
        receive.ifPresent(selectReceive -> {
                selectReceive.setSq(++Sq_no);
                selectReceive.setTokenValue(token);
                selectReceive.setReceive_id(Integer.parseInt(X_USER_ID));
                selectReceive.setReceive_room_id(X_ROOM_ID);
                money[0] = selectReceive.getReceiveOfMoney();
                receiveRepository.flush();
        });
        return money;
    }

    public boolean isOverSevenDays(Spread spread) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date spreadDate = format.parse(String.valueOf(spread.getSpreadDate()));
        Date nowDate = new Date();
        long calDate = nowDate.getTime() - spreadDate.getTime();
        long calDateDays = calDate / (24*60*60*1000);
        calDateDays = Math.abs(calDateDays);
        return false;
    }

    public boolean isOverTenMin(Spread spread) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date spreadDate = format.parse(String.valueOf(spread.getSpreadDate()));
        Date nowDate = new Date();
        long calDate = nowDate.getTime() - spreadDate.getTime();
        long calDateDays = calDate / (60*1000);
        calDateDays = Math.abs(calDateDays);
        return false;
    }
}

package com.kakaopay.restfulwebservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.restfulwebservice.models.Receive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spread")
public class Spread {

    @Id
    private String token;

    @JsonIgnore
    private Integer X_USER_ID;

    @JsonIgnore
    private String X_ROOM_ID;
    private Integer amountOfMoney;
    private Integer numOfPeople;
    private Date SpreadDate;

    @JsonIgnore
    @OneToMany(mappedBy = "token",  cascade = CascadeType.ALL)
    private List<Receive> takeMoneyList = new ArrayList<>();

    public List<Receive> getReceive() {
        if (Objects.isNull(takeMoneyList)) {
            takeMoneyList = new ArrayList<>();
        }
        return takeMoneyList;
    }
}

package com.kakaopay.restfulwebservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.restfulwebservice.uesr.Spread;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Receive implements Serializable {

    @Id
    @GeneratedValue
    private Integer sq;

    private String tokenValue;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spread_token")
    private Spread token;

    private Integer receive_id;

    private String receive_room_id;

    private Integer receiveOfMoney;




}

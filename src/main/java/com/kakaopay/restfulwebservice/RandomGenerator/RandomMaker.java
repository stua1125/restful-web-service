package com.kakaopay.restfulwebservice.RandomGenerator;

import antlr.build.Tool;

import java.util.Random;

public class RandomMaker {
    public static String getRandomString() {
        Random ran = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 3; i++){
            int index = ran.nextInt(3);
            switch(index){
                case 0:
                    sb.append((char)((Math.random() * 26) + 97));
                    break;
                case 1:
                    sb.append((char)((Math.random() * 26)  + 65));
                    break;
                case 2:
                    sb.append((int)((Math.random()*10000)%10));
                    break;
            }
        }
        return sb.toString();
    }
}

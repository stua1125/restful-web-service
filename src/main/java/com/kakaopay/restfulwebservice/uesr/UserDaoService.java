package com.kakaopay.restfulwebservice.uesr;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 메모리 데이터베이스
@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1,"Alice", new Date()));
        users.add(new User(2,"Sona", new Date()));
        users.add(new User(3,"Lee", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if(user.getId() == null) {
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for( User item : users) {
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }
}

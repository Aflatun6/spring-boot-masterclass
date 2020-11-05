package com.avelibeyli.springbootmasterclass.service;

import com.avelibeyli.springbootmasterclass.dao.UserDao;
import com.avelibeyli.springbootmasterclass.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid(userUid);
    }

    public int updateUser(User user) {
        Optional<User> userFound = userDao.selectUserByUserUid(user.getUserUid());
        if (userFound.isPresent()) {
            userDao.updateUser(user);
            return 1;
        }
        return -1;
    }

    public int removeUser(UUID userUid) {
        Optional<User> userFound = userDao.selectUserByUserUid(userUid);
        if (userFound.isPresent()) {
            userDao.deleteUserByUserUid(userUid);
            return 1;
        }
        return -1;
    }

    public int insertUser(User user) {
        return userDao.insertUser(UUID.randomUUID(), user);
    }
}

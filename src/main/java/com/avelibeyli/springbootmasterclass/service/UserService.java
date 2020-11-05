package com.avelibeyli.springbootmasterclass.service;

import com.avelibeyli.springbootmasterclass.dao.UserDao;
import com.avelibeyli.springbootmasterclass.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUsers();
        if (gender.isEmpty()) {
            return users;
        }
        try {
            User.Gender theGender = User.Gender.valueOf(gender.get().toUpperCase());
            return users.stream().filter(u -> u.getGender().equals(theGender)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid gender ", e);
        }
    }

    public Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid(userUid);
    }

    public int updateUser(User user) {
        Optional<User> userFound = userDao.selectUserByUserUid(user.getUserUid());
        if (userFound.isPresent()) {
            return userDao.updateUser(user);
        }
        return -1;
    }

    public int removeUser(UUID userUid) {
        Optional<User> userFound = userDao.selectUserByUserUid(userUid);
        if (userFound.isPresent()) {
            return userDao.deleteUserByUserUid(userUid);
        }
        return -1;
    }

    public int insertUser(User user) {
        UUID uuid = UUID.randomUUID();
        user.setUserUid(uuid);

        return userDao.insertUser(uuid, user);
    }
}

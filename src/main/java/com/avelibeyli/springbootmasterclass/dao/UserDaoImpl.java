package com.avelibeyli.springbootmasterclass.dao;

import com.avelibeyli.springbootmasterclass.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private Map<UUID, User> database;

    public UserDaoImpl() {
        database = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        database.put(uuid, new User(uuid, "Joe", "Jones", User.Gender.MALE, 22, "joe.jones@email.com"));
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUserUid(UUID userUid) {
        return Optional.ofNullable(database.get(userUid));
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserUid(), user);
        return 1;
    }

    @Override
    public int deleteUserByUserUid(UUID userUid) {
        database.remove(userUid);
        return 1;
    }

    @Override
    public int insertUser(UUID userUid, User user) {
        database.put(userUid, user);
        return 1;
    }
}

package com.avelibeyli.springbootmasterclass.dao;

import com.avelibeyli.springbootmasterclass.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class UserDaoImplTest {

    private UserDaoImpl userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
    }

    @Test
    void shouldSelectAllUsers() {
        List<User> users = userDao.selectAllUsers();
        assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getEmail()).isEqualTo("joe.jones@email.com");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    void shoulSelectUserByUserUid() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");
        userDao.insertUser(annaUid, anna);
        assertThat(userDao.selectAllUsers()).hasSize(2);

        Optional<User> annaOptional = userDao.selectUserByUserUid(annaUid);
        assertThat(annaOptional.isPresent()).isTrue();
        assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);
    }

    @Test
    void shoulNotSelectUserByRandomUid() {
        Optional<User> userByRandomUid = userDao.selectUserByUserUid(UUID.randomUUID());
        assertThat(userByRandomUid.isPresent()).isFalse();
    }

    @Test
    void updateUser() {
        UUID joeUid = userDao.selectAllUsers().get(0).getUserUid();

        User newJoe = new User(joeUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");
        userDao.updateUser(newJoe);

        Optional<User> user = userDao.selectUserByUserUid(joeUid);
        assertThat(user.isPresent()).isTrue();

        assertThat(userDao.selectAllUsers()).hasSize(1);
        assertThat(user.get()).isEqualToComparingFieldByField(newJoe);


    }

    @Test
    void deleteUserByUserUid() {
        UUID joeUid = userDao.selectAllUsers().get(0).getUserUid();
        userDao.deleteUserByUserUid(joeUid);

        assertThat(userDao.selectUserByUserUid(joeUid).isPresent()).isFalse();
        assertThat(userDao.selectAllUsers()).isEmpty();
    }

    @Test
    void insertUser() {
        UUID uuid = UUID.randomUUID();
        User newUser = new User(uuid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");

        userDao.insertUser(uuid, newUser);

        assertThat(userDao.selectAllUsers()).hasSize(2);
        assertThat(userDao.selectUserByUserUid(uuid).get()).isEqualToComparingFieldByField(newUser);


    }
}
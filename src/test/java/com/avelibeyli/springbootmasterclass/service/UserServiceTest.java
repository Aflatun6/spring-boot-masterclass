package com.avelibeyli.springbootmasterclass.service;

import com.avelibeyli.springbootmasterclass.dao.UserDaoImpl;
import com.avelibeyli.springbootmasterclass.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


class UserServiceTest {

    @Mock
    private UserDaoImpl userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userDao);
    }

    @Test
    void shouldGetAllUsers() {

        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");
        List<User> users = new ArrayList<>() {{
            add(anna);
        }};

        // Given means, that we expect from dao  our above created users. so  we check only service's functionality
        given(userDao.selectAllUsers()).willReturn(users);

        List<User> allUsers = userService.getAllUsers(Optional.empty());

        assertThat(allUsers).hasSize(1);

        User user = allUsers.get(0);
        assertUserFields(user);

    }

    @Test
    void shouldGetFilteredUsers() {

        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");

        UUID joeUid = UUID.randomUUID();
        User joe = new User(joeUid, "Joe", "Jones", User.Gender.MALE, 30, "joeisamane@gmail.com");
        List<User> users = new ArrayList<>() {{
            add(anna);
            add(joe);
        }};


        given(userDao.selectAllUsers()).willReturn(users);
        assertThat(users).hasSize(2);

        List<User> filteredUsers = userService.getAllUsers(Optional.of("female"));
        assertThat(filteredUsers).hasSize(1);

        User user = filteredUsers.get(0);
        assertUserFields(user);

    }

    @Test
    void shouldThrowExceptionWhenGenderIsInvalid() {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("fjklafkl;a")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid gender");
    }

    @Test
    void shouldGetUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");

        given(userDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));// It also means that DAO will return us that Anna . SO WE  BELIEVE THE DAO AND WRITE OURSELVES WHAT IT WOULD RETURN.

        Optional<User> OptionalUser = userService.getUser(annaUid);

        assertThat(OptionalUser.isPresent()).isTrue();
        User user = OptionalUser.get();

        assertUserFields(user);


    }

    @Test
    void shouldUpdateUser() {

        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");

        given(userDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(userDao.updateUser(anna)).willReturn(1);

        // THIS CAPTURES THE VALUE THAT WAS GIVEN AS AN ARGUMENT
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(anna);

        verify(userDao).selectUserByUserUid(annaUid);
        verify(userDao).updateUser(captor.capture());

        User user = captor.getValue();
        assertUserFields(user);

        assertThat(updateResult).isEqualTo(1);

    }

    @Test
    void shouldRemoveUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");

        given(userDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(userDao.deleteUserByUserUid(annaUid)).willReturn(1);

        // THIS CAPTURES THE VALUE THAT WAS GIVEN AS AN ARGUMENT
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        int deleteResult = userService.removeUser(annaUid);

        verify(userDao).selectUserByUserUid(annaUid);  // Verify that this function was invoked by this given argument
        verify(userDao).deleteUserByUserUid(captor.capture());

        UUID capturedAnnaUid = captor.getValue();

        assertThat(capturedAnnaUid).isEqualTo(annaUid);
        assertThat(deleteResult).isEqualTo(1);

    }

    @Test
    void shouldInsertUser() {

        User anna = new User(null, "Anna", "Smith", User.Gender.FEMALE, 25, "annasmith@gmail.com");
        given(userDao.insertUser(any(UUID.class), eq(anna))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int insertResult = userService.insertUser(anna);

        verify(userDao).insertUser(any(UUID.class), captor.capture());

        User user = captor.getValue();
        assertUserFields(user);
        assertThat(insertResult).isEqualTo(1);


    }

    private void assertUserFields(User user) {
        assertThat(user.getAge()).isEqualTo(25);
        assertThat(user.getFirstName()).isEqualTo("Anna");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getEmail()).isEqualTo("annasmith@gmail.com");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getUserUid()).isNotNull();
    }
}
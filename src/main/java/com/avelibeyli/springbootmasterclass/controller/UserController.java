package com.avelibeyli.springbootmasterclass.controller;

import com.avelibeyli.springbootmasterclass.entity.ErrorMessage;
import com.avelibeyli.springbootmasterclass.entity.User;
import com.avelibeyli.springbootmasterclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") UUID userId) {
        return userService.getUser(userId).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessage("user with id: " + userId + " was not found")));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateUser(@RequestBody User user) {
        return getIntegerResponseEntity(userService.updateUser(user));
    }

    @DeleteMapping("/{userUid}")
    public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid) {
        return getIntegerResponseEntity(userService.removeUser(userUid));
    }


    private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if (result == 1) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

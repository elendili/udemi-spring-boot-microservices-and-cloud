package com.example.demo.ui.controller;

import com.example.demo.ui.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("users")// :8080/users/
public class UserController {

    @GetMapping(value = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> get(@PathVariable String userId) {
        if (users.containsKey(userId)) {
            return ResponseEntity.ok(users.get(userId));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping
    public String getUsers() {
        return MessageFormat.format("{0} for user {1} was called",
                getCallerMethodName());
    }

    final Map<String, User> users = new ConcurrentHashMap<>();

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        String uid = UUID.randomUUID().toString();
        user.setUserID(uid);
        users.put(uid, user);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody User user) {
        if (users.containsKey(userId)) {
            User storedUser = users.get(userId);
            storedUser.setFirstName(user.getFirstName());
            storedUser.setLastName(user.getLastName());
            return ResponseEntity.ok(storedUser);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable String userId) {
        if (users.remove(userId) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    static String getCallerMethodName() {
        Optional<String> methodName = StackWalker.getInstance()
                .walk(frames ->
                        frames.skip(1).findFirst().map(StackWalker.StackFrame::getMethodName)
                );
        return methodName.orElse("<unknown method>");
    }
}

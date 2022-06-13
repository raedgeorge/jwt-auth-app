package com.atech.bootstrap;

import com.atech.domain.Role;
import com.atech.domain.User;
import com.atech.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        userService.saveRole(new Role(null, "ROLE_USER"));
        userService.saveRole(new Role(null, "ROLE_MANAGER"));
        userService.saveRole(new Role(null, "ROLE_ADMIN"));
        userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

        userService.saveUser(new User(null, "john doe", "john", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "chad darby", "chad", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "mosh hamedani", "mosh", "1234", new ArrayList<>()));
        userService.saveUser(new User(null, "jonas schmedtmann", "jonas", "1234", new ArrayList<>()));

        userService.addRoleToUser("john", "ROLE_USER");
        userService.addRoleToUser("john", "ROLE_MANAGER");

        userService.addRoleToUser("chad", "ROLE_MANAGER");

        userService.addRoleToUser("mosh", "ROLE_ADMIN");

        userService.addRoleToUser("jonas", "ROLE_USER");
        userService.addRoleToUser("jonas", "ROLE_ADMIN");
        userService.addRoleToUser("jonas", "ROLE_SUPER_ADMIN");

    }
}

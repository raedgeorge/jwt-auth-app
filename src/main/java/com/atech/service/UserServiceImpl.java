package com.atech.service;

import com.atech.domain.Role;
import com.atech.domain.User;
import com.atech.repository.RoleRepository;
import com.atech.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null){
            log.error("User [{}] not found in the database", username);
            throw new UsernameNotFoundException("User not found in the database");
        }
        else {
            log.info("User [{}] found in the database", username);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });

            return org.springframework.security.core.userdetails.User.builder().username(
                     user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        }
    }

    @Override
    public User saveUser(User user) {

        log.info("Saving new user [{}] to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role [{}] to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        log.info("Adding Role [{}] to User [{}]", roleName, username);
        User foundUser = userRepository.findByUsername(username);
        Role foundRole = roleRepository.findByName(roleName);

        foundUser.getRoles().add(foundRole);
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("searching for user [{}] in the database", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Finding all users in the system");
        return userRepository.findAll();
    }

}

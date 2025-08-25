package com.credit.module;

import com.credit.module.dao.UserRepository;
import com.credit.module.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class ServiceStarter {
    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(ServiceStarter.class, args);
    }

    /*
     * Initializes admin user via @PostConstruct. and plain text password.
     * This is not a good practice,
     * it is just for assessment and testing purposes.*/
    @PostConstruct
    public void init() {
        User user = new User();
        user.setId(1L);
        user.setName("admin");
        user.setSurname("admin");
        user.setUserId("admin");
        user.setPassword(bCryptPasswordEncoder.encode("admin1"));
        user.setRoles(Set.of("ROLE_ADMIN"));
        userRepository.save(user);
    }
}
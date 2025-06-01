package com.myproject.simpleonlineshop.data;


import com.myproject.simpleonlineshop.model.Role;
import com.myproject.simpleonlineshop.model.User;
import com.myproject.simpleonlineshop.repository.RoleRepository;
import com.myproject.simpleonlineshop.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    // since supportsAsyncExecution has default implementation in interface, it's not mandatory to implement it
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExists();
        createDefaultRolesIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i = 0; i < 7; i++) {
            String defaultEmail = "user"+i+"@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName(i+"th FirstName");
            user.setLastName(i+"th LastName");
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("default User " +i+" Created.");

        }
    }
    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 0; i < 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName(i + "th Admin");
            user.setLastName(i + "th Adminico");
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("default Admin " + i + " Created.");

        }
    }
    private void createDefaultRolesIfNotExists(Set<String> roles){
        roles.stream().filter(role -> roleRepository.findByName(role).isEmpty()).map(Role::new).forEach(roleRepository::save);
    }

}

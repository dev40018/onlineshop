package com.myproject.simpleonlineshop.data;


import com.myproject.simpleonlineshop.model.User;
import com.myproject.simpleonlineshop.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    // since supportsAsyncExecution has default implementation in interface, it's not mandatory to implement it
    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists(){
        for (int i = 0; i < 7; i++) {
            String defaultEmail = "user"+i+"@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName(i+"th FirstName");
            user.setLastName(i+"th LastName");
            user.setEmail(defaultEmail);
            user.setPassword(i+i+1+"pass");
            userRepository.save(user);
            System.out.println("default User " +i+" Created.");

        }
    }

}

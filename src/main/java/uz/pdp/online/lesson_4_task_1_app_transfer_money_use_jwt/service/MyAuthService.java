package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyAuthService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;



    List<Card> cardsList = new ArrayList<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    List<User> usersList = new ArrayList<>(Arrays.asList(
            new User("bernard", passwordEncoder.encode("bernard"), new ArrayList<>()),
            new User("gabriel", passwordEncoder.encode("gabriel"), new ArrayList<>()),
            new User("yusha", passwordEncoder.encode("yusha"), new ArrayList<>()),
            new User("mark", passwordEncoder.encode("mark"), new ArrayList<>()),
            new User("nelson", passwordEncoder.encode("nelson"), new ArrayList<>())));

        for (User user : usersList) {
            if (user.getUsername().equals(username))
                return user;
        }
        throw new UsernameNotFoundException("Username topilmadi");
    }

}

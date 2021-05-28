package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.User;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.security.JwtProvider;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.service.MyAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Autowired
    MyAuthService myAuthService;
@Autowired
    JwtProvider jwtProvider;
//@Autowired
//    PasswordEncoder passwordEncoder;
@Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?>loginToSystem(@RequestBody User user) {
    try {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            user.getPassword()));

        String token = jwtProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("Login yoki parol xato");
        }
    }
}

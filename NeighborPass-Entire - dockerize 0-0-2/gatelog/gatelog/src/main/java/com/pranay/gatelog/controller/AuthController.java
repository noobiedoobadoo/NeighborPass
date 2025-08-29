package com.pranay.gatelog.controller;


import com.pranay.gatelog.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    //private final AuthenticationManager authenticationManager;

    //private final JwtService jwtService;

    private record RegisterRequest(String username, String password, String houseNumber, String role){}

    private final UserInfoService userInfoService;



    /*
    @PostMapping("/register")//ONLY FOR TESTING
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            UserInfo userInfo = UserInfo.builder()
                    .username(registerRequest.username())
                    .password(registerRequest.password)
                    .houseNumber(registerRequest.houseNumber)
                    .build();
            userInfoService.createUserInfo(userInfo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in UserInfoService", HttpStatus.BAD_REQUEST);
        }
    }

     */

    private record LoginRequest(String username, String password){}


    /*
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        //User userFromDb = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(loginRequest.username());
            return new ResponseEntity<>(accessToken, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

     */
}

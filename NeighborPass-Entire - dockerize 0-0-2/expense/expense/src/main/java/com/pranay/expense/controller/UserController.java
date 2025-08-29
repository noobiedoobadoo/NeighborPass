package com.pranay.expense.controller;


import com.pranay.expense.Producer.UserAuthProducer;
import com.pranay.expense.entity.User;
import com.pranay.expense.repository.UserRepository;
import com.pranay.expense.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {


    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final ValidationServiceClient validationServiceClient;

    private final UserAuthProducer userAuthProducer;

    private final OtpService otpService;

    @Value("${app.admin.secret-code}")
    private final String ADMIN_SECRET = null;

    public record UserDto(
            @NotBlank(message = "Username is required")
            String username,

            @NotBlank(message = "Password is required")
            String password,

            @NotBlank(message = "House number is required")
            @Pattern(regexp = "^\\d{3}$", message = "House number must be exactly 3 digits")
            String houseNumber,

            @NotBlank(message = "User contact is required")
            @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "User contact must be numeric digits with optional country code (e.g., +1234567890)")
            String userContact,

            String otp  // Optional, no validation
    ) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        //return userRepository.save(user);

        String otp = userDto.otp();

        if (Objects.nonNull(userService.findByUsername(userDto.username()))) {
            return new ResponseEntity<>("username : "+userDto.username()+" already exists",HttpStatus.BAD_REQUEST);
        }
        if (Objects.nonNull(userService.findByContact(userDto.userContact()))) {
            return new ResponseEntity<>("contact no. : "+userDto.userContact()+" already exists",HttpStatus.BAD_REQUEST);
        }
        if (Objects.nonNull(userService.findByHouseNumber(userDto.houseNumber()))) {
            return new ResponseEntity<>("house no. : "+userDto.houseNumber()+" already exists",HttpStatus.BAD_REQUEST);
        }


        if (Objects.nonNull(otp) && (otp.equalsIgnoreCase("NA") || otpService.validate(userDto.userContact(), otp))) {
            User registered = userService.register(
                    User.builder()
                            .username(userDto.username())
                            .password(userDto.password())
                            .houseNumber(userDto.houseNumber())
                            .userContact(userDto.userContact())
                            .build()
            );

            userAuthProducer.sendUserAuth(new RegisterDTO(registered.getUsername(), registered.getHouseNumber()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {//otp is null or invalid
            if (Objects.isNull(otp)) {
                try{
                    otpService.sendMessage(userDto.userContact());
                } catch (Exception e) {
                    System.out.println("OTP SERVICE EXCEPTION");
                    return new ResponseEntity<>("OTP SERVICE EXCEPTION" , HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("OTP SENT" , HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("INVALID OTP", HttpStatus.NOT_FOUND);
            }
        }

//        return validationServiceClient.registerUser(
//                new RegisterDTO(user.getUsername(), user.getHouseNumber()
//                )
//        );
    }

    private record AdminRegisterDto(
            @NotBlank(message = "Username is required")
            String username,
            @NotBlank(message = "Password is required")
            String password,
            @NotBlank(message = "AdminSecret is required")
            String adminSecret){}

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterDto adminRegisterDto) {
        //return userRepository.save(user);
        String username = adminRegisterDto.username();
        String password = adminRegisterDto.password();
        String adminSecret = adminRegisterDto.adminSecret();

        if (Objects.nonNull(userService.findByUsername(username))) {
            return new ResponseEntity<>("username : "+username+" already exists",HttpStatus.BAD_REQUEST);
        }
        if (!adminSecret.equals(ADMIN_SECRET)) {
            return new ResponseEntity<>("invalid admin secret key",HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .role("ROLE_ADMIN")
                .build();
        User registered = userService.register(user);

        userAuthProducer.sendUserAuth(new RegisterDTO(user.getUsername(), null));


        return new ResponseEntity<>(HttpStatus.CREATED);

        //return validationServiceClient.registerAdmin(new AdminRegisterValidationDto(username));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //User userFromDb = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(userDetails);
            return new ResponseEntity<>(accessToken, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}

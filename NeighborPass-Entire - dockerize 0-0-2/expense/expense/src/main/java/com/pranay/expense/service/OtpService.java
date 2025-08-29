package com.pranay.expense.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Value("${twilio.phone-number}")
    private final String TWILIO_PHONENUMBER = null;

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID ;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN ;



    private Map<String ,String> otpMap = new HashMap<>();

    public OtpService() {
    }
    
    @PostConstruct
    public void initTwilio() {
        // This runs AFTER Spring injects the @Value properties
        Twilio.init(accountSid, authToken);
    }

    public Message sendMessage(String contact) {

        String otp = generateOTP();
        otpMap.put(contact, otp);


        Message message = Message
                .creator(new com.twilio.type.PhoneNumber(contact),
                        new com.twilio.type.PhoneNumber(TWILIO_PHONENUMBER),
                        "Your Verification code is: "+otp)
                .create();

        return message;
    }

    public Boolean validate(String contact, String otp) {
        return otpMap.getOrDefault(contact,"").equals(otp);
    }

    private String generateOTP() {
        return new DecimalFormat("0000")
                .format(new Random().nextInt(9999));
    }
}

package com.example.demo.OTP;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OTPService {

    private final OTPRepository otpRepository;
    private short OTP_LENGTH = 5;               //random otp is hardcoded to be of size 5 and numeric only for simplicity
    private short VALID_DURATION = 1;          //hardcoded 10 mins after otp creation for as valid duration

    OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    private static String generateRandomNumericOTP(int length) {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Generates a digit between 0 and 9
        }

        return otp.toString();
    }

    OTP generateOTP(String courseID, String lessonID) {
        OTP candidate = otpRepository.retrieve(courseID, lessonID);
        if (candidate != null)
        {
            return candidate;
        }
        LocalDateTime now = LocalDateTime.now();
        String format = generateRandomNumericOTP(OTP_LENGTH);
        OTP generated = new OTP(courseID, lessonID, format, now.plusMinutes(VALID_DURATION));
        otpRepository.create(generated);
        return generated;
    }

    OTP getOTP(String courseID, String lessonID) {
        return otpRepository.retrieve(courseID, lessonID);
    }

    boolean verifyOTP(String courseID, String lessonID, String otpFormat) {
        OTP validOTP = otpRepository.retrieve(courseID, lessonID);
        LocalDateTime now = LocalDateTime.now();
        if (validOTP == null || !otpFormat.equals(validOTP.format()) || now.isAfter(validOTP.expirationTime()))
        {
            //if the expired time on the passed otp has come, then delete it from the repository to reduce wasted speace
            if (validOTP != null && now.isAfter(validOTP.expirationTime()))
            {
                otpRepository.delete(validOTP);
            }
            return false;
        }
        return true;
    }

}

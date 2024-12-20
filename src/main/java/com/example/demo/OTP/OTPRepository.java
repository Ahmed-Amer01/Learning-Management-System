package com.example.demo.OTP;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OTPRepository {

    private List<OTP> otps = new ArrayList<OTP>();

    void create(OTP otp) {
        otps.add(otp);
    }

    OTP retrieve(String courseID, String lessonID) {
        for (OTP otp : otps) {
            if (otp.courseID().equals(courseID) && otp.lessonID().equals(lessonID)) {
                return otp;
            }
        }
        return null;
    }

    void delete(OTP otp) {
        otps.remove(otp);
    }

}

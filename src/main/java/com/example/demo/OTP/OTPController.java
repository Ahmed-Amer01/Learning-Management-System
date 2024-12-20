package com.example.demo.OTP;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{courseID}/{lessonID}")
public class OTPController {

    private final OTPService otpService;

    OTPController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/attendance/otp")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    //returns the generated otp in the response body
    public OTP postOTP(@PathVariable String courseID, @PathVariable String lessonID) {
        return otpService.generateOTP(courseID, lessonID);
    }

    @GetMapping("/otp")
    @ResponseBody
    public OTP getOTP(@PathVariable String courseID, @PathVariable String lessonID) {
        return otpService.getOTP(courseID, lessonID);
    }

    @PostMapping("/otp/verify")
    @ResponseBody
    public boolean verifyOTP(@RequestBody String otpFormat, @PathVariable String courseID, @PathVariable String lessonID) {
        return otpService.verifyOTP(courseID, lessonID, otpFormat);
    }

}

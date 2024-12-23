package com.example.demo.OTP;

import com.example.demo.Notifications.NotificationsManager.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OTPRepository extends JpaRepository<OTP, String> {
    Optional<OTP> findByCourseIDAndLessonID(String courseID, String lessonID);
}

/*@Repository
public class OTPRepository {

    private List<OTP> otps = new ArrayList<OTP>();

    void create(OTP otp) {
        otps.add(otp);
    }

    OTP retrieve(String courseID, String lessonID) {
        for (OTP otp : otps) {
            if (otp.getCourseID().equals(courseID) && otp.getLessonID().equals(lessonID)) {
                return otp;
            }
        }
        return null;
    }

    void delete(OTP otp) {
        otps.remove(otp);
    }

}*/

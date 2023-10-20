package frgp.utn.edu.ar.controllers.utils;

import frgp.utn.edu.ar.controllers.data.remote.mail.MailConnection;

public class MailService {
    public void sendMail(String email, String subject, String message) {
        try {
            MailConnection mailConnection = new MailConnection(email, subject, message);
            mailConnection.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

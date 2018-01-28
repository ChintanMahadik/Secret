package com.crypt.asus.secret;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public static int sendEmail(Session session, String toEmail, String subject, String body){
        try {
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("chintumahadik@gmail.com"));

            msg.setReplyTo(InternetAddress.parse("chintumahadik@gmail.com", false));

            msg.setSubject(subject, "UTF-8");

            //msg.setText(body, "UTF-8");
            msg.setContent(body,"text/html");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");

            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
            MyActivity.value=1;

        }
        catch (Exception e) {
            e.printStackTrace();
            MyActivity.value=0;
            return 0;
           }

        return 1;
    }
}
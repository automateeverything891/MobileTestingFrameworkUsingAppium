package com.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.config.VariableConstants;

public class SendMail {

	public static void custom_Mail() {

        String to = VariableConstants.to_mail;

        String from = VariableConstants.from_mail;

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
       
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(VariableConstants.from_mail, VariableConstants.password);

            }

        });

      
        session.setDebug(true);

        try {
           
            MimeMessage message = new MimeMessage(session);

            
            message.setFrom(new InternetAddress(from));

           
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            message.addRecipients(Message.RecipientType.CC, VariableConstants.cc_mails);
           
            message.setSubject("TESTING REPORT!!!");
            
            MimeBodyPart mimebody1 = new MimeBodyPart();
            mimebody1.setText("Hi Dev Team,\n"
            		+ "Automation test script has been executed successfully!. Please find the attachment. \n \n \n \n "
            		+ "Thanks & Regrads \n"
            		+ "Testting Team");
            MimeBodyPart mimebody = new MimeBodyPart();
        
            try {
				mimebody.attachFile(System.getProperty("user.dir")+"\\src\\main\\java\\com\\report\\extentreport.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Multipart multipart = new MimeMultipart();
            
            multipart.addBodyPart(mimebody);
            multipart.addBodyPart(mimebody1);
            message.setContent(multipart);            
            
 
            Transport.send(message);
            System.out.println("Sent message successfully!!!!!!!!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    } 
	
}

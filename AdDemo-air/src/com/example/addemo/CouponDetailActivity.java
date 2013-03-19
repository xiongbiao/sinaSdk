package com.example.addemo;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CouponDetailActivity extends Activity {

    private String mMailTo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        
        init();
    }

    private void init() {
        TextView title = (TextView) findViewById(R.id.coupon_title);
        title.setText(getIntent().getStringExtra("coupon_title"));
        
        Button sendSmsButton = (Button) findViewById(R.id.button_send_sms);
        sendSmsButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(CouponDetailActivity.this, 
                        "优惠券已通过短信发送至您的手机", Toast.LENGTH_SHORT).show();
            }
            
        });
        
        Button sendEmailButton = (Button) findViewById(R.id.button_send_email);
        sendEmailButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText contactInfo = (EditText) findViewById(R.id.contact_info);
                mMailTo = contactInfo.getText().toString();
                
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        sendMail(mMailTo);
                    }
                    
                });
                thread.start();
                Toast.makeText(CouponDetailActivity.this, 
                        "优惠券已发至您的邮箱", Toast.LENGTH_SHORT).show();
            }
            
        });
        
        Button backButton = (Button) findViewById(R.id.button_coupon_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
            
        });
    }
    
    private void sendMail(String mail) {
        final String username = "www.yingzi.tv@gmail.com";
        final String password = "linshimima";
 
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
 
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });
 
        try {
 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("www.yingzi.tv@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(mail));
            message.setSubject("优惠券");
            message.setText("eat me, drink me");
 
            Transport.send(message);
 
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

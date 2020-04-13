package com.ecommerce.service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import com.ecommerce.bean.Token;
@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private MailContentBuilder mailContentBuilder;
	
	public void emailSend(Token token) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(token.getUser().getEmailAdress());
		helper.setSubject("Link for resetting password"); 
		String content = mailContentBuilder.buildMesage(token);
		helper.setText(content , true);
			javaMailSender.send(message);
	}

}

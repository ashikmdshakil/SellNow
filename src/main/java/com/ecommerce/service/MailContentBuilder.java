package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ecommerce.bean.Token;
@Service
public class MailContentBuilder {
	@Autowired
	private TemplateEngine templateEngine;
	
	public String buildMesage(Token token) {
		Context context1 = new Context();
		context1.setVariable("message", token);
		return templateEngine.process("mailBody", context1);
	}
}

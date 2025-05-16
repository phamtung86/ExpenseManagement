package com.vti.mail;

import com.vti.entity.PasswordResetToken;
import com.vti.entity.User;
import org.springframework.mail.SimpleMailMessage;

import java.util.Locale;

public interface ISendMailService {
	void sendPasswordResetEmail(String contextPath, Locale locale, String token, User user, String endpoint, String content, String title);

	String validatePasswordResetToken(String token);

	boolean isTokenFound(PasswordResetToken passToken);

	boolean isTokenExpired(PasswordResetToken passToken);

	SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String endpoint, String content, String title);

	SimpleMailMessage constructEmail(String subject, String body, User user);
}

package com.vti.mail;

import com.vti.entity.PasswordResetToken;
import com.vti.entity.User;
import com.vti.repository.IPasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SendMailService implements ISendMailService {
	@Autowired
	private IPasswordResetTokenRepository passwordTokenRepository;

	@Autowired
	private JavaMailSender mailSender;

    @Autowired
	private MessageSource messages;

	@Value("${support.email}")
	private String supportEmail;



	@Override
	public void sendPasswordResetEmail(String contextPath, Locale locale, String token, User user, String endpoint, String content, String title) {
		SimpleMailMessage email = constructResetTokenEmail(contextPath, locale, token, user, endpoint, content, title);
		mailSender.send(email); // Gửi email qua JavaMailSender
	}

	@Override
	public String validatePasswordResetToken(String token) {
		PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

		return !isTokenFound(passToken) ? "invalidToken" : isTokenExpired(passToken) ? "expired" : null;
	}

	@Override
	public boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}

	@Override
	public boolean isTokenExpired(PasswordResetToken passToken) {
		long currentTimeMillis = System.currentTimeMillis();
		long expiryTimeMillis = passToken.getExpDate().getTime();
		return expiryTimeMillis < currentTimeMillis;
	}

	@Override
	public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String endpoint, String content, String title) {
		if (locale == null) {
			locale = Locale.US;
		}
		String url = contextPath + endpoint + token;
		String message = messages.getMessage("message.resetPassword", null,
				content, locale);
		return constructEmail(title, message + " \r\n" + url, user);
	}

	@Override
	public SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(supportEmail);
		return email;
	}
}

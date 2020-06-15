package com.allscontracting.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class Mail {

	private static final String GMAIL_PASSWORD = "Getalife1969";
	private static final String GMAIL_USER = "allscontractingdc@gmail.com";
	private static final int PORT = 465;
	private static final String HOST = "smtp.gmail.com";
	private Consumer<String> runnableOnError = (s) -> {};
	private Runnable runnableOnSuccess = () -> {};

	private String subject;
	private String emailTo;
	private String text;
	private FileSystemResource[] attachmentFiles = {} ;

	public Mail(String emailTo, String subject, String text, FileSystemResource... attachmentFiles) {
		this.emailTo = emailTo;
		this.subject = subject;
		this.text = text;
		if (attachmentFiles != null)
			this.attachmentFiles = attachmentFiles;
		else
			this.attachmentFiles = new FileSystemResource[]{};
	}

	public void send() {
		ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		emailExecutor.execute(() -> {
			try {
				MimeMessage mimeMessage = emailSender().createMimeMessage();
				MimeMessageHelper helper = getMiniMessageHelper(mimeMessage, subject, emailTo);
				helper.setText(text, true);
				for (FileSystemResource af : attachmentFiles) {
					helper.addAttachment(af.getFilename(), af);
				}
				emailSender().send(mimeMessage);
				this.runnableOnSuccess.run();
			} catch (MailException | MessagingException | IOException e) {
				this.runnableOnError.accept(e.getMessage());
			}
		});
		emailExecutor.shutdown();
	}

	public Mail onSuccess(Runnable runnable) {
		this.runnableOnSuccess = runnable;
		return this;
	}

	public Mail onError(Consumer<String> consumer) {
		this.runnableOnError = consumer;
		return this;
	}

	private JavaMailSender emailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(HOST);
		mailSender.setPort(PORT);
		mailSender.setUsername(GMAIL_USER);
		mailSender.setPassword(GMAIL_PASSWORD);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");
		return mailSender;
	}

	private MimeMessageHelper getMiniMessageHelper(MimeMessage message, String title, String emailTo)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(emailTo);
		helper.setSubject(title);
		helper.setFrom(GMAIL_USER, "All's Contracting Inc");
		return helper;
	}

}

package com.allscontracting.service.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSender {

	private static final String MAIL_TRANSPORT_PROTOCOL = "smtp";
	private static final String GMAIL_PASSWORD = "Getalife2009!";
	private static final String GMAIL_USER = "allscontractingdc@gmail.com";
	private static final int PORT = 465;
	private static final String HOST = "smtp.gmail.com";
	
	private final String subject;
	private final String text;
	private final List<String> emailTo;
	private final List<String> bcc;
	private final List<File> attachmentFiles;	
	private final JavaMailSender mailSender;
	
	private Consumer<String> runnableOnError = (s) -> {};
	private Runnable runnableOnSuccess = () -> {};

	public MailSender(List<String> emailTo, List<String> bcc, String subject, String text, List<File> attachments) {
		this.emailTo = Collections.unmodifiableList(emailTo); 
		this.subject = subject;
		this.text = text;
		this.bcc = Collections.unmodifiableList(bcc);
		this.attachmentFiles = Collections.unmodifiableList(attachments);
		this.mailSender = createEmailSender();
	}

	private JavaMailSender createEmailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(HOST);
		mailSender.setPort(PORT);
		mailSender.setUsername(GMAIL_USER);
		mailSender.setPassword(GMAIL_PASSWORD);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", MAIL_TRANSPORT_PROTOCOL);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "true");
		return mailSender;
	}

	public MailSender onSuccess(Runnable runnable) {
		this.runnableOnSuccess = runnable;
		return this;
	}

	public MailSender onError(Consumer<String> consumer) {
		this.runnableOnError = consumer;
		return this;
	}

	public void send() {
		ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		emailExecutor.execute(() -> {
			try {	
				
				MimeMessage mimeMessage = createMimeMessage();
				this.mailSender.send(mimeMessage);
				
				//on success
				this.runnableOnSuccess.run();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				//on error
				this.runnableOnError.accept(e.getMessage());
				
			}
		});
		emailExecutor.shutdown();
	}

	private MimeMessage createMimeMessage() throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = getMimeMessageHelper(mimeMessage, subject, emailTo, bcc);
		helper.setText(text, true);
		addAttachmentsTo(helper);
		return mimeMessage;
	}

	private void addAttachmentsTo(MimeMessageHelper helper) throws MessagingException {
		for (File af : attachmentFiles) {
			FileSystemResource file = new FileSystemResource(af);
			helper.addAttachment(file.getFilename(), file);
		}
	}

	private MimeMessageHelper getMimeMessageHelper(MimeMessage message, String title, List<String> emailTo, List<String> bcc)
	    throws MessagingException, UnsupportedEncodingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(emailTo.toArray(new String[0]));
		if (bcc != null)
			helper.setBcc(bcc.toArray(new String[0]));
		helper.setSubject(title);
		helper.setFrom(GMAIL_USER, "All's Contracting Inc");
		return helper;
	}

}

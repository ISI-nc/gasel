package nc.ccas.gasel.reports.exceptions;

import static nc.ccas.gasel.AppContext.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailEnv {

	public static void main(String[] args) throws IOException {
		sendMail("mcluseau@isi.nc", "mcluseau@isi.nc", "Test", "Test");
	}

	public static void sendMail(String from, String to, String subject,
			String messageBody) throws IOException {
		// Build addresses
		InternetAddress fromAddr;
		InternetAddress toAddr;
		try {
			fromAddr = new InternetAddress(from);
			toAddr = new InternetAddress(to);
		} catch (AddressException e) {
			throw new RuntimeException(e);
		}

		// Build session
		Properties props = new Properties();
		props.put("mail.smtp.host", mail("smtp-server"));
		Session session = Session.getInstance(props);

		// Build message
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(fromAddr);
			message.setRecipient(Message.RecipientType.TO, toAddr);
			message.setContent(messageBody,
					"text/plain; charset=\"iso-8859-1\"");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		// And send it
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}

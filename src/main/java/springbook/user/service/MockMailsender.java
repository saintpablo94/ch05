package springbook.user.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailsender implements MailSender {
	private List<String> requests = new ArrayList<String>();

	public List<String> getRequests() {
		return requests;
	}

	public void send(SimpleMailMessage simpleMessage) throws MailException {
		requests.add(simpleMessage.getTo()[0]);
	}

	public void send(SimpleMailMessage... simpleMessages) throws MailException {

	}

}

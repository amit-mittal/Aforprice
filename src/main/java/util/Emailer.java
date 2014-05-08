package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class Emailer {

	private final static Logger logger = Logger.getLogger(Emailer.class);
	private final static Emailer instance = new Emailer();
	private final Session session;

	private Emailer() {

		// Setup mail server
		final String username = "documentsaac@gmail.com";
		final String password = "darwazaakholo@#$%";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		// Get the default Session object.
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

	}

	public static Emailer getInstance() {
		return instance;
	}

	public boolean sendHTML(String subject, String text){
		return send(Constants.ALERTS.TO, null, null, subject, text, true);
	}

	public boolean sendText(String subject, String text){
		return send(Constants.ALERTS.TO, null, null, subject, text, false);
	}
	
	public boolean send(List<String> to, List<String> cc,
			List<String> bcc, String subject, String text, boolean isHTML) {
		try {
			if(ConfigParms.getInstance().isProduction())
				subject = "[Prod]" + subject;			
			else
				subject = "[QA]" + subject;
			if(ConfigParms.MODE == ConfigParms.RUNTIME_MODE.ONETIME){
				subject = "OneTime-" + subject;
			}
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			for (String s : to) {
				// Set To
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(s));
			}
			if (cc != null) { // Set cc if specified
				for (String s : cc) {
					message.addRecipient(Message.RecipientType.CC,
							new InternetAddress(s));
				}
			}
			if (bcc != null) { // Set bcc if specified
				for (String s : bcc) {
					message.addRecipient(Message.RecipientType.BCC,
							new InternetAddress(s));
				}
			}
			// Set subject
			if (subject != null) {
				message.setSubject(subject);
			}
			StringBuffer msg = new StringBuffer();
			if (text != null) {
				// Now set the actual message
				msg.append(text).append("\n\n");
			}
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();						
			for(int i = 0; i < stack.length; i++){				
				if(i <= 1){
					continue;
				}
				StackTraceElement e = stack[i];
				msg.append(e + "\n");				
			}
			if(isHTML)
				message.setContent(msg.toString(), "text/html");
			else
				message.setText(msg.toString());
			// Send message
			Transport.send(message);
			logger.info("Sent message successfully....");
		} catch (Exception ex) {
			logger.error("Failed to send message", ex);
			return false;
		}
		return true;
	}
	 
	public void sendMigratedDownloads(Map<String, Integer> processed){
		String subject = DateTimeUtils.currentDateYYYYMMDD(new Date()) + "-" + "Products Migration Results";
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");		
		html.append("<table border=\"1\">");		
		html.append("<tr>");
		for(String header: processed.keySet()){			
			html.append("<th>").append(header).append("</th>");			
		}
		html.append("</tr>");
		html.append("<tr>");		
		for(String key: processed.keySet()){
			html.append("<td>").append("<b>").append(processed.get(key)).append("</b>").append("</td>");								
		}				
		html.append("</tr>");		
		html.append("</table>").append("</body>").append("</html>");
		sendHTML(subject, html.toString());
	}

	public static void main(String[] args) {
		Emailer emailer = Emailer.getInstance();
		List<String> to = new ArrayList<String>();
		to.add("alertanurag@gmail.com");
		to.add("shahashish@gmail.com");
		to.add("jayanta.hazra@gmail.com");
		to.add("chirag1992m.cc@gmail.com");
		to.add("heyarpan@gmail.com");

		emailer.send( to, null, null, "Test Email sent by Alerter",
				"Test email. Please ignore", false);
	}
}

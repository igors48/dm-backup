package service;

import service.error.SendingException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

import static util.Assert.guard;
import static util.Parameter.*;

/**
 * Created by igor on 14.11.2016.
 */
public class Sender {

    public void sendContent(final String recipient, final String content) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(isValidString(content));

        sendMail(recipient, "downloaded file", "file", content);
    }

    public void sendException(final String recipient, final ServiceException exception) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(notNull(exception));

        sendMail(recipient, exception.toString(), null, null);
    }

    private static void sendMail(final String recipient, final String body, final String attachmentName, final String attachmentContent) throws ServiceException {

        try {
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(recipient));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            final Multipart multipart = new MimeMultipart();

            final MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(String.format("<p>%s</p>", body), "text/html");
            multipart.addBodyPart(htmlPart);

            if (attachmentContent != null) {
                final MimeBodyPart attachment = new MimeBodyPart();

                attachment.setFileName(attachmentName);
                DataSource dataSource = new ByteArrayDataSource(attachmentContent.getBytes("UTF-8"), "application/octet-stream");
                attachment.setDataHandler(new DataHandler(dataSource));
                multipart.addBodyPart(attachment);
            }

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception exception) {
            throw new SendingException(exception.getMessage());
        }
    }

}



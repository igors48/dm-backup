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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Assert.guard;
import static util.Parameter.*;

/**
 * Created by igor on 14.11.2016.
 */
public class Sender {

    private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());

    private static final String DATE_FORMAT_FOR_NAME = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String CSV = ".csv";

    public void sendContent(final String recipient, final String content) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(isValidString(content));

        final Date now = new Date();
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FOR_NAME);
        final String date = format.format(now);
        final String attachmentName = date + CSV;

        final String body = "File backed up at " + date;

        sendMail(recipient, body, body, attachmentName, content);
    }

    public void sendException(final String recipient, final ServiceException exception) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(notNull(exception));

        sendMail(recipient, "Backup error", exception.toString(), null, null);
    }

    private static void sendMail(final String recipient, final String subject, final String body, final String attachmentName, final String attachmentContent) throws ServiceException {

        LOGGER.log(Level.INFO, String.format("recipient [ %s ] body [ %s ] content [ %s ]", recipient, body, attachmentContent));

        try {
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(recipient));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            message.setSubject(subject);

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



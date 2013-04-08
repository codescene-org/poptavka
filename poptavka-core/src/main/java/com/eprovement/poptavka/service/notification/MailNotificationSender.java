package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.validation.EmailValidator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;

/**
 * Implementation of {@link NotificationSender} which sends notifications via email.
 */
public class MailNotificationSender implements NotificationSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailNotificationSender.class);

    private final PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");
    private final MailService mailService;
    private final String notificationFromAddress;

    /**
     * Creates new notification service uses given {@code mailService} for sending emails
     * and {@code notificationFromAddress} as a From address
     * @param mailService mail service for sending emails.
     * @param notificationFromAddress email address from which all notification emails are sent
     */
    public MailNotificationSender(MailService mailService, String notificationFromAddress) {
        Validate.notNull(mailService, "mailService cannot be null!");
        Validate.isTrue(EmailValidator.getInstance().isValid(notificationFromAddress),
                "Valid 'From' address for notifications must be specified!");

        this.mailService = mailService;
        this.notificationFromAddress = notificationFromAddress;
    }

    /**
     * This implementation sends notification via email in an asynchronous way.
     * User can specify optional message variables which will be used for expanding message template associated
     * with given notification.
     */
    @Override
    public void sendNotification(User user, Notification notification, Map<String, String> messageVariables) {
        Validate.notNull(user, "User for notification cannot be null!");
        Validate.isTrue(EmailValidator.getInstance().isValid(user.getEmail()),
                "User email address must be valid! user=" + user);
        Validate.notNull(notification, "notification cannot be null!");

        LOGGER.info("action=notification_async status=startuser={} notification={}", user, notification);
        final SimpleMailMessage notificationMailMessage = new SimpleMailMessage();
        notificationMailMessage.setFrom(notificationFromAddress);
        notificationMailMessage.setTo(user.getEmail());
        notificationMailMessage.setSubject(notification.getName());
        notificationMailMessage.setText(expandMessageBody(notification, messageVariables));

        mailService.sendAsync(notificationMailMessage);
        LOGGER.info("action=notification_async status=finish user={} notification={}", user, notification);
    }

    private String expandMessageBody(Notification notificationEntity, Map<String, String> messageVariables) {
        if (MapUtils.isEmpty(messageVariables)) {
            return notificationEntity.getMessageTemplate();
        }
        return placeholderHelper.replacePlaceholders(notificationEntity.getMessageTemplate(),
                MapUtils.toProperties(messageVariables));
    }

}
package edu.tglima.jssm.service;

import org.slf4j.Logger;
import java.text.DateFormat;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import org.thymeleaf.TemplateEngine;
import edu.tglima.jssm.model.Message;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import edu.tglima.jssm.helper.Constant;
import edu.tglima.jssm.model.ExceptionLog;
import org.springframework.stereotype.Service;
import edu.tglima.jssm.model.ConfigMessageFromSite;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MailService
{
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private DatabaseService dbService;

    public MailService(JavaMailSender javaMailSender, TemplateEngine templateEngine)
    {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public boolean sendMail(Message message, ConfigMessageFromSite configMessageFromSite)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        boolean hasError = false;

        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(configMessageFromSite.txtFrom);
            helper.setTo(configMessageFromSite.addressEmailShipping);
            helper.setSubject(configMessageFromSite.subjectMessage);
            helper.setText(this.generateMailContent(message), true);
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e)
        {
            hasError = true;
            logger.error(e.getMessage());

            logger.warn("processMessage => configMessageFromSite hasError");

            ExceptionLog exceptionLog = new ExceptionLog(
                    message.projectSource,
                    "sendMail",
                    e.getMessage(),
                    e.getStackTrace().toString());

            dbService.saveExceptionLog(exceptionLog);

        }
        return hasError;
    }

    private String generateMailContent(Message message)
    {
        Context context = new Context();
        DateFormat dateFormat = new SimpleDateFormat(Constant.PATTERN_DATE_TIME_BRAZIL);
        context.setVariable(Constant.MESSAGE, message);
        context.setVariable(Constant.DATE_REGISTER, dateFormat.format(message.dateRegister.toSqlTimestamp()));
        return templateEngine.process(Constant.MAIL_TEMPLATE, context);
    }

}

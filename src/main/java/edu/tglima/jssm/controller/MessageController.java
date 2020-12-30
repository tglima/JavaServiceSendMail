package edu.tglima.jssm.controller;

import java.util.Map;
import org.slf4j.Logger;
import edu.tglima.jssm.model.*;
import org.slf4j.LoggerFactory;
import edu.tglima.jssm.dto.ResultDTO;
import org.springframework.http.HttpStatus;
import edu.tglima.jssm.service.MailService;
import edu.tglima.jssm.service.DatabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MessageController
{
    @Autowired
    MailService mailService;

    @Autowired
    DatabaseService dbService;

    ResultDTO resultDTO;
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/getMessages")
    public HttpStatus getMessages()
    {
        logger.info("getMessages => Start");
        resultDTO = dbService.findMessages();

        if (!resultDTO.HasError)
        {
            Map<String, Message> messageMap = resultDTO.MapMessage;
            logger.info("getMessages => qtTotalMessages: {} ",  messageMap.size() );

            if(messageMap.size() > 0)
            {
                this.processMessage(messageMap);
            }
        }
        else
        {
            logger.error("getMessages => HasError");
            logger.error("getMessages => Error Message: " + resultDTO.ErrorMessage);
            return HttpStatus.BAD_REQUEST;
        }

        logger.info("getMessages => End");
        return HttpStatus.OK;
    }

    private void processMessage(Map<String, Message> messageMap)
    {
        logger.info("processMessage => Start");

        messageMap.forEach((key,msg) ->
        {
            ConfigMessageFromSite configMessageFromSite;
            boolean hasError;

            logger.info("| idMessage: {}  projectSource: {} |", key, msg.projectSource);

            resultDTO = new ResultDTO(false);
            resultDTO = dbService.getConfigMessageFromSite(msg.projectSource);

            if (!resultDTO.HasError)
            {
                configMessageFromSite = (ConfigMessageFromSite) resultDTO.Value;
                hasError = this.mailService.sendMail(msg, configMessageFromSite);
            }
            else
            {
                logger.warn("processMessage => configMessageFromSite hasError");

                ExceptionLog exceptionLog = new ExceptionLog(
                        msg.projectSource, "processMessage",
                        resultDTO.ErrorMessage, null);

                dbService.saveExceptionLog(exceptionLog);
                hasError = true;
            }

            dbService.updateMessage(key, msg, hasError);

        });

        logger.info("processMessage => End");
    }


}

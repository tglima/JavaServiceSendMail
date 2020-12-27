package edu.tglima.jssm.model;
import com.google.cloud.Timestamp;
import java.util.Map;
import java.util.HashMap;

public class Message {

    public Timestamp dateRegister;
    public String nameSender;
    public String emailSender;
    public String nuPhoneSender;
    public String messageText;
    public String projectSource;
    public boolean wasForwarded;

    public Integer qtAttemptForward;
    public boolean wasError;
    public Timestamp dateLastError;
    public Timestamp dateForwarded;

    public Message() {}

    public static Map<String, Object> ConvertToMap(Message pMessage)
    {
        Map<String, Object> hashMapMessage = new HashMap<>();
        hashMapMessage.put("emailSender", pMessage.emailSender);
        hashMapMessage.put("messageText", pMessage.messageText);
        hashMapMessage.put("nameSender", pMessage.nameSender);
        hashMapMessage.put("nuPhoneSender", pMessage.nuPhoneSender);
        hashMapMessage.put("projectSource", pMessage.projectSource);
        hashMapMessage.put("dateRegister", pMessage.dateRegister);
        hashMapMessage.put("wasForwarded", pMessage.wasForwarded);
        hashMapMessage.put("qtAttemptForward", pMessage.qtAttemptForward);
        hashMapMessage.put("wasError", pMessage.wasError);
        hashMapMessage.put("dateLastError", pMessage.dateLastError);
        hashMapMessage.put("dateForwarded", pMessage.dateForwarded);
        return hashMapMessage;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "dateRegister=" + dateRegister +
                ", nameSender='" + nameSender + '\'' +
                ", emailSender='" + emailSender + '\'' +
                ", nuPhoneSender='" + nuPhoneSender + '\'' +
                ", messageText='" + messageText + '\'' +
                ", projectSource='" + projectSource + '\'' +
                ", wasForwarded=" + wasForwarded +
                ", qtAttemptForward=" + qtAttemptForward +
                ", wasError=" + wasError +
                ", dateLastError=" + dateLastError +
                ", dateForwarded=" + dateForwarded +
                '}';
    }
}
package edu.tglima.jssm.service;

import org.slf4j.Logger;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import edu.tglima.jssm.model.Message;
import com.google.api.core.ApiFuture;
import edu.tglima.jssm.dto.ResultDTO;
import edu.tglima.jssm.helper.Constant;
import edu.tglima.jssm.model.ExceptionLog;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
import com.google.firebase.cloud.FirestoreClient;
import edu.tglima.jssm.model.ConfigMessageFromSite;

@Service
public class DatabaseService
{
    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private ResultDTO resultDTO;

    public ResultDTO findMessages()
    {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        resultDTO = new ResultDTO(false);

        Map<String, Message>  mapMessages = new HashMap<>();

        try
        {
            ApiFuture<QuerySnapshot> apiFuture =
                    dbFirestore.collection(Constant.COLLECTION_MESSAGE)
                            .whereEqualTo ("wasForwarded", false)
                            .get();
            List<QueryDocumentSnapshot> documentSnapshots = apiFuture.get().getDocuments();

            documentSnapshots.forEach(doc -> {
                Message message =  doc.toObject(Message.class);
                mapMessages.put(doc.getId(), message);
            });
            resultDTO.MapMessage = mapMessages;
        }
        catch (ExecutionException | InterruptedException ex)
        {
            logger.error(ex.getMessage());
            resultDTO.Value = null;
            resultDTO.HasError = true;
            resultDTO.ErrorMessage = ex.getMessage();
        }

        return resultDTO;
    }

    public void updateMessage(String keyDocument, Message message,  boolean hasError)
    {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        message.wasError = hasError;
        message.wasForwarded = !hasError;
        message.qtAttemptForward = message.qtAttemptForward == null ? 1 : message.qtAttemptForward +1;
        message.dateLastError = message.wasError ? Timestamp.now() : message.dateLastError;
        message.dateForwarded = message.wasForwarded ? Timestamp.now(): null;
        dbFirestore.
                collection(Constant.COLLECTION_MESSAGE).
                document(keyDocument).update(Message.ConvertToMap(message));
    }

    public void saveExceptionLog(ExceptionLog exceptionLog)
    {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        dbFirestore.collection(Constant.COLLECTION_LOG_EXCEPTION).
                document().set(exceptionLog);
    }

    public ResultDTO getConfigMessageFromSite(String projectSource)
    {
        resultDTO = new ResultDTO(false);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        String pathCollection = "projectConfiguration/"+projectSource+"/configMessageFromSite";
        DocumentReference documentReference = dbFirestore.collection(pathCollection).document(Constant.DEFAULT);
        ConfigMessageFromSite configMessageFromSite = null;

        try
        {
            ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = apiFuture.get();

            if (documentSnapshot.exists())
            {
                configMessageFromSite = documentSnapshot.toObject(ConfigMessageFromSite.class);
            }
            resultDTO.Value = configMessageFromSite;
        }
        catch(ExecutionException | InterruptedException e)
        {
            logger.error(e.getMessage());
            resultDTO = new ResultDTO(true, e.getMessage(), null, null);
        }

        return resultDTO;
    }

}

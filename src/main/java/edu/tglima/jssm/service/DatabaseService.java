package edu.tglima.jssm.service;

import org.slf4j.Logger;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import com.google.cloud.firestore.*;
import edu.tglima.jssm.model.Message;
import com.google.api.core.ApiFuture;
import edu.tglima.jssm.dto.ResultDTO;
import edu.tglima.jssm.helper.Constant;
import java.util.concurrent.ExecutionException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

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

}

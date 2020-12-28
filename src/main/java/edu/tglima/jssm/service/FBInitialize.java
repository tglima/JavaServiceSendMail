package edu.tglima.jssm.service;


import edu.tglima.jssm.helper.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class FBInitialize
{
    @Autowired
    ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(FBInitialize.class);

    @PostConstruct
    public void initialize()
    {
        try
        {
            Resource resource = resourceLoader.getResource(Constant.PATH_FILE_GOOGLE_ACCOUNT_KEY);
            FirebaseOptions firebaseOptions =  FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .setDatabaseUrl(Constant.URL_DB_FIREBASE)
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }

}

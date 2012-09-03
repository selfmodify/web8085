package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shastram.web8085.client.rpc.SaveFileData;

/**
 * Caters to box.net service
 * 
 * @author vijay
 * 
 */
public class BoxNetService {
    private static Logger logger = Logger.getLogger(BoxNetService.class
            .getName());
    private String apiKey = "e2ldex7lk8ydcmmnlv7s1oajh4siymqf";
    private String newTicketUrl = "https://www.box.com/api/1.0/rest?action=get_ticket&api_key="
            + apiKey;

    public static ObjectMapper jsonReader = getReader();

    private static ObjectMapper getReader() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public BoxNetService() {
    }

    public BoxNetData.BoxNetTicketResponse getNewTicket() throws Exception {
        BoxNetData.BoxNetTicketResponse response = null;
        URL url = new URL(newTicketUrl);
        response = (BoxNetData.BoxNetTicketResponse) readXml(new BufferedReader(
                new InputStreamReader(url.openStream())),
                BoxNetData.BoxNetTicketResponse.class);
        return response;
    }

    public static Object readXml(Reader reader, Class<?> cl) throws Exception {
        JAXBContext context = JAXBContext.newInstance(cl);
        return context.createUnmarshaller().unmarshal(reader);
    }


    public BoxNetData.BoxNetAuthResponse getAuthToken(BoxNetData.BoxNetTicketResponse ticket)
            throws Exception {
        String authUrl = "https://www.box.com/api/1.0/rest?action=get_auth_token&api_key="
                + apiKey + "&ticket=" + ticket.getTicket();
        URL url = new URL(authUrl);
        BoxNetData.BoxNetAuthResponse authResponse = (BoxNetData.BoxNetAuthResponse) readXml(
                new BufferedReader(new InputStreamReader(url.openStream())),
                BoxNetData.BoxNetAuthResponse.class);
        return authResponse;
    }

    public String getFileUploadResponse(SaveFileData saveFileData) throws Exception {
        ByteArrayBody body = new ByteArrayBody(saveFileData.getData().getBytes(), saveFileData.getFileName());
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("filename1", body);
        entity.addPart("folder_id", new StringBody("0"));
        String fileId = saveFileData.getFileId();
        String url = fileId == null ?
                "https://api.box.com/2.0/files/" + fileId + "/data" :
                    "https://api.box.com/2.0/files/data";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Authorization", "BoxAuth api_key=e2ldex7lk8ydcmmnlv7s1oajh4siymqf"
                + "&auth_token=" + saveFileData.getAuthToken());
        httpPost.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity);
        return result;
    }

    /**
     * Save file to box.net.
     * @param saveFileData
     * @return
     * @throws Exception
     */
    @Nullable
    public static BoxNetData.BoxNetFileUploadResponse saveFileToBoxNet(SaveFileData saveFileData, BoxNetService boxNetService)
            throws Exception {
        BoxNetData.BoxNetFileUploadResponse boxNetResponse = null;
        // Try the upload twice because the first time around box.net might return
        // 'a file with that id already exists' and then we need to hit it again with
        // the right fileId in order to overwrite the file.
        for (int i=0; i < 2; ++i) {
            String result = boxNetService.getFileUploadResponse(saveFileData);
            boxNetResponse = jsonReader.readValue(result, BoxNetData.BoxNetFileUploadResponse.class);
            if (boxNetResponse.entries.size() > 0) {
                BoxNetData.BoxNetFileUploadResponseEntry entry = boxNetResponse.entries.get(0);
                if (entry.type.equalsIgnoreCase("error")
                        && entry.status.equalsIgnoreCase("409")
                        && entry.context_info.conflicts.size() > 0) {
                    BoxNetData.BoxNetFileUploadConflicts conflicts = entry.context_info.conflicts.get(0);
                    // Copy the correct file id again and retry one more time.
                    saveFileData.setFileId(conflicts.id);
                    continue;
                }
            }
            // there was no error hence we bailout.
            break;
        }
        return boxNetResponse;
    }

    public void testBoxNetFileUploadResponse( ) throws IOException {
        jsonReader.readValue(FileUtils.readFileToString(new File("box-net-file-response.txt")),
                BoxNetData.BoxNetFileUploadResponse.class);
    }
    /*
    public static void main(String[] args) throws Exception {
        BoxNetService service = new BoxNetService();
        BoxNetTicketResponse ticket = service.getNewTicket();
        BoxNetAuthResponse authToken = service.getAuthToken(ticket);
        authToken.auth_token = "s7q6hkklutef9ex5jxk4z0kj7bkgcjq6";
        BoxNetService boxNetService = new BoxNetService();
        BoxNetFileUploadResponse saveResult =
                boxNetService.saveFileToBoxNet(
                new SaveFileData(authToken.auth_token, "noname-1.85",
                        "183787071",
                        "This is test data."));
        logger.info("SaveResult=" + saveResult);
    }
    */
}

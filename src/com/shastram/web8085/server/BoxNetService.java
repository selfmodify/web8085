package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxNetFileUploadConflicts {
        public String type;
        public String id;
        public BoxNetFileUploadConflicts() {
            
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxNetFileUploadContextInfo {
        public List<BoxNetFileUploadConflicts> conflicts;
        public BoxNetFileUploadContextInfo() {
            
        }
    }

    /**
     * Box.net response for every entry of file uploaded.
     * @author vijay
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxNetFileUploadResponseEntry {
        public String type;
        public String id;
        public String sequence_id;
        public String name;
        public String description;
        public int size;
        public String path;
        public String path_id;
        public String created_at;
        public String modified_at;
        public String etag;
        public String status;
        public String message;
        public String code;
        public BoxNetFileUploadContextInfo context_info;
        public BoxNetFileUploadResponseEntry() {
            
        }
    }
    
    /**
     * Box.net response
     * @author vijay
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxNetFileUploadResponse {
        public int total_count;
        public List<BoxNetFileUploadResponseEntry> entries;

        public BoxNetFileUploadResponse() {
            
        }
    }

    /**
     * Box.net Ticket response. The ticket is used in the next stage to get the
     * auth token.
     * 
     * @author vijay
     * 
     */
    @XmlRootElement(name = "response")
    public static class BoxNetTicketResponse {
        private String status;
        private String ticket;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }
    }

    @XmlRootElement(name = "user")
    public static class BoxNetUser {
        private String login;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        private String email;
    }

    /**
     * Get the auth token from box.net
     * 
     * @author vijay
     * 
     */
    @XmlRootElement(name = "response")
    public static class BoxNetAuthResponse {
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAuth_token() {
            return auth_token;
        }

        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

        public BoxNetUser getUser() {
            return user;
        }

        public void setUser(BoxNetUser user) {
            this.user = user;
        }

        private String status;
        private String auth_token;
        private BoxNetUser user;
    }

    public BoxNetService() {
    }

    public BoxNetTicketResponse getNewTicket() throws Exception {
        BoxNetTicketResponse response = null;
        URL url = new URL(newTicketUrl);
        response = (BoxNetTicketResponse) readXml(new BufferedReader(
                new InputStreamReader(url.openStream())),
                BoxNetTicketResponse.class);
        return response;
    }

    public static Object readXml(Reader reader, Class<?> cl) throws Exception {
        JAXBContext context = JAXBContext.newInstance(cl);
        return context.createUnmarshaller().unmarshal(reader);
    }


    public BoxNetAuthResponse getAuthToken(BoxNetTicketResponse ticket)
            throws Exception {
        String authUrl = "https://www.box.com/api/1.0/rest?action=get_auth_token&api_key="
                + apiKey + "&ticket=" + ticket.ticket;
        URL url = new URL(authUrl);
        BoxNetAuthResponse authResponse = (BoxNetAuthResponse) readXml(
                new BufferedReader(new InputStreamReader(url.openStream())),
                BoxNetAuthResponse.class);
        return authResponse;
    }

    /**
     * Save file to box.net.
     * @param saveFileData
     * @return
     * @throws Exception
     */
    public static BoxNetFileUploadResponse saveFileToBoxNet(SaveFileData saveFileData) throws Exception {
        HttpPost httpPost = new HttpPost("https://api.box.com/2.0/files/" + saveFileData.getFileId() + "/data");
        httpPost.addHeader("Authorization", "BoxAuth api_key=e2ldex7lk8ydcmmnlv7s1oajh4siymqf"
                + "&auth_token=" + saveFileData.getAuthToken());
        DefaultHttpClient client = new DefaultHttpClient();
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ByteArrayBody body = new ByteArrayBody(saveFileData.getData().getBytes(), saveFileData.getFileName());
        entity.addPart("filename1", body);
        entity.addPart("folder_id", new StringBody("0"));
        httpPost.setEntity(entity);
        HttpResponse response = client.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity);
        BoxNetFileUploadResponse boxNetResponse = jsonReader.readValue(result, BoxNetFileUploadResponse.class);
        return boxNetResponse;
    }

    public void testBoxNetFileUploadResponse( ) throws IOException {
        jsonReader.readValue(FileUtils.readFileToString(new File("box-net-file-response.txt")),
                BoxNetFileUploadResponse.class);
    }
    public static void main(String[] args) throws Exception {
        BoxNetService service = new BoxNetService();
        BoxNetTicketResponse ticket = service.getNewTicket();
        BoxNetAuthResponse authToken = service.getAuthToken(ticket);
        authToken.auth_token = "s7q6hkklutef9ex5jxk4z0kj7bkgcjq6";
        BoxNetFileUploadResponse saveResult = saveFileToBoxNet(
                new SaveFileData(authToken.auth_token, "noname-1.85",
                        "183787071",
                        "This is test data."));
        logger.info("SaveResult=" + saveResult);
    }
}

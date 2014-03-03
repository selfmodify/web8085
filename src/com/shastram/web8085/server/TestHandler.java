package com.shastram.web8085.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/oauth2callback")
public class TestHandler {

    private static Logger logger = Logger.getLogger(TestHandler.class.getName());

    @GET
    public String get(@QueryParam("code") String code) throws IOException {
        String hostName = "http://127.0.0.1:8888";
        ArrayList<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("code", code));
        formData.add(new BasicNameValuePair("redirect_uri", hostName + "/oauth2callback"));
        formData.add(new BasicNameValuePair("client_id", "1096071804926.apps.googleusercontent.com"));
        formData.add(new BasicNameValuePair("client_secret", "_cYDS_vapkNZbxmiuvz10T7V"));
        formData.add(new BasicNameValuePair("grant_type", "authorization_code"));
        try {
            HttpPost postReq = new HttpPost("https://accounts.google.com/o/oauth2/token");
            postReq.setEntity(new UrlEncodedFormEntity(formData));
            CloseableHttpClient req = HttpClients.createDefault();
            CloseableHttpResponse response = req.execute(postReq);
            ObjectMapper mapper = new ObjectMapper();
            InputStream input = response.getEntity().getContent();
            @SuppressWarnings("unchecked")
            HashMap<String,String> result = (HashMap<String,String>)mapper.readValue(input, HashMap.class);
            String accessToken = result.get("access_token");
            String refreshToken = result.get("refresh_token");
            saveFile(null, accessToken, "test.85.txt", "Hello World");
        } catch (IOException e) {
            throw e;
        }
        return "";
    }

    @PUT
    private String saveFile(@Nullable String fileId, String fileName, String data, String accessToken ) throws IOException {
        String url = "https://www.googleapis.com/upload/drive/v2/files" + (fileId == null ?
                "?uploadType=multipart" : ("/" + fileId));
        HttpPost fileUpload = new HttpPost(url);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addTextBody("title", "{ \"title\": \"" + fileName + "\" }", ContentType.APPLICATION_JSON)
                .addTextBody("file", data, ContentType.create("text/plain"))
                .build();
        
        fileUpload.addHeader("Authorization", "Bearer " + accessToken);
        // look at https://developers.google.com/drive/web/manage-uploads#multipart for explanation.
        fileUpload.setEntity(entity);
        CloseableHttpClient req = HttpClients.createDefault();
        CloseableHttpResponse response = req.execute(fileUpload);
        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        HashMap<String,String> result = (HashMap<String,String>)mapper.readValue(response.getEntity().getContent(), HashMap.class);
        logger.log(Level.INFO, "Response = " + result);
        String savedFileId = result.get("id");
        return savedFileId;
    }
    
    public static void main(String[] argv) throws IOException {
        TestHandler t = new TestHandler();
        //t.get("4/UG0ZXy4J9vmWV1kDqx0MBKQaPQzv.0h7sWmZuUFgVMqTmHjyTFGMOHDTuiAI");
        t.saveFile(null, "ya29.1.AADtN_UuT2pYgPg_pAohACaLYMaMXVsNzdsj5-oMq08tSGHwhuKH7Kj9MNUnrHc", "test.85", "ThisIsTest");
    }
}

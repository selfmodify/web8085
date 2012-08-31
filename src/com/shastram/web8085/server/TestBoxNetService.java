package com.shastram.web8085.server;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.mockito.Mockito;

import junit.framework.TestCase;

import com.shastram.web8085.client.rpc.SaveFileData;
import com.shastram.web8085.server.BoxNetService.BoxNetFileUploadResponse;
import com.shastram.web8085.server.BoxNetService.BoxNetTicketResponse;

public class TestBoxNetService extends TestCase {


    public void testBoxExchange() throws Exception {
        // read the error response data
        String errorResponse = FileUtils.readFileToString(
                new File("box-net-file-upload-error-response.txt"));

        // read the error response data
        String successResponse = FileUtils.readFileToString(
                new File("box-net-file-upload-response.txt"));
        SaveFileData saveFileData =
                new SaveFileData("s7q6hkklutef9ex5jxk4z0kj7bkgcjq6",
                        "noname-1.85",
                        "3011590059",
                        "This is test data.3");
        when(BoxNetService.getFileUploadResponse(saveFileData))
        .thenReturn(errorResponse, successResponse);
        BoxNetFileUploadResponse response = BoxNetService.saveFileToBoxNet(saveFileData);
        assertNotNull(response);
    }
    /**
     * Test box.net ticket response parsing.
     * @throws Exception
     */
    public void testBoxNetTicketResponse() throws Exception {
        Object response = BoxNetService.readXml(new StringReader("<response>\n"
                + "<status>get_ticket_ok</status>\n"
                + "<ticket>bxquuv025araaaaaze2n4md9zef95e8</ticket>\n"
                + "</response>"), BoxNetTicketResponse.class);
        assertNotNull(response);
    }

    /**
     * Test file upload response.
     * @throws IOException 
     */
    public void testBoxNetFileUploadResponse() throws IOException {
        BoxNetFileUploadResponse readValue =
                BoxNetService.jsonReader.readValue(new File("box-net-file-upload-response.txt"),
                BoxNetService.BoxNetFileUploadResponse.class);
        assertNotNull(readValue);
    }

    public void testBoxNetFileUploadErrorResponse() throws IOException {
        BoxNetFileUploadResponse readValue =
                BoxNetService.jsonReader.readValue(new File("box-net-file-upload-error-response.txt"),
                BoxNetService.BoxNetFileUploadResponse.class);
        assertNotNull(readValue);
    }

    /**
     * Test real file upload response from box.com.
     * @throws Exception
     */
    public void testBoxNetFileUpload() throws Exception {
        BoxNetFileUploadResponse saveResult =
                BoxNetService.saveFileToBoxNet(
                new SaveFileData("s7q6hkklutef9ex5jxk4z0kj7bkgcjq6",
                        "noname-1.85",
                        "3011590059",
                        "This is test data.3"));
        assertNotNull(saveResult);
    }
}

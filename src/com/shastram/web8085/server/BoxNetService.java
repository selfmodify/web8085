package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shastram.web8085.server.BoxNetService.BoxNetTicketResponse;


/**
 * Caters to box.net service
 * @author vijay
 *
 */
public class BoxNetService {
    private static boolean testing = false;
    private static Logger logger = Logger.getLogger(BoxNetService.class.getName());
    private String apiKey = "e2ldex7lk8ydcmmnlv7s1oajh4siymqf";
    private String newTicketUrl =
            "https://www.box.com/api/1.0/rest?action=get_ticket&api_key=" + apiKey;
    
    /**
     * Box.net Ticket response. The ticket is used in the next stage to
     * get the auth token.
     * @author vijay
     *
     */
    @XmlRootElement(name="response")
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

    @XmlRootElement(name="user")
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
     * @author vijay
     *
     */
    @XmlRootElement(name="response")
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
        if (testing) {
            readXml(new StringReader("<response>\n"
                    +"<status>get_ticket_ok</status>\n"
                    +"<ticket>bxquuv025araaaaaze2n4md9zef95e8</ticket>\n"
                    + "</response>"), BoxNetTicketResponse.class);
        } else {
            URL url = new URL(newTicketUrl);
            response = (BoxNetTicketResponse)readXml(
                    new BufferedReader(new InputStreamReader(url.openStream())),
                    BoxNetTicketResponse.class);
        }
        return response;
    }

    private Object readXml(Reader reader, Class cl) throws Exception {
        JAXBContext context = JAXBContext.newInstance(cl);
        return context.createUnmarshaller().unmarshal(reader);
    }

    public static void main(String[] args) throws Exception {
        BoxNetService service = new BoxNetService();
        BoxNetTicketResponse ticket = service.getNewTicket();
        BoxNetAuthResponse authToken = service.getAuthToken(ticket);
        logger.info("Status=" + authToken.status);
    }

    public BoxNetAuthResponse getAuthToken(BoxNetTicketResponse ticket) throws Exception {
        String authUrl =
                "https://www.box.com/api/1.0/rest?action=get_auth_token&api_key="
                + apiKey
                + "&ticket=" + ticket.ticket;
        URL url = new URL(authUrl);
        BoxNetAuthResponse authResponse =
                (BoxNetAuthResponse)readXml(
                        new BufferedReader(new InputStreamReader(url.openStream())),
                        BoxNetAuthResponse.class);
        return authResponse;
    }
}

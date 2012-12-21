package com.shastram.web8085.server;

/**
 * Box.net data structures.
 */
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class BoxNetData {
    /**
     * Auth token response from box.net
     * 
     * @author vijay
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
        public Exception exception;
        public List<BoxNetFileUploadResponseEntry> entries;

        public BoxNetFileUploadResponse() {
            
        }
        
        @Override
        public String toString() {
            if (entries != null) {
                StringBuilder sb = new StringBuilder();
                for (BoxNetFileUploadResponseEntry e: entries) {
                    sb.append(e.toString());
                    sb.append(",\n");
                }
                return sb.toString();
            } else if (exception != null) {
                return exception.toString();
            }
            return "NULL BoxNetFileUploadResponse";
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
}

package com.shastram.web8085.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.Query;
import com.shastram.web8085.client.FileData;
import com.shastram.web8085.client.FileInfo;
import com.shastram.web8085.client.LoginData;
import com.shastram.web8085.client.ServiceResponse;
import com.shastram.web8085.client.Web8085Service;
import com.shastram.web8085.server.db.ServerFileData;
import com.shastram.web8085.server.db.UserData;

public class Web8085ServiceImpl extends RemoteServiceServlet implements
        Web8085Service {
    private static Logger logger = Logger.getLogger(Web8085ServiceImpl.class.getName());
    private static final long serialVersionUID = 6343983694137057114L;

    @Override
    public List<String> getExampleNames() {
        List<String> list = Arrays.asList(TestInstructions.getTestNames());
        return list;
    }

    @Override
    public String saveToDrive(String src) {
        this.getThreadLocalRequest().getCookies();
        String scope = "&scope=https://www.googleapis.com/auth/drive "
                + "https://www.googleapis.com/auth/plus.login "
                + "https://www.googleapis.com/auth/plus.me "
                + "https://www.googleapis.com/auth/userinfo.email "
                + "https://www.googleapis.com/auth/userinfo.profile";
        String approval = "&approval_prompt=force&access_type=offline";
        String url = "https://accounts.google.com/o/oauth2/auth?"
                + "redirect_uri=http://127.0.0.1:8888/oauth2callback"
                + "&response_type=code"
                + "&client_id=1096071804926.apps.googleusercontent.com"
                + scope
                + approval;
        return url;
    }

    @Override
    public LoginData getLoginData(LoginData d) {
        UserService us = UserServiceFactory.getUserService();
        String url = "http://127.0.0.1:8888/Simulator.html?gwt.codesvr=127.0.0.1:9997";
        String loginUrl = us.createLoginURL(url);
        LoginData result = new LoginData(d.isProdMode());
        result.setLoginUrl(loginUrl);
        return result;
    }

    @Nullable
    public User getCurrentUser() {
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            logger.info("User is not logged in");
        } else {
            logger.info("Currently logged in user=" + currentUser.getEmail());
        }
        return currentUser;
    }

    @Override
    public ServiceResponse saveFile(FileData clientFileData) {
        final User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ServiceResponse(true/*loginRequired*/);
        }
        @SuppressWarnings("unused")
        UserData user = getOrCreateUser(currentUser);
        ServerFileData toBeSavedFileData = new ServerFileData(currentUser.getEmail(), clientFileData);
        ServerFileData existingFile = getFileFromDb(user, toBeSavedFileData);
        if (existingFile != null) {
            // File with this name already exist.
            if (clientFileData.isOverwriteExisting() ||
                    existingFile.getCreated().equals(clientFileData.getFileInfo().getDateCreated())) {
                // Free to overwrite file or
                // the create date of the files match hence we can overwrite the file.
                existingFile.setData(toBeSavedFileData.getFileContent());
                existingFile.updateLastModified();
                toBeSavedFileData = existingFile;
            } else {
                // Tell the user that they are about to overwrite a file.
                return ServiceResponse.aboutToOverwrite(existingFile.getCreated());
            }
        }
        // Save the file.
        ObjectifyService.ofy().save().entity(toBeSavedFileData).now();
        return ServiceResponse.fileSaved(toBeSavedFileData.getId(), toBeSavedFileData.getFileName(),
                toBeSavedFileData.getCreated(), toBeSavedFileData.getLastModified());
    }

    private ServerFileData getFileFromDb(UserData user, ServerFileData serverData) {
        return ObjectifyService.ofy().load().type(ServerFileData.class).id(serverData.getId()).now();
    }

    private UserData getOrCreateUser(final User currentUser) {
        final UserData userData = new UserData();
        ObjectifyService.ofy().transact(new VoidWork() {
            @Override
            public void vrun() {
                UserData user = getCurrentUserData(currentUser);
                if (user == null) {
                    user = new UserData(currentUser.getEmail(), currentUser.getNickname(), currentUser.getEmail());
                    userData.setData(user);
                    ObjectifyService.ofy().save().entity(user).now();
                }
            }
        });
        return userData;
    }

    private UserData getCurrentUserData(final User currentUser) {
        if (currentUser == null) {
            return null;
        }
        UserData user = ObjectifyService.ofy().load().type(UserData.class).id(currentUser.getEmail()).now();
        return user;
    }

    @Override
    public ServiceResponse listFiles() {
        final User currentUser = getCurrentUser();
        UserData user = getCurrentUserData(currentUser);
        if (user == null) {
            return new ServiceResponse(true/*loginRequired*/);
        }
        List<ServerFileData> serverFileList = ObjectifyService.ofy().load().type(ServerFileData.class).filter("owner", user.getId()).list();
        List<FileInfo> clientFileList = new ArrayList<>();
        for(ServerFileData f: serverFileList) {
            clientFileList.add(new FileInfo(f.getId(), f.getFileName(), f.getCreated(), f.getLastModified()));
        }
        ServiceResponse response = new ServiceResponse();
        response.setFileList(clientFileList);
        return response;
    }
}

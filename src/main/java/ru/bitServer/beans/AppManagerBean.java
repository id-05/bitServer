package ru.bitServer.beans;


import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.NetworkAdapter;
import ru.bitServer.service.NetworkSettingsParcer;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@ManagedBean(name = "appManagerBean")
@ViewScoped
public class AppManagerBean implements UserDao {

    @PostConstruct
    public void init() {
        System.out.println("appManagerBean");
    }

    public StreamedContent getGetResult() throws IOException {
        String strpath = "/home/tomcat/webapps/bitServer.war";
        InputStream inputStream = new FileInputStream(strpath);
        return DefaultStreamedContent.builder()
                .name("bitServer.war")
                .contentType("application/war")
                .stream(() -> inputStream)
                .build();
    }
}
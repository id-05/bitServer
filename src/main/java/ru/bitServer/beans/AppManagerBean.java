package ru.bitServer.beans;

import com.google.common.net.HttpHeaders;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "appManagerBean")
@ViewScoped
public class AppManagerBean implements UserDao {

    public List<BitServerApp> getAppList() {
        return appList;
    }

    public void setAppList(List<BitServerApp> appList) {
        this.appList = appList;
    }

    List<BitServerApp> appList;

    @PostConstruct
    public void init() {
        System.out.println("appManagerBean");
        getAppVersion();
    }

    void getAppVersion()  {
        try{
            Path file = Paths.get("/home/tomcat/webapps/bitServer.war");
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            attrs.creationTime();
            appList = new ArrayList<>();
            appList.add(new BitServerApp("bitServer",String.valueOf(attrs.creationTime()).substring(0,10)));
        }
        catch (Exception e){
            System.out.println("error read file attribute");
        }
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

    public void openTomcat() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String referrer =  request.getHeader(HttpHeaders.REFERER);
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String address = referrer.substring(j+3,i);
        LogTool.getLogger().info("http://" + address+"/manager/");
        //PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+address+"/viewer/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        PrimeFaces.current().executeScript("window.open('"+"http://"+address+"/manager/','_blank')");
    }

    public class BitServerApp {
        String name;
        String date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        BitServerApp(String name, String date){
            this.name = name;
            this.date = date;
        }
    }
}


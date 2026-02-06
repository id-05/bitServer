package ru.bitServer.beans;

import com.google.common.net.HttpHeaders;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.BitServerApp;
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
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

@ManagedBean(name = "appManagerBean")
@ViewScoped
public class AppManagerBean implements UserDao {

    public List<BitServerApp> getAppList() {
        return appList;
    }
    public List<BitServerApp> selectedVisibleApps = new ArrayList<>();

    public List<BitServerApp> getSelectedVisibleApps() {
        return selectedVisibleApps;
    }

    public void setSelectedVisibleApps(List<BitServerApp> selectedVisibleApps) {
        this.selectedVisibleApps = selectedVisibleApps;
    }

    public void setAppList(List<BitServerApp> appList) {
        this.appList = appList;
    }

    List<BitServerApp> appList;
    BitServerApp selectedVisibleApp;

    public BitServerApp getSelectedVisibleApp() {
        return selectedVisibleApp;
    }

    public void setSelectedVisibleApp(BitServerApp selectedVisibleApp) {
        this.selectedVisibleApp = selectedVisibleApp;
    }

    @PostConstruct
    public void init() {
        selectedVisibleApp = new BitServerApp();
        selectedVisibleApps.clear();
        getAppVersion();
        selectedVisibleApp = appList.get(0);
    }

    void getAppVersion()  {
        try{
            appList = new ArrayList<>();
            String pathtowar = "/home/tomcat/webapps/";
            Files.walk(Paths.get(pathtowar), FOLLOW_LINKS)
                    .forEach(file -> {
                        if(file.toFile().isFile() && file.toFile().getPath().endsWith(".war")){
                            BasicFileAttributes attrs;
                            try {
                                attrs = Files.readAttributes(file, BasicFileAttributes.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            attrs.creationTime();
                            appList.add(new BitServerApp(file.getFileName().getName(0).toString(), String.valueOf(attrs.creationTime()).substring(0,10)));
                        }
                    });
        }
        catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public StreamedContent getGetResult() throws IOException {
        String strpath = "/home/tomcat/webapps/" + selectedVisibleApp.getName(); // "/home/tomcat/webapps/"
        InputStream inputStream = new FileInputStream(strpath);
        return DefaultStreamedContent.builder()
                .name(selectedVisibleApp.getName())
                .contentType("application/war")
                .stream(() -> inputStream)
                .build();
    }

    public void onRowSelect(SelectEvent event) {
        selectedVisibleApp = (BitServerApp) event.getObject();
    }

    public void openTomcat() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String referrer =  request.getHeader(HttpHeaders.REFERER);
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String address = referrer.substring(j+3,i);
        if(address.contains(":")){
            int k = address.indexOf(":");
            String addressCutPort = address.substring(0,k);
            PrimeFaces.current().executeScript("window.open('"+"http://"+"admin"+":"+"password"+"@"+addressCutPort+":8080/manager/','_blank')");
        }else{
            PrimeFaces.current().executeScript("window.open('"+"http://"+"admin"+":"+"password"+"@"+address+":8080/manager/','_blank')");
        }
    }


}


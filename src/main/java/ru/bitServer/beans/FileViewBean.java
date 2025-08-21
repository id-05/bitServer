package ru.bitServer.beans;

import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import java.io.FileReader;

@ManagedBean(name = "fileviewBean", eager = true)
@RequestScoped
public class FileViewBean implements UserDao {

    String viewFileText;

    public String getViewFileText() {
        return viewFileText;
    }

    public void setViewFileText(String viewFileText) {
        this.viewFileText = viewFileText;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession() ;
        String filename = session.getAttribute("filename").toString();
        String logpath = getBitServerResource("logpathorthanc").getRvalue();

        StringBuilder logFile = new StringBuilder();
        try(FileReader reader = new FileReader(logpath+"/"+filename)) {
            int c;
            while ((c = reader.read()) != -1) {
                logFile.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().error(LogTool.getLogger() + " Error of read file init() networkSettingsBean: "+e.getMessage());
        }
        viewFileText = logFile.toString();
    }
}

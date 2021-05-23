package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.beans.MainBean;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static ru.bitServer.beans.MainBean.info;
import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "queueremoteBean", eager = true)
@SessionScoped
public class QueueremoteBean implements UserDao {

    //private static List<String> selectedModaliti = new ArrayList<>();
    private List<BitServerStudy> visibleStudiesList;
    private BitServerStudy selectedVisibleStudy;
    public List<Usergroup> usergroupList;
    public String selectedUserGroup;
    public Users currentUser;
    public String currentUserId;
    private UploadedFile resultFile;
    private OrthancRestApi connection;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public UploadedFile getResultFile() {
        return resultFile;
    }

    public void setResultFile(UploadedFile resultFile) {
        this.resultFile = resultFile;
    }

    public List<BitServerStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<BitServerStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    public BitServerStudy getSelectedVisibleStudy() {
        return selectedVisibleStudy;
    }

    public void setSelectedVisibleStudy(BitServerStudy selectedVisibleStudy) {
        this.selectedVisibleStudy = selectedVisibleStudy;
    }

    @PostConstruct
    public void init() {
        selectedVisibleStudy = new BitServerStudy();
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        System.out.println("QueueremoteBean");
        usergroupList = getActiveBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        dataoutput();
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void dataoutput() {
        visibleStudiesList = getBitServerStudyOnAnalisis(currentUser.getGroupUser());
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void addResult() throws IOException {
        selectedVisibleStudy.setStatus(2);
        selectedVisibleStudy.setUserwhodiagnost(currentUserId);
        System.out.println("sid = "+selectedVisibleStudy.getSid());
        selectedVisibleStudy.setDateresult(new Date());
        if(resultFile!=null){
            Path folder = Paths.get(MainBean.pathToSaveResult);
            String extension = FilenameUtils.getExtension(resultFile.getFileName());
            try (InputStream input = resultFile.getInputStream()) {
                FileOutputStream output = new FileOutputStream(folder.toString()+"/" + selectedVisibleStudy.getSid() + "." + extension);
                byte[] buffer = new byte[input.available()];
                input.read(buffer, 0, buffer.length);
                output.write(buffer, 0, buffer.length);
            }catch (Exception e){
                System.out.println("e = "+e.getMessage());
            }
            selectedVisibleStudy.setTyperesult(true);
            selectedVisibleStudy.setResult(folder.toString()+"\\" + selectedVisibleStudy.getSid() + "." + extension);
        }else{
            selectedVisibleStudy.setTyperesult(false);
        }
        connection.deleteStudyFromOrthanc(selectedVisibleStudy);
        updateStudyInBitServerStudyTable(selectedVisibleStudy);
        PrimeFaces.current().executeScript("PF('sidebar').hide()");
        showMessage("Информация","Заключение было прикреплено к исследованию! Статус исследования был изменен на 'Описано'",info);
        dataoutput();
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void redirectToOsimis(String sid) {
        PrimeFaces.current().executeScript("window.open('http://192.168.1.58:8042/osimis-viewer/app/index.html?study="+sid+"','_blank')");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        this.resultFile = null;
        UploadedFile file = event.getFile();
        if(file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            resultFile = event.getFile();
            addResult();
        }
    }

    public void lockedStudy(){
        resultFile = null;
        //selectedVisibleStudy.setLocked(true);
        //updateStudyInBitServerStudyTable(selectedVisibleStudy);
        PrimeFaces.current().executeScript("PF('uploadResultFile').reset()");
        PrimeFaces.current().ajax().update(":seachform:editorcomponent");
        PrimeFaces.current().ajax().update(":seachform:selectfilename");
        System.out.println("запись обрабатываемся!");
    }
}


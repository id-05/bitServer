package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.*;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "queueremoteBean")
@ViewScoped
public class QueueremoteBean implements UserDao {

    List<BitServerStudy> visibleStudiesList;
    BitServerStudy selectedVisibleStudy;
    List<Usergroup> usergroupList;
    String selectedUserGroup;
    Users currentUser;
    String currentUserId;
    UploadedFile resultFile;
    OrthancRestApi connection;

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
        //usergroupList = getRealBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        dataoutput();
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void dataoutput() {
        //visibleStudiesList = getBitServerStudyOnAnalisis(currentUser.getUgroup());
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void addResult(BitServerStudy selectedVisibleStudy, UploadedFile resultFile) throws IOException {
        selectedVisibleStudy.setStatus(2);
        selectedVisibleStudy.setUserwhodiagnost(currentUserId);
        System.out.println("sid = "+selectedVisibleStudy.getSid());
        selectedVisibleStudy.setDateresult(new Date());
        currentUser.setHasBlockStudy(false);
        updateUser(currentUser);
        if(resultFile!=null){
            Path folder = Paths.get(MainBean.pathToSaveResult);
            String extension = FilenameUtils.getExtension(resultFile.getFileName());
            try (InputStream input = resultFile.getInputStream()) {
                FileOutputStream output = new FileOutputStream(folder.toString()+"/" + selectedVisibleStudy.getSid() + "." + extension);
                byte[] buffer = new byte[input.available()];
                input.read(buffer, 0, buffer.length);
                output.write(buffer, 0, buffer.length);
            }catch (Exception e){
                LogTool.getLogger().warn("Error addResult QueueremoteBean "+e.getMessage());
            }
            selectedVisibleStudy.setTyperesult(true);
            selectedVisibleStudy.setResult(folder.toString()+"\\" + selectedVisibleStudy.getSid() + "." + extension);
        }else{
            selectedVisibleStudy.setTyperesult(false);
        }
        connection.deleteStudyFromOrthanc(selectedVisibleStudy.getAnonimstudyid());
        //updateStudy(selectedVisibleStudy);
        PrimeFaces.current().executeScript("PF('sidebar').hide()");
        showMessage("Информация","Заключение было прикреплено к исследованию! Статус исследования был изменен на 'Описано'",info);
        dataoutput();
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        if(file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            addResult(selectedVisibleStudy, file);
        }else{
            LogTool.getLogger().warn("Error handleFileUpload NetworkSettingsBean");
        }
    }

    public void getStudyToDiag() throws IOException {
        selectedVisibleStudy.setStatus(3);
        selectedVisibleStudy.setDatablock(new Date());
        selectedVisibleStudy.setUserwhoblock(currentUser.getUid().intValue());
        //updateStudy(selectedVisibleStudy);
        currentUser.setHasBlockStudy(true);
        currentUser.setBlockStudy(selectedVisibleStudy.getId().toString());
        updateUser(currentUser);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/remoteusercurrenttask.xhtml");
    }
}


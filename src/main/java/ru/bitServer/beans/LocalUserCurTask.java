package ru.bitServer.beans;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "lucurrenttaskBean")
@ViewScoped
public class LocalUserCurTask implements UserDao {

    BitServerUser currentUser;
    String currentUserId;
    BitServerStudy currentStudy;
    List<BitServerStudy> visibleStudiesList = new ArrayList<>();
    OrthancRestApi connection;
    String resulttext;

    public String getResulttext() {
        return resulttext;
    }

    public void setResulttext(String resulttext) {
        this.resulttext = resulttext;
    }

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
        this.currentUser = currentUser;
    }

    public BitServerStudy getCurrentStudy() {
        return currentStudy;
    }

    public void setCurrentStudy(BitServerStudy currentStudy) {
        this.currentStudy = currentStudy;
    }

    public List<BitServerStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<BitServerStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    @PostConstruct
    public void init() {
        System.out.println("lucurrenttaskBean");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        //currentStudy = getStudyById(currentUser.getBlockStudy());
        visibleStudiesList.clear();
        visibleStudiesList.add(currentStudy);
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        if(file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            addResult(currentStudy, file);
        }else{
            LogTool.getLogger().warn("Возникла ошибка в выборе или типе файла handleFileUpload localuser");

        }
    }

    public void addResult(BitServerStudy selectedVisibleStudy, UploadedFile resultFile) throws IOException {
        selectedVisibleStudy.setStatus(2);
        //selectedVisibleStudy.setUserwhodiagnost(currentUserId);
        selectedVisibleStudy.setDateresult(new Date());
        currentUser.setHasBlockStudy(false);
        //currentUser.setBlockStudy("");
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
                LogTool.getLogger().warn("Error save file addResult LocalUserCurTask "+e.getMessage());
            }
            selectedVisibleStudy.setTypeResult(true);
            selectedVisibleStudy.setResult(folder.toString()+"/" + selectedVisibleStudy.getSid() + "." + extension);
        }else{
            selectedVisibleStudy.setTypeResult(false);
        }
        connection.deleteStudyFromOrthanc(selectedVisibleStudy.getAnonimstudyid());
        //updateStudy(selectedVisibleStudy);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/localuser.xhtml");
    }

    public void saveResult(){

    }
}

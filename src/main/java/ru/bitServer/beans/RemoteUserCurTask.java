package ru.bitServer.beans;

import com.google.gson.JsonArray;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.BitServerGroup;
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
import java.net.HttpURLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "rucurrenttaskBean")
@ViewScoped
public class RemoteUserCurTask implements UserDao {
    BitServerUser currentUser;
    String currentUserId;
    BitServerStudy currentStudy;
    List<BitServerStudy> visibleStudiesList = new ArrayList<>();
    OrthancRestApi connection;
    BitServerGroup bufGroup;

    public BitServerGroup getBufGroup() {
        return bufGroup;
    }

    public void setBufGroup(BitServerGroup bufGroup) {
        this.bufGroup = bufGroup;
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
        System.out.println("rucurrenttaskBean");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        //currentStudy = getStudyById(currentUser.getBlockStudy());
        visibleStudiesList.clear();
        visibleStudiesList.add(currentStudy);
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        //bufGroup = getUsergroupById(currentUser.getUgroup());
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        if(file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            System.out.println("перед add");
            addResult(currentStudy, file);
        }else{
            LogTool.getLogger().warn("Error handleFileUpload rucurrenttaskBean");
        }
    }

    public void addResult(BitServerStudy selectedVisibleStudy, UploadedFile resultFile) throws IOException {
        selectedVisibleStudy.setStatus(2);
        //selectedVisibleStudy.setUserwhodiagnost(currentUserId);
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
                LogTool.getLogger().warn("Error addResult rucurrenttaskBean: "+e.getMessage());
            }
            selectedVisibleStudy.setTypeResult(true);
            selectedVisibleStudy.setResult(folder.toString()+"/" + selectedVisibleStudy.getSid() + "." + extension);
        }else{
            selectedVisibleStudy.setTypeResult(false);
        }
        connection.deleteStudyFromOrthanc(selectedVisibleStudy.getAnonimstudyid());
        //updateStudy(selectedVisibleStudy);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/remoteuser.xhtml");
    }

    public StreamedContent downloadStudy() throws Exception {
        BitServerStudy bufStudy = currentStudy;
        String url="/tools/create-archive";
        JsonArray idArray = new JsonArray();
        idArray.add(bufStudy.getAnonimstudyid());
        HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
        InputStream inputStream = conn.getInputStream();
        return DefaultStreamedContent.builder()
                .name("download"+"-"+bufStudy.getSdescription()+"."+"zip")
                .contentType("application/zip")
                .stream(() -> inputStream)
                .build();
    }

}

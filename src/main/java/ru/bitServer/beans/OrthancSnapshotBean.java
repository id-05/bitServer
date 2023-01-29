package ru.bitServer.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.JsonSettings;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancSettingSnapshot;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "snapshotBean")
@RequestScoped
public class OrthancSnapshotBean implements UserDao {

    ArrayList<OrthancSettingSnapshot> snapShots = new ArrayList<>();
    ArrayList<OrthancSettingSnapshot> selectedSnapShots = new ArrayList<>();
    OrthancSettingSnapshot selectedSnapshot = new OrthancSettingSnapshot();

    public OrthancSettingSnapshot getSelectedSnapshot() {
        return selectedSnapshot;
    }

    public void setSelectedSnapshot(OrthancSettingSnapshot selectedSnapshot) {
        this.selectedSnapshot = selectedSnapshot;
    }

    public ArrayList<OrthancSettingSnapshot> getSelectedSnapShots() {
        return selectedSnapShots;
    }

    public void setSelectedSnapShots(ArrayList<OrthancSettingSnapshot> selectedSnapShots) {
        this.selectedSnapShots = selectedSnapShots;
    }

    public ArrayList<OrthancSettingSnapshot> getSnapShots() {
        return snapShots;
    }

    public void setSnapShots(ArrayList<OrthancSettingSnapshot> snapShots) {
        this.snapShots = snapShots;
    }

    @PostConstruct
    public void init() {
        selectedSnapshot = new OrthancSettingSnapshot();
        snapShots = getAllOrthancSnapshots();
    }

    public StreamedContent downloadSnapshot(OrthancSettingSnapshot snapshot) {
        JsonObject bufJson = selectedSnapshot.getSettingJson();
        JsonSettings bufSettings = new JsonSettings(snapshot.getSettingJson().toString());
        InputStream inputStream = new ByteArrayInputStream(bufJson.toString().replace(",",",\n").getBytes(StandardCharsets.UTF_8));
        return DefaultStreamedContent.builder()
                .name(bufSettings.getOrthancName()+"_"+selectedSnapshot.getDate()+"_orthanc.json")
                .contentType("image/jpg")
                .stream(() -> inputStream)
                .build();
    }

    public void applySnapshot() throws IOException {
        saveJsonSettingtToFile(selectedSnapshot.getSettingJson());
        //loadConfig();
        FacesMessage message = new FacesMessage("Внимание", "Настройки применены!");
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(":form:backupDiaolg");
        PrimeFaces.current().ajax().update(":form:accordion");
    }
}

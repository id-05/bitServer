package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.OrthancSettingSnapshot;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "snapshotBean")
@ViewScoped
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

    public StreamedContent downloadSnapshot() {
        JsonObject bufJson = selectedSnapshot.getSettingJson();
        System.out.println("bufJson.toString "+bufJson.toString());
        //BitServerStudy bufStudy = selectedVisibleStudies.get(selectedVisibleStudies.size()-1);
        //String url="/tools/create-archive";
//        JsonArray jsonArray = new JsonArray();
//        jsonArray.add(bufStudy.getSid());
        //HttpURLConnection conn = connection.makePostConnection(url, jsonArray.toString());
        //InputStream inputStream = new ByteArrayInputStream(bufJson.toString().getBytes(StandardCharsets.UTF_8));
        //byte[] buf = IOUtils.toByteArray(inputStream);
        byte[] buffer = bufJson.toString().getBytes();
        //fileOutputStream.write(buffer, 0, buffer.length);
        InputStream inputStream = new ByteArrayInputStream(buffer);
        return DefaultStreamedContent.builder()
                .name(mainServer.getName()+"_"+FORMAT.format(selectedSnapshot.getDate()).replace(" ","_")+"_orthanc.json")
                .contentType("application/json")
                .stream(() -> inputStream)
                .build();
    }

    public void applySnapshot(){

    }
}

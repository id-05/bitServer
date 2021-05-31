package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "queueBean", eager = false)
@ViewScoped
public class QueueBean implements UserDao {

    public String filtrDate = "today";
    public Date firstdate;
    public Date seconddate;
    public int typeSeach = 0;
    private static List<String> selectedModaliti = new ArrayList<>();
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public ArrayList<OrthancStudy> studiesFromRestApi = new ArrayList<>();
    public List<BitServerStudy> studiesFromTableBitServer = new ArrayList<>();
    private List<BitServerStudy> visibleStudiesList;
    private List<BitServerStudy> selectedVisibleStudies = new ArrayList<>();
    private BitServerStudy selectedVisibleStudy;
    public List<Usergroup> usergroupList;
    public String selectedUserGroup;
    public List<String> usergroupListRuName;
    public Users currentUser;
    public OrthancRestApi connection;

    public List<String> getUsergroupListRuName() {
        usergroupListRuName = new ArrayList<>();
        for(Usergroup bufgroup:usergroupList){
            usergroupListRuName.add(bufgroup.getRuName());
        }
        return usergroupListRuName;
    }

    public void setUsergroupListRuName(List<String> usergroupListRuName) {
        this.usergroupListRuName = usergroupListRuName;
    }

    public String getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(String selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }

    public List<Usergroup> getUsergroupList() {
        return usergroupList;
    }

    public void setUsergroupList(List<Usergroup> usergroupList) {
        this.usergroupList = usergroupList;
    }

    public static List<String> getSelectedModaliti() {
        return selectedModaliti;
    }

    public static void setSelectedModaliti(List<String> selectedModaliti) {
        QueueBean.selectedModaliti = selectedModaliti;
    }

    public List<BitServerStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<BitServerStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    public List<BitServerStudy> getSelectedVisibleStudies() {
        return selectedVisibleStudies;
    }

    public void setSelectedVisibleStudies(List<BitServerStudy> selectedVisibleStudies) {
        this.selectedVisibleStudies = selectedVisibleStudies;
    }

    public BitServerStudy getSelectedVisibleStudy() {
        return selectedVisibleStudy;
    }

    public void setSelectedVisibleStudy(BitServerStudy selectedVisibleStudy) {
        this.selectedVisibleStudy = selectedVisibleStudy;
    }

    public String getFiltrDate() {
        return filtrDate;
    }

    public void setFiltrDate(String filtrDate) {
        this.filtrDate = filtrDate;
    }

    public Date getFirstdate() {
        return firstdate;
    }

    public void setFirstdate(Date firstdate) {
        this.firstdate = firstdate;
    }

    public Date getSeconddate() {
        return seconddate;
    }

    public void setSeconddate(Date seconddate) {
        this.seconddate = seconddate;
    }

    public int getTypeSeach() {
        return typeSeach;
    }

    public void setTypeSeach(int typeSeach) {
        this.typeSeach = typeSeach;
    }

    ////Status
    /// Заблокирован на описание - 3
    /// Описан - 2
    /// Отправлен на описание - 1
    /// Не описан - 0

    @PostConstruct
    public void init() {
        selectedVisibleStudy = new BitServerStudy();
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        System.out.println("QueueBean");
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        firstdate = new Date();
        seconddate = new Date();
        selectedModaliti.clear();
        selectedModaliti.add("CR");
        selectedModaliti.add("CT");
        selectedModaliti.add("MR");
        selectedModaliti.add("NM");
        selectedModaliti.add("PT");
        selectedModaliti.add("US");
        selectedModaliti.add("XA");
        selectedModaliti.add("CR");
        selectedModaliti.add("MG");
        selectedModaliti.add("DX");
        //readStudyFromDB();
        usergroupList = getActiveBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public Boolean firstDateSelect() {
        filtrDate = "targetdate";
        dataoutput();
        return true;
    }

    public Boolean secondDateSelect() {
        filtrDate = "range";
        dataoutput();
        return true;
    }

    public void dataoutput() {
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update(":seachform:send-button");
        selectedVisibleStudies.clear();
        visibleStudiesList = getBitServerStudy(typeSeach,filtrDate,firstdate,seconddate);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void readStudyFromDB() {
        selectedVisibleStudy = new BitServerStudy();
        JsonObject query = new JsonObject();
        query.addProperty("Level", "Studies");
        query.addProperty("CaseSensitive", false);
        query.addProperty("Expand", true);
        query.addProperty("Limit", 0);
        JsonObject queryDetails = new JsonObject();
        String dateStr;
        String dateStartFromBase = "20190101";
        seconddate = new Date();
        dateStr = dateStartFromBase + "-" + format.format(seconddate);
        queryDetails.addProperty("StudyDate", dateStr);
        queryDetails.addProperty("PatientID", "*");
        StringBuilder modalities = new StringBuilder();
        for (String buf : selectedModaliti) {
            modalities.append(buf).append("\\");
        }
        queryDetails.addProperty("Modality", modalities.toString());
        query.add("Query", queryDetails);
        StringBuilder sb = connection.makePostConnectionAndStringBuilder("/tools/find", query.toString());
        assert sb != null;

        boolean existInTable = false;
        studiesFromRestApi = getStudiesFromJson(sb.toString());
        studiesFromTableBitServer = getAllBitServerStudy();
        for(OrthancStudy bS:studiesFromRestApi){
            existInTable = false;
            for(BitServerStudy bBSS:studiesFromTableBitServer){
                if (bS.getOrthancId().equals(bBSS.getSid())) {
                    existInTable = true;
                    break;
                }
            }
            if(!existInTable) {
                BitServerStudy buf = new BitServerStudy(bS.getOrthancId(), bS.getShortId(), bS.getStudyDescription(), bS.getDate(),
                        bS.getModality(), new Date(), bS.getPatientName(), bS.getPatientBirthDate(), bS.getPatientSex(), "","",0);
                addStudyInBitServerStudyTable(buf);
            }
        }

        visibleStudiesList = getBitServerStudy(typeSeach,filtrDate,firstdate,seconddate);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    private ArrayList<OrthancStudy> getStudiesFromJson(String data) {
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(data);
        Iterator<JsonElement> studiesIterator = studies.iterator();
        ArrayList<OrthancStudy> studyList = new ArrayList<>();
        studyList.clear();

        while (studiesIterator.hasNext()) {
            JsonObject studyData = (JsonObject) studiesIterator.next();
            JsonObject parentPatientDetails = null;
            if (studyData.has("PatientMainDicomTags")) {
                parentPatientDetails = studyData.get("PatientMainDicomTags").getAsJsonObject();
            }
            String parentPatientID = studyData.get("ParentPatient").getAsString();
            String studyId = studyData.get("ID").getAsString();
            JsonObject studyDetails = studyData.get("MainDicomTags").getAsJsonObject();
            String patientSex = "N/A";
            String patientName = "N/A";
            String patientId = "N/A";
            String patientDobString = "N/A";
            Date patientDob = null;

            if (parentPatientDetails.has("PatientBirthDate")) {
                patientDobString = parentPatientDetails.get("PatientBirthDate").getAsString();
            }

            if(!patientDobString.equals("")){
                try {
                    patientDob = format.parse(patientDobString);
                } catch (Exception e) {
                    System.out.println("Error to transfer date 1  "+parentPatientDetails);
                }
            }

            if (parentPatientDetails.has("PatientSex")) {
                patientSex = parentPatientDetails.get("PatientSex").getAsString();
            }

            if (parentPatientDetails.has("PatientName")) {
                patientName = parentPatientDetails.get("PatientName").getAsString();
            }

            if (parentPatientDetails.has("PatientID")) {
                patientId = parentPatientDetails.get("PatientID").getAsString();
            }

            String accessionNumber = "N/A";
            if (studyDetails.has("AccessionNumber")) {
                accessionNumber = studyDetails.get("AccessionNumber").getAsString();
            }
            String studyInstanceUid = studyDetails.get("StudyInstanceUID").getAsString();
            String studyDate = null;
            Date studyDateObject = null;
            if (studyDetails.has("StudyDate")) {
                studyDate = studyDetails.get("StudyDate").getAsString();
            }

            try {
                studyDateObject = format.parse("19000101");
                assert studyDate != null;
                studyDateObject = format.parse(studyDate);
            } catch (Exception e) {
                System.out.println("Errot to transfer date 2");
            }

            try {
                studyDateObject = format.parse("19000101");
                assert studyDate != null;
                studyDateObject = format.parse(studyDate);
            } catch (Exception e) {
                System.out.println("Errot to transfer date 3");
            }

            String studyDescription = "N/A";
            if (studyDetails.has("StudyDescription")) {
                studyDescription = studyDetails.get("StudyDescription").getAsString();
            }

            String studyModality = "N/A";
            if (studyData.has("Series")) {
                JsonArray SeriesArray = studyData.get("Series").getAsJsonArray();
                String bufSerie = SeriesArray.get(0).getAsString();
                StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/series/"+bufSerie);
                JsonParser parserJsonSerie = new JsonParser();
                JsonObject serie = (JsonObject) parserJsonSerie.parse(sb.toString());
                JsonObject serieMainDicomTags = null;
                if (serie.has("MainDicomTags")) {
                    serieMainDicomTags = serie.get("MainDicomTags").getAsJsonObject();
                }
                if (serieMainDicomTags.has("Modality")) {
                    studyModality = serieMainDicomTags.get("Modality").getAsString();
                }
            }
            OrthancStudy studyObj = new OrthancStudy(studyDescription, studyModality, studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);
            studyList.add(studyObj);
        }
        return studyList;
    }

    public void sendToAgent(){
        PrimeFaces.current().executeScript("PF('statusDialog').show()");
        JsonObject query = new JsonObject();
        JsonObject queryDetails = new JsonObject();
        queryDetails.addProperty("PatientName", "ANONIM");
        queryDetails.addProperty("0010-1001", "ANONIM");
        query.add("Replace", queryDetails);
        JsonArray queryArray = new JsonArray();
        queryArray.add("StudyDescription");
        queryArray.add("SeriesDescription");
        query.add("Keep",queryArray);
        query.addProperty("KeepPrivateTags",true);
        query.addProperty("DicomVersion","2017c");
        int i = 0;
        for(BitServerStudy bufStudy:selectedVisibleStudies){
            if((bufStudy.getStatus()!=1)&(bufStudy.getStatus()!=2)) {
                StringBuilder sb = connection.makePostConnectionAndStringBuilder("/studies/" + bufStudy.getSid() + "/anonymize", query.toString());
                JsonParser parserJson = new JsonParser();
                JsonObject studies = (JsonObject) parserJson.parse(sb.toString());
                bufStudy.setAnonimstudyid(studies.get("ID").getAsString());
                bufStudy.setStatus(1);
                bufStudy.setDatesent(new Date());
                bufStudy.setUsergroupwhosees(selectedUserGroup);
                bufStudy.setUserwhosent(currentUser.getUid().toString());
                updateStudyInBitServerStudyTable(bufStudy);
                i++;
            }else{
                showMessage("Внимание","Исследование "+bufStudy.getShortid()+" "+bufStudy.getPatientname()+" уже было отправлено ранее!",info);
            }
        }
        showMessage("Внимание","Всего отправлено: " + i,info);
        //selectedVisibleStudies = null;
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().executeScript("window.location.reload();");
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        PrimeFaces.current().ajax().update(":seachform:send-button");
    }

    public void addAnamnes(){
        updateStudyInBitServerStudyTable(selectedVisibleStudy);
    }

    public boolean hasSelectedStudy() {
        return this.selectedVisibleStudies != null && !this.selectedVisibleStudies.isEmpty();
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void redirectToOsimis(String sid) {
        PrimeFaces.current().executeScript("window.open('http://"+osimisAddress+"/osimis-viewer/app/index.html?study="+sid+"','_blank')");
    }

    public StreamedContent getResult(BitServerStudy study) throws IOException {
        if(study.isTyperesult()){
            Path path = Paths.get(study.getResult());
            String extension = FilenameUtils.getExtension(study.getResult());
            InputStream inputStream = new FileInputStream(path.toString());
            return DefaultStreamedContent.builder()
                    .name(study.getPatientname()+"-"+study.getSdescription()+"."+extension)
                    .contentType("image/jpg")
                    .stream(() -> inputStream)
                    .build();
        }else{
            PrimeFaces.current().executeScript("PF('sidebar').show()");
            return null;
        }
    }

    public StreamedContent getResult2(BitServerStudy study) throws Exception {
        String url="/tools/create-archive";
        JsonArray idArray = new JsonArray();
        idArray.add(study.getSid());
        HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
        InputStream inputStream = conn.getInputStream();
        //File tempFile = File. createTempFile("temp", ".zip");
        File tempFile = new File("D:/results/"+study.getPatientname()+"-"+study.getSdescription()+".zip");

        FileOutputStream fos = new FileOutputStream(tempFile);
        int bytesRead = -1;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        fos.close();
        inputStream.close();
        conn.disconnect();
//        conn.disconnect();
        return DefaultStreamedContent.builder()
                .name(study.getPatientname()+"-"+study.getSdescription()+"."+"zip")
                .contentType("application/zip")
                .stream(() -> {
                    try {
                        return new FileInputStream(tempFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .build();
    }

    public void downloadStudy() throws Exception {

        for(BitServerStudy bufStudy:selectedVisibleStudies){
            File f = new File("D:/results/"+bufStudy.getPatientname()+"-"+bufStudy.getSdescription()+".zip");
            System.out.println("скачать исследование "+bufStudy.getSid());
            String url="/tools/create-archive";
            JsonArray idArray = new JsonArray();
            idArray.add(bufStudy.getSid());
            HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(f);
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            is.close();
            conn.disconnect();
        }
        selectedVisibleStudies.clear();
    }

    public void comebackStudy() throws IOException {
        for(BitServerStudy bufStudy:selectedVisibleStudies){
            if(!bufStudy.getUsergroupwhosees().equals("")){
                System.out.println("in circl "+bufStudy.getPatientname());
                bufStudy.setUsergroupwhosees("");
                bufStudy.setStatus(0);
                updateStudyInBitServerStudyTable(bufStudy);
                connection.deleteStudyFromOrthanc(bufStudy);
            }
        }
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().executeScript("window.location.reload();");
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        PrimeFaces.current().ajax().update(":seachform:send-button");
    }
}

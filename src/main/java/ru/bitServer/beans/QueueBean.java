package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.*;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
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
    //private List<String> selectedModaliti = new ArrayList<>();
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
    public OrthancSettings orthancSettings;
    public int uploadCount;
    public String freespace;
    List<DicomModaliti> modalities;
    List<DicomModaliti> selectedModalities;
    DicomModaliti selectedModaliti;

    public DicomModaliti getSelectedModaliti() {
        return selectedModaliti;
    }

    public void setSelectedModaliti(DicomModaliti selectedModaliti) {
        this.selectedModaliti = selectedModaliti;
    }

    public List<DicomModaliti> getSelectedModalities() {
        return selectedModalities;
    }

    public void setSelectedModalities(List<DicomModaliti> selectedModalities) {
        this.selectedModalities = selectedModalities;
    }

    public List<DicomModaliti> getModalities() {
        return modalities;
    }

    public void setModalities(List<DicomModaliti> modalities) {
        this.modalities = modalities;
    }

    public String getFreespace() {
        return freespace;
    }

    public void setFreespace(String freespace) {
        this.freespace = freespace;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

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

//    public List<String> getSelectedModaliti() {
//        return selectedModaliti;
//    }
//
//    public void setSelectedModaliti(List<String> selectedModaliti) {
//        this.selectedModaliti = selectedModaliti;
//    }

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
        selectedModaliti = new DicomModaliti("", "", "", "", "");
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        System.out.println("QueueBean");

        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();
        for(DicomModaliti bufModaliti:modalities){
            System.out.println(bufModaliti.getDicomname()+"  "+bufModaliti.getDicomtitle());
        }

        firstdate = new Date();
        seconddate = new Date();
        usergroupList = getRealBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();
        String referrer = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        System.out.println("refererr = "+referrer);
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
        visibleStudiesList = convertIdGroupToRuName(visibleStudiesList);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public List<BitServerStudy> convertIdGroupToRuName(List<BitServerStudy> sourceList){
        List<Usergroup> bufUserGroupList = getUsergroupList();
        for(int i = 0; i<sourceList.size(); i++){
            if(sourceList.get(i).getUsergroupwhosees()!=null) {
                for (Usergroup bufGroup : bufUserGroupList) {
                    if (sourceList.get(i).getUsergroupwhosees().equals(bufGroup.getId().toString())) {
                        sourceList.get(i).setUsergroupwhosees(bufGroup.getRuName());
                    }
                }
            }
        }
        return sourceList;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        HttpURLConnection conn = connection.sendDicom("/instances", file.getContent());
        conn.disconnect();
        uploadCount++;
        PrimeFaces.current().ajax().update(":addDICOM");
    }

    public void readStudyFromDB() {
        selectedVisibleStudy = new BitServerStudy();
        JsonObject query = new JsonObject();
        query.addProperty("Level", "Studies");
        query.addProperty("CaseSensitive", false);
        query.addProperty("Expand", true);
        query.addProperty("Limit", 0);
        JsonObject queryDetails = new JsonObject();

        String bufStr = getBitServerResource("syncdate").getRvalue();
        String dateStartFromBase = bufStr.replaceAll("-","");

        seconddate = new Date();
        String dateStr = dateStartFromBase + "-" + format.format(seconddate);
        queryDetails.addProperty("StudyDate", dateStr);
        queryDetails.addProperty("PatientID", "*");
        StringBuilder modalities = new StringBuilder();

        List<BitServerModality> modalityFromBase = getAllBitServerModality();
        for(BitServerModality bufModality:modalityFromBase){
            modalities.append(bufModality.getName()).append("\\");
        }

        queryDetails.addProperty("Modality", modalities.toString());
        query.add("Query", queryDetails);
        StringBuilder sb = connection.makePostConnectionAndStringBuilder("/tools/find", query.toString());
        assert sb != null;

        boolean existInTable = false;
        studiesFromRestApi = getStudiesFromJson(sb.toString());
        studiesFromTableBitServer = getAllBitServerStudy();
        for(OrthancStudy bufStudy:studiesFromRestApi){
            existInTable = false;
            for(BitServerStudy bBSS:studiesFromTableBitServer){
                if (bufStudy.getOrthancId().equals(bBSS.getSid())) {
                    existInTable = true;
                    break;
                }
            }
            if(!existInTable) {
                BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
                        bufStudy.getInstitutionName(), bufStudy.getDate(),
                        bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), "","",0);
                addStudy(buf);
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

        while (studiesIterator.hasNext()) {
            JsonObject studyData = (JsonObject) studiesIterator.next();
            System.out.println("studyData = "+studyData);
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

            assert parentPatientDetails != null;
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

            String studyInstitutionName = "N/A";
            if (studyDetails.has("InstitutionName")) {
                studyInstitutionName = studyDetails.get("InstitutionName").getAsString();
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
            OrthancStudy studyObj = new OrthancStudy(studyInstitutionName, studyDescription, studyModality, studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);
            studyList.add(studyObj);
        }
        return studyList;
    }

    public void sendToAgent(){
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
                bufStudy.setUsergroupwhosees(getUserGroupId(selectedUserGroup));
                bufStudy.setUserwhosent(currentUser.getUid().toString());
                updateStudy(bufStudy);
                i++;
            }else{
                showMessage("Внимание","Исследование "+bufStudy.getShortid()+" "+bufStudy.getPatientname()+" имеет недопустимый для этого действия статус!",info);
            }
        }

        showMessage("Внимание","Всего отправлено: " + i,info);
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        PrimeFaces.current().ajax().update(":seachform:send-button");
    }

    public String getUserGroupId(String groupName){
        String buf = "";
        List<Usergroup> bufUserGroupList = getUsergroupList();
        for(Usergroup bufGroup:bufUserGroupList){
            if(groupName.equals(bufGroup.getRuName())){
                buf = bufGroup.getId().toString();
                break;
            }
        }
        return buf;
    }

    public void addAnamnes(){
        updateStudy(selectedVisibleStudy);
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
        String buf;
        if(mainServer.getHttpmode().equals("true")){
            buf = "https";
        }else{
            buf = "http";
        }
        String referrer = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String bufStr = referrer.substring(j+3,referrer.length()-4-i);
        PrimeFaces.current().executeScript("window.open('"+buf+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+bufStr+"viewer/osimis-viewer/app/index.html?study="+sid+"','_blank')");
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
            //PrimeFaces.current().executeScript("PF('sidebar').show()");
            return null;
        }
    }

    public StreamedContent getResult2(BitServerStudy study) throws Exception {
        String url="/tools/create-archive";
        JsonArray idArray = new JsonArray();
        idArray.add(study.getSid());
        HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
        InputStream inputStream = conn.getInputStream();
        return DefaultStreamedContent.builder()
                .name(study.getPatientname()+"-"+study.getSdescription()+"."+"zip")
                .contentType("application/zip")
                .stream(() -> inputStream)
                .build();
    }

    public StreamedContent downloadStudy() throws Exception {
        BitServerStudy bufStudy = selectedVisibleStudies.get(selectedVisibleStudies.size()-1);
            String url="/tools/create-archive";
            JsonArray idArray = new JsonArray();
            idArray.add(bufStudy.getSid());
            HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
            InputStream inputStream = conn.getInputStream();
            return DefaultStreamedContent.builder()
                    .name(bufStudy.getPatientname()+"-"+bufStudy.getSdescription()+"."+"zip")
                    .contentType("application/zip")
                    .stream(() -> inputStream)
                    .build();
    }

    public void comebackStudy() throws IOException {
        for(BitServerStudy bufStudy:selectedVisibleStudies){
            if(!bufStudy.getUsergroupwhosees().equals("")){
                bufStudy.setUsergroupwhosees("");
                bufStudy.setStatus(0);
                updateStudy(bufStudy);
                connection.deleteStudyFromOrthanc(bufStudy);
                Users bufUser = getUserById(String.valueOf(bufStudy.getUserwhoblock()));
                bufUser.setHasBlockStudy(false);
                bufUser.setBlockStudy("0");
                updateUser(bufUser);
            }
        }
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        PrimeFaces.current().ajax().update(":seachform:send-button");
    }

    public void markAsHasResult() throws IOException {
        for(BitServerStudy bufStudy:selectedVisibleStudies){
            bufStudy.setUsergroupwhosees("");
            bufStudy.setStatus(2);
            updateStudy(bufStudy);
        }
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        PrimeFaces.current().ajax().update(":seachform:send-button");
    }

    public void getStudyToDiag() throws IOException {
        selectedVisibleStudy.setStatus(3);
        selectedVisibleStudy.setDatablock(new Date());
        selectedVisibleStudy.setUserwhoblock(currentUser.getUid().intValue());
        updateStudy(selectedVisibleStudy);
        currentUser.setHasBlockStudy(true);
        currentUser.setBlockStudy(selectedVisibleStudy.getId().toString());
        updateUser(currentUser);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/bitServer/views/localusercurrenttask.xhtml");
    }

    public void chooseAETitle(){
        String bufStr = "";
        for(BitServerStudy bufstudy:selectedVisibleStudies){
            bufStr = bufstudy.getSid();
            if(selectedVisibleStudies.size()>1){
                bufStr =  bufstudy.getSid() +",";
            }else{
                bufStr =  bufstudy.getSid();
            }
        }

        System.out.println(selectedModaliti.getDicomname());
        System.out.println("/modalities/" + selectedModaliti.getDicomname() + "/store"+"/"+bufStr);
        StringBuilder sb = connection.makePostConnectionAndStringBuilder("/modalities/" + selectedModaliti.getDicomname() + "/store", bufStr);
        System.out.println("answer = "+sb);
    }
}

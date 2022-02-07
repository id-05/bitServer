package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
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
    public int typeSeach = 5;
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
    OrthancRestApi connection;
    OrthancSettings orthancSettings;
    public int uploadCount;
    public String freespace;
    List<DicomModaliti> modalities;
    List<DicomModaliti> selectedModalities;
    DicomModaliti selectedModaliti;
    boolean datepickerVisible1;
    boolean datepickerVisible2;
    List<String> selectedModalitiName = new ArrayList<>();
    String colStatus;
    String colDateBirth;
    String colDate;
    String colDescription;
    String colModality;
    String colWhereSend;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();

    public String getColStatus() {
        return colStatus;
    }

    public void setColStatus(String colStatus) {
        this.colStatus = colStatus;
    }

    public String getColDateBirth() {
        return colDateBirth;
    }

    public void setColDateBirth(String colDateBirth) {
        this.colDateBirth = colDateBirth;
    }

    public String getColDate() {
        return colDate;
    }

    public void setColDate(String colDate) {
        this.colDate = colDate;
    }

    public String getColDescription() {
        return colDescription;
    }

    public void setColDescription(String colDescription) {
        this.colDescription = colDescription;
    }

    public String getColModality() {
        return colModality;
    }

    public void setColModality(String colModality) {
        this.colModality = colModality;
    }

    public String getColWhereSend() {
        return colWhereSend;
    }

    public void setColWhereSend(String colWhereSend) {
        this.colWhereSend = colWhereSend;
    }

    public List<String> getSelectedModalitiName() {
        return selectedModalitiName;
    }

    public void setSelectedModalitiName(List<String> selectedModalitiName) {
        this.selectedModalitiName = selectedModalitiName;
    }

    public boolean isDatepickerVisible1() {
        return datepickerVisible1;
    }

    public void setDatepickerVisible1(boolean datepickerVisible1) {
        this.datepickerVisible1 = datepickerVisible1;
    }

    public boolean isDatepickerVisible2() {
        return datepickerVisible2;
    }

    public void setDatepickerVisible2(boolean datepickerVisible2) {
        this.datepickerVisible2 = datepickerVisible2;
    }

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

        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();

        firstdate = new Date();
        seconddate = new Date();
        usergroupList = getRealBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();

        try {
            BitServerResources bufResource = getBitServerResource("updateafteropen");
            if (bufResource.getRvalue().equals("true")) {
                readStudyFromDB();
            }
        }catch (Exception e){
            System.out.println("error update after open "+e.getMessage());
        }

        selectedModalitiName.clear();
        selectedModalitiName.add("CR");
        selectedModalitiName.add("CT");
        selectedModalitiName.add("MR");
        selectedModalitiName.add("NM");
        selectedModalitiName.add("PT");
        selectedModalitiName.add("US");
        selectedModalitiName.add("XA");
        selectedModalitiName.add("CR");
        selectedModalitiName.add("MG");
        selectedModalitiName.add("DX");


        bitServerResourcesList = getAllBitServerResource();
        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){
                case "colstatus": colStatus = buf.getRvalue();
                    break;
                case "colDateBirth": colDateBirth = buf.getRvalue();
                    break;
                case "colDate": colDate = buf.getRvalue();
                    break;
                case "colDescription": colDescription = buf.getRvalue();
                    break;
                case "colModality": colModality = buf.getRvalue();
                    break;
                case "colWhereSend": colWhereSend = buf.getRvalue();
                    break;
            }
        }

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
        StringBuilder bufStr = new StringBuilder();
        for(String buf:selectedModalitiName) {
            if (bufStr.length()<1) {
                bufStr = new StringBuilder("" + buf + "");
            } else{
                bufStr.append(",").append(buf);
            }
        }

        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows();");
        PrimeFaces.current().ajax().update(":seachform:send-button");
        selectedVisibleStudies.clear();
        visibleStudiesList = getBitServerStudy(typeSeach,filtrDate,firstdate,seconddate, bufStr.toString());
        visibleStudiesList = convertIdGroupToRuName(visibleStudiesList);

        if(filtrDate.equals("targetdate")){
            datepickerVisible1 = true;
            datepickerVisible2 = false;
        }else{
            if(filtrDate.equals("range")){
                datepickerVisible1 = true;
                datepickerVisible2 = true;
            }else{
                datepickerVisible1 = false;
                datepickerVisible2 = false;
            }
        }
        //filteredRows = visibleStudiesList;

        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent component = view.findComponent(":seachform:dt-studys");
        DataTable dt = (DataTable) component.findComponent(":seachform:dt-studys");
        dt.resetValue();

        PrimeFaces.current().ajax().update(":seachform:datecard",":seachform:dt-studys");
        //PrimeFaces.current().ajax().update(":seachform:datecard");
    }

    public List<BitServerStudy> convertIdGroupToRuName(List<BitServerStudy> sourceList){
        List<Usergroup> bufUserGroupList = getUsergroupList();
        for (BitServerStudy bitServerStudy : sourceList) {
            if (bitServerStudy.getUsergroupwhosees() != null) {
                for (Usergroup bufGroup : bufUserGroupList) {
                    if (bitServerStudy.getUsergroupwhosees().equals(bufGroup.getId().toString())) {
                        bitServerStudy.setUsergroupwhosees(bufGroup.getRuName());
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

        boolean existInTable;
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

        String bufStrMod = "";
        for(String buf:selectedModalitiName){
            if(!bufStrMod.equals("")){
                bufStrMod = "'" + buf + "'";
            }else {
                bufStrMod = bufStr + ",'" + buf + "'";
            }
        }

        visibleStudiesList = getBitServerStudy(typeSeach,filtrDate,firstdate,seconddate,bufStrMod);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    private ArrayList<OrthancStudy> getStudiesFromJson(String data) {
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(data);
        Iterator<JsonElement> studiesIterator = studies.iterator();
        ArrayList<OrthancStudy> studyList = new ArrayList<>();

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
                assert serieMainDicomTags != null;
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
        String HttpOrHttps;
        if(mainServer.getHttpmode().equals("true")){
            HttpOrHttps = "https";
        }else{
            HttpOrHttps = "http";
        }
        String referrer = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        int i = referrer.indexOf("/bitServer/");
        int j = referrer.indexOf("://");
        String address = referrer.substring(j+3,i);
        if(address.contains(":")){
            BitServerResources bufResources = getBitServerResource("port");
            String port = bufResources.getRvalue();
            int k = address.indexOf(":");
            String addressCutPort = address.substring(0,k);
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+addressCutPort+":"+port+"/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }else{
            PrimeFaces.current().executeScript("window.open('"+HttpOrHttps+"://"+mainServer.getLogin()+":"+mainServer.getPassword()+"@"+address+"/viewer/osimis-viewer/app/index.html?study="+sid+"','_blank')");
        }
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

    public void chooseAETitle()  {
        JsonArray ids=new JsonArray();
        for(BitServerStudy bufstudy:selectedVisibleStudies){
            ids.add(bufstudy.getSid());
        }
        StringBuilder sb = null;

        try {
            sb = connection.makePostConnectionAndStringBuilderWithIOE("/modalities/" + selectedModaliti.getDicomname() + "/store", ids.toString());
        } catch (IOException e) {
            showMessage("Сообщение:","Возникла ошибка при отправке! "+e.getMessage(),error);
        }

        PrimeFaces.current().executeScript("PF('statusDialog').hide()");

        if(sb!=null){
            showMessage("Сообщение:","Данные успешно отправлены!",info);
        }else{
            showMessage("Сообщение:","Возникла ошибка при отправке!",error);
        }

    }
}

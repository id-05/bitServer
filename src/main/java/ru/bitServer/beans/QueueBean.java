package ru.bitServer.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.text.Transliterator;
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
import ru.bitServer.util.LogTool;
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
import java.util.List;
import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "queueBean")
@ViewScoped
public class QueueBean implements UserDao, DataAction {

    String filtrDate = "all";
    Date firstdate;
    Date seconddate;
    int typeSeach = 5;
    final SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy.MM.dd");
    List<BitServerStudy> studiesFromTableBitServer = new ArrayList<>();
    List<BitServerStudy> visibleStudiesList;
    List<BitServerStudy> selectedVisibleStudies = new ArrayList<>();
    BitServerStudy selectedVisibleStudy;
    List<Usergroup> usergroupList;
    String selectedUserGroup;
    List<String> usergroupListRuName;
    Users currentUser;
    OrthancRestApi connection;
    OrthancSettings orthancSettings;
    int uploadCount;
    String freespace;
    List<DicomModaliti> modalities;
    List<DicomModaliti> selectedModalities;
    DicomModaliti selectedModaliti;
    boolean datepickerVisible1;
    boolean datepickerVisible2;
    List<String> selectedModalitiName = new ArrayList<>();
    String colStatus;
    String colPreview;
    String colDateBirth;
    String colDate;
    String colDescription;
    String colModality;
    String colWhereSend;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    private int number;
    int progress;
    List<String> modalityName = new ArrayList<>();

    public List<String> getModalityName() {
        return modalityName;
    }

    public void setModalityName(List<String> modalityName) {
        this.modalityName = modalityName;
    }

    public String getNumber() {
        return number+"%";
    }

    public String getColStatus() {
        return colStatus;
    }

    public String getColPreview() {
        return colPreview;
    }

    public void setColPreview(String colPreview) {
        this.colPreview = colPreview;
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

    public static final String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
    public static final String LATIN_TO_CYRILLIC = "Latin-Cyrillic";

    ////Status
    /// ???????????????????????? ???? ???????????????? - 3
    /// ???????????? - 2
    /// ?????????????????? ???? ???????????????? - 1
    /// ???? ???????????? - 0

    @PostConstruct
    private void init() {
        System.out.println("queueBean");
        for(BitServerModality bufModality:getAllBitServerModality()){
            modalityName.add(bufModality.getName());
        }
        selectedModalitiName = modalityName;
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

        bitServerResourcesList = getAllBitServerResource();
        for(BitServerResources buf: bitServerResourcesList){
            switch (buf.getRname()){
                case "colstatus": colStatus = buf.getRvalue();
                    break;
                case "colpreview": colPreview = buf.getRvalue();
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
        dataoutput();
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
        System.out.println(selectedModalitiName);
        StringBuilder bufStr = getAllBitServerModality("name");
        resetViewTable();
        selectedVisibleStudies.clear();
        visibleStudiesList = getBitServerStudy(typeSeach,filtrDate,firstdate,seconddate, bufStr.toString());

        visibleStudiesList.removeIf(bufStudy -> !selectedModalitiName.contains(bufStudy.getModality()));

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
        PrimeFaces.current().executeScript("PF('visibleStudy').filter()");
        sortListener();
    }

    public void sortListener(){
        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent component = view.findComponent(":seachform:dt-studys");
        DataTable dt = (DataTable) component.findComponent(":seachform:dt-studys");
        dt.resetValue();
        PrimeFaces.current().ajax().update(":seachform:datecard",":seachform:dt-studys");
        PrimeFaces.current().executeScript("PF('visibleStudy').filter()");
    }

    public boolean filterByCustom(Object value, Object filter, Locale locale) {
        boolean result;
        if( isValid(value.toString().substring(0,1).toUpperCase()) == isValid(filter.toString().substring(0,1).toUpperCase())){
            result = value.toString().contains(filter.toString().toUpperCase());
        }else{
            if(isValid(value.toString().substring(0,1).toUpperCase())){
                Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
                result = true;
                for (int i = 0; i < filter.toString().length(); i++){
                    if(filter.toString().length()<=value.toString().length()) {
                        char c = filter.toString().toUpperCase().charAt(i);
                        String bufFilter = toLatinTrans.transliterate(String.valueOf(c));
                        String bufValue = String.valueOf(value.toString().toUpperCase().charAt(i));
                        if(bufFilter.equals("%")){
                            continue;
                        }
                        result = result & bufFilter.equals(bufValue);
                    }
                }
            }else{
                Transliterator toLatinTrans = Transliterator.getInstance(LATIN_TO_CYRILLIC);
                result = true;
                for (int i = 0; i < filter.toString().length(); i++){
                    if(filter.toString().length()<=value.toString().length()) {
                        char c = filter.toString().toUpperCase().charAt(i);
                        String bufFilter = toLatinTrans.transliterate(String.valueOf(c));
                        String bufValue = String.valueOf(value.toString().toUpperCase().charAt(i));
                        if(bufFilter.equals("%")){
                            continue;
                        }
                        result = result & bufFilter.equals(bufValue);
                    }
                }
            }
        }
        return result;
    }

    public static boolean isValid(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.UnicodeBlock.of(s.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC) ||
                    s.charAt(i) == '-') {
                return false;
            }
        }
        return true;
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
        String newID = connection.sendDicom("/instances", file.getContent());
        StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/studies/"+newID);
        JsonObject bufJson = (JsonObject) new JsonParser().parse(sb.toString());
        OrthancStudy bufStudy = connection.parseStudy(bufJson);
        studiesFromTableBitServer = getAllBitServerStudy();
        boolean existInTable = false;
        for (BitServerStudy bBSS : studiesFromTableBitServer) {
            if (bufStudy.getOrthancId().equals(bBSS.getSid())) {
                existInTable = true;
                break;
            }
        }
        if (!existInTable) {
            BitServerStudy buf = new BitServerStudy(bufStudy.getOrthancId(), bufStudy.getShortId(), bufStudy.getStudyDescription(),
                    bufStudy.getInstitutionName(), bufStudy.getDate(),
                    bufStudy.getModality(), new Date(), bufStudy.getPatientName(), bufStudy.getPatientBirthDate(), bufStudy.getPatientSex(), "", "", 0);
            addStudy(buf);
        }
        uploadCount++;
        PrimeFaces.current().ajax().update(":addDICOM");
        readStudyFromDB();
    }

    public void readStudyFromDB() {
        PrimeFaces.current().ajax().update(":seachform:updateBut");
        int i = syncDataBase(connection);
        showMessage("??????????????????", "?????????????????????????? ??????????????????! ?????????? ??????????????????: " + i, info);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        dataoutput();
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
                showMessage("????????????????","???????????????????????? "+bufStudy.getShortid()+" "+bufStudy.getPatientname()+" ?????????? ???????????????????????? ?????? ?????????? ???????????????? ????????????!",info);
            }
        }
        showMessage("????????????????","?????????? ????????????????????: " + i,info);
        selectedVisibleStudies.clear();
        dataoutput();
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
        resetViewTable();
    }

    public void resetViewTable(){
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
            return null;
        }
    }

    public StreamedContent downloadStudy() throws Exception {
        BitServerStudy bufStudy = selectedVisibleStudies.get(selectedVisibleStudies.size()-1);
            String url="/tools/create-archive";
            JsonArray idArray = new JsonArray();
            idArray.add(bufStudy.getSid());
            HttpURLConnection conn = connection.makePostConnection(url, idArray.toString());
            InputStream inputStream = conn.getInputStream();
            return DefaultStreamedContent.builder()
                    .name(bufStudy.getPatientname()+"-"+bufStudy.getSdescription()+"_"+FORMAT2.format(bufStudy.getSdate())+"."+"zip")
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
                connection.deleteStudyFromOrthanc(bufStudy.getAnonimstudyid());
                Users bufUser = getUserById(String.valueOf(bufStudy.getUserwhoblock()));
                bufUser.setHasBlockStudy(false);
                bufUser.setBlockStudy("0");
                updateUser(bufUser);
            }
        }
        selectedVisibleStudies.clear();
        dataoutput();
        resetViewTable();
    }

    public void markAsHasResult() {
        for(BitServerStudy bufStudy:selectedVisibleStudies){
            bufStudy.setUsergroupwhosees("");
            bufStudy.setStatus(2);
            updateStudy(bufStudy);
        }
        selectedVisibleStudies.clear();
        dataoutput();
        resetViewTable();
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

    public void DelStudy() throws IOException {
        deleteStudy(selectedVisibleStudy);
        connection.deleteStudyFromOrthanc(selectedVisibleStudy.getSid());
        visibleStudiesList.remove(selectedVisibleStudy);
        selectedVisibleStudy = new BitServerStudy();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("???????????????????????? ??????????????!"));
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        dataoutput();
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
            showMessage("??????????????????:","???????????????? ???????????? ?????? ????????????????! "+e.getMessage(),error);
        }

        PrimeFaces.current().executeScript("PF('statusDialog').hide()");

        if(sb!=null){
            showMessage("??????????????????:","???????????? ?????????????? ????????????????????!",info);
        }else{
            showMessage("??????????????????:","???????????????? ???????????? ?????? ????????????????!",error);
        }

    }
}

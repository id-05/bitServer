package ru.bitServer.beans;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.DataAction;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.JsonSettings;
import ru.bitServer.dicom.OrthancWebUser;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.OrthancSettingSnapshot;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import static ru.bitServer.beans.AuthoriseBean.showMessage;
import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "newSetOrth")
@ViewScoped
public class SettingsNewOrthanc implements UserDao, DataAction{

    BitServerUser currentUser;
    ArrayList<OrthancSettingSnapshot> snapShots = new ArrayList<>();
    ArrayList<OrthancSettingSnapshot> selectedSnapShots = new ArrayList<>();
    OrthancSettingSnapshot selectedSnapshot = new OrthancSettingSnapshot();
    DicomModaliti selectedDicomModality;
    OrthancWebUser selectedUser;
    JsonSettings json;
    OrthancRestApi connection;


    @PostConstruct
    public void init() {
        System.out.println("newSetOrth");
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        selectedSnapshot = new OrthancSettingSnapshot();
        snapShots = getAllOrthancSnapshots();
        try {
            selectedUser = new OrthancWebUser("", "");
            selectedDicomModality = new DicomModaliti("", "", "", "", "");
            loadConfig();
        }catch (Exception e){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            LogTool.getLogger().error("Error during init() SettingsOrthancBean "+e.getMessage());
            try{
                ec.redirect(ec.getRequestContextPath()
                        + "/views/errorpage.xhtml"+"?"+e.getMessage());
            }catch (Exception e2){
                LogTool.getLogger().error(this.getClass().getSimpleName() + ": Error during init() SettingsOrthancBean "+e2.getMessage());
            }
        }
    }

    public void echoTestClick(){
        JsonObject query = new JsonObject();
        query.addProperty("AET", selectedDicomModality.getDicomTitle());
        query.addProperty("CheckFind", false);
        query.addProperty("Host", selectedDicomModality.getIp());
        query.addProperty("Manufacturer", selectedDicomModality.getDicomProperty());
        query.addProperty("Port", selectedDicomModality.getDicomPort());
        query.addProperty("Timeout", 100);
        StringBuilder sb;
        sb = connection.makePostConnectionAndStringBuilder("/tools/dicom-echo", query.toString());
        if(sb!=null) {
            showMessage("Сообщение:","Echo-тест прошёл успешно!",info);
        }else{
            showMessage("Сообщение:","Устройство не ответило!",error);
        }
        PrimeFaces.current().executeScript("PF('statusDialog').hide()");
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
        loadConfig();
        showMessage("Внимание", "Настройки применены!",FacesMessage.SEVERITY_INFO);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" apply snapshot settings! Snapshot date: "+selectedSnapshot.getDate());
        PrimeFaces.current().ajax().update(":form:accordion:dt-users");
        PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
    }

    public void delSnapshot(){
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" dell snapshot settings! Snapshot date: "+selectedSnapshot.getDate());
        deleteFromBitServerTable(Long.parseLong(selectedSnapshot.getId()));
        snapShots = getAllOrthancSnapshots();
        PrimeFaces.current().ajax().update(":form:dt-snap:");
    }

    public void  resetServer(){
        showMessage("Внимание","Сервис Orthanc, будет перезагружен!",FacesMessage.SEVERITY_INFO);
        connection.makePostConnectionAndStringBuilder("/tools/reset","" );
    }

    public void restoreBackup(){
        init();
        showMessage("Сообщение","Все изменения отменены!", info);
    }


    public void loadConfig() {
        try{
            json = new JsonSettings(getJsonFromFile().toString());
            users = json.getUsers();
            dicomNode = json.getDicomNode();
            ServerName = json.getOrthancName();
            storageDirectory = json.getStorageDirectory();
            indexDirectory = json.getIndexDirectory();
            StorageCompression = json.isStorageCompression();
            MaximumStorageSize = json.getMaximumStorageSize();
            MaximumPatientCount = json.getMaximumPatientCount();
            ConcurrentJobs = json.getConcurrentJobs();
            HttpServerEnabled = json.isHttpServerEnabled();
            HttpPort = json.getHttpPort();
            HttpDescribeErrors = json.isHttpDescribeErrors();
            HttpCompressionEnabled = json.isHttpCompressionEnabled();
            DicomServerEnabled = json.isDicomServerEnabled();
            DicomAet = json.getDicomAet();
            DicomCheckCalledAet = json.isDicomCheckCalledAet();
            DicomPort = json.getDicomPort();
            DefaultEncoding = json.getDefaultEncoding();
            DeflatedTransferSyntaxAccepted = json.isDeflatedTransferSyntaxAccepted();
            JpegTransferSyntaxAccepted = json.isJpegTransferSyntaxAccepted();
            Jpeg2000TransferSyntaxAccepted = json.isJpeg2000TransferSyntaxAccepted();
            JpegLosslessTransferSyntaxAccepted = json.isJpegLosslessTransferSyntaxAccepted();
            JpipTransferSyntaxAccepted = json.isJpipTransferSyntaxAccepted();
            Mpeg2TransferSyntaxAccepted = json.isMpeg2TransferSyntaxAccepted();
            RleTransferSyntaxAccepted = json.isRleTransferSyntaxAccepted();
            UnknownSopClassAccepted = json.isUnknownSopClassAccepted();
            DicomScpTimeout = json.getDicomScpTimeout();
            RemoteAccessAllowed = json.isRemoteAccessAllowed();
            SslEnabled = json.isSslEnabled();
            SslCertificate = json.getSslCertificate();
            AuthenticationEnabled = json.isAuthenticationEnabled();
            DicomScuTimeout = json.getDicomScuTimeout();
            HttpProxy = json.getHttpProxy();
            HttpTimeout = json.getHttpTimeout();
            HttpsVerifyPeers = json.isHttpsVerifyPeers();
            HttpsCACertificates = json.getHttpsCACertificates();
            StableAge = json.getStableAge();
            StrictAetComparison = json.isStrictAetComparison();
            StoreMD5ForAttachments = json.isStoreMD5ForAttachments();
            LimitFindResults = json.getLimitFindResults();
            LimitFindInstances = json.getLimitFindInstances();
            LimitJobs = json.getLimitJobs();
            LogExportedResources = json.isLogExportedResources();
            KeepAlive = json.isKeepAlive();
            StoreDicom = json.isStoreDicom();
            DicomAssociationCloseDelay = json.getDicomAssociationCloseDelay();
            QueryRetrieveSize = json.getQueryRetrieveSize();
            CaseSensitivePN = json.isCaseSensitivePN();
            LoadPrivateDictionary = json.isLoadPrivateDictionary();
            dicomModalitiesInDb = json.isDicomModalitiesInDb();
            orthancPeerInDb = json.isOrthancPeerInDb();
            overwriteInstances = json.isOverwriteInstances();
            mediaArchiveSize = json.getMediaArchiveSize();
            storageAccessOnFind = json.getStorageAccessOnFind();
            httpVerbose = json.isHttpVerbose();
            tcpNoDelay = json.isTcpNoDelay();
            httpThreadsCount = json.getHttpThreadsCount();
            saveJobs = json.isSaveJobs();
            metricsEnabled = json.isMetricsEnabled();
            AllowFindSopClassesInStudy = json.isAllowFindSopClassesInStudy();
            dicomAlwaysAllowEcho = json.isDicomAlwaysAllowEcho();
            DicomAlwaysStore = json.isDicomAlwaysStore();
            CheckModalityHost = json.isCheckModalityHost();
            SynchronousCMove = json.isSynchronousCMove();
            JobsHistorySize = json.getJobsHistorySize();
            locale = json.getLocale();
            pluginsFolder = json.getPluginsFolder();
            worklistEnabled = getWorkListEnabled(json.getWorkLists());
            worklistPath = getWorkListPath(json.getWorkLists());
            webUsers = getWebUserFromJson(users.toString());
            dicomModalities = getDicomModalitisFromJson(dicomNode.toString());
        }catch (Exception e){
            LogTool.getLogger().error("Error during open orthanc.json! Try again! "+e.getMessage());
        }
    }


    public List<DicomModaliti> getDicomModalitisFromJson(String jsonStr){
        List<DicomModaliti> bufList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject orthancJson = parser.parse(jsonStr).getAsJsonObject();
        Set<String> keys = orthancJson.keySet();
        Object[] jsonkeys = keys.toArray();
        for (int i = 0; i <= jsonkeys.length - 1; i++) {
            JsonArray bufArray = orthancJson.getAsJsonArray(jsonkeys[i].toString());
            DicomModaliti node = new DicomModaliti("","","","","");
            node.setDicomName(jsonkeys[i].toString());
            node.setDicomTitle(bufArray.get(0).getAsString());
            node.setIp(bufArray.get(1).getAsString());
            node.setDicomPort(bufArray.get(2).getAsString());
            node.setDicomProperty(bufArray.get(3).getAsString());
            bufList.add(node);
        }
        return bufList;
    }

    public List<OrthancWebUser> getWebUserFromJson(String jsonStr){
        List<OrthancWebUser> bufUsers;
        bufUsers = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject orthancJson;
        orthancJson = parser.parse(jsonStr).getAsJsonObject();
        Set<String> keys = orthancJson.keySet();
        Object[] jsonkeys = keys.toArray();
        for(int i=0; i<=jsonkeys.length-1; i++){
            bufUsers.add(new OrthancWebUser(jsonkeys[i].toString(),orthancJson.get(jsonkeys[i].toString()).getAsString()));
        }
        return bufUsers;
    }

    public boolean getWorkListEnabled(JsonObject jsonObj){
        boolean result = false;
        if (jsonObj.has("Enable")) result = jsonObj.get("Enable").getAsBoolean();
        return result;
    }

    public String getWorkListPath(JsonObject jsonObj){
        String result = null;
        if (jsonObj.has("Database")) result = jsonObj.get("Database").getAsString();
        return result;
    }

    public List<OrthancWebUser> webUsers;

    public List<DicomModaliti> dicomModalities;

    String ServerName;
    JsonObject dicomNode = new JsonObject();
    JsonArray pluginsFolder = new JsonArray();
    JsonObject users = new JsonObject();
    String storageDirectory;
    String indexDirectory;
    boolean StorageCompression;
    int MaximumStorageSize;
    int MaximumPatientCount;
    boolean HttpServerEnabled;
    int HttpPort;
    boolean HttpDescribeErrors;
    boolean HttpCompressionEnabled;
    boolean DicomServerEnabled;
    String DicomAet;
    boolean DicomCheckCalledAet;
    int DicomPort;
    String DefaultEncoding;
    boolean DeflatedTransferSyntaxAccepted;
    boolean JpegTransferSyntaxAccepted;
    boolean Jpeg2000TransferSyntaxAccepted;
    boolean JpegLosslessTransferSyntaxAccepted;
    boolean JpipTransferSyntaxAccepted;
    boolean Mpeg2TransferSyntaxAccepted;
    boolean RleTransferSyntaxAccepted;
    boolean UnknownSopClassAccepted;
    int DicomScpTimeout;
    boolean RemoteAccessAllowed;
    boolean SslEnabled;
    boolean worklistEnabled;
    String worklistPath;
    String SslCertificate;
    boolean AuthenticationEnabled;
    int DicomScuTimeout;
    String HttpProxy;
    int HttpTimeout;
    boolean HttpsVerifyPeers;
    String HttpsCACertificates;
    String locale;
    int StableAge;
    boolean StrictAetComparison;
    boolean StoreMD5ForAttachments;
    int LimitFindResults;
    int LimitFindInstances;
    int LimitJobs;
    boolean LogExportedResources;
    boolean KeepAlive;
    boolean StoreDicom;
    int DicomAssociationCloseDelay;
    int QueryRetrieveSize;
    boolean CaseSensitivePN;
    boolean LoadPrivateDictionary;
    boolean dicomAlwaysAllowEcho;
    boolean DicomAlwaysStore;
    boolean CheckModalityHost;
    boolean SynchronousCMove;
    int JobsHistorySize;
    int ConcurrentJobs;
    boolean dicomModalitiesInDb;
    boolean orthancPeerInDb;
    boolean overwriteInstances;
    int mediaArchiveSize;
    String storageAccessOnFind;
    boolean httpVerbose;
    boolean tcpNoDelay;
    int httpThreadsCount;
    boolean saveJobs;
    boolean metricsEnabled;
    boolean AllowFindSopClassesInStudy;

    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void openNewModaliti() {
        selectedDicomModality = new DicomModaliti("","","","","");
    }

    public void AddNewModaliti(){
        if((!selectedDicomModality.getDicomTitle().equals(""))&(!selectedDicomModality.getDicomName().equals(""))
                &(!selectedDicomModality.getIp().equals(""))&(!selectedDicomModality.getDicomPort().equals(""))
                &(!selectedDicomModality.getDicomProperty().equals(""))&(!selectedDicomModality.getDicomName().contains("_"))&(!selectedDicomModality.getDicomName().contains(" ")))
        {
            if(Integer.parseInt(selectedDicomModality.getDicomPort())<65535) {
                boolean verifiUnical = true;
                if(dicomModalities.size()>0) {
                    for (DicomModaliti bufDicomModaliti : dicomModalities) {
                        if (bufDicomModaliti.getDicomName().equals(selectedDicomModality.getDicomName())) {
                            verifiUnical = false;
                            break;
                        }
                    }
                }
                if (verifiUnical) {
                    dicomModalities.add(new DicomModaliti(selectedDicomModality.getDicomTitle(), selectedDicomModality.getDicomName(),
                            selectedDicomModality.getIp(), selectedDicomModality.getDicomPort(), selectedDicomModality.getDicomProperty()));
                    PrimeFaces.current().executeScript("PF('manageModalitiDialog').hide()");
                    PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
                } else {
                    PrimeFaces.current().executeScript("PF('manageModalitiDialog').hide()");
                    PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
                }
            }else {
                showMessage("Внимание","Значение порта может должно быть в диапазоне от 104 - 65335!",FacesMessage.SEVERITY_ERROR);
            }
        }else{
            showMessage("Внимание","Все поля должны быть заполнены! Не допустимо использование символа: '_' или пробела в имени подальности!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void saveConfig() throws IOException {
        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("Name", ServerName);
        jsonOb.addProperty("StorageDirectory", storageDirectory);
        jsonOb.addProperty("IndexDirectory", indexDirectory);
        jsonOb.addProperty("StorageCompression", StorageCompression);
        jsonOb.addProperty("MaximumStorageSize", MaximumStorageSize);
        jsonOb.addProperty("MaximumPatientCount", MaximumPatientCount);
        jsonOb.add("Plugins",pluginsFolder);
        JsonArray luaScripts = new JsonArray();
        luaScripts.add("/usr/share/orthanc/lua/route.lua");
        jsonOb.add("LuaScripts",luaScripts);
        JsonObject jsonObj2 = new JsonObject();
        jsonObj2.addProperty("Enable", worklistEnabled);
        jsonObj2.addProperty("Database", worklistPath);
        jsonOb.add("Worklists",jsonObj2);
        BitServerResources bufRes = new BitServerResources("WorkListPath",worklistPath);
        updateBitServiceResource(bufRes);
        jsonOb.addProperty("ConcurrentJobs", ConcurrentJobs);
        jsonOb.addProperty("HttpServerEnabled", HttpServerEnabled);
        jsonOb.addProperty("HttpPort", HttpPort);
        jsonOb.addProperty("HttpDescribeErrors", HttpDescribeErrors);
        jsonOb.addProperty("HttpCompressionEnabled", HttpCompressionEnabled);
        jsonOb.addProperty("DicomServerEnabled", DicomServerEnabled);
        jsonOb.addProperty("DicomAet", DicomAet);
        jsonOb.addProperty("DicomCheckCalledAet", DicomCheckCalledAet);
        jsonOb.addProperty("DicomPort", DicomPort);
        jsonOb.addProperty("DefaultEncoding", DefaultEncoding);
        jsonOb.addProperty("DeflatedTransferSyntaxAccepted",DeflatedTransferSyntaxAccepted);
        jsonOb.addProperty("JpegTransferSyntaxAccepted",JpegTransferSyntaxAccepted);
        jsonOb.addProperty("Jpeg2000TransferSyntaxAccepted",Jpeg2000TransferSyntaxAccepted);
        jsonOb.addProperty("JpegLosslessTransferSyntaxAccepted",JpegLosslessTransferSyntaxAccepted);
        jsonOb.addProperty("JpipTransferSyntaxAccepted",JpipTransferSyntaxAccepted);
        jsonOb.addProperty("Mpeg2TransferSyntaxAccepted",Mpeg2TransferSyntaxAccepted);
        jsonOb.addProperty("RleTransferSyntaxAccepted",RleTransferSyntaxAccepted);
        jsonOb.addProperty("UnknownSopClassAccepted", UnknownSopClassAccepted);
        jsonOb.addProperty("DicomScpTimeout", DicomScpTimeout);
        jsonOb.addProperty("RemoteAccessAllowed", RemoteAccessAllowed);
        jsonOb.addProperty("SslEnabled", SslEnabled);
        jsonOb.addProperty("SslCertificate", SslCertificate);
        jsonOb.addProperty("AuthenticationEnabled", AuthenticationEnabled);

        JsonObject jsonObj = new JsonObject();
        for(int i=0; i<=webUsers.size()-1; i++){
            jsonObj.addProperty(webUsers.get(i).getLogin(), webUsers.get(i).getPass());
        }
        jsonOb.add("RegisteredUsers",jsonObj);

        jsonObj = new JsonObject();
        for(int i=0; i<=dicomModalities.size()-1; i++){
            JsonArray arrayJSON = new JsonArray();
            DicomModaliti node = dicomModalities.get(i);
            arrayJSON.add(node.getDicomTitle());
            arrayJSON.add(node.getIp());
            arrayJSON.add(node.getDicomPort());
            arrayJSON.add(node.getDicomProperty());
            jsonObj.add(node.getDicomName(), arrayJSON);
        }
        jsonOb.add("DicomModalities",jsonObj);
        jsonOb.addProperty("DicomModalitiesInDatabase", dicomModalitiesInDb);
        jsonOb.addProperty("DicomAlwaysAllowEcho", dicomAlwaysAllowEcho);
        jsonOb.addProperty("DicomAlwaysAllowStore", DicomAlwaysStore);
        jsonOb.addProperty("DicomCheckModalityHost", CheckModalityHost);
        jsonOb.addProperty("DicomScuTimeout", DicomScuTimeout);
        jsonObj = new JsonObject();
        jsonOb.add("OrthancPeers", jsonObj);
        jsonOb.addProperty("OrthancPeersInDatabase", orthancPeerInDb);
        jsonOb.addProperty("HttpProxy", HttpProxy);
        jsonOb.addProperty("HttpVerbose", httpVerbose);
        jsonOb.addProperty("HttpTimeout", HttpTimeout);
        jsonOb.addProperty("HttpsVerifyPeers", HttpsVerifyPeers);
        jsonOb.addProperty("HttpsCACertificates", HttpsCACertificates);
        jsonObj = new JsonObject();
        jsonOb.add("UserMetadata", jsonObj);
        jsonObj = new JsonObject();
        jsonOb.add("UserContentTyp", jsonObj);
        jsonOb.addProperty("StableAge", StableAge);
        jsonOb.addProperty("StrictAetComparison", StrictAetComparison);
        jsonOb.addProperty("StoreMD5ForAttachments", StoreMD5ForAttachments);
        jsonOb.addProperty("LimitFindResults", LimitFindResults);
        jsonOb.addProperty("LimitFindInstances", LimitFindInstances);
        jsonOb.addProperty("LimitJobs", LimitJobs);
        jsonOb.addProperty("LogExportedResources", LogExportedResources);
        jsonOb.addProperty("KeepAlive", KeepAlive);
        jsonOb.addProperty("StoreDicom", StoreDicom);
        jsonOb.addProperty("DicomAssociationCloseDelay", DicomAssociationCloseDelay);
        jsonOb.addProperty("QueryRetrieveSize", QueryRetrieveSize);
        jsonOb.addProperty("CaseSensitivePN", CaseSensitivePN);
        jsonOb.addProperty("AllowFindSopClassesInStudy", AllowFindSopClassesInStudy);
        jsonOb.addProperty("LoadPrivateDictionary", LoadPrivateDictionary);
        jsonObj = new JsonObject();
        jsonOb.add("Dictionary", jsonObj);
        jsonOb.addProperty("SynchronousCMove", SynchronousCMove);
        jsonOb.addProperty("JobsHistorySize", JobsHistorySize);
        jsonOb.addProperty("OverwriteInstances", overwriteInstances);
        jsonOb.addProperty("MediaArchiveSize", mediaArchiveSize);
        jsonOb.addProperty("ExecuteLuaEnabled",true);

        saveFile(jsonOb);

        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" save orthanc settings");
        snapShots = getAllOrthancSnapshots();
        PrimeFaces.current().ajax().update(":form:backupDiaolg");
    }

    public void saveFile(JsonObject newJson) throws IOException {
        ArrayList<String> changeList = new ArrayList<>();
        JsonObject oldJson = getJsonFromFile();
        Set<Map.Entry<String, JsonElement>> entrySet = oldJson.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            if(oldJson.get(entry.getKey())!=null & newJson.get(entry.getKey())!=null){
                if (!oldJson.get(entry.getKey()).toString().equals(newJson.get(entry.getKey()).toString())) {
                    changeList.add(entry.getKey());
                }
            }
        }
        if (changeList.size() > 0) {
            saveJsonSettingtToFile(newJson);
            saveSnapshot(new OrthancSettingSnapshot(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), changeList.toString(), newJson));
            showMessage("Сообщение", "Изменения сохранены! Изменения в:  " + changeList.toString(), info);
        } else {
            showMessage("Внимание", "Вы не внесли изменений!", FacesMessage.SEVERITY_INFO);
        }
    }

    public void deleteModaliti() {
        dicomModalities.remove(selectedDicomModality);
        selectedDicomModality= new DicomModaliti("","","","","");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Модальность удалена!"));
        PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException, SQLException {
        UploadedFile file = event.getFile();
        InputStream inputStream = new ByteArrayInputStream(file.getContent());
        String text = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        try {
            JsonParser parser = new JsonParser();
            JsonObject orthancJson;
            orthancJson = parser.parse(text).getAsJsonObject();
            if(orthancJson.has("DicomAet")&orthancJson.has("DicomModalities")){
                saveSnapshot(new OrthancSettingSnapshot(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "Загружен вручную", orthancJson));
                snapShots = getAllOrthancSnapshots();
                PrimeFaces.current().ajax().update(":form:dt-snap");
                LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" load snapshot settings");
            }else{
                showMessage("Внимание","Загруженный файл не соответствует формату!",FacesMessage.SEVERITY_ERROR);
            }
        }catch (Exception e){
            LogTool.getLogger().error("Error parse json JsonSetings: "+e.getMessage());
            showMessage("Внимание", "Проблема при загрузке файла настроек!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void setCurrentUser(BitServerUser currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<OrthancSettingSnapshot> getSnapShots() {
        return snapShots;
    }

    public void setSnapShots(ArrayList<OrthancSettingSnapshot> snapShots) {
        this.snapShots = snapShots;
    }

    public ArrayList<OrthancSettingSnapshot> getSelectedSnapShots() {
        return selectedSnapShots;
    }

    public void setSelectedSnapShots(ArrayList<OrthancSettingSnapshot> selectedSnapShots) {
        this.selectedSnapShots = selectedSnapShots;
    }

    public OrthancSettingSnapshot getSelectedSnapshot() {
        return selectedSnapshot;
    }

    public void setSelectedSnapshot(OrthancSettingSnapshot selectedSnapshot) {
        this.selectedSnapshot = selectedSnapshot;
    }

    public DicomModaliti getSelectedDicomModality() {
        return selectedDicomModality;
    }

    public void setSelectedDicomModality(DicomModaliti selectedDicomModality) {
        this.selectedDicomModality = selectedDicomModality;
    }

    public OrthancWebUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(OrthancWebUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public JsonSettings getJson() {
        return json;
    }

    public void setJson(JsonSettings json) {
        this.json = json;
    }

    public List<OrthancWebUser> getWebUsers() {
        return webUsers;
    }

    public void setWebUsers(List<OrthancWebUser> webUsers) {
        this.webUsers = webUsers;
    }

    public List<DicomModaliti> getDicomModalities() {
        return dicomModalities;
    }

    public void setDicomModalities(List<DicomModaliti> dicomModalities) {
        this.dicomModalities = dicomModalities;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public JsonObject getDicomNode() {
        return dicomNode;
    }

    public void setDicomNode(JsonObject dicomNode) {
        this.dicomNode = dicomNode;
    }

    public JsonArray getPluginsFolder() {
        return pluginsFolder;
    }

    public void setPluginsFolder(JsonArray pluginsFolder) {
        this.pluginsFolder = pluginsFolder;
    }

    public JsonObject getUsers() {
        return users;
    }

    public void setUsers(JsonObject users) {
        this.users = users;
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public void setStorageDirectory(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public boolean isStorageCompression() {
        return StorageCompression;
    }

    public void setStorageCompression(boolean storageCompression) {
        StorageCompression = storageCompression;
    }

    public int getMaximumStorageSize() {
        return MaximumStorageSize;
    }

    public void setMaximumStorageSize(int maximumStorageSize) {
        MaximumStorageSize = maximumStorageSize;
    }

    public int getMaximumPatientCount() {
        return MaximumPatientCount;
    }

    public void setMaximumPatientCount(int maximumPatientCount) {
        MaximumPatientCount = maximumPatientCount;
    }

    public boolean isHttpServerEnabled() {
        return HttpServerEnabled;
    }

    public void setHttpServerEnabled(boolean httpServerEnabled) {
        HttpServerEnabled = httpServerEnabled;
    }

    public int getHttpPort() {
        return HttpPort;
    }

    public void setHttpPort(int httpPort) {
        HttpPort = httpPort;
    }

    public boolean isHttpDescribeErrors() {
        return HttpDescribeErrors;
    }

    public void setHttpDescribeErrors(boolean httpDescribeErrors) {
        HttpDescribeErrors = httpDescribeErrors;
    }

    public boolean isHttpCompressionEnabled() {
        return HttpCompressionEnabled;
    }

    public void setHttpCompressionEnabled(boolean httpCompressionEnabled) {
        HttpCompressionEnabled = httpCompressionEnabled;
    }

    public boolean isDicomServerEnabled() {
        return DicomServerEnabled;
    }

    public void setDicomServerEnabled(boolean dicomServerEnabled) {
        DicomServerEnabled = dicomServerEnabled;
    }

    public String getDicomAet() {
        return DicomAet;
    }

    public void setDicomAet(String dicomAet) {
        DicomAet = dicomAet;
    }

    public boolean isDicomCheckCalledAet() {
        return DicomCheckCalledAet;
    }

    public void setDicomCheckCalledAet(boolean dicomCheckCalledAet) {
        DicomCheckCalledAet = dicomCheckCalledAet;
    }

    public int getDicomPort() {
        return DicomPort;
    }

    public void setDicomPort(int dicomPort) {
        DicomPort = dicomPort;
    }

    public String getDefaultEncoding() {
        return DefaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        DefaultEncoding = defaultEncoding;
    }

    public boolean isDeflatedTransferSyntaxAccepted() {
        return DeflatedTransferSyntaxAccepted;
    }

    public void setDeflatedTransferSyntaxAccepted(boolean deflatedTransferSyntaxAccepted) {
        DeflatedTransferSyntaxAccepted = deflatedTransferSyntaxAccepted;
    }

    public boolean isJpegTransferSyntaxAccepted() {
        return JpegTransferSyntaxAccepted;
    }

    public void setJpegTransferSyntaxAccepted(boolean jpegTransferSyntaxAccepted) {
        JpegTransferSyntaxAccepted = jpegTransferSyntaxAccepted;
    }

    public boolean isJpeg2000TransferSyntaxAccepted() {
        return Jpeg2000TransferSyntaxAccepted;
    }

    public void setJpeg2000TransferSyntaxAccepted(boolean jpeg2000TransferSyntaxAccepted) {
        Jpeg2000TransferSyntaxAccepted = jpeg2000TransferSyntaxAccepted;
    }

    public boolean isJpegLosslessTransferSyntaxAccepted() {
        return JpegLosslessTransferSyntaxAccepted;
    }

    public void setJpegLosslessTransferSyntaxAccepted(boolean jpegLosslessTransferSyntaxAccepted) {
        JpegLosslessTransferSyntaxAccepted = jpegLosslessTransferSyntaxAccepted;
    }

    public boolean isJpipTransferSyntaxAccepted() {
        return JpipTransferSyntaxAccepted;
    }

    public void setJpipTransferSyntaxAccepted(boolean jpipTransferSyntaxAccepted) {
        JpipTransferSyntaxAccepted = jpipTransferSyntaxAccepted;
    }

    public boolean isMpeg2TransferSyntaxAccepted() {
        return Mpeg2TransferSyntaxAccepted;
    }

    public void setMpeg2TransferSyntaxAccepted(boolean mpeg2TransferSyntaxAccepted) {
        Mpeg2TransferSyntaxAccepted = mpeg2TransferSyntaxAccepted;
    }

    public boolean isRleTransferSyntaxAccepted() {
        return RleTransferSyntaxAccepted;
    }

    public void setRleTransferSyntaxAccepted(boolean rleTransferSyntaxAccepted) {
        RleTransferSyntaxAccepted = rleTransferSyntaxAccepted;
    }

    public boolean isUnknownSopClassAccepted() {
        return UnknownSopClassAccepted;
    }

    public void setUnknownSopClassAccepted(boolean unknownSopClassAccepted) {
        UnknownSopClassAccepted = unknownSopClassAccepted;
    }

    public int getDicomScpTimeout() {
        return DicomScpTimeout;
    }

    public void setDicomScpTimeout(int dicomScpTimeout) {
        DicomScpTimeout = dicomScpTimeout;
    }

    public boolean isRemoteAccessAllowed() {
        return RemoteAccessAllowed;
    }

    public void setRemoteAccessAllowed(boolean remoteAccessAllowed) {
        RemoteAccessAllowed = remoteAccessAllowed;
    }

    public boolean isSslEnabled() {
        return SslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        SslEnabled = sslEnabled;
    }

    public boolean isWorklistEnabled() {
        return worklistEnabled;
    }

    public void setWorklistEnabled(boolean worklistEnabled) {
        this.worklistEnabled = worklistEnabled;
    }

    public String getWorklistPath() {
        return worklistPath;
    }

    public void setWorklistPath(String worklistPath) {
        this.worklistPath = worklistPath;
    }

    public String getSslCertificate() {
        return SslCertificate;
    }

    public void setSslCertificate(String sslCertificate) {
        SslCertificate = sslCertificate;
    }

    public boolean isAuthenticationEnabled() {
        return AuthenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled) {
        AuthenticationEnabled = authenticationEnabled;
    }

    public int getDicomScuTimeout() {
        return DicomScuTimeout;
    }

    public void setDicomScuTimeout(int dicomScuTimeout) {
        DicomScuTimeout = dicomScuTimeout;
    }

    public String getHttpProxy() {
        return HttpProxy;
    }

    public void setHttpProxy(String httpProxy) {
        HttpProxy = httpProxy;
    }

    public int getHttpTimeout() {
        return HttpTimeout;
    }

    public void setHttpTimeout(int httpTimeout) {
        HttpTimeout = httpTimeout;
    }

    public boolean isHttpsVerifyPeers() {
        return HttpsVerifyPeers;
    }

    public void setHttpsVerifyPeers(boolean httpsVerifyPeers) {
        HttpsVerifyPeers = httpsVerifyPeers;
    }

    public String getHttpsCACertificates() {
        return HttpsCACertificates;
    }

    public void setHttpsCACertificates(String httpsCACertificates) {
        HttpsCACertificates = httpsCACertificates;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getStableAge() {
        return StableAge;
    }

    public void setStableAge(int stableAge) {
        StableAge = stableAge;
    }

    public boolean isStrictAetComparison() {
        return StrictAetComparison;
    }

    public void setStrictAetComparison(boolean strictAetComparison) {
        StrictAetComparison = strictAetComparison;
    }

    public boolean isStoreMD5ForAttachments() {
        return StoreMD5ForAttachments;
    }

    public void setStoreMD5ForAttachments(boolean storeMD5ForAttachments) {
        StoreMD5ForAttachments = storeMD5ForAttachments;
    }

    public int getLimitFindResults() {
        return LimitFindResults;
    }

    public void setLimitFindResults(int limitFindResults) {
        LimitFindResults = limitFindResults;
    }

    public int getLimitFindInstances() {
        return LimitFindInstances;
    }

    public void setLimitFindInstances(int limitFindInstances) {
        LimitFindInstances = limitFindInstances;
    }

    public int getLimitJobs() {
        return LimitJobs;
    }

    public void setLimitJobs(int limitJobs) {
        LimitJobs = limitJobs;
    }

    public boolean isLogExportedResources() {
        return LogExportedResources;
    }

    public void setLogExportedResources(boolean logExportedResources) {
        LogExportedResources = logExportedResources;
    }

    public boolean isKeepAlive() {
        return KeepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        KeepAlive = keepAlive;
    }

    public boolean isStoreDicom() {
        return StoreDicom;
    }

    public void setStoreDicom(boolean storeDicom) {
        StoreDicom = storeDicom;
    }

    public int getDicomAssociationCloseDelay() {
        return DicomAssociationCloseDelay;
    }

    public void setDicomAssociationCloseDelay(int dicomAssociationCloseDelay) {
        DicomAssociationCloseDelay = dicomAssociationCloseDelay;
    }

    public int getQueryRetrieveSize() {
        return QueryRetrieveSize;
    }

    public void setQueryRetrieveSize(int queryRetrieveSize) {
        QueryRetrieveSize = queryRetrieveSize;
    }

    public boolean isCaseSensitivePN() {
        return CaseSensitivePN;
    }

    public void setCaseSensitivePN(boolean caseSensitivePN) {
        CaseSensitivePN = caseSensitivePN;
    }

    public boolean isLoadPrivateDictionary() {
        return LoadPrivateDictionary;
    }

    public void setLoadPrivateDictionary(boolean loadPrivateDictionary) {
        LoadPrivateDictionary = loadPrivateDictionary;
    }

    public boolean isDicomAlwaysAllowEcho() {
        return dicomAlwaysAllowEcho;
    }

    public void setDicomAlwaysAllowEcho(boolean dicomAlwaysAllowEcho) {
        this.dicomAlwaysAllowEcho = dicomAlwaysAllowEcho;
    }

    public boolean isDicomAlwaysStore() {
        return DicomAlwaysStore;
    }

    public void setDicomAlwaysStore(boolean dicomAlwaysStore) {
        DicomAlwaysStore = dicomAlwaysStore;
    }

    public boolean isCheckModalityHost() {
        return CheckModalityHost;
    }

    public void setCheckModalityHost(boolean checkModalityHost) {
        CheckModalityHost = checkModalityHost;
    }

    public boolean isSynchronousCMove() {
        return SynchronousCMove;
    }

    public void setSynchronousCMove(boolean synchronousCMove) {
        SynchronousCMove = synchronousCMove;
    }

    public int getJobsHistorySize() {
        return JobsHistorySize;
    }

    public void setJobsHistorySize(int jobsHistorySize) {
        JobsHistorySize = jobsHistorySize;
    }

    public int getConcurrentJobs() {
        return ConcurrentJobs;
    }

    public void setConcurrentJobs(int concurrentJobs) {
        ConcurrentJobs = concurrentJobs;
    }

    public boolean isDicomModalitiesInDb() {
        return dicomModalitiesInDb;
    }

    public void setDicomModalitiesInDb(boolean dicomModalitiesInDb) {
        this.dicomModalitiesInDb = dicomModalitiesInDb;
    }

    public boolean isOrthancPeerInDb() {
        return orthancPeerInDb;
    }

    public void setOrthancPeerInDb(boolean orthancPeerInDb) {
        this.orthancPeerInDb = orthancPeerInDb;
    }

    public boolean isOverwriteInstances() {
        return overwriteInstances;
    }

    public void setOverwriteInstances(boolean overwriteInstances) {
        this.overwriteInstances = overwriteInstances;
    }

    public int getMediaArchiveSize() {
        return mediaArchiveSize;
    }

    public void setMediaArchiveSize(int mediaArchiveSize) {
        this.mediaArchiveSize = mediaArchiveSize;
    }

    public String getStorageAccessOnFind() {
        return storageAccessOnFind;
    }

    public void setStorageAccessOnFind(String storageAccessOnFind) {
        this.storageAccessOnFind = storageAccessOnFind;
    }

    public boolean isHttpVerbose() {
        return httpVerbose;
    }

    public void setHttpVerbose(boolean httpVerbose) {
        this.httpVerbose = httpVerbose;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getHttpThreadsCount() {
        return httpThreadsCount;
    }

    public void setHttpThreadsCount(int httpThreadsCount) {
        this.httpThreadsCount = httpThreadsCount;
    }

    public boolean isSaveJobs() {
        return saveJobs;
    }

    public void setSaveJobs(boolean saveJobs) {
        this.saveJobs = saveJobs;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    public boolean isAllowFindSopClassesInStudy() {
        return AllowFindSopClassesInStudy;
    }

    public void setAllowFindSopClassesInStudy(boolean allowFindSopClassesInStudy) {
        AllowFindSopClassesInStudy = allowFindSopClassesInStudy;
    }

    public FacesMessage.Severity getInfo() {
        return info;
    }

    public void setInfo(FacesMessage.Severity info) {
        this.info = info;
    }

    public FacesMessage.Severity getError() {
        return error;
    }

    public void setError(FacesMessage.Severity error) {
        this.error = error;
    }

    public FacesMessage.Severity getWarning() {
        return warning;
    }

    public void setWarning(FacesMessage.Severity warning) {
        this.warning = warning;
    }
}
package ru.bitServer.beans;

import static ru.bitServer.beans.MainBean.mainServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.JsonSettings;
import ru.bitServer.dicom.OrthancWebUser;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

//eager -  Если данный параметр равен “true“, то MB создаётся до первого запроса данного бина.
// С другой стороны, если “eager = false“, то происходит, так называемая,
// “ленивая” инициализация, при которой бин создаётся только после запроса.
@ManagedBean(name = "settingsBean", eager = false)
@SessionScoped
public class SettingsBean {
    public static String authentication;
    public String ServerName;
    public JsonObject dicomNode = new JsonObject();
    public JsonObject orthancPeer = new JsonObject();
    public JsonObject contentType = new JsonObject();
    public JsonObject dictionary = new JsonObject();
    public JsonArray luaFolder = new JsonArray();
    public JsonArray pluginsFolder = new JsonArray();
    public JsonObject users = new JsonObject();
    public JsonObject userMetadata = new JsonObject();
    public String storageDirectory;
    public String indexDirectory;
    public boolean StorageCompression;
    public int MaximumStorageSize;
    public int MaximumPatientCount;
    public boolean HttpServerEnabled;
    public int HttpPort;
    public boolean HttpDescribeErrors;
    public boolean HttpCompressionEnabled;
    public boolean DicomServerEnabled;
    public String DicomAet;
    public boolean DicomCheckCalledAet;
    public int DicomPort;
    public String DefaultEncoding;
    public boolean DeflatedTransferSyntaxAccepted;
    public boolean JpegTransferSyntaxAccepted;
    public boolean Jpeg2000TransferSyntaxAccepted;
    public boolean JpegLosslessTransferSyntaxAccepted;
    public boolean JpipTransferSyntaxAccepted;
    public boolean Mpeg2TransferSyntaxAccepted;
    public boolean RleTransferSyntaxAccepted;
    public boolean UnknownSopClassAccepted;
    public int DicomScpTimeout;
    public boolean RemoteAccessAllowed;
    public boolean SslEnabled;
    public String SslCertificate;
    public boolean AuthenticationEnabled;
    public int DicomScuTimeout;
    public String HttpProxy;
    public int HttpTimeout;
    public boolean HttpsVerifyPeers;
    public String HttpsCACertificates;
    public String locale;
    public int StableAge;
    public boolean StrictAetComparison;
    public boolean StoreMD5ForAttachments;
    public int LimitFindResults;
    public int LimitFindInstances;
    public int LimitJobs;
    public boolean LogExportedResources;
    public boolean KeepAlive;
    public boolean StoreDicom;
    public int DicomAssociationCloseDelay;
    public int QueryRetrieveSize;
    public boolean CaseSensitivePN;
    public boolean LoadPrivateDictionary;
    public boolean dicomAlwaysAllowEcho;
    public boolean DicomAlwaysStore;
    public boolean CheckModalityHost;
    public boolean SynchronousCMove;
    public int JobsHistorySize;
    public int ConcurrentJobs;
    public boolean dicomModalitiesInDb;
    public boolean orthancPeerInDb;
    public boolean overwriteInstances;
    public int mediaArchiveSize;
    public String storageAccessOnFind;
    public boolean httpVerbose;
    public boolean tcpNoDelay;
    public int httpThreadsCount;
    public boolean saveJobs;
    public boolean metricsEnabled;
    public boolean AllowFindSopClassesInStudy;
    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;

    public List<OrthancWebUser> webUsers;

    public List<OrthancWebUser> selectedWebUsers;

    public OrthancWebUser selectedUser;

    public List<DicomModaliti> dicomModalities;

    public List<DicomModaliti> getSelectedDicomModalities() {
        return selectedDicomModalities;
    }

    public void setSelectedDicomModalities(List<DicomModaliti> selectedDicomModalities) {
        this.selectedDicomModalities = selectedDicomModalities;
    }

    public List<DicomModaliti> selectedDicomModalities;

    public DicomModaliti getSelectedDicomModality() {
        return selectedDicomModality;
    }

    public void setSelectedDicomModality(DicomModaliti selectedDicomModality) {
        this.selectedDicomModality = selectedDicomModality;
    }

    public DicomModaliti selectedDicomModality;

    public List<DicomModaliti> getDicomModalities() {
        return dicomModalities;
    }

    public void setDicomModalities(List<DicomModaliti> dicomModalities) {
        this.dicomModalities = dicomModalities;
    }

    public OrthancWebUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(OrthancWebUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<OrthancWebUser> getSelectedWebUsers() {
        return selectedWebUsers;
    }

    public void setSelectedWebUsers(List<OrthancWebUser> selectedWebUsers) {
        this.selectedWebUsers = selectedWebUsers;
    }

    public JsonSettings json;

    @PostConstruct
    public void init() {
        System.out.println("settings");
        selectedUser = new OrthancWebUser("","");
        selectedDicomModality = new DicomModaliti("","","","","");
        loadConfig();
        List<OrthancWebUser> webUsers;// = new ArrayList<>();
        webUsers = getWebUserFromJson(users.toString());
        this.webUsers = webUsers;
        List<DicomModaliti> dicomModalities;// = new ArrayList<>();;
        dicomModalities = getDicomModalitisFromJson(dicomNode.toString());
        this.dicomModalities = dicomModalities;
    }

    public void loadConfig(){
        System.out.println("loadconfig");
        String urlParameters = "f = io.open(\""+ ModifyStr(mainServer.getPathToJson()) +"orthanc.json\",\"r+\");"+
                "print(f:read(\"*a\"))"+
                "f:close()";
        StringBuilder stringBuilder = makePostConnectionAndStringBuilder("/tools/execute-script",urlParameters);
        System.out.println("out settings = "+stringBuilder);
        json = new JsonSettings(stringBuilder.toString());
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
            arrayJSON.add(node.getDicomtitle());
            arrayJSON.add(node.getIp());
            arrayJSON.add(node.getDicomport());
            arrayJSON.add(node.getDicomproperty());
            jsonObj.add(node.getDicomname(), arrayJSON);
        }

        jsonOb.add("DicomModalities",jsonObj);
        jsonOb.addProperty("DicomModalitiesInDatabase", dicomModalitiesInDb);
        jsonOb.addProperty("DicomAlwaysAllowEcho", dicomAlwaysAllowEcho);
        jsonOb.addProperty("DicomAlwaysAllowStore", DicomAlwaysStore);
        jsonOb.addProperty("DicomCheckModalityHost", CheckModalityHost);
        jsonOb.addProperty("DicomScuTimeout", DicomScuTimeout);

        jsonObj = new JsonObject();
        jsonOb.add("OrthancPeers", jsonObj);
        //orthancJson = parser.parse(prefs.getString("OrthancPeers", "")).getAsJsonObject();
        //jsonOb.add("OrthancPeers",orthancJson);

        jsonOb.addProperty("OrthancPeersInDatabase", orthancPeerInDb);
        jsonOb.addProperty("HttpProxy", HttpProxy);
        jsonOb.addProperty("HttpVerbose", httpVerbose);
        jsonOb.addProperty("HttpTimeout", HttpTimeout);
        jsonOb.addProperty("HttpsVerifyPeers", HttpsVerifyPeers);
        jsonOb.addProperty("HttpsCACertificates", HttpsCACertificates);

        jsonObj = new JsonObject();
        jsonOb.add("UserMetadata", jsonObj);
//        orthancJson = parser.parse(prefs.getString("UserMetadata", "")).getAsJsonObject();
//        jsonOb.add("UserMetadata", orthancJson);
        jsonObj = new JsonObject();
        jsonOb.add("UserContentTyp", jsonObj);
//        orthancJson = parser.parse(prefs.getString("UserContentType", "")).getAsJsonObject();
//        jsonOb.add("UserContentType", orthancJson);
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
//        orthancJson = parser.parse(prefs.getString("Dictionary", "")).getAsJsonObject();
//        jsonOb.add("Dictionary", orthancJson);
        jsonOb.addProperty("SynchronousCMove", SynchronousCMove);
        jsonOb.addProperty("JobsHistorySize", JobsHistorySize);
        jsonOb.addProperty("OverwriteInstances", overwriteInstances);
        jsonOb.addProperty("MediaArchiveSize", mediaArchiveSize);
        jsonOb.addProperty("ExecuteLuaEnabled",true);

        String modifyStr = ModifyStr(jsonOb.toString());
        String urlParameters = "f = io.open(\""+ ModifyStr(mainServer.getPathToJson()) +"orthanc.json\",\"w+\");" +
                "f:write(\""+modifyStr+"\"); "+
                "f:close()";
        //System.out.println("url param "+urlParameters);
        StringBuilder stringBuilder = makePostConnectionAndStringBuilder("/tools/execute-script",urlParameters);

        //System.out.println("saveout = "+jsonOb.toString());
        showMessage("Сообщение","Изменения сохранены!", info);
    }

    public void resetServer(){
        StringBuilder sb = makePostConnectionAndStringBuilder("/tools/reset","" );
        //System.out.println(sb);
        showMessage("Сообщение","Сервис перезагружен!", info);
    }

    public static StringBuilder makePostConnectionAndStringBuilder(String apiUrl, String post) {
        StringBuilder sb =null;
        try {
            sb = new StringBuilder();
            HttpURLConnection conn = makePostConnection(apiUrl, post);
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                if(truestring(output)){
                    int i = output.indexOf("}");
                    if(i!= -1){
                        if ((i == (output.length()-1)&(i!=0))) {
                            sb.append(output);
                        }else
                        {
                            sb.append(output);
                        }
                    }else {
                        sb.append(output);
                    }
                }
            }
            conn.disconnect();
            conn.getResponseMessage();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    public static HttpURLConnection makePostConnection(String apiUrl, String post) throws Exception {
        String fulladdress = "http://"+ mainServer.getIpaddress()+":"+ mainServer.getPort();
        HttpURLConnection conn = null ;
        URL url = new URL(fulladdress+apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        authentication = Base64.getEncoder().encodeToString((mainServer.getLogin()+":"+mainServer.getPassword()).getBytes());
        System.out.println("authentication = "+authentication);
        if(authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        OutputStream os = conn.getOutputStream();
        os.write(post.getBytes());
        os.flush();
        conn.getResponseMessage();
        return conn;
    }

    public String ModifyStr(String str){
        String result;
        String buf0;
        String buf;
        String buf2;
        buf0 = str.replace("\\","\\\\");
        buf = buf0.replace("/","\\/");
        buf2 = buf.replace("\"","\\\"");
        result = buf2.replace(",",",\\n");
        System.out.println("modifi = "+result);
        return result;
    }

    public void restoreBackup(){
        init();
        showMessage("Сообщение","Все изменения отменены!", info);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private static Boolean truestring(String str){
        String buf = "/*";
        boolean troubleSimbol = true;
        char char3 = buf.charAt(1);
        int j = str.indexOf("//");
        int k = str.indexOf("http");
        boolean ifSlash = false;
        boolean ifHTTP = false;
        boolean check = false;
        if(j!=-1){
            ifSlash = true;
            check = true;
        }
        if(k!=-1){
            ifHTTP = true;
        }
        if(ifSlash&ifHTTP){
            if(j<k){
                check = true;
            }else{
                check = false;
            }
        }
        str.replaceAll("\\s+","");
        if(str.length()>0){
            char char1 = str.charAt(0);
            if((char1 == char3)|(check)){
                troubleSimbol = false;
            }
        }
        return troubleSimbol;
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
                node.setDicomname(jsonkeys[i].toString());
                node.setDicomtitle(bufArray.get(0).getAsString());
                node.setIp(bufArray.get(1).getAsString());
                node.setDicomport(bufArray.get(2).getAsString());
                node.setDicomproperty(bufArray.get(3).getAsString());
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

    public JsonObject setWebUserToJson(){
        JsonObject jsonObj = new JsonObject();
        for(int i=0; i<=webUsers.size()-1; i++){
            jsonObj.addProperty(webUsers.get(i).getLogin(), webUsers.get(i).getPass());
        }
        return  jsonObj;
    }

    public void AddNewWebUser(){
        if((!selectedUser.getLogin().equals(""))&(!selectedUser.getPass().equals(""))) {
            webUsers.add(new OrthancWebUser(selectedUser.getLogin(),selectedUser.getPass()));
            PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accordion:dt-users");
        }else{
            System.out.println("else new web user");
        }
    }

    public void AddNewModaliti(){
        if((!selectedDicomModality.getDicomtitle().equals(""))&(!selectedDicomModality.getDicomname().equals(""))
                &(!selectedDicomModality.getIp().equals(""))&(!selectedDicomModality.getDicomport().equals(""))
                &(!selectedDicomModality.getDicomproperty().equals(""))) {
            System.out.println("add new modaliti");
            dicomModalities.add(new DicomModaliti(selectedDicomModality.getDicomtitle(),selectedDicomModality.getDicomname(),
                    selectedDicomModality.getIp(),selectedDicomModality.getDicomport(),selectedDicomModality.getDicomproperty()));
            PrimeFaces.current().executeScript("PF('manageModalitiDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Все поля должны быть заполнены!"));
        }
    }

    public void deleteModaliti() {
        this.dicomModalities.remove(this.selectedDicomModality);
        selectedDicomModality= new DicomModaliti("","","","","");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Модальность удалена!"));
        PrimeFaces.current().ajax().update(":form:accordion:dt-modaliti");
    }

    public void deleteWebUser() {
        this.webUsers.remove(this.selectedUser);
        selectedUser = new OrthancWebUser("","");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Пользователь удален!"));
        PrimeFaces.current().ajax().update(":form:accordion:dt-users");
    }

    public void openNew() {
        System.out.println("open new user");
        selectedUser = new OrthancWebUser("","");
    }

    public void openNewModaliti() {
        System.out.println("open new modaliti");
        selectedDicomModality = new DicomModaliti("","","","","");
    }

    public JsonArray getPluginsFolder() {
        return pluginsFolder;
    }

    public void setPluginsFolder(JsonArray pluginsFolder) {
        this.pluginsFolder = pluginsFolder;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


    public Object getWebUsers() {
        return webUsers;
    }

}

package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ManagedBean
@ViewScoped
public class SettingsBean {

    public String ServerName;
    public static boolean value1;
    public JsonObject dicomNode=new JsonObject();
    public JsonObject orthancPeer=new JsonObject();
    public JsonObject contentType=new JsonObject();
    public JsonObject dictionary=new JsonObject();
    public JsonArray luaFolder=new JsonArray();
    public JsonArray pluginsFolder=new JsonArray();
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

    public OrthancWebUser selectedUser;

    public OrthancWebUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(OrthancWebUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<OrthancWebUser> selectedUsers;

    @PostConstruct
    public void init() {
        loadConfig();
        List<OrthancWebUser> webUsers;
        webUsers = new ArrayList<>();
        webUsers = getWebUserFromJson(users.toString());
        this.webUsers = webUsers;
    }

    public void loadConfig(){
        openNew();
    value1 = true;
        System.out.println("loadconfig");
        StringBuilder stringBuilder = new StringBuilder();
            boolean resultOpenFile =false;
            try (FileInputStream fin = new FileInputStream("D://orthanc.json")) {
                int i = -1;
                while ((i = fin.read()) != -1) {
                    stringBuilder.append((char) i);
                }
                resultOpenFile = true;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                resultOpenFile = false;
            }

        //парсинг файла настроек
        if(resultOpenFile){
            StringBuilder stringBuilderBuf = new StringBuilder();
            String buf = stringBuilder.toString();
            String[] words = buf.split("\n");
                for (String word : words) {
                    if(truestring(word)){
                      stringBuilderBuf.append(word);
                    }
                }
            JsonSettings json = new JsonSettings(stringBuilderBuf.toString());


            users = json.users;
            ServerName = json.orthancName;
            storageDirectory = json.storageDirectory;
            indexDirectory = json.indexDirectory;
            StorageCompression = json.StorageCompression;
            MaximumStorageSize = json.MaximumStorageSize;
            MaximumPatientCount = json.MaximumPatientCount;
            ConcurrentJobs = json.ConcurrentJobs;
            HttpServerEnabled = json.HttpServerEnabled;
            HttpPort = json.HttpPort;
            HttpDescribeErrors = json.HttpDescribeErrors;
            HttpCompressionEnabled = json.HttpCompressionEnabled;
            DicomServerEnabled = json.DicomServerEnabled;
            DicomAet = json.DicomAet;
            DicomCheckCalledAet = json.DicomCheckCalledAet;
            DicomPort = json.DicomPort;
            DefaultEncoding = json.DefaultEncoding;
            DeflatedTransferSyntaxAccepted = json.DeflatedTransferSyntaxAccepted;
            JpegTransferSyntaxAccepted = json.JpegTransferSyntaxAccepted;
            Jpeg2000TransferSyntaxAccepted = json.Jpeg2000TransferSyntaxAccepted;
            JpegLosslessTransferSyntaxAccepted = json.JpegLosslessTransferSyntaxAccepted;
            JpipTransferSyntaxAccepted = json.JpipTransferSyntaxAccepted;
            Mpeg2TransferSyntaxAccepted = json.Mpeg2TransferSyntaxAccepted;
            RleTransferSyntaxAccepted = json.RleTransferSyntaxAccepted;
            UnknownSopClassAccepted = json.UnknownSopClassAccepted;
            DicomScpTimeout = json.DicomScpTimeout;
            RemoteAccessAllowed = json.RemoteAccessAllowed;
            SslEnabled = json.SslEnabled ;
            SslCertificate = json.SslCertificate;
            AuthenticationEnabled = json.AuthenticationEnabled;
            DicomScuTimeout = json.DicomScuTimeout;
            HttpProxy = json.HttpProxy;
            HttpTimeout = json.HttpTimeout;
            HttpsVerifyPeers = json.HttpsVerifyPeers;
            HttpsCACertificates = json.HttpsCACertificates;
            StableAge = json.StableAge;
            StrictAetComparison = json.StrictAetComparison;
            dicomModalitiesInDb = json.dicomModalitiesInDb;
            orthancPeerInDb = json.orthancPeerInDb;
            overwriteInstances = json.overwriteInstances;
            mediaArchiveSize = json.mediaArchiveSize;
            storageAccessOnFind = json.storageAccessOnFind;
            httpVerbose = json.httpVerbose;
            tcpNoDelay = json.tcpNoDelay;
            httpThreadsCount = json.httpThreadsCount;
            saveJobs = json.saveJobs;
            metricsEnabled = json.metricsEnabled;
            AllowFindSopClassesInStudy = json.AllowFindSopClassesInStudy;
            dicomAlwaysAllowEcho = json.dicomAlwaysAllowEcho;
            DicomAlwaysStore = json.DicomAlwaysStore;
            CheckModalityHost = json.CheckModalityHost;
            SynchronousCMove = json.SynchronousCMove;
            JobsHistorySize = json.JobsHistorySize;
            locale = json.locale;
        }else{
            FacesMessage message = new FacesMessage("Внимание", "Ошибка при чтении файла orthanc.json");
            message.setSeverity(FacesMessage.SEVERITY_INFO); //как выглядит окошко с сообщением
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void saveConfig(){
        showMessage("Сообщение","Изменения сохранены!", info);
    }

    public void resetServer(){
        showMessage("Сообщение","Сервис перезагружен!", info);
    }

    public void restoreBackup(){
        showMessage("Сообщение","Сервис перезагружен!", info);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type); //как выглядит окошко с сообщением
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

    public void setWebUserToJson(){

    }

    public void AddNewWebUser(){
        if((!selectedUser.getLogin().equals(""))&(!selectedUser.getPass().equals(""))) {
            System.out.println("addnewwebuser");
            webUsers.add(new OrthancWebUser(selectedUser.getLogin(),selectedUser.getPass()));
            PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            PrimeFaces.current().ajax().update("form:dt-users");
        }
    }

    public void openNew() {
        System.out.println("open new");
        selectedUser = new OrthancWebUser("","");
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

    public List<OrthancWebUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<OrthancWebUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }


}

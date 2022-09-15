package ru.bitServer.dicom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.util.LogTool;

public class JsonSettings {

    private JsonObject dicomNode = new JsonObject();
    private JsonObject orthancPeer = new JsonObject();
    private JsonObject contentType = new JsonObject();
    private JsonObject dictionary = new JsonObject();
   // private JsonArray luaFolder = new JsonArray();
    private JsonArray pluginsFolder = new JsonArray();
    private JsonObject users = new JsonObject();
    private JsonObject userMetadata = new JsonObject();
    private String orthancName;
    private String storageDirectory;
    private String indexDirectory;
    private boolean StorageCompression;
    private int MaximumStorageSize;
    private int MaximumPatientCount;
    private boolean HttpServerEnabled;
    private int HttpPort;
    private boolean HttpDescribeErrors;
    private boolean HttpCompressionEnabled;
    private boolean DicomServerEnabled;
    private String DicomAet;
    private boolean DicomCheckCalledAet;
    private int DicomPort;
    private String DefaultEncoding;
    private boolean DeflatedTransferSyntaxAccepted;
    private boolean JpegTransferSyntaxAccepted;
    private boolean Jpeg2000TransferSyntaxAccepted;
    private boolean JpegLosslessTransferSyntaxAccepted;
    private boolean JpipTransferSyntaxAccepted;
    private boolean Mpeg2TransferSyntaxAccepted;
    private boolean RleTransferSyntaxAccepted;
    private boolean UnknownSopClassAccepted;
    private int DicomScpTimeout;
    private boolean RemoteAccessAllowed;
    private boolean SslEnabled;
    private String SslCertificate;
    private boolean AuthenticationEnabled;
    private int DicomScuTimeout;
    private String HttpProxy;
    private int HttpTimeout;
    private boolean HttpsVerifyPeers;
    private String HttpsCACertificates;
    private String locale;
    private int StableAge;
    private boolean StrictAetComparison;
    private boolean StoreMD5ForAttachments;
    private int LimitFindResults;
    private int LimitFindInstances;
    private int LimitJobs;
    private boolean LogExportedResources;
    private boolean KeepAlive;
    private boolean StoreDicom;
    private int DicomAssociationCloseDelay;
    private int QueryRetrieveSize;
    private boolean CaseSensitivePN;
    private boolean LoadPrivateDictionary;
    private boolean dicomAlwaysAllowEcho;
    private boolean DicomAlwaysStore;
    private boolean CheckModalityHost;
    private boolean SynchronousCMove;
    private int JobsHistorySize;
    private int ConcurrentJobs;
    private boolean dicomModalitiesInDb;
    private boolean orthancPeerInDb;
    private boolean overwriteInstances;
    private int mediaArchiveSize;
    private String storageAccessOnFind;
    private boolean httpVerbose;
    private boolean tcpNoDelay;
    private int httpThreadsCount;
    private boolean saveJobs;
    private boolean metricsEnabled;
    private boolean AllowFindSopClassesInStudy;
    private JsonObject workLists = new JsonObject();

    public JsonObject getDicomNode() {
        return dicomNode;
    }

    public void setDicomNode(JsonObject dicomNode) {
        this.dicomNode = dicomNode;
    }

    public JsonObject getOrthancPeer() {
        return orthancPeer;
    }

    public void setOrthancPeer(JsonObject orthancPeer) {
        this.orthancPeer = orthancPeer;
    }

    public JsonObject getContentType() {
        return contentType;
    }

    public void setContentType(JsonObject contentType) {
        this.contentType = contentType;
    }

    public JsonObject getDictionary() {
        return dictionary;
    }

    public void setDictionary(JsonObject dictionary) {
        this.dictionary = dictionary;
    }

//    public JsonArray getLuaFolder() {
//        return luaFolder;
//    }
//
//    public void setLuaFolder(JsonArray luaFolder) {
//        this.luaFolder = luaFolder;
//    }

    public JsonObject getUsers() {
        return users;
    }

    public void setUsers(JsonObject users) {
        this.users = users;
    }

    public JsonObject getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(JsonObject userMetadata) {
        this.userMetadata = userMetadata;
    }

    public String getOrthancName() {
        return orthancName;
    }

    public void setOrthancName(String orthancName) {
        this.orthancName = orthancName;
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

    public JsonObject getWorkLists() {
        return workLists;
    }

    public void setWorkLists(JsonObject workLists) {
        this.workLists = workLists;
    }

    public JsonArray getPluginsFolder() {
        return pluginsFolder;
    }

    public void setPluginsFolder(JsonArray pluginsFolder) {
        this.pluginsFolder = pluginsFolder;
    }

    public JsonSettings(String data) {
        JsonParser parser = new JsonParser();
        JsonObject orthancJson=new JsonObject();
        try {
            orthancJson = parser.parse(data).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().warn("Error parse json JsonSetings: "+e.getMessage());
        }
        if (orthancJson.has("Name"))                           orthancName=orthancJson.get("Name").getAsString();
        if (orthancJson.has("StorageDirectory"))               storageDirectory=orthancJson.get("StorageDirectory").getAsString();
        if (orthancJson.has("IndexDirectory"))                 indexDirectory=orthancJson.get("IndexDirectory").getAsString();
        if (orthancJson.has("StorageCompression"))             StorageCompression=orthancJson.get("StorageCompression").getAsBoolean();
        if (orthancJson.has("MaximumStorageSize"))             MaximumStorageSize=Integer.parseInt(orthancJson.get("MaximumStorageSize").getAsString());
        if (orthancJson.has("MaximumPatientCount"))            MaximumPatientCount=Integer.parseInt(orthancJson.get("MaximumPatientCount").getAsString());
        if (orthancJson.has("HttpServerEnabled"))              HttpServerEnabled=orthancJson.get("HttpServerEnabled").getAsBoolean();
        if (orthancJson.has("HttpPort"))                       HttpPort=Integer.parseInt(orthancJson.get("HttpPort").getAsString());
        if (orthancJson.has("HttpDescribeErrors"))             HttpDescribeErrors=orthancJson.get("HttpDescribeErrors").getAsBoolean();
        if (orthancJson.has("HttpCompressionEnabled"))         HttpCompressionEnabled=orthancJson.get("HttpCompressionEnabled").getAsBoolean();
        if (orthancJson.has("DicomServerEnabled"))             DicomServerEnabled=orthancJson.get("DicomServerEnabled").getAsBoolean();
        if (orthancJson.has("DicomAet"))                       DicomAet=orthancJson.get("DicomAet").getAsString();
        if (orthancJson.has("DicomCheckCalledAet"))            DicomCheckCalledAet=orthancJson.get("DicomCheckCalledAet").getAsBoolean();
        if (orthancJson.has("DicomPort"))                      DicomPort=orthancJson.get("DicomPort").getAsInt();
        if (orthancJson.has("DefaultEncoding"))                DefaultEncoding=orthancJson.get("DefaultEncoding").getAsString();
        if (orthancJson.has("DeflatedTransferSyntaxAccepted")) DeflatedTransferSyntaxAccepted=orthancJson.get("DeflatedTransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("JpegTransferSyntaxAccepted"))    JpegTransferSyntaxAccepted=orthancJson.get("JpegTransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("Jpeg2000TransferSyntaxAccepted")) Jpeg2000TransferSyntaxAccepted=orthancJson.get("Jpeg2000TransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("JpegLosslessTransferSyntaxAccepted")) JpegLosslessTransferSyntaxAccepted=orthancJson.get("JpegLosslessTransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("JpipTransferSyntaxAccepted"))    JpipTransferSyntaxAccepted=orthancJson.get("JpipTransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("Mpeg2TransferSyntaxAccepted"))   Mpeg2TransferSyntaxAccepted=orthancJson.get("Mpeg2TransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("RleTransferSyntaxAccepted"))     RleTransferSyntaxAccepted=orthancJson.get("RleTransferSyntaxAccepted").getAsBoolean();
        if (orthancJson.has("UnknownSopClassAccepted"))       UnknownSopClassAccepted=orthancJson.get("UnknownSopClassAccepted").getAsBoolean();
        if (orthancJson.has("DicomScpTimeout"))               DicomScpTimeout=orthancJson.get("DicomScpTimeout").getAsInt();
        if (orthancJson.has("RemoteAccessAllowed"))           RemoteAccessAllowed=orthancJson.get("RemoteAccessAllowed").getAsBoolean();
        if (orthancJson.has("SslEnabled"))                    SslEnabled=orthancJson.get("SslEnabled").getAsBoolean();
        if (orthancJson.has("SslCertificate"))                SslCertificate=orthancJson.get("SslCertificate").getAsString();
        if (orthancJson.has("AuthenticationEnabled"))         AuthenticationEnabled=orthancJson.get("AuthenticationEnabled").getAsBoolean();
        if (orthancJson.has("DicomScuTimeout"))               DicomScuTimeout=orthancJson.get("DicomScuTimeout").getAsInt();
        if (orthancJson.has("HttpProxy"))                     HttpProxy=orthancJson.get("HttpProxy").getAsString();
        if (orthancJson.has("HttpTimeout"))                   HttpTimeout=orthancJson.get("HttpTimeout").getAsInt();
        if (orthancJson.has("HttpsVerifyPeers"))              HttpsVerifyPeers=orthancJson.get("HttpsVerifyPeers").getAsBoolean();
        if (orthancJson.has("HttpsCACertificates"))           HttpsCACertificates=orthancJson.get("HttpsCACertificates").getAsString();
        if (orthancJson.has("StableAge"))                     StableAge=orthancJson.get("StableAge").getAsInt();
        if (orthancJson.has("StrictAetComparison"))           StrictAetComparison=orthancJson.get("StrictAetComparison").getAsBoolean();
        if (orthancJson.has("StoreMD5ForAttachments"))        StoreMD5ForAttachments=orthancJson.get("StoreMD5ForAttachments").getAsBoolean();
        if (orthancJson.has("LimitFindResults"))              LimitFindResults=orthancJson.get("LimitFindResults").getAsInt();
        if (orthancJson.has("LimitFindInstances"))            LimitFindInstances=orthancJson.get("LimitFindInstances").getAsInt();
        if (orthancJson.has("LimitJobs"))                     LimitJobs=orthancJson.get("LimitJobs").getAsInt();
        if (orthancJson.has("LogExportedResources"))          LogExportedResources=orthancJson.get("LogExportedResources").getAsBoolean();
        if (orthancJson.has("KeepAlive"))                     KeepAlive=orthancJson.get("KeepAlive").getAsBoolean();
        if (orthancJson.has("StoreDicom"))                    StoreDicom=orthancJson.get("StoreDicom").getAsBoolean();
        if (orthancJson.has("DicomAssociationCloseDelay"))    DicomAssociationCloseDelay=orthancJson.get("DicomAssociationCloseDelay").getAsInt();
        if (orthancJson.has("QueryRetrieveSize"))             QueryRetrieveSize=orthancJson.get("QueryRetrieveSize").getAsInt();
        if (orthancJson.has("CaseSensitivePN"))               CaseSensitivePN=orthancJson.get("CaseSensitivePN").getAsBoolean();
        if (orthancJson.has("AllowFindSopClassesInStudy"))    AllowFindSopClassesInStudy=orthancJson.get("AllowFindSopClassesInStudy").getAsBoolean();
        if (orthancJson.has("LoadPrivateDictionary"))         LoadPrivateDictionary=orthancJson.get("LoadPrivateDictionary").getAsBoolean();
        if (orthancJson.has("DicomCheckModalityHost"))        CheckModalityHost=orthancJson.get("DicomCheckModalityHost").getAsBoolean();
        if (orthancJson.has("DicomAlwaysAllowStore"))         DicomAlwaysStore=orthancJson.get("DicomAlwaysAllowStore").getAsBoolean();
        if (orthancJson.has("DicomAlwaysAllowEcho"))          dicomAlwaysAllowEcho=orthancJson.get("DicomAlwaysAllowEcho").getAsBoolean();
        if (orthancJson.has("SynchronousCMove"))              SynchronousCMove=orthancJson.get("SynchronousCMove").getAsBoolean();
        if (orthancJson.has("JobsHistorySize"))               JobsHistorySize=orthancJson.get("JobsHistorySize").getAsInt();
        if (orthancJson.has("ConcurrentJobs"))                ConcurrentJobs=orthancJson.get("ConcurrentJobs").getAsInt();
        if (orthancJson.has("DicomModalitiesInDatabase"))     dicomModalitiesInDb=orthancJson.get("DicomModalitiesInDatabase").getAsBoolean();
        if (orthancJson.has("OrthancPeersInDatabase"))        orthancPeerInDb=orthancJson.get("OrthancPeersInDatabase").getAsBoolean();
        if (orthancJson.has("OverwriteInstances"))            overwriteInstances=orthancJson.get("OverwriteInstances").getAsBoolean();
        if (orthancJson.has("MediaArchiveSize"))              mediaArchiveSize=orthancJson.get("MediaArchiveSize").getAsInt();
        if (orthancJson.has("StorageAccessOnFind"))           storageAccessOnFind=orthancJson.get("StorageAccessOnFind").getAsString();
        if (orthancJson.has("Locale"))                        locale=orthancJson.get("Locale").getAsString();
        if (orthancJson.has("HttpVerbose"))                   httpVerbose=orthancJson.get("HttpVerbose").getAsBoolean();
        if (orthancJson.has("TcpNoDelay"))                    tcpNoDelay=orthancJson.get("TcpNoDelay").getAsBoolean();
        if (orthancJson.has("HttpThreadsCount"))              httpThreadsCount = orthancJson.get("HttpThreadsCount").getAsInt();
        if (orthancJson.has("SaveJobs"))                      saveJobs = orthancJson.get("SaveJobs").getAsBoolean();
        if (orthancJson.has("MetricsEnabled"))                metricsEnabled =orthancJson.get("MetricsEnabled").getAsBoolean();
        if (orthancJson.has("DicomModalities"))               dicomNode = orthancJson.get("DicomModalities").getAsJsonObject();
        if (orthancJson.has("RegisteredUsers"))               users = orthancJson.get("RegisteredUsers").getAsJsonObject();
     //   if (orthancJson.has("LuaScripts")) luaFolder= orthancJson.get("LuaScripts").getAsJsonArray();
        if (orthancJson.has("Plugins"))                       pluginsFolder = orthancJson.get("Plugins").getAsJsonArray();
        if (orthancJson.has("Worklists"))                     workLists = orthancJson.get("Worklists").getAsJsonObject();
        if (orthancJson.has("UserMetadata"))                  userMetadata = orthancJson.get("UserMetadata").getAsJsonObject();
        if (orthancJson.has("Dictionary"))                    dictionary = orthancJson.get("Dictionary").getAsJsonObject();
        if (orthancJson.has("UserContentType"))               contentType = orthancJson.get("UserContentType").getAsJsonObject();
        if (orthancJson.has("OrthancPeers")) {                orthancPeer = orthancJson.get("OrthancPeers").getAsJsonObject();}
    }
}

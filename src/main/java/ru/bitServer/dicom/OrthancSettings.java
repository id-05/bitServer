package ru.bitServer.dicom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.dao.DataAction;
import ru.bitServer.util.OrthancRestApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.bitServer.beans.MainBean.mainServer;

public class OrthancSettings implements DataAction {

    public String ServerName;
    public JsonObject dicomNode;
    public JsonObject orthancPeer = new JsonObject();
    public JsonObject contentType = new JsonObject();
    public JsonObject dictionary = new JsonObject();
    //public JsonArray luaFolder = new JsonArray();
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

    public String getDicomAet() {
        return DicomAet;
    }

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

    OrthancRestApi connection;

    public OrthancSettings(OrthancRestApi connection)  {
        this.connection = connection;

        String str = getJsonFromFile().toString();

        json = new JsonSettings(str);
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
        //luaFolder = json.getLuaFolder();
    }

    public void saveSettings() {
        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("Name", ServerName);
        jsonOb.addProperty("StorageDirectory", storageDirectory);
        jsonOb.addProperty("IndexDirectory", indexDirectory);
        jsonOb.addProperty("StorageCompression", StorageCompression);
        jsonOb.addProperty("MaximumStorageSize", MaximumStorageSize);
        jsonOb.addProperty("MaximumPatientCount", MaximumPatientCount);
        jsonOb.add("Plugins",pluginsFolder);
       // jsonOb.add("LuaScripts",luaFolder);
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
        StringBuilder stringBuilder = connection.makePostConnectionAndStringBuilder("/tools/execute-script",urlParameters);
    }

    public List<DicomModaliti> getDicomModalitis(){
        List<DicomModaliti> bufList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject orthancJson = parser.parse(dicomNode.toString()).getAsJsonObject();
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

    public String ModifyStr(String str){
        String result;
        String buf0;
        String buf;
        String buf2;
        buf0 = str.replace("\\","\\\\");
        buf = buf0.replace("/","\\/");
        buf2 = buf.replace("\"","\\\"");
        result = buf2.replace(",",",\\n");
        return result;
    }
}

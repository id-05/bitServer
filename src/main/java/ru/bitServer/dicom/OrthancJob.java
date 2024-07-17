package ru.bitServer.dicom;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;

import java.text.SimpleDateFormat;

public class OrthancJob implements UserDao {

    private String completionTime;
    private String description;
    private Integer failedInstancesCount;
    private Integer instancesCount;
    private String localAet;
    private String parentResources;
    private String remoteAet;
    private String creationTime;
    private String effectiveRuntime;
    private Integer errorCode;
    private String errorDescription;
    private String errorDetails;
    private String identificator;
    private Integer priority;
    private Integer progress;
    private String state;
    private String timestamp;
    private String type;
    private String statusStyle;
    private BitServerStudy bitServerSudy;

    public void setStatusStyle(String statusStyle) {
        this.statusStyle = statusStyle;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public BitServerStudy getBitServerSudy() {
        return bitServerSudy;
    }

    public void setBitServerSudy(BitServerStudy bitServerSudy) {
        this.bitServerSudy = bitServerSudy;
    }

    public void setCompletionTime(String completionTime) {
        completionTime = completionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public Integer getFailedInstancesCount() {
        return failedInstancesCount;
    }

    public void setFailedInstancesCount(Integer failedInstancesCount) {
        failedInstancesCount = failedInstancesCount;
    }

    public Integer getInstancesCount() {
        return instancesCount;
    }

    public void setInstancesCount(Integer instancesCount) {
        instancesCount = instancesCount;
    }

    public String getLocalAet() {
        return localAet;
    }

    public void setLocalAet(String localAet) {
        localAet = localAet;
    }

    public String getParentResources() {
        return parentResources;
    }

    public void setParentResources(String parentResources) {
        parentResources = parentResources;
    }

    public String getRemoteAet() {
        return remoteAet;
    }

    public void setRemoteAet(String remoteAet) {
        remoteAet = remoteAet;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        creationTime = creationTime;
    }

    public String getEffectiveRuntime() {
        String result ="--:--";
        if(effectiveRuntime!=null)
            {result = effectiveRuntime.substring(0,5);}
        return result;
    }

    public void setEffectiveRuntime(String effectiveRuntime) {
        effectiveRuntime = effectiveRuntime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        errorDescription = errorDescription;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        errorDetails = errorDetails;
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        priority = priority;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        progress = progress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        state = state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    public OrthancJob(){

    }

    public String getStatusStyle() {
        switch (state){
            case "0": this.statusStyle = "noresult";
                break;
            case "3":
                this.statusStyle = "inprocess";
                break;
            case "Success": this.statusStyle = "yesresult";
                break;
            default: break;
        }
        return statusStyle;
    }

    public OrthancJob(String data){
        JsonParser parser = new JsonParser();
        JsonObject orthancJson = new JsonObject();
        try {
            orthancJson = parser.parse(data).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().error("Error parse json JsonSetings: "+e.getMessage());
        }
        if (orthancJson.has("CompletionTime"))       this.completionTime = orthancJson.get("CompletionTime").getAsString();
        if (orthancJson.has("CreationTime"))         this.creationTime = orthancJson.get("CreationTime").getAsString();
        if (orthancJson.has("EffectiveRuntime"))     this.effectiveRuntime = orthancJson.get("EffectiveRuntime").getAsString();
        if (orthancJson.has("ErrorCode"))            this.errorCode = orthancJson.get("ErrorCode").getAsInt();
        if (orthancJson.has("ErrorDescription"))     this.errorDescription = orthancJson.get("ErrorDescription").getAsString();
        if (orthancJson.has("ErrorDetails"))         this.errorDetails = orthancJson.get("ErrorDetails").getAsString();
        if (orthancJson.has("ID"))                   this.identificator = orthancJson.get("ID").getAsString();
        if (orthancJson.has("Priority"))             this.priority = orthancJson.get("Priority").getAsInt();
        if (orthancJson.has("Progress"))             this.progress = orthancJson.get("Progress").getAsInt();
        if (orthancJson.has("State"))                this.state = orthancJson.get("State").getAsString();
        if (orthancJson.has("Timestamp"))            this.timestamp = orthancJson.get("Timestamp").getAsString();
        if (orthancJson.has("Type"))                 this.type = orthancJson.get("Type").getAsString();
        JsonObject bufJson = null;
        if (orthancJson.has("Content"))              bufJson = orthancJson.get("Content").getAsJsonObject();
        if (bufJson.has("Description"))              this.description = bufJson.get("Description").getAsString();
        if (bufJson.has("FailedInstancesCount"))     this.failedInstancesCount = bufJson.get("FailedInstancesCount").getAsInt();
        if (bufJson.has("InstancesCount"))           this.instancesCount = bufJson.get("InstancesCount").getAsInt();
        if (bufJson.has("LocalAet"))                 this.localAet = bufJson.get("LocalAet").getAsString();
        if (bufJson.has("ParentResources"))          this.parentResources = bufJson.get("ParentResources").getAsString();
        if (bufJson.has("RemoteAet"))                this.remoteAet = bufJson.get("RemoteAet").getAsString();
        if (bufJson.has("Description"))              this.description = bufJson.get("Description").getAsString();

        this.bitServerSudy = getBitServerStudyById(parentResources);

    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

//    {
//        "CompletionTime" : "20240712T020702.670306",
//            "Content" :    {
//        "Description" : "REST API",
//                "FailedInstancesCount" : 0,
//                "InstancesCount" : 493,
//                "LocalAet" : "ORTHANC",
//                "ParentResources" :       [         "170636df-fe756aaf-39d40fd3-75e2326c-76f6250c"      ],
//                "RemoteAet" : "DS4TB"
//    },
//
//        "CreationTime" : "20240712T020615.146749",
//            "EffectiveRuntime" : 47.523000000000003,
//            "ErrorCode" : 0,
//            "ErrorDescription" : "Success",
//            "ErrorDetails" : "",
//            "ID" : "62d7a15e-ced2-4ae9-8559-6646157b3183",
//            "Priority" : 0,
//            "Progress" : 100,
//            "State" : "Success",
//            "Timestamp" : "20240712T041110.820017",
//            "Type" : "DicomModalityStore"
//    }
}

package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.service.DicomRouteRule;
import ru.bitServer.service.DicomRuleParser;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "dicomrouteBean")
@ViewScoped
public class DicomRouteBean implements UserDao {

    BitServerUser currentUser;
    String currentUserId;
    ArrayList<DicomRouteRule> rules = new ArrayList<>();
    StringBuilder luascriptFile;
    String luascripttextFile;
    List<DicomModaliti> modalities;
    List<String> modalitiesName;
    DicomRouteRule selectedRule;

    public List<String> getModalitiesName() {
        modalitiesName = new ArrayList<>();
        for(DicomModaliti bufmodality:modalities){
            modalitiesName.add(bufmodality.getDicomName());
        }
        return modalitiesName;
    }

    public void setModalitiesName(List<String> modalitiesName) {
        this.modalitiesName = modalitiesName;
    }

    public DicomRouteRule getSelectedRule() {
        return selectedRule;
    }

    public void setSelectedRule(DicomRouteRule selectedRule) {
        this.selectedRule = selectedRule;
    }

    public String getLuascripttextFile() {
        return luascripttextFile;
    }

    public void setLuascripttextFile(String luascripttextFile) {
        this.luascripttextFile = luascripttextFile;
    }

    public ArrayList<DicomRouteRule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<DicomRouteRule> rules) {
        this.rules = rules;
    }

    OrthancRestApi connection;
    OrthancSettings orthancSettings;

    @PostConstruct
    public void init()  {
        selectedRule = new DicomRouteRule();
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();
        luascriptFile = getFileFromResName("luascriptpathfile");
        luascripttextFile = luascriptFile.toString();
        DicomRuleParser ruleParcer = new DicomRuleParser(luascriptFile);
        rules = ruleParcer.getRulesList();
    }

    public void resetOrthanc() {
        showMessage("Внимание","Сервис Orthanc, будет перезагружен!",FacesMessage.SEVERITY_INFO);
        connection.makePostConnectionAndStringBuilder("/tools/reset","" );
    }

    public void addNewRule(){
        selectedRule.setDeleteAfterRoute(false);
        rules.add(selectedRule);
        PrimeFaces.current().executeScript("PF('manageRule').hide()");
        PrimeFaces.current().ajax().update(":dicomroute:tabview1:dt-rules");
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new route "+selectedRule.getTag()+"/"+selectedRule.getTagValue());
    }

    public void deleteRuleFromList(){
        rules.remove(selectedRule);
        PrimeFaces.current().ajax().update(":dicomroute:tabview1:dt-rules");
    }

    public void initNewRule(){
        selectedRule = new DicomRouteRule();
    }

    public void onInputTextChange(){

    }

    public void onTabChange(){
        init();
        PrimeFaces.current().ajax().update(":dicomroute:tabview1");
    }

    public void saveSettingsCustomMode(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(getBitServerResource("luascriptpathfile").getRvalue()))
        {
            byte[] buffer = luascripttextFile.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().warn("Error saveSettingsCustomeMode DicomrouteBean: "+e.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения, перезагрузите сервис!",FacesMessage.SEVERITY_INFO);
    }

    public void saveSettings(){
        StringBuilder bufStringBuilder = new StringBuilder();
        bufStringBuilder.append("function OnStoredInstance(instanceId, tags, metadata)\n");

        for(DicomRouteRule bufRule:rules){
            if(bufRule.getTag().equals("all")){
                bufStringBuilder.append("   SendToModality(instanceId, '").append(bufRule.getNameRemoteModality()).append("')").append("\n");
            }else {
                bufStringBuilder.append("   if tags.").append(bufRule.getTag()).append(" == '").append(bufRule.getTagValue()).append("' then").append("\n");
                bufStringBuilder.append("       SendToModality(instanceId, '").append(bufRule.getNameRemoteModality()).append("')").append("\n");
                bufStringBuilder.append("   end\n");
            }
        }

        bufStringBuilder.append("end\n");

        try(FileOutputStream fileOutputStream = new FileOutputStream(getBitServerResource("luascriptpathfile").getRvalue()))
        {
            byte[] buffer = bufStringBuilder.toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
            LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" save dicomroute");
        }
        catch(IOException e){
            LogTool.getLogger().warn("Error saveSettings DicomrouteBean: "+e.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения, перезагрузите сервис!",FacesMessage.SEVERITY_INFO);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

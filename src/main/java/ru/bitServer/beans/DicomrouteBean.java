package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Usergroup;
import ru.bitServer.dao.Users;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.dicom.OrthancSettings;
import ru.bitServer.service.DicomrouteRule;
import ru.bitServer.service.DicomruleParser;
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


@ManagedBean(name = "dicomrouteBean", eager = false)
@ViewScoped
public class DicomrouteBean implements UserDao {

    Users currentUser;
    String currentUserId;
    String pathToFile;
    public ArrayList<DicomrouteRule> rules = new ArrayList<>();
    public StringBuilder luascriptFile;
    public String luascripttextFile;
    List<DicomModaliti> modalities;
    List<DicomModaliti> selectedModalities;
    List<String> modalitiesName;
    DicomModaliti selectedModaliti;
    public DicomrouteRule selectedRule;

    public List<String> getModalitiesName() {
        modalitiesName = new ArrayList<>();
        for(DicomModaliti bufmodaliti:modalities){
            modalitiesName.add(bufmodaliti.getDicomname());
        }
        return modalitiesName;
    }

    public void setModalitiesName(List<String> modalitiesName) {
        this.modalitiesName = modalitiesName;
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

    public DicomrouteRule getSelectedRule() {
        return selectedRule;
    }

    public void setSelectedRule(DicomrouteRule selectedRule) {
        this.selectedRule = selectedRule;
    }

    public String getLuascripttextFile() {
        return luascripttextFile;
    }

    public void setLuascripttextFile(String luascripttextFile) {
        this.luascripttextFile = luascripttextFile;
    }

    public ArrayList<DicomrouteRule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<DicomrouteRule> rules) {
        this.rules = rules;
    }

    OrthancRestApi connection;
    OrthancSettings orthancSettings;

    @PostConstruct
    public void init() {
        System.out.println("dicomrouteBean");
        selectedRule = new DicomrouteRule();

        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);

        connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
        orthancSettings = new OrthancSettings(connection);
        modalities = orthancSettings.getDicomModalitis();


//        String buf = "DFGDFGDFGDFGDFGDG";//orthancSettings.getLuaFolder().toString();
//        pathToFile = buf.substring(2,buf.length()-2);

        pathToFile = "D://route.lua";//"/usr/share/orthanc/lua/route.lua";

        luascriptFile = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                luascriptFile.append((char) c);
            }
        } catch (Exception e) {
            System.out.println("error of read file = "+e.getMessage());
        }
        luascripttextFile = luascriptFile.toString();
        DicomruleParser ruleParcer = new DicomruleParser(luascriptFile);
        rules = ruleParcer.getRulesList();
    }

    public void resetOrthanc() {
        showMessage("Внимание","Сервис Orthanc, будет перезагружен!",FacesMessage.SEVERITY_INFO);
        StringBuilder sb = connection.makePostConnectionAndStringBuilder("/tools/reset","" );
    }

    public void addNewRule(){
        //selectedRule.setNameRemoteModality(selectedModaliti.getDicomname());
        selectedRule.setDeleteAfteRoute(false);
        rules.add(selectedRule);
        PrimeFaces.current().executeScript("PF('manageRule').hide()");
        PrimeFaces.current().ajax().update(":dicomroute:tabview1:dt-rules");
    }

    public void deleteRuleFromList(){
        rules.remove(selectedRule);
        PrimeFaces.current().ajax().update(":dicomroute:tabview1:dt-rules");
    }

    public void initNewRule(){
        selectedRule = new DicomrouteRule();
    }

    public void onInputTextChange(){

    }

    public void saveSettingsCustomMode(){
        System.out.println("pathToFile = " + pathToFile);
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = luascripttextFile.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения, перезагрузите сервис!",FacesMessage.SEVERITY_INFO);
    }

    public void saveSettings(){
        StringBuilder bufStringBuilder = new StringBuilder();
        bufStringBuilder.append("function OnStoredInstance(instanceId, tags, metadata)\n");

        for(DicomrouteRule bufRule:rules){
            bufStringBuilder.append("SendToModality(instanceId, '").append(bufRule.getNameRemoteModality()).append("')").append("\n");
        }

        bufStringBuilder.append("end\n");

        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = bufStringBuilder.toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения, перезагрузите сервис!",FacesMessage.SEVERITY_INFO);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.DicomrouteRule;
import ru.bitServer.service.WorkListItem;
import ru.bitServer.util.DeleteWorkListFile;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static ru.bitServer.beans.AutoriseBean.showMessage;


@ManagedBean(name = "worklistBean")
@ViewScoped
public class WorkListBean implements UserDao {

    BitServerUser currentUser;
    String currentUserId;
    String pathToFile;
    StringBuilder worklistFile;
    String worklisttextFile;
    ArrayList<WorkListItem> items = new ArrayList<>();
    WorkListItem selectedItem;

    public WorkListItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(WorkListItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public ArrayList<WorkListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<WorkListItem> items) {
        this.items = items;
    }

    public String getWorklisttextFile() {
        return worklisttextFile;
    }

    public void setWorklisttextFile(String worklisttextFile) {
        this.worklisttextFile = worklisttextFile;
    }

    @PostConstruct
    public void init() {
        selectedItem = new WorkListItem();
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);

        BitServerResources bufResources = getBitServerResource("worklistpathfile");
        pathToFile = bufResources.getRvalue();

        worklistFile = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                worklistFile.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().warn("Error read luascript file: "+e.getMessage());
        }

        worklisttextFile = worklistFile.toString();
        //DicomruleParser ruleParcer = new DicomruleParser(luascriptFile);
        //rules = ruleParcer.getRulesList();
    }

    public void onTabChange(){
        init();
        PrimeFaces.current().ajax().update(":worklistmanager:tabview1");
    }

    public void initNewItem(){
        selectedItem = new WorkListItem();
    }

    public void saveSampleFile(){
        StringBuilder bufStringBuilder = new StringBuilder();
//        bufStringBuilder.append("function OnStoredInstance(instanceId, tags, metadata)\n");

//        for(DicomrouteRule bufRule:rules){
//            if(bufRule.getTag().equals("all")){
//                bufStringBuilder.append("   SendToModality(instanceId, '").append(bufRule.getNameRemoteModality()).append("')").append("\n");
//            }else {
//                bufStringBuilder.append("   if tags.").append(bufRule.getTag()).append(" == '").append(bufRule.getTagValue()).append("' then").append("\n");
//                bufStringBuilder.append("       SendToModality(instanceId, '").append(bufRule.getNameRemoteModality()).append("')").append("\n");
//                bufStringBuilder.append("   end\n");
//            }
//        }

//        bufStringBuilder.append("end\n");

        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = bufStringBuilder.toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
            LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" save wl-item");
        }
        catch(IOException e){
            LogTool.getLogger().warn("Error saveSettings DicomrouteBean: "+e.getMessage());
        }
        showMessage("Внимание","Файл изменён!", FacesMessage.SEVERITY_INFO);
    }

    public void deleteItemFromList(){
        items.remove(selectedItem);
        PrimeFaces.current().ajax().update(":worklistmanager:tabview1:dt-items");
    }

    public void addNewItem(){
        items.add(selectedItem);
        PrimeFaces.current().executeScript("PF('manageItem').hide()");
        PrimeFaces.current().ajax().update(":worklistmanager:tabview1:dt-items");
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" add new wl item "+selectedItem.getDicomTag()+"/"+selectedItem.getVR()+"/"+selectedItem.getValue());
    }

    public void saveSettingsCustomMode(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = worklisttextFile.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().error("Error saveSettingsCustomeMode WorkListBean: "+e.getMessage());
        }
        showMessage("Внимание","Изменения сохранены!",FacesMessage.SEVERITY_INFO);
    }

    public void onInputTextChange(){

    }

    public void deleteDirectory(){
        System.out.println("click delete");
        DeleteWorkListFile delWorkList = new DeleteWorkListFile();
        delWorkList.setCondition(true);
        delWorkList.run();
        showMessage("Внимание","Директория wl-файлов очищена!",FacesMessage.SEVERITY_INFO);
    }
}

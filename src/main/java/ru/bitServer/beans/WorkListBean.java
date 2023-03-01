package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.WorkListItem;
import ru.bitServer.service.WorkListItemParser;
import ru.bitServer.util.DeleteWorkListFile;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import static ru.bitServer.beans.AuthoriseBean.showMessage;

@ManagedBean(name = "worklistBean")
@ViewScoped
public class WorkListBean implements UserDao {

    BitServerUser currentUser;
    String currentUserId;
    StringBuilder worklistFile;
    String worklisttextFile;
    ArrayList<WorkListItem> items = new ArrayList<>();
    WorkListItem selectedItem;
    WorkListItemParser itemParcer;

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
        worklistFile = getFileFromResName("worklistsamplefile");
        worklisttextFile = worklistFile.toString();
        itemParcer = new WorkListItemParser(worklisttextFile);
        items = itemParcer.getItemList();
    }

    public void onTabChange(){
        init();
        PrimeFaces.current().ajax().update(":worklistmanager:tabview1");
    }

    public void initNewItem(){
        selectedItem = new WorkListItem();
    }

    public void saveSampleFile(boolean mode){
        File file = new File(getBitServerResource("worklistsamplefile").getRvalue());
        FileOutputStream is;
        try{    is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            if(mode) {
                w.write(worklisttextFile);
            }else{
                StringBuilder bufStringBuilder = new StringBuilder();
                for(WorkListItem bufItem:items){
                    bufStringBuilder.append(bufItem.getDicomTag()).append(" ").append(bufItem.getVR()).append(" ").append(bufItem.getValue()).append("\n");
                }
                w.write(bufStringBuilder.toString());
            }
            w.close();
        } catch (Exception e) {
            LogTool.getLogger().error("Error create wl-file: " + e.getMessage());
        }
        showMessage("Внимание","Шаблон сохранён!",FacesMessage.SEVERITY_INFO);
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

    public void onInputTextChange(){

    }

    public void deleteDirectory(){
        System.out.println("click delete");
        DeleteWorkListFile delWorkList = new DeleteWorkListFile();
        delWorkList.setCondition(true);
        delWorkList.run();
        showMessage("Внимание","Директория wl-файлов очищена!",FacesMessage.SEVERITY_INFO);
    }

    public void createWorkListFile(boolean mode){
        String shortfilename = getBitServerResource("WorkListPath").getRvalue() + new Date().getTime();
        boolean deleteBufFileAfter = getBitServerResource("deleteBufFileAfter").getRvalue().equals("true");
        String filename = shortfilename+".txt";

        File file = new File(filename);
        FileOutputStream is;
        try{
            is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
                if(mode) {
                    w.write(worklisttextFile);
                }else{
                    StringBuilder bufStringBuilder = new StringBuilder();
                    for(WorkListItem bufItem:items){
                        bufStringBuilder.append(bufItem.getDicomTag()).append(" ").append(bufItem.getVR()).append(" ").append(bufItem.getValue()).append("\n");
                    }
                    w.write(bufStringBuilder.toString());
                }
            w.close();
        } catch (Exception e) {
            LogTool.getLogger().error("Error create wl-file: " + e.getMessage());
        }

        try {
            Process proc = Runtime.getRuntime().exec("dump2dcm "+filename+" "+shortfilename+".wl");
            LogTool.getLogger().info("new file: "+new Date()+": "+shortfilename);
            proc.waitFor();

            if(deleteBufFileAfter){
                if(!file.delete()){
                    LogTool.getLogger().error("Error delete wl-file, maybe tomcat not have rules");
                }
            }

        }catch (Exception e){
            LogTool.getLogger().error("Error create wl-file: "+e.getMessage());
        }
        showMessage("Внимание","wl-файл создан!",FacesMessage.SEVERITY_INFO);
    }

}

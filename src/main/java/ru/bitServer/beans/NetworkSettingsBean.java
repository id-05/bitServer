package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@ManagedBean(name = "networkSettingsBean", eager = false)
@ViewScoped
public class NetworkSettingsBean implements UserDao {

    public Users currentUser;
    public String currentUserId;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public String pathToFile;
    public ArrayList<NetworkAdapter> adapters = new ArrayList<>();
    public NetworkAdapter selectedAdapter;
    public boolean advancedmode;
    public String configFileText;
    public StringBuilder newtworkSettingsFile;

    public String getConfigFileText() {
        return configFileText;
    }

    public void setConfigFileText(String configFileText) {
        this.configFileText = configFileText;
    }

    public boolean isAdvancedmode() {
        return advancedmode;
    }

    public void setAdvancedmode(boolean advancedmode) {
        this.advancedmode = advancedmode;
    }

    public NetworkAdapter getSelectedAdapter() {
        return selectedAdapter;
    }

    public void setSelectedAdapter(NetworkAdapter selectedAdapter) {
        this.selectedAdapter = selectedAdapter;
    }

    public ArrayList<NetworkAdapter> getAdapters() {
        return adapters;
    }

    public void setAdapters(ArrayList<NetworkAdapter> adapters) {
        this.adapters = adapters;
    }

    @PostConstruct
    public void init() {
        System.out.println("networkSettingsBean");
        selectedAdapter = new NetworkAdapter();
        advancedmode = false;
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        BitServerResources bufResources = getBitServerResource("networksetpathfile");
        pathToFile = bufResources.getRvalue();
        newtworkSettingsFile = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                newtworkSettingsFile.append((char) c);
            }
        } catch (Exception e) {
            System.out.println("error of read file");
        }
        configFileText = newtworkSettingsFile.toString();
        NetworkSettingsParcer settingsParcer = new NetworkSettingsParcer(newtworkSettingsFile);
        adapters = settingsParcer.getAdapterList();
    }

    public void resAdapter() throws InterruptedException, IOException {
        showMessage("Внимание","Сетевой адаптер будет перезагружен!",FacesMessage.SEVERITY_INFO);

        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/netresetscript");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
        }
    }

    public void serviceMode() throws InterruptedException, IOException {
        showMessage("Внимание","Сервер переведен в сервисный режим!",FacesMessage.SEVERITY_INFO);

        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/servicemode");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
        }
    }

    public void normalMode() throws InterruptedException, IOException {
        showMessage("Внимание","Сервер переведен в нормальный режим!",FacesMessage.SEVERITY_INFO);

        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/noneservicemode");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
        }

    }

    public void addNewAdapter(){
        boolean verifiUnical;
        if(selectedAdapter.getName()!=null){
            verifiUnical = true;
            for (NetworkAdapter bufAdapter : adapters) {
                if (bufAdapter.getName().equals(selectedAdapter.getName())) {
                    verifiUnical = false;
                    break;
                }
            }

            if (verifiUnical) {
                adapters.add(selectedAdapter);
                PrimeFaces.current().executeScript("PF('manageAdapter').hide()");
                PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
            } else {
                adapters.remove(selectedAdapter);
                adapters.add(selectedAdapter);
                adapters.sort(Comparator.comparing(NetworkAdapter::getName));
                PrimeFaces.current().executeScript("PF('manageAdapter').hide()");
                PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
            }
        }
    }

    public void deleteAdapter(){
        adapters.remove(selectedAdapter);
        PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
        System.out.println("delete adapter");
    }

    public void initNewAdapter(){
        selectedAdapter = new NetworkAdapter();
        System.out.println("initNewAdapterr");
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public void save() {
        System.out.println("save");
    }

    public void onTabChange(){
        System.out.println("onTabChange");
    }

    public void onInputTextChange(){

    }

    public void saveSettingsCustomMode(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = configFileText.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения перезагрузите сетевую службу!",FacesMessage.SEVERITY_INFO);
    }

    public void saveSettings(){
        StringBuilder bufStringBuilder = new StringBuilder();
        bufStringBuilder.append("# This file describes the network interfaces available on your system\n");
        bufStringBuilder.append("# and how to activate them. For more information, see interfaces(5).\n");
        bufStringBuilder.append("\n");
        bufStringBuilder.append("source /etc/network/interfaces.d/*\n");
        bufStringBuilder.append("");
        bufStringBuilder.append("# The loopback network interface\n");
        bufStringBuilder.append("auto lo\n");
        bufStringBuilder.append("iface lo inet loopback\n");
        bufStringBuilder.append("\n");
        for(NetworkAdapter bufAdapter:adapters){
            if(bufAdapter.getIpmode().equals("dhcp")){
                bufStringBuilder.append("iface ").append(bufAdapter.getName()).append(" inet ").append(bufAdapter.getIpmode()).append("\n");
                bufStringBuilder.append("auto ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("allow-hotplug ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("\n");
            }else {
                bufStringBuilder.append("iface " + bufAdapter.getName() + " inet " + bufAdapter.getIpmode() + "\n");
                bufStringBuilder.append("address " + bufAdapter.getIpaddress() + "\n");
                bufStringBuilder.append("netmask " + bufAdapter.getMask() + "\n");
                if(!bufAdapter.getGateway().equals("")) {
                    bufStringBuilder.append("gateway " + bufAdapter.getGateway() + "\n");
                }
                bufStringBuilder.append("auto " + bufAdapter.getName() + "\n");
                bufStringBuilder.append("allow-hotplug " + bufAdapter.getName() + "\n");
                bufStringBuilder.append("\n");
            }
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = bufStringBuilder.toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения перезагрузите сетевую службу!",FacesMessage.SEVERITY_INFO);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}

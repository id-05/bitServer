package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.service.NetworkAdapter;
import ru.bitServer.service.NetworkSettingsParcer;
import ru.bitServer.util.LogTool;
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

@ManagedBean(name = "networkSettingsBean")
@ViewScoped
public class NetworkSettingsBean implements UserDao {

    Users currentUser;
    String currentUserId;
    String pathToFile;
    ArrayList<NetworkAdapter> adapters = new ArrayList<>();
    NetworkAdapter selectedAdapter;
    boolean advancedmode;
    String configFileText;
    StringBuilder newtworkSettingsFile;

    public String getConfigFileText() {
        return configFileText;
    }

    public void setConfigFileText(String configFileText) {
        this.configFileText = configFileText;
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
            LogTool.getLogger().warn("Error of read file init() networkSettingsBean: "+e.getMessage());
        }
        configFileText = newtworkSettingsFile.toString();
        NetworkSettingsParcer settingsParcer = new NetworkSettingsParcer(newtworkSettingsFile);
        adapters = settingsParcer.getAdapterList();
    }

    public void resAdapter() {
        showMessage("????????????????","?????????????? ?????????????? ?????????? ????????????????????????!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/netresetscript");
            proc.waitFor();
        }catch (Exception e){
            showMessage("????????????????",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().warn("Error resAdapter() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public void serviceMode() {
        showMessage("????????????????","???????????? ?????????????????? ?? ?????????????????? ??????????!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/servicemode");
            proc.waitFor();
            LogTool.getLogger().debug("Admin: "+currentUser.getUid().toString()+" select service mode ");
        }catch (Exception e){
            showMessage("????????????????",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().warn("Error serviceMode() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public void normalMode() {
        showMessage("????????????????","???????????? ?????????????????? ?? ???????????????????? ??????????!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/noneservicemode");
            proc.waitFor();
            LogTool.getLogger().debug("Admin: "+currentUser.getUid().toString()+" select normal mode ");
        }catch (Exception e){
            showMessage("????????????????",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().warn("Error normalMode() NetworkSettingsBean: "+e.getMessage());
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
                LogTool.getLogger().debug("Admin: "+currentUser.getUid().toString()+" add new networkadapter "+selectedAdapter.getName());
            } else {
                adapters.remove(selectedAdapter);
                adapters.add(selectedAdapter);
                adapters.sort(Comparator.comparing(NetworkAdapter::getName));
                LogTool.getLogger().debug("Admin: "+currentUser.getUid().toString()+" change one of exist networkadapter "+selectedAdapter.getName());
            }
            PrimeFaces.current().executeScript("PF('manageAdapter').hide()");
            PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
        }
    }

    public void deleteAdapter(){
        adapters.remove(selectedAdapter);
        LogTool.getLogger().debug("Admin: "+currentUser.getSignature()+" delete networkadapter "+selectedAdapter.getName());
        PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
    }

    public void initNewAdapter(){
        selectedAdapter = new NetworkAdapter();
    }

    public void save() {

    }

    public void onTabChange(){
        init();
        PrimeFaces.current().ajax().update(":form:tabview1");
    }

    public void saveSettingsCustomMode(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = configFileText.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().warn("Error saveSettingsCustomMode() NetworkSettingsBean: "+e.getMessage());
        }
        showMessage("????????????????","?????????????????? ??????????????????! ?????? ???? ???????????????????? ?????????????????????????? ?????????????? ????????????!",FacesMessage.SEVERITY_INFO);
    }

    public void saveSettings(){
        StringBuilder bufStringBuilder = new StringBuilder();
        bufStringBuilder.append("# This file describes the network interfaces available on your system\n");
        bufStringBuilder.append("# and how to activate them. For more information, see interfaces(5).\n");
        bufStringBuilder.append("\n");
        bufStringBuilder.append("source /etc/network/interfaces.d/*\n");
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
                bufStringBuilder.append("iface ").append(bufAdapter.getName()).append(" inet ").append(bufAdapter.getIpmode()).append("\n");
                bufStringBuilder.append("address ").append(bufAdapter.getIpaddress()).append("\n");
                if(!bufAdapter.getMask().equals("")) {
                    bufStringBuilder.append("netmask ").append(bufAdapter.getMask()).append("\n");
                }
                if(!bufAdapter.getGateway().equals("")) {
                    bufStringBuilder.append("gateway ").append(bufAdapter.getGateway()).append("\n");
                }
                bufStringBuilder.append("auto ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("allow-hotplug ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("\n");
            }
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = bufStringBuilder.toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().warn("Error saveSettings() NetworkSettingsBean: "+e.getMessage());
        }
        showMessage("????????????????","?????????????????? ??????????????????! ?????? ???? ???????????????????? ?????????????????????????? ?????????????? ????????????!",FacesMessage.SEVERITY_INFO);
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}

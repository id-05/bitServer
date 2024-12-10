package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerUser;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.FirewallParcer;
import ru.bitServer.service.FirewallPort;
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

    BitServerUser currentUser;
    String currentUserId;
    String pathToFile;
    String pathToFileFirewall;
    ArrayList<NetworkAdapter> adapters = new ArrayList<>();

    ArrayList<FirewallPort> ports = new ArrayList<>();
    NetworkAdapter selectedAdapter;
    FirewallParcer firewallParcer;

    FirewallPort selectedPort;
    boolean advancedmode;
    String configFileText;

    StringBuilder SettingsFile;
    StringBuilder SettingsFileFirewall;

    public String getConfigFileText() {
        return configFileText;
    }

    public FirewallPort getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(FirewallPort selectedPort) {
        this.selectedPort = selectedPort;
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

    public ArrayList<FirewallPort> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<FirewallPort> ports) {
        this.ports = ports;
    }

    @PostConstruct
    public void init() {
        selectedAdapter = new NetworkAdapter();
        selectedPort = new FirewallPort();
        advancedmode = false;
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);

        BitServerResources bufResources = getBitServerResource("networksetpathfile");
        pathToFile = bufResources.getRvalue();
        SettingsFile = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                SettingsFile.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().error(LogTool.getLogger() + " Error of read file init() networkSettingsBean: "+e.getMessage());
        }
        configFileText = SettingsFile.toString();
        NetworkSettingsParcer settingsParcer = new NetworkSettingsParcer(SettingsFile);
        adapters = settingsParcer.getAdapterList();

        BitServerResources bufResFirewall = getBitServerResource("firewallpathfile");
        pathToFileFirewall = bufResFirewall.getRvalue();
        SettingsFileFirewall = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFileFirewall)) {
            int c;
            while ((c = reader.read()) != -1) {
                SettingsFileFirewall.append((char) c);
            }
        } catch (Exception e) {

            LogTool.getLogger().error(LogTool.getLogger() + " Error of read file init() networkSettingsBean: "+e.getMessage());
        }

        firewallParcer = new FirewallParcer(SettingsFileFirewall);
        ports = firewallParcer.getFirewallList();
    }

    public void resAdapter() {
        showMessage("Внимание","Сетевой адаптер будет перезагружен!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/netresetscript");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(LogTool.getLogger() + " Error resAdapter() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public void serviceMode() {
        showMessage("Внимание","Сервер переведен в сервисный режим!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/servicemode");
            proc.waitFor();
            LogTool.getLogger().info("Admin: "+currentUser.getUid().toString()+" select service mode ");
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(LogTool.getLogger() + " Error serviceMode() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public void normalMode() {
        showMessage("Внимание","Сервер переведен в нормальный режим!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/noneservicemode");
            proc.waitFor();
            LogTool.getLogger().info("Admin: "+currentUser.getUid().toString()+" select normal mode ");
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(LogTool.getLogger() +" Error normalMode() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public void addNewAdapter(){
        try {
            boolean verifiUnical;
            if (selectedAdapter.getName() != null) {
                verifiUnical = true;
                for (NetworkAdapter bufAdapter : adapters) {
                    if (bufAdapter.getName().equals(selectedAdapter.getName())) {
                        verifiUnical = false;
                        break;
                    }
                }
                if (verifiUnical) {
                    adapters.add(selectedAdapter);
                    LogTool.getLogger().info("Admin: " + currentUser.getUid().toString() + " add new networkadapter " + selectedAdapter.getName());
                } else {
                    adapters.remove(delAdapterFromList(selectedAdapter.getName()));
                    adapters.add(selectedAdapter);
                    adapters.sort(Comparator.comparing(NetworkAdapter::getName));
                    LogTool.getLogger().info("Admin: " + currentUser.getUid().toString() + " change one of exist networkadapter " + selectedAdapter.getName());
                }
                PrimeFaces.current().executeScript("PF('manageAdapter').hide()");
                PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
            } else {
                LogTool.getLogger().debug(LogTool.getLogger() +" selectedAdapter.getName()==null");
            }
        }catch (Exception e){
            LogTool.getLogger().error(LogTool.getLogger() + " Error during save new adapter");
            showMessage("Внимание","Изменения сохранены! Ошибка сохранения нового адаптера!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addNewPort(){
        try {
            boolean verifiUnical;
            if (selectedPort.getPort() != null) {
                verifiUnical = true;
                for (FirewallPort bufPort : ports) {
                    if (bufPort.getPort().equals(selectedPort.getPort())) {
                        verifiUnical = false;
                        break;
                    }
                }
                if (verifiUnical) {
                    ports.add(selectedPort);
                    LogTool.getLogger().info("Admin: " + currentUser.getUid().toString() + " add new firewallport " + selectedPort.getPort());
                } else {
                    ports.remove(delPortFromList(selectedPort.getPort()));
                    ports.add(selectedPort);
                    ports.sort(Comparator.comparing(FirewallPort::getPort));
                    LogTool.getLogger().info("Admin: " + currentUser.getUid().toString() + " change one of exist Firewallport " + selectedPort.getPort());
                }
                PrimeFaces.current().executeScript("PF('managePort').hide()");
                PrimeFaces.current().ajax().update(":form:tabview1:dt-ports");
            } else {
                LogTool.getLogger().debug(LogTool.getLogger() +" selectedAdapter.getName()==null");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            LogTool.getLogger().error(LogTool.getLogger() + " Error during save new adapter");
            showMessage("Внимание","Изменения сохранены! Ошибка сохранения нового адаптера!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public NetworkAdapter delAdapterFromList(String buf){
        NetworkAdapter delAdapter = new NetworkAdapter();
        for(NetworkAdapter bufAdapter : adapters){
            if(bufAdapter.getName().equals(buf)){
                return bufAdapter;
            }
        }
        return delAdapter;
    }

    public FirewallPort delPortFromList(String buf){
        FirewallPort delPort = new FirewallPort();
        for(FirewallPort bufPort : ports){
            if(bufPort.getPort().equals(buf)){
                return bufPort;
            }
        }
        return delPort;
    }

    public void deleteAdapter(){
        adapters.remove(selectedAdapter);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete networkadapter "+selectedAdapter.getName());
        PrimeFaces.current().ajax().update(":form:tabview1:dt-adapters");
    }

    public void deletePort(){
        ports.remove(selectedPort);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete firewall port "+selectedPort.getPort());
        PrimeFaces.current().ajax().update(":form:tabview1:dt-ports");
    }

    public void initNewAdapter(){
        selectedAdapter = new NetworkAdapter();
    }

    public void initNewPort(){
        selectedPort = new FirewallPort();
    }

    public void save() {

    }

    public void onTabChange(){
        init();
        PrimeFaces.current().ajax().update(":form:tabview1");
    }

    public void saveSettingsFirewall(){
        BitServerResources bufResFirewall = getBitServerResource("firewallpathfile");
        pathToFileFirewall = bufResFirewall.getRvalue();

        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFileFirewall))
        {
            byte[] buffer = firewallParcer.updateFirewallFile(ports).toString().getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().error(LogTool.getLogger() + " Error saveSettingsFirewall() NetworkSettingsBean: "+e.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения перезагрузите службу!",FacesMessage.SEVERITY_INFO);
    }

    public void saveSettingsCustomMode(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToFile))
        {
            byte[] buffer = configFileText.getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
        }
        catch(IOException e){
            LogTool.getLogger().error(LogTool.getLogger() + " Error saveSettingsCustomMode() NetworkSettingsBean: "+e.getMessage());
        }
        showMessage("Внимание","Изменения сохранены! Для их применения перезагрузите сетевую службу!",FacesMessage.SEVERITY_INFO);
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
            if(bufAdapter.getIpMode().equals("dhcp")){
                bufStringBuilder.append("iface ").append(bufAdapter.getName()).append(" inet ").append(bufAdapter.getIpMode()).append("\n");
                bufStringBuilder.append("auto ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("allow-hotplug ").append(bufAdapter.getName()).append("\n");
                bufStringBuilder.append("\n");
            }else {
                bufStringBuilder.append("iface ").append(bufAdapter.getName()).append(" inet ").append(bufAdapter.getIpMode()).append("\n");
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
            showMessage("Внимание","Изменения сохранены! Для их применения перезагрузите сетевую службу!",FacesMessage.SEVERITY_INFO);
        }
        catch(IOException e){
            LogTool.getLogger().error(LogTool.getLogger() + " Error saveSettings() NetworkSettingsBean: "+e.getMessage());
            showMessage("Внимание","Возникла ошибка в процессе сохранения! Более подробно смотрите в лог файле!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void resetIptables(){
        showMessage("Внимание","Служба IPTABLES будет перезагружена!",FacesMessage.SEVERITY_INFO);
        try {
            Process proc = Runtime.getRuntime().exec("sudo ./home/tomcat/scripts/iptablesresetscript");
            proc.waitFor();
        }catch (Exception e){
            showMessage("Внимание",e.getMessage(),FacesMessage.SEVERITY_INFO);
            LogTool.getLogger().error(LogTool.getLogger() + " Error resetIptables() NetworkSettingsBean: "+e.getMessage());
        }
    }

    public StreamedContent getGetResult() throws IOException {
        String strpath = "/home/tomcat/webapps/bitServer.war";
        InputStream inputStream = new FileInputStream(strpath);
        return DefaultStreamedContent.builder()
                .name("bitServer.war")
                .contentType("application/war")
                .stream(() -> inputStream)
                .build();
    }
}
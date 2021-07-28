package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
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

    public void resetAdapter(){
        System.out.println("reset adapter");
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
                System.out.println("add new adapter");
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

    public void saveSettings(){
        System.out.println("saveSettings");
    }

}

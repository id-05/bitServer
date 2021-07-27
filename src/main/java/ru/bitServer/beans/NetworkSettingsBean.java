package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.dicom.OrthancPatient;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "networkSettingsBean", eager = false)
@ViewScoped
public class NetworkSettingsBean implements UserDao {

    public Users currentUser;
    public String currentUserId;
    public List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    public String pathToFile;
    public ArrayList<NetworkAdapter> adapters = new ArrayList<>();
    public NetworkAdapter selectedAdapter;

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
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        BitServerResources bufResources = getBitServerResource("networksetpathfile");
        pathToFile = bufResources.getRvalue();
        System.out.println("path to file = "+pathToFile);

        adapters.clear();
        for(int i=0; i<5; i++){
            NetworkAdapter bufAdapter = new NetworkAdapter(String.valueOf(i) ,String.valueOf(i),String.valueOf(i) ,
                    String.valueOf(i), String.valueOf(i), String.valueOf(i), String.valueOf(i));
            adapters.add(bufAdapter);
        }
    }

    public void resetAdapter(){
        System.out.println("reset adapter");
    }

    public void addNewAdapter(){
        adapters.add(selectedAdapter);
        PrimeFaces.current().executeScript("PF('manageAdapter').hide()");
        PrimeFaces.current().ajax().update(":form:dt-adapters");
        System.out.println("add new adapter");
    }

    public void deleteAdapter(){
        System.out.println("delete adapter");
    }

    public void initNewAdapter(){
        selectedAdapter = new NetworkAdapter();
        System.out.println("initNewAdapterr");
    }

}

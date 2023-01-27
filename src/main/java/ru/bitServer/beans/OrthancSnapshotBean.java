package ru.bitServer.beans;

import ru.bitServer.dao.UserDao;
import ru.bitServer.util.OrthancSettingSnapshot;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;

@ManagedBean(name = "snapshotBean")
@ViewScoped
public class OrthancSnapshotBean implements UserDao {

    ArrayList<OrthancSettingSnapshot> snapShots = new ArrayList<>();

    @PostConstruct
    public void init() {
        snapShots = getAllOrthancSnapshots();
        for(OrthancSettingSnapshot bufSnap:snapShots){
            System.out.println(bufSnap.getDate()+bufSnap.getSettingJson().toString());
        }
    }
}

package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.bitServer.beans.MainBean.mainServer;
import static ru.bitServer.beans.MainBean.osimisAddress;

@ManagedBean(name = "luresultsBean")
@ViewScoped
public class LocalUserResults implements UserDao {

    Users currentUser;
    String currentUserId;
    List<BitServerStudy> visibleStudiesList = new ArrayList<>();
    BitServerStudy selectedVisibleStudy;

    public BitServerStudy getSelectedVisibleStudy() {
        return selectedVisibleStudy;
    }

    public void setSelectedVisibleStudy(BitServerStudy selectedVisibleStudy) {
        this.selectedVisibleStudy = selectedVisibleStudy;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public List<BitServerStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<BitServerStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    @PostConstruct
    public void init() {
        System.out.println("luresultsBean");
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);
        visibleStudiesList.clear();
        List<BitServerStudy> bufList = getAllBitServerStudy();
        for(BitServerStudy bufStudy:bufList){
            if(bufStudy.getUserwhodiagnost()!=null) {
                if (bufStudy.getUserwhodiagnost().equals(currentUser.getUid().toString())) {
                    visibleStudiesList.add(bufStudy);
                }
            }
        }
    }

    public StreamedContent getResult(BitServerStudy study) throws IOException {
        if(study.isTyperesult()){
            Path path = Paths.get(study.getResult());
            System.out.println(path.toString());
            String extension = FilenameUtils.getExtension(study.getResult());
            InputStream inputStream = new FileInputStream(path.toString());
            return DefaultStreamedContent.builder()
                    .name(study.getPatientname()+"-"+study.getSdescription()+"."+extension)
                    .contentType("image/jpg")
                    .stream(() -> inputStream)
                    .build();
        }else{
            return null;
        }
    }

    public void redirectToOsimis(String sid) {
        PrimeFaces.current().executeScript("window.open('https://"+
                mainServer.getLogin()+":"+mainServer.getPassword()+"@"+
                osimisAddress+"osimis-viewer/app/index.html?study="+sid+"','_blank')");
    }
}


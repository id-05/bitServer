package ru.bitServer.beans;

import ru.bitServer.dicom.OrthancSerie;
import ru.bitServer.util.SessionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@ManagedBean(name = "bitviewerBean")
@ViewScoped
public class BitviewerBean {

    ArrayList<OrthancSerie> seriesList = new ArrayList();
    OrthancSerie selectedSerie;

    public ArrayList<OrthancSerie> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(ArrayList<OrthancSerie> seriesList) {
        this.seriesList = seriesList;
    }

    public OrthancSerie getSelectedSerie() {
        return selectedSerie;
    }

    public void setSelectedSerie(OrthancSerie selectedSerie) {
        this.selectedSerie = selectedSerie;
    }


    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession() ;
        String studyId = session.getAttribute("study").toString();

    }
}

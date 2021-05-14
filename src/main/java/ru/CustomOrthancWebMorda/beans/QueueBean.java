package ru.CustomOrthancWebMorda.beans;

import org.primefaces.PrimeFaces;
import ru.CustomOrthancWebMorda.beans.dao.Users;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "queueBean", eager = false)
@SessionScoped
public class QueueBean {

    public String filtrDate = "today";
    public Date firstdate;
    public Date seconddate;
    public String typeSeach = "no";

    public List<VisibleStudy> visibleStudiesList;
    public List<VisibleStudy> selectedVisibleStudies;
    public VisibleStudy selectedVisibleStudy;

    public VisibleStudy getSelectedVisibleStudy() {
        return selectedVisibleStudy;
    }

    public void setSelectedVisibleStudy(VisibleStudy selectedVisibleStudy) {
        this.selectedVisibleStudy = selectedVisibleStudy;
    }

    public List<VisibleStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<VisibleStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    public List<VisibleStudy> getSelectedVisibleStudies() {
        return selectedVisibleStudies;
    }

    public void setSelectedVisibleStudies(List<VisibleStudy> selectedVisibleStudies) {
        this.selectedVisibleStudies = selectedVisibleStudies;
    }

    public String getTypeSeach() {
        return typeSeach;
    }

    public void setTypeSeach(String typeSeach) {
        this.typeSeach = typeSeach;
    }

    public Date getFirstdate() {
        return firstdate;
    }

    public void setFirstdate(Date firstdate) {
        this.firstdate = firstdate;
    }

    public Date getSeconddate() {
        return seconddate;
    }

    public void setSeconddate(Date seconddate) {
        this.seconddate = seconddate;
    }

    public String getFiltrDate() {
        return filtrDate;
    }

    public void setFiltrDate(String filtrDate) {
        this.filtrDate = filtrDate;
    }

    @PostConstruct
    public void init() {
        System.out.println("QueueBean");

        firstdate = new Date();
        seconddate = new Date();
        visibleStudiesList = new ArrayList<>();
        visibleStudiesList.add(new VisibleStudy("1","1","1","1"));
        visibleStudiesList.add(new VisibleStudy("2","2","2","2"));
        visibleStudiesList.add(new VisibleStudy("3","3","3","3"));
    }

    public Boolean firstDateSelect(){
        filtrDate = "targetdate";
        PrimeFaces.current().ajax().update(":seachform");
        return true;
    }

    public Boolean secondDateSelect(){
        filtrDate = "range";
        PrimeFaces.current().ajax().update(":seachform");
        return true;
    }

    public void dataoutput(){
        System.out.println("dsdjl ="+typeSeach);
    }
}

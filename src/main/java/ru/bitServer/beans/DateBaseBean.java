package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import ru.bitServer.dao.*;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static ru.bitServer.beans.AutoriseBean.showMessage;
import static ru.bitServer.beans.MainBean.*;

@ManagedBean(name = "datebaseBean")
@ViewScoped
public class DateBaseBean implements UserDao {

    List<BitServerResources> listResources = new ArrayList<>();
    BitServerResources selectedResource;
    //BitServerResources selectedResources;
    List<BitServerStudy> listStudys = new ArrayList<>();
    BitServerStudy selectedStudy;
    BitServerStudy selectedStudys;
    List<BitServerScheduler> listSchedulers = new ArrayList<>();
    BitServerScheduler selectedScheduler;
    BitServerScheduler selectedSchedulers;
    List<BitServerModality> listModalitys = new ArrayList<>();
    BitServerModality selectedModality;
    BitServerModality selectedModalitys;
    Users currentUser;
    String currentUserId;
    List<BitServerResources> bitServerResourcesList = new ArrayList<>();
    final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public BitServerModality getSelectedModalitys() {
        return selectedModalitys;
    }

    public void setSelectedModalitys(BitServerModality selectedModalitys) {
        this.selectedModalitys = selectedModalitys;
    }

    public BitServerScheduler getSelectedSchedulers() {
        return selectedSchedulers;
    }

    public void setSelectedSchedulers(BitServerScheduler selectedSchedulers) {
        this.selectedSchedulers = selectedSchedulers;
    }

    public BitServerStudy getSelectedStudys() {
        return selectedStudys;
    }

    public void setSelectedStudys(BitServerStudy selectedStudys) {
        this.selectedStudys = selectedStudys;
    }

//    public BitServerResources getSelectedResources() {
//        return selectedResources;
//    }
//
//    public void setSelectedResources(BitServerResources selectedResources) {
//        this.selectedResources = selectedResources;
//    }

    public List<BitServerModality> getListModalitys() {
        return listModalitys;
    }

    public void setListModalitys(List<BitServerModality> listModalitys) {
        this.listModalitys = listModalitys;
    }

    public BitServerModality getSelectedModality() {
        return selectedModality;
    }

    public void setSelectedModality(BitServerModality selectedModality) {
        this.selectedModality = selectedModality;
    }

    public List<BitServerScheduler> getListSchedulers() {
        return listSchedulers;
    }

    public void setListSchedulers(List<BitServerScheduler> listSchedulers) {
        this.listSchedulers = listSchedulers;
    }

    public BitServerScheduler getSelectedScheduler() {
        return selectedScheduler;
    }

    public void setSelectedScheduler(BitServerScheduler selectedScheduler) {
        this.selectedScheduler = selectedScheduler;
    }

    public List<BitServerStudy> getListStudys() {
        return listStudys;
    }

    public void setListStudys(List<BitServerStudy> listStudys) {
        this.listStudys = listStudys;
    }

    public BitServerStudy getSelectedStudy() {
        return selectedStudy;
    }

    public void setSelectedStudy(BitServerStudy selectedStudy) {
        this.selectedStudy = selectedStudy;
    }

    public BitServerResources getSelectedResource() {
        return selectedResource;
    }

    public void setSelectedResource(BitServerResources selectedResource) {
        this.selectedResource = selectedResource;
    }

    public List<BitServerResources> getListResources() {
        return listResources;
    }

    public void setListResources(List<BitServerResources> listResources) {
        this.listResources = listResources;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("userid").toString();
        currentUser = getUserById(currentUserId);

        listSchedulers = getAllBitServerSheduler();
        listResources = getAllBitServerResource();
        listStudys = getStudy (5, "all", new Date(), new Date(),"");//getAllBitServerStudy();
        listModalitys = getAllBitServerModality();

        //selectedResource = new BitServerResources();
        selectedStudy = new BitServerStudy();
        selectedModality = new BitServerModality();
        selectedScheduler = new BitServerScheduler();
    }

    public  List<BitServerStudy> getStudy (int state, String dateSeachType, Date firstdate, Date seconddate, String strModality) {
        List<BitServerStudy> resultList = new ArrayList<>();
        String query;

        switch (dateSeachType){
            case "today":
                firstdate = new Date();
                seconddate = new Date();
                break;
            case "yesterday":
                Instant now = Instant.now();
                Instant yesterday = now.minus(1, ChronoUnit.DAYS);
                firstdate = Date.from(yesterday);
                seconddate = Date.from(yesterday);
                break;
            case "targetdate":
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(new SimpleDateFormat("yyyy").format(firstdate)), Integer.parseInt(new SimpleDateFormat("MM").format(firstdate))-1
                        ,Integer.parseInt(new SimpleDateFormat("dd").format(firstdate)),23,59);
                seconddate = cal.getTime();
                break;
        }

        firstdate.setHours(00);
        firstdate.setMinutes(00);
        firstdate.setSeconds(00);
        seconddate.setHours(23);
        seconddate.setMinutes(59);
        seconddate.setSeconds(59);
        String reqizite = "sid, shortid, sdescription, sdate, modality, patientname, patientbirthdate, patientsex, status";
        //String reqizite = "sid, shortid, sdate, patientname";
        if(state==5){
            if(dateSeachType.equals("all")){
                query = "select "+reqizite+" from BitServerStudy";
            }else{
                query = "select "+reqizite+" from BitServerStudy " +
                        "where sdate BETWEEN  '"+FORMAT.format(firstdate)+"' AND '"+FORMAT.format(seconddate)+"'";
            }
        }else{
            if(dateSeachType.equals("all")){
                query = "select "+reqizite+" from BitServerStudy " +
                        "where status = "+state;
            }else{
                query = "select "+reqizite+" from BitServerStudy " +
                        "where status = "+state +" and sdate BETWEEN  '"+FORMAT.format(firstdate)+"' AND '"+FORMAT.format(seconddate)+"'";
            }
        }

        try {
            // JDBC variables for opening and managing connection
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //timeRequest = ((new Date()).getTime() - timeStart)/1000.00;
            while (rs.next()) {
                resultList.add(new BitServerStudy(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getDate(7),
                        rs.getString(8),
                        rs.getInt(9)));
            }
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName()+": "+ e.getMessage());
        }
        PrimeFaces.current().ajax().update(":seachform:for_txt_count2");
        PrimeFaces.current().ajax().update(":seachform:txt_count2");
        PrimeFaces.current().ajax().update(":seachform:");
        PrimeFaces.current().ajax().update(":seachform:ajs");
        PrimeFaces.current().executeScript("PF('ajs').hide()");
        return resultList;
    }

    public void initNewResources(){
        selectedResource = new BitServerResources();
    }

    public void initDefaultResources(){
        for(BitServerResources bufResources:listResources){
            deleteFromBitServerTable(bufResources.getId());
        }
        listResources.clear();
        selectedResource = new BitServerResources();

        bitServerResourcesList = getAllBitServerResource();
        ArrayList<String> bufResourceName = new ArrayList<>();
        for (BitServerResources buf : bitServerResourcesList) {
            bufResourceName.add(buf.getRname());
        }

        if(!bufResourceName.contains("orthancaddress")){saveBitServiceResource(new BitServerResources("orthancaddress","127.0.0.1")); }
        if(!bufResourceName.contains("port")){saveBitServiceResource(new BitServerResources("port","8042")); }
        if(!bufResourceName.contains("login")){saveBitServiceResource(new BitServerResources("login","doctor")); }
        if(!bufResourceName.contains("password")){saveBitServiceResource(new BitServerResources("password","doctor")); }
        if(!bufResourceName.contains("pathtojson")){saveBitServiceResource(new BitServerResources("pathtojson","/etc/orthanc/")); }
        if(!bufResourceName.contains("pathtoresultfile")){saveBitServiceResource(new BitServerResources("pathtoresultfile","/dataimage/results/")); }
        if(!bufResourceName.contains("networksetpathfile")){saveBitServiceResource(new BitServerResources("networksetpathfile","/etc/network/interfaces")); }
        if(!bufResourceName.contains("httpmode")){saveBitServiceResource(new BitServerResources("httpmode","false")); }
        if(!bufResourceName.contains("luascriptpathfile")){saveBitServiceResource(new BitServerResources("luascriptpathfile","/usr/share/orthanc/lua/route.lua")); }
        if(!bufResourceName.contains("updateafteropen")){saveBitServiceResource(new BitServerResources("updateafteropen","false")); }
        if(!bufResourceName.contains("showStat")){saveBitServiceResource(new BitServerResources("showStat","true")); }
        if(!bufResourceName.contains("logpath")){saveBitServiceResource(new BitServerResources("logpath","/dataimage/results/")); }
        if(!bufResourceName.contains("colstatus")){saveBitServiceResource(new BitServerResources("colstatus","false")); }
        if(!bufResourceName.contains("colpreview")){saveBitServiceResource(new BitServerResources("colpreview","false")); }
        if(!bufResourceName.contains("colDateBirth")){saveBitServiceResource(new BitServerResources("colDateBirth","false")); }
        if(!bufResourceName.contains("colDate")){saveBitServiceResource(new BitServerResources("colDate","true")); }
        if(!bufResourceName.contains("colDescription")){saveBitServiceResource(new BitServerResources("colDescription","false")); }
        if(!bufResourceName.contains("colModality")){saveBitServiceResource(new BitServerResources("colModality","false")); }
        if(!bufResourceName.contains("colWhereSend")){saveBitServiceResource(new BitServerResources("colWhereSend","false")); }
        if(!bufResourceName.contains("datastorage")){saveBitServiceResource(new BitServerResources("datastorage","/dataimage/")); }
        if(!bufResourceName.contains("workListLifeTime")){saveBitServiceResource(new BitServerResources("workListLifeTime","1")); }
        if(!bufResourceName.contains("hl7port")){saveBitServiceResource(new BitServerResources("hl7port","4043")); }
        if(!bufResourceName.contains("WorkListPath")){saveBitServiceResource(new BitServerResources("WorkListPath","/dataimage/results/")); }
        if(!bufResourceName.contains("debug")){saveBitServiceResource(new BitServerResources("debug","false")); }
        if(!bufResourceName.contains("OptionSend")){saveBitServiceResource(new BitServerResources("OptionSend","false")); }
        if(!bufResourceName.contains("OptionDownload")){saveBitServiceResource(new BitServerResources("OptionDownload","true")); }
        if(!bufResourceName.contains("ShowSeachTime")){saveBitServiceResource(new BitServerResources("ShowSeachTime","true")); }
        if(!bufResourceName.contains("ShowHelp")){saveBitServiceResource(new BitServerResources("ShowHelp","true")); }
        if(!bufResourceName.contains("PeriodUpdate")){saveBitServiceResource(new BitServerResources("PeriodUpdate","5")); }

        listResources = getAllBitServerResource();

        showMessage("Внимание!","Все данные были удалены и заполнены значениями по умолчанию!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" select default resources");
    }

    public void initNewModality(){
        selectedModality = new BitServerModality();
    }

    public void addNewResource(){
        if((selectedResource.getRname()!=null)&(!selectedResource.getRvalue().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerResources bufResource:listResources){
                if (bufResource.getRname().equals(selectedResource.getRname())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                saveBitServiceResource(selectedResource);
                LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" add new resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
            }else{
                updateBitServiceResource(selectedResource);
                LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" update resource");
                listResources = getAllBitServerResource();
                PrimeFaces.current().executeScript("PF('manageResourceDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-resources");
                if(selectedResource.getRname().equals("hl7port")){
                    MainBean.HL7service();
                }
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addNewModality(){
        if(!selectedModality.getName().equals(""))
        {
            updateBitServerModality(selectedModality);
            listModalitys = getAllBitServerModality();
            PrimeFaces.current().executeScript("PF('manageModalityDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-modality");
            LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" add new modality "+selectedModality.getName());
        }else{
            showMessage("Внимание!","Поле должно быть заполнено!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void addNewScheduler(){
        if((!selectedScheduler.getModality().equals(""))&(!selectedScheduler.getSource().equals("")))
        {
            boolean verifiUnical = true;
            for(BitServerScheduler bufScheduler:listSchedulers){
                if (bufScheduler.getModality().equals(selectedScheduler.getModality())) {
                    verifiUnical = false;
                    break;
                }
            }
            if(verifiUnical) {
                saveNewRule(selectedScheduler);
                listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }else{
                updateRule(selectedScheduler);
                listSchedulers = getAllBitServerSheduler();
                PrimeFaces.current().executeScript("PF('manageSchedulerDialog').hide()");
                PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
            }
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteScheduler(){
        deleteRule(selectedScheduler);
        listSchedulers.remove(selectedScheduler);
        selectedScheduler = new BitServerScheduler();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Правило удалено!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-scheduler");
    }

    public void setRandomData(){
        for(int i=1; i<100000; i++){

            Calendar cal = new GregorianCalendar();
            cal.set(2017,10,10);
            Date startDate = cal.getTime();
            Date endDate = new Date();
            long random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
            Date sdate = new Date(random);

            cal.set(2021,10,10);
            startDate = cal.getTime();
            random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
            Date dateaddinbase = new Date(random);

            cal.set(1975,10,10);
            startDate = cal.getTime();
            //startDate.setDate(1);
            random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
            Date patientbirthdate = new Date(random);

            String sid = generateString(59);
            String shortid = generateString(59);
            String sdescription = generateString(59);
            String source = generateString(59);
            String patientname = generateString(59);
            String anamnes = generateString(59);
            String result = generateString(59);

            List<BitServerModality> listModality = getAllBitServerModality();

            int min = 0;
            int max = listModality.size()-1;
            int diff = max - min;
            Random randomModality = new Random();
            int j = randomModality.nextInt(diff + 1);
            j += min;

            String modality = listModality.get(j).getName();

            min = 0;
            max = 3;
            diff = max - min;
            Random statusStudy = new Random();
            j = statusStudy.nextInt(diff + 1);
            j += min;
            int status = j;

            min = 0;
            max = 1;
            diff = max - min;
            statusStudy = new Random();
            j = statusStudy.nextInt(diff + 1);
            j += min;

            List<String> maleList = new ArrayList<>();
            maleList.add("F");
            maleList.add("M");

            BitServerStudy bufStudy = new BitServerStudy(sid, shortid, sdescription, source, sdate,
                    modality, dateaddinbase, patientname, patientbirthdate,
                    maleList.get(j), anamnes, result, status);

            addStudyJDBC(bufStudy);

        }
        System.out.println("end");
    }

    public String generateString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public void deleteResource(){
        deleteFromBitServerTable(selectedResource.getId());
        listResources.remove(selectedResource);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete Resource: "+selectedResource.getRname());
        selectedResource = new BitServerResources();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ресурс удален!"));
        PrimeFaces.current().ajax().update(":form:accord:dt-resources");
    }

    public void deleteModality(){
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete Modality "+selectedModality.getName());
        deleteBitServerModality(selectedModality);
        listModalitys.remove(selectedModality);
        selectedModality = new BitServerModality();
        showMessage("Внимание!","Модальность удалена!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-modality");
    }

    public void updateStudyInBase(){
        if((selectedStudy.getId() != null) & (!selectedStudy.getModality().equals("")))
        {
            updateStudy(selectedStudy);
            LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" update study in base "+selectedStudy.getSid());
            listStudys = getStudy (5, "all", new Date(), new Date(),"");//getAllBitServerStudy();
            PrimeFaces.current().executeScript("PF('manageStudyDialog').hide()");
            PrimeFaces.current().ajax().update(":form:accord:dt-study");
        }else{
            showMessage("Внимание!","Все поля должны быть заполнены!",FacesMessage.SEVERITY_ERROR);
        }
    }

    public void deleteStudyFromBase(){
        deleteStudy(selectedStudy);
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete study from base "+selectedStudy.getSid());
        listStudys.remove(selectedStudy);
        selectedStudy = new BitServerStudy();
        showMessage("Внимание!","Исследование удалено!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-study");
    }

    public void deleteAllStudy() throws SQLException {
        deleteAllStudyJDBC();
        LogTool.getLogger().info("Admin: "+currentUser.getSignature()+" delete all study from base");
        listStudys.clear();
        selectedStudy = new BitServerStudy();
        showMessage("Внимание!","Все данные удалены!",FacesMessage.SEVERITY_ERROR);
        PrimeFaces.current().ajax().update(":form:accord:dt-study");
        LogTool.getLogger().info("Admin "+ currentUser.getUname()+" delete all record from bitserverstudy");
    }
}

package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@ManagedBean(name = "remoteDashboard")
@ViewScoped
public class RemoteUserDashboardBean implements UserDao {

    DashboardModel model;
    int countBlock = 0;
    int countToday = 0;
    int countWeek = 0;
    int countMounth = 0;
    int countAll = 0;
    Date timeLeft;
    Users currentUser;
    BitServerStudy currentStudy;
    List<BitServerStudy> myStudies = new ArrayList<>();
    List<BitServerStudy> allHasResultStudies = new ArrayList<>();
    LineChartModel lineModel;
    String typeChart = "mounth";
    int sliderDate = 50;
    Date firstdate;
    Date seconddate;

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public int getSliderDate() {
        return sliderDate;
    }

    public void setSliderDate(int slidaerDate) {
        this.sliderDate = slidaerDate;
    }

    public String getTypeChart() {
        return typeChart;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public int getCountBlock() {
        return countBlock;
    }

    public void setCountBlock(int countBlock) {
        this.countBlock = countBlock;
    }

    public Date getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Date timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getCountMounth() {
        return countMounth;
    }

    public void setCountMounth(int countMounth) {
        this.countMounth = countMounth;
    }

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }

    public int getCountToday() {
        return countToday;
    }

    public void setCountToday(int countToday) {
        this.countToday = countToday;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
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

    @PostConstruct
    public void init()  {
        System.out.println("remote user dashboard");
        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        try {
            if (currentUser.isHasBlockStudy()) {
                currentStudy = getStudyById(currentUser.getBlockStudy());
                countBlock = 1;
                Date nowDate = new Date();
                Date startLockDate = currentStudy.getDatablock();
                long timestamp = (startLockDate.getTime() + 3600000L * MainBean.timeOnWork) - nowDate.getTime();
                timeLeft = new Date(timestamp);
            }
        }catch (Exception e){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try{
                ec.redirect(ec.getRequestContextPath()
                        + "/views/errorpage.xhtml?"+e.getMessage());
                LogTool.getLogger().warn("Error init() remoteDashboard: "+e.getMessage());
            }catch (Exception e2){
                LogTool.getLogger().warn("Error init() remoteDashboard: "+e.getMessage());
            }
        }

        myStudies = getMyStudy(currentUser);
        allHasResultStudies = getAllBitServerStudy();
        allHasResultStudies.sort(Comparator.comparing(BitServerStudy::getSdate));
        try {
            firstdate = allHasResultStudies.get(0).getSdate();
            seconddate = allHasResultStudies.get(allHasResultStudies.size() - 1).getSdate();
        }catch (Exception e){
            LogTool.getLogger().warn("Error init() remoteDashboard: "+e.getMessage());
            firstdate = new Date();
            seconddate = new Date();
        }
            PrimeFaces.current().ajax().update(":remoteform:serverstatistics");

        countAll = myStudies.size();

        LocalDate now = LocalDate.now();
        LocalDate startOfCurrentWeek = now.with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek().plus(6)));
        Date startWeek = Date.from( startOfCurrentWeek.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endWeek = Date.from( endOfWeek.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        LocalDate initial = LocalDate.now(), firstDay = initial.withDayOfMonth(1), endDay = initial.withDayOfMonth(initial.lengthOfMonth());
        Date startMonth = Date.from( firstDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endMonth = Date.from( endDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        for(BitServerStudy bufStudy:myStudies){
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            Date todayWithZeroTime = null;
            try {
                todayWithZeroTime = formatter.parse(formatter.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(bufStudy.getDateresult().after(todayWithZeroTime) && bufStudy.getDateresult().before(tomorrow)) {
                countToday++;
            }

            if( (bufStudy.getDateresult().getTime() > startWeek.getTime()) && (bufStudy.getDateresult().getTime()<endWeek.getTime()) ){
                countWeek++;
            }

            if( (bufStudy.getDateresult().getTime() > startMonth.getTime()) && (bufStudy.getDateresult().getTime()<endMonth.getTime()) ){
                countMounth++;
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        lineModel = initModel(cal,"MM.yyyy");
        preInitModel();

        model = new DefaultDashboardModel();

        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();

        column1.addWidget("current");
        column1.addWidget("mystatistics");
        column3.addWidget("serverstatistics");

        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
    }

    public void preInitModel(){
        lineModel.setTitle("Исследования");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Time Period"));
    }

    public Boolean dateSelect() {
        chartOutput();
        return true;
    }

    public void chartOutput(){
        System.out.println("cartoutput");
        Calendar cal = Calendar.getInstance();
        lineModel.clear();
        switch (typeChart){

            case "mounth":{
                cal.add(Calendar.MONTH, -1);
                lineModel = initModel(cal,"MM.yyyy");
                preInitModel();
                PrimeFaces.current().ajax().update(":remoteform:serverstatistics");
            }
            break;
            case "year":{
                cal.add(Calendar.YEAR, -1);
                lineModel = initModel(cal,"yyyy");
                preInitModel();
                PrimeFaces.current().ajax().update(":remoteform:serverstatistics");
            }
            break;
        }
    }

    private LineChartModel initModel(Calendar cal, String pattern) {
        //Calendar calend = Calendar.getInstance();
        Date endDate = seconddate;//calend.getTime();
        LineChartModel bufModel = new LineChartModel();
        ChartSeries bufSeries = new ChartSeries();
        bufSeries.setLabel("Всего на описание");
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(pattern);
        Date refDate =  firstdate;//cal.getTime();
        for(BitServerStudy bufStudy:allHasResultStudies){
            if(bufStudy.getSdate().after(firstdate)&&(bufStudy.getSdate().before(seconddate))){
                long bufDatemillis = 0;
                try {
                   bufDatemillis = (formatter.parse(formatter.format(bufStudy.getSdate()))).getTime();
                } catch (Exception e) {
                    LogTool.getLogger().warn("Error initModel remoteDashboard: "+e.getMessage());
                }
                Integer bufCount = resultMap.get(bufDatemillis);
                if(bufCount==null){
                    resultMap.put(bufDatemillis,1);
                }else{
                    bufCount = bufCount + 1;
                    resultMap.replace(bufDatemillis,bufCount);
                }
            }
        }
        bufSeries.setLabel("Всего на описание");
        for(Map.Entry<Long, Integer> item : resultMap.entrySet()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getKey());
            Date bufDate = calendar.getTime();
            bufSeries.set(formatter.format(bufDate),  item.getValue());
        }
        bufModel.addSeries(bufSeries);
        return bufModel;
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

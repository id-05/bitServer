package ru.bitServer.beans;

import org.primefaces.PrimeFaces;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@ManagedBean(name = "remoteDashboard", eager = true)
@RequestScoped
public class RemoteUserDashboardBean implements UserDao {

    private DashboardModel model;
    private int countBlock = 0;
    private int countToday = 0;
    private int countWeek = 0;
    private int countMounth = 0;
    private int countAll = 0;
    private Date timeLeft;
    public Users currentUser;
    private BitServerStudy currentStudy;
    private List<BitServerStudy> myStudies = new ArrayList<>();
    private List<BitServerStudy> allHasResultStudies = new ArrayList<>();
    public LineChartModel lineModel;
    private String typeChart = "week";
    private int sliderDate = 50;

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

    public int getCountWeek() {
        return countWeek;
    }

    public void setCountWeek(int countWeek) {
        this.countWeek = countWeek;
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

    public List<BitServerStudy> getMyStudies() {
        return myStudies;
    }

    public void setMyStudies(List<BitServerStudy> myStudies) {
        this.myStudies = myStudies;
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

    @PostConstruct
    public void init() {

        HttpSession session = SessionUtils.getSession();
        currentUser = getUserById(session.getAttribute("userid").toString());
        if(currentUser.isHasBlockStudy()){
            currentStudy = getStudyById(currentUser.getBlockStudy());
            countBlock = 1;
            Date nowDate = new Date();
            Date startLockDate = currentStudy.getDatablock();
            long timestamp = (startLockDate.getTime()+ 3600000L * MainBean.timeOnWork) - nowDate.getTime();
            timeLeft = new Date(timestamp);
        }

        myStudies = getMyStudy(currentUser);

        allHasResultStudies = getAllBitServerStudy();
        allHasResultStudies.sort(Comparator.comparing(BitServerStudy::getSdate));


        //allHasResultStudies = getAllHasResultStudies();

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

    public void chartOutput(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        //Date bufDate = cal.getTime();
        lineModel.clear();
        switch (typeChart){
            case "week":{
                cal.add(Calendar.DATE, -7);
                lineModel = initModel(cal,"dd.MM");
                preInitModel();
                PrimeFaces.current().ajax().update(":remoteform:serverstatistics");
            }
            break;
            case "mounth":{
                cal.add(Calendar.MONTH, -1);
                lineModel = initModel(cal,"dd.MM");
                preInitModel();
                PrimeFaces.current().ajax().update(":remoteform:serverstatistics");
            }
            break;
            case "year":{
                cal.add(Calendar.YEAR, -1);
                lineModel = initModel(cal,"MM.yyyy");
                preInitModel();
                PrimeFaces.current().ajax().update(":remoteform:serverstatistics");
            }
            break;
        }
    }

    private LineChartModel initModel(Calendar cal, String pattern) {
        Calendar calend = Calendar.getInstance();
        calend.add(Calendar.YEAR, -1);
        Date endDate = calend.getTime();

        LineChartModel bufModel = new LineChartModel();
        ChartSeries bufSeries = new ChartSeries();
        bufSeries.setLabel("Всего на описание");
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(pattern);
        Date refDate =  cal.getTime();
        for(BitServerStudy bufStudy:allHasResultStudies){
            if(bufStudy.getSdate().after(refDate)&&(bufStudy.getSdate().before(endDate))){
                System.out.println("sdate = "+bufStudy.getSdate());
                long bufDatemillis = 0;
                try {
                   bufDatemillis = (formatter.parse(formatter.format(bufStudy.getSdate()))).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
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

package ru.bitServer.beans;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import ru.bitServer.dao.*;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "statisticsBean")
@RequestScoped
public class StatisticsBean implements UserDao {

    DashboardModel model;
    Users currentUser;
    List<BitServerStudy> allStudies = new ArrayList<>();
    LineChartModel lineModel;
    private PieChartModel pieModality;
    private PieChartModel pieSource;
    String typeChart = "mounth";
    Date firstdate;
    Date seconddate;
    List<String> sourcelist = new ArrayList<>();
    boolean showStat;

    public boolean isShowStat() {
        return showStat;
    }

    public void setShowStat(boolean showStat) {
        this.showStat = showStat;
    }

    public List<String> getSourcelist() {
        return sourcelist;
    }

    public void setSourcelist(List<String> sourcelist) {
        this.sourcelist = sourcelist;
    }

    public PieChartModel getPieModality() {
        return pieModality;
    }

    public PieChartModel getPieSource() {
        return pieSource;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    @PostConstruct
    public void init()  {
        allStudies = getAllBitServerStudy();
        allStudies.sort(Comparator.comparing(BitServerStudy::getSdate));
        try {
            BitServerResources bufResource = getBitServerResource("showStat");
            showStat = bufResource.getRvalue().equals("true");
        }catch (Exception e){
            LogTool.getLogger().warn("Error init() QueueBean "+e.getMessage());
        }
        try {
            firstdate = allStudies.get(0).getSdate();
            seconddate = allStudies.get(allStudies.size() - 1).getSdate();
        }catch (Exception e){
            firstdate = new Date();
            seconddate = new Date();
        }
        lineModel = initModel("MM.yyyy");
        preInitModel();
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        column1.addWidget("serverstatistics");
        column2.addWidget("modality");
        column3.addWidget("source");
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
    }

    public void preInitModel(){
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Период"));
        lineModel.getAxis(AxisType.X).setTickAngle(45);
        lineModel.getAxis(AxisType.Y).setLabel("Количество исследований");
        lineModel.getAxis(AxisType.Y).setMin(0);
        lineModel.getAxis(AxisType.Y).setTickAngle(1);
        initPieModality();
        initPieSource();
    }

    public void initPieModality(){
        pieModality = new PieChartModel();
        List<BitServerModality> modalityList = getAllBitServerModality();
        for(BitServerModality bufModality:modalityList){
            pieModality.set(bufModality.getName(), allStudies.stream().filter(BitServerStudy->BitServerStudy.getModality().equals(bufModality.getName())).count());
        }
        pieModality.setLegendPosition("e");
        pieModality.setShowDatatip(true);
        pieModality.setShowDataLabels(true);
        pieModality.setDataFormat("value");
    }

    public void initPieSource(){
        pieSource = new PieChartModel();
        List<String> buflist = new ArrayList<>();
        for(BitServerStudy bufStudy:allStudies){
            buflist.add(bufStudy.getSource());
        }
        Set<String> set = new LinkedHashSet<>(buflist);
        sourcelist.addAll(set);

        for(String bufSource:sourcelist){
            pieSource.set(bufSource, getBitServerStudySource(bufSource).size());
        }

        pieSource.setLegendPosition("e");
        pieSource.setShowDatatip(true);
        pieSource.setShowDataLabels(true);
        pieSource.setDataFormat("value");
    }

    public void setMounths(){
        typeChart = "mounth";
        chartOutput();
    }

    public void setYears(){
        typeChart = "year";
        chartOutput();
    }

    public void chartOutput(){
        lineModel.clear();
        switch (typeChart){
            case "mounth":{
                lineModel = initModel("MM.yyyy");
                preInitModel();
            }
            break;
            case "year":{
                lineModel = initModel("yyyy");
                preInitModel();
            }
            break;
        }
    }

    private LineChartModel initModel(String pattern) {
        LineChartModel bufModel = new LineChartModel();
        ChartSeries bufSeries = new ChartSeries();
        bufSeries.setLabel("Исследования");
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(pattern);
        for(BitServerStudy bufStudy:allStudies){
            if( (bufStudy.getSdate().after(firstdate)&&(bufStudy.getSdate().before(seconddate))) |
                    ( (bufStudy.getSdate().equals(firstdate))|(bufStudy.getSdate().equals(seconddate)) ) ){
                long bufDatemillis = 0;
                try {
                    bufDatemillis = (formatter.parse(formatter.format(bufStudy.getSdate()))).getTime();
                } catch (Exception e) {
                    LogTool.getLogger().warn("Error initModel statisticsBean: "+e.getMessage());
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

        for(Map.Entry<Long, Integer> item : resultMap.entrySet()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getKey());
            Date bufDate = calendar.getTime();
            bufSeries.set(formatter.format(bufDate),  item.getValue());
        }
        bufModel.addSeries(bufSeries);
        return bufModel;
    }
}
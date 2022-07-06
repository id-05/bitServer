package ru.bitServer.beans;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.*;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dao.Users;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "statisticsBean")
@ViewScoped
public class StatisticsBean implements UserDao {

    DashboardModel model;
    Users currentUser;
    List<BitServerStudy> allStudies = new ArrayList<>();
    LineChartModel lineModel;
    String typeChart = "mounth";
    Date firstdate;
    Date seconddate;

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
        firstdate = new Date();
        seconddate = new Date();
        firstdate = allStudies.get(0).getSdate();
        seconddate = allStudies.get(allStudies.size() - 1).getSdate();
        lineModel = initModel("MM.yyyy");
        preInitModel();
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        column1.addWidget("serverstatistics");
        model.addColumn(column1);
    }

    public void preInitModel(){
        lineModel.setTitle("Исследования");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Период"));
        lineModel.getAxis(AxisType.X).setTickAngle(45);
        lineModel.getAxis(AxisType.Y).setLabel("Количество исследований");
        lineModel.getAxis(AxisType.Y).setMin(0);
        lineModel.getAxis(AxisType.Y).setTickAngle(1);
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
            if(bufStudy.getSdate().after(firstdate)&&(bufStudy.getSdate().before(seconddate))){
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
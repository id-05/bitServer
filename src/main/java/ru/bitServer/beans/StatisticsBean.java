package ru.bitServer.beans;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import ru.bitServer.dao.*;
import ru.bitServer.util.LogTool;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@ManagedBean(name = "statisticsBean")
@RequestScoped
public class StatisticsBean implements UserDao {

    DashboardModel model;
    BitServerUser currentUser;
    LineChartModel lineModel;
    PieChartModel pieModality;
    PieChartModel pieSource;
    String typeChart = "mounth";
    Map<Long, Integer> resultMapLong = new TreeMap<>();
    Map<Long, Integer> resultMapShort = new TreeMap<>();
    ArrayList<String> bufDateList = new ArrayList<>();

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

    public BitServerUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BitServerUser currentUser) {
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

            BitServerResources bufResource = getBitServerResource("showStat");
            showStat = bufResource.getRvalue().equals("true");

        resultMapLong.clear();
        resultMapShort.clear();

        bufDateList = getDateStatistics();
        resultMapLong = getStatMap(bufDateList,"MM.yyyy");
        resultMapShort = getStatMap(bufDateList,"yyyy");

        if(showStat){
            System.out.println("showstat");
            typeChart = "mounth";
            chartOutput();
            createModalityPieModel();
            //createSourcePieModel();
            model = new DefaultDashboardModel();
            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();
            DashboardColumn column3 = new DefaultDashboardColumn();
            column1.addWidget("serverstatistics");
            column2.addWidget("modality");
            column3.addWidget("raid");
            model.addColumn(column1);
            model.addColumn(column2);
            model.addColumn(column3);
        }
    }

    public void createSourcePieModel(){
        List<String> buflist = new ArrayList<>();
        for(BitServerStudy bufStudy:MainBean.allStudies){
            buflist.add(bufStudy.getSource());
        }
        Set<String> set = new LinkedHashSet<>(buflist);
        sourcelist.addAll(set);

        pieSource = new PieChartModel();
        ChartData data = new ChartData();
        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        if(sourcelist.size()>0) {
            for (String bufSource : sourcelist) {
                values.add(MainBean.allStudies.stream().filter(BitServerStudy -> BitServerStudy.getSource().equals(bufSource)).count());
                labels.add(bufSource);
                bgColors.add("rgb(" + ThreadLocalRandom.current().nextInt(1, 254) +
                        ", " + ThreadLocalRandom.current().nextInt(1, 254) +
                        ", " + ThreadLocalRandom.current().nextInt(1, 254) + ")");
            }
        }
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        pieSource.setData(data);
    }

    private void createModalityPieModel() {
        pieModality = new PieChartModel();
        ChartData data = new ChartData();
        //List<BitServerModality> modalityList = getAllBitServerModality();
        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
//        for(BitServerModality bufModality:modalityList){
//            values.add(MainBean.allStudies.stream().filter(BitServerStudy->BitServerStudy.getModality().equals(bufModality.getName())).count());
//            labels.add(bufModality.getName());
//            bgColors.add( "rgb("+ThreadLocalRandom.current().nextInt(1, 254)+
//                        ", "+ThreadLocalRandom.current().nextInt(1, 254)+
//                    ", "+ThreadLocalRandom.current().nextInt(1, 254)+")");
//        }
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        pieModality.setData(data);
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
        switch (typeChart){
            case "mounth":
                lineModel = initModel("MM.yyyy");
            break;
            case "year":
                lineModel = initModel("yyyy");
            break;
        }
    }

    private LineChartModel initModel(String pattern) {
        LineChartModel bufModel = new LineChartModel();
        ChartData data = new ChartData();
        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Map<Long, Integer> resultMap;
        DateFormat formatter = new SimpleDateFormat(pattern);

        resultMap = getResultMap(pattern);
        if(resultMap.size()>0) {
            for (Map.Entry<Long, Integer> item : resultMap.entrySet()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(item.getKey());
                Date bufDate = calendar.getTime();
                values.add(item.getValue());
                labels.add(formatter.format(bufDate));
            }
        }
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Исследования");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        LineChartOptions options = new LineChartOptions();
        bufModel.setOptions(options);
        bufModel.setData(data);
        return bufModel;
    }

    public Map<Long, Integer> getResultMap(String pattern){
        Map<Long, Integer> resultMap = new TreeMap<>();
        switch (pattern){
            case("MM.yyyy"):
                resultMap = resultMapLong;
                break;
            case("yyyy"):
                resultMap = resultMapShort;
                break;
        }
        return resultMap;
    }

    public Map<Long, Integer> getStatMap(List<String> allStudies,String dateformat){
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(dateformat);
        Date firstdate;
        Date seconddate;
        try {
            firstdate = allStudies.get(0).getSdate();
            seconddate = allStudies.get(allStudies.size() - 1).getSdate();
        }catch (Exception e){
            firstdate = new Date();
            seconddate = new Date();
        }
        for(BitServerStudy bufStudy:allStudies){
            if( (bufStudy.getSdate().after(firstdate)&&(bufStudy.getSdate().before(seconddate))) |
                    ( (bufStudy.getSdate().equals(firstdate))|(bufStudy.getSdate().equals(seconddate)) ) ){
                long bufDatemillis = 0;
                try {
                    bufDatemillis = (formatter.parse(formatter.format(bufStudy.getSdate()))).getTime();
                } catch (Exception e) {
                    LogTool.getLogger().error(this.getClass().getSimpleName()+": Error getStatMap: "+e.getMessage());
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
        return resultMap;
    }
}
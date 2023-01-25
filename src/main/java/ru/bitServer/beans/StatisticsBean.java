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
import ru.bitServer.service.ColorBar;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean(name = "statisticsBean")
@ViewScoped
public class StatisticsBean implements UserDao {

    DashboardModel model;
    BitServerUser currentUser;
    LineChartModel lineModel;
    PieChartModel pieModel;
    String typeChart = "mounth";
    Map<Long, Integer> resultMapLong = new TreeMap<>();
    Map<Long, Integer> resultMapShort = new TreeMap<>();
    ArrayList<String> bufDateList = new ArrayList<>();
    String diagramTitle;
    ChartData data = new ChartData();
    PieChartDataSet dataSet = new PieChartDataSet();

    public String getDiagramTitle() {
        return diagramTitle;
    }

    public void setDiagramTitle(String diagramTitle) {
        this.diagramTitle = diagramTitle;
    }

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
        return pieModel;
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
    public void init() {
        showStat = getBitServerResource("showStat").getRvalue().equals("true");
        resultMapLong.clear();
        resultMapShort.clear();

        bufDateList = getDateFromMaindicomTags("",32);
        Collections.sort(bufDateList);

        resultMapLong = getStatMap(bufDateList,"MM.yyyy");
        resultMapShort = getStatMap(bufDateList,"yyyy");
        diagramTitle = "Распределение по модальностям";

        if(showStat){
            typeChart = "mounth";

            chartOutput();
            createPieModel();

            model = new DefaultDashboardModel();

            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();
            DashboardColumn column3 = new DefaultDashboardColumn();
            column1.addWidget("serverstatistics");
            column2.addWidget("pieName");
            column3.addWidget("raid");
            model.addColumn(column1);
            model.addColumn(column2);
            model.addColumn(column3);
        }
    }

    private void createPieModel() {
        pieModel = new PieChartModel();

        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        ColorBar colorBar = new ColorBar();
        if(diagramTitle.equals("Распределение по модальностям")) {
            List<String> modalityList = getDateFromMaindicomTags("DISTINCT", 96);
            for (String bufModality : modalityList) {
                values.add(getModalitiOfStudies().stream().filter(String -> String.equals(bufModality)).count());
                labels.add(bufModality);
                bgColors.add(colorBar.getColor());
            }
        }
        if(diagramTitle.equals("Распределение по источникам")) {
            List<String> sourceList = getSourceDicom().stream().distinct().collect(Collectors.toList());
            for (String bufSource : sourceList) {
                values.add(getSourceDicom().stream().filter(String -> String.equals(bufSource)).count());
                labels.add(bufSource);
                bgColors.add(colorBar.getColor());
            }
        }
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        pieModel.setData(data);
    }

    public void setMounths(){
        typeChart = "mounth";
        chartOutput();
    }

    public void setYears(){
        typeChart = "year";
        chartOutput();
    }

    public void setModaliti(){
        diagramTitle = "Распределение по модальностям";
        createPieModel();
    }

    public void setSource(){
        diagramTitle = "Распределение по источникам";
        createPieModel();
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

    public Map<Long, Integer> getStatMap(List<String> studiesList, String dateformat) {
        Map<Long, Integer> resultMap = new TreeMap<>();
        DateFormat formatter = new SimpleDateFormat(dateformat);
        Date firstdate;
        Date seconddate;

        try {
            firstdate = strToDate(studiesList.get(0));
            seconddate = strToDate(studiesList.get(studiesList.size() - 1));
        }catch (Exception e){
            firstdate = new Date();
            seconddate = new Date();
        }

        for(String bufStr:studiesList){
            if( (strToDate(bufStr).after(firstdate)&&(strToDate(bufStr).before(seconddate))) |
                    ( (strToDate(bufStr).equals(firstdate))|(strToDate(bufStr).equals(seconddate)) ) ){
                long bufDatemillis = 0;
                try {
                    bufDatemillis = (formatter.parse(formatter.format(strToDate(bufStr)))).getTime();
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

    public Date strToDate(String buf) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        try{
            date = formatter.parse(buf);
        }catch (Exception e){
            LogTool.getLogger().error(this.getClass().getSimpleName() + ": " + "Error on procedure: strToDate " + e.getMessage());
        }
        return date;
    }
}
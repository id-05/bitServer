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
    LineChartModel lineModel = new LineChartModel();
    PieChartModel pieModel = new PieChartModel();
    String typeChart = "mounth";
    Map<Long, Integer> resultMapLong = new TreeMap<>();
    Map<Long, Integer> resultMapShort = new TreeMap<>();
    ArrayList<String> bufDateList = new ArrayList<>();
    String diagramTitle;

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
            pieOutput();

            model = new DefaultDashboardModel();

            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();

            column1.addWidget("serverstatistics");
            column2.addWidget("pieName");

            model.addColumn(column1);
            model.addColumn(column2);

        }
    }

    private void pieOutput() {
        ChartData dataPie = new ChartData();
        PieChartDataSet dataSetPie = new PieChartDataSet();
        List<Number> valuesPie = new ArrayList<>();
        List<String> labelsPie = new ArrayList<>();
        List<String> bgColorsPie = new ArrayList<>();
        ColorBar colorBar = new ColorBar();
        if(diagramTitle.equals("Распределение по модальностям")) {
            List<String> modalityList = getDateFromMaindicomTags("DISTINCT", 96);
            for (String bufModality : modalityList) {
                valuesPie.add(getModalitiOfStudies().stream().filter(String -> String.equals(bufModality)).count());
                labelsPie.add(bufModality);
                bgColorsPie.add(colorBar.getColor());
            }
        }
        if(diagramTitle.equals("Распределение по источникам")) {
            List<String> sourceList = getSourceDicom().stream().distinct().collect(Collectors.toList());
            for (String bufSource : sourceList) {
                valuesPie.add(getSourceDicom().stream().filter(String -> String.equals(bufSource)).count());
                labelsPie.add(bufSource);
                bgColorsPie.add(colorBar.getColor());
            }
        }
        dataSetPie.setData(valuesPie);
        dataSetPie.setBackgroundColor(bgColorsPie);
        dataPie.addChartDataSet(dataSetPie);
        dataPie.setLabels(labelsPie);
        pieModel.setData(dataPie);
    }

    public void setMounths(){
        typeChart = "mounth";
        chartOutput();
        System.out.println("mounth");
    }

    public void setYears(){
        typeChart = "year";
        chartOutput();
        System.out.println("year");
    }

    public void setModaliti(){
        diagramTitle = "Распределение по модальностям";
        System.out.println("Распределение по модальностям");
        pieOutput();
    }

    public void setSource(){
        diagramTitle = "Распределение по источникам";
        System.out.println("Распределение по источникам");
        pieOutput();
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
        LineChartModel bufModelLine = new LineChartModel();
        ChartData dataLine = new ChartData();
        LineChartDataSet dataSetLine = new LineChartDataSet();
        List<Object> valuesLine = new ArrayList<>();
        List<String> labelsLine = new ArrayList<>();
        Map<Long, Integer> resultMap;
        DateFormat formatter = new SimpleDateFormat(pattern);
        resultMap = getResultMap(pattern);
        if(resultMap.size()>0) {
            for (Map.Entry<Long, Integer> item : resultMap.entrySet()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(item.getKey());
                Date bufDate = calendar.getTime();
                valuesLine.add(item.getValue());
                labelsLine.add(formatter.format(bufDate));
            }
        }
        dataSetLine.setData(valuesLine);
        dataSetLine.setFill(false);
        dataSetLine.setLabel("Исследования");
        dataSetLine.setBorderColor("rgb(75, 192, 192)");
        dataSetLine.setTension(0.1);
        dataLine.addChartDataSet(dataSetLine);
        dataLine.setLabels(labelsLine);
        LineChartOptions options = new LineChartOptions();
        bufModelLine.setOptions(options);
        bufModelLine.setData(dataLine);
        return bufModelLine;
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
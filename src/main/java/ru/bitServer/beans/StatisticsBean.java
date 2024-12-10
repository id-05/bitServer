package ru.bitServer.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    Map<String, Long> ModalityMap = new TreeMap<>();
    Map<String, Long> SourceMap = new TreeMap<>();
    String diagramTitle;

    public String getDiagramTitle() {
        return diagramTitle;
    }

    public void setDiagramTitle(String diagramTitle) {
        this.diagramTitle = diagramTitle;
    }

    boolean showStat;

    public boolean isShowStat() {
        return showStat;
    }

    public void setShowStat(boolean showStat) {
        this.showStat = showStat;
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

        if(showStat){
            resultMapLong.clear();
            resultMapShort.clear();
            ModalityMap.clear();
            SourceMap.clear();
            JsonObject statJson;
            JsonParser parser = new JsonParser();
            try {
                String sourceString = makeGetConnectionAndStringBuilder("/bitServerStat/statistics/getinfo").toString();
                statJson = parser.parse(sourceString).getAsJsonObject();

                String buf = statJson.get("DateMapLong").toString().replace("\\","").replace("{","").replace("}","").replace("\"","");
                String[] lines = buf.split(",");
                int i;
                for(String line:lines){
                    i = line.indexOf(":");
                    resultMapLong.put(Long.parseLong(line.substring(0,i)),Integer.parseInt(line.substring(i+1)));
                }

                buf = statJson.get("DateMapShort").toString().replace("\\","").replace("{","").replace("}","").replace("\"","");
                lines = buf.split(",");
                for(String line:lines){
                    i = line.indexOf(":");
                    resultMapShort.put(Long.parseLong(line.substring(0,i)),Integer.parseInt(line.substring(i+1)));
                }

                buf = statJson.get("ModalityMap").toString().replace("\\","").replace("{","").replace("}","").replace("\"","");
                lines = buf.split(",");
                for(String line:lines){
                    i = line.indexOf(":");
                    ModalityMap.put(line.substring(0,i),Long.parseLong(line.substring(i+1)));
                }

                buf = statJson.get("SourceMap").toString().replace("\\","").replace("{","").replace("}","").replace("\"","");
                lines = buf.split(",");
                for(String line:lines){
                    i = line.indexOf(":");
                    SourceMap.put(line.substring(0,i),Long.parseLong(line.substring(i+1)));
                }


            }catch (Exception e){
                LogTool.getLogger().error(this.getClass().getSimpleName()+": (Error during get state):"+ e.getMessage());
            }

            diagramTitle = "Распределение по модальностям";
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

    public StringBuilder makeGetConnectionAndStringBuilder(String apiUrl) {
        StringBuilder stringBuilder;
        try {
            stringBuilder = new StringBuilder();
            HttpURLConnection conn = makeGetConnection(apiUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            LogTool.getLogger().error("Error makeGetConnectionAndStringBuilder restApi "+e.getMessage());
            stringBuilder = new StringBuilder();
            stringBuilder.append("error");
        }
        return stringBuilder;
    }

    public HttpURLConnection makeGetConnection(String apiUrl) throws Exception {
        HttpURLConnection conn;
        String fulladdress = "http://127.0.0.1:8080";
        URL url = new URL(fulladdress+apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.getResponseMessage();
        return conn;
    }
    private void pieOutput() {
        ChartData dataPie = new ChartData();
        PieChartDataSet dataSetPie = new PieChartDataSet();
        List<Number> valuesPie = new ArrayList<>();
        List<String> labelsPie = new ArrayList<>();
        List<String> bgColorsPie = new ArrayList<>();
        ColorBar colorBar = new ColorBar();
        if(diagramTitle.equals("Распределение по модальностям")) {
            if(ModalityMap.size()>0) {
                for (Map.Entry<String, Long> item : ModalityMap.entrySet()) {
                    valuesPie.add(item.getValue());
                    labelsPie.add(item.getKey());
                    bgColorsPie.add(colorBar.getColor());
                }
            }
        }
        if(diagramTitle.equals("Распределение по источникам")) {
            if(SourceMap.size()>0) {
                for (Map.Entry<String, Long> item : SourceMap.entrySet()) {
                    valuesPie.add(item.getValue());
                    labelsPie.add(item.getKey());
                    bgColorsPie.add(colorBar.getColor());
                }
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
    }

    public void setYears(){
        typeChart = "year";
        chartOutput();
    }

    public void setModaliti(){
        diagramTitle = "Распределение по модальностям";
        pieOutput();
    }

    public void setSource(){
        diagramTitle = "Распределение по источникам";
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
}
package ru.bitServer.beans;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.BitServerStudy;
import ru.bitServer.dao.UserDao;
import ru.bitServer.service.SourceDevice;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@ManagedBean(name = "reportBean")
@ViewScoped
public class ReportBean implements UserDao {


    String FONT;

    Font font;
    Font fontsmall;
    final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy.MM.dd");
    final SimpleDateFormat FORMAT2 = new SimpleDateFormat("dd.MM.yyyy");
    String filtrDate = "all";
    Date firstdate;
    Date seconddate;
    boolean datepickerVisible1;
    boolean datepickerVisible2;
    String pdfFilePath;

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    List<String> selectedModalitiName = new ArrayList<>();

    List<String> modalityName = new ArrayList<>();

    List<SourceDevice> devicesList = new ArrayList<>();

    SourceDevice selectedSourceDevice;

    public SourceDevice getSelectedSourceDevice() {
        return selectedSourceDevice;
    }

    public void setSelectedSourceDevice(SourceDevice selectedSourceDevice) {
        this.selectedSourceDevice = selectedSourceDevice;
    }

    public List<SourceDevice> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(List<SourceDevice> devicesList) {
        this.devicesList = devicesList;
    }

    List<BitServerStudy> StudiesList;

    List<SourceDevice> selectedDevicesList = new ArrayList<>();

    public List<SourceDevice> getSelectedDevicesList() {
        return selectedDevicesList;
    }

    public void setSelectedDevicesList(List<SourceDevice> selectedDevicesList) {
        this.selectedDevicesList = selectedDevicesList;
    }

    Map<String, Long> sourceMap = new TreeMap<>();

    public List<String> getModalityName() {
        return modalityName;
    }

    public void setModalityName(List<String> modalityName) {
        this.modalityName = modalityName;
    }

    public List<String> getSelectedModalitiName() {
        return selectedModalitiName;
    }

    public void setSelectedModalitiName(List<String> selectedModalitiName) {
        this.selectedModalitiName = selectedModalitiName;
    }

    public boolean isDatepickerVisible1() {
        return datepickerVisible1;
    }

    public String getGetModalitiNameToString() {
        return selectedModalitiName.toString();
    }

    public void setDatepickerVisible1(boolean datepickerVisible1) {
        this.datepickerVisible1 = datepickerVisible1;
    }

    public boolean isDatepickerVisible2() {
        return datepickerVisible2;
    }

    public void setDatepickerVisible2(boolean datepickerVisible2) {
        this.datepickerVisible2 = datepickerVisible2;
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
    private void init() {
        firstdate = new Date();
        seconddate = new Date();
        devicesList = getDeviceList();
        FONT = getBitServerResource("FontForPdfFilePath").getRvalue();//"D://font/ArialRegular.ttf";
        for(SourceDevice bufDevice:devicesList){
            selectedModalitiName.add(bufDevice.getVisibleName());
            modalityName = selectedModalitiName;
        }
        selectedSourceDevice = new SourceDevice();
//        for(SourceDevice bufDevice:devicesList){
//            System.out.println(bufDevice.getStationName()+" "+bufDevice.getModality()+" "+bufDevice.getVisibleName()+" "+bufDevice.getLastActive());
//        }
        pdfFilePath = getBitServerResource("pdfFilePath").getRvalue();
        if (pdfFilePath.equals("empty")){
            pdfFilePath = "/dataimage/results";
        }
    }

    public void onDeviceEdit(){
        for(SourceDevice bufDevice:devicesList){
            if(!bufDevice.getVisibleName().equals(getBitServerResource(bufDevice.getStationName()).getRvalue())){
                BitServerResources bufResource = getBitServerResource(bufDevice.getStationName());
                bufResource.setRvalue(bufDevice.getVisibleName());
                updateBitServiceResource(bufResource);
            }
        }
        devicesList.clear();
        selectedModalitiName.clear();
        devicesList = getDeviceList();
        for(SourceDevice bufDevice:devicesList){
            selectedModalitiName.add(bufDevice.getVisibleName());
            modalityName = selectedModalitiName;
        }
        PrimeFaces.current().ajax().update(":reportform:");
    }

    public void onUpdate(){
        System.out.println("update");
        //PrimeFaces.current().ajax().update(":reportform:test");
    }

    public void dataoutput() throws Exception {
        if(filtrDate.equals("targetdate")){
            datepickerVisible1 = true;
            datepickerVisible2 = false;
        }else{
            if(filtrDate.equals("range")){
                datepickerVisible1 = true;
                datepickerVisible2 = true;
            }else{
                datepickerVisible1 = false;
                datepickerVisible2 = false;
            }
        }
        List<String> realModalityName = new ArrayList<>();
        for(String buf:selectedModalitiName){
            for(SourceDevice bufDevice:devicesList){
                if(bufDevice.getVisibleName().equals(buf)){
                    realModalityName.add(bufDevice.getStationName());
                }
            }
        }
        StudiesList = getStudyFromOrthancShort(filtrDate, firstdate, seconddate, realModalityName);
        PrimeFaces.current().ajax().update(":reportform:");
        pdfCreate(StudiesList);
    }

    public StreamedContent getGetResult() throws Exception {
        dataoutput();
        String strpath = pdfFilePath+"/report.pdf";
        InputStream inputStream = new FileInputStream(strpath);
        return DefaultStreamedContent.builder()
                .name("report.pdf")
                .contentType("application/pdf")
                .stream(() -> inputStream)
                .build();
    }

    public Boolean firstDateSelect() {
        //dataoutput();
        return true;
    }

    public Boolean secondDateSelect() {
        //dataoutput();
        return true;
    }

    public void pdfCreate(List<BitServerStudy> StudiesList) throws Exception {
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font = new Font(bf,16,Font.NORMAL);
        fontsmall = new Font(bf,12,Font.NORMAL);

        Document document = new Document();
        PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(pdfFilePath+"/report.pdf")));
        document.open();

        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Chunk chunk = new Chunk("Отчет о полученных сервером исследованиях", font);
        p.add(chunk);
        document.add(p);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        String buf;
        if(filtrDate.equals("all")){
            buf = "за всё время";
        }else{
            if(filtrDate.equals("targetdate")){
                buf = "за "+ FORMAT2.format(firstdate);
            }else {
                buf = "с " + FORMAT2.format(firstdate) + " по " + FORMAT2.format(seconddate);
            }
        }
        document.add(new Chunk("Период: "+buf, fontsmall));
        document.add(new Paragraph("\n"));

        String listString = String.join(", ",selectedModalitiName);

        document.add(new Chunk("Для источника: "+listString, fontsmall));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Chunk("      Таблица AET Источник / COUNT ", fontsmall));
        document.add(new Paragraph("\n"));
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);
        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Chunk("      Таблица AET Источник / STUDY ", fontsmall));
        document.add(new Paragraph("\n"));
        PdfPTable table2 = new PdfPTable(6);
        addTableHeader2(table2);
        addRows2(table2);
        float[] columnWidths = new float[]{7f, 20f, 20f, 20f, 20f, 20f};
        table2.setWidths(columnWidths);
        table2.setWidthPercentage(90);
        document.add(table2);
        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("№", "Источник", "Количетсво")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setBorderWidth(2);
                    Phrase p = new Phrase(columnTitle, fontsmall);
                    header.setPhrase(p);
                    table.addCell(header);
                });
    }

    private void addTableHeader2(PdfPTable table) {
        Stream.of("№", "Источник","ID", "ФИО", "Дата", "Описание")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setBorderWidth(2);
                    Phrase p = new Phrase(columnTitle, fontsmall);
                    header.setPhrase(p);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        ArrayList<String> bufList = new ArrayList<>();
        String buf;
        for(BitServerStudy bufStudy:StudiesList){
            if(bufStudy.getStationName().equals("")){
                buf = "Manual upload";
            }else{
                buf = bufStudy.getStationName();
            }
            bufList.add(buf);
        }
        sourceMap.clear();
        for (String bufSource : bufList) {
            sourceMap.put(bufSource, bufList.stream().filter(String -> String.equals(bufSource)).count());
        }
        int i=0;
        for (Map.Entry<String, Long> item : sourceMap.entrySet()) {
            table.addCell(String.valueOf(i+1));
            Phrase p = new Phrase(getVisibleNameByStationName(item.getKey()), fontsmall);
            table.addCell(p);
            table.addCell(String.valueOf(item.getValue()));
            i++;
        }
    }

    private void addRows2(PdfPTable table) {
        int i=1;
        for(BitServerStudy bufStudy:StudiesList){
            table.addCell(String.valueOf(i));
            Phrase p = new Phrase(getVisibleNameByStationName(bufStudy.getStationName()), fontsmall);
            table.addCell(p);
            p = new Phrase(bufStudy.getShortid(), fontsmall);
            table.addCell(p);
            p = new Phrase(bufStudy.getPatientName(), fontsmall);
            table.addCell(p);
            table.addCell(FORMAT.format(bufStudy.getSdate()));
            p = new Phrase(bufStudy.getSdescription(), fontsmall);
            table.addCell(p);
            i++;
        }
    }

    public String getVisibleNameByStationName(String StationName){
        String result = "";
        for(SourceDevice buf:devicesList){
            if(buf.getStationName().equals(StationName)){
                return buf.getVisibleName();
            }
        }
        return result;
    }
}

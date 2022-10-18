package ru.bitServer.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.dicom.DicomTag;
import ru.bitServer.dicom.OrthancStudy;
import ru.bitServer.dicom.UploadDicomFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

@ManagedBean(name = "tagEditorBean")
@ViewScoped
public class TagEditor {

    ArrayList<DicomTag> TagList = new ArrayList<>();
    ArrayList<UploadDicomFile> FileList = new ArrayList<>();
    DicomTag selectTag = new DicomTag();
    UploadDicomFile selectedFile;
    boolean selectedExit = false;

    public boolean isSelectedExit() {
        return selectedExit;
    }

    public void setSelectedExit(boolean selectedExit) {
        this.selectedExit = selectedExit;
    }

    public ArrayList<UploadDicomFile> getFileList() {
        return FileList;
    }

    public void setFileList(ArrayList<UploadDicomFile> fileList) {
        FileList = fileList;
    }

    public UploadDicomFile getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(UploadDicomFile selectedFile) {
        this.selectedFile = selectedFile;
    }

    public ArrayList<DicomTag> getTagList() {
        return TagList;
    }

    public void setTagList(ArrayList<DicomTag> tagList) {
        TagList = tagList;
    }

    public DicomTag getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(DicomTag selectTag) {
        this.selectTag = selectTag;
    }

    @PostConstruct
    public void init() {

    }

    public void handleFileUpload(FileUploadEvent event) throws IOException, SQLException {
        UploadedFile file = event.getFile();

        UploadDicomFile dfile = new UploadDicomFile(file.getContent(),file.getFileName());

        FileList.add(dfile);
        prepareTagTable(dfile.getData());
        PrimeFaces.current().executeScript("PF('addDICOM').hide()");
        PrimeFaces.current().ajax().update(":tageditor:tabview1:dt-files");
    }

    public void prepareTagTable(byte[] file) throws IOException {
        TagList.clear();
        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(file));
        Attributes attributes = din.readDataset(-1, -1);
        //Attributes fmi = din.readFileMetaInformation();

        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        for (Field field : fields){
            try {
                String buf = attributes.getString(field.getInt(field.getName()),"none");
                if(!buf.equals("none")){
                    DicomTag bufTag = new DicomTag(field.getName(),attributes.getString(field.getInt(field.getName()),"none"));
                    TagList.add(bufTag);
                }

            }catch (Exception e){

            }
        }
        PrimeFaces.current().ajax().update(":tageditor:tabview1:dt-tags");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        selectTag.setHasChange(true);
        System.out.println("selectTag ="+selectTag.getTagName());
        if (newValue != null && !newValue.equals(oldValue)) {
            System.out.println("Old: " + oldValue + ", New:" + newValue);
        }
    }

    public void onTabChange(){
        System.out.println("tab change");
    }

    public void onFileSelect() throws IOException {
        prepareTagTable(selectedFile.getData());
        selectedExit = true;
        PrimeFaces.current().ajax().update(":tageditor:tabview1:saveselected");
    }

    public StreamedContent saveChangeCurrent() throws IOException, IllegalAccessException {
        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(selectedFile.getData()));
        Attributes attributes = din.readDataset(-1, -1);
        Attributes fmi = din.readFileMetaInformation();

        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        for(DicomTag bufTag:TagList){
            if(bufTag.getTagName().equals("PatientName")){
                Field bufField = getField(bufTag.getTagName(),fields);
                attributes.setString(bufField.getInt(bufField.getName()), VR.valueOf(567), bufTag.getTagValue());
            }
        }

        File f = new File("newFile.dcm");
        DicomOutputStream dos = new DicomOutputStream(f);
        dos.writeDataset(fmi, attributes);

        byte[] bytes = Files.readAllBytes(f.toPath());
        InputStream inputStream = new ByteArrayInputStream(bytes);//selectedFile.getData());
        return DefaultStreamedContent.builder()
                .name(selectedFile.getFileName())
                .contentType("application/dcm")
                .stream(() -> inputStream)
                .build();
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    public void saveChangeAll(){

    }

    public String getTagValue(String TagName, ArrayList<DicomTag> TagArray){
        String buf = "";
        for(DicomTag bufTag:TagArray){
            if(bufTag.getTagName().equals(TagName)){
                return bufTag.getTagValue();
            }
        }
        return buf;
    }

    public Field getField(String TagName, Field[] fields){
        Field buf = null;
        for(Field bufField:fields){
            if(bufField.getName().equals(TagName)){
                return bufField;
            }
        }
        return buf;
    }
}

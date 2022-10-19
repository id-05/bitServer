package ru.bitServer.beans;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import ru.bitServer.dicom.DicomTag;
import ru.bitServer.dicom.UploadDicomFile;
import ru.bitServer.util.LogTool;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@ManagedBean(name = "tagEditorBean")
@ViewScoped
public class TagEditor {

    ArrayList<DicomTag> TagList = new ArrayList<>();
    ArrayList<UploadDicomFile> FileList = new ArrayList<>();
    DicomTag selectTag = new DicomTag();
    UploadDicomFile selectedFile;
    boolean selectedExist = false;

    public boolean isSelectedExist() {
        return selectedExist;
    }

    public void setSelectedExist(boolean selectedExist) {
        this.selectedExist = selectedExist;
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
        Attributes attributes = din.readDataset();
        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        for (Field field : fields){
            try {
                String[] bufStrings = attributes.getStrings(field.getInt(field.getName()));
                if(bufStrings.length>0 && (field.getInt(field.getName()) != Tag.PixelData) ){
                    DicomTag bufTag = new DicomTag(field.getName(),Arrays.toString(bufStrings).replace("[","").replace("]",""));
                    TagList.add(bufTag);
                }

            }catch (Exception e){
                LogTool.getLogger().info(this.getClass().getSimpleName()+": "+ "Error during parce DICOM Tag: "+e.getMessage());
            }
        }
        PrimeFaces.current().ajax().update(":tageditor:tabview1:dt-tags");
    }

    public void onTagSelect(){
        selectTag.setHasChange(true);
        System.out.println("selectTag = "+selectTag.getTagName());
    }

    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//        selectTag.setHasChange(true);
//        System.out.println("selectTag = "+selectTag.getTagName());
//        System.out.println("selectTag ="+selectTag.getTagName());
//        if (newValue != null && !newValue.equals(oldValue)) {
//            System.out.println("Old: " + oldValue + ", New:" + newValue);
//        }
    }

//    public void selectedTag() {
//        selectTag.setHasChange(true);
//        System.out.println("selectTag = "+selectTag.getTagName());
//    }

    public void onTabChange(){
        //System.out.println("tab change");
    }

    public void onFileSelect() throws IOException {
        prepareTagTable(selectedFile.getData());
        selectedExist = true;
        PrimeFaces.current().ajax().update(":tageditor:tabview1:saveselected");
    }

    public StreamedContent saveChangeCurrent() throws IOException {
        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(selectedFile.getData()));
        Attributes attributes = din.readDataset();
        Attributes fmi = din.readFileMetaInformation();
        Tag tag = new Tag();
        Field[] fields = tag.getClass().getDeclaredFields();
        for(DicomTag bufTag:TagList){
           // if(bufTag.isHasChange()) {
                //System.out.println(bufTag.getTagName());
                Field bufField = getField(bufTag.getTagName(), fields);
                try {
                    attributes.setString(bufField.getInt(bufField.getName()),
                            attributes.getVR(bufField.getInt(bufField.getName())),
                            bufTag.getTagValue().replace(", ", "\\"));
                } catch (Exception e) {
                    LogTool.getLogger().info(this.getClass().getSimpleName() + ": " + "Save dicom file trouble, maybe VR compatible only number: " + e.getMessage());
                }
           // }
        }

        File f = new File("newFile.dcm");
        DicomOutputStream dos = new DicomOutputStream(f);
        dos.writeDataset(fmi, attributes);
        byte[] bytes = Files.readAllBytes(f.toPath());
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return DefaultStreamedContent.builder()
                .name(selectedFile.getFileName())
                .contentType("application/dcm")
                .stream(() -> inputStream)
                .build();
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

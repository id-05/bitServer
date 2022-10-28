package ru.bitServer.beans;

//import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.image.ColorModelFactory;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
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
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//import ru.bitServer.beans.AdminBean.get16bitBuffImage;

@ManagedBean(name = "tagEditorBean")
@SessionScoped
public class TagEditor {

    ArrayList<DicomTag> TagList = new ArrayList<>();
    ArrayList<UploadDicomFile> FileList = new ArrayList<>();
    DicomTag selectTag = new DicomTag();
    UploadDicomFile selectedFile;
    boolean selectedExist = false;
    byte[] curDicom;

    public byte[] getCurDicom() {
        return curDicom;
    }

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
        ImageIO.scanForPlugins();
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException, SQLException {
        UploadedFile file = event.getFile();
        UploadDicomFile dfile = new UploadDicomFile(file.getContent(),file.getFileName());
        FileList.add(dfile);
        //prepareTagTable(dfile.getData());
        PrimeFaces.current().executeScript("PF('addDICOM').hide()");
        PrimeFaces.current().ajax().update(":tageditor:tabview1:dt-files");
    }

    public void prepareTagTable(byte[] file) throws IOException {
        TagList.clear();
        curDicom = file;
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
                //LogTool.getLogger().info(this.getClass().getSimpleName()+": "+ "Error during parce DICOM Tag: "+e.getMessage());
            }
        }
        PrimeFaces.current().ajax().update(":tageditor:tabview1:dt-tags");
        PrimeFaces.current().ajax().update(":tageditor:tabview1:img1");
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

    public BufferedImage createBufferedImgdFromDICOMfile(byte[] dicomf) throws IOException, IllegalAccessException {
        Raster raster = null ;
        System.out.println("Input: " + dicomf.length);
        //Open the DICOM file and get its pixel data
        try {
            ImageIO.scanForPlugins();
            File f = new File("");
            ByteArrayInputStream bais = new ByteArrayInputStream(dicomf);
            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");
            ImageReader reader = (ImageReader) iter.next();
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            ImageInputStream iis = ImageIO.createImageInputStream(bais);
            reader.setInput(iis, false);
            //Returns a new Raster (rectangular array of pixels) containing the raw pixel data from the image stream
            raster = reader.readRaster(0, param);
            if (raster == null)
                System.out.println("Error: couldn't read Dicom image!");
            iis.close();
        }
        catch(Exception e) {
            System.out.println("Error: couldn't read dicom image! "+ e.getMessage());
        }
        return get16bitBuffImage(raster);
    }

    public StreamedContent getGraphicText() {
        //try {
        if(curDicom!=null){
            System.out.println("curDicom1 = "+curDicom.length);
        }

        return DefaultStreamedContent.builder()
                .contentType("image/png")
                .name("test")
                .stream(() -> {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(curDicom);//new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImg, "png", os);
                        int w = bufferedImg.getWidth(null);
                        System.out.println("w = "+w);
                        int h = bufferedImg.getHeight(null);
                        System.out.println("h = "+w);
                        // image is scaled two times at run time]
                        int scale = 2;
                        if(w==512){
                            scale = 1;
                        }

                        BufferedImage bi = new BufferedImage(w * scale, h * scale,
                                BufferedImage.TYPE_USHORT_GRAY);
                        Graphics g = bi.getGraphics();
                        g.drawImage(bufferedImg, 1, 1, w * scale, h * scale, null);
                        ImageIO.write(bi, "png", bos);
                        return new ByteArrayInputStream(bos.toByteArray());//, "image/png");
                    }
                    catch (Exception e) {
                        System.out.println("error = "+e.getMessage());
                        LogTool.getLogger().info("here "+e.getMessage());
                        return null;
                    }
                })
                .build();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    public BufferedImage get16bitBuffImage(Raster raster) throws IOException{
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                new int[]{16},
                false,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_USHORT);

        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(curDicom));
        Attributes attributes = din.readDataset();
        Attributes fmi = din.readFileMetaInformation();

        int windowWidth = Integer.parseInt(attributes.getString(Tag.WindowWidth , "1"));
        int windowCenter = Integer.parseInt(attributes.getString(Tag.WindowCenter , "1"));

        int minY = 0;
        int maxY = 0;

        for(short buf:pixels){
            if(buf > maxY){
                maxY = buf;
            }
        }
        System.out.println("maxY = "+maxY);
        minY = maxY;
        for(short buf:pixels){
            if(buf < minY){
                minY = buf;
            }
        }

        int koef = 65536 / maxY;

        for(int i=0;i<pixels.length;i++){
//            short buf = pixels[i];
//            if (pixels[i] <= (windowCenter - 0.5 - (windowWidth-1) /2)) {
//                pixels[i] = (short) minY;
//            } else if (pixels[i] > (windowCenter - 0.5 + (windowWidth-1) /2)){
//                pixels[i] = (short) maxY;
//            } else {
//                pixels[i] = (short) (((pixels[i] - (windowCenter - 0.5)) / (windowWidth-1) + 0.5) * (maxY- minY) + minY);
//            }
           pixels[i] = Short.valueOf((short) (pixels[i] * koef));
        }
        DataBufferUShort db = new DataBufferUShort(pixels, pixels.length);
        WritableRaster outRaster = Raster.createInterleavedRaster(
                db,
                raster.getWidth(),
                raster.getHeight(),
                raster.getWidth(),
                1,
                new int[1],
                null);
        return new BufferedImage(colorModel, outRaster, false, null);
    }

}

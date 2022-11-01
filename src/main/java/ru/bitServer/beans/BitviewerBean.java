package ru.bitServer.beans;

import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.dao.DataAction;
import ru.bitServer.dao.UserDao;
import ru.bitServer.dicom.OrthancSerie;
import ru.bitServer.util.LogTool;
import ru.bitServer.util.OrthancRestApi;
import ru.bitServer.util.SessionUtils;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "bitviewerBean")
@SessionScoped
public class BitviewerBean implements UserDao, DataAction {

    ArrayList<OrthancSerie> seriesList = new ArrayList();
    OrthancSerie selectedSerie = new OrthancSerie();
    int curInstance = 0;
    OrthancRestApi conn;
    int number1 = 0;
    int x;
    int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public ArrayList<OrthancSerie> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(ArrayList<OrthancSerie> seriesList) {
        this.seriesList = seriesList;
    }

    public OrthancSerie getSelectedSerie() {
        return selectedSerie;
    }

    public void setSelectedSerie(OrthancSerie selectedSerie) {
        this.selectedSerie = selectedSerie;
    }

    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession() ;
        String studyId = session.getAttribute("study").toString();
        seriesList = getSeriesFromStudy(studyId);
        selectedSerie = seriesList.get(0);
        conn = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
    }

    public void onSerieSelected(){
        curInstance = 0;
        PrimeFaces.current().ajax().update(":bitviewer:img1");
    }

    public void onSerieSelected2(){
        System.out.println("split event");
        curInstance = number1;
        PrimeFaces.current().ajax().update(":bitviewer:img1");
    }

    public void imageclick(){
        System.out.println("imageclick");
        System.out.println(x + ", " + y);
    }

    public void nextInstance(){
        if((curInstance+1)<selectedSerie.getInstancesOfByte().size()){
            curInstance = curInstance + 1;
        }
        PrimeFaces.current().ajax().update(":bitviewer:img1");
    }

    public void lastInstance(){
        if(curInstance>0){
            curInstance = curInstance - 1;
        }
        PrimeFaces.current().ajax().update(":bitviewer:img1");
    }

    public StreamedContent getImgDcm() {
        System.out.println("getImgDcm "+curInstance);
        byte[] bufFile = selectedSerie.getInstancesOfByte().get(curInstance);
        return DefaultStreamedContent.builder()
                .contentType("image/png")
                .stream(() -> {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(bufFile);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImg, "png", os);
                        int w = bufferedImg.getWidth(null);
                        int h = bufferedImg.getHeight(null);
                        int scale = 2;
                        if(w==512){
                            scale = 1;
                        }
                        BufferedImage bi = new BufferedImage(w * scale, h * scale,
                                BufferedImage.TYPE_USHORT_GRAY);
                        Graphics g = bi.getGraphics();
                        g.drawImage(bufferedImg, 1, 1, w * scale, h * scale, null);
                        ImageIO.write(bi, "png", bos);
                        return new ByteArrayInputStream(bos.toByteArray());
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

    public BufferedImage createBufferedImgdFromDICOMfile(byte[] dicomf) throws IOException {
        Raster raster = null ;
        try {
            ImageIO.scanForPlugins();
            File f = new File("");
            ByteArrayInputStream bais = new ByteArrayInputStream(dicomf);
            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");
            ImageReader reader = (ImageReader) iter.next();
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            ImageInputStream iis = ImageIO.createImageInputStream(bais);
            reader.setInput(iis, false);
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

    public BufferedImage get16bitBuffImage(Raster raster) throws IOException{
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                new int[]{16},
                false,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_USHORT);
        int minY = 0;
        int maxY = 0;

        for(short buf:pixels){
            if(buf > maxY){
                maxY = buf;
            }
        }
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

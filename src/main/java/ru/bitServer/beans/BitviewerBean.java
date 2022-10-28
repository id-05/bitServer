package ru.bitServer.beans;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.io.DicomInputStream;
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
import javax.faces.bean.ViewScoped;
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
        PrimeFaces.current().ajax().update(":bitviewer:img1");
    }

    public void lastInstance(){
        if((curInstance+1)<selectedSerie.getInstances().size()){
            curInstance++;
        }
        onSerieSelected();
    }

    public void nextInstance(){
        if(curInstance>0){
            curInstance--;
        }
        onSerieSelected();
    }

    public StreamedContent getImgDcm() throws Exception {
        byte[] bufFile = getDicomAsByte(conn, selectedSerie.getInstances().get(curInstance));
        System.out.println(bufFile.length);

        return DefaultStreamedContent.builder()
                .contentType("image/png")
                .name("test")
                .stream(() -> {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(bufFile);//new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
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

    public BufferedImage get16bitBuffImage(Raster raster) throws IOException{
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                new int[]{16},
                false,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_USHORT);

//        DicomInputStream din = new DicomInputStream(new ByteArrayInputStream(curDicom));
//        Attributes attributes = din.readDataset();
//        Attributes fmi = din.readFileMetaInformation();
//
//        int windowWidth = Integer.parseInt(attributes.getString(Tag.WindowWidth , "1"));
//        int windowCenter = Integer.parseInt(attributes.getString(Tag.WindowCenter , "1"));

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

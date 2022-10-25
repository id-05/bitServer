package ru.bitServer.beans;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.bitServer.util.LogTool;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;

//import ru.bitServer.beans.AdminBean.get16bitBuffImage;


@ManagedBean(name = "BufBean")
@RequestScoped
public class BufBean {

    byte[] curDicom = null;

    @PostConstruct
    public void init() {
      //  curDicom = TagEditor.getCurDicom();
    }

    public BufferedImage createBufferedImgdFromDICOMfile(byte[] dicomf) {
        Raster raster = null ;
        System.out.println("Input: " + dicomf.length);

        //Open the DICOM file and get its pixel data
        try {
            //byte[] dicomf = new byte[0];
            ImageIO.scanForPlugins();

            File f = new File("");
            ByteArrayInputStream bais = new ByteArrayInputStream(dicomf);
            //Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");

            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");

            ImageReader reader = (ImageReader) iter.next();

            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();

            //System.out.println("param.getWindowIndex()= "+param.getWindowIndex()+" "+param.getWindowCenter()+" "+param.);

            ImageInputStream iis = ImageIO.createImageInputStream(bais);

            //ImageInputStream iis = ImageIO.createImageInputStream(dicomf);//dicomFile);
            reader.setInput(iis, false);

            //Returns a new Raster (rectangular array of pixels) containing the raw pixel data from the image stream
            raster = reader.readRaster(0, param);
            //System.out.println("6");
            if (raster == null)
                System.out.println("Error: couldn't read Dicom image!");
            iis.close();
        }
        catch(Exception e) {
            System.out.println("Error: couldn't read dicom image! "+ e.getMessage());
            //e.printStackTrace();
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
                        System.out.println("yesQ");
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        BufferedImage bufferedImg = createBufferedImgdFromDICOMfile(curDicom);//new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImg, "png", os);
                        //System.out.println(bufferedImg.);
                        int w = bufferedImg.getWidth(null);
                        System.out.println("w = "+w);
                        int h = bufferedImg.getHeight(null);
                        System.out.println("h = "+w);

                        // image is scaled two times at run time
                        int scale = 2;

                        BufferedImage bi = new BufferedImage(w * scale, h * scale,
                                BufferedImage.TYPE_INT_ARGB);
                        Graphics g = bi.getGraphics();

                        g.drawImage(bufferedImg, 2, 2, w * scale, h * scale, null);

                        ImageIO.write(bi, "png", bos);

                        return new ByteArrayInputStream(bos.toByteArray());//, "image/png");

                        //   return new ByteArrayInputStream(os.toByteArray());
//                            BufferedImage bufferedImg = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
//                            Graphics2D g2 = bufferedImg.createGraphics();
//                            g2.drawString("This is a text", 0, 10);
//                            ByteArrayOutputStream os = new ByteArrayOutputStream();
//                            ImageIO.write(bufferedImg, "png", os);
//                            return new ByteArrayInputStream(os.toByteArray());
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

    public BufferedImage get16bitBuffImage(Raster raster) {
        //System.out.println("8");
        short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
        //System.out.println("9");
        ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                new int[]{16},
                false,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_USHORT);
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

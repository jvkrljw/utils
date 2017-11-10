package qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtil {
	private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码大小
    private static final int QRCODE_SIZE = 300;
    // LOGO的宽
    private static final int WIDTH = 60;
    // LOGO的高
    private static final int HEIGHT = 60;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static BufferedImage createImage(String content, String imgPath,
            boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
     // 锟斤拷锟斤拷图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }
    
    private static void insertImage(BufferedImage source, String imgPath,
            boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(""+imgPath+"   锟斤拷锟侥硷拷锟斤拷锟斤拷锟节ｏ拷");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压锟斤拷LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
            		Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 锟斤拷锟斤拷锟斤拷小锟斤拷锟酵�
            g.dispose();
            src = image;
        }
     // 锟斤拷锟斤拷LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }
    
    public static String encode(String content, String imgPath, String destPath,
            boolean needCompress,String fileName) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        if(fileName == null || "".equals(fileName)){
        	fileName = new Random().nextInt(99999999)+".jpg";
        }else{
        	fileName += ".jpg";
        }
        ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+fileName));
        return fileName;
    }
    
    public static void mkdirs(String destPath) {
        File file =new File(destPath);   
        //锟斤拷锟侥硷拷锟叫诧拷锟斤拷锟斤拷时锟斤拷mkdirs锟斤拷锟皆讹拷锟斤拷锟斤拷锟斤拷锟侥柯硷拷锟斤拷锟斤拷锟斤拷mkdir锟斤拷(mkdir锟斤拷锟侥柯硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟阶筹拷锟届常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }
    
    public static String encode(String content, String imgPath, String destPath,String fileName)
            throws Exception {
        return QRCodeUtil.encode(content, imgPath, destPath, false, fileName);
    }

   
    public static String encode(String content, String destPath,
            boolean needCompress,String fileName) throws Exception {
        return QRCodeUtil.encode(content, null, destPath, needCompress, fileName);
    }

   
    public static String encode(String content, String destPath,String fileName) throws Exception {
        return QRCodeUtil.encode(content, null, destPath, false, fileName);
    }
    
    public static void encode(String content, String imgPath,
            OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

   
    public static void encode(String content, OutputStream output)
            throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }
    
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }
    
    
}

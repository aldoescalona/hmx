package mx.historicos.api.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/*
 * @author mkyong
 *
 */
public class IOImageScalator {

    private BufferedImage source;

    public IOImageScalator(InputStream is) throws IOException {
        source = ImageIO.read(is);
    }

    public void scale(int maxWidth, int maxHeight, String fileout) throws IOException {
        scale(maxWidth, maxHeight, new File(fileout));
    }

    public void scale(int maxWidth, int maxHeight, File fileout) throws IOException {
        
        if (!fileout.getParentFile().exists()) {
                fileout.getParentFile().mkdirs();
            }
        
        float width = source.getWidth();
        float height = source.getHeight();

        float scale = 1;

        if (shouldResizeWidth(maxWidth)) {
            scale = maxWidth / width;
        }

        if (shouldResizeHeight(maxHeight)) {
            float heightScale = maxHeight / height;
            if (heightScale < scale) {
                scale = heightScale;
            }
        }

        Float neww = width * scale;
        Float newh = height * scale;

        int type = source.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : source.getType();
        BufferedImage resizedImage = new BufferedImage(neww.intValue(), newh.intValue(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(source, 0, 0, neww.intValue(), newh.intValue(), null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        ImageIO.write(resizedImage, "png", fileout);
    }

    private boolean shouldResizeWidth(int maxWidth) {
        return source.getWidth() > maxWidth;
    }

    private boolean shouldResizeHeight(int maxHeight) {
        return source.getHeight() > maxHeight;
    }

    // private static final int IMG_WIDTH = 100;
    // private static final int IMG_HEIGHT = 100;

    
    
}

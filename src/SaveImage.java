import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SaveImage {
  public static void save(GridUI grid, int iteration) throws Exception {
  
    BufferedImage bImg = new BufferedImage(grid.GRID_WIDTH, grid.GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D cg = bImg.createGraphics();
    grid.paint(cg);
    try {
            if (ImageIO.write(bImg, "png", new File("./output/output_image" + iteration + ".png")))
            {
                System.out.println("-- saved");
            }
    } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
    }
  
  }
}
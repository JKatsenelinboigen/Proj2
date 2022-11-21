import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SaveImage {
  public static void save(int index) throws Exception {
    Robot robot = new Robot();
    int x = 100;
    int y = 100;
    int width = 200;
    int height = 200;
    Rectangle area = new Rectangle(x, y, width, height);
    BufferedImage bufferedImage = robot.createScreenCapture(area);
    String fileName = "Grid";

    // Capture the whole screen
    area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    bufferedImage = robot.createScreenCapture(area);
    File file = new File(fileName + index + ".png");
    ImageIO.write(bufferedImage, "png", file);

  }
}
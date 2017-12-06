import java.awt.*;
import java.util.Date;

import javax.swing.*;

public class Wakeup {
  public static void main(String[] args) throws AWTException, InterruptedException {
    Robot r = new Robot();
    Point p = MouseInfo.getPointerInfo().getLocation();
    Point p1 = new Point(p.x + 1, p.y);
    
    while(true){
       move(p1, r);
       move(p, r);

       Thread.sleep(60000 * 4);
    }
  }
  
  static void move(Point p, Robot r){
    r.mouseMove(p.x, p.y);
    System.out.printf("moveTo time=%s : x=%s, y=%s\n", new Date(), p.x, p.y);
  }
}
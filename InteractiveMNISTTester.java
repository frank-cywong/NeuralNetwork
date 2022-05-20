import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class InteractiveMNISTTester extends JPanel{
  public InteractiveMNISTTester(){
    setBackground(Color.BLACK);
  }
  public static void main(String[] args){
    JFrame frame = new JFrame("Interactive MNIST-trained Classification Neural Network Tester");
    InteractiveMNISTTester panel = new InteractiveMNISTTester();
    frame.add(panel);
    //448 x 448 drawing area, additional 152 pixels to the right for output
    frame.setSize(600, 448);
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

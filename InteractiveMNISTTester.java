import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class InteractiveMNISTTester extends JPanel{
  double[] pixelValues = new double[784];
  public InteractiveMNISTTester(){
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(448, 448));
    addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent me){
        setPixel(me.getX(), me.getY());
      }
    });
    addMouseMotionListener(new MouseAdapter(){
      public void mouseDragged(MouseEvent me){
        if(me.getX() < 448 && me.getY() < 448){
          setPixel(me.getX(), me.getY());
        }
      }
    });
  }
  public void setPixel(int col, int row){
    int boxCol = col / 16;
    int boxRow = row / 16;
    if(pixelValues[boxRow * 28 + boxCol] == 0.0){
      pixelValues[boxRow * 28 + boxCol] = 1.0;
      repaint();
    }
  }
  public void paintComponent(Graphics g){
    for(int i = 0; i < pixelValues.length; i++){
      if(pixelValues[i] == 1){
        g.setColor(Color.WHITE);
        int col = (i % 28) * 16;
        int row = (i / 28) * 16;
        g.fillRect(col, row, col + 16, row + 16);
      } else {
        g.setColor(Color.BLACK);
        int col = (i % 28) * 16;
        int row = (i / 28) * 16;
        g.fillRect(col, row, col + 16, row + 16);
      }
    }
  }
  public static void main(String[] args){
    JFrame frame = new JFrame("Interactive MNIST-trained Classification Neural Network Tester");
    InteractiveMNISTTester panel = new InteractiveMNISTTester();
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
    frame.getContentPane().add(panel);
    JPanel controls = new JPanel();
    JButton button = new JButton("Classify digit");
    button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        Network network = new Network("MNISTSoftmaxTest25Epochs94Percent.model");
        double[] predicted = network.evaluate(panel.pixelValues);
        int predict = 0;
        double predictmax = 0;
        for(int j = 0; j < 10; j++){
          if(predicted[j] > predictmax){
            predictmax = predicted[j];
            predict = j;
          }
        }
        System.out.println("Digit classified as: " + predict + ", with " + (double)Math.round(predictmax * 1000) / 1000.0 + " confidence");
        System.out.print("Full confidence values: ");
        for(int j = 0; j < 10; j++){
          if(j != 0){
            System.out.print("; ");
          }
          System.out.print("Digit " + j + ": " + (double)Math.round(predicted[j] * 1000) / 1000.0);
        }
        System.out.print("\n");
        System.exit(0);
      }
    });
    controls.add(button);
    frame.getContentPane().add(controls);
    //448 x 448 drawing area, additional 152 pixels to the right for output
    frame.setSize(600, 448);
    frame.pack();
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //panel.setPixel(0,0);
    //panel.setPixel(16,0);
  }
}

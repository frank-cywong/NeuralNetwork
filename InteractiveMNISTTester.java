import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
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
    JLabel result = new JLabel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
    //GridBagConstraints gbc = new GridBagConstraints();
    result.setText("  Results will appear here!                       ");
    result.setVisible(true);
    JLabel result2 = new JLabel();
    JLabel result3 = new JLabel();
    JLabel result4 = new JLabel();
    JLabel padding = new JLabel();
    result2.setText("                                                                                              ");
    result2.setVisible(true);
    result3.setVisible(true);
    result4.setVisible(true);
    padding.setText("                                                                                              ");
    padding.setVisible(true);
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
        String outputstring = "Digit classified as: " + predict + ", with " + (double)Math.round(predictmax * 1000) / 1000.0 + " confidence";
        String outputstring2 = "Full confidence values: ";
        String outputstring3 = "";
        for(int j = 0; j < 5; j++){
          if(j != 0){
            outputstring3 += "; ";
          }
          outputstring3 += ("" + j + ": " + (double)Math.round(predicted[j] * 1000) / 1000.0);
        }
        String outputstring4 = "";
        for(int j = 5; j < 10; j++){
          if(j != 5){
            outputstring4 += "; ";
          }
          outputstring4 += ("" + j + ": " + (double)Math.round(predicted[j] * 1000) / 1000.0);
        }
        //System.out.print("\n");
        //System.exit(0);
        result.setText(outputstring);
        result2.setText(outputstring2);
        result3.setText(outputstring3);
        result4.setText(outputstring4);
        panel.pixelValues = new double[784];
        panel.repaint();
      }
    });
    controls.add(button);
    controls.add(result);
    controls.add(result2);
    controls.add(result3);
    controls.add(result4);
    controls.add(padding);
    frame.getContentPane().add(controls);
    //448 x 448 drawing area, additional 952 pixels to the right for output
    frame.setSize(1400, 448);
    frame.pack();
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //panel.setPixel(0,0);
    //panel.setPixel(16,0);
  }
}

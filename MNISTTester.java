import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
public class MNISTTester{
  public static double[][][] loadTrainingData() throws IOException{
    double[][][] output = new double[2][][];
    double[][] labels = new double[60000][10];
    InputStream in = new FileInputStream("./mnist-raw/train-labels.idx1-ubyte");
    in.read(new byte[8]); // skip first 8 bytes
    byte[] bytes = new byte[60000];
    in.read(bytes);
    for(int i = 0; i < 60000; i++){
      for(int j = 0; j < 10; j++){
        labels[i][j] = (bytes[i] == j ? 1 : 0);
      }
    }
    output[1] = labels;
    in = new FileInputStream("./mnist-raw/train-images.idx3-ubyte");
    in.read(new byte[16]); // skip first 16 bytes
    double[][] data = new double[60000][768];
    for(int i = 0; i < 60000; i++){
      bytes = new byte[768];
      in.read(bytes);
      for(int j = 0; j < 768; j++){
        data[i][j] = bytes[j] / 255; // normalise
      }
    }
    output[0] = data;
    return output;
  }
  public static double[][][] loadTestingData() throws IOException{
    double[][][] output = new double[2][][];
    double[][] labels = new double[10000][10];
    InputStream in = new FileInputStream("./mnist-raw/t10k-labels.idx1-ubyte");
    in.read(new byte[8]); // skip first 8 bytes
    byte[] bytes = new byte[10000];
    in.read(bytes);
    for(int i = 0; i < 10000; i++){
      for(int j = 0; j < 10; j++){
        labels[i][j] = (bytes[i] == j ? 1 : 0);
      }
    }
    output[1] = labels;
    in = new FileInputStream("./mnist-raw/t10k-images.idx3-ubyte");
    in.read(new byte[16]); // skip first 16 bytes
    double[][] data = new double[10000][768];
    for(int i = 0; i < 10000; i++){
      bytes = new byte[768];
      in.read(bytes);
      for(int j = 0; j < 768; j++){
        data[i][j] = bytes[j] / 255; // normalise
      }
    }
    output[0] = data;
    return output;
  }
  public static void main(String[] args) throws IOException{
    if(args[0].equals("train")){
      Network network = null;
      if(args[1].equals("new")){
        network = new Network(4, 0.1, new int[] {768, 128, 74, 10}, new String[] {"identity", "lrelu", "lrelu", "sigmoid"});
        network.randomize_weights();
      } else {
        network = new Network(args[1]);
      }
      System.out.println("Loading training data");
      double[][][] trainingData = loadTrainingData();
      System.out.println("Training data loaded");
      for(int i = 0; i < Integer.parseInt(args[2]); i++){
        double error = network.trainOneEpoch(trainingData[0], trainingData[1]);
        if(i % Integer.parseInt(args[3]) == 0){
          System.out.println("Error for epoch " + i + ": " + error);
        }
      }
      //System.out.println(network.outputNetwork(false));
      BufferedWriter writer = new BufferedWriter(new FileWriter(args[4]));
      writer.write(network.outputNetwork(false));
      writer.close();
    } else if (args[0].equals("test")){
      Network network = new Network(args[1]);
      double[][][] testData = loadTestingData();
      int correctCount = 0;
      for(int i = 0; i < 10000; i++){
        double[] predicted = network.evaluate(testData[0][i]);
        int answer = 0;
        int predict = 0;
        double predictmax = 0;
        for(int j = 0; j < 10; j++){
          if(predicted[j] > predictmax){
            predictmax = predicted[j];
            predict = j;
          }
          if(testData[1][i][j] == 1){
            answer = j;
          }
        }
        if(predict == answer){
          correctCount++;
        }
      }
      System.out.println("Correct count: " + correctCount + " (" + (correctCount / 100) + "%)");
    }
  }
}
import java.util.Arrays;
public class Tests{
  public static void main(String[] args){
    /* Test 1: Forward propagation given premade model
    Network test1 = new Network("SampleXOR.model");
    System.out.println(Arrays.toString(test1.evaluate(new double[]{0,0})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{0,1})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{1,0})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{1,1})));
    */
    Network test2 = new Network(3, 0.1, new int[] {2, 5, 1}, new String[]{"identity", "sigmoid", "sigmoid"});
    //Network test2 = new Network("DebugTest2.model");
    test2.training_mode = 0;
    test2.randomize_weights();
    double[][] test2cases = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    double[][] test2correct = new double[][]{{0}, {1}, {1}, {0}};
    for(int i = 0; i < 10000; i++){
      //System.out.println(test2.outputNetwork());
      System.out.println("Error for epoch " + i + ": " + test2.trainOneEpoch(test2cases, test2correct));
    }
    System.out.println(test2.outputNetwork(false));
    for(int i = 0; i < 4; i++){
      System.out.println(Arrays.toString(test2.evaluate(test2cases[i])));
    }
  }
}

import java.util.Arrays;
public class Tests{
  public static void main(String[] args){
    Network test1 = new Network("SampleXOR.model");
    System.out.println(Arrays.toString(test1.evaluate(new double[]{0,0})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{0,1})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{1,0})));
    System.out.println(Arrays.toString(test1.evaluate(new double[]{1,1})));
  }
}

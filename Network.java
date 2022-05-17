import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
public class Network{
  // sort of linked list like in terms of storage actually
  Layer input = null;
  Layer output = null;
  double learning_rate;
  public Network(){
  }
  // Constructs new network from custom file format
  /*
    N -> total layers, a -> learning rate
    then N segments
    for each segment: X -> size of layer
    then binary flag numbers, 1 << 0: weights given (cant be if first layer), 1 << 1: values given
    then string for activation function: "identity", "relu", "lrelu", "sigmoid"
    if weights given, size * (input.size + 1) numbers in next line for weights, parse 2d array to 1d, a[i][j] = a[i*jlen + j]
    if values given, size numbers in next line for values
    
    Sample:
    3 0.05
    2 0 identity
    2 1 lrelu
    0 1 -1 0 -1 1
    1 1 sigmoid
    0 5 5
  */
  public Network(String filename){
    try {
      File f = new File(filename);
      Scanner in = new Scanner(f);
      int N = in.nextInt();
      double a = in.nextDouble();
      Layer prevlayer = null;
      for(int i = 0; i < N; i++){
        int X = in.nextInt();
        int flags = in.nextInt();
        String activation_function = in.next();
        Layer curlayer = new Layer(X);
        curlayer.learning_rate = learning_rate;
        switch(activation_function){
          case "identity":
            curlayer.activation_function = new IdentityFunction();
            break;
          case "lrelu":
            curlayer.activation_function = new LeakyReLU();
            break;
          case "relu":
            curlayer.activation_function = new RectifiedLinearActivationFunction();
            break;
          case "sigmoid":
            curlayer.activation_function = new SigmoidActivationFunction();
            break;
          default:
            throw new IllegalArgumentException("No valid activation function detected");
        }
        if(i != 0){
          curlayer.weights = new double[curlayer.size][prevlayer.size + 1];
        }
        if((flags & (1 << 0)) != 0){ // weights given
          if(i == 0){
            throw new IllegalArgumentException("First input layer cannot have weights");
          }
          for(int k = 0; k < curlayer.size; k++){
            for(int j = 0; j < (prevlayer.size + 1); j++){
              curlayer.weights[k][j] = in.nextDouble();
            }
          }
        }
        if((flags & (1 << 1)) != 0){ // values given
          for(int k = 0; k < curlayer.size; k++){
            curlayer.values[k] = in.nextDouble();
          }
        }
        if(i == 0){
          input = curlayer;
        }
        curlayer.input = prevlayer;
        prevlayer.output = curlayer;
        if(i == (N - 1)){
          output = curlayer;
        }
        prevlayer = curlayer;
      }
    } catch (FileNotFoundException e){
      throw new IllegalArgumentException("File not found"); // passthrough but in a more standard format
    }
  }
  public double[] evaluate(double[] inputValues){
    if(input.size != inputValues.length){
      throw new IllegalArgumentException("inputValues length does not match input layer size");
    }
    for(int i = 0; i < input.size; i++){
      input.values[i] = inputValues[i];
    }
    return evaluate();
  }
  public double[] evaluate(){
    Layer curlayer = input;
    while(curlayer != output){
      curlayer = curlayer.output;
      curlayer.forward_propagation();
    }
    return output.values;
  }
  public static double square(double x){
    return x * x;
  }
  // Uses RMS to calculate error
  public static double calculate_error(double[] target, double[] predicted){
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    double sum = 0;
    for(int i = 0; i < target.length; i++){
      sum += square(predicted[i] - target[i]);
    }
    sum /= target.length;
    return Math.sqrt(sum);
  }
}

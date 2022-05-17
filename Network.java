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
  public Network(int layer_count, double learning_rate, int[] layer_sizes, String[] layer_activation_functions){
    this.learning_rate = learning_rate;
    Layer prevlayer = null;
    for(int i = 0; i < layer_count; i++){
      Layer curlayer = new Layer(layer_sizes[i]);
      curlayer.learning_rate = learning_rate;
      switch(layer_activation_functions[i]){
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
      if(i == 0){
        input = curlayer;
      } else {
        prevlayer.output = curlayer;
        curlayer.input = prevlayer;
        curlayer.weights = new double[curlayer.size][prevlayer.size + 1];
      }
      if(i == (layer_count - 1)){
        output = curlayer;
      }
      prevlayer = curlayer;
    }
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
        } else {
          curlayer.input = prevlayer;
          prevlayer.output = curlayer;
        }
        if(i == (N - 1)){
          output = curlayer;
        }
        prevlayer = curlayer;
      }
    } catch (FileNotFoundException e){
      throw new IllegalArgumentException("File not found"); // passthrough but in a more standard format
    }
  }
  public String outputNetwork(){
    String s = "";
    int layercount = 1;
    Layer curlayer = input;
    // special case for input
    s += ("" + curlayer.size + " 2 " + curlayer.activation_function + "\n");
    for(int i = 0; i < curlayer.size; i++){
      if(i != 0){
        s += " ";
      }
      s += curlayer.values[i];
    }
    s += "\n";
    while(curlayer != output){
      layercount++;
      curlayer = curlayer.output;
      s += ("" + curlayer.size + " 3 " + curlayer.activation_function + "\n");
      for(int i = 0; i < curlayer.weights.length; i++){
        for(int j = 0; j < curlayer.weights[i].length; j++){
          if(i != 0 || j != 0){
            s += " ";
          }
          s += curlayer.weights[i][j];
        }
      }
      s += "\n";
      for(int i = 0; i < curlayer.size; i++){
        if(i != 0){
          s += " ";
        }
        s += curlayer.values[i];
      }
      s += "\n";
    }
    s = ("" + layercount + " " + learning_rate + "\n" + s);
    return s;
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
  public void randomize_weights(){ // starts each weight off with a random number between 0 and 1
    Layer curlayer = input;
    while(curlayer != output){
      curlayer = curlayer.output;
      for(int i = 0; i < curlayer.weights.length; i++){
        for(int j = 0; j < curlayer.weights[i].length; j++){
          curlayer.weights[i][j] = Math.random();
        }
      }
    }
  }
  public void backpropagate(double[] target){
    Layer curlayer = output;
    // first thing to calculate, dE/df, where f is the final output, for mean squared error, just 2 * (predicted - actual)
    double[] error_partial_derivs = new double[output.size];
    for(int i = 0; i < output.size; i++){
      error_partial_derivs[i] = 2 * (output.values[i] - target[i]);
    }
    while(curlayer != input){
      error_partial_derivs = curlayer.back_propagation(error_partial_derivs);
      curlayer = curlayer.input;
    }
  }
  // returns error
  public double trainOneTest(double[] testcase, double[] correct){ // stochastic training for simplicity
    evaluate(testcase);
    double rv = (calculate_error(correct, output.values));
    backpropagate(correct);
    return rv;
  }
  // returns avg error
  public double trainOneEpoch(double[][] testcases, double[][] correctvalues){
    if(testcases.length != correctvalues.length){
      throw new IllegalArgumentException("Testcases and correct value count are not equal");
    }
    double rv = 0;
    for(int i = 0; i < testcases.length; i++){
      rv += trainOneTest(testcases[i], correctvalues[i]);
    }
    rv /= testcases.length;
    return rv;
  }
  public static double square(double x){
    return x * x;
  }
  // Uses MSE to calculate error
  public static double calculate_error(double[] target, double[] predicted){
    System.out.println("Expected: " + target[0] + ", got: " + predicted[0]);
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    double sum = 0;
    for(int i = 0; i < target.length; i++){
      sum += square(predicted[i] - target[i]);
    }
    sum /= target.length;
    return sum;
  }
}

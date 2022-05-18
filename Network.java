import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
public class Network{
  // sort of linked list like in terms of storage actually
  static final int BATCH = 1; // BATCH MODE MAY NOT BE WORKING, COULD NOT GET IT TO WORK FOR XOR, POSSIBLE LOCAL MINIMUM OR IMPLEMENTATION BUG
  static final int STOCHASTIC = 0;
  static final int VERSION_NUMBER = 1;
  int training_mode = STOCHASTIC;
  Layer input = null;
  Layer output = null;
  double learning_rate;
  LossFunction loss_function = new MSE(); // by default use MSE
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
    N -> total layers, a -> learning rate, T -> training mode, v -> model version (in case i add more things its backwards compatible), L -> loss function type (MSE or log loss)
    then N segments
    for each segment: X -> size of layer
    then binary flag numbers, 1 << 0: weights given (cant be if first layer), 1 << 1: values given
    then string for activation function: "identity", "relu", "lrelu", "sigmoid"
    if weights given, size * (input.size + 1) numbers in next line for weights, parse 2d array to 1d, a[i][j] = a[i*jlen + j]
    if values given, size numbers in next line for values
    
    Sample:
    3 0.05 0 1 MSE
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
      this.learning_rate = a;
      training_mode = in.nextInt();
      int v = in.nextInt();
      switch(v){
        case 1:
          break;
        default:
          throw new IllegalArgumentException("Version number not recognised");
      }
      String lossfunction = in.next();
      switch(lossfunction){
        case "MSE":
          this.loss_function = new MSE();
          break;
        // to do: add logloss
        default:
          throw new IllegalArgumentException("Loss function not recognised");
      }
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
  public String outputNetwork(boolean includeValues){
    String s = "";
    int layercount = 1;
    Layer curlayer = input;
    // special case for input
    if(includeValues){
      s += ("" + curlayer.size + " 2 " + curlayer.activation_function + "\n");
      for(int i = 0; i < curlayer.size; i++){
        if(i != 0){
          s += " ";
        }
        s += curlayer.values[i];
      }
      s += "\n";
    } else {
      s += ("" + curlayer.size + " 0 " + curlayer.activation_function + "\n");
    }
    while(curlayer != output){
      layercount++;
      curlayer = curlayer.output;
      if(includeValues){
        s += ("" + curlayer.size + " 3 " + curlayer.activation_function + "\n");
      } else {
        s += ("" + curlayer.size + " 1 " + curlayer.activation_function + "\n");
      }
      for(int i = 0; i < curlayer.weights.length; i++){
        for(int j = 0; j < curlayer.weights[i].length; j++){
          if(i != 0 || j != 0){
            s += " ";
          }
          s += curlayer.weights[i][j];
        }
      }
      s += "\n";
      if(includeValues){
        for(int i = 0; i < curlayer.size; i++){
          if(i != 0){
            s += " ";
          }
          s += curlayer.values[i];
        }
        s += "\n";
      }
    }
    s = ("" + layercount + " " + learning_rate + " " + training_mode + " " + VERSION_NUMBER + " " + loss_function + "\n" + s);
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
  public void randomize_weights(){ // starts each weight off with a random number between -1 and 1
    Layer curlayer = input;
    while(curlayer != output){
      curlayer = curlayer.output;
      for(int i = 0; i < curlayer.weights.length; i++){
        for(int j = 0; j < curlayer.weights[i].length; j++){
          curlayer.weights[i][j] = (Math.random() * 2) - 1;
        }
      }
    }
  }
  public double[] get_error_partial_derivs(double[] target, double[] predicted){
    // first thing to calculate, dE/df, where f is the final output, for mean squared error, just 2 * (predicted - actual)
    /*
    double[] error_partial_derivs = new double[predicted.length];
    for(int i = 0; i < output.size; i++){
      //System.out.println("OUTPUT: " + output.values[i] + ", TARGET: " + target[i]);
      error_partial_derivs[i] = 2 * (predicted[i] - target[i]);
    }
    return error_partial_derivs;
    */
    return loss_function.compute_derivative(target, predicted);
  }
  public void backpropagate(double[] error_partial_derivs){
    Layer curlayer = output;
    while(curlayer != input){
      error_partial_derivs = curlayer.back_propagation(error_partial_derivs);
      curlayer = curlayer.input;
    }
  }
  // returns [error, error partial derivatives relative to output values]
  public double[][] trainOneTest(double[] testcase, double[] correct){
    evaluate(testcase);
    double[] rv1 = new double[]{(loss_function.compute_loss(correct, output.values))};
    double[][] rv = new double[2][];
    rv[0] = rv1;
    rv[1] = get_error_partial_derivs(correct, output.values);
    if(training_mode == STOCHASTIC){
      backpropagate(rv[1]);
    }
    //System.out.println(outputNetwork());
    return rv;
  }
  // returns avg error
  public double trainOneEpoch(double[][] testcases, double[][] correctvalues){
    if(testcases.length != correctvalues.length){
      throw new IllegalArgumentException("Testcases and correct value count are not equal");
    }
    double[][] rv;
    double errorsum = 0;
    double[] gradientavg = new double[correctvalues[0].length];
    for(int i = 0; i < testcases.length; i++){
      rv = trainOneTest(testcases[i], correctvalues[i]);
      if(training_mode == BATCH){
        sumTo(gradientavg, rv[1]);
      }
      errorsum += (rv[0][0] / testcases.length);
      if(i % 500 == 0 && i >= 500){
        System.out.println("Currently at test case " + i + ", current error sum: " + errorsum);
      }
    }
    if(training_mode == BATCH){
      for(int i = 0; i < gradientavg.length; i++){
        gradientavg[i] /= testcases.length;
      }
      backpropagate(gradientavg);
    }
    return errorsum;
  }
  public static void sumTo(double[] a, double[] b){
    if(a.length != b.length){
      throw new IllegalArgumentException("sumTo called with two arrays of different length");
    }
    for(int i = 0; i < a.length; i++){
      a[i] += b[i];
    }
  }
  /*
  public static double square(double x){
    return x * x;
  }
  // Uses MSE to calculate error
  public static double calculate_error(double[] target, double[] predicted){
    //System.out.println("Expected: " + target[0] + ", got: " + predicted[0]);
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    double sum = 0;
    for(int i = 0; i < target.length; i++){
      sum += square(predicted[i] - target[i]);
      //System.out.println(predicted[i]);
    }
    sum /= target.length;
    return sum;
  }
  */
}

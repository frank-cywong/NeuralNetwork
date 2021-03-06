//import java.util.Arrays;
public class Layer{
  // step 0: variables
  double learning_rate;
  int size;
  Layer input;
  Layer output;
  double[][] weights; // 2d array, [size][input.size + 1], weight[x][0] = weight for constant term
  double[] values;
  ActivationFunction activation_function;
  // step 0.5: constructors
  public Layer(int size){
    this.size = size;
    weights = new double[size][];
    values = new double[size];
  }
  // step 1: forward propagation
  public void forward_propagation(){
    double[] preactivation_vector = new double[size];
    for(int i = 0; i < size; i++){
      double tempv = weights[i][0]; // constant term
      for(int j = 0; j < input.size; j++){
        tempv += weights[i][j+1] * input.values[j];
      }
      //values[i] = activation_function.compute(tempv);
      preactivation_vector[i] = tempv;
    }
    values = activation_function.compute(preactivation_vector);
  }
  // step 2: back propagate to adjust weights
  /*
    let e be this function's (f(x)) error
    target output: de / dw for each weight w
    given de/df, df/dw for each component f, we can compute de/dw
    de/df is also de / (input of next layer) - pass as arg
    de/dx can be computed also by chain rule
    df/dw is just input value of that corresponds to weight in this case
    eg. for weight[i][j+1] -> input.values[i]
    eg. for weight[i][0] -> 1
    as each weight only affects the i-th component, df(k)/dw, where k != i = 0, so they can be ignored
    inputs needed: double[] of de/df for all i nodes in layer
    outputs: [adjusted weights locally] and return de/dx for all x input values
    de/dx(j) = sum(de/df(i) * df(i)/ dx(j)), df(i)/dx(j) = weights[i][j+1]
    
    also wrapper in front that's the activation function:
    let A(x) be the activation function
    de / df(i) = de/dA * dA/df(i), A(x) = <A(x1), A(x2), ... A(xi)>
    ie just multiply by dA/df(i) evaled at output of f (ie. values[i])
  */
  public double[] back_propagation(double[] partial_derivs){
    double[] adj_partial_derivs = new double[partial_derivs.length];
    //System.out.println("PARTIAL DERIVS: " + Arrays.toString(partial_derivs));
    //System.out.println("VALUES: " + Arrays.toString(values));
    if(activation_function.returnValType() == 1){
      double[] activation_function_partial_derivs = activation_function.compute_derivative(values);
      for(int i = 0; i < partial_derivs.length; i++){
        adj_partial_derivs[i] = partial_derivs[i] * activation_function_partial_derivs[i];
      }
    } else {
      double[][] activation_function_derivative_matrix = activation_function.compute_derivative_matrix(values);
      for(int i = 0; i < partial_derivs.length; i++){
        for(int j = 0; j < partial_derivs.length; j++){
          adj_partial_derivs[i] += partial_derivs[j] * activation_function_derivative_matrix[j][i];
        }
      }
    }
    //System.out.println("ADJ PARTIAL DERIVS: " + Arrays.toString(adj_partial_derivs));
    double[] output_array = new double[input.size]; // calculate output before we adjust weights
    for(int j = 0; j < input.size; j++){
      double tempsum = 0;
      for(int i = 0; i < values.length; i++){
        tempsum += adj_partial_derivs[i] * weights[i][j+1];
      }
      output_array[j] = tempsum;
    }
    adjust_weights(adj_partial_derivs);
    return output_array;
  }
  // just adjust weights without computing input partial derivs, used for first layer and internally, input should be adjusted for the activation function already
  public void adjust_weights(double[] adj_partial_derivs){
    /*
    double[] adj_partial_derivs = new double[partial_derivs.length];
    for(int i = 0; i < partial_derivs.length; i++){
      adj_partial_derivs[i] = partial_derivs[i] * activation_function.compute_derivative(values[i]);
    }
    */
    for(int i = 0; i < values.length; i++){
      for(int j = 0; j < input.size + 1; j++){
        //System.out.println(adj_partial_derivs[i]);
        //System.out.println(learning_rate);
        weights[i][j] -= learning_rate * adj_partial_derivs[i] * (j == 0 ? 1 : input.values[j-1]);
        //System.out.println("ADJUSTING WEIGHT " + i + ", " + j + " BY: " + learning_rate * adj_partial_derivs[i] * (j == 0 ? 1 : input.values[j-1]));
      }
    }
  }
}

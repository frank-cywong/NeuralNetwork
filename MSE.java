public class MSE implements LossFunction{
  public static double square(double x){
    return x * x;
  }
  public double compute_loss(double[] target, double[] predicted){
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
  public double[] compute_derivative(double[] target, double[] predicted){
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    double[] error_partial_derivs = new double[predicted.length];
    for(int i = 0; i < predicted.length; i++){
      //System.out.println("OUTPUT: " + output.values[i] + ", TARGET: " + target[i]);
      error_partial_derivs[i] = 2 * (predicted[i] - target[i]);
    }
    return error_partial_derivs;
  }
  public String toString(){
    return "MSE";
  }
}

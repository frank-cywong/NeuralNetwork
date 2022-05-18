public class MSE implements LossFunction{
  public double compute_loss(double[] target, double[] predicted){
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    // - 1/N sum(ln(corrected_probabilities)), corrected_probabilities = predicted if actually true (1), (1 - predicted) if actually false (0)
    double sum = 0;
    for(int i = 0; i < predicted.length; i++){
      sum += Math.log(target[i] == 1 ? predicted[i] : (1 - predicted[i]));
    }
    sum *= -1;
    sum /= predicted.length;
    return sum;
  }
  public double[] compute_derivative(double[] target, double[] predicted){
    if(target.length != predicted.length){
      throw new IllegalArgumentException("Target and predicted lengths do not match");
    }
    // TO DO: ACTUALLY CALCULATE THIS BY HAND AND FIND THE CORRECT FORMULA
    double[] error_partial_derivs = new double[predicted.length];
    for(int i = 0; i < predicted.length; i++){
      //System.out.println("OUTPUT: " + output.values[i] + ", TARGET: " + target[i]);
      error_partial_derivs[i] = 2 * (predicted[i] - target[i]);
    }
    return error_partial_derivs;
  }
  public String toString(){
    return "LogLoss";
  }
}

public class SigmoidActivationFunction implements ActivationFunction{
  public double compute(double v){
    return(1 / (1 + (Math.exp(-1 * v))));
  }
  public double compute_derivative(double v){
    double expv = Math.exp(-1 * v);
    return(expv / (1 + expv * expv));
  }
  public String toString(){
    return "sigmoid";
  }
}

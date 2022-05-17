public class RectifiedLinearActivationFunction implements ActivationFunction{
  public static double compute(double v){
    return(1 / (1 + (Math.exp(-1 * v))));
  }
  public static double compute_derivative(double v){
    double expv = Math.exp(-1 * v);
    return(expv / (1 + expv * expv));
  }
}

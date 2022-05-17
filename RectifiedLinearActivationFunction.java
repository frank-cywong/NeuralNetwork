public class RectifiedLinearActivationFunction implements ActivationFunction{
  public static double compute(double v){
    return(v > 0 ? v : 0);
  }
  public static double compute_derivative(double v){
    return(v > 0 ? 1 : 0); // assume if value is 0, just use 0 as derivative
  }
}

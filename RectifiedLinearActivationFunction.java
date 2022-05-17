public class RectifiedLinearActivationFunction implements ActivationFunction{
  public double compute(double v){
    return(v > 0 ? v : 0);
  }
  public double compute_derivative(double v){
    return(v > 0 ? 1 : 0); // assume if value is 0, just use 0 as derivative
  }
  public String toString(){
    return "relu";
  }
}

public class LeakyReLU implements ActivationFunction{
  public double compute(double v){
    return(v > 0 ? v : 0.01 * v);
  }
  public double compute_derivative(double v){ // COMPUTE THE DERIVATIVE GIVEN THE POST-ACTIVATION VALUE
    return(v > 0 ? 1 : 0.01); // assume if value is 0, just use 0.01 as derivative
  }
  public String toString(){
    return "lrelu";
  }
}

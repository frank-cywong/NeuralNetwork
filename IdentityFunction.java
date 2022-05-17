public class IdentityFunction implements ActivationFunction{
  public double compute(double v){
    return v;
  }
  public double compute_derivative(double v){
    return 1;
  }
  public String toString(){
    return "identity";
  }
}

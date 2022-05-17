public class IdentityFunction implements ActivationFunction{
  public double compute(double v){
    return v;
  }
  public double compute_derivative(double v){ // COMPUTE THE DERIVATIVE GIVEN THE POST-ACTIVATION VALUE
    return 1;
  }
  public String toString(){
    return "identity";
  }
}

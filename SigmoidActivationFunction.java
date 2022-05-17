public class SigmoidActivationFunction implements ActivationFunction{
  public double compute(double v){
    return(1 / (1 + (Math.exp(-1 * v))));
  }
  public double compute_derivative(double v){ // COMPUTE THE DERIVATIVE GIVEN THE POST-ACTIVATION VALUE
    return((1 - v) * v);
  }
  public String toString(){
    return "sigmoid";
  }
}

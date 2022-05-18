public interface ActivationFunction{
  public double compute(double v);
  public default double[] compute(double[] v){
    double[] output = new double[v.length];
    for(int i = 0; i < v.length; i++){
      output[i] = compute(v[i]);
    }
    return output;
  }
  public double compute_derivative(double v); // COMPUTE THE DERIVATIVE GIVEN THE POST-ACTIVATION VALUE
  public default double[] compute_derivative(double[] v){ // COMPUTE DERIVATIVE BUT FOR A VECTOR, SO OVERRIDING COMPUTE_DERIVATIVE IS NOT NEEDED
    double[] output = new double[v.length];
    for(int i = 0; i < v.length; i++){
      output[i] = compute_derivative(v[i]);
    }
    return output;
  }
  public String toString();
}

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
  public default double[][] compute_derivative_matrix(double[] v){
    throw new UnsupportedOperationException("Output for this activation function is a vector, not a matrix");
  }
  public default int returnValType(){
    return 1; // 1 for 1D, 2 for 2D
  }
  public String toString();
}

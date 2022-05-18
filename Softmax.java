public class Softmax implements ActivationFunction{
  public double compute(double v){
    throw new UnsupportedOperationException("Softmax requires the entire vector to compute the output vector");
  }
  // S(v)i = e^(vi) / (sum across all j (e^(vj)))
  public double[] compute(double[] v){
    double[] output = new double[v.length];
    double[] exps = new double[v.length];
    double sum = 0;
    for(int i = 0; i < v.length; i++){
      exps[i] = Math.exp(v[i]);
      sum += exps[i];
    }
    for(int i = 0; i < v.length; i++){
      output[i] = exps[i] / sum;
    }
    return output;
  }
  public double compute_derivative(double v){
    throw new UnsupportedOperationException("Softmax requires the entire vector to compute the derivatives");
  }
  public double[] compute_derivative(double[] v){ // COMPUTE DERIVATIVE BUT FOR A VECTOR, SO OVERRIDING COMPUTE_DERIVATIVE IS NOT NEEDED
    double[] output = new double[v.length];
    for(int i = 0; i < v.length; i++){
      output[i] = compute_derivative(v[i]);
    }
    return output;
  }
  public String toString(){
    return "softmax";
  }
}

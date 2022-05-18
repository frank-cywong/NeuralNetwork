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
  public double[] compute_derivative(double[] v){
    throw new UnsupportedOperationException("Softmax outputs a matrix of the Jacobian derivative");
  }
  /*
    Also needs to return 2d array
    Returns 2D array a[i][j], where a[i][j] = dA(i)/dF(j) (ie. like jacobian derivative matrix)
    This is so dE/dA(i) * [return value] = dE/dF(i), where F is the function plugged into this activation function A(v)
    in general, multi dimensional chain rule, dE/dF(i) = sum for all j(dE/dA(j) * dA(j)/dF(i))
  */
  public double[][] compute_derivative_matrix(double[] v){ // COMPUTE DERIVATIVE BUT FOR A VECTOR, SO OVERRIDING COMPUTE_DERIVATIVE IS NOT NEEDED
    double[][] output = new double[v.length][v.length];
    for(int i = 0; i < v.length; i++){ // ith output of softmax
      for(int j = 0; j < v.length; j++){ // relative to jth input of softmax
        /* 
        Logarithmic differentiation from https://towardsdatascience.com/derivative-of-the-softmax-function-and-the-categorical-cross-entropy-loss-ffceefc081d1
        dA(i)/dF(j), consider ln(Ai)
        dA(i)/dF(j) = d(ln(Ai)) / dF(j) / (d(ln(Ai))/d(Ai)) = d(ln(Ai)) / dF(j) / (1/Ai) = d(ln(Ai)) / dF(j) * Ai by chain rule
        ln(Ai) = Fi - ln(sum over all k(e^Fk))
        d(ln(Ai)) / dF(j) = dFi / dFj - d/dFj(ln(sum over all k(e^Fk)))
        dFi / dFj = 1 if i == j otherwise 0
        using chain rule again, d/dFj(ln(sum over all k(e^Fk))) = 1/(sum over all k(e^Fk)*d/dFj(sum over all k (e^Fk))
        = 1/(sum over all k(e^Fk) * e^(Fj) = Aj
        ie. final result: dA(i)/dF(j) = Ai * ((1 if i == j, otherwise 0) - Aj)
        */
        output[i][j] = v[i] * ((i == j ? 1 : 0) - v[j]);
      }
    }
    return output;
  }
  public int returnValType(){
    return 2;
  }
  public String toString(){
    return "softmax";
  }
}

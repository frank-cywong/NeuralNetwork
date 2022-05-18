public interface LossFunction{
  public double compute_loss(double[] target, double[] predicted);
  public double[] compute_derivative(double[] target, double[] predicted); // COMPUTE dL/dF for the function output
  public String toString();
}

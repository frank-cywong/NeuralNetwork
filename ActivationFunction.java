public interface ActivationFunction{
  public double compute(double v);
  public double compute_derivative(double v); // COMPUTE THE DERIVATIVE GIVEN THE POST-ACTIVATION VALUE
  public String toString();
}

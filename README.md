# NeuralNetwork
A successful attempt at a simple feedforward neural network in Java

### Detailed Specifications:
- Multiple activation function support (Identity for input layer, Rectified Linear Activation Function, LeakyReLU, Sigmoid, and Softmax)
- Stochastic training only (batch training feature discontinued)
- Multiple loss function support (Mean Square Error & Log Loss)

### Successful Models:
1. Manually adjusted weights for XOR: `SampleXOR.model`, first successful forward propagation test
2. Backpropagation adjusted 2-4-1 XOR model, with hidden layer being LReLU: `XOR-LRELU-BackpropagationTrained.model`, first successful backpropagation test
3. Backpropagation adjusted 2-5-1 XOR model, with hidden layer being sigmoid: `XOR-LRELU-Sigmoid.model`
4. Backpropagation adjusted 2-5-2 XOR classification model with Softmax & Log loss: `XOR-SoftmaxLogloss-BackpropagationTrained.model`, first successful softmax & logloss test
5. Backpropagation adjusted 784-50-20-10 MNIST classification model, hidden layers being sigmoid: `MNISTSoftmaxTest25Epochs94Percent.model`, first successful MNIST model, 94% accuracy on testing data that it hasn't seen before

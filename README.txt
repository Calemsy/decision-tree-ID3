MNIST.c			从网络上下载的原始数据  42000  数据的格式为 label+feature
MNISTDecisionTree.c	将MNIST.c处理为feature+label

MNIST01DecisionTree.c	将MNISTDecisionTree.c进行二值处理，非0的数据都设置为1

minMNISTDecisionTree.c	MNISTDecisionTree.c的十分之一
	minisoPorbe.data	:训练集
	minisoTraining.data	:测试集

MNISTtree.data		MNIST决策树的输出，构造规则
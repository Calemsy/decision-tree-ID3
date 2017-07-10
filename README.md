# DecisionTreeID3
Machine Learning Algorithm ID3 of Decision Tree( java )

### 代码
### 1. DecisionTree.java 决策树的数据结构
不像python中有一个功能比较强大的字典，所以这里自定义了一个决策树的数据结构(类DecisionTree)，两个域：
1. `String`：用来表示该树(子树)的属性(feature)。
2.  `HashMap<String, Object>` : key的值表示feature的取值，Object是子树(DecisionTree)或者是最终的label。
典型的一个递归的定义。并且在该类中提供了：
1. 遍历树的方法。
2. 将构造的树输出到指定的文件中。
### 2. ID3.java 
*ID3*算法实现
### 3. IOOperation.java
用于读取训练集等数据。
### 4. Football.java
利用决策树算法来决定要不要出去踢球。
### 5. Lenses.java
利用决策树算法来决定适合什么类型的隐形眼镜
### 6. Mnist.java
计算决策树算法在手写字数据MNISI上的表现
### 7. PretreatMNIST.java
对数据集MNIST的预处理
***

### 数据
**MNIST.c**：从网络上下载的原始数据  42000  数据的格式为 label+feature</br>
**MNISTDecisionTree.c**：将MNIST.c处理为feature+label</br>
**MNIST01DecisionTree.c**：将MNISTDecisionTree.c进行二值处理，非0的数据都设置为1</br>
**minMNISTDecisionTree.c**：MNISTDecisionTree.c的十分之一</br>
	**minisoPorbe.data**：minMNISTDecisionTree.c产生的训练集合</br>
	**minisoTraining.data**：minMNISTDecisionTree.c产生的测试集合</br>
**MNISTtree.data**：MNIST数据产生的决策树的构造规则</br>

[详细介绍](http://blog.csdn.net/robin_xu_shuai/article/details/74011205#)

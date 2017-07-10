package com.ali;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ID3 {

	private DecisionTree decisionTree = null;
	private String dataSetName = null;
	private String splitFlag = " ";

	private ArrayList<ArrayList<String>> feature; // 所有的training data
	private ArrayList<String> featureName;// 特征的名称:feature1,feature2,...,featureX,label
	public ArrayList<String> bankFeatureName = new ArrayList<>();

	public ID3(String dataSet, String splitFlag) {
		this.setSplitFlag(splitFlag);
		dataSetName = dataSet;
		IOoperation iOoperation = new IOoperation(dataSetName, this.splitFlag);
		iOoperation.getFeatureAndLabel();
		this.feature = iOoperation.getFeature();
		this.featureName = iOoperation.getFeatureName();
		for (int i = 0; i < this.featureName.size(); i++) {
			this.bankFeatureName.add(this.featureName.get(i));
		}

		System.out.println("feature size is " + (featureName.size() - 1)); // -1是减去label
		System.out.println("sample size is " + feature.size());
	}

	/**
	 * 将data中第index列上值为value的样本返回，并且在返回的结果中，所有的样本不包括index列的特征
	 * 
	 * @param data
	 * @param index
	 * @param value
	 * @return
	 */
	public ArrayList<ArrayList<String>> splitDataSetByFeature(ArrayList<ArrayList<String>> data, int index,
			String value) {
		ArrayList<ArrayList<String>> subData = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			ArrayList<String> newSample = new ArrayList<>();
			if (data.get(i).get(index).equals(value)) {
				for (int j = 0; j < data.get(i).size(); j++) {
					if (j != index) {
						newSample.add(data.get(i).get(j));
					}
				}
				subData.add(newSample);
			}
		}
		return subData;
	}

	/**
	 * 当传过来一个data之后，我们根据最后一列来计算entropy
	 * 
	 * @param data
	 * @return
	 */
	public double calculateShannonEntropy(ArrayList<ArrayList<String>> data) {
		double shannon = 0.0;

		int length = data.get(0).size(); // length-1就是label的index
		HashMap<String, Integer> uniqueLabels = new HashMap<>();
		for (int i = 0; i < data.size(); i++) {
			if (uniqueLabels.containsKey(data.get(i).get(length - 1))) {
				uniqueLabels.replace(data.get(i).get(length - 1), uniqueLabels.get(data.get(i).get(length - 1)) + 1);
			} else {
				uniqueLabels.put(data.get(i).get(length - 1), 1);
			}
		}
		for (String one : uniqueLabels.keySet()) {
			shannon += -(((double) (uniqueLabels.get(one)) / (data.size()))
					* Math.log((double) (uniqueLabels.get(one)) / (data.size())) / Math.log(2));
		}
		return shannon;
	}

	/**
	 * 对于一个数据集data，要选择其中的最好的feature来划分数据， 所以需要一列一列的比较(比较使用哪个
	 * 特征来划分得到的信息增益最大)。对于每一列来说，计算该列中的属性值有多少种，然后计算每种属
	 * 性值的熵的大小，然后按照比例求和。最后比较每一列的熵值的总和，信息增益最大的属性就是我们想要 找的最好的属性。
	 * 
	 * @param data
	 * @return
	 */
	public int chooseBestFeature(ArrayList<ArrayList<String>> data, ArrayList<String> featureName) {
		int featureSize = data.get(0).size();
		int dataSize = data.size();
		int bestFuatrue = -1;
		double bestInfoGain = 0.0;
		double infoGain = 0.0;
		double baseShannon = this.calculateShannonEntropy(data);
		double shannon = 0.0;
		HashMap<String, Integer> featureStatistic = new HashMap<>(); // 统计某一个属性下的取值的范围及个数
		for (int i = 0; i < featureSize - 1; i++) {
			for (int j = 0; j < data.size(); j++) {
				if (featureStatistic.containsKey(data.get(j).get(i))) {
					featureStatistic.replace(data.get(j).get(i), featureStatistic.get(data.get(j).get(i)) + 1);
				} else {
					featureStatistic.put(data.get(j).get(i), 1);
				}
			}

			ArrayList<ArrayList<String>> subdata;
			for (String featureValue : featureStatistic.keySet()) {
				subdata = this.splitDataSetByFeature(data, i, featureValue);
				shannon += this.calculateShannonEntropy(subdata)
						* ((double) featureStatistic.get(featureValue) / dataSize);
			}
			infoGain = baseShannon - shannon;
			if (infoGain > bestInfoGain) {
				bestInfoGain = infoGain;
				bestFuatrue = i;
			}
			shannon = 0.0;
			featureStatistic.clear();
		}
		return bestFuatrue;
	}

	/**
	 * 递归的构造决策树，注意函数的返回类型是object，而不是DecisionTree，这是因为当我们构造到叶子结点的
	 * 时候，我们可能返回的是String(正例还是反例，yes or no，而不再是棵子树)，这就需要我们的类型更 "大"的，
	 * 所以使用Object。
	 * 
	 * @param data
	 * @param featureName
	 * @return
	 */
	public Object createDecisionTree(ArrayList<ArrayList<String>> data, ArrayList<String> featureName) {

		int dataSize = data.size();
		int featureSize = data.get(0).size();
		// 如果没有特征了，data.get(0).size = 1说明只剩下标签了， 开始投票。
		if (data.get(0).size() == 1) {
			return vote(data);
		}
		// 判断是不是所有的sample的label都一致了， 如果是，返回这个统一的类别标签。
		HashSet<String> labels = new HashSet<>();
		for (int i = 0; i < dataSize; i++) {
			if (!labels.contains(data.get(i).get(featureSize - 1))) {
				labels.add(data.get(i).get(featureSize - 1));
			}
		}
		if (labels.size() == 1) {
			return data.get(0).get(featureSize - 1);
		}

		// 选择最好的feature来进行决策树(子决策树)的构建
		int bestFeatureIndex = this.chooseBestFeature(data, featureName);
		String bestFeature = featureName.get(bestFeatureIndex);
		featureName.remove(bestFeatureIndex);

		// 统计上一步选出的最好的属性，都有那些取值。
		HashSet<String> bestFeatureValuesSet = new HashSet<>();
		for (int i = 0; i < data.size(); i++) {
			if (!bestFeatureValuesSet.contains(data.get(i).get(bestFeatureIndex))) {
				bestFeatureValuesSet.add(data.get(i).get(bestFeatureIndex));
			}
		}

		DecisionTree tree = new DecisionTree();
		tree.setAttributeName(bestFeature);

		// 最好的属性的每一个取值，都形成一个子树的root， 开始递归。
		Iterator<String> iterator = bestFeatureValuesSet.iterator();
		while (iterator.hasNext()) {
			ArrayList<String> subFeatureName = new ArrayList<>();
			for (int i = 0; i < featureName.size(); i++) {
				subFeatureName.add(featureName.get(i));
			} // 这段代码的意思是理解递归的一个关键的点。
			String featureValue = iterator.next();
			tree.children.put(featureValue,
					createDecisionTree(splitDataSetByFeature(data, bestFeatureIndex, featureValue), subFeatureName));
		}
		return tree;
	}

	/**
	 * 当已经没有属性可以作为划分的依据了， 但是类的标签依然不同， 那么这个时候就要投票决定了。
	 * 这个时候data的形成应该是只有一列标签了。那么我们就找这一列标签中最多的，作为类别返回。
	 * 
	 * @param data
	 * @return
	 */
	public String vote(ArrayList<ArrayList<String>> data) {
		String voteResult = null;
		int dataSize = data.size();
		int length = data.get(0).size();

		HashMap<String, Integer> sta = new HashMap<>();
		for (int i = 0; i < dataSize; i++) {
			if (!sta.keySet().contains(data.get(i).get(length - 1))) {
				sta.put(data.get(i).get(length - 1), 1);
			} else {
				sta.replace(data.get(i).get(length - 1), sta.get(data.get(i).get(length - 1)) + 1);
			}
		}
		int maxValue = Collections.max(sta.values());
		for (String key : sta.keySet()) {
			if (maxValue == sta.get(key)) {
				voteResult = key;
			}
		}
		return voteResult;
	}

	public DecisionTree trainingDecisionTree() {
		this.decisionTree = null;
		System.out.println("\r\n=====================================================================\r\n");
		this.decisionTree = (DecisionTree) this.createDecisionTree(this.feature, this.featureName);
		System.out.println("=====================================================================\r\n");
		System.out.println("\r\ndecision tree");
		this.decisionTree.saveDecisionTree(this.decisionTree);
		return this.decisionTree;
	}

	public void setSplitFlag(String splitFlag) {
		this.splitFlag = splitFlag;
	}

	public static void main(String args[]) {

	}
}

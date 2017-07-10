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

	private ArrayList<ArrayList<String>> feature; // ���е�training data
	private ArrayList<String> featureName;// ����������:feature1,feature2,...,featureX,label
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

		System.out.println("feature size is " + (featureName.size() - 1)); // -1�Ǽ�ȥlabel
		System.out.println("sample size is " + feature.size());
	}

	/**
	 * ��data�е�index����ֵΪvalue���������أ������ڷ��صĽ���У����е�����������index�е�����
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
	 * ��������һ��data֮�����Ǹ������һ��������entropy
	 * 
	 * @param data
	 * @return
	 */
	public double calculateShannonEntropy(ArrayList<ArrayList<String>> data) {
		double shannon = 0.0;

		int length = data.get(0).size(); // length-1����label��index
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
	 * ����һ�����ݼ�data��Ҫѡ�����е���õ�feature���������ݣ� ������Ҫһ��һ�еıȽ�(�Ƚ�ʹ���ĸ�
	 * ���������ֵõ�����Ϣ�������)������ÿһ����˵����������е�����ֵ�ж����֣�Ȼ�����ÿ����
	 * ��ֵ���صĴ�С��Ȼ���ձ�����͡����Ƚ�ÿһ�е���ֵ���ܺͣ���Ϣ�����������Ծ���������Ҫ �ҵ���õ����ԡ�
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
		HashMap<String, Integer> featureStatistic = new HashMap<>(); // ͳ��ĳһ�������µ�ȡֵ�ķ�Χ������
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
	 * �ݹ�Ĺ����������ע�⺯���ķ���������object��������DecisionTree��������Ϊ�����ǹ��쵽Ҷ�ӽ���
	 * ʱ�����ǿ��ܷ��ص���String(�������Ƿ�����yes or no���������ǿ�����)�������Ҫ���ǵ����͸� "��"�ģ�
	 * ����ʹ��Object��
	 * 
	 * @param data
	 * @param featureName
	 * @return
	 */
	public Object createDecisionTree(ArrayList<ArrayList<String>> data, ArrayList<String> featureName) {

		int dataSize = data.size();
		int featureSize = data.get(0).size();
		// ���û�������ˣ�data.get(0).size = 1˵��ֻʣ�±�ǩ�ˣ� ��ʼͶƱ��
		if (data.get(0).size() == 1) {
			return vote(data);
		}
		// �ж��ǲ������е�sample��label��һ���ˣ� ����ǣ��������ͳһ������ǩ��
		HashSet<String> labels = new HashSet<>();
		for (int i = 0; i < dataSize; i++) {
			if (!labels.contains(data.get(i).get(featureSize - 1))) {
				labels.add(data.get(i).get(featureSize - 1));
			}
		}
		if (labels.size() == 1) {
			return data.get(0).get(featureSize - 1);
		}

		// ѡ����õ�feature�����о�����(�Ӿ�����)�Ĺ���
		int bestFeatureIndex = this.chooseBestFeature(data, featureName);
		String bestFeature = featureName.get(bestFeatureIndex);
		featureName.remove(bestFeatureIndex);

		// ͳ����һ��ѡ������õ����ԣ�������Щȡֵ��
		HashSet<String> bestFeatureValuesSet = new HashSet<>();
		for (int i = 0; i < data.size(); i++) {
			if (!bestFeatureValuesSet.contains(data.get(i).get(bestFeatureIndex))) {
				bestFeatureValuesSet.add(data.get(i).get(bestFeatureIndex));
			}
		}

		DecisionTree tree = new DecisionTree();
		tree.setAttributeName(bestFeature);

		// ��õ����Ե�ÿһ��ȡֵ�����γ�һ��������root�� ��ʼ�ݹ顣
		Iterator<String> iterator = bestFeatureValuesSet.iterator();
		while (iterator.hasNext()) {
			ArrayList<String> subFeatureName = new ArrayList<>();
			for (int i = 0; i < featureName.size(); i++) {
				subFeatureName.add(featureName.get(i));
			} // ��δ������˼�����ݹ��һ���ؼ��ĵ㡣
			String featureValue = iterator.next();
			tree.children.put(featureValue,
					createDecisionTree(splitDataSetByFeature(data, bestFeatureIndex, featureValue), subFeatureName));
		}
		return tree;
	}

	/**
	 * ���Ѿ�û�����Կ�����Ϊ���ֵ������ˣ� ������ı�ǩ��Ȼ��ͬ�� ��ô���ʱ���ҪͶƱ�����ˡ�
	 * ���ʱ��data���γ�Ӧ����ֻ��һ�б�ǩ�ˡ���ô���Ǿ�����һ�б�ǩ�����ģ���Ϊ��𷵻ء�
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

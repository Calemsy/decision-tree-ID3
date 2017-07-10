package com.ali;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Mnist {

	ID3 id3 = null;
	DecisionTree decisionTree = null;
	private ArrayList<ArrayList<String>> probeSet = new ArrayList<>();
	
	private String trainingSetFileName = "./MNIST/training.data";
	private String probeSetFileName = "./MNIST/probe.data";

	public Mnist() {
		this.id3 = new ID3(this.trainingSetFileName, ",");
		this.decisionTree = id3.trainingDecisionTree();
	}

	public void calculatePrecise() {

		this.getProbeSet();

		DecisionTree treePointer = null;
		String predictLabel = null;
		int labelIndex = this.probeSet.get(0).size() - 1;
		double success = 0.0;

		for (int i = 0; i < this.probeSet.size(); i++) {
			// treePointer = this.decisionTree;
			try {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(this.decisionTree);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						byteArrayOutputStream.toByteArray());
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
				treePointer = (DecisionTree) objectInputStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}

			HashMap<String, String> userData = new HashMap<>();
			for (int j = 0; j < this.probeSet.get(i).size() - 1; j++) {
				userData.put(id3.bankFeatureName.get(j), this.probeSet.get(i).get(j));
			}

			while (true) {
				// System.out.println(((DecisionTree)
				// treePointer).getAttributeName());
				// System.out.println(userData.get(((DecisionTree)
				// treePointer).getAttributeName()));
				if (((DecisionTree) treePointer).children
						.get((userData).get(((DecisionTree) treePointer).getAttributeName())) != null) {
					// 这里判断的情况是：在probe set中某一个sample的某个feature的value在所有的training
					// set中的sample
					// 中的该feature上都没有出现过。所以决策树根本没有针对这个feature这个取值的判断（或者说分支）。
					// 例如，天气这个feature，在training set中只有rain和sunny,但是在probe
					// set中却出现了windy
					// 此时，决策树没有学过当天气取值为windy应该做什么。所以这种情况下我们只能“无知”的返回。
					if (!((((DecisionTree) treePointer).children
							.get((userData.get((((DecisionTree) treePointer).getAttributeName()))))).getClass()
									.getSimpleName().equals("String"))) {
						treePointer = (DecisionTree) ((DecisionTree) treePointer).children
								.get(userData.get(((DecisionTree) treePointer).getAttributeName()));
					} else {// 类型是string，说明到了决策节点，或者说是叶子节点。
						predictLabel = (String) ((DecisionTree) treePointer).children
								.get(userData.get((((DecisionTree) treePointer).getAttributeName())));
						break;
					}
				} else {
					predictLabel = "-1";
					break;
				}
			}
			if (this.probeSet.get(i).get(labelIndex).equals(predictLabel)) {
				// System.out.println("YES true label is: " + this.probeSet.get(i).get(labelIndex) + " predict label is " + predictLabel);
				success += 1;
			} else {
				// System.out.println("NO true label is: " + this.probeSet.get(i).get(labelIndex) + " predict label is " + predictLabel);
			}
		}
		System.out.println("\r\tprecise is " + success / (double) this.probeSet.size());
	}

	private void getProbeSet() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(this.probeSetFileName));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				ArrayList<String> temp = new ArrayList<>();
				String[] arr = line.split(",");
				for (String string : arr) {
					temp.add(string);
				}
				this.probeSet.add(temp);
			}
			bufferedReader.close();

//			// fetch top 10
//			System.out.println("top 10 of probe set: ");
//			for (int i = 0; i < 10 && i < this.probeSet.size(); i++) {
//				System.out.println(this.probeSet.get(i));
//			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Probe Set Size :" + this.probeSet.size());
	}

	public static void main(String args[]) {
		Mnist mnist = new Mnist();
		mnist.calculatePrecise();
	}

}

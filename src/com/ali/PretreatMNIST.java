/**
 * 原始的MINST数据的个数是 label + features
 * 决策树算法需要的格式是features + label，所以需要先预处理下在feed给算法
 */
package com.ali;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PretreatMNIST {

	private final String trainSetFile = "./Mnist/training.data";
	private final String probeSetFile = "./Mnist/probe.data";

	private ArrayList<String> data;

	/**
	 * 原始数据是label+feature，现在改成feature+label only-one-run
	 */
	public void pretreatData() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./MNIST.c"));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./MNISTDecisionTree.c"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] sample = line.split(",");
				for (int i = 1; i < sample.length; i++) {
					bufferedWriter.write(sample[i] + ",");
				}
				bufferedWriter.write(sample[0]);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获得原始数据中的4000条在本地处理,调试好后再服务器上跑40000+的数据
	 */
	public void getSubSet() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./MNISTDecisionTree.c"));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./minMNISTDecisionTree.c"));
			String line;
			int lineCount = 0;
			while ((line = bufferedReader.readLine()) != null && (lineCount++) < 4000) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将数据进行修改，将非0的像素值都设置为1，如果像素值为0则保留原来的值
	 * 目前觉得当数据集是有限的时候，无法针对灰度图（像素值从0-255）来进行手写字识别
	 * 当数据集很大很大的时候，无穷大的时候，可以使用决策树来识别。事实上，当数据集 较小的时候，假设在training
	 * set中，所有的sample的pixel200的取值上只有0和1的取值， 那么当probe
	 * set中某个sample在pixel200处的取值为2（除了0和1）的时候，很有可能
	 * 决策树就无法判别了，因为在training的时候，根本没有learning在pixel200值为2时候 的情形。
	 * 所以在这里将所有的值转为0或者1，这种情况还是有可能出现的，只是概率小了很多。 only-one-run
	 */
	public void imageGray2Binary() {
		String inFileName = "MNISTDecisionTree.c";
		String outFileName = "MNIST01DecisionTree.c";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFileName));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFileName));

			String line = null;
			line = bufferedReader.readLine();
			bufferedWriter.write(line);
			bufferedWriter.newLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] strings = line.split(",");
				for (int i = 0; i < strings.length; i++) {
					if (i < strings.length - 1 && !strings[i].equals("0")) {
						strings[i] = "1";
					}
					if (i < strings.length - 1)
						bufferedWriter.write(strings[i] + ",");
					else {
						bufferedWriter.write(strings[i]);
						bufferedWriter.newLine();
					}
				}
			}
			bufferedWriter.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 划分训练集和预测集
	 */
	public void partitionTrainAndProbe() {
		int trainSetCounts, probeSetCounts;
		data = new ArrayList<>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./minMNIST01DecisionTree.c"));
			BufferedWriter bwTrain = new BufferedWriter(new FileWriter(this.trainSetFile));
			BufferedWriter bwProbe = new BufferedWriter(new FileWriter(this.probeSetFile));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				data.add(line);
			}

			trainSetCounts = (int) (data.size() * 0.9);
			probeSetCounts = data.size() - trainSetCounts;
			System.out.println("data size is " + data.size());
			System.out.println("trainingSet size is " + trainSetCounts);
			System.out.println("probeSet size is " + probeSetCounts);

			int i;
			for (i = 0; i < trainSetCounts; i++) {
				bwTrain.write(data.get(i));
				bwTrain.newLine();
			}
			for (i = trainSetCounts; i < data.size(); i++) {
				bwProbe.write(data.get(i));
				bwProbe.newLine();
			}
			bwProbe.close();
			bwTrain.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		PretreatMNIST mnist = new PretreatMNIST();
		// minst.pretreatData();
		// minst.getSubSet();
		// mnist.imageGray2Binary();
		mnist.partitionTrainAndProbe();
	}
}

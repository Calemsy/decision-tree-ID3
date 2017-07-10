/**
 * ԭʼ��MINST���ݵĸ����� label + features
 * �������㷨��Ҫ�ĸ�ʽ��features + label��������Ҫ��Ԥ��������feed���㷨
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
	 * ԭʼ������label+feature�����ڸĳ�feature+label only-one-run
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
	 * ���ԭʼ�����е�4000���ڱ��ش���,���Ժú��ٷ���������40000+������
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
	 * �����ݽ����޸ģ�����0������ֵ������Ϊ1���������ֵΪ0����ԭ����ֵ
	 * Ŀǰ���õ����ݼ������޵�ʱ���޷���ԻҶ�ͼ������ֵ��0-255����������д��ʶ��
	 * �����ݼ��ܴ�ܴ��ʱ��������ʱ�򣬿���ʹ�þ�������ʶ����ʵ�ϣ������ݼ� ��С��ʱ�򣬼�����training
	 * set�У����е�sample��pixel200��ȡֵ��ֻ��0��1��ȡֵ�� ��ô��probe
	 * set��ĳ��sample��pixel200����ȡֵΪ2������0��1����ʱ�򣬺��п���
	 * ���������޷��б��ˣ���Ϊ��training��ʱ�򣬸���û��learning��pixel200ֵΪ2ʱ�� �����Ρ�
	 * ���������ｫ���е�ֵתΪ0����1��������������п��ܳ��ֵģ�ֻ�Ǹ���С�˺ܶࡣ only-one-run
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
	 * ����ѵ������Ԥ�⼯
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

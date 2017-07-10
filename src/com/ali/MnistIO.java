package com.ali;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MnistIO {

	public ArrayList<ArrayList<String>> trainingSet;
	public ArrayList<ArrayList<String>> probeSet;
	public ArrayList<String> featureName;

	public MnistIO() {
		trainingSet = new ArrayList<>();
		probeSet = new ArrayList<>();
		featureName = new ArrayList<>();
	}

	private void getTrainingSet() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./MNIST/training.data"));
			String line;
			
			line = bufferedReader.readLine();
			for (String string : line.split(",")) {
				this.featureName.add(string);
			}

			while ((line = bufferedReader.readLine()) != null) {
				ArrayList<String> temp = new ArrayList<>();
				String[] arr = line.split(",");
				for (int i = 0; i < arr.length; i++) {
					temp.add(arr[i]);
				}
				this.trainingSet.add(temp);
				//temp.clear(); ´ó¿Ó	
			}
			bufferedReader.close();

			// fetch top 10
			System.out.println(featureName);
			System.out.println("top 10 training set: ");
			for (int i = 0; i < 10; i++) {
				System.out.println(this.trainingSet.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getProbeSet() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./MNIST/probe.data"));
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

			// fetch top 10
			System.out.println("top 10 of probe set: ");
			for (int i = 0; i < 10; i++) {
				System.out.println(this.probeSet.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		MnistIO mnistIO = new MnistIO();
		mnistIO.getTrainingSet();
		mnistIO.getProbeSet();
	}
}

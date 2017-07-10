package com.ali;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @feature 所有的数据，包括sample的feature和label，每一行的最后一个数据为label,
 *          数据的类型为String(feature和label的类型都是String)
 * @featureName 所有属性的名称。
 * @author robin
 *
 */
public class IOoperation {

	private String filename;
	public ArrayList<ArrayList<String>> feature;// 指所有的训练数据
	public ArrayList<String> featureName;// 指所有的训练数据的类名

	private String splitFlag = null;

	public IOoperation(String filename, String splitFlag) {
		this.filename = filename;
		feature = new ArrayList<>();
		featureName = new ArrayList<>();
		this.splitFlag = splitFlag;
	}

	public ArrayList<ArrayList<String>> getFeature() {
		return feature;
	}

	public ArrayList<String> getFeatureName() {
		return featureName;
	}

	public void getFeatureAndLabel() {
		try {
			Scanner scanner = new Scanner(new File(this.filename));
			String line0[] = scanner.nextLine().split(this.splitFlag); // 根据数据格式的不同而定
			for (int i = 0; i < line0.length; i++) {
				featureName.add(line0[i]);
			}
			while (scanner.hasNextLine()) {
				String line[] = scanner.nextLine().split(this.splitFlag); // 根据数据格式的不同而定
				ArrayList<String> oneSample = new ArrayList<>();
				for (int i = 0; i < line.length; i++) {
					oneSample.add(line[i]);
				}
				feature.add(oneSample);
			}
			scanner.close();

//			System.out.println("show the information about input:");
//			for (int i = 0; i < featureName.size(); i++) {
//				System.out.print(featureName.get(i) + " ");
//			}
//			System.out.println();
//			for (int i = 0; i < 20 && i < feature.size(); i++) {
//				for (int j = 0; j < feature.get(i).size(); j++) {
//					System.out.print(feature.get(i).get(j) + " ");
//				}
//				System.out.println();
//			}
//			System.out.println();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Training Set Size :" + this.feature.size());
	}

//	public static void main(String args[]) {
//		IOoperation iOoperation = new IOoperation("./lenses.txt", "\t");
//		iOoperation.getFeatureAndLabel();
//	}
}
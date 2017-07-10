package com.ali;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DecisionTree implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String attributeName;
	public HashMap<String, Object> children;
	private String decisionTree = "./outputTree/decisionTree.data";

	public void printTree(Object tree, ArrayList<String> record, BufferedWriter bufferedWriter) {
		if (tree instanceof String) {
			record.add((String) tree);
			System.out.println(record);
			try {
				bufferedWriter.write(record.toString());
				bufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			record.remove(record.size() - 1);
			record.remove(record.size() - 1);
			return;
		}
		record.add(((DecisionTree) tree).getAttributeName());
		for (String key : ((DecisionTree) tree).children.keySet()) {
			record.add(key);
			printTree(((DecisionTree) tree).children.get(key), record, bufferedWriter);
		}
		int count = 1;
		while( record.size() > 0 && count <= 2){
			record.remove(record.size() - 1);
			count++;
		}
	}
	
	public void saveDecisionTree(Object tree)
	{
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.decisionTree));
			this.printTree(tree, new ArrayList<>(), bufferedWriter);
			bufferedWriter.close();
			System.out.println("\r\nthe decision tree has saved in the file: './outputTree/decisionTree.data'");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printTree1(Object tree) {
		if (tree == null) {
			return;
		}
		if (tree instanceof String) {
			System.out.println(tree);
			return;
		}
		System.out.print(((DecisionTree) tree).getAttributeName());
		for (String key : ((DecisionTree) tree).children.keySet()) {
			System.out.print("\t" + key + "\t");
			printTree1(((DecisionTree) tree).children.get(key));
		}
	}
	
	public DecisionTree() {
		this.children = new HashMap<>();
	}
	public String getAttributeName() {
		return attributeName;
	}
	public HashMap<String, Object> getChildren() {
		return children;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public void setChildren(HashMap<String, Object> children) {
		this.children = children;
	}
}

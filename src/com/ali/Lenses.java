package com.ali;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lenses {
	ID3 id3 = null;
	DecisionTree decisionTree = null;

	public Lenses() {
		this.id3 = new ID3("./lenses.txt", "\t");
		this.decisionTree = id3.trainingDecisionTree();
	}

	public String answerYou() {
		Scanner scanner = new Scanner(System.in);
		Map<String, String> userData = new HashMap<>();
		String answer = null;
		System.out.println(
				"\r\nI can help you choose the type of contact lenses£¬as long as you answer me a few quentions");
		System.out.println("ok, let's start.");
		System.out.println("tearRate? please type 'reduced' or 'normal'");
		userData.put("tearRate", scanner.next());
		System.out.println("astigmatic? please type 'yes' or 'no'");
		userData.put("astigmatic", scanner.next());
		System.out.println("prescript? please type 'hyper' or 'myope'");
		userData.put("prescript", scanner.next());
		System.out.println("age? please type 'pre', 'presbyopic' or 'young'");
		userData.put("age", scanner.next());

		System.out.println("show the information about your input");
		System.out.println(userData);

		DecisionTree treePointer = this.decisionTree;

		while (true) {
			if (!((((DecisionTree) treePointer).children
					.get((userData.get((((DecisionTree) treePointer).getAttributeName()))))).getClass().getSimpleName()
							.equals("String"))) {
				treePointer = (DecisionTree) ((DecisionTree) treePointer).children
						.get(userData.get(((DecisionTree) treePointer).getAttributeName()));
			} else {
				answer = (String) ((DecisionTree) treePointer).children
						.get(userData.get((((DecisionTree) treePointer).getAttributeName())));
				return answer;
			}
		}
	}

	public static void main(String args[]) {
		Lenses lenses = new Lenses();
		System.out.println("\r\nThe type of contants lenses that fits you is " + lenses.answerYou());
	}
}

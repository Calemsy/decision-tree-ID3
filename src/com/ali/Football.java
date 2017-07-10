package com.ali;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Football {

	ID3 id3 = null;
	DecisionTree decisionTree = null;

	public Football() {
		this.id3 = new ID3("./football.txt", " ");	
		// 创建一个ID3对象，将trainingSet的名称和文件中用于分隔数据的符号(这里是空格" ")传递给构造函数
		this.decisionTree = id3.trainingDecisionTree();
		// 构造决策树
	}

	public String answerYou() {
		Scanner scanner = new Scanner(System.in);
		Map<String, String> userData = new HashMap<>();
		String answer = null;
		System.out.println("\r\n回答几个问题，我可以帮你决定是否应该出去踢球。");
		System.out.println("天气怎么样呢? 晴? 阴? 还是 雨?");
		userData.put("天气", scanner.next());
		System.out.println("温度怎么样呢? 炎热? 适中? 还是 寒冷?");
		userData.put("温度", scanner.next());
		System.out.println("湿度呢? 高? 还是 正常?");
		userData.put("湿度", scanner.next());
		System.out.println("风速呢? 强? 还是弱? ");
		userData.put("风速", scanner.next());

		System.out.println("show the information about your input");
		System.out.println(userData);

		DecisionTree treePointer = this.decisionTree;

		while (true) {
			System.out.println("用户在" + ((DecisionTree) treePointer).getAttributeName() + "的取值是"
					+ userData.get((((DecisionTree) treePointer).getAttributeName())));
			if (!((((DecisionTree) treePointer).children
					.get((userData.get((((DecisionTree) treePointer).getAttributeName()))))).getClass().getSimpleName()
							.equals("String"))) {
				treePointer = (DecisionTree) ((DecisionTree) treePointer).children
						.get(userData.get(((DecisionTree) treePointer).getAttributeName()));
			} else {
				System.out.println(((DecisionTree) treePointer).children
						.get(userData.get((((DecisionTree) treePointer).getAttributeName()))));
				answer = (String) ((DecisionTree) treePointer).children
						.get(userData.get((((DecisionTree) treePointer).getAttributeName())));
				return answer;
			}
		}
	}

	public static void main(String args[]) {
		Football football = new Football();
		System.out.println("\r\n足球活动应该" + football.answerYou());
	}
}

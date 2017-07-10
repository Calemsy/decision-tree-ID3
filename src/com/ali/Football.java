package com.ali;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Football {

	ID3 id3 = null;
	DecisionTree decisionTree = null;

	public Football() {
		this.id3 = new ID3("./football.txt", " ");	
		// ����һ��ID3���󣬽�trainingSet�����ƺ��ļ������ڷָ����ݵķ���(�����ǿո�" ")���ݸ����캯��
		this.decisionTree = id3.trainingDecisionTree();
		// ���������
	}

	public String answerYou() {
		Scanner scanner = new Scanner(System.in);
		Map<String, String> userData = new HashMap<>();
		String answer = null;
		System.out.println("\r\n�ش𼸸����⣬�ҿ��԰�������Ƿ�Ӧ�ó�ȥ����");
		System.out.println("������ô����? ��? ��? ���� ��?");
		userData.put("����", scanner.next());
		System.out.println("�¶���ô����? ����? ����? ���� ����?");
		userData.put("�¶�", scanner.next());
		System.out.println("ʪ����? ��? ���� ����?");
		userData.put("ʪ��", scanner.next());
		System.out.println("������? ǿ? ������? ");
		userData.put("����", scanner.next());

		System.out.println("show the information about your input");
		System.out.println(userData);

		DecisionTree treePointer = this.decisionTree;

		while (true) {
			System.out.println("�û���" + ((DecisionTree) treePointer).getAttributeName() + "��ȡֵ��"
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
		System.out.println("\r\n����Ӧ��" + football.answerYou());
	}
}

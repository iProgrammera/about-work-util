package com.dailintong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Tony
 * @version ����ʱ�䣺2016��4��26�� ����3:33:52 ��˵��
 */
public class CodeLine {

	public static int normalLines = 0; // ��Ч��������
	public static int whiteLines = 0; // �հ�����
	public static int commentLines = 0; // ע������

	public static void main(String[] args) throws IOException {

		File file = new File("D://Programmer//workspace//Eclipse//AppealSystem");
		if (file.exists()) {
			statistic(file);
		}
		System.out.println("����Ч��������: " + normalLines);
		System.out.println("�ܿհ�������" + whiteLines);
		System.out.println("��ע��������" + commentLines);
		System.out.println("��������" + (normalLines + whiteLines + commentLines));
	}

	private static void statistic(File file) throws IOException {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				statistic(files[i]);
			}
		}
		if (file.isFile()) {
			// ͳ����չ��Ϊjava���ļ�
			if (file.getName().matches(".*//.java")) {
				parse(file);
			}else if(file.getName().matches(".*//.jsp")){
				parse(file);
			}else if(file.getName().matches(".*//.js")){
				parse(file);
			}
		}

	}

	public static void parse(File file) {
		BufferedReader br = null;
		// �жϴ����Ƿ�Ϊע����
		boolean comment = false;
		int temp_whiteLines = 0;
		int temp_commentLines = 0;
		int temp_normalLines = 0;

		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.matches("^[//s&&[^//n]]*$")) {
					// ����
					whiteLines++;
					temp_whiteLines++;
				} else if (line.startsWith("/*") && !line.endsWith("*/")) {
					// �жϴ���Ϊ"/*"��ͷ��ע����
					commentLines++;
					comment = true;
				} else if (comment == true && !line.endsWith("*/")) {
					// Ϊ����ע���е�һ�У����ǿ�ͷ�ͽ�β��
					commentLines++;
					temp_commentLines++;
				} else if (comment == true && line.endsWith("*/")) {
					// Ϊ����ע�͵Ľ�����
					commentLines++;
					temp_commentLines++;
					comment = false;
				} else if (line.startsWith("//")) {
					// ����ע����
					commentLines++;
					temp_commentLines++;
				} else {
					// ����������
					normalLines++;
					temp_normalLines++;
				}
			}

			System.out.println("��Ч����" + temp_normalLines + " ,�հ�����" + temp_whiteLines + " ,ע������" + temp_commentLines
					+ " ,������" + (temp_normalLines + temp_whiteLines + temp_commentLines) + "     " + file.getName());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

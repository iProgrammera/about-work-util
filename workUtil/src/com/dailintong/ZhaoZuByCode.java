package com.dailintong;
/** 
* @author Tony 
* @version ����ʱ�䣺2016��6��22�� ����5:16:38 
* ��˵��    ��������
*/
public class ZhaoZuByCode {

	public static void main(String[] args) {
		int[] attr = {0,1,4,6,8,9};
		int[] index = {1,4,5,1,0,2,4,0,3,0,0};
		StringBuffer sb = new StringBuffer();
		for(int i : index){
			sb.append(attr[i]);
		}
		System.out.println("��ϵ��ʽ��" + sb.toString());
	}

}

package com.dailintong;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**     -----���޳�û-----
������������������������������ 
�������������������ߩ��������ߩ� 
�������������������������������� 
�������������������ש������ס��� 
�������������������������������� 
�����������������������ߡ������� 
�������������������������������� 
��������������������������������������������¥�����Ҵ�����Bug��
���������������������������� 
���������������������������� 
������������������������������������ 
�����������������������������������ǩ� 
�������������������������������������� 
�����������������������������ש����� 
�����������������������ϩϡ����ϩ� 
�����������������������ߩ������ߩ�
 */
public class CopyFileBySvnLog {

	public static void main(String[] args) {

		String oldroot = "D:\\Programmer\\workspace\\Eclipse\\ISS_FI";
		String newroot = "D:\\Programmer\\workspace\\SVNUPDATE\\ISS_FI";
		String xiangmu="/FI_v2/develop/Develop_FIv2.0" ;
		
//		String oldroot = "D:\\Programmer\\workspace\\Eclipse\\AppealSystem";
//		String newroot = "D:\\Programmer\\workspace\\SVNUPDATE\\Appeal";
//		String xiangmu="/AppealSystem" ;
		
		boolean iswithlb=true;//�Ƿ��ӡ�����ļ��б�
		try {
			FileReader fr = new FileReader("D:\\Programmer\\workspace\\SVNUPDATE\\SVN.txt");
			BufferedReader br = new BufferedReader(fr);
			String filename = br.readLine();
			int count=0;
			int nblcount=0;
			int okcount=0;
			Set<String> scount=new HashSet<String>();
			Set<String> snblcount=new HashSet<String>();
			Set<String> sokcount=new HashSet<String>();
			Set<String> deletecount=new HashSet<String>();
			Set<String> configcount=new HashSet<String>();
			while (filename != null) {
				//+���Ӷ�ֱ�Ӵ�С���꿽���б��ʽ������
				if(filename.startsWith("Deleted :")){
					filename=filename.replace("Deleted : ", "");
					if(filename.contains("/src/"))filename="\\WebRoot\\WEB-INF\\classes\\"+filename.substring(filename.indexOf("/src/")+5);
					filename=filename.replace(xiangmu, "");
					filename=filename.replace(".java", ".class");
					filename=filename.replaceAll("/", "\\\\");
					deletecount.add(filename);
					filename = br.readLine();
					continue;
				}else if(filename.contains(".properties")||filename.contains("datasource.xml")||filename.contains("FileDomainConfig.xml")||filename.contains("SMSConfig.xml")){
					if(filename.startsWith("Added : ")&&filename.contains("(Copy from path:")){
						filename=filename.substring(0, filename.indexOf("(Copy from path:"));
					}
					filename=filename.replace("Modified : ", "");
					filename=filename.replace("Added : ", "");
					if(filename.contains("/src/"))filename="\\WebRoot\\WEB-INF\\classes\\"+filename.substring(filename.indexOf("/src/")+5);
					filename=filename.replace(xiangmu, "");
					filename=filename.replace(".java", ".class");
					filename=filename.replaceAll("/", "\\\\");
					configcount.add(filename);
					filename = br.readLine();
					continue;
				}else if((!filename.startsWith("Modified : ")&&!filename.startsWith("Added : "))){
					filename = br.readLine();
					continue;
				}else{
					//���ӶԿ����ļ���֧��
					if(filename.startsWith("Added : ")&&filename.contains("(Copy from path:")){
						filename=filename.substring(0, filename.indexOf("(Copy from path:"));
					}
					filename=filename.replace("Modified : ", "");
					filename=filename.replace("Added : ", "");
					if(filename.contains("/src/"))filename="\\WebRoot\\WEB-INF\\classes\\"+filename.substring(filename.indexOf("/src/")+5);
					filename=filename.replace(xiangmu, "");
					filename=filename.replace(".java", ".class");
					filename=filename.replaceAll("/", "\\\\");
					count++;okcount++;scount.add(filename);sokcount.add(filename);
					System.out.println("���Ƶ�"+count+"���ļ���"+filename);
				}
				//-
				File f = new File(oldroot + filename);
				if (f.exists()) {// �ƶ��ļ�
					File fto = new File(newroot + filename.replaceAll("/", "\\\\"));
					if (!fto.getParentFile().exists())
						fto.getParentFile().mkdirs();
					// f.renameTo(fto);
					try {
						FileUtils.copyFile(f, fto);
					} catch (IOException i) {
						System.out.println("�ļ�����ʧ�ܣ�" + fto);okcount--;sokcount.remove(filename);
					}

					// ��Ҫ������ļ�ʱ.class��ʱ��Ҫ���Ǳ�����ǰ�ļ��У����Ƿ������ڲ���
					if (f.getName().endsWith(".class")) {
						File parentDIR = f.getParentFile();// �õ����ļ������ļ���
						File[] fbrothers = parentDIR.listFiles();// �õ�ͬ�ļ����µ������ļ�
						for (File fb : fbrothers) {// ��������������а���filename+$����Ҳ����������ȥ
							if (fb.getName().startsWith(f.getName().replace(".class", "") + "$")) {
								count++;nblcount++;okcount++;scount.add(fb.getAbsolutePath().replace(oldroot, ""));sokcount.add(fb.getAbsolutePath().replace(oldroot, ""));snblcount.add(fb.getAbsolutePath().replace(oldroot, ""));
								System.out.println("���Ƶ�"+count+"���ļ���"+fb.getName());
								File fbnew = new File(fto.getParent()+File.separatorChar+fb.getName());
								if (!fbnew.getParentFile().exists())
									fbnew.getParentFile().mkdirs();
								try {
									FileUtils.copyFile(fb, fbnew);
								} catch (IOException i) {
									System.out.println("�ļ�����ʧ�ܣ�" + fb);okcount--;sokcount.remove(fb.getAbsolutePath().replace(oldroot, ""));
								}
							}
						}

					}

				}else{
					System.out.println("�ļ������ڣ�" + f);okcount--;sokcount.remove(filename);
				}
				filename = br.readLine();
			}
			br.close();
			System.out.println("һ������"+count+"�Σ��ɹ�"+okcount+"�Σ������ڲ���"+nblcount+"�Ρ�");
			System.out.println("ȥ�غ󹲸����ļ�"+scount.size()+"�����ɹ�"+sokcount.size()+"���������ڲ���"+snblcount.size()+"����");
		if(iswithlb){
		Iterator<String> i=sokcount.iterator();
		int j=0;
		System.out.println("-------------���Ƴɹ��ļ��б�-------------");
		while(i.hasNext()){
			System.out.println((++j)+":"+i.next().toString());
		}
		System.out.println("-------------���Ƴɹ����ļ��б�-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
			String s=i.next().toString();
			if(s.endsWith(".class"))
			System.out.println((++j)+":"+s);
		}
		System.out.println("-------------���Ƴɹ�sql�ļ��б�-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
			String s=i.next().toString();
			if(s.endsWith(".sql")||s.endsWith(".SQL"))
			System.out.println((++j)+":"+s);
		}
		System.out.println("-------------���Ƴɹ������ļ��б�-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
		
			String s=i.next().toString();
			if(!s.endsWith(".sql")&&!s.endsWith(".SQL")&&!s.endsWith(".class"))
			System.out.println((++j)+":"+s);
		}
		StringBuffer shoudong=new StringBuffer();
		shoudong.append("-------------��Ҫ�ֶ�ɾ���ļ��б�-------------\n");
		j=0;
		i=deletecount.iterator();
		while(i.hasNext()){
			String s=(++j)+":"+i.next().toString();
			shoudong.append(s+"\n");
		}
		shoudong.append("-------------��Ҫ�ֶ��޸������ļ��б�-------------\n");
		
		i=configcount.iterator();
		while(i.hasNext()){
			String s=(++j)+":"+i.next().toString();
			shoudong.append(s+"\n");
			
		}
		String shoudongss=shoudong.toString();
		System.out.println(shoudongss);
		File fto = new File(newroot+ "\\���ֶ�ɾ���ļ����ֶ��޸������ļ��б�.txt");
		FileUtils.writeStringToFile(fto, shoudongss);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

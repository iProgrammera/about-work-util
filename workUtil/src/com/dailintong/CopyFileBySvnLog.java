package com.dailintong;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**     -----神兽出没-----
　　　　　　　　┏┓　　　┏┓ 
　　　　　　　┏┛┻━━━┛┻┓ 
　　　　　　　┃　　　━　　　┃ 
　　　　　　　┃　┳┛　┗┳　┃ 
　　　　　　　┃　　　　　　　┃ 
　　　　　　　┃　　　┻　　　┃ 
　　　　　　　┗━┓　　　┏━┛ 
　　　　　　　　　┃　　　┃　　　　　神兽镇楼，保我代码无Bug！
　　　　　　　　　┃　　　┃ 
　　　　　　　　　┃　　　┃ 
　　　　　　　　　┃　　　┗━━━┓ 
　　　　　　　　　┃　　　　　　　┣┓ 
　　　　　　　　　┃　　　　　　　┏┛ 
　　　　　　　　　┗┓┓┏━┳┓┏┛ 
　　　　　　　　　　┃┫┫　┃┫┫ 
　　　　　　　　　　┗┻┛　┗┻┛
 */
public class CopyFileBySvnLog {

	public static void main(String[] args) {

		String oldroot = "D:\\Programmer\\workspace\\Eclipse\\ISS_FI";
		String newroot = "D:\\Programmer\\workspace\\SVNUPDATE\\ISS_FI";
		String xiangmu="/FI_v2/develop/Develop_FIv2.0" ;
		
//		String oldroot = "D:\\Programmer\\workspace\\Eclipse\\AppealSystem";
//		String newroot = "D:\\Programmer\\workspace\\SVNUPDATE\\Appeal";
//		String xiangmu="/AppealSystem" ;
		
		boolean iswithlb=true;//是否打印最终文件列表
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
				//+增加对直接从小海龟拷的列表格式的适配
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
					//增加对拷贝文件的支持
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
					System.out.println("复制第"+count+"个文件："+filename);
				}
				//-
				File f = new File(oldroot + filename);
				if (f.exists()) {// 移动文件
					File fto = new File(newroot + filename.replaceAll("/", "\\\\"));
					if (!fto.getParentFile().exists())
						fto.getParentFile().mkdirs();
					// f.renameTo(fto);
					try {
						FileUtils.copyFile(f, fto);
					} catch (IOException i) {
						System.out.println("文件拷贝失败：" + fto);okcount--;sokcount.remove(filename);
					}

					// 当要打包的文件时.class的时候，要考虑遍历当前文件夹，看是否有其内部类
					if (f.getName().endsWith(".class")) {
						File parentDIR = f.getParentFile();// 得到此文件所处文件夹
						File[] fbrothers = parentDIR.listFiles();// 得到同文件夹下的所有文件
						for (File fb : fbrothers) {// 遍历，如果名称中包含filename+$，则也打到升级包中去
							if (fb.getName().startsWith(f.getName().replace(".class", "") + "$")) {
								count++;nblcount++;okcount++;scount.add(fb.getAbsolutePath().replace(oldroot, ""));sokcount.add(fb.getAbsolutePath().replace(oldroot, ""));snblcount.add(fb.getAbsolutePath().replace(oldroot, ""));
								System.out.println("复制第"+count+"个文件："+fb.getName());
								File fbnew = new File(fto.getParent()+File.separatorChar+fb.getName());
								if (!fbnew.getParentFile().exists())
									fbnew.getParentFile().mkdirs();
								try {
									FileUtils.copyFile(fb, fbnew);
								} catch (IOException i) {
									System.out.println("文件拷贝失败：" + fb);okcount--;sokcount.remove(fb.getAbsolutePath().replace(oldroot, ""));
								}
							}
						}

					}

				}else{
					System.out.println("文件不存在：" + f);okcount--;sokcount.remove(filename);
				}
				filename = br.readLine();
			}
			br.close();
			System.out.println("一共复制"+count+"次，成功"+okcount+"次，其中内部类"+nblcount+"次。");
			System.out.println("去重后共复制文件"+scount.size()+"个，成功"+sokcount.size()+"个，其中内部类"+snblcount.size()+"个。");
		if(iswithlb){
		Iterator<String> i=sokcount.iterator();
		int j=0;
		System.out.println("-------------复制成功文件列表-------------");
		while(i.hasNext()){
			System.out.println((++j)+":"+i.next().toString());
		}
		System.out.println("-------------复制成功类文件列表-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
			String s=i.next().toString();
			if(s.endsWith(".class"))
			System.out.println((++j)+":"+s);
		}
		System.out.println("-------------复制成功sql文件列表-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
			String s=i.next().toString();
			if(s.endsWith(".sql")||s.endsWith(".SQL"))
			System.out.println((++j)+":"+s);
		}
		System.out.println("-------------复制成功其他文件列表-------------");
		i=sokcount.iterator();
		while(i.hasNext()){
		
			String s=i.next().toString();
			if(!s.endsWith(".sql")&&!s.endsWith(".SQL")&&!s.endsWith(".class"))
			System.out.println((++j)+":"+s);
		}
		StringBuffer shoudong=new StringBuffer();
		shoudong.append("-------------需要手动删除文件列表-------------\n");
		j=0;
		i=deletecount.iterator();
		while(i.hasNext()){
			String s=(++j)+":"+i.next().toString();
			shoudong.append(s+"\n");
		}
		shoudong.append("-------------需要手动修改配置文件列表-------------\n");
		
		i=configcount.iterator();
		while(i.hasNext()){
			String s=(++j)+":"+i.next().toString();
			shoudong.append(s+"\n");
			
		}
		String shoudongss=shoudong.toString();
		System.out.println(shoudongss);
		File fto = new File(newroot+ "\\需手动删除文件及手动修改配置文件列表.txt");
		FileUtils.writeStringToFile(fto, shoudongss);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

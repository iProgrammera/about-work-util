package com.dailintong;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class ReadExcel
{
	public ReadExcel()
	{
	}
/**
 * 读取Excel，解析为由String数组组成的列表，列表中每一个数组代表Excel中的一行，从第一行读取
 * @param excelFile
 * @param colcount 每行读几列
 * @return
 */
	public static List<String[]> getExcel(InputStream excelFilein,int colcount)
	{
		List<String[]> list = new ArrayList<String[]>();
		try
		{
			HSSFWorkbook workbook = new HSSFWorkbook(excelFilein);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 0; r < rows; r++)
			{
				HSSFRow row = sheet.getRow(r);
				if (row != null)
				{
					String value = "";
					String strs[] = new String[colcount];
					boolean isallnull=true;//所有列都是空值时，认为不存在此列
					for (short c = 0; c < colcount; c++)
					{
						HSSFCell cell = row.getCell(c);
						if (cell != null)
							switch (cell.getCellType())
							{
							case 0: // '\0'
								
								HSSFCellStyle style = cell.getCellStyle();
								int i = style.getDataFormat();
								if(HSSFDateUtil.isCellDateFormatted(cell)||(i==31)){
									value = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd"); 
									if(!value.startsWith("19")&&!value.startsWith("20"))value = (new StringBuilder(String.valueOf((double)cell.getNumericCellValue()))).toString();
								}else{
									value = (new StringBuilder(String.valueOf(cell.getNumericCellValue()))).toString();
								}
								break;

							case 1: // '\001'
								value = (new StringBuilder()).append(cell.getRichStringCellValue()).toString();
								break;
							 case HSSFCell.CELL_TYPE_FORMULA:
								 try {
							     value = String.valueOf(cell.getNumericCellValue());
								 } catch (IllegalStateException e) {
								     value = String.valueOf(cell.getRichStringCellValue());
								 }
								 break;
							default:
								value = "";
								break;
							}
						if(cell==null)strs[c] = "";
						else strs[c] = value;
						if(!trimWhitespace(strs[c]).equals(""))isallnull=false;
					}
					if(!isallnull)list.add(strs);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	//以下两个是工具类方法
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}
	

}
package helper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtilBig {

	 public static Workbook createWorkBookx(List<Map<String, Object>> list,
			 String []keys,String columnNames[],String gzb) throws IOException {
	        // 创建excel工作簿
		 	int rowaccess=100;
	        Workbook wb = new SXSSFWorkbook(rowaccess);
	        // 创建第一个sheet（页），并命名
	        if (list != null  && list.size() > 0) {
		        // 创建两种单元格格式
		        CellStyle cs = wb.createCellStyle();
		        CellStyle cs2 = wb.createCellStyle();

		        // 创建两种字体
		        Font f = wb.createFont();
		        Font f2 = wb.createFont();

		        // 创建第一种字体样式（用于列名）
		        f.setFontHeightInPoints((short) 10);
		        f.setColor(IndexedColors.BLACK.getIndex());
		        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		        // 创建第二种字体样式（用于值）
		        f2.setFontHeightInPoints((short) 10);
		        f2.setColor(IndexedColors.BLACK.getIndex());

		        // 设置第一种单元格的样式（用于列名）
		        cs.setFont(f);
		        cs.setBorderLeft(CellStyle.BORDER_THIN);
		        cs.setBorderRight(CellStyle.BORDER_THIN);
		        cs.setBorderTop(CellStyle.BORDER_THIN);
		        cs.setBorderBottom(CellStyle.BORDER_THIN);
		        cs.setAlignment(CellStyle.ALIGN_CENTER);

		        // 设置第二种单元格的样式（用于值）
		        cs2.setFont(f2);
		        cs2.setBorderLeft(CellStyle.BORDER_THIN);
		        cs2.setBorderRight(CellStyle.BORDER_THIN);
		        cs2.setBorderTop(CellStyle.BORDER_THIN);
		        cs2.setBorderBottom(CellStyle.BORDER_THIN);
		        cs2.setAlignment(CellStyle.ALIGN_CENTER);
	            int totle=list.size();
	            int mus=1000000;//设置每页显示50000笔数据
	            int avg=totle/mus;
	            int index=0;
	            for(int i = 0; i < avg+1; i++){
	            	Sheet sh  = wb.createSheet("Sheet"+(i+1));
	            	
	            	// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
	    	        for(int k=0;k<keys.length;k++){
	    	        	sh.setColumnWidth((int) k, (int) (35.7 * 150));
	    	        }
	            	// 创建第一行
	            	Row xRow0 = sh.createRow(0);
	            	//set Sheet页头部
	            	for(int j=0;j<columnNames.length;j++){
	    	            Cell cell = xRow0.createCell(j);
	    	            cell.setCellValue(columnNames[j]);
	    	            cell.setCellStyle(cs);
	    	        }
                    int num=i*mus;
                    
                    for(int m=num;m<(num+mus);m++){
	                    if(index==totle){
	                    	list.clear();
	                    	break;
	                    }
	                    Row xRow = sh.createRow(m + 1-mus*i);
	                    for(int j=0;j<keys.length;j++){
	                    	Cell cell = xRow.createCell(j);
	    	                cell.setCellValue(list.get(m).get(keys[j]) == null?" ": list.get(m).get(keys[j]).toString());
	    	                cell.setCellStyle(cs2);
	    	            }
	                        index++;
	                        //每当行数达到设置的值就刷新数据到硬盘,以清理内存
	                        if(m%rowaccess==0){
	                        	((SXSSFSheet)sh).flushRows();
	                        }
                    }
	            }
	        }
	        return wb;
	    }
}

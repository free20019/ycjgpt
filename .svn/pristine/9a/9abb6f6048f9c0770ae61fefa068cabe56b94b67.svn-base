package helper;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
public class ExcelUtilHB {
	 public static Workbook createWorkBook(List<Map<String,Object>> tlelist,
			 	List<Map<String,Object>> nrlist,
	    		List<Map<String,Object>> cslist,
	    		List<Map<String,Object>> wzlist,
	    		List<Map<String,Object>> jtsjlist,String gzb) {
	        // 创建excel工作簿
	        Workbook wb = new HSSFWorkbook();
	        // 创建第一个sheet（页），并命名
	        Sheet sheet = wb.createSheet(gzb);
	        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
//	        for(int i=0;i<keys.length;i++){
//	            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
//	        }

	        // 创建第一行
	        Row row = sheet.createRow((short) 0);

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

//	        Font f3=wb.createFont();
//	        f3.setFontHeightInPoints((short) 10);
//	        f3.setColor(IndexedColors.RED.getIndex());

	        // 设置第一种单元格的样式（用于列名）
	        cs.setFont(f);
	        cs.setBorderLeft(CellStyle.BORDER_THIN);
	        cs.setBorderRight(CellStyle.BORDER_THIN);
	        cs.setBorderTop(CellStyle.BORDER_THIN);
	        cs.setBorderBottom(CellStyle.BORDER_THIN);
	        cs.setAlignment(CellStyle.ALIGN_CENTER);
	        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
	        cs.setAlignment(CellStyle.ALIGN_CENTER);//水平
	        cs.setWrapText(true);

	        // 设置第二种单元格的样式（用于值）
	        cs2.setFont(f2);
	        cs2.setBorderLeft(CellStyle.BORDER_THIN);
	        cs2.setBorderRight(CellStyle.BORDER_THIN);
	        cs2.setBorderTop(CellStyle.BORDER_THIN);
	        cs2.setBorderBottom(CellStyle.BORDER_THIN);
	        cs2.setAlignment(CellStyle.ALIGN_CENTER);
	        cs2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
	        cs2.setAlignment(CellStyle.ALIGN_CENTER);//水平
	        cs2.setWrapText(true);
	        /**
	         * 往excle写内容
	         */
	        Cell cell = row.createCell(0);
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            cell.setCellValue("日期");
            sheet.setColumnWidth((short) 0, (short) (40 * 150));
            cell.setCellStyle(cs);
            Cell cell1 = row.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 2));  
            cell1.setCellValue(String.valueOf(tlelist.get(0).get("RIQI")));
            cell1.setCellStyle(cs2);
            Cell cell2 = row.createCell(3);
            cell2.setCellValue("设备终端数:");
            sheet.setColumnWidth((short) 3, (short) (35.7 * 150));
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(4);
            cell3.setCellValue(String.valueOf(tlelist.get(0).get("ZHONGDUAN")));
            sheet.setColumnWidth((short) 4, (short) (40 * 150));
            cell3.setCellStyle(cs2);
            Cell cell4 = row.createCell(5);
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 11));
            cell4.setCellValue("相关线路沿途路段的特殊天气、路况情况及措施");
            cell4.setCellStyle(cs);
            
            
            Row row1 = sheet.createRow((short) 1);
            Cell cellsx = row1.createCell(3);
	        cellsx.setCellValue("设备上线数");
	        cellsx.setCellStyle(cs);
	        Cell cellsxs = row1.createCell(4);
	        cellsxs.setCellValue(String.valueOf(tlelist.get(0).get("SHANGXIAN")));
	        cellsxs.setCellStyle(cs2);
	        Cell cell10 = row1.createCell(5);
            sheet.addMergedRegion(new CellRangeAddress(1, 3, 5, 11));
            cell10.setCellValue(String.valueOf(tlelist.get(0).get("TIANQI")).equals("null")?"":String.valueOf(tlelist.get(0).get("TIANQI")));
            cell10.setCellStyle(cs2);
            
            
            //创建第三行
            Row row2 = sheet.createRow((short) 2);
            Cell cell5 = row2.createCell(0);
            cell5.setCellValue("监控人员");
            cell5.setCellStyle(cs);
            Cell cell6 = row2.createCell(1);
            cell6.setCellValue(String.valueOf(tlelist.get(0).get("JIANK1")).equals("null")?"":String.valueOf(tlelist.get(0).get("JIANK1")));
            cell6.setCellStyle(cs2);
            Cell cell7 = row2.createCell(2);
            cell7.setCellValue(String.valueOf(tlelist.get(0).get("JIANK2")).equals("null")?"":String.valueOf(tlelist.get(0).get("JIANK2")));
            cell7.setCellStyle(cs2);
            Cell cell8 = row2.createCell(3);
            cell8.setCellValue(String.valueOf(tlelist.get(0).get("JIANK3")).equals("null")?"":String.valueOf(tlelist.get(0).get("JIANK3")));
            cell8.setCellStyle(cs2);
            Cell cell9 = row2.createCell(4);
            cell9.setCellValue(String.valueOf(tlelist.get(0).get("JIANK4")).equals("null")?"":String.valueOf(tlelist.get(0).get("JIANK4")));
            cell9.setCellStyle(cs2);
            
            Row row3 = sheet.createRow((short) 3);
            Cell cell11 = row3.createCell(0);
            cell11.setCellValue("监控时段");
            cell11.setCellStyle(cs);
            Cell cell12 = row3.createCell(1);
            cell12.setCellValue((String.valueOf(tlelist.get(0).get("SHIDS1")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDS1")))+"/"+(String.valueOf(tlelist.get(0).get("SHIDE1")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDE1"))));
            cell12.setCellStyle(cs2);
            Cell cell13 = row3.createCell(2);
            cell13.setCellValue((String.valueOf(tlelist.get(0).get("SHIDS2")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDS2")))+"/"+(String.valueOf(tlelist.get(0).get("SHIDE2")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDE2"))));
            cell13.setCellStyle(cs2);
            Cell cell14 = row3.createCell(3);
            cell14.setCellValue((String.valueOf(tlelist.get(0).get("SHIDS3")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDS3")))+"/"+(String.valueOf(tlelist.get(0).get("SHIDE3")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDE3"))));
            cell14.setCellStyle(cs2);
            Cell cell15 = row3.createCell(4);
            cell15.setCellValue((String.valueOf(tlelist.get(0).get("SHIDS4")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDS4")))+"/"+(String.valueOf(tlelist.get(0).get("SHIDE4")).equals("null")?"":String.valueOf(tlelist.get(0).get("SHIDE4"))));
            cell15.setCellStyle(cs2);
            
            Row row4 = sheet.createRow((short) 4);
            Cell cellch = row4.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(4, 5, 0, 0));
            cellch.setCellValue("车号");
            cellch.setCellStyle(cs);
            Cell cellsp = row4.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(4, 5, 1, 1));
            cellsp.setCellValue("是否安装视频");
            sheet.setColumnWidth((short) 1, (short) (35.7 * 150));
            cellsp.setCellStyle(cs);
            Cell celljsy = row4.createCell(2);
            sheet.addMergedRegion(new CellRangeAddress(4, 5, 2, 2));
            celljsy.setCellValue("驾驶员/押运员");
            sheet.setColumnWidth((short) 2, (short) (35.7 * 150));
            celljsy.setCellStyle(cs);
            Cell celllx = row4.createCell(3);
            sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 5));
            celllx.setCellValue("运行时间、线路");
            celllx.setCellStyle(cs);
            Cell celljl = row4.createCell(6);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 11));
            celljl.setCellValue("实时监控中发现的异常情况记录");
            celljl.setCellStyle(cs);
            
            Row row5 = sheet.createRow((short) 5);
            Cell cellsj1 = row5.createCell(6);
            cellsj1.setCellValue("时间");
            cellsj1.setCellStyle(cs);
            Cell cellqk1 = row5.createCell(7);
            cellqk1.setCellValue("情况");
            cellqk1.setCellStyle(cs);
            Cell cellcs1 = row5.createCell(8);
            cellcs1.setCellValue("措施");
            cellcs1.setCellStyle(cs);
            Cell cellsj2 = row5.createCell(9);
            cellsj2.setCellValue("时间");
            cellsj2.setCellStyle(cs);
            Cell cellqk2 = row5.createCell(10);
            cellqk2.setCellValue("情况");
            cellqk2.setCellStyle(cs);
            Cell cellcs2 = row5.createCell(11);
            cellcs2.setCellValue("措施");
            cellcs2.setCellStyle(cs);
            
            for (int i = 0; i < nrlist.size(); i++) {
            		int srow = 6+i*12;
            		int erow = 6+i*12+11;
            		int a = 0;
            		for (int j = 0; j < 24; j+=2) {
            			Row row7 = sheet.createRow((short) srow+a);
                		Cell cells1 = row7.createCell(0);
                		sheet.addMergedRegion(new CellRangeAddress(srow, erow, 0, 0));
                		cells1.setCellValue(String.valueOf(nrlist.get(i).get("VHIC")));
                		cells1.setCellStyle(cs2);
                		Cell cells2 = row7.createCell(1);
                		sheet.addMergedRegion(new CellRangeAddress(srow, erow, 1, 1));
                		cells2.setCellValue(String.valueOf(nrlist.get(i).get("SHIPIN")));
                		cells2.setCellStyle(cs2);
                		Cell cells3 = row7.createCell(2);
                		sheet.addMergedRegion(new CellRangeAddress(srow, erow, 2, 2));
                		cells3.setCellValue(String.valueOf(nrlist.get(i).get("JIASY")));
                		cells3.setCellStyle(cs2);
                		Cell cells4 = row7.createCell(3);
                		sheet.addMergedRegion(new CellRangeAddress(srow, erow, 3, 5));
                		cells4.setCellValue(String.valueOf(nrlist.get(i).get("YXSTART")));
                		cells4.setCellStyle(cs2);
            			String yctime1 = "YCTIME"+(j+1);
            			String ycqk1 = "YCQK"+(j+1);
            			String yccs1 = "YCCS"+(j+1);
            			String yctime2 = "YCTIME"+(j+2);
            			String ycqk2 = "YCQK"+(j+2);
            			String yccs2 = "YCCS"+(j+2);
            			Cell cells6 = row7.createCell(6);
            			cells6.setCellValue(String.valueOf(nrlist.get(i).get(yctime1)));
            			sheet.setColumnWidth((short) 6, (short) (40 * 150));
            			cells6.setCellStyle(cs2);
            			Cell cells7 = row7.createCell(7);
            			cells7.setCellValue(String.valueOf(nrlist.get(i).get(ycqk1)));
            			cells7.setCellStyle(cs2);
            			Cell cells8 = row7.createCell(8);
            			cells8.setCellValue(String.valueOf(nrlist.get(i).get(yccs1)));
            			cells8.setCellStyle(cs2);
            			Cell cells9 = row7.createCell(9);
            			cells9.setCellValue(String.valueOf(nrlist.get(i).get(yctime2)));
            			sheet.setColumnWidth((short) 9, (short) (40 * 150));
            			cells9.setCellStyle(cs2);
            			Cell cells10 = row7.createCell(10);
            			cells10.setCellValue(String.valueOf(nrlist.get(i).get(ycqk2)));
            			cells10.setCellStyle(cs2);
            			Cell cells11 = row7.createCell(11);
            			cells11.setCellValue(String.valueOf(nrlist.get(i).get(yccs2)));
            			cells11.setCellStyle(cs2);
            			a++;
					}
			}
            int hh = 6+nrlist.size()*12;
            Row rowcs = sheet.createRow((short) hh);
    		Cell cells1 = rowcs.createCell(0);
    		cells1.setCellValue("具体内容措施");
    		cells1.setCellStyle(cs);
    		Cell cells2 = rowcs.createCell(1);
    		sheet.addMergedRegion(new CellRangeAddress(hh, hh, 1, 11));
    		cells2.setCellValue("具体内容措施：超速车辆停车休息时，电话提醒减速慢行；疲劳驾驶超过规定时间未停车休息，电话提醒，确保安全！");
    		cells2.setCellStyle(cs2);
    		Row rowwf = sheet.createRow((short) hh+1);
    		Cell cellw0 = rowwf.createCell(0);
    		sheet.addMergedRegion(new CellRangeAddress(hh+1, hh+10, 0, 0));
    		cellw0.setCellValue("违法违章情况分析");
    		cellw0.setCellStyle(cs);
    		Cell cellw1 = rowwf.createCell(1);
    		sheet.addMergedRegion(new CellRangeAddress(hh+1, hh+6, 1, 1));
    		cellw1.setCellValue("超速排名");
    		cellw1.setCellStyle(cs);
    		Cell cellw2 = rowwf.createCell(2);
    		cellw2.setCellValue("车号");
    		cellw2.setCellStyle(cs);
    		Cell cellw3 = rowwf.createCell(3);
    		cellw3.setCellValue("姓名");
    		cellw3.setCellStyle(cs);
    		Cell cellw4 = rowwf.createCell(4);
    		cellw4.setCellValue("超速报警次数");
    		cellw4.setCellStyle(cs);
    		Cell cellw5 = rowwf.createCell(5);
    		cellw5.setCellValue("最高时速(km/s)");
    		cellw5.setCellStyle(cs);
    		int hh1 = hh+1;
    		for (int j = 0; j < cslist.size(); j++) {
    			Row rowwf1 = sheet.createRow((short) hh1+1+j);
    			Cell cellww2 = rowwf1.createCell(2);
        		cellww2.setCellValue(String.valueOf(cslist.get(j).get("VHIC")));
        		cellww2.setCellStyle(cs2);
        		Cell cellww3 = rowwf1.createCell(3);
        		cellww3.setCellValue(String.valueOf(cslist.get(j).get("NAME")).equals("null")?"":String.valueOf(cslist.get(j).get("NAME")));
        		cellww3.setCellStyle(cs2);
        		Cell cellww4 = rowwf1.createCell(4);
        		cellww4.setCellValue(String.valueOf(cslist.get(j).get("CSNUMBER")));
        		cellww4.setCellStyle(cs2);
        		Cell cellww5 = rowwf1.createCell(5);
        		cellww5.setCellValue(String.valueOf(cslist.get(j).get("SHIS")));
        		cellww5.setCellStyle(cs2);
			}
    		int hh2 = hh1+cslist.size()+1;
    		Row rowwz = sheet.createRow((short) hh2);
    		Cell cellwz = rowwz.createCell(1);
    		sheet.addMergedRegion(new CellRangeAddress(hh2, hh2+3, 1, 1));
    		cellwz.setCellValue("超速排名");
    		cellwz.setCellStyle(cs);
    		Cell cellwz1 = rowwz.createCell(2);
    		cellwz1.setCellValue("车号");
    		cellwz1.setCellStyle(cs);
    		Cell cellwz2 = rowwz.createCell(3);
    		cellwz2.setCellValue("姓名");
    		cellwz2.setCellStyle(cs);
    		Cell cellwz3 = rowwz.createCell(4);
    		cellwz3.setCellValue("超速报警次数");
    		cellwz3.setCellStyle(cs);
    		int bz = hh2+4;
    		Row rowbz = sheet.createRow((short) bz);
     		Cell cellbz1 = rowbz.createCell(0);
     		cellbz1.setCellValue("备注");
     		cellbz1.setCellStyle(cs);
     		Cell cellbz2 = rowbz.createCell(1);
     		sheet.addMergedRegion(new CellRangeAddress(bz, bz, 1, 11));
     		cellbz2.setCellValue(String.valueOf(tlelist.get(0).get("BEIZHU")).equals("null")?"":String.valueOf(tlelist.get(0).get("BEIZHU")));
     		cellbz2.setCellStyle(cs2);
     		Row rowjtsj = sheet.createRow((short) bz+1);
     		Cell celljtsj = rowjtsj.createCell(0);
     		sheet.addMergedRegion(new CellRangeAddress(bz+1, bz+1, 0, 11));
     		celljtsj.setCellValue("超速违章车辆具体违章时间");
     		celljtsj.setCellStyle(cs);
     		for (int i = 0; i < jtsjlist.size(); i++) {
     			Row rowjtsj1 = sheet.createRow((short) bz+2+i);
     			Cell celljtsj1 = rowjtsj1.createCell(0);
     			celljtsj1.setCellValue(String.valueOf(jtsjlist.get(i).get("VHIC")));
     			celljtsj1.setCellStyle(cs2);
        		Cell celljtsj2 = rowjtsj1.createCell(1);
        		sheet.addMergedRegion(new CellRangeAddress(bz+2+i, bz+2+i, 1, 11));
        		celljtsj2.setCellValue(String.valueOf(jtsjlist.get(i).get("ILL_TIME")));
        		celljtsj2.setCellStyle(cs2);
			}
	        return wb;
	    }
}

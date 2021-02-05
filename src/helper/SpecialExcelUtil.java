package helper;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

public class SpecialExcelUtil {
	public  String download(HttpServletRequest request,
    		HttpServletResponse response, List<Map<String, Object>> list,String[] key2,String name) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
        	createWorkBookReport(list,key2,name).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((name + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return null;
    }
	/*
	  * 运力分析导出
	  */
	private  Workbook createWorkBookReport(List<Map<String, Object>> list,String[] key,String name)  {
	        
	        Workbook workbook = new HSSFWorkbook();
	        Sheet sheet = workbook.createSheet(name);
	        // 创建两种单元格格式
	        CellStyle cs = workbook.createCellStyle();
	        CellStyle cs2 = workbook.createCellStyle();

	        // 创建两种字体
	        Font f = workbook.createFont();
	        Font f2 = workbook.createFont();

	        // 创建第一种字体样式（用于列名）
	        f.setFontHeightInPoints((short) 10);
	        f.setColor(IndexedColors.BLACK.getIndex());
	        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

	        // 创建第二种字体样式（用于值）
	        f2.setFontHeightInPoints((short) 10);
	        f2.setColor(IndexedColors.BLACK.getIndex());

	        // 设置第一种单元格的样式（用于列名）
	        cs.setFont(f);
	        setStyle(cs);

	        // 设置第二种单元格的样式（用于值）
	        cs2.setFont(f2);
	        setStyle(cs2);

	        // 创建第一行
	        Row row = sheet.createRow((short) 0);
	        CellRange(sheet,0,1  ,0,0);
	        CellRange(sheet,0,1  ,10,10);
	        //需要合并三列的列名
	        String[] threeColCloumnNames = new String[]{"每日派单","每日完单","活跃车辆"};
	        for(int i=0;i<threeColCloumnNames.length;i++){
	            CellRange(sheet, 0,0,(i*3) +1 ,(i*3) +3);
	            //给单元格加黑框样式
	            row.createCell((i*3) +2 ).setCellStyle(cs);
	            row.createCell((i*3) +3 ).setCellStyle(cs);
	            //给单元格赋值
	            Cell cell = row.createCell((i*3) +1 );
	            cell.setCellValue(threeColCloumnNames[i]);
	            cell.setCellStyle(cs);
	        }
	        //需要合并二列的列名
	        String[] twoColCloumnNames = new String[]{"当前热点区域分析","当前热点时段分析"};
	        for(int i=0;i<twoColCloumnNames.length;i++){
	            CellRange(sheet, 0,0,i*2+11 ,i*2+12);
	            //给单元格加黑框样式
	            row.createCell(i*2+11 ).setCellStyle(cs);
	            row.createCell(i*2+12).setCellStyle(cs);
	            //给单元格赋值
	            Cell cell = row.createCell(i*2+11 );
	            cell.setCellValue(threeColCloumnNames[i]);
	            cell.setCellStyle(cs);
	        }
	        // 创建第二行
	        Row row1 = sheet.createRow((short) 1);
	        //设置正常列名
	        String[] cloumnNames = new String[]{"日期","派单总量(单)","昨日环比(%)","上周同比(%)","完单总量(单)","昨日环比(%)","上周同比(%)","接单车数(辆)","昨日环比(%)","上周同比(%)","网约车完单率","当日热点区域","热点区域订单数","当日热点时段","热点时段订单数"};
	        for(int i=0;i<cloumnNames.length;i++){
	            //设置列宽
	            sheet.setColumnWidth(i, 5000);
	            if(i==0 || i == 10){
	                Cell cell = row.createCell(i);
	                cell.setCellValue(cloumnNames[i]);
	                cell.setCellStyle(cs);
	                continue;
	            }
	            //给单元格赋值
	            Cell cell = row1.createCell(i);
	            cell.setCellValue(cloumnNames[i]);
	            cell.setCellStyle(cs);
	        }
	        //合并单元格
	        for(int i=0;i<list.size() / 5 ;i++){
	            for (int j=0;j<11;j++){
	                CellRange(sheet,2+i*5,6+i*5  ,j,j);
	            }
	        }
	        for(int i=0;i<list.size();i++){
	            Row rowN = sheet.createRow((short) i+2);
	            for (int j=0;j<key.length;j++){
	                Cell cell = rowN.createCell(j);
	                cell.setCellValue(String.valueOf(list.get(i).get(key[j])));
	                cell.setCellStyle(cs2);
	            }
	        }
	        return workbook;
	    }
	    //设置样式
	 private  void setStyle(CellStyle cs){
	        cs.setBorderLeft(CellStyle.BORDER_THIN);
	        cs.setBorderRight(CellStyle.BORDER_THIN);
	        cs.setBorderTop(CellStyle.BORDER_THIN);
	        cs.setBorderBottom(CellStyle.BORDER_THIN);
	        cs.setAlignment(CellStyle.ALIGN_CENTER);
	        //垂直居中
	        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

	    }
	    //合并单元格
	    private  void CellRange( Sheet sheet,int firstRow, int lastRow,int firstCol, int lastCol){
	        CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow,lastRow  ,firstCol,lastCol);
	        sheet.addMergedRegion(cellRangeAddress);
	    }
}

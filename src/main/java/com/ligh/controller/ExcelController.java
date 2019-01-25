package com.ligh.controller;

/**
 * Created by ${ligh} on 2019/1/25 上午8:42
 */
import com.ligh.domain.User;
import com.ligh.service.UserService;
import com.ligh.utils.ExcelUtils;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelController {
    @Autowired
    private UserService userService;
    @RequestMapping("/")
    public String toIndex(){
        return "poi";
    }
    @RequestMapping("/excel/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        if(!ExcelUtils.validateExcel(file.getOriginalFilename())){
            System.out.println("文件必须是excel!");
            return null;
        }
        long size=file.getSize();
        if(file.getOriginalFilename()==null || file.getOriginalFilename().equals("") || size==0){
            System.out.println("文件不能为空");
            return null;
        }
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        int numberOfSheets = workbook.getNumberOfSheets();//获得有多少sheet
        HSSFSheet sheet = workbook.getSheetAt(0);//默认只有一个sheet
        int rows = sheet.getPhysicalNumberOfRows();//获得sheet有多少行
        //遍历行
        for (int j = 0; j < rows; j++) {
            if (j == 0) {
                continue;//标题行(省略)
            }
            HSSFRow row = sheet.getRow(j);
            User user = null;
            for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                HSSFCell cell = row.getCell(k);
                System.out.println(cell.toString());
                user = new User();
                //获取当前行数据
                long id = (long)row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String username = row.getCell(2).getStringCellValue();
                Date date = row.getCell(3).getDateCellValue();
                user.setId(id);
                user.setName(name);
                user.setUsername(username);
                user.setCreateTime(date);

                //存储到数据库


            }
            userService.insertUser(user);

        }
        return "poi";
    }
    //生成user表excel
    @GetMapping(value = "/excel/getUser")
    @ResponseBody
    public String getUser(HttpServletResponse response) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook,sheet);
       /* List<User> rows = new ArrayList<>();//userService.getAll();
        rows.add(new User((long)1,"小明","牛逼",new Date()));
        rows.add(new User((long)2,"中明","牛2逼",new Date()));*/
        List<User> rows = userService.queryAllUser();
        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        //新增数据行，并且设置单元格数据
        int rowNum=1;
        for(User user:rows){
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getUsername());
            HSSFCell cell = row.createCell(3);
            cell.setCellValue(user.getCreateTime());
            cell.setCellStyle(style);
            rowNum++;
        }
        String fileName = "导出excel例子.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }
    //创建表头
    private void createTitle(HSSFWorkbook workbook,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(3,17*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("显示名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
    }
    //生成excel文件
    protected void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }
    //浏览器下载excel
    protected void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}

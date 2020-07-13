package com.mark.markcoffee.fileDown;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.mark.markcoffee.entity.Person;
import com.mark.markcoffee.util.FileUtil;
import com.ylkz.constants.redisKey.RedisKey_CodeConstants;
import com.ylkz.entity.dto.DemandDataDto;
import com.ylkz.util.ExcelUtils;
import com.ylkz.util.RedisUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "download")
public class DownloadController {

    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

//    @Autowired
//    private RedisUtil redisUtil;

    private static Map<Object, Object> codePostMap = new HashMap<Object, Object>(16);    //岗位属性
    private static Map<Object, Object> cityMap = new HashMap<>(16);       //省市区






    @RequestMapping(value = "export",method = RequestMethod.GET)
    public void export(HttpServletResponse response){

        //模拟从数据库获取需要导出的数据
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person("路飞","1",28,new Date());
        Person person2 = new Person("娜美","2", 25,new Date());
        Person person3 = new Person("索隆","1", 28,new Date());
        Person person4 = new Person("小狸猫","1", 16,new Date());
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        //导出操作
        try {
            FileUtil.exportExcel(personList,"花名册","草帽一伙",Person.class,"海贼王.xls",response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "importExcel",method = RequestMethod.GET)
    public void importExcel(){
        String filePath = "D:\\海贼王.xls";
        //解析excel，
        List<Person> personList = null;
        try {
            personList = FileUtil.importExcel(filePath,1,1, Person.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //也可以使用MultipartFile,使用 FileUtil.importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass)导入
        System.out.println("导入数据一共【"+personList.size()+"】行");

        //TODO 保存数据库
    }



    /**
     * 导出职位发布模板
     *
     * @param response
     */
    /*@RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) {
        long start = System.currentTimeMillis();
        //初始化code
        initializationMap();
        initializationCityMap();
        String fileName = "职位发布模板";
        try {
            Workbook workbook = ExcelExportUtil.exportExcel( new ExportParams(null, fileName, ExcelType.XSSF), DemandDataDto.class, null);
            //职位类型
            String[] codePost_array = codePostMap.values().toArray(new String[codePostMap.size()]);
            ExcelUtils.selectList(workbook, 1,3, 3, codePost_array);
            //省市区
            List<Object> list = new ArrayList<>(cityMap.keySet());
            Object[] objects = list.stream()
                    .map(s -> s.toString().substring(0, s.toString().indexOf(",")))
                    .toArray();
            String[] city_array = Arrays.copyOf(objects, objects.length, String[].class);
            ExcelUtils.selectList(workbook, 1,4, 4, city_array);
            //学历
            String[] education_array = new String[]{"小学", "初中","中专","高中","大专","本科","硕士","博士"};
            ExcelUtils.selectList(workbook, 1,4, 4, education_array);
            //性别
            String[] gender_array = new String[]{"不限","男", "女"};
            ExcelUtils.selectList(workbook, 1,4, 4, gender_array);
            ExcelUtils.downLoadExcel(fileName, response, workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("导出excel所花时间：" + (System.currentTimeMillis() - start));
    }*/

    public void initializationMap() {
       /* if (redisUtil.hasKey(RedisKey_CodeConstants.BASE_CODEPOST_INFO)) {
            codePostMap = redisUtil.hmget(RedisKey_CodeConstants.BASE_CODEPOST_INFO);
        }*/
    }

    public void initializationCityMap() {
       /* if (redisUtil.hasKey(RedisKey_CodeConstants.BASE_ALLCITY_INFO)) {
            cityMap = redisUtil.hmget(RedisKey_CodeConstants.BASE_ALLCITY_INFO);
        }*/
    }




}

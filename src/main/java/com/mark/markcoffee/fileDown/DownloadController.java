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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = "download")
public class DownloadController {

    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Autowired
    private RestTemplate restTemplate;

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


    /**
     * 多线程下载大文件
     */
    @RequestMapping(value = "downLoadBigFile",method = RequestMethod.GET)
    public void downLoadBigFile() throws IOException {
        multiThreadDownload("https://dldir1.qq.com/qqtv/TencentVideo11.17.7063.0.exe","D://tempFile/",10);
    }



    public void multiThreadDownload(String fileUrl,String filePath,int threadNum) throws IOException {
        System.out.println("开始下载文件。。。");
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        long startTime = System.currentTimeMillis();

        //通过Http协议的Head方法获取到文件的总大小
        HttpHeaders httpHeads = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeads);
        ResponseEntity<String> entity = restTemplate.exchange(fileUrl, HttpMethod.HEAD, requestEntity, String.class);

        long contentLength = entity.getHeaders().getContentLength();
        //均分文件的大小
        long step = contentLength / threadNum;

        List<CompletableFuture<Object>> futures = new ArrayList<>();
        for (int index = 0; index < threadNum; index++) {
            //计算出每个线程的下载开始位置和结束位置
            long start = step * index;
            long end = index == threadNum - 1 ? 0 : (step * (index + 1) - 1);
            FileResponseExtractor extractor = new FileResponseExtractor(filePath + ".download." + index);

            CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
                RequestCallback callback = request -> {
                    //设置HTTP请求头Range信息，开始下载到临时文件
                    request.getHeaders().add(HttpHeaders.RANGE, "bytes=" + start + "-" + end);
                };
                return restTemplate.execute(fileUrl, HttpMethod.GET, callback, extractor);
            }, executorService);
            futures.add(future);
        }
        
        //创建最终文件
        FileChannel outChannel = new FileOutputStream(new File(filePath)).getChannel();

        futures.forEach(future -> {
            try {
                File tmpFile = (File) future.get();
                FileChannel tmpIn = new FileInputStream(tmpFile).getChannel();
                //合并每个临时文件
                outChannel.transferFrom(tmpIn, outChannel.size(), tmpIn.size());
                tmpIn.close();
                //合并完成后删除临时文件
                tmpFile.delete();
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            }
        });
        outChannel.close();
        executorService.shutdown();

        System.out.println("下载文件完成，总共耗时: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }



}

package com.mark.markcoffee;

import com.mark.markcoffee.entity.AlbumDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.mark.markcoffee.util.DateTimeUtil.*;

/**
 * Java 8 应用
 */
public class Java8Test {

    private static Logger logger = LoggerFactory.getLogger(Java8Test.class);

    private static List<AlbumDto> albumDtos = Arrays.asList(
            new AlbumDto(1, "rqw459", "img/123", 1, 0),
            new AlbumDto(2, "rqw460", "img/123", 1, 2),
            new AlbumDto(3, "rqw461", "img/123", 1, 1),
            new AlbumDto(4, "rqw462", "img/123", 1, 4),
            new AlbumDto(5, "rqw463", "img/123", 1, 3),
            new AlbumDto(6, "rqw464", "img/123", 1, 0)
    );


    /**
     * 排序
     */
    private static void sort() {
        logger.info("——————数字排序——————");
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.sort(integers, Comparator.naturalOrder());
        integers.forEach(System.out::println);
        logger.info("——————字符串集合排序——————");
        List<String> namesNotNull = Arrays.asList("结衣", "浩二", "张三", "李四", "王五", "郝六", "阿七", "田八", "", "王石");
        logger.info("首字母倒序");
        Collections.sort(namesNotNull, Comparator.naturalOrder());
        namesNotNull.forEach(System.out::println);
        logger.info("首字母正序");
        Collections.sort(namesNotNull, Comparator.reverseOrder());
        namesNotNull.forEach(System.out::println);
        logger.info("包含null字符串集合排序(包含null值要使用nullsFirst or nullsLast 配合Comparator实现)");
        List<String> namesContainsNull = Arrays.asList("结衣", "浩二", "张三", "李四", "王五", "郝六", "阿七", "田八", null, "王石");
        Collections.sort(namesContainsNull, Comparator.nullsLast(Comparator.reverseOrder()));
        namesContainsNull.forEach(System.out::println);
        logger.info("——————对象集合排序——————");
        logger.info("二次排序");
        logger.info("注:comparing和thenComparing(先按照x排序，在按照y排序)、reversed(倒序)");
        albumDtos.stream()
                .sorted(Comparator.comparing(AlbumDto::getCategoryCode).reversed().thenComparing(AlbumDto::getSourceId))
                .forEach(System.out::println);
    }


    /**
     * Optional(可选接口)
     *
     * @param albumDto
     * @throws Throwable
     */
    private static Integer testOptional(AlbumDto albumDto) throws Throwable {
        Integer integer = Optional.ofNullable(albumDto)
                .map(AlbumDto::getCategoryCode)
                .filter(o -> o != 0)
                .orElse(-1)
                .intValue();

        return integer;
        /*Optional.ofNullable(albumDto)
                .map(AlbumDto::getCategoryCode)
                .filter(o -> o == 0)
                .ifPresent(System.out::println);*/
    }


    /**
     * stream
     */
    private static void testStream() {
        //初始化
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = strings.size();
        List<String> filtered = Arrays.asList();
        String mergedString = null;
        List<Integer> squaresList = Arrays.asList();
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> integers = Arrays.asList(1, 2, 13, 4, 15, 6, 17, 8, 19);
        Random random = new Random();


        System.out.println("使用 Java 8: ");
        System.out.println("列表: " + strings);

        count = strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串数量为: " + count);

        count = strings.stream().filter(string -> string.length() == 3).count();
        System.out.println("字符串长度为 3 的数量为: " + count);

        filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选后的列表: " + filtered);

        mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.println("Squares List: " + squaresList);
        System.out.println("列表: " + integers);

        IntSummaryStatistics stats = integers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
        System.out.println("随机数(0-10十个随机数): ");
        random.ints(10, 0, 10 + 1).sorted().forEach(System.out::println);

        // 并行处理
        count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量为: " + count);
    }

    /**
     * Java8 新引入线程安全的时间格式控件
     */
    private static void testLocalDate() {
        //获取当前时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dtf.format(LocalDateTime.now());
        System.out.println(format);
        //指定日期
        LocalDate localDate = LocalDate.of(2018, 12, 31);
        String format1 = dtf.format(localDate);
        System.out.println(format1);

        Integer year = 2019;
        System.out.println(getFirstDayOfThisYear(year));
        System.out.println(getLastDayOfThisYear(year));
        //
        System.out.println(DATETIME_FORMATTER.format(plusDays(1)));
        System.out.println(DATETIME_FORMATTER.format(plusDays(-1)));

        // 取第一个周一
        LocalDate ld = LocalDate.parse("2019-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println(DATE_FORMATTER.format(ld));
        //
        System.out.println(DATETIME_FORMATTER.format(firstDayOfWeekInYearMonth(year, 3)));
        System.out.println("-------------------");
        // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format();
        System.out.println(getStartDayOfWeekToString());
        System.out.println(getEndDayOfWeekToString());
        System.out.println("-------------------");
        System.out.println(DATETIME_FORMATTER.format(todayStart()));
        System.out.println(DATETIME_FORMATTER.format(todayEnd()));
    }


    /**
     * Java8 内置了Base64编码解码器
     */
    private static void base64Util() {
        try {
            // 使用基本编码
            String base64encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
            System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);

            // 解码
            byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);
            System.out.println("解码得到原始字符串: " + new String(base64decodedBytes, "utf-8"));
            base64encodedString = Base64.getUrlEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
            System.out.println("Base64 编码字符串 (URL) :" + base64encodedString);

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < 10; ++i) {
                stringBuilder.append(UUID.randomUUID().toString());
            }
            byte[] mimeBytes = stringBuilder.toString().getBytes("utf-8");
            String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
            System.out.println("Base64 编码字符串 (MIME) :\n" + mimeEncodedString);

        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

    /**
     * 测试局部变量再使用异步编程时是否存在变量不一致情况
     */
    private static void check() {
        AtomicReference<Double> i = new AtomicReference<>(0D);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            for (int j = 0; j < 2; j++) {
                new Thread(() -> {
                    System.out.println(i.getAndSet(i.get() + 1D));
                    countDownLatch.countDown();
                }).start();
            }
//        i.getAndSet(i.get() + 3D);
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(i.get());
    }

    public static void main(String[] args) {
        String[] array = new String[]{"Apple", "Orange", "Banana", "Lemon"};
        Arrays.sort(array, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        System.out.println(String.join(", ", array));


        Object o = new Object();
        Object o2 = o;
        o = null;
        System.out.println(o2);
        System.out.println(o == o2);
        check();
//        testStream();
//        sort();
       /* try {
            System.out.println(testOptional(albumDtos.get(0)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/
//        testStream();
//        testLocalDate();
//        base64Util();
        /*long durationHours = DateTimeUtil.durationHours(DateTimeUtil.parseLocalDateTime("2019-09-11 12:35:00"), DateTimeUtil.now());
        System.out.println(durationHours >= 3);
        AlbumDto albumDto = new AlbumDto();
        albumDto.setImgPath("123");
        AlbumDto albumDto1 = new AlbumDto();
        albumDto.setImgPath("456");
        System.out.println(albumDto.hashCode());
        System.out.println(albumDto1.hashCode());

        int count = 1;
        int newCount = count + 1;
        System.out.println(count);


        Matcher matcher = Pattern.compile("http://123").matcher("http://123?sign=456");
        System.out.println(matcher.matches());*/
    }


}

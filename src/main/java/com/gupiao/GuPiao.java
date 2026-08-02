package com.gupiao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GuPiao {




    /*
    *获取每天的价格
    * https://quote.eastmoney.com/kcb/512880.html#fullScreenChart
    * https://push2his.eastmoney.com/api/qt/stock/kline/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61&beg=0&end=20500101&ut=fa5fd1943c7b386f172d6893dbfba10b&rtntype=6&secid=1.512880&klt=101&fqt=1&cb=jsonp1725694300917
    * 只使用第二个就行了，把参数1.512880换成0.159915 159572就得到了创业板的
    *   1.512880 证券
    *   1.512900 南方证券
    *
    *
    *
    * */

    public static void main(String[] args){


        double liRunZuiDa = 0;
        String liRunZuiDaInfo = "";
        int gapQian = 10000;
        List<Long> times = time();
        List<JiaGe> jiaGeList = get();

        for(int h=0; h<100; h++) {
            jiaGeList.remove(0);
            if(times.get(1)<toTime(jiaGeList.get(0).getRiQi())){
                times.remove(0);
            }
            double gap = 0.03;
            while (gap<0.15) {
                int touZiChuShi = 30000;
                while (touZiChuShi<100000) {
                    for (int j = 0; j < 20; j++) {
                        int touZiZuiDa = touZiChuShi;
                        int touZiDongTai = touZiChuShi;
                        List<Double> maiMai = new ArrayList<>();
                        maiMai.add(0.0);
                        maiMai.add(14 - j * 0.01);//最低价

                        for (int i = 2; i < 100; i++) {
                            maiMai.add(maiMai.get(i - 1) * (1 + gap));
                        }
                        int oldIndex = 1;
                        for (int i = 2; i < 100; i++) {
                            if (maiMai.get(i - 1) > jiaGeList.get(0).getDi()) {
                                oldIndex = i - 1;
                                break;
                            }
                        }

                        int yueCount = 0;//一共多少个月
                        int yueTatal = 0;//每个月投资多少，

                        int timesCount = 0;
                        int count = 0;
                        for (JiaGe jiaGe : jiaGeList) {

                            Long currentTime = toTime(jiaGe.getRiQi());
                            if (currentTime > times.get(timesCount)) {
                                timesCount++;
                                yueTatal += touZiDongTai;
                            }
                            double di = jiaGe.getDi();
                            double gao = jiaGe.getGao();
                            double 买价 = maiMai.get(oldIndex - 1);
                            double 卖价 = maiMai.get(oldIndex + 1);
                            if (买价 > di) {
//                    System.out.println(jiaGe.getRiQi() + "，买：" + 买价);
                                oldIndex--;
                                touZiDongTai += gapQian;
                            }
                            //手里有票才会卖，否则就等着买
                            if(touZiDongTai > 0){
                                if (卖价 < gao) {
//                    System.out.println(jiaGe.getRiQi() + "，卖：" + 卖价);
                                    oldIndex++;
                                    count++;
                                    touZiDongTai -= gapQian;
                                }
                            }

                            if (touZiDongTai == 0) {
//                            System.out.println("高位了：" + jiaGe.getRiQi() + " ：" +  卖价);
                            }

                            if (touZiDongTai > touZiZuiDa) {
                                touZiZuiDa = touZiDongTai;
                            }
                        }
//            System.out.println("最大投资：" + touZiZuiDa);
//            System.out.println("最后投资：" + touZiDongTai);
            /*double feiYong = count * 5 + touZiChuShi * 0.01 + count * 0.0003 * gapQian;
            System.out.println("交易费用：" + feiYong);
            System.out.println(gap + "，挣了多少钱：" + (count * gap * gapQian - feiYong));*/
                        Double liRun = Double.parseDouble(String.format("%.3f", count * gap * gapQian));
                        Double ninaHua = liRun / yueTatal * 12;
                        if(liRun>5 * 10000){
                            System.out.println("年化：" + ninaHua);
                            System.out.println("初始资金：" +touZiChuShi +"，" + String.format("%.3f", gap) + "，交易次数：" + count + "，利润：" + liRun + "， 最低价：" + maiMai.get(1)
                                    + "，第一次买：" + jiaGeList.get(0).getRiQi()
                                    + "，最大投资：" + touZiZuiDa);
                        }
                        if (liRun > liRunZuiDa) {
                            liRunZuiDa = liRun;
                            liRunZuiDaInfo = "初始资金：" +touZiChuShi +"，" + String.format("%.3f", gap) + "，交易次数：" + count + "，利润：" + liRun + "， 最低价：" + maiMai.get(1)
                                    + "，第一次买：" + jiaGeList.get(0).getRiQi()
                                    + "，最大投资：" + touZiZuiDa;
                        }

                    }
                    touZiChuShi+=10000;
                }

                gap += 0.005;
            }
        }
        System.out.println("最多挣：" + liRunZuiDa);
        System.out.println(liRunZuiDaInfo);
    }

    public static Long toTime(String dateString) {

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

        // 将字符串解析为 LocalDate
        LocalDate date = LocalDate.parse(dateString, formatter);

        // 将 LocalDate 转换为时间戳（毫秒）
        long timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return timestamp + 3 * 60 * 60 * 1000;
    }

    public static List<Long> time(){
        List<Long> times = new ArrayList<>();
            // 起始日期和时间
//            LocalDateTime startDate = LocalDateTime.of(2017, 3, 1, 1, 0); // 2023年3月1号凌晨1点
            LocalDateTime startDate = LocalDateTime.of(2018, 2, 1, 1, 0); // 2023年3月1号凌晨1点
            LocalDateTime endDate = LocalDateTime.of(2025, 4, 1, 1, 0);   // 2025年3月1号凌晨1点

            // 循环生成每个月的1号凌晨1点的时间戳
            LocalDateTime currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // 转换为时间戳（毫秒）
                long timestamp = currentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                times.add(timestamp);

                // 增加一个月
                currentDate = currentDate.plusMonths(1);
            }
            return times;
    }

    public static List<JiaGe> get() {
        List<JiaGe> jiaGeList = new ArrayList<>();
        jiaGeList.add(new JiaGe("2021/12/1",15.68,15.21));
        jiaGeList.add(new JiaGe("2021/12/2",16.58,15.51));
        jiaGeList.add(new JiaGe("2021/12/3",17.01,16.1));
        jiaGeList.add(new JiaGe("2021/12/6",17.08,16.57));
        jiaGeList.add(new JiaGe("2021/12/7",16.99,16.56));
        jiaGeList.add(new JiaGe("2021/12/8",17.06,16.82));
        jiaGeList.add(new JiaGe("2021/12/9",17.04,16.72));
        jiaGeList.add(new JiaGe("2021/12/10",16.96,16.6));
        jiaGeList.add(new JiaGe("2021/12/13",17.14,16.44));
        jiaGeList.add(new JiaGe("2021/12/14",17.29,16.81));
        jiaGeList.add(new JiaGe("2021/12/15",17.44,17));
        jiaGeList.add(new JiaGe("2021/12/16",17.07,16.56));
        jiaGeList.add(new JiaGe("2021/12/17",17.39,16.85));
        jiaGeList.add(new JiaGe("2021/12/20",17.42,17.07));
        jiaGeList.add(new JiaGe("2021/12/21",17.55,17.14));
        jiaGeList.add(new JiaGe("2021/12/22",18.18,17.38));
        jiaGeList.add(new JiaGe("2021/12/23",19.03,18.06));
        jiaGeList.add(new JiaGe("2021/12/24",19.15,18.43));
        jiaGeList.add(new JiaGe("2021/12/27",19.25,18.65));
        jiaGeList.add(new JiaGe("2021/12/28",18.98,18.45));
        jiaGeList.add(new JiaGe("2021/12/29",18.95,18.35));
        jiaGeList.add(new JiaGe("2021/12/30",18.62,18.33));
        jiaGeList.add(new JiaGe("2021/12/31",18.95,18.42));
        jiaGeList.add(new JiaGe("2022/1/4",20.41,18.62));
        jiaGeList.add(new JiaGe("2022/1/5",21.1,20.05));
        jiaGeList.add(new JiaGe("2022/1/6",20.42,19.53));
        jiaGeList.add(new JiaGe("2022/1/7",20.25,19.55));
        jiaGeList.add(new JiaGe("2022/1/10",20.96,19.49));
        jiaGeList.add(new JiaGe("2022/1/11",20.83,19.95));
        jiaGeList.add(new JiaGe("2022/1/12",20.75,19.71));
        jiaGeList.add(new JiaGe("2022/1/13",20.44,19.12));
        jiaGeList.add(new JiaGe("2022/1/14",19.73,18.73));
        jiaGeList.add(new JiaGe("2022/1/17",18.99,18.15));
        jiaGeList.add(new JiaGe("2022/1/18",19.18,18.31));
        jiaGeList.add(new JiaGe("2022/1/19",19.11,18.18));
        jiaGeList.add(new JiaGe("2022/1/20",19.87,18.65));
        jiaGeList.add(new JiaGe("2022/1/21",19.41,18.7));
        jiaGeList.add(new JiaGe("2022/1/24",19.36,18.27));
        jiaGeList.add(new JiaGe("2022/1/25",18.89,18.22));
        jiaGeList.add(new JiaGe("2022/1/26",18.9,17.96));
        jiaGeList.add(new JiaGe("2022/1/27",19.39,18.51));
        jiaGeList.add(new JiaGe("2022/1/28",19.99,18.65));
        jiaGeList.add(new JiaGe("2022/2/7",20.21,18.68));
        jiaGeList.add(new JiaGe("2022/2/8",20.28,19.53));
        jiaGeList.add(new JiaGe("2022/2/9",21.18,19.95));
        jiaGeList.add(new JiaGe("2022/2/10",22.17,20.61));
        jiaGeList.add(new JiaGe("2022/2/11",21.6,20.9));
        jiaGeList.add(new JiaGe("2022/2/14",21.48,20.3));
        jiaGeList.add(new JiaGe("2022/2/15",20.89,20.3));
        jiaGeList.add(new JiaGe("2022/2/16",20.57,19.52));
        jiaGeList.add(new JiaGe("2022/2/17",19.95,19.05));
        jiaGeList.add(new JiaGe("2022/2/18",20.61,19.53));
        jiaGeList.add(new JiaGe("2022/2/21",20.74,19.9));
        jiaGeList.add(new JiaGe("2022/2/22",20.4,19.81));
        jiaGeList.add(new JiaGe("2022/2/23",20.72,19.38));
        jiaGeList.add(new JiaGe("2022/2/24",19.67,18.97));
        jiaGeList.add(new JiaGe("2022/2/25",19.43,18.62));
        jiaGeList.add(new JiaGe("2022/2/28",19.1,18.64));
        jiaGeList.add(new JiaGe("2022/3/1",20,18.85));
        jiaGeList.add(new JiaGe("2022/3/2",20.53,19.64));
        jiaGeList.add(new JiaGe("2022/3/3",20.1,19.47));
        jiaGeList.add(new JiaGe("2022/3/4",20.47,19.55));
        jiaGeList.add(new JiaGe("2022/3/7",20.89,20.02));
        jiaGeList.add(new JiaGe("2022/3/8",20.52,19.45));
        jiaGeList.add(new JiaGe("2022/3/9",20.13,18.5));
        jiaGeList.add(new JiaGe("2022/3/10",19.81,19.06));
        jiaGeList.add(new JiaGe("2022/3/11",20.03,18.54));
        jiaGeList.add(new JiaGe("2022/3/14",20.18,19.03));
        jiaGeList.add(new JiaGe("2022/3/15",19.35,18.2));
        jiaGeList.add(new JiaGe("2022/3/16",18.81,17.74));
        jiaGeList.add(new JiaGe("2022/3/17",19.83,18.43));
        jiaGeList.add(new JiaGe("2022/3/18",20.44,19.37));
        jiaGeList.add(new JiaGe("2022/3/21",20.84,20.06));
        jiaGeList.add(new JiaGe("2022/3/22",21.65,20.36));
        jiaGeList.add(new JiaGe("2022/3/23",21.47,20.88));
        jiaGeList.add(new JiaGe("2022/3/24",21.53,20.92));
        jiaGeList.add(new JiaGe("2022/3/25",21.83,21.05));
        jiaGeList.add(new JiaGe("2022/3/28",21.44,20.82));
        jiaGeList.add(new JiaGe("2022/3/29",21.94,20.67));
        jiaGeList.add(new JiaGe("2022/3/30",21.83,21.33));
        jiaGeList.add(new JiaGe("2022/3/31",21.95,21.06));
        jiaGeList.add(new JiaGe("2022/4/1",21.93,21.15));
        jiaGeList.add(new JiaGe("2022/4/6",22.06,21.36));
        jiaGeList.add(new JiaGe("2022/4/7",21.57,20.7));
        jiaGeList.add(new JiaGe("2022/4/8",20.96,20));
        jiaGeList.add(new JiaGe("2022/4/11",21.8,20.36));
        jiaGeList.add(new JiaGe("2022/4/12",21.85,21.22));
        jiaGeList.add(new JiaGe("2022/4/13",21.93,20.55));
        jiaGeList.add(new JiaGe("2022/4/14",20.97,19.85));
        jiaGeList.add(new JiaGe("2022/4/15",21.15,20.35));
        jiaGeList.add(new JiaGe("2022/4/18",22.14,20.6));
        jiaGeList.add(new JiaGe("2022/4/19",22.63,21.83));
        jiaGeList.add(new JiaGe("2022/4/20",22.05,21.15));
        jiaGeList.add(new JiaGe("2022/4/21",21.4,20));
        jiaGeList.add(new JiaGe("2022/4/22",20.45,19.64));
        jiaGeList.add(new JiaGe("2022/4/25",20.82,19.45));
        jiaGeList.add(new JiaGe("2022/4/26",19.83,19.25));
        jiaGeList.add(new JiaGe("2022/4/27",20.55,18.46));
        jiaGeList.add(new JiaGe("2022/4/28",20.35,19.27));
        jiaGeList.add(new JiaGe("2022/4/29",19.79,17.48));
        jiaGeList.add(new JiaGe("2022/5/5",18.29,17.61));
        jiaGeList.add(new JiaGe("2022/5/6",18.44,17.35));
        jiaGeList.add(new JiaGe("2022/5/9",18.93,17.97));
        jiaGeList.add(new JiaGe("2022/5/10",18.31,17.65));
        jiaGeList.add(new JiaGe("2022/5/11",18.41,17.61));
        jiaGeList.add(new JiaGe("2022/5/12",18.31,17.24));
        jiaGeList.add(new JiaGe("2022/5/13",17.72,16.95));
        jiaGeList.add(new JiaGe("2022/5/16",17.58,16.89));
        jiaGeList.add(new JiaGe("2022/5/17",17.72,17.15));
        jiaGeList.add(new JiaGe("2022/5/18",17.67,16.98));
        jiaGeList.add(new JiaGe("2022/5/19",17.45,16.85));
        jiaGeList.add(new JiaGe("2022/5/20",17.35,16.89));
        jiaGeList.add(new JiaGe("2022/5/23",17.82,17.08));
        jiaGeList.add(new JiaGe("2022/5/24",17.69,16.97));
        jiaGeList.add(new JiaGe("2022/5/25",17.55,16.65));
        jiaGeList.add(new JiaGe("2022/5/26",17.55,17.21));
        jiaGeList.add(new JiaGe("2022/5/27",17.82,17.11));
        jiaGeList.add(new JiaGe("2022/5/30",17.61,16.98));
        jiaGeList.add(new JiaGe("2022/5/31",17.6,17.05));
        jiaGeList.add(new JiaGe("2022/6/1",17.64,17.23));
        jiaGeList.add(new JiaGe("2022/6/2",17.49,17.15));
        jiaGeList.add(new JiaGe("2022/6/6",17.1,16.45));
        jiaGeList.add(new JiaGe("2022/6/7",17.37,16.67));
        jiaGeList.add(new JiaGe("2022/6/8",17.85,16.97));
        jiaGeList.add(new JiaGe("2022/6/9",18.44,17.63));
        jiaGeList.add(new JiaGe("2022/6/10",18.34,17.67));
        jiaGeList.add(new JiaGe("2022/6/13",18.84,17.86));
        jiaGeList.add(new JiaGe("2022/6/14",19.55,18.53));
        jiaGeList.add(new JiaGe("2022/6/15",19.51,18.88));
        jiaGeList.add(new JiaGe("2022/6/16",20.26,19.28));
        jiaGeList.add(new JiaGe("2022/6/17",20.11,19.5));
        jiaGeList.add(new JiaGe("2022/6/20",20.17,19.26));
        jiaGeList.add(new JiaGe("2022/6/21",20.51,19.67));
        jiaGeList.add(new JiaGe("2022/6/22",21.81,20.19));
        jiaGeList.add(new JiaGe("2022/6/23",21.61,20.91));
        jiaGeList.add(new JiaGe("2022/6/24",21.71,21.1));
        jiaGeList.add(new JiaGe("2022/6/27",22.94,21.72));
        jiaGeList.add(new JiaGe("2022/6/28",23.65,22.19));
        jiaGeList.add(new JiaGe("2022/6/29",22.68,21.11));
        jiaGeList.add(new JiaGe("2022/6/30",21.63,20.4));
        jiaGeList.add(new JiaGe("2022/7/1",21.6,20.53));
        jiaGeList.add(new JiaGe("2022/7/4",23.85,21.78));
        jiaGeList.add(new JiaGe("2022/7/5",23.96,23.15));
        jiaGeList.add(new JiaGe("2022/7/6",23.74,22.98));
        jiaGeList.add(new JiaGe("2022/7/7",24.45,23.25));
        jiaGeList.add(new JiaGe("2022/7/8",24.64,23.87));
        jiaGeList.add(new JiaGe("2022/7/11",25.7,24.5));
        jiaGeList.add(new JiaGe("2022/7/12",25.44,24.27));
        jiaGeList.add(new JiaGe("2022/7/13",24.77,23.89));
        jiaGeList.add(new JiaGe("2022/7/14",24.65,23.97));
        jiaGeList.add(new JiaGe("2022/7/15",24.44,23.45));
        jiaGeList.add(new JiaGe("2022/7/18",23.75,23.07));
        jiaGeList.add(new JiaGe("2022/7/19",23.89,22.91));
        jiaGeList.add(new JiaGe("2022/7/20",23.63,22.65));
        jiaGeList.add(new JiaGe("2022/7/21",23.85,22.78));
        jiaGeList.add(new JiaGe("2022/7/22",23.28,22.51));
        jiaGeList.add(new JiaGe("2022/7/25",23,22.39));
        jiaGeList.add(new JiaGe("2022/7/26",23.3,22.67));
        jiaGeList.add(new JiaGe("2022/7/27",23.04,22.38));
        jiaGeList.add(new JiaGe("2022/7/28",23.55,22.26));
        jiaGeList.add(new JiaGe("2022/7/29",23.78,22.21));
        jiaGeList.add(new JiaGe("2022/8/1",23.94,23.22));
        jiaGeList.add(new JiaGe("2022/8/2",23.85,22.85));
        jiaGeList.add(new JiaGe("2022/8/3",23.45,22.45));
        jiaGeList.add(new JiaGe("2022/8/4",22.79,22.33));
        jiaGeList.add(new JiaGe("2022/8/5",22.96,22.05));
        jiaGeList.add(new JiaGe("2022/8/8",23.25,22.24));
        jiaGeList.add(new JiaGe("2022/8/9",22.3,21.05));
        jiaGeList.add(new JiaGe("2022/8/10",21.98,21.28));
        jiaGeList.add(new JiaGe("2022/8/11",21.74,21.27));
        jiaGeList.add(new JiaGe("2022/8/12",21.74,21.35));
        jiaGeList.add(new JiaGe("2022/8/15",21.86,21.29));
        jiaGeList.add(new JiaGe("2022/8/16",22.58,21.37));
        jiaGeList.add(new JiaGe("2022/8/17",22.71,22.25));
        jiaGeList.add(new JiaGe("2022/8/18",22.53,21.84));
        jiaGeList.add(new JiaGe("2022/8/19",24.33,21.97));
        jiaGeList.add(new JiaGe("2022/8/22",24.5,23.66));
        jiaGeList.add(new JiaGe("2022/8/23",24.94,24.15));
        jiaGeList.add(new JiaGe("2022/8/24",25.02,23.93));
        jiaGeList.add(new JiaGe("2022/8/25",25,24.25));
        jiaGeList.add(new JiaGe("2022/8/26",24.88,24.09));
        jiaGeList.add(new JiaGe("2022/8/29",25.2,24.25));
        jiaGeList.add(new JiaGe("2022/8/30",24.45,23.55));
        jiaGeList.add(new JiaGe("2022/8/31",24.45,23.17));
        jiaGeList.add(new JiaGe("2022/9/1",23.94,23.18));
        jiaGeList.add(new JiaGe("2022/9/2",23.83,23.28));
        jiaGeList.add(new JiaGe("2022/9/5",24.05,23.07));
        jiaGeList.add(new JiaGe("2022/9/6",23.81,23.2));
        jiaGeList.add(new JiaGe("2022/9/7",23.51,23));
        jiaGeList.add(new JiaGe("2022/9/8",23.66,22.45));
        jiaGeList.add(new JiaGe("2022/9/9",22.9,21.89));
        jiaGeList.add(new JiaGe("2022/9/13",22.84,22.08));
        jiaGeList.add(new JiaGe("2022/9/14",22.51,22.04));
        jiaGeList.add(new JiaGe("2022/9/15",22.22,21.08));
        jiaGeList.add(new JiaGe("2022/9/16",21.57,21));
        jiaGeList.add(new JiaGe("2022/9/19",22.07,21.08));
        jiaGeList.add(new JiaGe("2022/9/20",21.5,20.65));
        jiaGeList.add(new JiaGe("2022/9/21",20.88,20.22));
        jiaGeList.add(new JiaGe("2022/9/22",20.47,19.86));
        jiaGeList.add(new JiaGe("2022/9/23",20.23,19.39));
        jiaGeList.add(new JiaGe("2022/9/26",20.14,19.2));
        jiaGeList.add(new JiaGe("2022/9/27",19.89,19.17));
        jiaGeList.add(new JiaGe("2022/9/28",20.23,19.56));
        jiaGeList.add(new JiaGe("2022/9/29",20.29,19.5));
        jiaGeList.add(new JiaGe("2022/9/30",20.37,19.56));
        jiaGeList.add(new JiaGe("2022/10/10",21.6,20.76));
        jiaGeList.add(new JiaGe("2022/10/11",21.83,20.56));
        jiaGeList.add(new JiaGe("2022/10/12",21.9,21.23));
        jiaGeList.add(new JiaGe("2022/10/13",22.42,21.58));
        jiaGeList.add(new JiaGe("2022/10/14",22.15,21.3));
        jiaGeList.add(new JiaGe("2022/10/17",21.54,20.89));
        jiaGeList.add(new JiaGe("2022/10/18",21.94,21.16));
        jiaGeList.add(new JiaGe("2022/10/19",21.74,20.35));
        jiaGeList.add(new JiaGe("2022/10/20",20.6,20.06));
        jiaGeList.add(new JiaGe("2022/10/21",20.7,20.08));
        jiaGeList.add(new JiaGe("2022/10/24",20.28,19.3));
        jiaGeList.add(new JiaGe("2022/10/25",19.68,18.65));
        jiaGeList.add(new JiaGe("2022/10/26",19.53,17.28));
        jiaGeList.add(new JiaGe("2022/10/27",18.11,17.36));
        jiaGeList.add(new JiaGe("2022/10/28",17.85,17.2));
        jiaGeList.add(new JiaGe("2022/10/31",17.55,17.15));
        jiaGeList.add(new JiaGe("2022/11/1",18.03,17.19));
        jiaGeList.add(new JiaGe("2022/11/2",18.37,17.69));
        jiaGeList.add(new JiaGe("2022/11/3",18.08,17.72));
        jiaGeList.add(new JiaGe("2022/11/4",18.44,17.91));
        jiaGeList.add(new JiaGe("2022/11/7",18.59,18.13));
        jiaGeList.add(new JiaGe("2022/11/8",18.63,17.81));
        jiaGeList.add(new JiaGe("2022/11/9",17.92,17.45));
        jiaGeList.add(new JiaGe("2022/11/10",17.81,17.38));
        jiaGeList.add(new JiaGe("2022/11/11",18.33,17.71));
        jiaGeList.add(new JiaGe("2022/11/14",17.98,17.35));
        jiaGeList.add(new JiaGe("2022/11/15",17.67,17.42));
        jiaGeList.add(new JiaGe("2022/11/16",17.7,17.22));
        jiaGeList.add(new JiaGe("2022/11/17",17.63,16.96));
        jiaGeList.add(new JiaGe("2022/11/18",17.5,16.89));
        jiaGeList.add(new JiaGe("2022/11/21",16.78,16.45));
        jiaGeList.add(new JiaGe("2022/11/22",17.16,16.7));
        jiaGeList.add(new JiaGe("2022/11/23",16.91,16.33));
        jiaGeList.add(new JiaGe("2022/11/24",16.9,16.53));
        jiaGeList.add(new JiaGe("2022/11/25",16.96,16.66));
        jiaGeList.add(new JiaGe("2022/11/28",16.87,16.45));
        jiaGeList.add(new JiaGe("2022/11/29",17.19,16.87));
        jiaGeList.add(new JiaGe("2022/11/30",17.65,17.12));
        jiaGeList.add(new JiaGe("2022/12/1",17.88,17.37));
        jiaGeList.add(new JiaGe("2022/12/2",18.24,17.43));
        jiaGeList.add(new JiaGe("2022/12/5",18.55,17.89));
        jiaGeList.add(new JiaGe("2022/12/6",18.59,18.15));
        jiaGeList.add(new JiaGe("2022/12/7",18.72,18.15));
        jiaGeList.add(new JiaGe("2022/12/8",18.07,17.63));
        jiaGeList.add(new JiaGe("2022/12/9",17.75,17.38));
        jiaGeList.add(new JiaGe("2022/12/12",17.88,17.34));
        jiaGeList.add(new JiaGe("2022/12/13",18.47,17.33));
        jiaGeList.add(new JiaGe("2022/12/14",18.53,18.05));
        jiaGeList.add(new JiaGe("2022/12/15",18.79,18.09));
        jiaGeList.add(new JiaGe("2022/12/16",18.4,18.02));
        jiaGeList.add(new JiaGe("2022/12/19",18.63,17.87));
        jiaGeList.add(new JiaGe("2022/12/20",18.34,17.85));
        jiaGeList.add(new JiaGe("2022/12/21",18.94,17.96));
        jiaGeList.add(new JiaGe("2022/12/22",19.33,18.63));
        jiaGeList.add(new JiaGe("2022/12/23",19.14,18.55));
        jiaGeList.add(new JiaGe("2022/12/26",19.05,18.73));
        jiaGeList.add(new JiaGe("2022/12/27",19.55,18.9));
        jiaGeList.add(new JiaGe("2022/12/28",19.42,18.76));
        jiaGeList.add(new JiaGe("2022/12/29",18.8,18.46));
        jiaGeList.add(new JiaGe("2022/12/30",19.3,18.55));
        jiaGeList.add(new JiaGe("2023/1/3",19.22,18.67));
        jiaGeList.add(new JiaGe("2023/1/4",18.84,18.5));
        jiaGeList.add(new JiaGe("2023/1/5",18.77,18.16));
        jiaGeList.add(new JiaGe("2023/1/6",18.55,18.17));
        jiaGeList.add(new JiaGe("2023/1/9",19.06,18.32));
        jiaGeList.add(new JiaGe("2023/1/10",18.99,18.4));
        jiaGeList.add(new JiaGe("2023/1/11",19.14,18.47));
        jiaGeList.add(new JiaGe("2023/1/12",19.32,18.8));
        jiaGeList.add(new JiaGe("2023/1/13",19.91,19));
        jiaGeList.add(new JiaGe("2023/1/16",20.91,19.75));
        jiaGeList.add(new JiaGe("2023/1/17",20.76,20.19));
        jiaGeList.add(new JiaGe("2023/1/18",20.39,19.87));
        jiaGeList.add(new JiaGe("2023/1/19",20.38,19.94));
        jiaGeList.add(new JiaGe("2023/1/20",20.45,20.01));
        jiaGeList.add(new JiaGe("2023/1/30",20.35,19.53));
        jiaGeList.add(new JiaGe("2023/1/31",20.23,19.59));
        jiaGeList.add(new JiaGe("2023/2/1",20.12,19.65));
        jiaGeList.add(new JiaGe("2023/2/2",20.55,19.86));
        jiaGeList.add(new JiaGe("2023/2/3",20.44,19.78));
        jiaGeList.add(new JiaGe("2023/2/6",20.47,19.56));
        jiaGeList.add(new JiaGe("2023/2/7",20.37,19.72));
        jiaGeList.add(new JiaGe("2023/2/8",19.81,19.46));
        jiaGeList.add(new JiaGe("2023/2/9",19.7,19.36));
        jiaGeList.add(new JiaGe("2023/2/10",19.7,19.42));
        jiaGeList.add(new JiaGe("2023/2/13",19.65,18.91));
        jiaGeList.add(new JiaGe("2023/2/14",19.59,19.04));
        jiaGeList.add(new JiaGe("2023/2/15",19.5,19.15));
        jiaGeList.add(new JiaGe("2023/2/16",19.39,18.78));
        jiaGeList.add(new JiaGe("2023/2/17",19.03,18.73));
        jiaGeList.add(new JiaGe("2023/2/20",19.19,18.5));
        jiaGeList.add(new JiaGe("2023/2/21",19.9,19.08));
        jiaGeList.add(new JiaGe("2023/2/22",19.98,19.47));
        jiaGeList.add(new JiaGe("2023/2/23",19.87,19.49));
        jiaGeList.add(new JiaGe("2023/2/24",19.81,19.41));
        jiaGeList.add(new JiaGe("2023/2/27",19.73,19.25));
        jiaGeList.add(new JiaGe("2023/2/28",19.63,19.14));
        jiaGeList.add(new JiaGe("2023/3/1",19.53,19.1));
        jiaGeList.add(new JiaGe("2023/3/2",19.31,18.96));
        jiaGeList.add(new JiaGe("2023/3/3",19.14,18.8));
        jiaGeList.add(new JiaGe("2023/3/6",18.96,18.64));
        jiaGeList.add(new JiaGe("2023/3/7",18.75,18.13));
        jiaGeList.add(new JiaGe("2023/3/8",18.15,17.86));
        jiaGeList.add(new JiaGe("2023/3/9",18.23,17.75));
        jiaGeList.add(new JiaGe("2023/3/10",17.75,17.46));
        jiaGeList.add(new JiaGe("2023/3/13",17.87,17.38));
        jiaGeList.add(new JiaGe("2023/3/14",19.15,17.8));
        jiaGeList.add(new JiaGe("2023/3/15",19.55,18.91));
        jiaGeList.add(new JiaGe("2023/3/16",19.2,18.1));
        jiaGeList.add(new JiaGe("2023/3/17",18.67,18.16));
        jiaGeList.add(new JiaGe("2023/3/20",18.63,18.13));
        jiaGeList.add(new JiaGe("2023/3/21",18.54,18.25));
        jiaGeList.add(new JiaGe("2023/3/22",18.87,18.33));
        jiaGeList.add(new JiaGe("2023/3/23",19.17,18.56));
        jiaGeList.add(new JiaGe("2023/3/24",19.16,18.7));
        jiaGeList.add(new JiaGe("2023/3/27",19.65,18.68));
        jiaGeList.add(new JiaGe("2023/3/28",20,19.33));
        jiaGeList.add(new JiaGe("2023/3/29",19.85,19.14));
        jiaGeList.add(new JiaGe("2023/3/30",19.52,19.17));
        jiaGeList.add(new JiaGe("2023/3/31",21.02,19.98));
        jiaGeList.add(new JiaGe("2023/4/3",20.95,19.86));
        jiaGeList.add(new JiaGe("2023/4/4",21.63,20.21));
        jiaGeList.add(new JiaGe("2023/4/6",22.15,21.23));
        jiaGeList.add(new JiaGe("2023/4/7",21.46,20.89));
        jiaGeList.add(new JiaGe("2023/4/10",21.49,20.65));
        jiaGeList.add(new JiaGe("2023/4/11",21.31,20.67));
        jiaGeList.add(new JiaGe("2023/4/12",21.1,20.39));
        jiaGeList.add(new JiaGe("2023/4/13",20.71,20.32));
        jiaGeList.add(new JiaGe("2023/4/14",20.52,20.05));
        jiaGeList.add(new JiaGe("2023/4/17",20.55,20.1));
        jiaGeList.add(new JiaGe("2023/4/18",20.7,20.21));
        jiaGeList.add(new JiaGe("2023/4/19",20.95,20.35));
        jiaGeList.add(new JiaGe("2023/4/20",21.1,20.45));
        jiaGeList.add(new JiaGe("2023/4/21",21.29,20.44));
        jiaGeList.add(new JiaGe("2023/4/24",20.71,19.82));
        jiaGeList.add(new JiaGe("2023/4/25",20.24,19.88));
        jiaGeList.add(new JiaGe("2023/4/26",20.33,19.56));
        jiaGeList.add(new JiaGe("2023/4/27",20.05,19.48));
        jiaGeList.add(new JiaGe("2023/4/28",19.75,19.06));
        jiaGeList.add(new JiaGe("2023/5/4",19.37,19.03));
        jiaGeList.add(new JiaGe("2023/5/5",19.1,18.5));
        jiaGeList.add(new JiaGe("2023/5/8",18.76,18.31));
        jiaGeList.add(new JiaGe("2023/5/9",18.8,18.34));
        jiaGeList.add(new JiaGe("2023/5/10",18.74,18.38));
        jiaGeList.add(new JiaGe("2023/5/11",18.84,18.35));
        jiaGeList.add(new JiaGe("2023/5/12",18.65,18.35));
        jiaGeList.add(new JiaGe("2023/5/15",18.39,17.92));
        jiaGeList.add(new JiaGe("2023/5/16",18.51,18.15));
        jiaGeList.add(new JiaGe("2023/5/17",18.53,18.17));
        jiaGeList.add(new JiaGe("2023/5/18",18.74,17.82));
        jiaGeList.add(new JiaGe("2023/5/19",17.92,17.48));
        jiaGeList.add(new JiaGe("2023/5/22",17.77,17.41));
        jiaGeList.add(new JiaGe("2023/5/23",17.72,17.39));
        jiaGeList.add(new JiaGe("2023/5/24",17.44,17.07));
        jiaGeList.add(new JiaGe("2023/5/25",17.18,16.68));
        jiaGeList.add(new JiaGe("2023/5/26",17.03,16.59));
        jiaGeList.add(new JiaGe("2023/5/29",16.95,16.44));
        jiaGeList.add(new JiaGe("2023/5/30",16.83,16.15));
        jiaGeList.add(new JiaGe("2023/5/31",16.58,16.3));
        jiaGeList.add(new JiaGe("2023/6/1",16.52,16.11));
        jiaGeList.add(new JiaGe("2023/6/2",16.49,16.12));
        jiaGeList.add(new JiaGe("2023/6/5",16.39,15.74));
        jiaGeList.add(new JiaGe("2023/6/6",16.05,15.77));
        jiaGeList.add(new JiaGe("2023/6/7",15.83,15.63));
        jiaGeList.add(new JiaGe("2023/6/8",16.94,15.71));
        jiaGeList.add(new JiaGe("2023/6/9",16.6,16.21));
        jiaGeList.add(new JiaGe("2023/6/12",16.77,16.21));
        jiaGeList.add(new JiaGe("2023/6/13",17,16.51));
        jiaGeList.add(new JiaGe("2023/6/14",16.73,16.3));
        jiaGeList.add(new JiaGe("2023/6/15",16.69,16.35));
        jiaGeList.add(new JiaGe("2023/6/16",17.01,16.56));
        jiaGeList.add(new JiaGe("2023/6/19",17.65,16.73));
        jiaGeList.add(new JiaGe("2023/6/20",17.41,16.97));
        jiaGeList.add(new JiaGe("2023/6/21",17.32,16.96));
        jiaGeList.add(new JiaGe("2023/6/26",17.23,16.7));
        jiaGeList.add(new JiaGe("2023/6/27",17.45,16.84));
        jiaGeList.add(new JiaGe("2023/6/28",17.45,17.13));
        jiaGeList.add(new JiaGe("2023/6/29",18.15,17.18));
        jiaGeList.add(new JiaGe("2023/6/30",18.26,17.65));
        jiaGeList.add(new JiaGe("2023/7/3",18.15,17.76));
        jiaGeList.add(new JiaGe("2023/7/4",18.14,17.67));
        jiaGeList.add(new JiaGe("2023/7/5",17.89,17.26));
        jiaGeList.add(new JiaGe("2023/7/6",18.06,17.28));
        jiaGeList.add(new JiaGe("2023/7/7",18.33,17.57));
        jiaGeList.add(new JiaGe("2023/7/10",18.52,17.96));
        jiaGeList.add(new JiaGe("2023/7/11",18.43,17.88));
        jiaGeList.add(new JiaGe("2023/7/12",18.4,17.89));
        jiaGeList.add(new JiaGe("2023/7/13",18.34,17.91));
        jiaGeList.add(new JiaGe("2023/7/14",18.4,18.17));
        jiaGeList.add(new JiaGe("2023/7/17",18.63,18.11));
        jiaGeList.add(new JiaGe("2023/7/18",18.84,18.3));
        jiaGeList.add(new JiaGe("2023/7/19",18.81,18.33));
        jiaGeList.add(new JiaGe("2023/7/20",18.8,18.49));
        jiaGeList.add(new JiaGe("2023/7/21",19.35,18.5));
        jiaGeList.add(new JiaGe("2023/7/24",19.42,18.4));
        jiaGeList.add(new JiaGe("2023/7/25",18.55,17.65));
        jiaGeList.add(new JiaGe("2023/7/26",17.87,17.53));
        jiaGeList.add(new JiaGe("2023/7/27",18.4,17.73));
        jiaGeList.add(new JiaGe("2023/7/28",18.4,17.97));
        jiaGeList.add(new JiaGe("2023/7/31",18.83,18.05));
        jiaGeList.add(new JiaGe("2023/8/1",18.83,18.05));
        jiaGeList.add(new JiaGe("2023/8/2",18.13,17.42));
        jiaGeList.add(new JiaGe("2023/8/3",17.77,17.43));
        jiaGeList.add(new JiaGe("2023/8/4",17.9,17.6));
        jiaGeList.add(new JiaGe("2023/8/7",17.99,17.72));
        jiaGeList.add(new JiaGe("2023/8/8",17.92,17.56));
        jiaGeList.add(new JiaGe("2023/8/9",17.69,17.26));
        jiaGeList.add(new JiaGe("2023/8/10",17.8,17.26));
        jiaGeList.add(new JiaGe("2023/8/11",17.68,17.02));
        jiaGeList.add(new JiaGe("2023/8/14",17.13,16.76));
        jiaGeList.add(new JiaGe("2023/8/15",17.3,16.85));
        jiaGeList.add(new JiaGe("2023/8/16",17.21,16.92));
        jiaGeList.add(new JiaGe("2023/8/17",17.18,16.76));
        jiaGeList.add(new JiaGe("2023/8/18",17.17,16.85));
        jiaGeList.add(new JiaGe("2023/8/21",16.88,16.62));
        jiaGeList.add(new JiaGe("2023/8/22",16.87,16.52));
        jiaGeList.add(new JiaGe("2023/8/23",16.88,16.58));
        jiaGeList.add(new JiaGe("2023/8/24",16.84,16.43));
        jiaGeList.add(new JiaGe("2023/8/25",17.28,16.82));
        jiaGeList.add(new JiaGe("2023/8/28",17.65,16.86));
        jiaGeList.add(new JiaGe("2023/8/29",17,16.72));
        jiaGeList.add(new JiaGe("2023/8/30",16.87,16.56));
        jiaGeList.add(new JiaGe("2023/8/31",16.78,16.37));
        jiaGeList.add(new JiaGe("2023/9/1",16.7,16.46));
        jiaGeList.add(new JiaGe("2023/9/4",16.75,16.51));
        jiaGeList.add(new JiaGe("2023/9/5",16.71,16.33));
        jiaGeList.add(new JiaGe("2023/9/6",16.56,16.4));
        jiaGeList.add(new JiaGe("2023/9/7",16.6,16.21));
        jiaGeList.add(new JiaGe("2023/9/8",16.39,16.13));
        jiaGeList.add(new JiaGe("2023/9/11",16.45,16.21));
        jiaGeList.add(new JiaGe("2023/9/12",17.08,16.3));
        jiaGeList.add(new JiaGe("2023/9/13",17.05,16.7));
        jiaGeList.add(new JiaGe("2023/9/14",16.9,16.35));
        jiaGeList.add(new JiaGe("2023/9/15",16.84,16.46));
        jiaGeList.add(new JiaGe("2023/9/18",16.71,16.33));
        jiaGeList.add(new JiaGe("2023/9/19",17.13,16.58));
        jiaGeList.add(new JiaGe("2023/9/20",17.35,16.72));
        jiaGeList.add(new JiaGe("2023/9/21",17.34,17.06));
        jiaGeList.add(new JiaGe("2023/9/22",17.19,16.88));
        jiaGeList.add(new JiaGe("2023/9/25",17.46,17.09));
        jiaGeList.add(new JiaGe("2023/9/26",17.49,17.19));
        jiaGeList.add(new JiaGe("2023/9/27",17.55,17.31));
        jiaGeList.add(new JiaGe("2023/9/28",17.52,16.97));
        jiaGeList.add(new JiaGe("2023/10/9",17.45,16.65));
        jiaGeList.add(new JiaGe("2023/10/10",17.82,17.28));
        jiaGeList.add(new JiaGe("2023/10/11",17.68,17.21));
        jiaGeList.add(new JiaGe("2023/10/12",17.57,17.21));
        jiaGeList.add(new JiaGe("2023/10/13",17.58,17.25));
        jiaGeList.add(new JiaGe("2023/10/16",17.38,17.11));
        jiaGeList.add(new JiaGe("2023/10/17",17.46,17.09));
        jiaGeList.add(new JiaGe("2023/10/18",17.74,17.38));
        jiaGeList.add(new JiaGe("2023/10/19",17.57,16.92));
        jiaGeList.add(new JiaGe("2023/10/20",16.98,16.67));
        jiaGeList.add(new JiaGe("2023/10/23",17.35,16.8));
        jiaGeList.add(new JiaGe("2023/10/24",17.55,16.98));
        jiaGeList.add(new JiaGe("2023/10/25",17.7,17.33));
        jiaGeList.add(new JiaGe("2023/10/26",18.14,17.4));
        jiaGeList.add(new JiaGe("2023/10/27",18.42,17.87));
        jiaGeList.add(new JiaGe("2023/10/30",18.8,18.17));
        jiaGeList.add(new JiaGe("2023/10/31",18.74,18.43));
        jiaGeList.add(new JiaGe("2023/11/1",18.6,18.36));
        jiaGeList.add(new JiaGe("2023/11/2",18.75,18.15));
        jiaGeList.add(new JiaGe("2023/11/3",18.43,18.17));
        jiaGeList.add(new JiaGe("2023/11/6",18.37,17.75));
        jiaGeList.add(new JiaGe("2023/11/7",18.22,17.85));
        jiaGeList.add(new JiaGe("2023/11/8",18.19,17.85));
        jiaGeList.add(new JiaGe("2023/11/9",18.28,17.96));
        jiaGeList.add(new JiaGe("2023/11/10",18.45,18.06));
        jiaGeList.add(new JiaGe("2023/11/13",18.54,18.1));
        jiaGeList.add(new JiaGe("2023/11/14",18.58,18.3));
        jiaGeList.add(new JiaGe("2023/11/15",18.45,18.09));
        jiaGeList.add(new JiaGe("2023/11/16",18.35,17.95));
        jiaGeList.add(new JiaGe("2023/11/17",18.13,17.9));
        jiaGeList.add(new JiaGe("2023/11/20",18.55,18.01));
        jiaGeList.add(new JiaGe("2023/11/21",18.8,18.35));
        jiaGeList.add(new JiaGe("2023/11/22",18.85,18.38));
        jiaGeList.add(new JiaGe("2023/11/23",18.87,18.63));
        jiaGeList.add(new JiaGe("2023/11/24",19.21,18.68));
        jiaGeList.add(new JiaGe("2023/11/27",19.35,18.85));
        jiaGeList.add(new JiaGe("2023/11/28",19.05,18.75));
        jiaGeList.add(new JiaGe("2023/11/29",18.96,18.75));
        jiaGeList.add(new JiaGe("2023/11/30",19.06,18.81));
        jiaGeList.add(new JiaGe("2023/12/1",19.07,18.8));
        jiaGeList.add(new JiaGe("2023/12/4",19.7,18.79));
        jiaGeList.add(new JiaGe("2023/12/5",19.71,19.2));
        jiaGeList.add(new JiaGe("2023/12/6",19.93,19.04));
        jiaGeList.add(new JiaGe("2023/12/7",20.06,19.38));
        jiaGeList.add(new JiaGe("2023/12/8",19.8,19.34));
        jiaGeList.add(new JiaGe("2023/12/11",19.79,19.05));
        jiaGeList.add(new JiaGe("2023/12/12",19.8,19.26));
        jiaGeList.add(new JiaGe("2023/12/13",19.53,19.05));
        jiaGeList.add(new JiaGe("2023/12/14",19.2,18.54));
        jiaGeList.add(new JiaGe("2023/12/15",19.31,18.83));
        jiaGeList.add(new JiaGe("2023/12/18",19.54,19.02));
        jiaGeList.add(new JiaGe("2023/12/19",19.32,18.85));
        jiaGeList.add(new JiaGe("2023/12/20",19.07,18.75));
        jiaGeList.add(new JiaGe("2023/12/21",18.88,18.55));
        jiaGeList.add(new JiaGe("2023/12/22",19.01,18.68));
        jiaGeList.add(new JiaGe("2023/12/25",19.06,18.3));
        jiaGeList.add(new JiaGe("2023/12/26",19.11,18.75));
        jiaGeList.add(new JiaGe("2023/12/27",19.74,18.77));
        jiaGeList.add(new JiaGe("2023/12/28",19.75,19.43));
        jiaGeList.add(new JiaGe("2023/12/29",19.87,19.35));
        jiaGeList.add(new JiaGe("2024/1/2",20.15,19.65));
        jiaGeList.add(new JiaGe("2024/1/3",20.18,19.65));
        jiaGeList.add(new JiaGe("2024/1/4",19.83,19.5));
        jiaGeList.add(new JiaGe("2024/1/5",19.75,19.39));
        jiaGeList.add(new JiaGe("2024/1/8",19.84,19.11));
        jiaGeList.add(new JiaGe("2024/1/9",19.35,19.05));
        jiaGeList.add(new JiaGe("2024/1/10",19.43,18.61));
        jiaGeList.add(new JiaGe("2024/1/11",19.72,18.87));
        jiaGeList.add(new JiaGe("2024/1/12",19.95,19.39));
        jiaGeList.add(new JiaGe("2024/1/15",19.85,19.08));
        jiaGeList.add(new JiaGe("2024/1/16",19.55,19.13));
        jiaGeList.add(new JiaGe("2024/1/17",19.65,19.03));
        jiaGeList.add(new JiaGe("2024/1/18",19.21,18.5));
        jiaGeList.add(new JiaGe("2024/1/19",19.14,18.7));
        jiaGeList.add(new JiaGe("2024/1/22",18.95,18.4));
        jiaGeList.add(new JiaGe("2024/1/23",18.7,18.28));
        jiaGeList.add(new JiaGe("2024/1/24",18.7,17.97));
        jiaGeList.add(new JiaGe("2024/1/25",18.57,18.28));
        jiaGeList.add(new JiaGe("2024/1/26",18.99,18.18));
        jiaGeList.add(new JiaGe("2024/1/29",19.3,18.6));
        jiaGeList.add(new JiaGe("2024/1/30",18.98,18.46));
        jiaGeList.add(new JiaGe("2024/1/31",18.82,18.2));
        jiaGeList.add(new JiaGe("2024/2/1",18.9,18.27));
        jiaGeList.add(new JiaGe("2024/2/2",19.32,18.73));
        jiaGeList.add(new JiaGe("2024/2/5",19.75,18.9));
        jiaGeList.add(new JiaGe("2024/2/6",19.73,18.55));
        jiaGeList.add(new JiaGe("2024/2/7",19.54,18.58));
        jiaGeList.add(new JiaGe("2024/2/8",19.7,18.85));
        jiaGeList.add(new JiaGe("2024/2/19",19.4,18.66));
        jiaGeList.add(new JiaGe("2024/2/20",19.53,18.75));
        jiaGeList.add(new JiaGe("2024/2/21",19.49,19.23));
        jiaGeList.add(new JiaGe("2024/2/22",19.43,19.17));
        jiaGeList.add(new JiaGe("2024/2/23",19.51,19.08));
        jiaGeList.add(new JiaGe("2024/2/26",19.45,18.95));
        jiaGeList.add(new JiaGe("2024/2/27",19.65,19.08));
        jiaGeList.add(new JiaGe("2024/2/28",19.55,19.07));
        jiaGeList.add(new JiaGe("2024/2/29",19.24,18.76));
        jiaGeList.add(new JiaGe("2024/3/1",18.9,18.35));
        jiaGeList.add(new JiaGe("2024/3/4",18.73,18.38));
        jiaGeList.add(new JiaGe("2024/3/5",19.23,18.56));
        jiaGeList.add(new JiaGe("2024/3/6",19.24,18.99));
        jiaGeList.add(new JiaGe("2024/3/7",19.46,19));
        jiaGeList.add(new JiaGe("2024/3/8",19.26,18.68));
        jiaGeList.add(new JiaGe("2024/3/11",18.92,18.54));
        jiaGeList.add(new JiaGe("2024/3/12",18.88,18.53));
        jiaGeList.add(new JiaGe("2024/3/13",18.78,18.27));
        jiaGeList.add(new JiaGe("2024/3/14",18.43,17.87));
        jiaGeList.add(new JiaGe("2024/3/15",18.01,17.56));
        jiaGeList.add(new JiaGe("2024/3/18",17.68,17.27));
        jiaGeList.add(new JiaGe("2024/3/19",18.45,17.53));
        jiaGeList.add(new JiaGe("2024/3/20",18.92,18.16));
        jiaGeList.add(new JiaGe("2024/3/21",19.24,18.51));
        jiaGeList.add(new JiaGe("2024/3/22",19.16,18.61));
        jiaGeList.add(new JiaGe("2024/3/25",19.05,18.71));
        jiaGeList.add(new JiaGe("2024/3/26",19.1,18.71));
        jiaGeList.add(new JiaGe("2024/3/27",18.94,18.37));
        jiaGeList.add(new JiaGe("2024/3/28",18.65,18.21));
        jiaGeList.add(new JiaGe("2024/3/29",18.75,18.1));
        jiaGeList.add(new JiaGe("2024/4/1",19.2,18.61));
        jiaGeList.add(new JiaGe("2024/4/2",19.24,18.78));
        jiaGeList.add(new JiaGe("2024/4/3",19.45,18.78));
        jiaGeList.add(new JiaGe("2024/4/8",19.6,19.23));
        jiaGeList.add(new JiaGe("2024/4/9",19.45,18.64));
        jiaGeList.add(new JiaGe("2024/4/10",19.05,18.55));
        jiaGeList.add(new JiaGe("2024/4/11",18.67,18.1));
        jiaGeList.add(new JiaGe("2024/4/12",18.49,17.83));
        jiaGeList.add(new JiaGe("2024/4/15",18.13,17.68));
        jiaGeList.add(new JiaGe("2024/4/16",18.79,17.96));
        jiaGeList.add(new JiaGe("2024/4/17",18.55,18.02));
        jiaGeList.add(new JiaGe("2024/4/18",18.62,18.03));
        jiaGeList.add(new JiaGe("2024/4/19",18.3,17.96));
        jiaGeList.add(new JiaGe("2024/4/22",19.13,18.02));
        jiaGeList.add(new JiaGe("2024/4/23",19.05,18.69));
        jiaGeList.add(new JiaGe("2024/4/24",19.01,18.69));
        jiaGeList.add(new JiaGe("2024/4/25",19.03,18.78));
        jiaGeList.add(new JiaGe("2024/4/26",19.35,18.82));
        jiaGeList.add(new JiaGe("2024/4/29",19.11,18.57));
        jiaGeList.add(new JiaGe("2024/4/30",19.24,18.46));
        jiaGeList.add(new JiaGe("2024/5/6",19.1,18.62));
        jiaGeList.add(new JiaGe("2024/5/7",19.02,18.51));
        jiaGeList.add(new JiaGe("2024/5/8",19.83,18.68));
        jiaGeList.add(new JiaGe("2024/5/9",20.13,19.47));
        jiaGeList.add(new JiaGe("2024/5/10",20.66,20));
        jiaGeList.add(new JiaGe("2024/5/13",21.21,20.52));
        jiaGeList.add(new JiaGe("2024/5/14",21.34,20.91));
        jiaGeList.add(new JiaGe("2024/5/15",21.3,20.55));
        jiaGeList.add(new JiaGe("2024/5/16",21.12,20.36));
        jiaGeList.add(new JiaGe("2024/5/17",21.39,20.7));
        jiaGeList.add(new JiaGe("2024/5/20",22.18,21.21));
        jiaGeList.add(new JiaGe("2024/5/21",22.15,21.58));
        jiaGeList.add(new JiaGe("2024/5/22",22,21.4));
        jiaGeList.add(new JiaGe("2024/5/23",22.04,21.35));
        jiaGeList.add(new JiaGe("2024/5/24",22.04,21.4));
        jiaGeList.add(new JiaGe("2024/5/27",22.25,21.42));
        jiaGeList.add(new JiaGe("2024/5/28",22.2,21.58));
        jiaGeList.add(new JiaGe("2024/5/29",21.87,21.44));
        jiaGeList.add(new JiaGe("2024/5/30",21.93,21.25));
        jiaGeList.add(new JiaGe("2024/5/31",21.42,21.05));
        jiaGeList.add(new JiaGe("2024/6/3",21.72,21.2));
        jiaGeList.add(new JiaGe("2024/6/4",21.83,21.31));
        jiaGeList.add(new JiaGe("2024/6/5",21.7,21.39));
        jiaGeList.add(new JiaGe("2024/6/6",21.54,21.16));
        jiaGeList.add(new JiaGe("2024/6/7",21.82,21.17));
        jiaGeList.add(new JiaGe("2024/6/11",21.55,20.92));
        jiaGeList.add(new JiaGe("2024/6/12",21.35,21));
        jiaGeList.add(new JiaGe("2024/6/13",21,19.97));
        jiaGeList.add(new JiaGe("2024/6/14",20.59,19.77));
        jiaGeList.add(new JiaGe("2024/6/17",20.34,19.98));
        jiaGeList.add(new JiaGe("2024/6/18",20.49,19.9));
        jiaGeList.add(new JiaGe("2024/6/19",19.96,19.68));
        jiaGeList.add(new JiaGe("2024/6/20",20.06,19.46));
        jiaGeList.add(new JiaGe("2024/6/21",20.13,19.79));
        jiaGeList.add(new JiaGe("2024/6/24",19.98,19.46));
        jiaGeList.add(new JiaGe("2024/6/25",19.8,19.39));
        jiaGeList.add(new JiaGe("2024/6/26",19.81,19.3));
        jiaGeList.add(new JiaGe("2024/6/27",20.07,19.61));
        jiaGeList.add(new JiaGe("2024/6/28",19.92,19.43));
        jiaGeList.add(new JiaGe("2024/7/1",20.43,19.71));
        jiaGeList.add(new JiaGe("2024/7/2",20.42,19.8));
        jiaGeList.add(new JiaGe("2024/7/3",20.03,19.68));
        jiaGeList.add(new JiaGe("2024/7/4",19.86,19.55));
        jiaGeList.add(new JiaGe("2024/7/5",19.74,19.11));
        jiaGeList.add(new JiaGe("2024/7/8",20.15,19.52));
        jiaGeList.add(new JiaGe("2024/7/9",20.07,19.08));
        jiaGeList.add(new JiaGe("2024/7/10",19.57,18.97));
        jiaGeList.add(new JiaGe("2024/7/11",19.71,19.26));
        jiaGeList.add(new JiaGe("2024/7/12",19.44,18.98));
        jiaGeList.add(new JiaGe("2024/7/15",20.29,19.26));
        jiaGeList.add(new JiaGe("2024/7/16",20.55,19.83));
        jiaGeList.add(new JiaGe("2024/7/17",20.46,19.97));
        jiaGeList.add(new JiaGe("2024/7/18",20.52,20.18));
        jiaGeList.add(new JiaGe("2024/7/19",20.64,20.25));
        jiaGeList.add(new JiaGe("2024/7/22",20.56,20.21));
        jiaGeList.add(new JiaGe("2024/7/23",20.62,19.85));
        jiaGeList.add(new JiaGe("2024/7/24",19.96,19.28));
        jiaGeList.add(new JiaGe("2024/7/25",19.68,19.4));
        jiaGeList.add(new JiaGe("2024/7/26",19.63,19.23));
        jiaGeList.add(new JiaGe("2024/7/29",19.51,18.86));
        jiaGeList.add(new JiaGe("2024/7/30",19.58,18.92));
        jiaGeList.add(new JiaGe("2024/7/31",19.87,19.22));
        jiaGeList.add(new JiaGe("2024/8/1",20.14,19.57));
        jiaGeList.add(new JiaGe("2024/8/2",20.34,19.56));
        jiaGeList.add(new JiaGe("2024/8/5",20.49,19.97));
        jiaGeList.add(new JiaGe("2024/8/6",20.26,19.8));
        jiaGeList.add(new JiaGe("2024/8/7",20.14,19.57));
        jiaGeList.add(new JiaGe("2024/8/8",19.95,19.61));
        jiaGeList.add(new JiaGe("2024/8/9",19.92,19.6));
        jiaGeList.add(new JiaGe("2024/8/12",20.04,19.43));
        jiaGeList.add(new JiaGe("2024/8/13",20,19.5));
        jiaGeList.add(new JiaGe("2024/8/14",19.94,19.59));
        jiaGeList.add(new JiaGe("2024/8/15",19.95,19.59));
        jiaGeList.add(new JiaGe("2024/8/16",19.74,19.29));
        jiaGeList.add(new JiaGe("2024/8/19",19.35,18.91));
        jiaGeList.add(new JiaGe("2024/8/20",19.01,18.1));
        jiaGeList.add(new JiaGe("2024/8/21",18.25,17.59));
        jiaGeList.add(new JiaGe("2024/8/22",17.76,17.03));
        jiaGeList.add(new JiaGe("2024/8/23",17.47,17.13));
        jiaGeList.add(new JiaGe("2024/8/26",17.42,17.01));
        jiaGeList.add(new JiaGe("2024/8/27",17.32,16.42));
        jiaGeList.add(new JiaGe("2024/8/28",16.74,16.33));
        jiaGeList.add(new JiaGe("2024/8/29",16.85,16.6));
        jiaGeList.add(new JiaGe("2024/8/30",17.14,16.41));
        jiaGeList.add(new JiaGe("2024/9/2",17.07,16.73));
        jiaGeList.add(new JiaGe("2024/9/3",17.05,16.66));
        jiaGeList.add(new JiaGe("2024/9/4",16.9,16.46));
        jiaGeList.add(new JiaGe("2024/9/5",16.71,16.41));
        jiaGeList.add(new JiaGe("2024/9/6",16.77,16.45));
        jiaGeList.add(new JiaGe("2024/9/9",16.48,16.23));
        jiaGeList.add(new JiaGe("2024/9/10",16.58,16.17));
        jiaGeList.add(new JiaGe("2024/9/11",16.24,16.03));
        jiaGeList.add(new JiaGe("2024/9/12",16.15,15.75));
        jiaGeList.add(new JiaGe("2024/9/13",15.85,15.58));
        jiaGeList.add(new JiaGe("2024/9/18",15.68,14.97));
        jiaGeList.add(new JiaGe("2024/9/19",15.79,15.17));
        jiaGeList.add(new JiaGe("2024/9/20",15.62,15.16));
        jiaGeList.add(new JiaGe("2024/9/23",15.75,15.34));
        jiaGeList.add(new JiaGe("2024/9/24",16.31,15.41));
        jiaGeList.add(new JiaGe("2024/9/25",16.77,16.34));
        jiaGeList.add(new JiaGe("2024/9/26",17.24,16.23));
        jiaGeList.add(new JiaGe("2024/9/27",18.45,17.56));
        jiaGeList.add(new JiaGe("2024/9/30",20.15,18.05));
        jiaGeList.add(new JiaGe("2024/10/8",23.75,19.57));
        jiaGeList.add(new JiaGe("2024/10/9",20.38,18.52));
        jiaGeList.add(new JiaGe("2024/10/10",20.1,19.25));
        jiaGeList.add(new JiaGe("2024/10/11",19.02,18.31));
        jiaGeList.add(new JiaGe("2024/10/14",18.75,18.28));
        jiaGeList.add(new JiaGe("2024/10/15",18.53,17.92));
        jiaGeList.add(new JiaGe("2024/10/16",18.13,17.6));
        jiaGeList.add(new JiaGe("2024/10/17",18,17.46));
        jiaGeList.add(new JiaGe("2024/10/18",18.54,17.34));
        jiaGeList.add(new JiaGe("2024/10/21",18.99,18.2));
        jiaGeList.add(new JiaGe("2024/10/22",19.17,18.38));
        jiaGeList.add(new JiaGe("2024/10/23",19.8,19.07));
        jiaGeList.add(new JiaGe("2024/10/24",19.06,18.45));
        jiaGeList.add(new JiaGe("2024/10/25",18.9,18.4));
        jiaGeList.add(new JiaGe("2024/10/28",18.93,18.43));
        jiaGeList.add(new JiaGe("2024/10/29",18.9,18.48));
        jiaGeList.add(new JiaGe("2024/10/30",18.84,18.36));
        jiaGeList.add(new JiaGe("2024/10/31",18.7,18.38));
        jiaGeList.add(new JiaGe("2024/11/1",18.71,18.38));
        jiaGeList.add(new JiaGe("2024/11/4",18.54,18.25));
        jiaGeList.add(new JiaGe("2024/11/5",18.83,18.35));
        jiaGeList.add(new JiaGe("2024/11/6",18.99,18.6));
        jiaGeList.add(new JiaGe("2024/11/7",19.5,18.7));
        jiaGeList.add(new JiaGe("2024/11/8",19.76,19.03));
        jiaGeList.add(new JiaGe("2024/11/11",18.94,18.66));
        jiaGeList.add(new JiaGe("2024/11/12",19.29,18.71));
        jiaGeList.add(new JiaGe("2024/11/13",18.88,18.51));
        jiaGeList.add(new JiaGe("2024/11/14",18.7,18.15));
        jiaGeList.add(new JiaGe("2024/11/15",18.27,17.88));
        jiaGeList.add(new JiaGe("2024/11/18",18.22,17.77));
        jiaGeList.add(new JiaGe("2024/11/19",18.05,17.63));
        jiaGeList.add(new JiaGe("2024/11/20",18,17.71));
        jiaGeList.add(new JiaGe("2024/11/21",18.12,17.83));
        jiaGeList.add(new JiaGe("2024/11/22",18.05,17.25));
        jiaGeList.add(new JiaGe("2024/11/25",17.44,17.03));
        jiaGeList.add(new JiaGe("2024/11/26",17.37,17.1));
        jiaGeList.add(new JiaGe("2024/11/27",17.34,16.96));
        jiaGeList.add(new JiaGe("2024/11/28",17.36,17.11));
        jiaGeList.add(new JiaGe("2024/11/29",17.48,17.11));
        jiaGeList.add(new JiaGe("2024/12/2",17.53,17.21));
        jiaGeList.add(new JiaGe("2024/12/3",17.79,17.28));
        jiaGeList.add(new JiaGe("2024/12/4",17.49,17.16));
        jiaGeList.add(new JiaGe("2024/12/5",17.23,17.06));
        jiaGeList.add(new JiaGe("2024/12/6",17.37,17.12));
        jiaGeList.add(new JiaGe("2024/12/9",17.28,16.95));
        jiaGeList.add(new JiaGe("2024/12/10",17.56,17.2));
        jiaGeList.add(new JiaGe("2024/12/11",17.52,17.18));
        jiaGeList.add(new JiaGe("2024/12/12",17.77,17.38));
        jiaGeList.add(new JiaGe("2024/12/13",17.65,17.37));
        jiaGeList.add(new JiaGe("2024/12/16",17.67,17.12));
        jiaGeList.add(new JiaGe("2024/12/17",17.24,17.03));
        jiaGeList.add(new JiaGe("2024/12/18",17.16,16.98));
        jiaGeList.add(new JiaGe("2024/12/19",16.99,16.71));
        jiaGeList.add(new JiaGe("2024/12/20",17.04,16.81));
        jiaGeList.add(new JiaGe("2024/12/23",16.97,16.66));
        jiaGeList.add(new JiaGe("2024/12/24",17.04,16.78));
        jiaGeList.add(new JiaGe("2024/12/25",16.94,16.72));
        jiaGeList.add(new JiaGe("2024/12/26",16.91,16.73));
        jiaGeList.add(new JiaGe("2024/12/27",17.07,16.65));
        jiaGeList.add(new JiaGe("2024/12/30",16.98,16.7));
        jiaGeList.add(new JiaGe("2024/12/31",16.83,16.43));
        jiaGeList.add(new JiaGe("2025/1/2",16.74,16.1));
        jiaGeList.add(new JiaGe("2025/1/3",16.31,15.98));
        jiaGeList.add(new JiaGe("2025/1/6",16.59,16.19));
        jiaGeList.add(new JiaGe("2025/1/7",16.54,16.21));
        jiaGeList.add(new JiaGe("2025/1/8",16.51,16.12));
        jiaGeList.add(new JiaGe("2025/1/9",16.4,15.91));
        jiaGeList.add(new JiaGe("2025/1/10",16,15.69));
        jiaGeList.add(new JiaGe("2025/1/13",15.81,15.55));
        jiaGeList.add(new JiaGe("2025/1/14",16.18,15.76));
        jiaGeList.add(new JiaGe("2025/1/15",16.14,15.91));
        jiaGeList.add(new JiaGe("2025/1/16",16.16,15.78));
        jiaGeList.add(new JiaGe("2025/1/17",16.04,15.76));
        jiaGeList.add(new JiaGe("2025/1/20",16.25,15.91));
        jiaGeList.add(new JiaGe("2025/1/21",16.18,15.92));
        jiaGeList.add(new JiaGe("2025/1/22",15.97,15.8));
        jiaGeList.add(new JiaGe("2025/1/23",16.15,15.84));
        jiaGeList.add(new JiaGe("2025/1/24",16.01,15.67));
        jiaGeList.add(new JiaGe("2025/1/27",16.31,16.06));
        jiaGeList.add(new JiaGe("2025/2/5",16.22,16.03));
        jiaGeList.add(new JiaGe("2025/2/6",16.18,15.94));
        jiaGeList.add(new JiaGe("2025/2/7",16.32,15.99));
        jiaGeList.add(new JiaGe("2025/2/10",16.23,16.07));
        jiaGeList.add(new JiaGe("2025/2/11",16.16,15.88));
        jiaGeList.add(new JiaGe("2025/2/12",15.98,15.81));
        jiaGeList.add(new JiaGe("2025/2/13",16.49,15.92));
        jiaGeList.add(new JiaGe("2025/2/14",16.43,16.18));
        jiaGeList.add(new JiaGe("2025/2/17",16.36,16.09));
        jiaGeList.add(new JiaGe("2025/2/18",16.35,16.01));
        jiaGeList.add(new JiaGe("2025/2/19",16.3,15.92));
        jiaGeList.add(new JiaGe("2025/2/20",16.25,16.09));
        jiaGeList.add(new JiaGe("2025/2/21",16.71,16.2));
        jiaGeList.add(new JiaGe("2025/2/24",16.7,16.37));
        jiaGeList.add(new JiaGe("2025/2/25",16.44,16.22));
        jiaGeList.add(new JiaGe("2025/2/26",16.36,16.13));
        jiaGeList.add(new JiaGe("2025/2/27",16.41,16.22));
        jiaGeList.add(new JiaGe("2025/2/28",16.46,16.09));
        jiaGeList.add(new JiaGe("2025/3/3",16.53,16.18));
        jiaGeList.add(new JiaGe("2025/3/4",16.52,16.22));
        jiaGeList.add(new JiaGe("2025/3/5",16.67,16.28));
        jiaGeList.add(new JiaGe("2025/3/6",16.38,16.19));
        jiaGeList.add(new JiaGe("2025/3/7",16.42,16.21));
        jiaGeList.add(new JiaGe("2025/3/10",16.68,16.42));
        jiaGeList.add(new JiaGe("2025/3/11",16.86,16.31));
        jiaGeList.add(new JiaGe("2025/3/12",16.77,16.57));
        jiaGeList.add(new JiaGe("2025/3/13",16.7,16.55));
        jiaGeList.add(new JiaGe("2025/3/14",16.8,16.58));


        return jiaGeList;
    }



}

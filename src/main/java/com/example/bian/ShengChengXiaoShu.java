package com.example.bian;

import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.market.ExchangeInfoEntry;
import com.example.bian.client.model.market.ExchangeInformation;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by adimn on 2021/8/16. 生成小数
 */
public class  ShengChengXiaoShu{



    public static void main(String[] args) throws IOException ,Exception{

        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        //方法2
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        ExchangeInformation exchangeInformation = syncRequestClient.getExchangeInformation();
        List<ExchangeInfoEntry> exchangeInfoEntryList = exchangeInformation.getSymbols();
        System.out.println();
        int i= 0;
        for(ExchangeInfoEntry entry : exchangeInfoEntryList) {

            List<List<Map<String, String>>> filters = entry.getFilters();
            String tick = "1";
            for (List<Map<String, String>> filter : filters) {
                for (Map<String, String> map : filter) {
                    if (map.keySet().contains("tickSize")) {
//                        System.out.println(entry.getSymbol());
                        if ("1".equals(map.get("tickSize"))) {
                            tick = "1";
                        } else {
                            tick = map.get("tickSize").split("1")[0] + "1";
                        }
                        break;
                    }
                }
            }
            //"IOTXUSDT": "5,0",
            String out = "";
            if (i == exchangeInfoEntryList.size() - 1) {
                out = "\"" + entry.getSymbol() + "\"" + ":\"" + get(tick) + "," + entry.getQuantityPrecision() + "\"";
            } else {
                out = "\"" + entry.getSymbol() + "\"" + ":\"" + get(tick) + "," + entry.getQuantityPrecision() + "\",";
            }
            System.out.println(out);
            i++;
        }


        //方法1
        /*
         * 1、登录币安，打开合约的行情页面 ，在network搜索 userLeverage 这个关键字的rest接口，会返回所有的币种信息
         * 2、记事本里替换成 list 需要的格式
         * 3、生成结果是这样的"tickSize":"0.00001","minQty":"1","quoteAsset":"US。在excel中使用【数据-分割】的功能根据分隔符【：】进行分割，得到价格的小数位和个数的小数位（生成的结果在记事本里替换成 list1 需要的格式）
         * 4、excel拼接(=A1&B1&C1)
         *
         *
         * */
        //1、手动搜索有多少币种

//        输出小数();

//        计算小数位();


        System.out.println();

    }

    public static void 输出小数() throws  Exception{


        List<String> list = new ArrayList<String>() {
            private static final long serialVersionUID = -7193171626884170372L;

            {
                add("BTCUSDT");
                add("ETHUSDT");
                add("BCHUSDT");
                add("XRPUSDT");
                add("EOSUSDT");
                add("LTCUSDT");
                add("TRXUSDT");
                add("ETCUSDT");
                add("LINKUSDT");
                add("XLMUSDT");
                add("ADAUSDT");
                add("XMRUSDT");
                add("DASHUSDT");
                add("ZECUSDT");
                add("XTZUSDT");
                add("BNBUSDT");
                add("ATOMUSDT");
                add("ONTUSDT");
                add("IOTAUSDT");
                add("BATUSDT");
                add("VETUSDT");
                add("NEOUSDT");
                add("QTUMUSDT");
                add("IOSTUSDT");
                add("THETAUSDT");
                add("ALGOUSDT");
                add("ZILUSDT");
                add("KNCUSDT");
                add("ZRXUSDT");
                add("COMPUSDT");
                add("OMGUSDT");
                add("DOGEUSDT");
                add("SXPUSDT");
                add("LENDUSDT");
                add("KAVAUSDT");
                add("BANDUSDT");
                add("RLCUSDT");
                add("WAVESUSDT");
                add("MKRUSDT");
                add("SNXUSDT");
                add("DOTUSDT");
                add("DEFIUSDT");
                add("YFIUSDT");
                add("BALUSDT");
                add("CRVUSDT");
                add("TRBUSDT");
                add("YFIIUSDT");
                add("RUNEUSDT");
                add("SUSHIUSDT");
                add("SRMUSDT");
                add("BZRXUSDT");
                add("EGLDUSDT");
                add("SOLUSDT");
                add("ICXUSDT");
                add("STORJUSDT");
                add("BLZUSDT");
                add("UNIUSDT");
                add("AVAXUSDT");
                add("FTMUSDT");
                add("HNTUSDT");
                add("ENJUSDT");
                add("FLMUSDT");
                add("TOMOUSDT");
                add("RENUSDT");
                add("KSMUSDT");
                add("NEARUSDT");
                add("AAVEUSDT");
                add("FILUSDT");
                add("RSRUSDT");
                add("LRCUSDT");
                add("MATICUSDT");
                add("OCEANUSDT");
                add("CVCUSDT");
                add("BELUSDT");
                add("CTKUSDT");
                add("AXSUSDT");
                add("ALPHAUSDT");
                add("ZENUSDT");
                add("SKLUSDT");
                add("GRTUSDT");
                add("1INCHUSDT");
                add("AKROUSDT");
                add("DOTECOUSDT");
                add("CHZUSDT");
                add("SANDUSDT");
                add("ANKRUSDT");
                add("LUNAUSDT");
                add("BTSUSDT");
                add("LITUSDT");
                add("UNFIUSDT");
                add("DODOUSDT");
                add("REEFUSDT");
                add("RVNUSDT");
                add("SFPUSDT");
                add("XEMUSDT");
                add("BTCSTUSDT");
                add("COTIUSDT");
                add("CHRUSDT");
                add("MANAUSDT");
                add("ALICEUSDT");
                add("HBARUSDT");
                add("ONEUSDT");
                add("LINAUSDT");
                add("STMXUSDT");
                add("DENTUSDT");
                add("CELRUSDT");
                add("HOTUSDT");
                add("MTLUSDT");
                add("OGNUSDT");
                add("BTTUSDT");
                add("NKNUSDT");
                add("SCUSDT");
                add("DGBUSDT");
                add("1000SHIBUSDT");
                add("BAKEUSDT");
                add("GTCUSDT");
                add("BTCDOMUSDT");
                add("KEEPUSDT");
                add("TLMUSDT");
                add("IOTXUSDT");
                add("AUDIOUSDT");
                add("RAYUSDT");
                add("C98USDT");
                add("MASKUSDT");
                add("ATAUSDT");
                add("DYDXUSDT");
                add("1000XECUSDT");
                add("GALAUSDT");
                add("CELOUSDT");
                add("ARUSDT");
                add("KLAYUSDT");
                add("ARPAUSDT");
                add("NUUSDT");
                add("CTSIUSDT");
                add("LPTUSDT");
                add("ENSUSDT");
                add("PEOPLEUSDT");
                add("ANTUSDT");
                add("ROSEUSDT");
                add("DUSKUSDT");
                add("1000BTTCUSDT");
                add("FLOWUSDT");
                add("IMXUSDT");
                add("API3USDT");
                add("ANCUSDT");
                add("GMTUSDT");
                add("APEUSDT");
                add("BNXUSDT");
                add("WOOUSDT");
                add("FTTUSDT");
                add("JASMYUSDT");
                add("DARUSDT");
                add("GALUSDT");
                add("OPUSDT");
                add("INJUSDT");
                add("STGUSDT");
                add("FOOTBALLUSDT");
                add("SPELLUSDT");
                add("1000LUNCUSDT");
                add("LUNA2USDT");
                add("LDOUSDT");
                add("CVXUSDT");
                add("ICPUSDT");
                add("APTUSDT");
                add("QNTUSDT");
                add("BLUEBIRDUSDT");



            }
        };
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        for (String symbol : list) {
            String url = "https://www.binance.com/zh-CN/futures/" + symbol;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);

            Response response = call.execute();
            String result = response.body().string();
//            System.out.println(result.contains("minQty"));
//            System.out.println(result.indexOf("\"minQty\":\""));
            if(result.contains("minQty")){
//                System.out.println(result.substring(result.indexOf("\"minQty\":\""), result.indexOf("\"minQty\":\"")+16));
//                System.out.println(result.substring(result.indexOf("\"tickSize\":\""), result.indexOf("\"tickSize\":\"")+50));
                String re = result.substring(result.indexOf("\"tickSize\":\""), result.indexOf("\"tickSize\":\"")+50);
//                String re = "\"tickSize\":\"0.10\",\"minQty\":\"0.001\",\"quoteAsset\":\"U";
                // "tickSize":"0.10","minQty":"0.001","quoteAsset":"U
                String tick = re.split(",")[0].split("1")[0] + "1";
                String qty = re.split(",")[1].split("1")[0] + "1";

                String out ="\"" + symbol + "\"" + ":\"" + get(tick) + "," + get(qty) + "\",";
                //"IOTXUSDT": "5,0",
                System.out.println(out);
            }else {
                System.out.println("没查到");
            }

        }
    }

    public static String get(String tick){
        int dcimalDigits = 0;
        int indexOf = tick.indexOf(".");
        if (indexOf > 0) {
            dcimalDigits = tick.length() - 1 - indexOf;
        }
        return dcimalDigits + "";
    }
        //计算小数位
    public static void JiSuanXiaoShuWei(){
        List<String> list1 = new ArrayList<String>() {
            private static final long serialVersionUID = -1099621126369222980L;

            {

                add("1");
                add("1");
                add("0.001");
                add("1");
                add("0.001");
                add("0.1");
                add("0.001");
                add("1");
                add("1");
                add("0.1");
                add("1");
                add("0.1");
                add("1");
                add("0.001");
                add("0.1");
                add("1");
                add("0.1");
                add("0.01");
                add("1");
                add("0.01");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("0.001");
                add("0.1");
                add("0.001");
                add("0.1");
                add("0.001");
                add("1");
                add("0.1");
                add("0.01");
                add("0.1");
                add("0.01");
                add("1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("1");
                add("0.01");
                add("0.1");
                add("1");
                add("0.1");
                add("0.1");
                add("1");
                add("1");
                add("0.1");
                add("0.001");
                add("0.1");
                add("1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.001");
                add("0.1");
                add("0.1");
                add("0.001");
                add("0.001");
                add("0.1");
                add("0.1");
                add("0.1");
                add("0.001");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("1");
                add("0.1");
                add("0.1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("1");
                add("1");
                add("1");
                add("0.001");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("0.1");
                add("0.1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("1");
                add("0.01");
                add("1");
                add("0.1");
                add("0.001");
                add("0.001");
                add("1");
                add("1");
                add("0.01");
                add("1");
                add("0.1");


            }};

        for (String s : list1) {
            int dcimalDigits = 0;
            int indexOf = s.indexOf(".");
            if (indexOf > 0) {
                dcimalDigits = s.length() - 1 - indexOf;
            }
            System.out.println(dcimalDigits);
        }
    }
}

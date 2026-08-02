package com.example.bian.genDan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.*;

public class GenDanZhengQian {

    /*
    * https://www.binancezh.info/bapi/futures/v1/private/future/copy-trade/copy-portfolio/create
    *定额
    * {
    "agreeShare": true,
    "leverageMode": "FOLLOW_LEAD",
    "marginMode": "FOLLOW_LEAD",
    "investAmount": 11,
    "costPerOrder": 10,
    "copyModel": "FIXED_AMT",
    "investAsset": "USDT",
    "leadPortfolioId": "3899711535133359873"
}
* 定比
{
    "agreeShare": true,
    "leverageMode": "FOLLOW_LEAD",
    "marginMode": "FOLLOW_LEAD",
    "investAmount": 10,
    "copyModel": "FIXED_RATIO",
    "costPerOrder": 0,
    "investAsset": "USDT",
    "leadPortfolioId": "3899711535133359873"
}
*
* {
    "code": "000000",
    "message": null,
    "messageDetail": null,
    "data": {
        "copyPortfolioId": "3901214144038380545",
        "status": "ACTIVE"
    },
    "success": true
}
    *
    * */

    public static void main(String[] args) throws InterruptedException {

        // 对https也开启代理
        if(PrivateConfig.daiLi.equals("1")) {
            PrivateConfig.printLog("开代理");
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "10819");
        }

        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.before(args[0], args[1]);

//        PrivateConfig.getJGXsw();
//        PrivateConfig.xsw(true);


        GenDanZhengQian genDan = new GenDanZhengQian();
        genDan.method("a100111");

    }


    public static void method(String time) throws InterruptedException {

        if (time.contains("a")) {
            int tian = Integer.parseInt(time.replace("a", ""));
            if (System.currentTimeMillis() > (1733903329000L + 86400000L * tian)) {
                System.out.println("license过期了");
                return;
            }
        } else {
            if (System.currentTimeMillis() > (Long.parseLong("1733907902000") + 86400000L)) {
                System.out.println("license过期了");
                return;
            }
        }
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 1, 1,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());
        boolean chengGong = false;
        int i = 60;
        while (true) {
            String s = getOrder(threadPoolExecutor);
            i++;
            if (StringUtils.isNotBlank(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                if ("000000".equals(jsonObject.getString("code"))) {
                    chengGong = true;
                    break;
                } else {
                    if (i > 60) {
                        i = 0;
                        System.out.println(jsonObject.getString("message"));
                    }
                }
                Thread.sleep(Long.parseLong(PrivateConfig.shiJian));
            }
        }


        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");
        Thread.sleep(1000 * 60);
        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");
        Thread.sleep(1000 * 60);
        System.out.println(PrivateConfig.zhengQian_shui + "：跟单成功了");
        T5.sendMe(PrivateConfig.zhengQian_shui + "：跟单成功了");

    }

    public static String getOrder(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        String s1;
        if(PrivateConfig.genDan_position.equals("1")){
            s1 = "{\"investAmount\": "
                    + PrivateConfig.zhengQian_investAmount + ",\"costPerOrder\":"
                    + PrivateConfig.zhengQian_costPerOrder + ",\"inviteCode\":\""
                    + PrivateConfig.zhengQian_inviteCode + "\",\"copyModel\":\""
                    + PrivateConfig.zhengQian_copyModel + "\",\"leadPortfolioId\": \""
                    + PrivateConfig.zhengQian_portfolioId + "\",\"enableAutoInvest\": false,\n" +
                    "\t\"leverageMode\": \"FOLLOW_LEAD\",\n" +
                    "\t\"marginMode\": \"FOLLOW_LEAD\",\n" +
                    "\t\"investAsset\": \"USDT\",\n" +
                    "\t\"mirrorMode\": \"NONE\",\n" +
                    "\t\"totalStopLossUsdt\": 0,\n" +
                    "\t\"followNewSymbols\": true,\n" +
                    "\t\"canTradFi\": false,\n" +
                    "\t\"slippage\": 0.001,\n" +
                    "\"availableSymbols\":[\"BTCUSDT\",\"ETHUSDT\",\"BCHUSDT\",\"XRPUSDT\",\"LTCUSDT\",\"TRXUSDT\",\"ETCUSDT\",\"LINKUSDT\",\"XLMUSDT\",\"ADAUSDT\",\"XMRUSDT\",\"DASHUSDT\",\"ZECUSDT\",\"XTZUSDT\",\"BNBUSDT\",\"ATOMUSDT\",\"ONTUSDT\",\"IOTAUSDT\",\"BATUSDT\",\"VETUSDT\",\"NEOUSDT\",\"QTUMUSDT\",\"IOSTUSDT\",\"THETAUSDT\",\"ALGOUSDT\",\"ZILUSDT\",\"KNCUSDT\",\"ZRXUSDT\",\"COMPUSDT\",\"DOGEUSDT\",\"KAVAUSDT\",\"BANDUSDT\",\"RLCUSDT\",\"SNXUSDT\",\"DOTUSDT\",\"YFIUSDT\",\"CRVUSDT\",\"TRBUSDT\",\"RUNEUSDT\",\"SUSHIUSDT\",\"EGLDUSDT\",\"SOLUSDT\",\"ICXUSDT\",\"STORJUSDT\",\"UNIUSDT\",\"AVAXUSDT\",\"ENJUSDT\",\"KSMUSDT\",\"NEARUSDT\",\"AAVEUSDT\",\"FILUSDT\",\"RSRUSDT\",\"BELUSDT\",\"AXSUSDT\",\"ZENUSDT\",\"SKLUSDT\",\"GRTUSDT\",\"1INCHUSDT\",\"CHZUSDT\",\"SANDUSDT\",\"ANKRUSDT\",\"RVNUSDT\",\"SFPUSDT\",\"COTIUSDT\",\"CHRUSDT\",\"MANAUSDT\",\"ALICEUSDT\",\"HBARUSDT\",\"ONEUSDT\",\"CELRUSDT\",\"HOTUSDT\",\"MTLUSDT\",\"OGNUSDT\",\"1000SHIBUSDT\",\"GTCUSDT\",\"BTCDOMUSDT\",\"IOTXUSDT\",\"C98USDT\",\"MASKUSDT\",\"DYDXUSDT\",\"1000XECUSDT\",\"GALAUSDT\",\"CELOUSDT\",\"ARUSDT\",\"ARPAUSDT\",\"CTSIUSDT\",\"LPTUSDT\",\"ENSUSDT\",\"PEOPLEUSDT\",\"ROSEUSDT\",\"DUSKUSDT\",\"FLOWUSDT\",\"IMXUSDT\",\"API3USDT\",\"GMTUSDT\",\"APEUSDT\",\"WOOUSDT\",\"JASMYUSDT\",\"OPUSDT\",\"INJUSDT\",\"STGUSDT\",\"SPELLUSDT\",\"1000LUNCUSDT\",\"LUNA2USDT\",\"LDOUSDT\",\"ICPUSDT\",\"APTUSDT\",\"QNTUSDT\",\"FETUSDT\",\"MAGICUSDT\",\"TUSDT\",\"MINAUSDT\",\"ASTRUSDT\",\"GMXUSDT\",\"CFXUSDT\",\"STXUSDT\",\"ACHUSDT\",\"SSVUSDT\",\"CKBUSDT\",\"LQTYUSDT\",\"USDCUSDT\",\"IDUSDT\",\"ARBUSDT\",\"JOEUSDT\",\"TLMUSDT\",\"HFTUSDT\",\"XVSUSDT\",\"BLURUSDT\",\"EDUUSDT\",\"SUIUSDT\",\"1000PEPEUSDT\",\"1000FLOKIUSDT\",\"UMAUSDT\",\"NMRUSDT\",\"MAVUSDT\",\"XVGUSDT\",\"WLDUSDT\",\"PENDLEUSDT\",\"ARKMUSDT\",\"AGLDUSDT\",\"YGGUSDT\",\"DODOXUSDT\",\"BNTUSDT\",\"SEIUSDT\",\"CYBERUSDT\",\"ARKUSDT\",\"BICOUSDT\",\"BIGTIMEUSDT\",\"WAXPUSDT\",\"BSVUSDT\",\"RIFUSDT\",\"POLYXUSDT\",\"GASUSDT\",\"POWRUSDT\",\"TIAUSDT\",\"CAKEUSDT\",\"MEMEUSDT\",\"TWTUSDT\",\"ORDIUSDT\",\"STEEMUSDT\",\"ILVUSDT\",\"KASUSDT\",\"BEAMXUSDT\",\"1000BONKUSDT\",\"PYTHUSDT\",\"SUPERUSDT\",\"USTCUSDT\",\"ONGUSDT\",\"ETHWUSDT\",\"JTOUSDT\",\"1000SATSUSDT\",\"AUCTIONUSDT\",\"1000RATSUSDT\",\"ACEUSDT\",\"MOVRUSDT\",\"XAIUSDT\",\"WIFUSDT\",\"MANTAUSDT\",\"ONDOUSDT\",\"LSKUSDT\",\"ALTUSDT\",\"JUPUSDT\",\"ZETAUSDT\",\"RONINUSDT\",\"DYMUSDT\",\"PIXELUSDT\",\"STRKUSDT\",\"GLMUSDT\",\"PORTALUSDT\",\"AXLUSDT\",\"METISUSDT\",\"AEVOUSDT\",\"VANRYUSDT\",\"BOMEUSDT\",\"ETHFIUSDT\",\"ENAUSDT\",\"WUSDT\",\"TNSRUSDT\",\"SAGAUSDT\",\"TAOUSDT\",\"REZUSDT\",\"BBUSDT\",\"NOTUSDT\",\"TURBOUSDT\",\"IOUSDT\",\"ZKUSDT\",\"MEWUSDT\",\"LISTAUSDT\",\"ZROUSDT\",\"RENDERUSDT\",\"BANANAUSDT\",\"RAREUSDT\",\"GUSDT\",\"SYNUSDT\",\"BRETTUSDT\",\"POPCATUSDT\",\"SUNUSDT\",\"DOGSUSDT\",\"FLUXUSDT\",\"RPLUSDT\",\"POLUSDT\",\"1MBABYDOGEUSDT\",\"NEIROUSDT\",\"FIDAUSDT\",\"CATIUSDT\",\"HMSTRUSDT\",\"EIGENUSDT\",\"DIAUSDT\",\"1000CATUSDT\",\"SCRUSDT\",\"GOATUSDT\",\"MOODENGUSDT\",\"SAFEUSDT\",\"SANTOSUSDT\",\"COWUSDT\",\"CETUSUSDT\",\"1000000MOGUSDT\",\"GRASSUSDT\",\"DRIFTUSDT\",\"ACTUSDT\",\"PNUTUSDT\",\"BANUSDT\",\"AKTUSDT\",\"SCRTUSDT\",\"1000CHEEMSUSDT\",\"THEUSDT\",\"MORPHOUSDT\",\"CHILLGUYUSDT\",\"KAIAUSDT\",\"AEROUSDT\",\"ACXUSDT\",\"ORCAUSDT\",\"MOVEUSDT\",\"RAYSOLUSDT\",\"KOMAUSDT\",\"VIRTUALUSDT\",\"SPXUSDT\",\"MEUSDT\",\"AVAUSDT\",\"VELODROMEUSDT\",\"MOCAUSDT\",\"VANAUSDT\",\"PENGUUSDT\",\"LUMIAUSDT\",\"USUALUSDT\",\"AIXBTUSDT\",\"FARTCOINUSDT\",\"KMNOUSDT\",\"CGPTUSDT\",\"HIVEUSDT\",\"DEXEUSDT\",\"PHAUSDT\",\"GRIFFAINUSDT\",\"ZEREBROUSDT\",\"BIOUSDT\",\"COOKIEUSDT\",\"ALCHUSDT\",\"SWARMSUSDT\",\"SONICUSDT\",\"PROMUSDT\",\"SUSDT\",\"SOLVUSDT\",\"ARCUSDT\",\"AVAAIUSDT\",\"TRUMPUSDT\",\"MELANIAUSDT\",\"VTHOUSDT\",\"ANIMEUSDT\",\"PIPPINUSDT\",\"VVVUSDT\",\"BERAUSDT\",\"TSTUSDT\",\"LAYERUSDT\",\"HEIUSDT\",\"GPSUSDT\",\"SHELLUSDT\",\"KAITOUSDT\",\"REDUSDT\",\"VICUSDT\",\"EPICUSDT\",\"BMTUSDT\",\"MUBARAKUSDT\",\"FORMUSDT\",\"TUTUSDT\",\"BROCCOLI714USDT\",\"BROCCOLIF3BUSDT\",\"SIRENUSDT\",\"BANANAS31USDT\",\"BRUSDT\",\"PLUMEUSDT\",\"NILUSDT\",\"PARTIUSDT\",\"JELLYJELLYUSDT\",\"MAVIAUSDT\",\"PAXGUSDT\",\"WALUSDT\",\"GUNUSDT\",\"ATHUSDT\",\"BABYUSDT\",\"PROMPTUSDT\",\"STOUSDT\",\"FHEUSDT\",\"KERNELUSDT\",\"WCTUSDT\",\"INITUSDT\",\"AERGOUSDT\",\"BANKUSDT\",\"DEEPUSDT\",\"HYPERUSDT\",\"JSTUSDT\",\"SIGNUSDT\",\"PUNDIXUSDT\",\"CTKUSDT\",\"AIOTUSDT\",\"DOLOUSDT\",\"HAEDALUSDT\",\"SXTUSDT\",\"ASRUSDT\",\"ALPINEUSDT\",\"B2USDT\",\"SYRUPUSDT\",\"DOODUSDT\",\"OGUSDT\",\"SKYAIUSDT\",\"NXPCUSDT\",\"CVCUSDT\",\"AGTUSDT\",\"AWEUSDT\",\"BUSDT\",\"SOONUSDT\",\"HUMAUSDT\",\"AUSDT\",\"SOPHUSDT\",\"MERLUSDT\",\"HYPEUSDT\",\"1000000BOBUSDT\",\"LAUSDT\",\"HOMEUSDT\",\"RESOLVUSDT\",\"TAIKOUSDT\",\"SQDUSDT\",\"PUMPBTCUSDT\",\"SPKUSDT\",\"MYXUSDT\",\"FUSDT\",\"NEWTUSDT\",\"HUSDT\",\"SAHARAUSDT\",\"ICNTUSDT\",\"BULLAUSDT\",\"IDOLUSDT\",\"MUSDT\",\"PUMPUSDT\",\"CROSSUSDT\",\"AINUSDT\",\"CUSDT\",\"VELVETUSDT\",\"TACUSDT\",\"ERAUSDT\",\"TAUSDT\",\"CVXUSDT\",\"SLPUSDT\",\"ZORAUSDT\",\"TAGUSDT\",\"ESPORTSUSDT\",\"TREEUSDT\",\"PLAYUSDT\",\"NAORISUSDT\",\"TOWNSUSDT\",\"PROVEUSDT\",\"ALLUSDT\",\"INUSDT\",\"CARVUSDT\",\"AIOUSDT\",\"XNYUSDT\",\"USELESSUSDT\",\"SAPIENUSDT\",\"XPLUSDT\",\"WLFIUSDT\",\"SOMIUSDT\",\"BASUSDT\",\"BTRUSDT\",\"MITOUSDT\",\"HEMIUSDT\",\"LINEAUSDT\",\"QUSDT\",\"ARIAUSDT\",\"TAKEUSDT\",\"PTBUSDT\",\"OPENUSDT\",\"FLOCKUSDT\",\"SKYUSDT\",\"AVNTUSDT\",\"HOLOUSDT\",\"XPINUSDT\",\"UBUSDT\",\"ZKCUSDT\",\"TOSHIUSDT\",\"STBLUSDT\",\"0GUSDT\",\"BARDUSDT\",\"ASTERUSDT\",\"TRADOORUSDT\",\"BLESSUSDT\",\"FLUIDUSDT\",\"COAIUSDT\",\"HANAUSDT\",\"MIRAUSDT\",\"AKEUSDT\",\"ORDERUSDT\",\"LIGHTUSDT\",\"XANUSDT\",\"FFUSDT\",\"EDENUSDT\",\"NOMUSDT\",\"TRUTHUSDT\",\"2ZUSDT\",\"EVAAUSDT\",\"LYNUSDT\",\"KGENUSDT\",\"4USDT\",\"GIGGLEUSDT\",\"MONUSDT\",\"YBUSDT\",\"METUSDT\",\"EULUSDT\",\"ENSOUSDT\",\"CLOUSDT\",\"RECALLUSDT\",\"ZBTUSDT\",\"LABUSDT\",\"RIVERUSDT\",\"币安人生USDT\",\"BLUAIUSDT\",\"TURTLEUSDT\",\"APRUSDT\",\"ONUSDT\",\"KITEUSDT\",\"ATUSDT\",\"CCUSDT\",\"MMTUSDT\",\"TRUSTUSDT\",\"UAIUSDT\",\"FOLKSUSDT\",\"STABLEUSDT\",\"JCTUSDT\",\"ALLOUSDT\",\"CLANKERUSDT\",\"BEATUSDT\",\"PIEVERSEUSDT\",\"SENTUSDT\",\"IRYSUSDT\",\"POWERUSDT\",\"WETUSDT\",\"NIGHTUSDT\",\"USUSDT\",\"CYSUSDT\",\"RAVEUSDT\",\"ZKPUSDT\",\"GUAUSDT\",\"LITUSDT\",\"BREVUSDT\",\"COLLECTUSDT\",\"MAGMAUSDT\",\"ZAMAUSDT\",\"FOGOUSDT\",\"FRAXUSDT\",\"SPORTFUNUSDT\",\"AIAUSDT\",\"ACUUSDT\",\"我踏马来了USDT\",\"ELSAUSDT\",\"SKRUSDT\",\"SPACEUSDT\",\"FIGHTUSDT\",\"BIRBUSDT\",\"GWEIUSDT\",\"MEGAUSDT\",\"INXUSDT\",\"TRIAUSDT\",\"ESPUSDT\",\"AZTECUSDT\",\"OPNUSDT\",\"ROBOUSDT\",\"KATUSDT\",\"MANTRAUSDT\",\"龙虾USDT\",\"CFGUSDT\",\"EDGEUSDT\",\"BSBUSDT\",\"XAUTUSDT\",\"BASEDUSDT\",\"PRLUSDT\",\"GENIUSUSDT\",\"CHIPUSDT\",\"OPGUSDT\",\"AIGENSYNUSDT\",\"BILLUSDT\",\"PHAROSUSDT\",\"STARUSDT\",\"CTRUSDT\",\"SLXUSDT\",\"ZESTUSDT\",\"BTWUSDT\",\"REUSDT\",\"ARXUSDT\",\"OUSDT\",\"CAPUSDT\",\"GRAMUSDT\",\"DATAIPUSDT\"]}";
        }else {
            s1 = "{\"leverageMode\": \"FOLLOW_LEAD\",\"marginMode\": \"FOLLOW_LEAD\",\"investAmount\": "
                    + PrivateConfig.zhengQian_investAmount + ",\"costPerOrder\":"
                    + PrivateConfig.zhengQian_costPerOrder + ",\"inviteCode\":\""
                    + PrivateConfig.zhengQian_inviteCode + "\",\"copyModel\":\""
                    + PrivateConfig.zhengQian_copyModel + "\",\"investAsset\": \"USDT\",\"leadPortfolioId\": \""
                    + PrivateConfig.zhengQian_portfolioId + "\"}";
        }

        //订单的顺序：第一个就是最近的一个
        Callable callable = new Callable() {
            @Override
            public String call() throws Exception {
                if(PrivateConfig.genDan_position.equals("1")){
                    return Postman.sendPostPhone(s1);
                }else {
                    return Postman.sendPost("https://" + PrivateConfig.genDan_url + "/bapi/futures/v1/private/future/copy-trade/copy-portfolio/create",
                            s1, PrivateConfig.genDan_cookie, PrivateConfig.genDan_token);
                }
            }
        };

        int h = 0;
        while (true) {
            try {
                Future future = threadPoolExecutor.submit(callable);
                try {
                    String s = (String) (future.get(3, TimeUnit.SECONDS));
                    return s;
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } catch (Throwable t) {
                    t.printStackTrace();
                    Thread.sleep(3000);//前面有超时，歇2秒再跟
                } finally {
                    future.cancel(true);
                    h++;
                    if (h > 5) {
                        h = 0;
                        PrivateConfig.printLog("添加跟单失败");
                        T5.searchAll("添加跟单失败，有问题！");
                    }
                }
            } catch (Exception e1) {
            }
            Thread.sleep(2000);
        }
    }




}

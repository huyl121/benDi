package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class 分红 {


    public static void main(String[] args) throws InterruptedException, IOException {

        // 对https也开启代理
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

//        System.out.println(Binance.getSymbolRice("BTCUSDT", 1773217740089L, 1773217740089L));
        method();
        System.out.println("结束");
    }


    public static void method() throws InterruptedException, IOException {
        Map<String, BigDecimal> 客户Map = new HashMap<>();
        //以下是大资金返润
        /*客户Map.put("AtoZkkk", new BigDecimal("0.2"));
        客户Map.put("浆果", new BigDecimal("0.2"));
        客户Map.put("知行合一王道也 ", new BigDecimal("0.2"));
        客户Map.put("BTC星辰小迷弟", new BigDecimal("0.4"));
        客户Map.put("浮浮沉沉", new BigDecimal("0.4"));
        客户Map.put("T_user-faf8898", new BigDecimal("0.2"));
        客户Map.put("User-0ad9b", new BigDecimal("0.4"));
        客户Map.put("启飞888", new BigDecimal("0.2"));
        客户Map.put("曹氏宗親會大統領-小韭", new BigDecimal("0.2"));
        客户Map.put("城北美髯公-007", new BigDecimal("0.2"));
        客户Map.put("ETH阿莫", new BigDecimal("0.2"));
        客户Map.put("步云海", new BigDecimal("0.2"));
        客户Map.put("Btc可乐c", new BigDecimal("0.2"));
        客户Map.put("User-fad85", new BigDecimal("0.4"));
        客户Map.put("理性如磐石-方得长久安", new BigDecimal("0.2"));
        客户Map.put("EllenMeliana", new BigDecimal("0.2"));
        客户Map.put("三线彩虹", new BigDecimal("0.2"));
        客户Map.put("珍珠奶茶真好喝", new BigDecimal("0.3"));
        客户Map.put("-无敌老狗-", new BigDecimal("0.2"));
        客户Map.put("shuiyuai", new BigDecimal("0.5"));
        客户Map.put("Elfen0116", new BigDecimal("0.2"));
        客户Map.put("散步的哈士奇", new BigDecimal("0.2"));
        客户Map.put("kkkqx", new BigDecimal("0.2"));
        客户Map.put("十年长虹", new BigDecimal("0.2"));
        客户Map.put("User-d2f11", new BigDecimal("0.2"));
        客户Map.put("量化基金五号 - 银行 165x 125x", new BigDecimal("0.3"));



        //以下是推荐朋友
        客户Map.put("叫什么叫_", new BigDecimal("0.5"));*/

        //以下是大资金返润
        客户Map.put("理性如磐石-方得长久安", new BigDecimal("0.2"));
        客户Map.put("Sunpillar", new BigDecimal("0.2"));
        客户Map.put("九爺的狗", new BigDecimal("0.5"));//111


        //以下是plus转星辰
        客户Map.put("屋嘎龙男孩", new BigDecimal("1"));
        客户Map.put("10u上1w", new BigDecimal("1"));

        //以下是推荐朋友
        客户Map.put("User-f5f95", new BigDecimal("0.05"));

        //以下是中奖
        客户Map.put("0xNan", new BigDecimal("0.2"));
        客户Map.put("牛币爷爷", new BigDecimal("0.2"));



//        带单账号 d = new 带单账号("4892870556182209281", "智能操作公域-星辰", "30ac6ae715c98a8c980c127a47a2a97c", "bnc-uuid=1e34f244-8e54-4b6d-8b0e-4b823daffe5b; g_state={\"i_l\":0,\"i_ll\":1782462745233,\"i_b\":\"NJRUeUJlxpUHDePobVD/q2DeRC2mfqtLTj7Su7hi1YA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782462745233}; BNC_FV_KEY=332d7bde643fa6af9a59d9fb8abf8c7618c59f41; se_gd=hFbEwWloDAbAB4bdVCAtgZZDxXFMABVU1AeVfVU5lNQVQBVNWWMd1; se_gsd=VjQgL0ZVJQAgDQ0nJxM0CiUHUgYNBgIPVlRGVVZVW1NWJFNT1; r30t=1; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221193259156%22%2C%22first_id%22%3A%2219f030f4c5d113-03335728ce3464e-5c123e18-921600-19f030f4c5e6dd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwZjRjNWQxMTMtMDMzMzU3MjhjZTM0NjRlLTVjMTIzZTE4LTkyMTYwMC0xOWYwMzBmNGM1ZTZkZCIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjExOTMyNTkxNTYifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221193259156%22%7D%7D; OptanonAlertBoxClosed=2026-06-29T02:29:02.926Z; _gcl_au=1.1.658042552.1782700169; changeBasisTimeZone=; futures-layout=pro; userPreferredCurrency=USD_USD; aws-waf-token=3a228432-043a-48b2-adb9-120ef70193bd:AQoAbDAj3tAUAAAA:4zvyeScXe7kM07RqTdVqa6uBOPoqFB0KzS+7NtScIECfa872z3wdAcpJ+A5ByEx4LotXdGZnCuRfpg3V6Q9dlS4u28C3fz1lXTC7U8Zyx3F0RkuK6Eo1B8qVI1ggJrD9YfyaDoLImkET8IjxbWKrVwoOb/i2vr/Wjx+FTYlOaVAbyJ8yD8Kleqe9TMJEvtEzNbjLRSngcbFc+bzVk3EJKWSOmQpG4Ad4JX7tnLWdVvUMG5VlCUaLLaBqWMcV/eIMrq+0gzq5Oi+N; r20t=web.1193259156.FE9B44BB2B69F39D0A2F171D05B3ACE4; cr00=9A068EC1864941ACF73DAF38D81EC2A2; d1og=web.1193259156.DBE6058D33B8A5F2D6C24ABB9E780D28; r2o1=web.1193259156.781E0BC7D407D722CE5B55C7A9F462D2; f30l=web.1193259156.30DBE88ED7402E8D31DE2300129272A8; p20t=web.1193259156.8DE5DBD81505A1F9810EBCBC4311FE48; _gid=GA1.2.2110886933.1785721693; theme=dark; BNC_FV_KEY_T=101-ygs9i658JziFeS%2FghAE%2FErHouDzXVy%2FBwOw9%2FnJfJtlvvQwKSK0Z2N7l8zaD3JwvvyOvghxae7gbzkjYo49%2Bcw%3D%3D-mnR3ULfTQemUGodT2HFtPg%3D%3D-de; BNC_FV_KEY_EXPIRE=1785774219105; _gat_UA-162512367-1=1; _uetsid=69bc52608edd11f1adb0f90ba37c5878; _uetvid=5beb27a0736211f18710d1f1f63ed495; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A23%3A50+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=de433a9d-63c4-4c84-8063-1fc127a3ad3e&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782700144551&AwaitingReconsent=false&fclco=&lastConsentTs=1782700142&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1785752620$o25$g1$t1785752633$j47$l0$h0; _ga=GA1.1.531584620.1782700144");
        带单账号 d = new 带单账号("5075332206126952449", "智能操作那英私域-熬鹰资本", "c97ccc34e31fd0ebe175d8f637506b33", "bnc-uuid=ed561181-5803-41e9-953c-61baa37142ab; OptanonAlertBoxClosed=2026-06-26T08:06:54.053Z; BNC_FV_KEY=331c533e7f37a79eb29fa8f4dbfd0a1e3bdd2c27; se_gd=QsEVlVgAWEGVgUGIbDVIgZZV1F1UABXW1ZVZaV0JlNQVQUVNWWYK1; _gcl_au=1.1.133583968.1782461256; se_gsd=ewAnCidwISU3BiM0NAg1MxAgBQgWBgoYVFhFVVVQW1NWM1NT1; g_state={\"i_l\":0,\"i_ll\":1782464190912,\"i_b\":\"39IFH/gVZGareG0hRufhIYEOQIJl3Yjxd4i2ga0Eiik\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782464190912}; r30t=1; BNC-Location=CN; userPreferredCurrency=USD_USD; changeBasisTimeZone=; futures-layout=pro; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221052357632%22%2C%22first_id%22%3A%2219fb385ec3b34a-0e5e214a60801b-5c123e18-2073600-19fb385ec3c79%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmYjM4NWVjM2IzNGEtMGU1ZTIxNGE2MDgwMWItNWMxMjNlMTgtMjA3MzYwMC0xOWZiMzg1ZWMzYzc5IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTA1MjM1NzYzMiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221052357632%22%7D%7D; r20t=web.1052357632.AAE602B89BF89F0A41C798A4238AADBD; cr00=46D8D69A844D8BD66D8D791E548EA088; d1og=web.1052357632.D7F4E5592E5A5A5AF93BB4BB1C79EFC1; r2o1=web.1052357632.EEDF3F924137C3A807E25E22EB7C07B6; f30l=web.1052357632.5DF3F206CEB1509D89E35E00CADD497F; p20t=web.1052357632.5465B3AB7919EC94956E9BBC4842F008; _gid=GA1.2.1344251157.1785672162; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAeqZJQV4HAAAA:lMWze1K8fKMOJJNKSAVmJ5F0wNp7ZuK1CEBrHK0U2Ebbwg+5l0Ve2xOvIdn3x4QCDcDE3icZdJihf5QwXrgwKHDSJNmhiOn5G8BNMmHz6C9LhjddB78rOEXw1X4Z56ssnBsswgbQ9JrpV8p0tOy74CO+ZoQ1l5K3NyAD3OeNRTOEM2TIislX5sSd6GriiUaJwDiv97BGpeIHss0jmi81FxDrI5J1/4R8p/V6oMJ+4HMt8Kxby8T5zWsZT+fRiqlDt9FU6OrxwOIF; theme=dark; _gat_UA-162512367-1=1; BNC_FV_KEY_T=101-%2BudlL5OVfKJn5N938A8AJf7P%2BmjFBzmuXdujoIWC%2BwHaHW1sbuwds7oTL78a3MTobTc3QUXYKG9446VyCtiiyw%3D%3D-j4JcYVFzZCXnz%2BydiREu5g%3D%3D-e5; BNC_FV_KEY_EXPIRE=1785774409550; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A26%3A51+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=c9622c8a-404d-4ebc-bbab-8c446aca86db&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782461214&intType=1&crTime=1782461216002&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=4dcca1508edd11f1a6badb0d02978aae; _uetvid=18e21330713611f19f158961dc5c1393; _ga=GA1.2.1230848962.1782461244; _ga_3WP50LGEEC=GS2.1.s1785752809$o39$g1$t1785752815$j54$l0$h0");
//        带单账号 d = new 带单账号("5075315128924628993", "八爷公域", "2452121fe3c47f2ad3e7c017b3e392f8", "bnc-uuid=97bc9d5c-c9f8-4630-b5fe-5e81a7a529fb; BNC_FV_KEY=331a400171e2a798a1212c63b9c78b62df7535cd; OptanonAlertBoxClosed=2026-06-26T08:23:55.009Z; se_gd=RUBCBXA0aAbBw4BIWURZgZZUAChMJBQVlQNVaUUJlNQVQAlNWWwI1; se_gsd=ezU2GkZ1JQAjIFICISY7MDYnDAYVBwJSUlhAVVZQW1NWN1NT1; lang=zh-CN; BNC-Location=CN; userPreferredCurrency=USD_USD; g_state={\"i_l\":0,\"i_ll\":1785473145528,\"i_b\":\"3y5ap/ZRA1I6q+srDxD5rvT+tWSqTaOmY40PUuFSVN4\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1785473145528}; r20t=web.1207159852.08B1CAB67E1FFC1E15A4F8CF1DA6A156; r30t=1; cr00=0F53E517A791879319E256E8C9761EDA; d1og=web.1207159852.C55BEFCAC28FB63945621F9BFC9FBF43; r2o1=web.1207159852.E200569134DD376EF792DB5BC46A5B85; f30l=web.1207159852.98AB292C3E9B677EC3B8E8AF0758EF9F; logined=y; currentAccount=; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221207159852%22%2C%22first_id%22%3A%2219f0306e088348-0ba4d65a09f91e-5c123e18-921600-19f0306e089241%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwNmUwODgzNDgtMGJhNGQ2NWEwOWY5MWUtNWMxMjNlMTgtOTIxNjAwLTE5ZjAzMDZlMDg5MjQxIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTIwNzE1OTg1MiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221207159852%22%7D%7D; changeBasisTimeZone=; futures-layout=pro; _gcl_au=1.1.1663483023.1782462239.-.-.1785494571.1689156991.1785494572.1785494648; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAcAtKEv4AAAAA:82E5ajVA1t0MdEA9z/SntSgrt184clEZEKtG1TuZwwavH5fxggx/EG7xJ70UhqgMCHL9LFCfn0Mnsk+Fk9pL2OkMzY5IRf3rIzHscK50LPFoz3t4DAkquqBh8EMaSQobI7kAuswK87nW+RpFZ0lJo1lN9JrBK1eXeju1JEZHiJfIvSrf2p+iypsqQ3+Bh0r8HWcQ7u+ggUAnLjxA75XTBNl6fVbuVWDUFi1Wg5J7FGyDi39C98kK92Uk3+KjZtm1c1OL8D4OYh6z; theme=dark; p20t=web.1207159852.82054834BF1069CC0554FF352029B839; BNC_FV_KEY_T=101-4STetkFWoqVvUmFPHQ4SmJ4uDcM%2FzmVxVqXqI0KL7if2cjDe3RyU6o1xyIZ2KU9H%2FJi8deTzH7Lj7T%2FAQ3B8qQ%3D%3D-2YiOWuC0M7sUM5c%2FkTxIFg%3D%3D-f5; BNC_FV_KEY_EXPIRE=1785774751400; _gid=GA1.2.1016291729.1785753152; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A32%3A33+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=d5925164-bd69-416a-8e34-fafca79150e9&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782462236672&AwaitingReconsent=false&fclco=&lastConsentTs=1782462235&intType=1&geolocation=JP%3B13; _uetsid=a62daad08f2611f1aaf1abf604c15308; _uetvid=636f2310713811f198020903633126c6; _ga=GA1.2.1085919742.1782462241; _ga_3WP50LGEEC=GS2.1.s1785753152$o24$g1$t1785753164$j48$l0$h0");
//        带单账号 d = new 带单账号("5159567329928561664", "八爷私域", "2452121fe3c47f2ad3e7c017b3e392f8", "bnc-uuid=97bc9d5c-c9f8-4630-b5fe-5e81a7a529fb; BNC_FV_KEY=331a400171e2a798a1212c63b9c78b62df7535cd; OptanonAlertBoxClosed=2026-06-26T08:23:55.009Z; se_gd=RUBCBXA0aAbBw4BIWURZgZZUAChMJBQVlQNVaUUJlNQVQAlNWWwI1; se_gsd=ezU2GkZ1JQAjIFICISY7MDYnDAYVBwJSUlhAVVZQW1NWN1NT1; lang=zh-CN; BNC-Location=CN; userPreferredCurrency=USD_USD; g_state={\"i_l\":0,\"i_ll\":1785473145528,\"i_b\":\"3y5ap/ZRA1I6q+srDxD5rvT+tWSqTaOmY40PUuFSVN4\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1785473145528}; r20t=web.1207159852.08B1CAB67E1FFC1E15A4F8CF1DA6A156; r30t=1; cr00=0F53E517A791879319E256E8C9761EDA; d1og=web.1207159852.C55BEFCAC28FB63945621F9BFC9FBF43; r2o1=web.1207159852.E200569134DD376EF792DB5BC46A5B85; f30l=web.1207159852.98AB292C3E9B677EC3B8E8AF0758EF9F; logined=y; currentAccount=; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221207159852%22%2C%22first_id%22%3A%2219f0306e088348-0ba4d65a09f91e-5c123e18-921600-19f0306e089241%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwNmUwODgzNDgtMGJhNGQ2NWEwOWY5MWUtNWMxMjNlMTgtOTIxNjAwLTE5ZjAzMDZlMDg5MjQxIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTIwNzE1OTg1MiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221207159852%22%7D%7D; changeBasisTimeZone=; futures-layout=pro; _gcl_au=1.1.1663483023.1782462239.-.-.1785494571.1689156991.1785494572.1785494648; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAcAtKEv4AAAAA:82E5ajVA1t0MdEA9z/SntSgrt184clEZEKtG1TuZwwavH5fxggx/EG7xJ70UhqgMCHL9LFCfn0Mnsk+Fk9pL2OkMzY5IRf3rIzHscK50LPFoz3t4DAkquqBh8EMaSQobI7kAuswK87nW+RpFZ0lJo1lN9JrBK1eXeju1JEZHiJfIvSrf2p+iypsqQ3+Bh0r8HWcQ7u+ggUAnLjxA75XTBNl6fVbuVWDUFi1Wg5J7FGyDi39C98kK92Uk3+KjZtm1c1OL8D4OYh6z; theme=dark; p20t=web.1207159852.82054834BF1069CC0554FF352029B839; BNC_FV_KEY_T=101-4STetkFWoqVvUmFPHQ4SmJ4uDcM%2FzmVxVqXqI0KL7if2cjDe3RyU6o1xyIZ2KU9H%2FJi8deTzH7Lj7T%2FAQ3B8qQ%3D%3D-2YiOWuC0M7sUM5c%2FkTxIFg%3D%3D-f5; BNC_FV_KEY_EXPIRE=1785774751400; _gid=GA1.2.1016291729.1785753152; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A32%3A33+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=d5925164-bd69-416a-8e34-fafca79150e9&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782462236672&AwaitingReconsent=false&fclco=&lastConsentTs=1782462235&intType=1&geolocation=JP%3B13; _uetsid=a62daad08f2611f1aaf1abf604c15308; _uetvid=636f2310713811f198020903633126c6; _ga=GA1.2.1085919742.1782462241; _ga_3WP50LGEEC=GS2.1.s1785753152$o24$g1$t1785753164$j48$l0$h0");
//        带单账号 d = new 带单账号("5156877574234870784", "智能操作hk大叔私域", "ffe8b67403a405c220513f07eafa1be0", "bnc-uuid=3b0ff1f3-29d1-4cb3-8e6a-f02887ab6fdb; se_gd=lUaEgARJTDWDlwH5XE1cgZZAgFg4OBRVFIDRQV05lJUWwBlNWWAM1; se_gsd=BzomAQV/IDUlMyg3JwMiDjYECwNTAQFUVFREW1daW1JSVFNT1; BNC_FV_KEY=33a7ccf9d5d3ad249de5533c0d363d0a65d5f2a7; OptanonAlertBoxClosed=2026-07-03T13:04:29.934Z; lang=zh-CN; BNC-Location=CN; userPreferredCurrency=USD_USD; changeBasisTimeZone=; g_state={\"i_l\":0,\"i_ll\":1784811872648,\"i_b\":\"kcu0zDnzk4QrY0jf5z4xuLsAcM7Ls7cplM2Tx1lAIJ0\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1784811872648}; r30t=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221258638471%22%2C%22first_id%22%3A%2219f281490e1720-0f8f229ffdc0a68-5c123e18-2073600-19f281490e279d%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMjgxNDkwZTE3MjAtMGY4ZjIyOWZmZGMwYTY4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMjgxNDkwZTI3OWQiLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjU4NjM4NDcxIn0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221258638471%22%7D%7D; r20t=web.1258638471.70128FF6E0A30643E78EC1C2BE76A3E1; cr00=EC3A8A41AC7CE09BBCC613353F3A8D6C; d1og=web.1258638471.CEDD12F33EB46747F8DAEDEEB82C7A81; r2o1=web.1258638471.3465379BAD5440F7A343266B2CBE8778; f30l=web.1258638471.B0F268DFC4F12B922D923FDC7BB0BEE8; p20t=web.1258638471.0609CC6F6AD4C55249562049C4B80DB0; _gcl_au=1.1.1406294404.1783391091.-.-.1785494234.843074355.1785494234.1785494441; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAcSULzn8lAAAA:fkxWdZ+r+MC0EF/ZXdhtEqavseVOy9eN4DIAa9ljPnQI6ffmVge8oSXJtuciOnfcW46xhBfrCs/GYQFAbNYMbRSK207T/DNluafrj1SlHVfrzrGGXE4q+HkBH17/i4PBLAAksBF5ZcSiVVlXDT5juUJ2wEYMY+QRP4sUdb+KN3UUdInUhK3daRQCB1L9mZM9Q0CagtwZvJeBHuDuPHjFp2oCQnd/bJEdCJ+vUPjZZhG1JwOjhCClRuaPes44GV2VIzuWv3vmDfDs; _gid=GA1.2.1861801107.1785721795; BNC_FV_KEY_T=101-2%2FeJEAmYX6ibA%2FSPzOjqJVCSL%2BuL0dVrEckAg8%2BFCkFmvJtsWV4F3RvNcH8QQkg1UHrDzEu%2FREkty8Hf%2BdCv%2BQ%3D%3D-5mC0cMfwIAES%2BHongoUz%2Fw%3D%3D-15; BNC_FV_KEY_EXPIRE=1785771896148; futures-layout=pro; theme=dark; _gat_UA-162512367-1=1; _uetsid=b281e7208edd11f18a43af3324df1711; _uetvid=09db0b8079ab11f1ac45c5ea1e6cd3e0; OptanonConsent=consentId=83ac653b-b2fc-4a1f-8a64-c74acfae268a&datestamp=Mon+Aug+03+2026+18%3A28%3A42+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&interactionCount=1&isAnonUser=1&prevHadToken=0&crTime=1783083870735&isGpcEnabled=0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1783083869&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _ga_3WP50LGEEC=GS2.1.s1785752600$o10$g1$t1785752925$j38$l0$h0; _ga=GA1.1.1499286990.1783083871");
//        带单账号 d = new 带单账号("5108833703275227137", "智能操作银行公域-意钦私域", "72b02a4e7c50177768ea9a225c2f0094", "bnc-uuid=12e4f412-23c2-4a9d-8cbd-6f83dc2def5b; OptanonAlertBoxClosed=2026-06-26T04:48:10.475Z; se_gd=g4NUBTxkWHWBB0CMHAFYgZZXBDhlUBZVlYG5bUkdlNQVwFlNWWAD1; se_gsd=fyoiL0J0LDUjGQk0NyY3BVcrFhMIAgoKUV1KV11RW1NWI1NT1; _gcl_au=1.1.1872081329.1782449304; BNC_FV_KEY=339d7dc1f222af1f9c9b2207f79aafe7ceac974d; BNC-Location=CN; changeBasisTimeZone=; futures-layout=pro; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAmQoPVFcxAAAA:fCMH7hFDfetr0ThCmjunXyHzokgmuJ9EVo2u8Oct9t6IYiNBuz2tT1A11g8B9ByHBy4FoHwGnTZZONwa4W8jcARfX5+mVUqF9dDghiA57tEOAbjYY8G4ib91vgtB+O389ZTHyRHmF0l6ws3DJXxxs9vzs1DtO1F09/yUydhxt+K3FS7JlaqgtJj2QaNrLi1ZmrDedNsUPkftKwa9qMU6g2emSeHvmOUOSPn3a9nNGNx4U5jWLLA3U6V5dONoH9gVxc7wElydpXWp; g_state={\"i_l\":0,\"i_ll\":1785723735149,\"i_b\":\"h8WkT6bjO+RX+yfkdLEGMdmTIDq4h6vycScXR2cxDKc\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1785723735149}; _gid=GA1.2.1964714413.1785723746; r20t=web.1222792435.ADA8972600CF349FAFADC601377C0988; r30t=1; cr00=FAB75C04DDCFDD0FE9FA2DA886DE42FE; d1og=web.1222792435.84180CE38C97B0203551CF53BFA156B2; r2o1=web.1222792435.52AA5E32CADDBA798B7F48410FCC72AC; f30l=web.1222792435.DD6F0D4559AC7967B9999FB60C0CD741; lang=zh-CN; currentAccount=; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221222792435%22%2C%22first_id%22%3A%2219f0241b46c305-06912c094551f64-5c123e18-2073600-19f0241b46d397%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDI0MWI0NmMzMDUtMDY5MTJjMDk0NTUxZjY0LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDI0MWI0NmQzOTciLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjIyNzkyNDM1In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221222792435%22%7D%7D; theme=dark; p20t=web.1222792435.D23B11C98A27BB63FFFD29BDDCC196AD; BNC_FV_KEY_T=101-48Br5Os8%2F%2FLRWfiNiUYAEA%2BP8opk%2FcOkZWFa8Krfk4%2FvdV6sef1IIwv8XeFEq8AE06Qz8vkSHp%2BbZ0GjGBmETw%3D%3D-Yp1kXq71hPGJ%2B0%2BN1NszAw%3D%3D-c0; BNC_FV_KEY_EXPIRE=1785775012468; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A36%3A54+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=72368bb5-926d-4a2f-859e-ab49ecb71499&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782449290&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _ga_3WP50LGEEC=GS2.1.s1785753412$o19$g1$t1785753416$j56$l0$h0; _ga=GA1.1.1454322713.1782449305; _uetsid=34b799b08ee311f1969c3f4914e25cff; _uetvid=43ee9020711a11f1af4b19b7105ceaf3");



//        带单账号 d = new 带单账号("5004754260218990081", "智能操作私域-仓王", "", "");
//        带单账号 d = new 带单账号("5099835914695652352", "智能操作Kimi大林私域-不停梭迷弟", "", "");
//        带单账号 d = new 带单账号("5005577217312895745", "智能操作小周同学私域-小周同学", "5a758d457d2e52845fc6d5d06dc85c18", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAgDg8YioBAAAA:7mJBEMJo72Qn5k/4OZ7/ZQBu2tTDn1iwW3oOxNz7vHgd4WZqsog7OzBlKfj+lAVf0use09Lb2fAiRhn2YehZ4PX8Ej2YV0pCBRAkfmJJ82Q9ZL9IjPmdTGCuPDQPc9YkCpj74KTS7iOAgaS8fyJodAHDe/yJsyyT4MUF1qsVn74bZGSAApVioScWrhZh7gWwnDw=; bnc-uuid=00078672-a427-4d69-86df-877509c40a5b; se_gd=1IDGhABgATGFF0AdbBFYgZZUACQlRBXWlpcNfVEVlNQVQBFNWWVU1; se_gsd=VgAhK0J2MCUnIwUiJQwiUyohBVBUDwoWV19EVVBVW1NWI1NT1; BNC_FV_KEY=331e7153cc34a99c90c6ea8cd9ceabaa097a1ec3; OptanonAlertBoxClosed=2026-06-26T09:06:13.491Z; g_state={\"i_l\":0,\"i_ll\":1782464912502,\"i_b\":\"Ma6n9svbSRDYufumLIlJCmYkwRWXyeT/ZqYgoPmAxeA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782464912502}; _gcl_au=1.1.1561541001.1782464934; r20t=web.1229775668.0461C18FFF0DE853BDBCFEB5720AE7AD; r30t=1; cr00=7849545E1CD44E37128F1CC51D72217A; d1og=web.1229775668.7972DC6895511EC01C1457B870E67682; r2o1=web.1229775668.7F3388CF701492678F67C18202952C3B; f30l=web.1229775668.3592FF5489261440F283441F0B1544D7; currentAccount=; logined=y; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221229775668%22%2C%22first_id%22%3A%2219f032dd7ae1e-0fb0c6e6815e9b-5c123e18-2073600-19f032dd7af68d%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMyZGQ3YWUxZS0wZmIwYzZlNjgxNWU5Yi01YzEyM2UxOC0yMDczNjAwLTE5ZjAzMmRkN2FmNjhkIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTIyOTc3NTY2OCJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221229775668%22%7D%7D; theme=dark; p20t=web.1229775668.E81ED539A4E87C7B7D74F05932B45FE7; _gid=GA1.2.1667659499.1782702349; BNC_FV_KEY_T=101-c9ejCpArDYHzXzp4yiRdreOtRSRP0yonScXOwfmsh7Sz1%2FRQyjB%2FH6sY6Fnqsd79mQhaAFL5bgHLAH5Je26BxA%3D%3D-Cc%2FbL3cqdp3lQ4cCqWpfNQ%3D%3D-fe; BNC_FV_KEY_EXPIRE=1782723949257; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A05%3A57+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=b9fdc09a-12a2-4698-8d79-339fead07500&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&crTime=1782464777973&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782464773&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=7545b4a0736711f1a6f1f78642708aee; _uetvid=a92ddaf0713e11f18a1b87d7629238c1; _ga=GA1.2.456841213.1782464780; _ga_3WP50LGEEC=GS2.1.s1782702348$o2$g1$t1782702386$j22$l0$h0");
//        带单账号 d = new 带单账号("", "智能操作风火公域-风火", "", "");
//        带单账号 d = new 带单账号("5062549412865388289", "智能操作林小柔公域-林小柔", "a3832785029f7c0b1f4fd0341734e7fd", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAqzggIrEHAAAA:QU6aqEGmJZaToJpAz9KrvHptUBcfBepiIZnYjAa8vKotiAYRpojpP3caabCxjQ3uIRahzgeNxxai4zdVml6ONfqSeaMUBs/BYKX6v5blJeY0fp/I++0Jc11IdAO/sNrG3YmfSorcVsg/g/hOtzOfUxEnkyPOEis/zLyYQ5zyHVWXwiL9UPn4xtWIvjv4JUdzb/I=; bnc-uuid=7cce6ff4-2df8-41fe-a84b-a84d1148f83b; se_gd=RgEBhVBkAATBxJRVSDlZgZZCgVAxQBUVVQPdRVkRlNQVQBFNWWcG1; se_gsd=ZDEnFThlJTYjIwkiIAw1MAQgFgsNBwpUVV5EVVRbW1NWCVNT1; BNC_FV_KEY=33517da47da5a9d39cb0787094c86a57ce43fa25; OptanonAlertBoxClosed=2026-06-26T08:05:40.820Z; g_state={\"i_l\":0,\"i_ll\":1782527236915,\"i_b\":\"LZkOh+LhTkn2GTs8wsBouaRdSTHFu0Zt41yNh3uuQew\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782527236915}; _gcl_au=1.1.945959946.1782527247.548639628.1782527250.1782527249; r20t=web.1231016191.6064174924A79DD05874C6C9EB761C97; r30t=1; cr00=C571781CF012528D8765ECD58B53A6FA; d1og=web.1231016191.7BE2B694E1584EBD570A2EB908D59CE9; r2o1=web.1231016191.C5B148028D8EBEA156F1544959AA2DD3; f30l=web.1231016191.661140EE228DD27760814B8F2DE60E73; currentAccount=; logined=y; lang=zh-CN; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221231016191%22%2C%22first_id%22%3A%2219f02f31d51375-0e08d971254b388-5c123e18-921600-19f02f31d523b7%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDJmMzFkNTEzNzUtMGUwOGQ5NzEyNTRiMzg4LTVjMTIzZTE4LTkyMTYwMC0xOWYwMmYzMWQ1MjNiNyIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjEyMzEwMTYxOTEifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221231016191%22%7D%7D; theme=dark; p20t=web.1231016191.5C3C3ADFB79A3882B099C271D6BD0725; BNC_FV_KEY_T=101-7zQgcXJO%2Fdjn0b%2B8PwEYC3vuMsM0W7OQs6TT3tbOQdHunHwxduzN71nIxF5PzrwHPpDGOe0Fl1AfWQB3Qq%2Bc7w%3D%3D-wI0FswG0zNHzcWkoVQ0X1A%3D%3D-85; BNC_FV_KEY_EXPIRE=1782724115824; _gid=GA1.2.1523406004.1782702517; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A09%3A18+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=c7504a21-d6c0-4520-b8ee-e1a6b1f80dbd&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782461142867&AwaitingReconsent=false&fclco=&lastConsentTs=1782461140&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1782702516$o3$g1$t1782702560$j16$l0$h0; _ga=GA1.1.1332803934.1782461151; _uetsid=e3f7fc50736711f1a1e18fd894e6efb5; _uetvid=bdf0be1071cf11f18246c5b5e8f89687");
//        带单账号 d = new 带单账号("5078042887361143808", "智能操作私域-", "", "");
//        带单账号 d = new 带单账号("5084159823592983552", "智能操作绝对私域-Eenis", "", "");
//        带单账号 d = new 带单账号("5089735604832694016", "智能操作那英推荐私域-uTybtc", "bd7437e9da3b3c5ebd9a8c6e0cba7c46", "bnc-uuid=1e025924-1c4a-4e77-8cdf-ea2e0b199fcb; OptanonAlertBoxClosed=2026-06-26T09:25:59.998Z; se_gd=Q4GERXR4IDNFggR0BBwYgZZUwA1BVBVUlcIJRWkNlNQVQE1NWWIL1; se_gsd=AiAhChVlICwnM1YqJzY7M1ciWxcSBgQUWVlGVVFbW1NWElNT1; BNC_FV_KEY=33e64befed40af64aa71c739730d1194e30decab; _gcl_au=1.1.168757418.1782465991.1333705688.1782466068.1782466068; BNC-Location=CN; lang=zh-CN; changeBasisTimeZone=; userPreferredCurrency=USD_USD; futures-layout=pro; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAsRE0AmopAAAA:Y79l5kvaSSr5k4zSV1DwfQTtLaIoKnl1AWJk3yzpch0n/m4E0IITfs/rWoyTV3H7Flvmm5rS1sZhY8x+4hDn/AAd/S6MXW9htMtIUUNR3tg2JlcL28SMikZMoaBUzSMAbOk6ujtjjwurAeFuVCEo1Pj0bsVOfq+0+f7Qlm9HLxzHv1evAJnloXIfsupfNz+1Gky0nGFeClEFT0f/upItylHn8rnQrY16YJUJ1j6d33bh4oQDmu2Zuzh6I3jPAsNMsiTo1Pqm6KtN; _gid=GA1.2.1998021955.1784532999; g_state={\"i_l\":0,\"i_ll\":1784556540561,\"i_b\":\"SLAvMrNgnwZLyela2JLSG5+Z5meW5dtH+jIQ3KO0fqE\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1784556540561}; r20t=web.1251985875.820F62A816BFBC2C1B1BFD1662A6FB92; r30t=1; cr00=394F1D9434E4CC4151B3586F1C8A9D89; d1og=web.1251985875.D955E8B911E468F8BE855A0F0919BB00; r2o1=web.1251985875.346DE5785018BC587980ADE35A74814A; f30l=web.1251985875.F1328DF6AD68E12CD4431D438D1994DB; currentAccount=; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221251985875%22%2C%22first_id%22%3A%2219f03401270474-0cba1e33452e008-5c123e18-2073600-19f03401271727%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDM0MDEyNzA0NzQtMGNiYTFlMzM0NTJlMDA4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDM0MDEyNzE3MjciLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjUxOTg1ODc1In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221251985875%22%7D%7D; theme=dark; p20t=web.1251985875.EA11DABA1FF750916861FABACAA7A301; _gat_UA-162512367-1=1; BNC_FV_KEY_T=101-hS7L%2BRoMItgmyLorzeUKzMiJXExkXtPPrV5Q%2FaDeJfuTx%2Bd6DwrXLUrdiOjD4n%2BLTebKLekm5tN%2B22B6r2NCRQ%3D%3D-WQhsDhwhyV2hwY%2BLxRW42A%3D%3D-14; BNC_FV_KEY_EXPIRE=1784661257533; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A14%3A19+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=f45f522b-b404-4b2b-9fdc-de049394f1e3&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782465960&intType=1&crTime=1782465961298&geolocation=JP%3B13&AwaitingReconsent=false; _ga_3WP50LGEEC=GS2.1.s1784639657$o15$g1$t1784639662$j55$l0$h0; _ga=GA1.1.1667499196.1782465992; _uetsid=9d40c450841311f1a5f1396efc3b1c91; _uetvid=22662410714111f1ab22f50cde14c96d");

//        带单账号 d = new 带单账号("5105441994801342464", "智能操作Money私域-money", "", "");
//        带单账号 d = new 带单账号("5126587793094958592", "智能操作hk大叔公域", "8bf3ac6e265391733b8bca43e8cf1088", "bnc-uuid=5e97b89f-ea5a-4fdb-a07d-3e79c468e7db; g_state={\"i_l\":0,\"i_ll\":1783084395045,\"i_b\":\"H5GINkpXKCwrlRVjXOLAG3zvyyzbxbWxF9cYzHcwTyg\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1783084395045}; se_gd=gMGGgUw4BQFGVJS0OVQUgZZXADwZVBUUVAVNbW09lJUWwBlNWWEd1; se_gsd=Ai01OzhkNSQmFlYhJQM2BS4iDxMDAAQEWFRCW1BRW1JSNFNT1; OptanonAlertBoxClosed=2026-07-03T13:13:20.117Z; _gcl_au=1.1.383274714.1783084402.1621929085.1783084403.1783084402; BNC_FV_KEY=330bf882da84ad88a9fa97c404a58b434a974891; r30t=1; BNC-Location=CN; lang=zh-CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221258634514%22%2C%22first_id%22%3A%2219f281cadfe3f1-0a6c16c16c16c18-5c123e18-2073600-19f281cadff445%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMjgxY2FkZmUzZjEtMGE2YzE2YzE2YzE2YzE4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMjgxY2FkZmY0NDUiLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjU4NjM0NTE0In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221258634514%22%7D%7D; changeBasisTimeZone=; futures-layout=pro; userPreferredCurrency=USD_USD; _gid=GA1.2.1787328124.1785721885; r20t=web.1258634514.E6D8ED63C7DA3B81963180FFB2967954; cr00=8CCFB3B069BE297802F6E49F0CB25A96; d1og=web.1258634514.617CB709A2B69DE7A97819F1090C03EF; r2o1=web.1258634514.2B7046C1C58F58C6BBED3DF319DFB27F; f30l=web.1258634514.6883CB2E68EB38BC6C6B61FF1134F196; p20t=web.1258634514.471EBEF6790B78A4557C0B9A452520D4; aws-waf-token=44446c2f-cc04-4c93-94c0-4ffdc34bf54c:BgoAeqZJQQ8XAAAA:+36Ju/hVNGEnpQ7pjzkpha8KBcKxlm0eZ57TBP+CvBnzUh6jf755mTVrlu4whHRAeMP219hMX5/n64btkFSfxIpbxNn8gv4ChMHq1Ao2mY8gerV7rIWEwzX6qQMtAvrog884y9p3IkeG/AUMqIrxylKesoA2BuaG8N9sGzQ+Y0X9j/sllEu+qo9NNNDhi+eoP2s+P9Z7RZ22gJrO3KxcfMzbGCiJz3XOT4nH+oR8xZusIwbv3JhbGVdVnRZZAIVQ5duqXUfLfb2f; BNC_FV_KEY_T=101-Tv6xfRhcxVPfGky9KZr8fvuSiZfCYwkR%2B%2BrHRKa6r6khMCpDkoUTP%2FMa%2FMS3%2BLWFlHflW%2BIRqeozJswjBys7PQ%3D%3D-1b6KO3%2FzEye2Y6Ps0GZXNA%3D%3D-68; BNC_FV_KEY_EXPIRE=1785774629943; theme=dark; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Aug+03+2026+18%3A35%3A09+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=357614cb-d0f7-4918-bca7-7d50ae9c4287&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1783084400876&AwaitingReconsent=false&fclco=&lastConsentTs=1783084400&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1785753030$o28$g1$t1785753312$j56$l0$h0; _ga=GA1.1.333784097.1783084402; _uetsid=adfb67908ede11f1a49339db1bdc500a; _uetvid=f7bdad0076e011f19bcd3580492af4b3");
//        带单账号 d = new 带单账号("5086488055220053761", "智能操作私域-意钦公域", "56ca25f01adcaf5751311d0e796c2cd7", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAZ0Ygi7AEAAAA:/6/fo90T/MEWN0aJimW68oUh4lLr5Lsyx3eUJOiPZ0uJ8ocSSWeq3JdHAaaVqCmhKNfwlsJnbcwTK3Hq46rc4aX2pqMpCdQ3ZwTZsG0i1rV1pUlPGt2tqbks+B0DmsJme6a9FNO8PcYQ31qhlTuzqwlwUyaH+nZsQQLnxIBsbj60LkHWZM7ZMSWmX//26iQlQaQ=; bnc-uuid=1e34f244-8e54-4b6d-8b0e-4b823daffe5b; g_state={\"i_l\":0,\"i_ll\":1782462745233,\"i_b\":\"NJRUeUJlxpUHDePobVD/q2DeRC2mfqtLTj7Su7hi1YA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782462745233}; BNC_FV_KEY=332d7bde643fa6af9a59d9fb8abf8c7618c59f41; se_gd=hFbEwWloDAbAB4bdVCAtgZZDxXFMABVU1AeVfVU5lNQVQBVNWWMd1; se_gsd=VjQgL0ZVJQAgDQ0nJxM0CiUHUgYNBgIPVlRGVVZVW1NWJFNT1; r20t=web.1193259156.07EE38FB360CFCA43B1BA0665A405F58; r30t=1; cr00=D77611E750F6DB3768BD2327656AEE78; d1og=web.1193259156.A38160507DDBD1E53083B55EB886CCCC; r2o1=web.1193259156.B493C042A18783BF90BC45800F8536D3; f30l=web.1193259156.0D99F3B91FBA3637C221815ADFFB5EA3; currentAccount=; logined=y; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221193259156%22%2C%22first_id%22%3A%2219f030f4c5d113-03335728ce3464e-5c123e18-921600-19f030f4c5e6dd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwZjRjNWQxMTMtMDMzMzU3MjhjZTM0NjRlLTVjMTIzZTE4LTkyMTYwMC0xOWYwMzBmNGM1ZTZkZCIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjExOTMyNTkxNTYifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221193259156%22%7D%7D; theme=dark; BNC_FV_KEY_T=101-dqP2oiZlA7HNjhsKhLF1BH%2B8m0CyzxOAo%2BXeoZf%2BaYPyLzfybVNfy%2B%2FO1z9Qz5H%2BBsn2W8veJtXXezoUCURR0w%3D%3D-JlypXBqBaHFvtecXFQc58A%3D%3D-22; BNC_FV_KEY_EXPIRE=1782721737280; p20t=web.1193259156.2889856ADDCE26D8BE88EE6F3AFFFD62; OptanonAlertBoxClosed=2026-06-29T02:29:02.926Z; _gid=GA1.2.1214418029.1782700145; _gcl_au=1.1.658042552.1782700169; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+10%3A29%3A51+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=de433a9d-63c4-4c84-8063-1fc127a3ad3e&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782700144551&AwaitingReconsent=false&fclco=&lastConsentTs=1782700142&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1782700144$o1$g1$t1782700194$j10$l0$h0; _ga=GA1.1.531584620.1782700144; _uetsid=5beb1b60736211f1b2204f631838bb59; _uetvid=5beb27a0736211f18710d1f1f63ed495");





        JSONArray jsonArray = new JSONArray();
        System.out.println("下面是：" + d.getName() + "的分润");
        for (int i = 1; i < 6; i++) {
            JSONArray temp = sendPOST(1, d.getCookie(), d.getToken(), i, d.getId());
            if (CollectionUtils.isEmpty(temp)) {
                break;
            } else {
                jsonArray.addAll(temp);
            }
            Thread.sleep(5000);
        }
        String startTime = "2026-07-27 12:00:00";
        String endDime = "2026-08-03 09:00:00";
        Long startTimeLong = zhuanHuan(startTime);
        Long endTimeLong = zhuanHuan(endDime);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            Long timeFen = jsonObject.getLong("time");
            if (timeFen >= startTimeLong && timeFen <= endTimeLong) {
                String nickname = jsonObject.getString("nickname");
                if (客户Map.containsKey(nickname)) {
                    BigDecimal qian = jsonObject.getBigDecimal("sharedAmount").multiply(客户Map.get(nickname)).setScale(1, BigDecimal.ROUND_DOWN);
                    System.out.println(nickname + ":" + qian);
                }
            }
        }

    }

    public static JSONArray sendPOST(int daiLi, String cookie, String token, int pageNumber, String portfolioId) throws IOException {
        Response response;
        if (daiLi == 1) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"pageNumber\":"+ pageNumber + ",\"pageSize\":200,\"portfolioId\":" + portfolioId + "}");
            Request request = new Request.Builder()
                    .url("https://www.binance.com/bapi/futures/v1/private/future/copy-trade/lead-portfolio/profit-shared-history")
                    .method("POST", body)
                    .addHeader("authority", "www.binance.com")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("csrftoken", token)
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "b283926a-cc01-4019-9413-0a4797a681ab")
                    .addHeader("fvideo-token", "Rbtb9zlqn93PAtJt+S2F4CiwnVqIefwB2aXE0XFq6OFC9wgxNrm2jlr3WdzAKwd1DCj89K7ofYdV5TcMqslzlQ1VZD+NSJmvgVAufKetNAuMZoxad9ZaesLGYj+MmxMueaeGYy7SWkicufGzblTuAE7nXrkxiru5X2/oFdVEvpA4mYhSZ+ZDZHa5g4EFFawCY=45")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "33346763535facb6869b0be721d5249536f862b8")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "dfa76664-9fbb-4550-ab69-1e5aca37ef1b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "dfa76664-9fbb-4550-ab69-1e5aca37ef1b")
                    .addHeader("bnc-location", "CN")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("content-type", "application/json")
                    .addHeader("bnc-level", "0")
                    .addHeader("clienttype", "web")
                    .addHeader("accept", "*/*")
                    .addHeader("origin", "https://www.binance.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.binance.com/zh-CN/copy-trading/profit-sharing?portfolioId=4577970699563577857")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("cookie", cookie)
                    .build();
            response = client.newCall(request).execute();
        }else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"pageNumber\":"+ pageNumber + ",\"pageSize\":200,\"portfolioId\":" + portfolioId + "}");

            Request request = new Request.Builder()
                    .url("https://www.usnbweb.mobi/bapi/futures/v1/private/future/copy-trade/lead-portfolio/profit-shared-history")
                    .method("POST", body)
                    .addHeader("authority", "www.usnbweb.mobi")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("csrftoken", token)
                    .addHeader("bnc-time-zone", "Asia/Shanghai")
                    .addHeader("lang", "zh-CN")
                    .addHeader("device-info", "eyJzY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA4MCIsImF2YWlsYWJsZV9zY3JlZW5fcmVzb2x1dGlvbiI6IjE5MjAsMTA0MCIsInN5c3RlbV92ZXJzaW9uIjoiV2luZG93cyA4LjEiLCJicmFuZF9tb2RlbCI6InVua25vd24iLCJzeXN0ZW1fbGFuZyI6InpoLUNOIiwidGltZXpvbmUiOiJHTVQrMDg6MDAiLCJ0aW1lem9uZU9mZnNldCI6LTQ4MCwidXNlcl9hZ2VudCI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDYuMzsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzk3LjAuNDY5Mi43MSBTYWZhcmkvNTM3LjM2IiwibGlzdF9wbHVnaW4iOiJQREYgVmlld2VyLENocm9tZSBQREYgVmlld2VyLENocm9taXVtIFBERiBWaWV3ZXIsTWljcm9zb2Z0IEVkZ2UgUERGIFZpZXdlcixXZWJLaXQgYnVpbHQtaW4gUERGIiwiY2FudmFzX2NvZGUiOiJ1bmtub3duIiwid2ViZ2xfdmVuZG9yIjoiR29vZ2xlIEluYy4gKEdvb2dsZSkiLCJ3ZWJnbF9yZW5kZXJlciI6IkFOR0xFIChHb29nbGUsIFZ1bGthbiAxLjIuMCAoU3dpZnRTaGFkZXIgRGV2aWNlIChTdWJ6ZXJvKSAoMHgwMDAwQzBERSkpLCBTd2lmdFNoYWRlciBkcml2ZXItNS4wLjApIiwiYXVkaW8iOiIxMjQuMDQzNDc1Mjc1MTYwNzQiLCJwbGF0Zm9ybSI6IldpbjMyIiwid2ViX3RpbWV6b25lIjoiQXNpYS9TaGFuZ2hhaSIsImRldmljZV9uYW1lIjoiQ2hyb21lIFY5Ny4wLjQ2OTIuNzEgKFdpbmRvd3MpIiwiZmluZ2VycHJpbnQiOiIzMWFiNmVkNjdmNTFhM2EyMzM5MDEwNmZjZmRmYTkwNyIsImRldmljZV9pZCI6IiIsInJlbGF0ZWRfZGV2aWNlX2lkcyI6IiJ9")
                    .addHeader("bnc-uuid", "1831bbfa-0e49-429f-8a7f-48b04fce3c9b")
                    .addHeader("fvideo-token", "PR3ypaeBHlU2fNOETorjVrId+w4BJOsZ1yjJFwvoxd9TjKOm6lsabHYZwLWUvAOaMrgpuZsWjEEDC4jV0ZkGQoiNBgGBQwTCeKodLhtjum1vxFSpGLbWoTae4U+IgdeKq9qJ1wmCOat70TwHj4+vObSukRAqSLCJlrsuOQIcPzJmNG+MDsyj22y5BlaM0Mv2w=57")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("fvideo-id", "33c0cb12aab4a440ba0e7fde24b420289e7df811")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("x-ui-request-trace", "15a70b88-0ab9-458a-a76a-e9ea4fa4624b")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("x-trace-id", "15a70b88-0ab9-458a-a76a-e9ea4fa4624b")
                    .addHeader("bnc-location", "CN")
                    .addHeader("x-passthrough-token", "")
                    .addHeader("content-type", "application/json")
                    .addHeader("bnc-level", "0")
                    .addHeader("clienttype", "web")
                    .addHeader("accept", "*/*")
                    .addHeader("origin", "https://www.usnbweb.mobi")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.usnbweb.mobi/zh-CN/copy-trading/profit-sharing?portfolioId=4892870556182209281")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9")
                    .addHeader("cookie", cookie)
                    .build();
            response = client.newCall(request).execute();
        }


        String s = response.body().string();
        JSONArray jsonArray = null;
        if (StringUtils.isNotBlank(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            if ("000000".equals(jsonObject.getString("code"))) {
                jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");

            }
        }
        return jsonArray;
    }

    static long zhuanHuan(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 转成时间戳（毫秒）
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        long timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return timestamp;
    }


}

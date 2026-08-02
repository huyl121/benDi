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

        //以下是plus转星辰
        客户Map.put("屋嘎龙男孩", new BigDecimal("1"));
        客户Map.put("10u上1w", new BigDecimal("1"));

        //以下是中奖

        客户Map.put("Eatonliu", new BigDecimal("1"));
        客户Map.put("coldrain01", new BigDecimal("1"));
        客户Map.put("藍莫瞳", new BigDecimal("1"));
        客户Map.put("0xNan", new BigDecimal("1"));


//        带单账号 d = new 带单账号("4892870556182209281", "智能操作公域-星辰", "ec0ebaa2f1f315bd29d1038404e2e85f", "bnc-uuid=1e34f244-8e54-4b6d-8b0e-4b823daffe5b; g_state={\"i_l\":0,\"i_ll\":1782462745233,\"i_b\":\"NJRUeUJlxpUHDePobVD/q2DeRC2mfqtLTj7Su7hi1YA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782462745233}; BNC_FV_KEY=332d7bde643fa6af9a59d9fb8abf8c7618c59f41; se_gd=hFbEwWloDAbAB4bdVCAtgZZDxXFMABVU1AeVfVU5lNQVQBVNWWMd1; se_gsd=VjQgL0ZVJQAgDQ0nJxM0CiUHUgYNBgIPVlRGVVZVW1NWJFNT1; r30t=1; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221193259156%22%2C%22first_id%22%3A%2219f030f4c5d113-03335728ce3464e-5c123e18-921600-19f030f4c5e6dd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwZjRjNWQxMTMtMDMzMzU3MjhjZTM0NjRlLTVjMTIzZTE4LTkyMTYwMC0xOWYwMzBmNGM1ZTZkZCIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjExOTMyNTkxNTYifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221193259156%22%7D%7D; OptanonAlertBoxClosed=2026-06-29T02:29:02.926Z; _gcl_au=1.1.658042552.1782700169; changeBasisTimeZone=; futures-layout=pro; userPreferredCurrency=USD_USD; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAu14vemEzAAAA:7rXU2A5+Gwg2M/GB6zLwgRwn5ew4/aH3uSD/9YMWWpHr4z4/gTchn5hA3t9vcJl4RqzAo7RkRPg7GhB99tq5rfj0P8JU0j2fq9MZNP1FexbKhu9IVLGe7lMGcZ9IemTvRa6wQ5iOwDxJ1Mg9TeQSlz+t0RgL7lQ+q2G9N1nn+Ii3ukZtLQOZzJV/8fULiSW6Q/s8zPdXQyGfZxDIgJmD7K0aC5QQupFA9x0xIBjZ4W6GS4CCvXM4SqC3qXbixbNRls4e5yrTKm28; r20t=web.1193259156.6A3CC79690B595B4E451F31889380CE0; cr00=5E4B50EC88E6D7E207719A94EE0A24FA; d1og=web.1193259156.B170BF4B0A2665D90F1D72BE17D9EDA4; r2o1=web.1193259156.DE070E0C078922E9DE405B213222C36F; f30l=web.1193259156.EE5857FEC3D3D3991813B0EA60E41CBF; p20t=web.1193259156.80768BA86B17F272F10B853E01668C63; _uetvid=5beb27a0736211f18710d1f1f63ed495; theme=dark; BNC_FV_KEY_T=101-ruhW3Zqkliq70A0g2VZWy5keVWe75cxEOd6i1eipJnLxrOfwJRPY8Eb9qqvLkY1z%2BFEFMHWIVC8Ij6zyn%2BaVmA%3D%3D-%2FhvKl7mHtG0MJRJTBbsuag%3D%3D-30; BNC_FV_KEY_EXPIRE=1784661563238; _gid=GA1.2.336097439.1784639965; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A19%3A25+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=de433a9d-63c4-4c84-8063-1fc127a3ad3e&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782700144551&AwaitingReconsent=false&fclco=&lastConsentTs=1782700142&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1784639964$o16$g1$t1784639967$j57$l0$h0; _ga=GA1.1.531584620.1782700144");
//        带单账号 d = new 带单账号("5086488055220053761", "智能操作私域-意钦公域", "56ca25f01adcaf5751311d0e796c2cd7", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAZ0Ygi7AEAAAA:/6/fo90T/MEWN0aJimW68oUh4lLr5Lsyx3eUJOiPZ0uJ8ocSSWeq3JdHAaaVqCmhKNfwlsJnbcwTK3Hq46rc4aX2pqMpCdQ3ZwTZsG0i1rV1pUlPGt2tqbks+B0DmsJme6a9FNO8PcYQ31qhlTuzqwlwUyaH+nZsQQLnxIBsbj60LkHWZM7ZMSWmX//26iQlQaQ=; bnc-uuid=1e34f244-8e54-4b6d-8b0e-4b823daffe5b; g_state={\"i_l\":0,\"i_ll\":1782462745233,\"i_b\":\"NJRUeUJlxpUHDePobVD/q2DeRC2mfqtLTj7Su7hi1YA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782462745233}; BNC_FV_KEY=332d7bde643fa6af9a59d9fb8abf8c7618c59f41; se_gd=hFbEwWloDAbAB4bdVCAtgZZDxXFMABVU1AeVfVU5lNQVQBVNWWMd1; se_gsd=VjQgL0ZVJQAgDQ0nJxM0CiUHUgYNBgIPVlRGVVZVW1NWJFNT1; r20t=web.1193259156.07EE38FB360CFCA43B1BA0665A405F58; r30t=1; cr00=D77611E750F6DB3768BD2327656AEE78; d1og=web.1193259156.A38160507DDBD1E53083B55EB886CCCC; r2o1=web.1193259156.B493C042A18783BF90BC45800F8536D3; f30l=web.1193259156.0D99F3B91FBA3637C221815ADFFB5EA3; currentAccount=; logined=y; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221193259156%22%2C%22first_id%22%3A%2219f030f4c5d113-03335728ce3464e-5c123e18-921600-19f030f4c5e6dd%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMwZjRjNWQxMTMtMDMzMzU3MjhjZTM0NjRlLTVjMTIzZTE4LTkyMTYwMC0xOWYwMzBmNGM1ZTZkZCIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjExOTMyNTkxNTYifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221193259156%22%7D%7D; theme=dark; BNC_FV_KEY_T=101-dqP2oiZlA7HNjhsKhLF1BH%2B8m0CyzxOAo%2BXeoZf%2BaYPyLzfybVNfy%2B%2FO1z9Qz5H%2BBsn2W8veJtXXezoUCURR0w%3D%3D-JlypXBqBaHFvtecXFQc58A%3D%3D-22; BNC_FV_KEY_EXPIRE=1782721737280; p20t=web.1193259156.2889856ADDCE26D8BE88EE6F3AFFFD62; OptanonAlertBoxClosed=2026-06-29T02:29:02.926Z; _gid=GA1.2.1214418029.1782700145; _gcl_au=1.1.658042552.1782700169; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+10%3A29%3A51+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=de433a9d-63c4-4c84-8063-1fc127a3ad3e&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782700144551&AwaitingReconsent=false&fclco=&lastConsentTs=1782700142&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1782700144$o1$g1$t1782700194$j10$l0$h0; _ga=GA1.1.531584620.1782700144; _uetsid=5beb1b60736211f1b2204f631838bb59; _uetvid=5beb27a0736211f18710d1f1f63ed495");
        带单账号 d = new 带单账号("5108833703275227137", "智能操作银行公域-意钦私域", "325050ce6bcbab2cddb7f3649404d7fd", "bnc-uuid=12e4f412-23c2-4a9d-8cbd-6f83dc2def5b; OptanonAlertBoxClosed=2026-06-26T04:48:10.475Z; se_gd=g4NUBTxkWHWBB0CMHAFYgZZXBDhlUBZVlYG5bUkdlNQVwFlNWWAD1; se_gsd=fyoiL0J0LDUjGQk0NyY3BVcrFhMIAgoKUV1KV11RW1NWI1NT1; _gcl_au=1.1.1872081329.1782449304; BNC_FV_KEY=339d7dc1f222af1f9c9b2207f79aafe7ceac974d; BNC-Location=CN; changeBasisTimeZone=; futures-layout=pro; g_state={\"i_l\":0,\"i_ll\":1783311425647,\"i_b\":\"CUd27omDaNJ6ZQD7npqum8/+jcN2Sd83TKaox8kL0pc\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1783311425647}; r30t=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221222792435%22%2C%22first_id%22%3A%2219f0241b46c305-06912c094551f64-5c123e18-2073600-19f0241b46d397%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDI0MWI0NmMzMDUtMDY5MTJjMDk0NTUxZjY0LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDI0MWI0NmQzOTciLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjIyNzkyNDM1In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221222792435%22%7D%7D; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAptYwtDYkAAAA:jTxmKxYMqiIzHPKhAuep5tCCw4Jcv83gGxlx2Oi6Q6S4R7oplhZ4PJv2Bj5kXpanFROJ6ku7r4rO6RvUJiQfAo8NLlqbH3S5OuURqoDIPp8dDquCNKIT/aPAC0KuPSF52hgrwlyvhIen7xRLQpXdVug+dVYHD181RXahYQNmEephGwnL8v6sTr8BurNZ2+bxyMhbwx9M4n72ABiFO93Zjp7s/OYvnjd9kaqAHDEDxKDkleJ2NWbaxT0GL0q917+VKhh0oEQpssP3; r20t=web.1222792435.5773062CB773941B7655D29B58DDF515; cr00=7C97ED43945AE43D1BA3EE3CB0A538D0; d1og=web.1222792435.3BC50E4C58FA1F754FC420067C1D4016; r2o1=web.1222792435.AD32544EAEF57BEED2D38F529E628DBF; f30l=web.1222792435.FAB52BAEB8B65D421AA1813E65EF035B; p20t=web.1222792435.5D161EBD39D245681D853BB845DA3F8F; theme=dark; BNC_FV_KEY_T=101-byKDU%2BygA7M%2BseSAdJpfneu%2Fa2xqxmLDZO648vEnxPiZbGxrb%2FehhTpMkIUR3gIDnUijWKFoN8svt6JuCdGoIQ%3D%3D-QEcJ50XyLksRBg%2FGfH%2B2oQ%3D%3D-2c; BNC_FV_KEY_EXPIRE=1784661371118; _gid=GA1.2.651903466.1784639771; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A16%3A13+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=72368bb5-926d-4a2f-859e-ab49ecb71499&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782449290&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=5c776ca0850611f1a8c1994e3e0c79a9; _uetvid=43ee9020711a11f1af4b19b7105ceaf3; _ga=GA1.2.1454322713.1782449305; _gat_UA-162512367-1=1; _ga_3WP50LGEEC=GS2.1.s1784639771$o13$g1$t1784639890$j60$l0$h0");
//        带单账号 d = new 带单账号("4965357180596700929", "智能操作银行私域-星辰银行", "325050ce6bcbab2cddb7f3649404d7fd", "bnc-uuid=12e4f412-23c2-4a9d-8cbd-6f83dc2def5b; OptanonAlertBoxClosed=2026-06-26T04:48:10.475Z; se_gd=g4NUBTxkWHWBB0CMHAFYgZZXBDhlUBZVlYG5bUkdlNQVwFlNWWAD1; se_gsd=fyoiL0J0LDUjGQk0NyY3BVcrFhMIAgoKUV1KV11RW1NWI1NT1; _gcl_au=1.1.1872081329.1782449304; BNC_FV_KEY=339d7dc1f222af1f9c9b2207f79aafe7ceac974d; BNC-Location=CN; changeBasisTimeZone=; futures-layout=pro; g_state={\"i_l\":0,\"i_ll\":1783311425647,\"i_b\":\"CUd27omDaNJ6ZQD7npqum8/+jcN2Sd83TKaox8kL0pc\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1783311425647}; r30t=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221222792435%22%2C%22first_id%22%3A%2219f0241b46c305-06912c094551f64-5c123e18-2073600-19f0241b46d397%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDI0MWI0NmMzMDUtMDY5MTJjMDk0NTUxZjY0LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDI0MWI0NmQzOTciLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjIyNzkyNDM1In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221222792435%22%7D%7D; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAptYwtDYkAAAA:jTxmKxYMqiIzHPKhAuep5tCCw4Jcv83gGxlx2Oi6Q6S4R7oplhZ4PJv2Bj5kXpanFROJ6ku7r4rO6RvUJiQfAo8NLlqbH3S5OuURqoDIPp8dDquCNKIT/aPAC0KuPSF52hgrwlyvhIen7xRLQpXdVug+dVYHD181RXahYQNmEephGwnL8v6sTr8BurNZ2+bxyMhbwx9M4n72ABiFO93Zjp7s/OYvnjd9kaqAHDEDxKDkleJ2NWbaxT0GL0q917+VKhh0oEQpssP3; r20t=web.1222792435.5773062CB773941B7655D29B58DDF515; cr00=7C97ED43945AE43D1BA3EE3CB0A538D0; d1og=web.1222792435.3BC50E4C58FA1F754FC420067C1D4016; r2o1=web.1222792435.AD32544EAEF57BEED2D38F529E628DBF; f30l=web.1222792435.FAB52BAEB8B65D421AA1813E65EF035B; p20t=web.1222792435.5D161EBD39D245681D853BB845DA3F8F; theme=dark; BNC_FV_KEY_T=101-byKDU%2BygA7M%2BseSAdJpfneu%2Fa2xqxmLDZO648vEnxPiZbGxrb%2FehhTpMkIUR3gIDnUijWKFoN8svt6JuCdGoIQ%3D%3D-QEcJ50XyLksRBg%2FGfH%2B2oQ%3D%3D-2c; BNC_FV_KEY_EXPIRE=1784661371118; _gid=GA1.2.651903466.1784639771; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A16%3A13+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=72368bb5-926d-4a2f-859e-ab49ecb71499&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782449290&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=5c776ca0850611f1a8c1994e3e0c79a9; _uetvid=43ee9020711a11f1af4b19b7105ceaf3; _ga=GA1.2.1454322713.1782449305; _gat_UA-162512367-1=1; _ga_3WP50LGEEC=GS2.1.s1784639771$o13$g1$t1784639890$j60$l0$h0");
//        带单账号 d = new 带单账号("5004754260218990081", "智能操作私域-仓王", "", "");
//        带单账号 d = new 带单账号("4936201898008384000", "智能操作私域-仓王ETH", "", "");
//        带单账号 d = new 带单账号("5053878404924732417", "智能操作Kimi大林公域-大林", "20539185f2aa865234e401e72a4d6680", "bnc-uuid=fbd3e2d4-cd1b-4990-86b9-cf631155ca3b; BNC_FV_KEY=338f63a3fa11ae0d82071d09c3ccadc881d573ba; aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAohg7tNsEAAAA:jpoA++CfvrWi9/tOVw5QW1OgfhWwDVtIbDxEA9TmH9BYXtkswyCXMsQnYBTdL/uoZ+wUCNqTK7/XJGcptopnk/iaCJe9uKPHh5RPjzatMVb8Yf/qbSJ10k+6ytaqv6eHWBJqt5W7sJl5N5azUi1G3mRGqENwsKTnY+Q967XRD93WvulNBnMdk/uob5SDkcZIvwo=; g_state={\"i_l\":0,\"i_ll\":1782463054976,\"i_b\":\"RKh6740GKuHYVJ/JzgX1lfeX85Q/DPjtGqqCXC6r7yo\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782463054976}; se_gd=QRTElAQdREIVFIR5VVwogZZBxAhsXBXVlIHRYVEVlNQVQEFNWWNS1; se_gsd=BzQ1KztlISs3MAYxJwgiMzExAF1XDwsbV19EVVdSW1NWElNT1; r20t=web.1238115593.BC65D318F600931F50EF3554D2D780D9; r30t=1; cr00=6D83ADE25611585978BB2607A6D46B22; d1og=web.1238115593.6E52E699FF6BF178AA2519E1E8BAAF00; r2o1=web.1238115593.990FAB0DCD546D3576E1B2C2BE199E1A; f30l=web.1238115593.B6BA03AE5DCFAD1445BC79C87D37A686; currentAccount=; logined=y; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221238115593%22%2C%22first_id%22%3A%2219f023d44cecb-05e8f6a8b33c308-5c123e18-2073600-19f023d44cf72a%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDIzZDQ0Y2VjYi0wNWU4ZjZhOGIzM2MzMDgtNWMxMjNlMTgtMjA3MzYwMC0xOWYwMjNkNDRjZjcyYSIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjEyMzgxMTU1OTMifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221238115593%22%7D%7D; OptanonAlertBoxClosed=2026-06-27T11:06:43.132Z; theme=dark; p20t=web.1238115593.062F21B8A4D2870DFF08225531684B42; BNC_FV_KEY_T=101-WqLtR1ayIRbhFz1owTTuXQHX2iYXKm%2FBoI4A353csE7l7sjKL4ZP1njoZfMjbxuvZvGp6MFasSa0TgphAY5r0w%3D%3D-i1qsLOZcwtelQtCF3WkMUw%3D%3D-cb; BNC_FV_KEY_EXPIRE=1782723715524; _gid=GA1.2.384942752.1782702116; _gcl_au=1.1.2055870773.1782702120; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A04%3A32+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=7791ab2f-f75e-4227-a8f4-fae1ea549e96&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782558404095&AwaitingReconsent=false&fclco=&lastConsentTs=1782558403&intType=1&geolocation=JP%3B13; _uetsid=e6e248a0736611f1937771df42ad3ac0; _uetvid=e6e295d0736611f1a6b9c3a58afc7909; _ga=GA1.2.1215483798.1782558407; _ga_3WP50LGEEC=GS2.1.s1782702116$o3$g1$t1782702280$j32$l0$h0");
//        带单账号 d = new 带单账号("5099835914695652352", "智能操作Kimi大林私域-不停梭迷弟", "", "");
//        带单账号 d = new 带单账号("5005577217312895745", "智能操作小周同学私域-小周同学", "5a758d457d2e52845fc6d5d06dc85c18", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAgDg8YioBAAAA:7mJBEMJo72Qn5k/4OZ7/ZQBu2tTDn1iwW3oOxNz7vHgd4WZqsog7OzBlKfj+lAVf0use09Lb2fAiRhn2YehZ4PX8Ej2YV0pCBRAkfmJJ82Q9ZL9IjPmdTGCuPDQPc9YkCpj74KTS7iOAgaS8fyJodAHDe/yJsyyT4MUF1qsVn74bZGSAApVioScWrhZh7gWwnDw=; bnc-uuid=00078672-a427-4d69-86df-877509c40a5b; se_gd=1IDGhABgATGFF0AdbBFYgZZUACQlRBXWlpcNfVEVlNQVQBFNWWVU1; se_gsd=VgAhK0J2MCUnIwUiJQwiUyohBVBUDwoWV19EVVBVW1NWI1NT1; BNC_FV_KEY=331e7153cc34a99c90c6ea8cd9ceabaa097a1ec3; OptanonAlertBoxClosed=2026-06-26T09:06:13.491Z; g_state={\"i_l\":0,\"i_ll\":1782464912502,\"i_b\":\"Ma6n9svbSRDYufumLIlJCmYkwRWXyeT/ZqYgoPmAxeA\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782464912502}; _gcl_au=1.1.1561541001.1782464934; r20t=web.1229775668.0461C18FFF0DE853BDBCFEB5720AE7AD; r30t=1; cr00=7849545E1CD44E37128F1CC51D72217A; d1og=web.1229775668.7972DC6895511EC01C1457B870E67682; r2o1=web.1229775668.7F3388CF701492678F67C18202952C3B; f30l=web.1229775668.3592FF5489261440F283441F0B1544D7; currentAccount=; logined=y; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221229775668%22%2C%22first_id%22%3A%2219f032dd7ae1e-0fb0c6e6815e9b-5c123e18-2073600-19f032dd7af68d%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDMyZGQ3YWUxZS0wZmIwYzZlNjgxNWU5Yi01YzEyM2UxOC0yMDczNjAwLTE5ZjAzMmRkN2FmNjhkIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTIyOTc3NTY2OCJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221229775668%22%7D%7D; theme=dark; p20t=web.1229775668.E81ED539A4E87C7B7D74F05932B45FE7; _gid=GA1.2.1667659499.1782702349; BNC_FV_KEY_T=101-c9ejCpArDYHzXzp4yiRdreOtRSRP0yonScXOwfmsh7Sz1%2FRQyjB%2FH6sY6Fnqsd79mQhaAFL5bgHLAH5Je26BxA%3D%3D-Cc%2FbL3cqdp3lQ4cCqWpfNQ%3D%3D-fe; BNC_FV_KEY_EXPIRE=1782723949257; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A05%3A57+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=b9fdc09a-12a2-4698-8d79-339fead07500&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&crTime=1782464777973&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782464773&intType=1&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=7545b4a0736711f1a6f1f78642708aee; _uetvid=a92ddaf0713e11f18a1b87d7629238c1; _ga=GA1.2.456841213.1782464780; _ga_3WP50LGEEC=GS2.1.s1782702348$o2$g1$t1782702386$j22$l0$h0");
//        带单账号 d = new 带单账号("", "智能操作风火公域-风火", "", "");
//        带单账号 d = new 带单账号("5062549412865388289", "智能操作林小柔公域-林小柔", "a3832785029f7c0b1f4fd0341734e7fd", "aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAqzggIrEHAAAA:QU6aqEGmJZaToJpAz9KrvHptUBcfBepiIZnYjAa8vKotiAYRpojpP3caabCxjQ3uIRahzgeNxxai4zdVml6ONfqSeaMUBs/BYKX6v5blJeY0fp/I++0Jc11IdAO/sNrG3YmfSorcVsg/g/hOtzOfUxEnkyPOEis/zLyYQ5zyHVWXwiL9UPn4xtWIvjv4JUdzb/I=; bnc-uuid=7cce6ff4-2df8-41fe-a84b-a84d1148f83b; se_gd=RgEBhVBkAATBxJRVSDlZgZZCgVAxQBUVVQPdRVkRlNQVQBFNWWcG1; se_gsd=ZDEnFThlJTYjIwkiIAw1MAQgFgsNBwpUVV5EVVRbW1NWCVNT1; BNC_FV_KEY=33517da47da5a9d39cb0787094c86a57ce43fa25; OptanonAlertBoxClosed=2026-06-26T08:05:40.820Z; g_state={\"i_l\":0,\"i_ll\":1782527236915,\"i_b\":\"LZkOh+LhTkn2GTs8wsBouaRdSTHFu0Zt41yNh3uuQew\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782527236915}; _gcl_au=1.1.945959946.1782527247.548639628.1782527250.1782527249; r20t=web.1231016191.6064174924A79DD05874C6C9EB761C97; r30t=1; cr00=C571781CF012528D8765ECD58B53A6FA; d1og=web.1231016191.7BE2B694E1584EBD570A2EB908D59CE9; r2o1=web.1231016191.C5B148028D8EBEA156F1544959AA2DD3; f30l=web.1231016191.661140EE228DD27760814B8F2DE60E73; currentAccount=; logined=y; lang=zh-CN; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221231016191%22%2C%22first_id%22%3A%2219f02f31d51375-0e08d971254b388-5c123e18-921600-19f02f31d523b7%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDJmMzFkNTEzNzUtMGUwOGQ5NzEyNTRiMzg4LTVjMTIzZTE4LTkyMTYwMC0xOWYwMmYzMWQ1MjNiNyIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjEyMzEwMTYxOTEifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221231016191%22%7D%7D; theme=dark; p20t=web.1231016191.5C3C3ADFB79A3882B099C271D6BD0725; BNC_FV_KEY_T=101-7zQgcXJO%2Fdjn0b%2B8PwEYC3vuMsM0W7OQs6TT3tbOQdHunHwxduzN71nIxF5PzrwHPpDGOe0Fl1AfWQB3Qq%2Bc7w%3D%3D-wI0FswG0zNHzcWkoVQ0X1A%3D%3D-85; BNC_FV_KEY_EXPIRE=1782724115824; _gid=GA1.2.1523406004.1782702517; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A09%3A18+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=c7504a21-d6c0-4520-b8ee-e1a6b1f80dbd&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1782461142867&AwaitingReconsent=false&fclco=&lastConsentTs=1782461140&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1782702516$o3$g1$t1782702560$j16$l0$h0; _ga=GA1.1.1332803934.1782461151; _uetsid=e3f7fc50736711f1a1e18fd894e6efb5; _uetvid=bdf0be1071cf11f18246c5b5e8f89687");
//        带单账号 d = new 带单账号("5063664421244039936", "智能操作林小柔私域-大爷的弟弟", "", "");
//        带单账号 d = new 带单账号("5075332206126952449", "智能操作那英私域-熬鹰资本", "d682fe9bb34830134cbb89f0f1d6f86e", "bnc-uuid=ed561181-5803-41e9-953c-61baa37142ab; OptanonAlertBoxClosed=2026-06-26T08:06:54.053Z; BNC_FV_KEY=331c533e7f37a79eb29fa8f4dbfd0a1e3bdd2c27; se_gd=QsEVlVgAWEGVgUGIbDVIgZZV1F1UABXW1ZVZaV0JlNQVQUVNWWYK1; _gcl_au=1.1.133583968.1782461256; se_gsd=ewAnCidwISU3BiM0NAg1MxAgBQgWBgoYVFhFVVVQW1NWM1NT1; g_state={\"i_l\":0,\"i_ll\":1782464190912,\"i_b\":\"39IFH/gVZGareG0hRufhIYEOQIJl3Yjxd4i2ga0Eiik\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782464190912}; r30t=1; BNC-Location=CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221052357632%22%2C%22first_id%22%3A%2219f02f80db616b-09dafcb2f44564-5c123e18-921600-19f02f80db7685%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDJmODBkYjYxNmItMDlkYWZjYjJmNDQ1NjQtNWMxMjNlMTgtOTIxNjAwLTE5ZjAyZjgwZGI3Njg1IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTA1MjM1NzYzMiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221052357632%22%7D%7D; userPreferredCurrency=USD_USD; changeBasisTimeZone=; futures-layout=pro; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAlPwzfuwXAAAA:SYYZhJgfb6kPTXoYvnmjmdOUOs+MJHNfYTI7nAegVWRsGmBhDYVMyruGtz58FiJBAjFGU6GaKC5jaXb4na3WTvAhnbSML5x0iDM3yyFRQ7cccUWlYetTSnK4ZnlwoHjPublZysuQ8KWmG0De8LY5K11Dgp7x5QdWRhDne812BR/ceq2doAmLqVEamRvfBLIrI9D8c7G5xyUumDFDNctJCkz478lBaJzV+RzyKSO+KXYq3oMXE4FvkaiL6b6FvRrPdhawaXGrzfIx; r20t=web.1052357632.664A0518FEBA940508DB4B6B1DDEC3CC; cr00=D650F3A91F1CA63D72E8820F8FE20A63; d1og=web.1052357632.C8B568B4FE02A153A652C39294B958B6; r2o1=web.1052357632.4994E2A6DF84169BF6E51E76ABFB86EB; f30l=web.1052357632.13AECBB869D6F052DEA2B88A893105E0; p20t=web.1052357632.7482DF9169EDEEED0F24DF0C66237822; _gid=GA1.2.1498567501.1784532509; theme=dark; BNC_FV_KEY_T=101-SKDNelOKCalmQ4PAEP8Co2PHKYUU1J0LVNppVHaQ6He64fX%2B6Kf3PTXWvA03qMkrvIq49WXPHQGro4rQ1tFjbw%3D%3D-m1GJpc2U7p4O1AqbVjB8bA%3D%3D-d2; BNC_FV_KEY_EXPIRE=1784660919679; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A08%3A42+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=c9622c8a-404d-4ebc-bbab-8c446aca86db&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782461214&intType=1&crTime=1782461216002&geolocation=JP%3B13&AwaitingReconsent=false; _uetsid=9ebc6e80840c11f1b335e53fea68637a; _uetvid=18e21330713611f19f158961dc5c1393; _ga=GA1.2.1230848962.1782461244; _ga_3WP50LGEEC=GS2.1.s1784639319$o22$g1$t1784639327$j52$l0$h0");
//        带单账号 d = new 带单账号("5078042887361143808", "智能操作私域-", "", "");
//        带单账号 d = new 带单账号("5084159823592983552", "智能操作绝对私域-Eenis", "", "");
//        带单账号 d = new 带单账号("5086424148931820033", "智能操作弟弟私域-", "", "");
//        带单账号 d = new 带单账号("5089735604832694016", "智能操作那英推荐私域-uTybtc", "bd7437e9da3b3c5ebd9a8c6e0cba7c46", "bnc-uuid=1e025924-1c4a-4e77-8cdf-ea2e0b199fcb; OptanonAlertBoxClosed=2026-06-26T09:25:59.998Z; se_gd=Q4GERXR4IDNFggR0BBwYgZZUwA1BVBVUlcIJRWkNlNQVQE1NWWIL1; se_gsd=AiAhChVlICwnM1YqJzY7M1ciWxcSBgQUWVlGVVFbW1NWElNT1; BNC_FV_KEY=33e64befed40af64aa71c739730d1194e30decab; _gcl_au=1.1.168757418.1782465991.1333705688.1782466068.1782466068; BNC-Location=CN; lang=zh-CN; changeBasisTimeZone=; userPreferredCurrency=USD_USD; futures-layout=pro; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAsRE0AmopAAAA:Y79l5kvaSSr5k4zSV1DwfQTtLaIoKnl1AWJk3yzpch0n/m4E0IITfs/rWoyTV3H7Flvmm5rS1sZhY8x+4hDn/AAd/S6MXW9htMtIUUNR3tg2JlcL28SMikZMoaBUzSMAbOk6ujtjjwurAeFuVCEo1Pj0bsVOfq+0+f7Qlm9HLxzHv1evAJnloXIfsupfNz+1Gky0nGFeClEFT0f/upItylHn8rnQrY16YJUJ1j6d33bh4oQDmu2Zuzh6I3jPAsNMsiTo1Pqm6KtN; _gid=GA1.2.1998021955.1784532999; g_state={\"i_l\":0,\"i_ll\":1784556540561,\"i_b\":\"SLAvMrNgnwZLyela2JLSG5+Z5meW5dtH+jIQ3KO0fqE\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1784556540561}; r20t=web.1251985875.820F62A816BFBC2C1B1BFD1662A6FB92; r30t=1; cr00=394F1D9434E4CC4151B3586F1C8A9D89; d1og=web.1251985875.D955E8B911E468F8BE855A0F0919BB00; r2o1=web.1251985875.346DE5785018BC587980ADE35A74814A; f30l=web.1251985875.F1328DF6AD68E12CD4431D438D1994DB; currentAccount=; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221251985875%22%2C%22first_id%22%3A%2219f03401270474-0cba1e33452e008-5c123e18-2073600-19f03401271727%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMDM0MDEyNzA0NzQtMGNiYTFlMzM0NTJlMDA4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMDM0MDEyNzE3MjciLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjUxOTg1ODc1In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221251985875%22%7D%7D; theme=dark; p20t=web.1251985875.EA11DABA1FF750916861FABACAA7A301; _gat_UA-162512367-1=1; BNC_FV_KEY_T=101-hS7L%2BRoMItgmyLorzeUKzMiJXExkXtPPrV5Q%2FaDeJfuTx%2Bd6DwrXLUrdiOjD4n%2BLTebKLekm5tN%2B22B6r2NCRQ%3D%3D-WQhsDhwhyV2hwY%2BLxRW42A%3D%3D-14; BNC_FV_KEY_EXPIRE=1784661257533; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A14%3A19+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=f45f522b-b404-4b2b-9fdc-de049394f1e3&interactionCount=1&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&fclco=&lastConsentTs=1782465960&intType=1&crTime=1782465961298&geolocation=JP%3B13&AwaitingReconsent=false; _ga_3WP50LGEEC=GS2.1.s1784639657$o15$g1$t1784639662$j55$l0$h0; _ga=GA1.1.1667499196.1782465992; _uetsid=9d40c450841311f1a5f1396efc3b1c91; _uetvid=22662410714111f1ab22f50cde14c96d");
//        带单账号 d = new 带单账号("5100479022215674624", "智能操作稳健第一公域-稳健第一", "64ddcf34ee34c251de295ac4af1febe6", "bnc-uuid=2868f88b-91e8-4d35-a90f-e06cd8e76dfb; se_gd=AQWD1AxoKFXAxwJkSV1lgZZBgAQAWBSUFBTdeV0BVNUUwFVNWVcG1; se_gsd=WTE1FQV9IjogGQ0oJlUiIzIyAldaAgIDVFpAU1RUVFNSVFNT1; OptanonAlertBoxClosed=2026-02-25T06:24:20.082Z; BNC_FV_KEY=3357d2680c61a42c83f8773b0ebeff85229a2556; BNC-Location=CN; userPreferredCurrency=USD_USD; theme=dark; __BNC_USER_DEVICE_ID__={\"d41d8cd98f00b204e9800998ecf8427e\":{\"date\":1772000723212,\"value\":\"\"}}; campaign=www.binance.com; source=referral; lang=en-gb; aws-waf-token=a1e9f3ab-150b-4318-8f3c-a86b205ce520:AQoAt7ggiVABAAAA:d4DvqKRlwuV9LeIPjavDAcvq0A4PyBT1f9aTLxW4iXkUM6b1JHdXAiYewbfd1ikvBEOw5zsw7HYLnOZoVOjDFaVeITi8Mam214QJRT+/qOBgNpnokB/WERkos+r84Kn6FCecqg3exHiAZ0zHEvVhIzjuWW9Q6AjDHDqWu9szH6OJBkQX9eq5uDrexfS8UMP5/W4=; g_state={\"i_l\":0,\"i_ll\":1782564313339,\"i_b\":\"JwYgyN6urV80iCX/FWW3zhuJvQny7hoqXSRzfCpiZiE\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1782564313339}; _gcl_au=1.1.1986369537.1782564321; r20t=web.1244862496.88488205258A2F944F2D0A7C95FB9FC0; r30t=1; cr00=5F5D0A7F0E149548C7B1621FC623E0FA; d1og=web.1244862496.FB32C8790B3A5F6BB5D2D76338003C81; r2o1=web.1244862496.14D11E635182FFC39A58233757C8D126; f30l=web.1244862496.6D51AFD4A2349AE7E0ABD3B13A9AC54A; currentAccount=; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221244862496%22%2C%22first_id%22%3A%2219c93784a173db-08c33f8fa07b9c8-5c123e18-2073600-19c93784a18642%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTljOTM3ODRhMTczZGItMDhjMzNmOGZhMDdiOWM4LTVjMTIzZTE4LTIwNzM2MDAtMTljOTM3ODRhMTg2NDIiLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjQ0ODYyNDk2In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221244862496%22%7D%2C%22%24device_id%22%3A%2219e838773175ac-094edf337b1e69-5c123e18-2073600-19e838773187d9%22%7D; _gid=GA1.2.456755906.1782698070; BNC_FV_KEY_T=101-fJS2fCSAgigovUI3lA11G5BMIGMagrbWtMye%2F5Y%2B%2FD8GKBBQfbvCmWThP9Fgk4P%2BX%2BKqhWe%2Bi1TGp3Wi2DoEwQ%3D%3D-qGgxHUHdGXp7IATq1%2FEaeQ%3D%3D-5b; BNC_FV_KEY_EXPIRE=1782719670140; p20t=web.1244862496.3DA8D5ABD2524C4E9A587143176E9C73; _gat_UA-162512367-1=1; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jun+29+2026+11%3A30%3A10+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=f04b8e41-6bc0-44a4-9d2f-6ba69de8a223&interactionCount=1&isAnonUser=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&intType=1&geolocation=SG%3B&AwaitingReconsent=false&isDntEnabled=0&prevHadToken=0; _uetsid=d3071c10736a11f1a238a9646472b159; _uetvid=1e7885f0154311f1ac8f312e96b4f1d6; _ga=GA1.2.2051999651.1772000661; _ga_3WP50LGEEC=GS2.1.s1782703804$o24$g1$t1782703832$j32$l0$h0");
//        带单账号 d = new 带单账号("5126587793094958592", "智能操作hk大叔", "b6e189155568af7a37f6c024409b12d9", "bnc-uuid=5e97b89f-ea5a-4fdb-a07d-3e79c468e7db; g_state={\"i_l\":0,\"i_ll\":1783084395045,\"i_b\":\"H5GINkpXKCwrlRVjXOLAG3zvyyzbxbWxF9cYzHcwTyg\",\"i_e\":{\"enable_itp_optimization\":24},\"i_et\":1783084395045}; se_gd=gMGGgUw4BQFGVJS0OVQUgZZXADwZVBUUVAVNbW09lJUWwBlNWWEd1; se_gsd=Ai01OzhkNSQmFlYhJQM2BS4iDxMDAAQEWFRCW1BRW1JSNFNT1; OptanonAlertBoxClosed=2026-07-03T13:13:20.117Z; _gcl_au=1.1.383274714.1783084402.1621929085.1783084403.1783084402; BNC_FV_KEY=330bf882da84ad88a9fa97c404a58b434a974891; r30t=1; BNC-Location=CN; lang=zh-CN; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221258634514%22%2C%22first_id%22%3A%2219f281cadfe3f1-0a6c16c16c16c18-5c123e18-2073600-19f281cadff445%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTlmMjgxY2FkZmUzZjEtMGE2YzE2YzE2YzE2YzE4LTVjMTIzZTE4LTIwNzM2MDAtMTlmMjgxY2FkZmY0NDUiLCIkaWRlbnRpdHlfbG9naW5faWQiOiIxMjU4NjM0NTE0In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221258634514%22%7D%7D; changeBasisTimeZone=; futures-layout=pro; userPreferredCurrency=USD_USD; aws-waf-token=a486fb2c-53d9-4852-8923-7f4e8928faca:BgoAhlg1YVwkAAAA:zZK8Ub1ALpTEkVwG9dPt9ag/qQqPxvVZCPJJfZo5b+TSRGrSNcqrUGSMqbEYWLIB+TKdi5GEQpbIHAzNBO2gscT4f9N7e9kNt7lq/x+pwM3GR9M2kio5YGI46sg63z93qsdaWvzDI15mf4WG9dj+St3MK2Lz8vN58NoXfzxcEysMXCQFgFWQtUFF6qhxx9oBknpXJqhjS1vUvTx4iB0Wvl4SsaDOUCJaWSsebO9Bdm//58/zpUhDANi0nSm3LJJq5VqJ2FYw8mr1; _uetvid=f7bdad0076e011f19bcd3580492af4b3; theme=dark; r20t=web.1258634514.B35627F1C8D79CD186F7131536B0B68F; cr00=E150553AAC74C4663BF56791D100E906; d1og=web.1258634514.4AA4C572FC01E4E885238A9E1294174D; r2o1=web.1258634514.BFF106E0D172F782CCA0215A99FFF3EB; f30l=web.1258634514.E0B7BD595DDC391A74479C51443FF6A3; p20t=web.1258634514.15ADFF2C7ABF2384BB7A656771689B0F; BNC_FV_KEY_T=101-xkj8PModi5ur4uECL1H4qnqBOmz%2BSvkXMI1FOVE7zcl2sf3Pupb0JRS%2B5cDSaElRdyw9ciARZ2DkmiW2e2%2BDwg%3D%3D-B06sF7L9P0yjQZUbpx422w%3D%3D-f3; BNC_FV_KEY_EXPIRE=1784661092054; _gid=GA1.2.1254790560.1784639493; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Jul+21+2026+21%3A11%3A33+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202604.2.0&browserGpcFlag=0&isDntEnabled=0&isIABGlobal=false&hosts=&consentId=357614cb-d0f7-4918-bca7-7d50ae9c4287&interactionCount=2&isAnonUser=1&prevHadToken=0&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&crTime=1783084400876&AwaitingReconsent=false&fclco=&lastConsentTs=1783084400&intType=1&geolocation=JP%3B13; _ga_3WP50LGEEC=GS2.1.s1784639493$o18$g1$t1784639496$j57$l0$h0; _ga=GA1.1.333784097.1783084402");
//        带单账号 d = new 带单账号("5105441994801342464", "智能操作Money私域-money", "", "");





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
        String startTime = "2026-07-13 12:00:00";
        String endDime = "2026-07-20 09:00:00";
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

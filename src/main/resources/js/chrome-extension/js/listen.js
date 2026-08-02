
$(function () {

    console.log("执行插件")
    $.get("http://localhost:8186/kaiShi", {data: 1});
    //js是单线程的，在队列里的方法只会一个个的执行
    // 每隔500毫秒在队列里放一下这个方法。如果js在运行其他的，那么会晚放。也就是如果上一个方法执行了600毫秒，或者其他的地方在使用js这个线程，那么会等js有空的时候再执行你这个方法，所以实际上，可能会大于等于500毫秒
    var doneFlg = 0
    if(doneFlg == 1){
        setInterval(task0311333, 500);
    }else {
        setInterval(task0311333456, 500);
    }

    var kaiShi = true;
    var resultOld = '';
    var resultOldTemp = '';
    var alarm = 0;
    var diaoYongCount = 0;
    var typeStr = new Array //存放能做的USDT
    var typeStrTemp = new Array //存放能做的USDT
    var moneyMin = new Map
    var moneyMax = new Map

    //无脑跟
    function task0311333() {

        // console.time("a")
        var liArr = document.querySelectorAll('#app li')
        //如果没有登录成功，不执行下面的代码
        if(document.URL != "https://coolco.vip/realfirm/24/24"){
            return
        }
        var resultNew = '';
        for (var i = 0; i < liArr.length; i++) {
            var li = liArr[i];

            var type = li.getElementsByTagName("h3")[0].innerText
            var count = li.getElementsByTagName("b")[3].innerText
            resultNew += type + "," + count + ","
        }

        if (resultOld != resultNew) {
            resultOld = resultNew;
            //网页第一次打开时不会执行
            if(!kaiShi) {
                // 这样的ajax调用不会等待返回结果，就会自动向下执行
                $.get("http://localhost:8186/genDan", {data: resultOld});
            }

        }
        //已经获取到了当前的状态，从第二次开始就该执行了
        kaiShi = false;
        alarm++;
        if (alarm >= 60) {
            diaoYongCount++;
            alarm = 0
            $.get("http://localhost:8186/jianKong", {data: resultOld, diaoYongCount:diaoYongCount, time:1});

        }

        if(alarm % 10 == 0){
            console.log(new Date())
        }

        if(diaoYongCount > 60) {
            if (isEmpty(resultOld)) {
                diaoYongCount=0;
                setInterval("location.reload()", 2000);
            }
        }
        // console.timeEnd("a")

    }

    function task0311333456() {

        // console.time("a")
        var liArr = document.querySelectorAll('#app li')
        //如果没有登录成功，不执行下面的代码
        if(document.URL != "https://coolco.vip/realfirm/24/24"){
            return
        }
        var resultNew = '';
        var resultNewTemp = '';
        for (var i = 0; i < liArr.length; i++) {
            var li = liArr[i];

            var type = li.getElementsByTagName("h3")[0].innerText
            var money = li.getElementsByTagName("b")[0].innerText
            var count = li.getElementsByTagName("b")[3].innerText

            type = type.split('USDT')[0] + 'USDT';

            if(typeStrTemp.indexOf(type) == -1){
                typeStrTemp.push(type)
            }


            resultNewTemp += type + "," + count + ","

            if(!moneyMin.has(type)){
                moneyMin.set(type, 10000)
            }
            if(!moneyMax.has(type)){
                moneyMax.set(type, -10000)
            }

            if(parseInt(money) < moneyMin.get(type)){
                moneyMin.set(type, parseInt(money))
            }

            if(parseInt(money) > moneyMax.get(type)){
                moneyMax.set(type, parseInt(money))
            }

            //老师赔的多时，再跟
            if(parseInt(money) < -1){
                if(typeStr.indexOf(type) == -1){
                   typeStr.push(type)
                }
            }

            //跟着老师做了，且挣得超过一定金额时，自动平仓
            if(typeStr.indexOf(type) != -1 && parseInt(money) > 280000){
                typeStr.splice(typeStr.indexOf(type), 1);
                $.get("http://localhost:8186/qingCang", {symbol: type});
				$.get("http://localhost:8190/sendEmail", {data:type + "这单挣了"});
            }

            if(typeStr.indexOf(type) != -1){
                resultNew += type + "," + count + ","
            }

        }
        
        if (resultOld != resultNew) {
            resultOld = resultNew;
            //网页第一次打开时不会执行
            if(!kaiShi) {
                // 这样的ajax调用不会等待返回结果，就会自动向下执行
                $.get("http://localhost:8186/genDan", {data: resultOld});
            }

            for (var j = 0; j < typeStr.length; j++) {
                //如果已经平仓了，那么这单结束
                if (resultOld.indexOf(typeStr[j]) == -1) {
                    //从有变成无时，删除
                    typeStr.splice(j, 1); // 将使后面的元素依次前移，数组长度减1
                    j--; // 如果不减，将漏掉一个元素

                }
            }


        }

        if (resultOldTemp != resultNewTemp) {
            resultOldTemp = resultNewTemp;
            
            for (var j = 0; j < typeStrTemp.length; j++) {
                //如果已经平仓了，那么这单结束
                if (resultOldTemp.indexOf(typeStrTemp[j]) == -1) {
                    //从有变成无时，通知
                    $.get("http://localhost:8190/sendEmail", {data:typeStrTemp[j] + "这单结束了。最多赔了" + moneyMin.get(typeStrTemp[j]) + "最多挣了" + moneyMax.get(typeStrTemp[j])});
                    moneyMin.delete(typeStrTemp[j])
                    moneyMax.delete(typeStrTemp[j])
                    
                    typeStrTemp.splice(j, 1); // 将使后面的元素依次前移，数组长度减1
                    j--; // 如果不减，将漏掉一个元素
                    
                }
            }
        }

        //已经获取到了当前的状态，从第二次开始就该执行了
        kaiShi = false;
        alarm++;
        if (alarm >= 60) {
            diaoYongCount++;
            alarm = 0
            $.get("http://localhost:8186/jianKong", {data: resultOld, diaoYongCount:diaoYongCount, time:1});
        }

        if(alarm % 10 == 0){
            console.log(new Date())
        }
        
        if(diaoYongCount > 60) {
            if (isEmpty(resultOldTemp)) {
                diaoYongCount=0;
				sleep1(2000, () => {
					location.reload()
				})

                // setInterval("location.reload()", 2000);
				// location.reload();
            }
        }
        // console.timeEnd("a")

    }
	
	function sleep1(ms, callback) {
        setTimeout(callback, ms)
    }
            //sleep 1s
            
    function cha(time2, time1) {
        var time21 = time2.getSeconds()*1000 + time2.getMilliseconds() - time1.getSeconds()*1000 - time1.getMilliseconds();
        return time21;
    }
    
    function isEmpty(obj) {
        if (typeof obj == "undefined" || obj == null || obj == "") {
            return true;
        } else {
            return false;
        }
    }

});

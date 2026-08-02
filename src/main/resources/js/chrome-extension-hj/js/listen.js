
$(function () {

    console.log("执行插件")
    $.get("http://localhost:9186/kaiShi", {data: 1});
    //js是单线程的，在队列里的方法只会一个个的执行
    // 每隔500毫秒在队列里放一下这个方法。如果js在运行其他的，那么会晚放。也就是如果上一个方法执行了600毫秒，或者其他的地方在使用js这个线程，那么会等js有空的时候再执行你这个方法，所以实际上，可能会大于等于500毫秒
    setInterval(task0311333, 500);
    var kaiShi = true;
    var resultOld = '';
    var alarm = 0;
    var diaoYongCount = 0;
    var time1;
    var time2;
    var time3;
    var time4;
    var time5;
    var time6;
    var time7 = new Date();
    var time8;

    function task0311333() {
        time1 = new Date();
        if(cha(time1, time7) > 600){
            $.get("http://localhost:9186/tongZhi", {data: "调用间隔时间为：" + cha(time1, time7)});
        }
        time7 = time1;
        // console.time("a")
        var liArr = document.querySelectorAll('#app li')
        //如果没有登录成功，不执行下面的代码
        if(document.URL != "https://coolco.vip/realfirm/47/57"){
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
            time3 = new Date();
            //网页第一次打开时不会执行
            if(!kaiShi) {
                // 这样的ajax调用不会等待返回结果，就会自动向下执行
                $.get("http://localhost:9186/genDan", {data: resultOld});
            }
            time4 = new Date();

            var time43 = cha(time4 , time3);
            if(time43 > 1000){
                console.log("跟单时间为：" + time43)
                $.get("http://localhost:9186/tongZhi", {data: "跟单时间为：" + time43});
            }
        }
        //已经获取到了当前的状态，从第二次开始就该执行了
        kaiShi = false;
        alarm++;
        if (alarm >= 60) {
            diaoYongCount++;
            alarm = 0
            time5 = new Date();
            $.get("http://localhost:9186/jianKong", {data: resultOld, diaoYongCount:diaoYongCount, time:time5});
            time6 = new Date();
            var time65 = cha(time6 , time5);
            if(time65 > 1000){
                console.log("监控时间为：" + time65)
                $.get("http://localhost:9186/tongZhi", {data: "监控时间为：" + time65});
            }
        }

        if(alarm % 10 == 0){
            console.log(new Date())
        }
        
        if(diaoYongCount > 60) {
            if (isEmpty(resultOld)) {
                diaoYongCount=0;
                location.reload();
            }
        }
        // console.timeEnd("a")
        time2 = new Date();
        var time21 = cha(time2 , time1);
		// console.log("循环时间为：" + time21)
        if(time21 > 600){
            console.log("执行时间为：" + time21)
            $.get("http://localhost:9186/tongZhi", {data: "执行时间为：" + time21});
        }
    }

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

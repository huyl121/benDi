
$(function () {

    console.log("执行插件")
    setInterval(task0311333, 500);

    var resultOld = '';
    var alarm = 0;
    var diaoYongCount = 0;
    var time1;
    var time2;
    var time3;
    var time4;
    var time5;
    var time6;

    function task0311333() {
        time1 = new Date();
        // console.time("a")
        var liArr = document.querySelectorAll('#app li')
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
            $.get("http://localhost:9186/genDan", {data: resultOld});
            time4 = new Date();

            var time43 = cha(time4 , time3);
            if(time43 > 1000){
                console.log("跟单时间为：" + time43)
                $.get("http://localhost:9186/tongZhi", {data: "跟单时间为：" + time43});
            }
        }
        alarm++;
        if (alarm >= 60) {
            diaoYongCount++;
            alarm = 0
            time5 = new Date();
            $.get("http://localhost:9186/jianKong", {data: resultOld});
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
        console.log("循环时间为：" + time21)
        var time21 = cha(time2 , time1);
        if(time21 > 2000){
            console.log("循环时间为：" + time21)
            $.get("http://localhost:9186/tongZhi", {data: "循环时间为：" + time21});
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

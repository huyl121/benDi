/**
 * Created by adimn on 2021/7/30.
 *
 * 进入redis目录 ，运行 redis-server.exe redis.windows.conf。
 * 默认端口：6379
 */

(function main(func) {
        const script = document.createElement('script');
        document.querySelector('head')?.appendChild(script);
        script.src = 'https://code.jquery.com/jquery-3.1.1.min.js';
        script.addEventListener('load', function () {
          func(jQuery);
        });
      })(function ($) {

        /*function loop() {
          task();
          setTimeout(loop, 300);
        }
        loop();*/

        setInterval(task, 500);

        var resultOld = '';
        var alarm = 0;
        var diaoYongCount = 0;
        var zongCount = 0;
        function task() {
            // console.time("a")
            var liArr = document.querySelectorAll('#app li')
            // var liArr = document.getElementById('app').getElementsByTagName('li');
            var resultNew = '';
            for(var i=0;i<liArr.length;i++){
                var li = liArr[i];

                var type = li.getElementsByTagName("h3")[0].innerText
                var count =li.getElementsByTagName("b")[3].innerText
                resultNew +=type+","+count+","
            }
            // $.get("http://localhost:8181/genDan", {data: resultOld});
            diaoYongCount++;
            if(diaoYongCount > 1000){
                diaoYongCount=0;
                zongCount++;
                console.log(zongCount)
            }
            if(resultOld != resultNew){
                resultOld = resultNew;
                $.get("http://localhost:9186/genDan", {data: resultOld});
            }
            alarm++;
            if(alarm >= 120){
                alarm = 0;
                // console.log('alarm')
                $.get("http://localhost:9186/jianKong", {data: resultOld});
            }
            // console.timeEnd("a")
        }
      });



function sleep(time) {
    i = 0;
    (function sleep1() {
        console.log(i);
        if(i===1) return;
        setTimeout(sleep1, 1000 * time);
        i++;
})()
}
var script = document.createElement('script');
script.src = "https://code.jquery.com/jquery-3.1.1.min.js";
document.getElementsByTagName('head')[0].appendChild(script);
sleep(3);

(function loop() {
    $.get("/test", {data: "John"});
    setTimeout(loop, 1000);
})();

var liArr = document.getElementById('app').getElementsByTagName('li');
var result = '';
for(var i=0;i<liArr.length;i++){
	var li = liArr[i];

	var type = li.getElementsByTagName("h3")[0].innerText
	var count =li.getElementsByTagName("b")[3].innerText
	result +=type+","+count+","
}
console.log(result)



var redis = require('redis')

var client = redis.createClient(6379, '127.0.0.1')
client.on('error', function (err) {
  console.log('Error ' + err);
});

// 1 键值对
client.set('color', 'red', redis.print);
client.get('color', function(err, value) {
  if (err) throw err;
  console.log('Got: ' + value)
  client.quit();
})
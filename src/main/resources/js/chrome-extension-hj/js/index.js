console.log("我被执行了！")

//alert("我被执行了！")

$("#openListen").click(e=>{
	window.open(chrome.extension.getURL("back.html"))
})

$("#callbackJs").click(e=>{
	var bg = chrome.extension.getBackgroundPage();
})

// alert("111");

var html = $(".justify-center").html();
console.log(html)

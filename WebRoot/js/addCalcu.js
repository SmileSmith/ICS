$(function(){
	var datatable = document.getElementById("datatable");
	initRem();
	init();
	tableTr();
});
window.onresize = init();

function init(){
	
	window.onresize = function(){
		initRem();		
	};
	//按钮样式切换
	$('#addLine').bind("mouseover ",function(){
		$(this).addClass("button-on");
	}).bind("mouseout",function(){
		$(this).removeClass("button-on");
	});
	$('#reset').bind("mouseover ",function(){
		$(this).addClass("button-on");
	}).bind("mouseout",function(){
		$(this).removeClass("button-on");
	});	
	$('#saveData').bind("mouseover ",function(){
		$(this).addClass("saveButton-on");
	}).bind("mouseout",function(){
		$(this).removeClass("saveButton-on");
	});		
}
function initRem(){
		document.getElementsByTagName('html')[0].style.fontSize = window.innerWidth/80 + 'px';	
}

function tableTr(){
	var tr = document.createElement('tr');
	var th=[];
	tr.id="title";
	for(var i=0;i<10;i++){
		th[i]=document.createElement('th');
	}
	th[0].setAttribute("id","sequence");
	th[0].innerHTML="序号";
	th[1].innerHTML="起算日期";
	th[2].innerHTML="截止日期";
	th[3].innerHTML="利率";
	th[4].innerHTML="本金";
	th[5].innerHTML="还款";
	th[6].innerHTML="还款抵扣利息";
	th[7].innerHTML="还款抵扣本金";
	th[8].innerHTML="剩余利息";
	th[9].innerHTML="剩余本金";
	for(var i=0;i<10;i++){
		tr.appendChild(th[i]);
	}
	datatable.appendChild(tr);
}

/**
 * 计算新增行数据
 */
function countData(){
	debugger;
	
	
	//获得输入信息  定义为JSON格式接收，传值到后台
	var data ='{';
	var _input = $("#send input[type='text']");
	for(var i = 0; i < _input.length; i++){
		data = data + '\"'+_input[i].id + '\":\"' + _input[i].value + '\",';
	}
	data = data +'\"rateType\":\"'+$("#rateType").val()+ '\"}';
	var jsonData =JSON.parse(data);
	$.ajax({
		  type:"POST",
	  	  url:"CalueLineServlet",
	  	  data:jsonData,
	  	  dataType:"json",
	  	  success:function(){alert("OK");},
	  	  error:function(){alert("NO");},
	});
	
	
	var tr = document.createElement('tr');
	var td=[];
	for(var i=0;i<10;i++){
		td[i]=document.createElement('td');
	}
	td[0].setAttribute("id","sequence");
	td[0].innerHTML=" 1";
	td[1].innerHTML=" 20140405";
	td[2].innerHTML=" 20160504";
	td[3].innerHTML=" 3%";
	td[4].innerHTML="11111111";
	td[5].innerHTML=" 35235235235";
	td[6].innerHTML="99999999.99";
	td[7].innerHTML=" 234234234";
	td[8].innerHTML=" 234234234";
	td[9].innerHTML=" 23423423";
	for(var i=0;i<10;i++){
		tr.appendChild(td[i]);
	}
	datatable.appendChild(tr);	
};

/**
 * 根据要求获得表格数据 并已JSON格式返回数据
 */
function tableValue(rowType){
	//开始取数据的行数  
	var start = 1;
	var alltr =  $("#datatable tr");
	var alltrNum = alltr.size();
	if(alltrNum == 0){
		return null;
	}
	if(rowType == 'beforeRow'){
		var relLineId = $("#relLineId").val();
		if(null != relLineId && "" != relLineId){
			var sequencetd = $("#datatable tr #sequence");
			var bfroreSequencetd; 
			for(var i = sequencetd.length-1; i >= 0; i--){
				var val = sequencetd[i].val();
				if(null != val && "" != val){
					bfroreSequencetd = val;
				}
				if(null != bfroreSequencetd && bfroreSequencetd == parseInt(val)+1){
					start = val;
				}
			}
		} else {
			start = alltrNum -1;
		}
		
		//获取一行数据里面有多少个td
		var tdNum =  $("#datatable tr").eq[0].find("td").size();
		
		//循环取出
		for(var i = start; i < alltrNum; i++){
			for(var j = 0; j < tdNum ; j++){
				//待续
			}
		}
	} 
};

/**
 * 校验日期和计算开始日期和结束日期之间的天数
 */
function countDays(){
	debugger;
	var startDate = $("#startDate").val().trim();
	var endDate = $("#endDate").val().trim();
	
	//对日期进行校验
	if(null == startDate || "" == startDate || null == endDate || "" == endDate){
		return ;
	} else if (null != startDate && (startDate < 19000101 || startDate>99999999)){
		alert("请输入正确的开始日期");
		return ;
	} else if (null != endDate &&  (endDate < 19000101 || endDate>99999999)){
		alert("请输入正确的结束日期");
		return ;
	} else if (endDate <= startDate){
		alert("结束时间必须大于开始时间！");
		return ;
	}
	
	//转化为日期格式
	var start = new Date(startDate.substring(0, 4) + "'" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
	var end = new Date(endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8));
	//计算天数，然后赋值
	$("#days").val(Math.round((end.getTime() - start.getTime())/(1000*3600*24)));
};

function getCaseNo(){
	$('#caseNo').val($('#inpuCaseNo').val());
	$('div[class^=alertCase]').css("display","none");
}
function saveCal(){
	//TODO
}

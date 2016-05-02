var showFlag=true;
var relativeFlag=false;
var insertFlag=false;
var delFlag=false;

$(function(){
	initTableTh();
	//网址上有案件号参数时，不为新增案件  不需要弹出新增框
	var calId = getUrlParam("calId");
	var caseNo = getUrlParam("caseNo");
	if(calId != null && calId != ""){
		//查询场景
		$("#calId").val(calId);
		$("#caseNo").val(caseNo);
		$(".alertCaseBg, .alertCaseDivBorder, .alertCaseDiv").hide();
		//加载表格数据
		findCalBycalId(calId);
	} else {
		//新建场景
		initInputTr();
		resetInput();
		relativeFlag=false;
		insertFlag=false;
	}
	
	initPageStyle();
	
	
});


function initTableTh(){
	var tr = $('<tr>');
	var th = new Array();
	tr.attr({"class":"title","id":"tableTitle"});
	for(var i=0;i<13;i++){
		th[i]=$('<th>');
	}
	th[0].html("序号").attr("class","id").attr("name","sequence");
	th[1].html("起算日期").attr("class","sDate").attr("name","startDate");
	th[2].html("截止日期").attr("class","eDate").attr("name","endDate");
	th[3].html("本金").attr("class","principal").attr("name","principal");
	th[4].html("天数").attr("class","days").attr("name","days");
	th[5].html("四倍同期利率").attr("class","rate").attr("name","rate");
	th[6].html("还款").attr("class","repayment").attr("name","repayment");
	th[7].html("利息").attr("class","interest").attr("name","interest");
	th[8].html("还款抵扣利息").attr("class","intReduce").attr("name","intRepay");
	th[9].html("还款抵扣本金").attr("class","priReduce").attr("name","priRepay");
	th[10].html("剩余利息").attr("class","intResidual").attr("name","intResidual");
	th[11].html("剩余本金").attr("class","priResidual").attr("name","priResidual");
	th[12].html("操作").attr("class","operation");
	for(var i=0;i<13;i++){
		tr.append(th[i]);
	}
	$("#dataTable").append(tr);
}

function initInputTr(index){
	if (typeof(index)=='undefined' || null ==index || '' ==index)index=0;
	var tr = $('<tr>',{"id":"line"+index,"class":"line inputLine"});
	var lineObject = lineFactory();
	lineObject.sequence = -1;
	lineObject.index = index;
	lineObject.rateType = $('#rateType').val();
	tr = convertObjctToElement(lineObject, tr);
	
	if(index>getLastIndex()){
		//下方新增输入行		
		$("#dataTable").append(tr);
	} else {
		//中间插入行
		$("#dataTable tr[id=line"+index+"]").before(tr);
	}
	
}


/**
 * 计算数据（新增）
 */
function countData(){
	$('.inputLine').find('td[name=startDate]').html($('#startDate').val());
	$('.inputLine').find('td[name=endDate]').html($('#endDate').val());
	$('.inputLine').find('td[name=days]').html($('#days').val());
	$('.inputLine').find('td[name=rate]').html($('#rate').val());
	$('.inputLine').find('td[name=principal]').html($('#principal').val());
	$('.inputLine').find('td[name=repayment]').html($('#repayment').val());
	
	
	if(insertFlag||delFlag){
		countDataForInsert();
		return;
	}
	var lineList = [];
	//取表格区域的数据组成对象（包括输入行）
	var trElementList = $('#dataTable').find('.line');
	//表格有数据库
	if(trElementList.length > 1){
		
		if(!relativeFlag){
			//取最近主行
			var lastPrimaryIndex = getLastPrimaryIndex();		
			var lastPrimaryTr = $("#line"+lastPrimaryIndex);
			
			var lastPrimaryLine =  convertElementToObjct(lastPrimaryTr,lineFactory());
			lineList.push(lastPrimaryLine);	
			lineList.unshift(convertElementToObjct($('.inputLine'),lineFactory()));	
		}else{			
			//取最近主行后面的所有行
			var i;
			for(i=trElementList.length-1;i>=0;i--){
				var tr = $(trElementList[i]);
				lineList.push(convertElementToObjct($(tr),lineFactory()));
				if(tr.find('#sequence').html() != "0")break;
			}
			//加上最近主行再前面主行后面的所有数据，取剩余本金要用
			if(i>=1){
				for(var j=i-1;j>=0;j--){
					var tr = $(trElementList[j]);
					lineList.push(convertElementToObjct($(tr),lineFactory()));
					if(tr.find('#sequence').html() != "0")break;
				}
			}
		}
	} else {
		//输入行放在最前面
		lineList.unshift(convertElementToObjct($('.inputLine'),lineFactory()));		
	}
	

	
	var jsonData = JSON.stringify(lineList);
	var realtiveFlagStr = "";
	if(relativeFlag){
		realtiveFlagStr = "realtiveByPricipal";		
	}
	$.ajax({
			async:false,
			type:"POST",
			url:"CalueLineServlet",
			data:{"param":jsonData,"realtiveFlag":realtiveFlagStr},
			dataType:"json",
			success:function(list){
				//删掉输入行
				$('.inputLine').remove();
				
				if(!relativeFlag){
					//非关联行
					addLine(list[0]);
					$("#principal").val($("#line"+getLastIndex()).find('td[name=priResidual]').html());	
			
				} else if(relativeFlag&&list.length>1){
					//关联行
					var pageLastIndex = getLastIndex();
					//要删掉的行数
					var deleteNum = list.length-1;
					
					//从下往上删掉
					for(var i= pageLastIndex; i> pageLastIndex-deleteNum;i--){
						$('#line' + i).remove();
					}
					//从上往下新增
					for(var i= list.length-1; i>=0;i--){
						addLine(list[i]);
					}

					$("#principal").val($("#line"+getLastPrimaryIndex()).find('td[name=priResidual]').html());
					$('#repayment').removeAttr("disabled");
				}
				//加入输入行
				initInputTr(getLastIndex()+1);
				resetInput();
				

				//重置关联标识
				relativeFlag=false;
				insertFlag=false;
				//页面滚动
				$('html, body, .content').animate({scrollTop: $('html').height()}, 300); 				
			},
	  	  error:function(){console.log("NO");},
	});

	
};


/**
 * 计算数据（插入）
 *//*
function countDataForInsert(){
	
	var operateMode ="";
	
	if(confirm("是否所有本金用前一行剩余本金替换?")) {
		operateMode ="replace";
	} else {
		operateMode ="compare";
	}
	
	var lineList = new Array();
	var inputIndex = getInputIndex();//插入行
	
	//取输入区域的数据组成对象
	var sendFormObject = $('#send').find('input,select');
	var inputLine = new lineFactory();
	$.each(sendFormObject,function(i,item){
		var name = $(item).attr("name");
		for (prop in inputLine){ 
			if(name == prop){
				inputLine[prop] = $(item).val();
			} 
		}
	});
	
	//取表格区域的数据组成对象
	var trElementList = $('#dataTable').find('.line');
	$.each(trElementList,function(i,tr){
		lineList.push(convertElementToObjct($(tr),lineFactory()));
	});
	
	var beforePriIndex = -1;
	
	//获取插入行的主行位置或者前一条主行信息
	for(var i = inputIndex-1; i >= 0; i--){
		if(lineList[i].sequence != 0 || lineList[i].sequence !="0"){
			beforePriIndex = i;
			break;
		}
	}
	//改变输入行的sequence 
	if(lineList[inputIndex].sequence != 0){
		lineList[inputIndex].sequence = -1 ;
	}
	
	lineList = lineList.slice(beforePriIndex);
 
	var jsonData = JSON.stringify(lineList);

	$.ajax({
		type:"POST",
		url:"OpeCalueLineServlet",
	    data:{"param":jsonData,"operateMode":operateMode,"beforePriIndex":beforePriIndex},
	    dataType:"json",
	    async:false,
	    success:function(data){
	    	var list = data;
	    	$('#dataTable').find('.line').remove();
			//从上往下新增
			for(var i= 0;i< list.length; i++){
				addLine(list[i]);
			}
			$('#repayment').removeAttr("disabled");
			//加入输入行
			initInputTr(getLastIndex()+1);
			resetInput();
			//重置关联标识
			relativeFlag=false;
			insertFlag=false;
			//页面滚动
			$('html, body, .content').animate({scrollTop: $('html').height()}, 300); 	
		    },
	    error:function(){
	    	alter("no");
	    	}
	});
}*/
/**
 * 计算数据（插入）
 */
function countDataForInsert(){  
	var lineList = new Array();
	
	//取表格区域的数据组成对象
	var trElementList = $('#dataTable').find('.line');
	$.each(trElementList,function(i,tr){
		lineList.push(convertElementToObjct($(tr),lineFactory()));
	});
	
	if(!delFlag){
		var inputIndex = getInputIndex();//插入行
		//改变输入行的sequence 
		if(lineList[inputIndex].sequence != 0){
			lineList[inputIndex].sequence = -1 ;
		}		
	}


	var jsonData = JSON.stringify(lineList);
	$.ajax({
		type:"POST",
		url:"OpeCalueLineServlet",
	    data:{"param":jsonData,"type":"Add"},
	    dataType:"json",
	    async:false,
	    success:function(data){
	    	var list = data;
	    	$('#dataTable').find('.line').remove();
			//从上往下新增
			for(var i= 0;i< list.length; i++){
				addLine(list[i]);
			}
			$('#repayment').removeAttr("disabled");
			$("#principal").removeAttr("disabled");
			//加入输入行
			initInputTr(getLastIndex()+1);
			resetInput();
			//重置关联标识
			relativeFlag=false;
			insertFlag=false;
			//页面滚动
			$('html, body, .content').animate({scrollTop: $('html').height()}, 300); 	
		    },
	    error:function(){alter("no");}
	});
} 
 
/**
 * 校验日期和计算开始日期和结束日期之间的天数
 */

function countDays(){
	var startDate = $("#startDate").val().trim();
	var endDate = $("#endDate").val().trim();
	
	//YYYYMMDD正则表达式  区分是否闰年
	var regyyyyMMdd = /(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)/;
	
	//对日期进行校验
	if(null == startDate || "" == startDate || null == endDate || "" == endDate){
		return ;
	} else if (null != startDate && !regyyyyMMdd.test(startDate)){
		alert("请输入正确的开始日期");
		return ;
	} else if (null != endDate &&  !regyyyyMMdd.test(endDate)){
		alert("请输入正确的结束日期");
		return ;
	} else if (endDate <= startDate){
		alert("结束时间必须大于开始时间！");
		return ;
	}
	
	//转化为日期格式
	var start = new Date(startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
	var end = new Date(endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8));
	//计算天数，然后赋值
	$("#days").val(Math.round((end.getTime() - start.getTime())/(1000*3600*24)));
};

/**
 * 根据案件ID查询已经保存的数据
 */
function findCalBycalId(calId){
	$.ajax({
		type:"GET",
		url:"FindCalueLineServlet",
	    data:{"calId":calId},
	    dataType:"json",
	    async:false,
	    success:function initTable(data){
			if(data!= null){
				for(var i = 0 ;i < data.length; i++ ){
					//TODO 
					addLine(data[i]);

				}

				var lastLine = data[data.length -1];
				initInputTr(lastLine.index +1);
				resetInput();
				relativeFlag=false;
				insertFlag=false;
			
			}
		},
	    error:function(){alert(查询失败);}
	});
};

/**
 * 保存所有数据
 */
function saveCal(){
	var lineList = new Array();
	
	//取表格区域的数据组成对象
	var trElementList = $('#dataTable').find('tr[class=line]');	
	$.each(trElementList,function(i,tr){
		lineList.unshift(convertElementToObjct($(tr),lineFactory()));
	});
	
	var jsonData =JSON.stringify(lineList);
	var calId = $("#calId").val();
	var caseId = $("#caseNo").val();
	
	$.ajax({
			async:true,
			type:"POST",
			//TODO
			url:"SaveCalueLineServlet",
			data:{"param":jsonData,"calId":calId,"caseId":caseId},
			success:function(data){
				if(!data){
					history.go(-1);
					console.log("save success....");					
				} else {
					
				}

			},
	  	  error:function(){console.log("save error....");},
	});	
	
}


/**
 * 重置
 */
function resetInput(){

	//取输入行的上一行
	var $inputBeforeLine = $('#line'+(getInputIndex()-1));
	//去输入行前面的主行
	var $inputPrimaryLine = $('#line'+getInputPrimaryIndex());
	
	//取输入行的下一行
	var $inputNextLine  = $('#line'+(getInputIndex()+1));
	
	//输入区域数据处理
	
	if(0 == $inputBeforeLine.length){
		$("#startDate").val("");
		$("#rate").val($inputNextLine.find('td[name=rate]').html());
		$("#rateType").val($inputNextLine.find('td[name=rateType]').html());
		
		$(".inputLine td[name=startDate]").html("");
		$(".inputLine td[name=rate]").html($('#rate').val());
		$(".inputLine td[name=rateType]").html($('#rateType').val());		
	} else {
		var beforeEndDate = $inputBeforeLine.find('td[name=endDate]').html();
		$("#startDate").val(afterOneDay(beforeEndDate)); 
		$("#rate").val($inputBeforeLine.find('td[name=rate]').html());
		$("#rateType").val($inputBeforeLine.find('td[name=rateType]').html());
		
		$(".inputLine td[name=startDate]").html(afterOneDay(beforeEndDate));
		$(".inputLine td[name=rate]").html($('#rate').val());
		$(".inputLine td[name=rateType]").html($('#rateType').val());
	}
	
	
	if (!relativeFlag){
		//插入主行的场景
		$('#repayment').removeAttr("disabled");
		$("#principal").val($inputPrimaryLine.find('td[name=priResidual]').html());
		$(".inputLine td[name=principal]").html($('#principal').val());
	} 
	
	$('#endDate, #days, #repayment').val("");

	$('#startDate').focus();
	

};


/**
 * 计算后一天的日期返回
 */
function afterOneDay(dateString){
	var date = new Date((new Date(dateString.substring(0, 4) + "-" + dateString.substring(4, 6) + "-" + dateString.substring(6, 8))/1000+86400)*1000);
	return date.getFullYear()+""+(date.getMonth()+1>=10?date.getMonth()+1:"0"+(date.getMonth()+1))+""+(date.getDate()<10?"0"+date.getDate():date.getDate()); 
}


/**
 * 获取案件号并更新样式
 */
function getCaseNo(){
	$('#caseNo').val($('#inpuCaseNo').val());
	$('div[class^=alertCase]').css("display","none");
	$('#startDate').focus();
}





/* *****各种公用的功能函数 ***** */


/**
 * 计算后新增一行
 * param lineObject
 */
function addLine(lineObject){
	var tr = $('<tr>',{"id":"line"+lineObject.index,"class":"line"});
	tr = convertObjctToElement(lineObject, tr);
	$("#dataTable").append(tr);	
}


/**
 * 行对象转行元素
 * param lineObject
 * return tr
 */
function convertObjctToElement(lineObject, lineTrElement){
	var td = new Array();
	for(var i=0;i<17;i++){
		td[i]=$('<td>');
	}

	td[0].html(lineObject.sequence).attr({"class":"sequence","id":"sequence","name":"sequence"});
	td[1].html(lineObject.startDate).attr({"class":"startDate","name":"startDate"});
	td[2].html(lineObject.endDate).attr({"class":"endDate","name":"endDate"});
	td[3].html(lineObject.principal).attr({"class":"principal","name":"principal"});
	td[4].html(lineObject.days).attr({"class":"days","name":"days"});
	td[5].html(lineObject.rate).attr({"class":"rate","name":"rate"});
	td[6].html(lineObject.repayment).attr({"class":"repayment","name":"repayment"});
	td[7].html(lineObject.interest).attr({"class":"interest","name":"interest"});
	td[8].html(lineObject.intRepay).attr({"class":"intRepay","name":"intRepay"});
	td[9].html(lineObject.priRepay).attr({"class":"priRepay","name":"priRepay"});
	td[10].html(lineObject.intResidual).attr({"class":"intResidual","name":"intResidual"});
	td[11].html(lineObject.priResidual).attr({"class":"priResidual","name":"priResidual"});
	td[12].html("<button id='add" + lineObject.index +"' onclick='setInsert(this.id)'>插入</button>" 
				+ "<button id='del" + lineObject.index +"' onclick='setDelLine(this.id)'>删除</button>").attr("class","operation");
	td[13].html(lineObject.index).attr({"style":"display:none","name":"index"});
	td[14].html(lineObject.lineId).attr({"style":"display:none","name":"lineId"});
	td[15].html(lineObject.prilineid).attr({"style":"display:none","name":"prilineid"});
	td[16].html(lineObject.rateType).attr({"style":"display:none","name":"rateType"});
	if(lineObject.sequence == 0 || lineObject.sequence == "0"){
		//关联行跨行的行数
		var rowspan = getLastIndex() - getLastPrimaryIndex() + 2;
		
		td[0].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=sequence]').attr("rowspan",rowspan);
		
		td[6].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=repayment]').attr("rowspan",rowspan);

		td[8].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=intRepay]').attr("rowspan",rowspan);

		td[9].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=priRepay]').attr("rowspan",rowspan);

		td[10].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=intResidual]').attr("rowspan",rowspan);

		td[11].css("display","none");
		$('#line' + getLastPrimaryIndex()).find('td[name=priResidual]').attr("rowspan",rowspan);		
		
		//本金为0 也跨行
		if(lineObject.principal == 0 || lineObject.principal == "0"){
			td[3].css("display","none");
			$('#line' + getLastPrimaryIndex()).find('td[name=principal]').attr("rowspan",rowspan);			
		}
		
	} else if(lineObject.sequence == -1|| lineObject.sequence == "-1") {
		td[0].html("1");
		td[12].html("");
		if(lineObject.index != 0){
			td[0].html("<button id='rel" + lineObject.index +"' onclick='setRealtive();'>关联</button>");
		}
		
		if(lineObject.index <= getLastIndex()){
			//中间插入行
			td[12].html("<button id='del" + lineObject.index +"' onclick='setDelLine(this.id)'>删除</button>");
		}		
	}
	
	for(var i=0;i<17;i++){
		lineTrElement.append(td[i]);
	}
	return lineTrElement;
}

/**
 * 行元素转行对象
 * param lineTrElement
 * return lineObject
 */
function convertElementToObjct(lineTrElement, lineObject){
	var lineTdElements = lineTrElement.find('td');
	$.each(lineTdElements,function(i,item){
		var name = $(item).attr("name");
		for (prop in lineObject){ 
			if(name == prop){
				lineObject[prop] = $(item).html();
			} 
		} 		
	});	
	return lineObject;
}

/**
 * 行对象工厂
 */
function lineFactory(){
	var line = new Object();
	line.lineId ="";
	line.prilineid ="";	
	line.index ="";
	
	line.sequence ="";
	line.startDate ="";
	line.endDate ="";
	line.principal ="";
	line.days ="";
	line.rate ="";
	line.repayment ="";
	line.interest ="";
	line.intRepay ="";
	line.priRepay ="";
	line.intResidual ="";
	line.priResidual ="";
	
	line.rateType ="";
	
	return line;
}

 
/**
 * 获取最后一行行号（不包括输入行）
 */
function getLastIndex(){
	var trList = $("#dataTable").find('tr[class=line]');
	var index = -1;
	if(trList.length>0){
		var indexStr = $(trList[trList.length-1]).find('td[name=index]').html();
		index = Number(indexStr);
	}
	return index ;
}

/**
 * 获取最近主行行号（不包括输入行）
 */
function getLastPrimaryIndex(){
	var trList = $("#dataTable").find('tr[class=line]');
	var index = -1;
	if(trList.length>0){
		for(var i=trList.length-1;i>=0;i--){
			if($(trList[i]).find("#sequence").html() != "0"){
				var indexStr = $(trList[i]).find('td[name=index]').html();
				index = Number(indexStr);
				break;
			}	
		}
	}
	return index;
}

/**
 * 获取输入行的前一主行行号
 */
function getInputPrimaryIndex(){
	
	var inputLineIndex = getInputIndex();
	var inputPrimaryIndex = -1;
	
	var trElementList = $('#dataTable').find('tr[class=line]');	
	for(var i=inputLineIndex-1;i>=0;i--){
		if($(trElementList[i]).find("#sequence").html() != "0"){
			inputPrimaryIndex = $(trElementList[i]).find("td[name=index]").html();
			break;
		}
	}
	return Number(inputPrimaryIndex);
}

/**
 * 获取输入行行号
 */
function getInputIndex(){
	var inputLineIndex = -1;
	var $inputLine = $('.inputLine'); 
	inputLineIndex = $('.inputLine td[name=index]').html();
	return Number(inputLineIndex);
}


/**
 * 设置关联标识
 */
function setRealtive(){
	if(!relativeFlag){
		relativeFlag=true;
		$('#principal').val("");
		$('#repayment').val("").attr("disabled","disabled");
		
		var $inputLine = $('.inputLine'); 
		var inputLineIndex = getInputIndex();
		var inputPrimaryIndex = getInputPrimaryIndex();

		
		var rowspan = inputLineIndex - inputPrimaryIndex + 1;
			
		$inputLine.find('#sequence').css("display","none");
		$inputLine.find('#sequence').html(0);
		$('#line' + inputPrimaryIndex).find('td[name=sequence]').attr("rowspan",rowspan);
		
		$('.operation button:not("#del'+inputLineIndex+'")').attr("disabled","disabled");
		
		
		if(inputLineIndex>getLastIndex()){
			//输入不行为插入行，后面行的sequence更新
			var trElementList = $('#dataTable').find('tr[class=line]');
			var $tr;
			for(var i=inputLineIndex;i<trElementList.length;i++){
				$tr = $(trElementList[i]);
				var newIndex = Number($tr.find('td[name=index]').html())+1;
				//sequence不为0的行需要-1
				var newSequence = Number($tr.find('td[name=sequence]').html())-1;
				if(newSequence>=0)$tr.find('td[name=sequence]').html(newSequence);
			}			
		}		
	} else {
		//TODO 再次点击取消关联
	}
	
	$('#startDate').focus();
};

/**
 * 设置插入标识
 */
function setInsert(btnid){
	if(!insertFlag){
		
		insertFlag = true;
		var inputLineIndex = Number(btnid.substring(3,btnid.length));
		//默认插入主行
		var rel = false;
		//关联行判断
		if ($('#line'+inputLineIndex).find('td[name=sequence]').html()=="0"){
			rel = true;
		}
		
		$('.inputLine').remove();
		initInputTr(Number(inputLineIndex));
		
		var inputPrimaryIndex = getInputPrimaryIndex();
		
		//后面行的index更新
		var trElementList = $('#dataTable').find('tr[class=line]');
		var $tr;
		var newIndex;
		var newSequence;
		
		for(var i=inputLineIndex;i<trElementList.length;i++){
			$tr = $(trElementList[i]);
			newIndex = Number($tr.find('td[name=index]').html())+1;
			$tr.attr("id","line"+newIndex);
			$tr.find('[id^=del]').attr("id","del"+newIndex);
			$tr.find('[id^=add]').attr("id","add"+newIndex);
			$tr.find('td[name=index]').html(newIndex);
			//插入主行，sequence要更新
			if(!rel){
				if($tr.find('td[name=sequence]').html()!="0"){
					newSequence = Number($tr.find('td[name=sequence]').html())+1;
					$tr.find('td[name=sequence]').html(newSequence);			
				}
			}
		}	

		$('.operation button:not("#del'+inputLineIndex+'")').attr("disabled","disabled");	
		
		if(rel){
			setRealtive();
			//插入行为关联行的情况
			var newRowSpan = Number($("#line"+inputPrimaryIndex).find('td[name=sequence]').attr("rowspan"))+1;
			
			$("#line"+inputLineIndex).find('td[name=sequence]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=sequence]').attr("rowspan",newRowSpan);
			
			$("#line"+inputLineIndex).find('td[name=repayment]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=repayment]').attr("rowspan",newRowSpan);
			
			$("#line"+inputLineIndex).find('td[name=intRepay]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=intRepay]').attr("rowspan",newRowSpan);
			
			$("#line"+inputLineIndex).find('td[name=priRepay]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=priRepay]').attr("rowspan",newRowSpan);
			
			$("#line"+inputLineIndex).find('td[name=intResidual]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=intResidual]').attr("rowspan",newRowSpan);
			
			$("#line"+inputLineIndex).find('td[name=priResidual]').css("display","none");
			$("#line"+inputPrimaryIndex).find('td[name=priResidual]').attr("rowspan",newRowSpan);
			
			//判断是哪种类型的关联，本金要不要隐藏
			if($("#line"+(inputLineIndex+1)).find('td[name=principal]').html() == "0" ||
					$("#line"+(inputLineIndex+1)).find('td[name=principal]').html() == ""){
				$("#line"+inputLineIndex).find('td[name=principal]').css("display","none");
				$("#line"+inputPrimaryIndex).find('td[name=principal]').attr("rowspan",newRowSpan);
				$("#principal").attr("disabled","disabled");
			}
			
		}		
	}
	
	resetInput();
}

/**
 * 设置删除
 * 
 */
function setDelLine(btnid){
	var delLineIndex = Number(btnid.substring(3,btnid.length));//删除行
	var inputLineIndex = getInputIndex();//输入行
	var lastLineIndex = getLastIndex();//最后一行
	var delPriLineIndex;//删除行的前一主行
	var rel = false;
	
	if(delLineIndex == inputLineIndex){
		//删除的是插入行，处理删除行后面sequence和index，并在最下方初始化输入行
		var inputPrimaryIndex = getInputPrimaryIndex();
		$('#line'+delLineIndex).remove();

		
		if ($('#line'+(inputLineIndex+1)).find('td[name=sequence]').html()=="0"){
			//删除的是关联行
			rel = true;
		}		
		
		//inputLineIndex后面行的index更新
		var trElementList = $('#dataTable').find('tr[class=line]');
		var $tr;
		var newIndex;
		var newSequence;
		
		for(var i=inputLineIndex;i<trElementList.length;i++){
			$tr = $(trElementList[i]);
			newIndex = Number($tr.find('td[name=index]').html())-1;
			$tr.attr("id","line"+newIndex);
			$tr.find('[id^=del]').attr("id","del"+newIndex);
			$tr.find('[id^=add]').attr("id","add"+newIndex);
			$tr.find('td[name=index]').html(newIndex);
			//删除的是插入主行，sequence要更新
			if(!rel){
				if($tr.find('td[name=sequence]').html()!="0"){
					newSequence = Number($tr.find('td[name=sequence]').html())-1;
					$tr.find('td[name=sequence]').html(newSequence);			
				}
			}
		}			

		if(rel){
			//删除的是插入关联行，对应主行的跨行要更新
			var newRowSpan = Number($("#line"+inputPrimaryIndex).find('td[name=sequence]').attr("rowspan"))-1;
			$("#line"+inputPrimaryIndex).find('td[name=sequence]').attr("rowspan",newRowSpan);
			$("#line"+inputPrimaryIndex).find('td[name=repayment]').attr("rowspan",newRowSpan);
			$("#line"+inputPrimaryIndex).find('td[name=intRepay]').attr("rowspan",newRowSpan);
			$("#line"+inputPrimaryIndex).find('td[name=priRepay]').attr("rowspan",newRowSpan);
			$("#line"+inputPrimaryIndex).find('td[name=intResidual]').attr("rowspan",newRowSpan);
			$("#line"+inputPrimaryIndex).find('td[name=priResidual]').attr("rowspan",newRowSpan);
			
			//判断是哪种类型的关联，本金要不要隐藏
			if($("#line"+inputLineIndex).find('td[name=principal]').html() == "0" ||
					$("#line"+inputLineIndex).find('td[name=principal]').html() == ""){
				$("#line"+inputPrimaryIndex).find('td[name=principal]').attr("rowspan",newRowSpan);
			}
			
		}		
		
	} else {
		
		//删除的是数据行，要判断删除的是否是关联行
		if ($('#line'+delLineIndex).find('td[name=sequence]').html()=="0"){
			//删除的是关联行
			rel = true;
		}
		
		var trElementList = $('#dataTable').find('tr[class=line]');
		var $tr;
		var newIndex;
		var newSequence;
		var delPrimaryIndex;
		var delLineNum = 1;

		//取删除上的前一主行
		for(var i= delLineIndex;i>=0;i--){
			if($(trElementList[i]).find('td[name=sequence]').html()!="0"){
				delPrimaryIndex = i;
				break;
			}
		}

		//取删除上的后一主行
		if(!rel){
			var delAfterPrimaryIndex;
			for(var i= delLineIndex+1;i<trElementList.length;i++){
				if($(trElementList[i]).find('td[name=sequence]').html()!="0"){
					delAfterPrimaryIndex = i;
					delLineNum = delAfterPrimaryIndex - delLineIndex;
					break;
				}
			}			
		} 
		
		if(rel){
			//删除的是插入关联行，对应主行的跨行要更新
			
			var newRowSpan = Number($("#line"+delPrimaryIndex).find('td[name=sequence]').attr("rowspan"))-1;
			$("#line"+delPrimaryIndex).find('td[name=sequence]').attr("rowspan",newRowSpan);
			$("#line"+delPrimaryIndex).find('td[name=repayment]').attr("rowspan",newRowSpan);
			$("#line"+delPrimaryIndex).find('td[name=intRepay]').attr("rowspan",newRowSpan);
			$("#line"+delPrimaryIndex).find('td[name=priRepay]').attr("rowspan",newRowSpan);
			$("#line"+delPrimaryIndex).find('td[name=intResidual]').attr("rowspan",newRowSpan);
			$("#line"+delPrimaryIndex).find('td[name=priResidual]').attr("rowspan",newRowSpan);
			
			//判断是哪种类型的关联，本金要不要修改跨行
			if($("#line"+delLineIndex).find('td[name=principal]').html() == "0" ||
					$("#line"+delLineIndex).find('td[name=principal]').html() == ""){
				$("#line"+delPrimaryIndex).find('td[name=principal]').attr("rowspan",newRowSpan);
			} 
			//删除该行和下方关联的行
		} 
		
		
		//删除行
		if(rel){
			//删除行
			$('#line'+delLineIndex).remove();	

		} else {
			for(var i= delLineIndex+1;i<trElementList.length;i++){
				if($('#line'+i).find('td[name=sequence]').html() != "0")break;
				$("#line"+i).remove();
			}
			$('#line'+delLineIndex).remove();
		}		
		
		
		//后面行的index更新
		for(var i= delLineIndex;i<trElementList.length;i++){

			$tr = $(trElementList[i]);
			newIndex = Number($tr.find('td[name=index]').html())-delLineNum;
			$tr.attr("id","line"+newIndex);
			$tr.find('[id^=del]').attr("id","del"+newIndex);
			$tr.find('[id^=add]').attr("id","add"+newIndex);
			$tr.find('td[name=index]').html(newIndex);
			//删除的是插入主行，sequence要更新
			if(!rel){
				if($tr.find('td[name=sequence]').html()!="0"){
					newSequence = Number($tr.find('td[name=sequence]').html())-1;
					$tr.find('td[name=sequence]').html(newSequence);
				}
			}
		}			
		$('.inputLine').remove();
	}
	delFlag = true;
	//直接计算 
	countData();
	
/*	relativeFlag=false;
	insertFlag=false;
	initInputTr(getLastIndex()+1);
	resetInput();
	//解锁所有按钮
	$('.operation button').removeAttr("disabled");*/
	delFlag = false;
	
	
}



/* *****各种公用的功能函数 End***** */





/* *****各种辅助函数***** */


//屏幕滚动时不展示输入框
var scrolling = false; 
$(window).scroll(function(){
	if(showFlag){
		$("#input").css("visibility","hidden");
		if (scrolling){
			clearTimeout(scrolling);
		} 
		scrolling = setTimeout(function(){ 
			$("#input").css("visibility","visible");
		},100000);		
	}
});
//鼠标移入窗体内展示输入框
$('html').mouseover(function(){
	if(showFlag)$("#input").css("visibility","visible");
});

//窗体大小改变事件
window.onresize = initRem();


//页面样式初始化
function initPageStyle(){
	window.onresize = function(){
		initRem();		
	};

	
	//橙色按钮样式切换
	$('#addButton, #reset, #getCaseNo, #back').bind("mouseover ",function(){
		$(this).addClass("button-on");
	}).bind("mouseout",function(){
		$(this).removeClass("button-on");
	});

	//绿色
	$('#saveButton').bind("mouseover ",function(){
		$(this).addClass("saveButton-on");
	}).bind("mouseout",function(){
		$(this).removeClass("saveButton-on");
	});
	
	$('#showButton').bind("click",function(){
		if(!showFlag){
			$('#input').css("visibility","visible");
			$('#showButton').find('label').html("<");				
			showFlag=true;
			$('#data').removeClass('dataAllScreen');
			$('th').removeClass('thAllScreen');
			$('.operation').show();
			$('.inputLine').show();
		} else if(showFlag){
			$('#input').css("visibility","hidden");
			$('#showButton').find('label').html(">");			
			showFlag=false;

			$('#data').addClass('dataAllScreen');
			$('th').addClass('thAllScreen');
			$('.operation').hide();
			$('.inputLine').hide();
		}
		
	});
	
	/**
	 * 输入区域数据绑定事件
	 */
	$('#startDate').bind('keyup blur',function(){
		$('.inputLine td[name=startDate]').html($(this).val());
	});
	$('#endDate').bind('keyup blur',function(){
		$('.inputLine td[name=endDate]').html($(this).val());
	});	
	$('#days').bind('keyup blur',function(){
		$('.inputLine td[name=days]').html($(this).val());
	});
	$('#rate').bind('keyup blur',function(){
		$('.inputLine td[name=rate]').html($(this).val());
	});
	$('#rateType').bind('change',function(){
		$('.inputLine td[name=rateType]').html($(this).val());
		$('#tableTitle').find('th[name=rate]').html($(this).find('option:selected').html());
	});	
	$('#principal').bind('keyup blur',function(){
		$('.inputLine td[name=principal]').html($(this).val());
	});
	$('#repayment').bind('keyup blur',function(){
		$('.inputLine td[name=repayment]').html($(this).val());
	});
	
	/**
	 * 回车绑定事件
	 */  
	$('#repayment, #principal, #rate').bind('keypress',function(e) {
		if(e.which == 13) { $("#addButton").click(); }  
	}); 
	$('#inpuCaseNo').bind('keypress',function(e) {
		if(e.which == 13) {$("#getCaseNo").click();	}  
	}); 
	

	
}

//根据屏幕像素比初始化标准rem大小（响应式布局）
function initRem(){
		$('html')[0].style.fontSize = window.innerWidth/80 + 'px';
		console.log(window.innerWidth/80 + 'px');
}

//获取地址栏参数
function getUrlParam(name)
{
var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
var r = window.location.search.substr(1).match(reg);  //匹配目标参数
if (r!=null) return unescape(r[2]); 
return null; //返回参数值
} 

/* *****各种辅助函数 END ***** */

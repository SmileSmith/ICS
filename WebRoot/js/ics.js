$(function(){
	
	initRem();
	init();
	

	
	
	
});

function init(){
	
	window.onresize = function(){
		initRem();	
	};

	//按钮样式切换
	$('#search').bind("mouseover ",function(){
		$(this).addClass("button-on");
	}).bind("mouseout",function(){
		$(this).removeClass("button-on");
	});	
	$('#allRecord').bind("mouseover",function(){
		$(this).addClass("button-on");
	}).bind("mouseout",function(){
		$(this).removeClass("button-on");
	});	
	$('#newCal').bind("mouseover",function(){
		$(this).css({"background-color":"#F1F1F1","color":"#EF9494"});
	}).bind("mouseout",function(){
		$(this).css({"background-color":"#EF9494","color":"#F1F1F1"});
	});


	
}
function initRem(){
		$('html')[0].style.fontSize = window.innerWidth/80 + 'px';	
}

function initTableTh(){
	var $datatable = $("#casetable");
	var tr = document.createElement('tr');
	var th=[];
	tr.id="title";
	for(var i=0;i<5;i++){
		th[i]=document.createElement('th');
	}
	th[0].innerHTML="序号";
	th[1].innerHTML="案件号";
	th[2].innerHTML="计算结果";
	th[3].innerHTML="计算日期";
	th[4].innerHTML="操作";
	for(var i=0;i<5;i++){
		tr.appendChild(th[i]);
	}
	$datatable.get(0).appendChild(tr);
}

function addLine(no,calId,caseNo,arrearage,updateDate){
	var $datatable = $("#casetable");
	
	var tr = document.createElement('tr');
	var td = [];
    var a = document.createElement('a');
	a.setAttribute("href", "http://localhost:8080/ICS/addCalcu.html?calId="+calId+"&caseNo="+caseNo);
    a.innerHTML = caseNo;
    
	for(var i=0;i<6;i++){
		td[i]=document.createElement('td');
	}
	td[0].innerHTML = no;
	td[1].style.display = "none";
	td[1].setAttribute("id","calId");
	td[1].setAttribute("value",calId);
	td[2].appendChild(a);
	td[3].innerHTML = arrearage;
	td[4].innerHTML = updateDate;
	var delBtn = document.createElement("input");
	delBtn.setAttribute("class", "init button-operatte");
	delBtn.setAttribute("type", "button");
	delBtn.setAttribute("value", "删除");
	delBtn.setAttribute("id", calId);
	delBtn.setAttribute("onclick", "deleleCaseByCalId(this.id)");
	td[5].appendChild(delBtn);
	
	for(var i=0;i<6;i++){
		tr.appendChild(td[i]);
	}
	$datatable.get(0).appendChild(tr);	
}


/**
 * 根据要求查询出满足条件的所有行
 */
function findAllRecord(type){
	
	//初始化清空表格数据
	$("tr").remove();
	//初始化表头
	initTableTh();
	
	//需要搜索的案件ID，查询全部时为空
	var searchCaseID = null;
	//根据点击不同的
	if(type == 'one'){
		 searchCaseID = $("#caseId").val();
	} 
	
    //成功需要执行的函数
	var allRecordSuccess = function(data){
		var ccList = data;
		for(var i=0;i<ccList.length;i++){
			
			var cc = ccList[i];
			var no,calId,caseNo,arrearage,updateDate;
			no=i+1;
			for(var proName in cc){
				switch(proName){ 
					case "calId": calId=cc[proName];break; 
					case "caseId": caseNo=cc[proName];break;
					case "arrearage": arrearage=cc[proName];break;
					case "updateDate": updateDate=cc[proName];break;
					default: "";
				} 
			}
			addLine(no,calId,caseNo,arrearage,updateDate);
		}
	};
	
	//失败需要执行的函数
	var allRecordError = function(){
		alert("查询失败");
	};

	$.ajax({
		type:"GET",
		url:"CaseCalueServlet",
		dataType:"json",
		success:allRecordSuccess,
		error:allRecordError
	});
};

/**
 * 根据案件ID删除ID及所有计算
 * @param calId
 */
function deleleCaseByCalId(calId){
	if(confirm("确定是否删除该案件及其所有记录?")) {
		$.ajax({
			type:"GET",
			url:"DelCaseCalueServlet?calId="+calId,
		    success:function(){$("#casetable tr td[value='"+calId+"']").parent().remove();},
		    error:function(){alert("删除失败");},
		});
	} else {
		
	}
};





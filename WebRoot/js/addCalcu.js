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
	}
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
	for(i=0;i<10;i++){
		th[i]=document.createElement('th');
	}
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
	for(i=0;i<10;i++){
		tr.appendChild(th[i]);
	}
	datatable.appendChild(tr);
}
function addLine(){
	var tr = document.createElement('tr');
	var td=[];
	for(i=0;i<10;i++){
		td[i]=document.createElement('td');
	}
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
	for(i=0;i<10;i++){
		tr.appendChild(td[i]);
	}
	datatable.appendChild(tr);	
}

function getCaseNo(){
	$('#caseNo').val($('#inpuCaseNo').val());
	$('div[class^=alertCase]').css("display","none");
}
function saveCal(){
	//TODO
}

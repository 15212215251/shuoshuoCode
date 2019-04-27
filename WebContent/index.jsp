<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="resources/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript">
		function test(){
			$.ajax({
				contentType: 'application/json',
				async: false,
				url: "listUser",
				data: '{"id":"2","age":"20","name":"21"}', 
				type: "post",
				dataType: "json",
				success: function(data){
					console.info(data);
				}			
			});
		}
		function test1(){
			$.ajax({
				contentType : 'application/json',
				async:false,
				url: "bornTransactionNo",
				data: '{"id":"10001","sid":"20001","key":"key001","encrypt":"encrypt001"}', 
				type: "post",
				dataType:"json",
				success: function(data){
					console.info(data);
				}
			});
		}
		
		function test9(){
			$.ajax({
				contentType : 'application/json',
				async:false,
				url: "pushOrderResult",
				data: '{"androidId":"123456789","transactionNo":"bd483351f3b4342f","status":"2005","key":"key001","encrypt":"encrypt001"}', 
				type: "post",
				dataType:"json",
				success: function(data){
					console.info(data);
				}
			});
		}
	</script>
  </head>
  
  <body>
    <input type="submit" value="测试" onclick="test();">
    <input type="submit" value="测试返回交易号" onclick="test1();">
     <input type="submit" value="安卓主板支付成功或失败的回掉接口" onclick="test9();">
  </body>
</html>

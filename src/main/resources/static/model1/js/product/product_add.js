//表单校验并提交
function validateAndSubmit(){
    //校验信息置空
    document.getElementById("pname_tip").innerHTML="";
    document.getElementById("price_tip").innerHTML="";
    document.getElementById("count_tip").innerHTML="";
    document.getElementById("pimage_pic_tip").innerHTML="";
    document.getElementById("pimage_pic2_tip").innerHTML="";

    //校验非空 name  price count  image_url_1 image_url_2
    var name=document.getElementById("name").value;
    var price=document.getElementById("price").value;
    var count=document.getElementById("count").value;
    var image_1=document.getElementById("image_1").value;
    var image_2=document.getElementById("image_2").value;

    console.log(name+" "+price+" "+count+" "+image_1+" "+image_2);
    if(name==""){
        document.getElementById("pname_tip").innerHTML="商品名称不能为空";
    }
    if(price==""){
        document.getElementById("price_tip").innerHTML="商品价格不能为空";
    }
    if(image_1==""){
        document.getElementById("pimage_pic1_tip").innerHTML="商品图片1不能为空";
    }
    if(image_2==""){
        document.getElementById("pimage_pic2_tip").innerHTML="商品图片2不能为空";
    }
    if(name==""||price==""||image_1==""||image_2==""){
        console.log("校验失败");
        return;
    }

    //校验数据格式
    var reg=/^[-\+]?\d+(\.\d+)?$/;
    var flag= true;
    if(!reg.test(price)){
        document.getElementById("price_tip").innerHTML="价格格式不正确";
        flag=false;
    }
    var reg1=/^[0-9]+$/;
    if(!reg1.test(count)){
        document.getElementById("count_tip").innerHTML="库存格式不正确";
        flag=false;
    }
    if(!flag){
        console.log("校验失败");
        return;
    }

    console.log("校验成功");

    //表单提交
    //document.getElementById("form1").submit();
    $("#form1").submit();
};
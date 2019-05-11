//表单校验并提交
function validateAndSubmit(){
    //校验信息置空
    document.getElementById("pname_tip").innerHTML="";
    document.getElementById("price_tip").innerHTML="";
    document.getElementById("count_tip").innerHTML="";
    document.getElementById("pimage_pic_tip").innerHTML="";
    document.getElementById("pimage_pic2_tip").innerHTML="";


    //校验非空 pname categoryid shop_price market_price is_hot pimage
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
    //修改商品时可以不选择图片  使用原图片
    // if(image_1==""){
    //     document.getElementById("pimage_pic1_tip").innerHTML="商品图片1不能为空";
    // }
    // if(image_2==""){
    //     document.getElementById("pimage_pic2_tip").innerHTML="商品图片2不能为空";
    // }
    if(name==""||price==""){
        console.log("校验失败：参数为空");
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
        console.log("校验失败：格式不正确");
        return;
    }

    console.log("校验成功");

    //表单提交
    //document.getElementById("form1").submit();
    $("#form1").submit();
};

//删除商品
function deleteProduct() {
    var id=document.getElementById("sock_id").value;
    console.log("删除商品：" + id);
    if (confirm("您确认要删除该商品么？（删除后商品所有信息都将消失）")) {
        location.href="./delete.do?id="+id;
    }
};

//获取分类标签
function getTags(){

    var id=document.getElementById("sock_id").value;

    /* ajax请求 添加分类*/
    $.ajax({
        type:'get',
        async:false,
        url:'./tag/list.do',
        data:{
            'id':id,
        },
        dataType: 'json',
        success:function(data){

            if(data.success){

            }else{
                console.log("获取分类标签失败：" + id);
            }

        },
        error:function(){
            console.log("获取分类标签失败：未知原因");
        }
    });
};

//删除分类标签
function deleteThisTag(tagid){
    var id=document.getElementById("sock_id").value;//商品id
    var name=document.getElementById("name").value;//商品名称
    console.log("删除商品" + name+"的"+tagid+"标签");
    if (confirm("确认删除该标签？")) {
        /* ajax请求 删除分类*/
        $.ajax({
            type:'get',
            async:false,
            url:'./update/deleteTag.do',
            data:{
                id:id,
                tagid:tagid,
            },
            dataType: 'json',
            success:function(data){
                if(data.success){
                    alert("删除成功！");
                    window.location.reload();
                }else{
                    alert("删除失败："+data.msg);
                }

            },
            error:function(){
                alert("添加菜单失败：未知原因");
            }
        });
    }
};

//删除添加标签
function submit() {
    var id = document.getElementById("sock_id").value;
    var tagid=document.getElementById("categoryid").value;
    console.log("商品"+id+"添加标签"+tagid);
    /* ajax请求 添加分类*/
    $.ajax({
        type:'post',
        async:false,
        url:'../product/update/addTag.do',
        data:{
            id:id,
            tagid:tagid,
        },
        dataType: 'json',
        success:function(data){

            if(data.success){
                alert("添加成功！");
                window.location.reload();
            }else{
                alert("添加失败："+data.msg);
            }

        },
        error:function(){
            alert("添加失败：未知原因");
        }
    });
};
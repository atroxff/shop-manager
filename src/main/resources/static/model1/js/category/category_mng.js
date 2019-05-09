/*添加新分类*/
function submit() {
    var cname = document.getElementById("cname").value;
    //var num  = document.getElementById("num").value;
    var flag  = true;
    /*校验非空*/
    if(cname==""){
        alert("分类名称不能为空");
        return;
    }


    /* ajax请求 添加分类*/
    $.ajax({
        type:'post',
        async:false,
        url:'./tag/add.do',
        data:{
            'cname':cname,
        },
        dataType: 'json',
        success:function(data){

            if(data.success){
                alert("添加成功！");
                var url = './tag.do';
                window.location.replace(url);
            }else{
                alert("添加失败："+data.msg);
            }

        },
        error:function(){
            alert("添加菜单失败：未知原因");
        }
    });
};

/*删除分类*/
function deleteCategory(cid){
   if(confirm("您确认要删除该项么？")){
       location.href="./tag/delete.do?cid="+cid;
   }
};

/*修改分类-弹出框*/
$('#changeModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);// 触发事件的按钮
    var cid   = button.data('cid');
    var cname = button.data('cname');
    var num = button.data('num');

    var modal = $(this);

    modal.find('.modal-title').text('修改分类');
    modal.find('.modal-body #cid_chg').text(cid);
    modal.find('.modal-body #cname_chg').val(cname);
    modal.find('.modal-body #num_chg').val(num);
})

/*修改分类-提交*/
function changeCategory(){

    var cid = document.getElementById("cid_chg").innerText;
    var cname = document.getElementById("cname_chg").value;

    /*校验非空*/
    if(cname==""){
        alert("分类名称不能为空");
        return;
    }



    /* ajax请求 修改分类*/
    $.ajax({
        type:'post',
        async:false,
        url:'./tag/update.do',
        data:{
            'cid':cid,
            'cname':cname,
        },
        dataType: 'json',
        success:function(data){

            if(data.success){
                alert("修改成功！");
                var url = './tag.do';
                window.location.replace(url);
            }else{
                alert("修改分类失败！"+data.msg);
            }

        },
        error:function(msg){
            alert("修改菜单失败！");
        }
    });
};

function changeShowSize(){
    var size = document.getElementById("showSize").value;
    $.ajax({
        type:'post',
        async:false,
        url:ContextPath+'/admin/category/ajax/changeShowSize.do',
        data:{
            'size':size,
        },
        dataType: 'json',
        success:function(data){

            if(data.success){
                alert("修改成功！");
                var url = ContextPath + '/admin/category.do';
                window.location.replace(url);
            }else{
                alert("修改失败："+data.msg);
            }

        },
        error:function(){
            alert("修改失败：ajax请求返回error");
        }
    });

}
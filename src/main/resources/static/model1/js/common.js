function logout(){
    var src = location.href;
    console.debug(src);
    if(confirm("您确认要退出么")){
        if(src.split('/').length==3) {
            location.href = "./logout.do";
        }
        else{
            location.href = "../logout.do";
        }
    }
}

function test(pid) {
    if(confirm("您确认要退出么"+id)){
        location.href = "#";
    }
}
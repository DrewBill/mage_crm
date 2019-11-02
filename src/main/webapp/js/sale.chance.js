function formatterState(val) {
    if (val==0){
        return "未分配";
    } else if (val==1){
        return "已分配";
    } else {
        return "未定义";
    }
}
$(function () {
    searchSaleChances();
})
//查询方法
function searchSaleChances() {
    $("#dg").datagrid("load",{
        createMan:$("#createMan").val(),
        customerName:$("#customerName").val(),
        createDate:$("#createDate").datebox("getValue"),
        state:$("#state").combobox("getValue")
    })
}
//打开添加框
function openAddAccountDialog() {
    $("#dlg").dialog("open");
}
//关闭对话框
function closeDialog() {
    $("#dlg").dialog("close");
}
function saveAccount() {
    //根据id的值是否为空来判断是添加还是修改 添加的话id=null 修改id！=null
    var id=$("#id").val();
    var url=ctx+"/sale_chance/update";
    if (isEmpty(id)){//添加
        url=ctx+"/sale_chance/insert";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function (params) {
            params.createMan=$.cookie("trueName");
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if (data.code==200){
                $.messager.alert("来自CRM项目",data.msg,"info");
                closeDialog();
                searchSaleChances();
            }else {
                $.messager.alert("来自CRM项目",data.msg,"error");
            }
        }
    })
}
function openModifyAccountDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if (rows.length==0){
        $.messager.alert("来自crm","请选中一条记录","error");
        return;
    }
    if (rows.length>1){
        $.messager.alert("来自crm","最多选中一条记录","error");
        return;
    }
    $("#fm").form('load',rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改营销机会记录");
}
function deleteAccount() {
    var rows=$("#dg").datagrid("getSelections");
    if (rows.length==0){
        $.messager.alert("来自crm","至少选中一条所要删除的记录","error");
        return;
    }
    var params="id=";
    for (var i=0;i<rows.length;i++){
        if (i<rows.length-1){
            params=params+rows[i].id+"&id=";
        } else {
            params=params+rows[i].id;
        }
    }
    console.log(params);
    $.messager.confirm("来自CRM","你确定要删除所选的记录嘛？",function (r) {
        if (r){
            $.ajax({
                url:ctx+"/sale_chance/delete",
                data:params,
                dataType:'json',
                success:function (data) {
                    if (data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        searchSaleChances();
                    }else {
                        $.messager.alert("来自crm",data.msg,"info");
                    }
                }
            })
        }
    })
}

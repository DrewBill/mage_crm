$(function () {
    var lossId=$("#lossId").val();
    $("#dg").edatagrid({
        url:ctx + "/customer_repri/customerReprieveByLossId?lossId=" + lossId,
        saveUrl:ctx + "/customer_repri/insertReprive?lossId=" + lossId,
        updateUrl:ctx + "/customer_repri/updateReprive?lossId=" + lossId
    })
    var state=$("#state").val();
    if (state == 1) {
        $("#toolbar").remove();
        $("#dg").edatagrid("disableEditing");
    }
});
function saveCustomerRepri() {
    $("#dg").edatagrid("addRow");
    $("#dg").edatagrid("load");
}
function delCustomerRepri() {
    var rows=$("#dg").edatagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("crm","请先在选择要删除的行","info");
        return;
    }
    $.messager.confirm("Crm","确定要删除吗？",function (r) {
        if(r){
            $.ajax({
                url:ctx + "/customer_repri/delete",
                type:"post",
                data:"id="+rows[0].id,
                dataType:"json",
                success:function (data) {
                    $.messager.alert("crm",data.msg,"info");
                    if(data.code==200){
                        $("#dg").edatagrid("load");
                    }
                }
            })
        }
    })
}
function updateCustomerLossState() {
    $.messager.confirm("crm","确认客户流失吗",function (r) {
        if (r){
            $.messager.prompt("crm","请输入原因",function (msg) {
                if (msg){
                    $.ajax({
                        type:'post',
                        url:ctx + "/customer_loss/updateCustomerLossState",
                        data: "lossId=" + $("#lossId").val() + "&lossReason=" + msg,
                        dataType: "json",
                        success:function (data){
                            $.messager.alert("crm",data.msg,"info");
                            if (data.code==200){
                                $("#toolbar").remove();
                            }
                        }
                    })
                }else {
                    $.messager.alert("crm","流失原因不能为空","info");
                }
            })
        }
    })
}
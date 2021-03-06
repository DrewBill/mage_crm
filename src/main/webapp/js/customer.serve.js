$(function () {
    loadCustomerServeType()
})
function loadCustomerServeType() {
    $.ajax({
        type:'post',
        url:ctx+"/customer_serve/queryCustomerServeType",
        dataType:'json',
        success:function (data) {
            if (data.code==200){
                var types=data.types;
                var datas=data.datas;
                //基于准备好的dom, 初始化echarts实例
                var chart = echarts.init(document.getElementById("main"));
                var option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        data:types
                    },
                    series: [
                        {
                            name:'服务类型',
                            type:'pie',
                            radius: ['50%', '70%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:datas
                        }
                    ]
                };
                //设置图表的配置项和数据项
                chart.setOption(option);
            }else{
                $.messager.alert("crm","暂无数据","info");
            }
        }
    })
}
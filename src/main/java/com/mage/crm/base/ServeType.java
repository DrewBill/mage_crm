package com.mage.crm.base;

public enum ServeType {
    CREATE("1"),    //已创建
    ASSIGN("2"),    //已分配
    PROCEED("3"),   //已处理
    FEEDBACK("4"),  //已反馈
    ARCHIVE("5");   //已归档
    private String type;
    ServeType(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }
}

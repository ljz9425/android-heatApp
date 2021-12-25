package com.whisht.heatapp.database.dao;

import android.content.Context;

import java.util.List;

public class GovernWorkDao extends AbstractBaseDao<String> {
    public GovernWorkDao(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return "t_govern_work";
    }

    public  List<String> getData(){
        List<String> idData = queryGovernWorkColList("t_govern_work",new String[]{"gid,readtime"});
        return idData;
    }

    public  void replaceData(String id){
        execute("replace into t_govern_work(gid) values(\""+id+"\");");
    }

    public  void deleteDataByDate(String date){
        execute("delete from t_govern_work where readtime<\""+date+"\"");
    }
}

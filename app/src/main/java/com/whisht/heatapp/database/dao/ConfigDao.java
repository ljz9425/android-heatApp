package com.whisht.heatapp.database.dao;

import android.content.Context;

import com.whisht.heatapp.database.data.ConfigData;


public class ConfigDao extends AbstractBaseDao<ConfigData> {
    public ConfigDao(Context context) {
        super(context);
    }

    @Override
    public String getTable() {
        return "t_config";
    }
}

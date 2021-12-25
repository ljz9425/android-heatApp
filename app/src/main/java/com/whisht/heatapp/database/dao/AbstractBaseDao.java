package com.whisht.heatapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.whisht.heatapp.database.sqlite.BaseDao;
import com.whisht.heatapp.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractBaseDao<T> extends BaseDao<T> {
    private Class<T> classz = null;
    public AbstractBaseDao(Context context) {
        super(context);
        Type type = getClass().getGenericSuperclass();
        ParameterizedType ptype= (ParameterizedType)type;
        Type[] types = ptype.getActualTypeArguments();
        classz = (Class<T>) types[0];
    }
    public abstract String getTable();
    public int signAllDelete(){
        ContentValues cv = new ContentValues();
        cv.put("isDel","1");
        return update(getTable(),cv,"",new String[]{});
    }
    public int deleteAllSigned(){
        return delete(getTable(),"isDel=?",new String[]{"1"});
    }
    public int delete(String whereClause, String[] whereArgs){
        return delete(getTable(),whereClause,whereArgs);
    }
    public long insert(ContentValues values){
        return insert(getTable(),values);
    }
    public long replace(ContentValues values){
        return replace(getTable(),values);
    }
    public long insert(T t){
        Map<String,String> tMap = CommonUtils.convertBaseEntityToMap(t,true);
        if(tMap.size() < 1)
            return 0;
        Iterator<String> it = tMap.keySet().iterator();
        String key;
        ContentValues cv = new ContentValues();
        while(it.hasNext()){
            key = it.next();
            cv.put(key,tMap.get(key));
        }
        return insert(cv);
    }
    public long replace(T t){
        Map<String,String> tMap = CommonUtils.convertBaseEntityToMap(t,true);
        if(tMap.size() < 1)
            return 0;
        Iterator<String> it = tMap.keySet().iterator();
        String key;
        ContentValues cv = new ContentValues();
        while(it.hasNext()){
            key = it.next();
            cv.put(key,tMap.get(key));
        }
        return replace(cv);
    }
    public int update(ContentValues values,
                      String whereClause, String[] whereArgs){
        return update(getTable(),values,whereClause,whereArgs);
    }
    /**
     * 查询数据
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @return                  返回Cursor
     */
    public Cursor query(String[] columns, String selection,
                        String[] selectionArgs) {
        return query(getTable(),columns,selection,selectionArgs);
    }
    public int getCount(){
        return getCount(null,null);
    }
    /**
     * 查询数据条数
     * @param selection 查询条件
     * @param selectionArgs 查询条件参数
     * @return
     */
    public int getCount(String selection,String[] selectionArgs){
        Cursor c = query(new String[]{"count(1) as cou"},selection,selectionArgs);
        if(c.moveToFirst()){
            int index = c.getColumnIndex("cou");
            if(index >= 0){
                return c.getInt(index);
            }
        }
        c.close();
        return 0;
    }
    /**
     * 判断数据是否存在
     * @param selection 查询条件
     * @param selectionArgs 查询条件参数
     * @return
     */
    public boolean isExist(String selection,String[] selectionArgs){
        Cursor c = query(new String[]{"count(1) as cou"},selection,selectionArgs);
        if(c.moveToFirst()){
            int index = c.getColumnIndex("cou");
            if(index >= 0){
                return c.getInt(index) > 0;
            }
        }
        return false;
    }

    /**
     * 查询数据
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @param groupBy     		groupBy
     * @return                  返回Cursor
     */
    public Cursor query(String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having, String orderBy) {
        return query(getTable(),columns,selection,selectionArgs,groupBy,having,orderBy);
    }


    /**
     * 查询数据
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @param orderBy           排序              如：id desc
     * @return                  返回Cursor
     */
    public Cursor query(String[] columns, String selection,
                        String[] selectionArgs, String orderBy) {
        return query(getTable(),columns,selection,selectionArgs,orderBy);
    }

    /**
     * 查询数据
     * @param distinct          每行是唯一     true:表示唯一       false:表示不唯一
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @param orderBy           排序              如：id desc
     * @param limit             查询的条数              如：10
     * @return                  返回Cursor
     */
    public Cursor query(boolean distinct, String[] columns, String selection,
                        String[] selectionArgs, String orderBy, String limit){
        return query(distinct,getTable(),columns,selection,selectionArgs,orderBy,limit);
    }

    /**
     * 查询数据    单个对象
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @return                  返回Object
     */
    @SuppressWarnings("unchecked")
    public T queryObject(String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String order){
        return queryObject(classz,getTable(),columns,selection,selectionArgs,groupBy,order);
    }
    /**
     * 查询数据    单个对象
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @return                  返回Object
     */
    @SuppressWarnings("unchecked")
    public T queryObject(String[] columns, String selection,
                         String[] selectionArgs){
        return queryObject(classz, getTable(),columns, selection, selectionArgs);
    }
    /**
     * 查询数据    带分页功能
     * @param columns           要查询的列名
     * @param selection         查询条件    如：( id=?)
     * @param selectionArgs     条件里的参数，用来替换"?"
     * @param orderBy           排序              如：id desc
     * @param pageNo            页码              不分页时，为null
     * @param pageSize          每页的个数        不分页时，为null
     * @return                  返回List
     */
    @SuppressWarnings("unchecked")
    public List<T> queryList(String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize){
        return queryList(classz,getTable(),columns,selection,selectionArgs,groupBy,orderBy,pageNo,pageSize);
    }

    @SuppressWarnings("unchecked")
    public List<T> queryList(String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize,String orderType){
        return queryList(classz,getTable(),columns,selection,selectionArgs,groupBy,orderBy,pageNo,pageSize,orderType);
    }

    @SuppressWarnings("unchecked")
    public List<String> queryStringList(String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize,String orderType){
        return queryList(getTable(),columns,selection,selectionArgs,groupBy,orderBy,pageNo,pageSize,orderType);
    }




    public List<T> queryList(String[] columns,String section,String[] selectionArgs){
        return queryList(columns,section,selectionArgs,null,null,null,null);
    }

    public List<T> queryListAll(String orderBy){
        return queryList(classz,getTable(),new String[]{"*"},null,null,null,orderBy,null,null);
    }

    public List<T> queryListAll(){
        return queryListAll(null);
    }
}

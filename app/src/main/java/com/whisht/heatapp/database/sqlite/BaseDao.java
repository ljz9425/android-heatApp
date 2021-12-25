package com.whisht.heatapp.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.whisht.heatapp.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 基础DAO，每个DAO都要继承此类
 *
 * @author steven
 *
 * http://www.juziku.com/sunlightcs/
 *
 */
public class BaseDao<T> {
	private String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private SQLiteDatabase db;

	public Context getmContext() {
		return mContext;
	}

	public BaseDao(Context context) {
		this.mContext = context;
		DBHelper dbHelper = new DBHelper(mContext, Configuration.DB_NAME,
				null, Configuration.DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 增加、删除、修改表时，调用此方法
	 * @param sql  DDL语句
	 * @throws SQLException
	 */
	public void execute(String sql){
		db.execSQL(sql);
	}

	/**
	 * 删除表中的记录
	 * @param table   表名
	 * @param whereClause 删除条件   如：( id>? and time>?)
	 * @param whereArgs   条件里的参数    用来替换"?"    第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * @return  返回删除的条数
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}

	/**
	 * 插入数据
	 * @param table 表名
	 * @param values    ContentValues对象
	 * @return          返回当前行ID值，如果失败返回-1
	 */
	public long insert(String table, ContentValues values){
		return this.insert(table, null, values);
	}

    public long replace(String table, ContentValues values){
        return db.replace(table, null, values);
    }

	/**
	 * 插入数据
	 * @param table 表名
	 * @param values    ContentValues对象
	 * @param nullColumnHack   空列
	 * @return          返回当前行ID值，如果失败返回-1
	 */
	public long insert(String table, String nullColumnHack,
                       ContentValues values) throws SQLException {
		return db.insertOrThrow(table, nullColumnHack, values);
	}

	/**
	 * 修改数据
	 * @param table         表名
	 * @param values        ContentValues对象          表示要修改的列，如： name="steven" 即 values.put("name", "steven");
	 * @param whereClause   修改条件   如：( id=?)
	 * @param whereArgs     条件里的参数    用来替换"?"    第1个参数，代表第1个问号；第2个参数，代表第2个问号；依此类推......
	 * @return              返回修改的条数
	 */
	public int update(String table, ContentValues values,
                      String whereClause, String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}

	public boolean batchUpdate(String sql,List<Object[]> args){
		boolean success = true;
		try{
			db.beginTransaction();
			for(int i=0;i<args.size();i++){
				db.execSQL(sql,args.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
			LogUtils.i(TAG,e.getMessage());
			success = false;
		}finally{
			//显示的设置数据事务是否成功
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return success;
	}

	public boolean batchUpdate(List<String> sql){
		boolean success = true;
		try{
			db.beginTransaction();
			for(int i=0;i<sql.size();i++){
				db.execSQL(sql.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
			LogUtils.i(TAG,e.getMessage());
			success = false;
		}finally{
			//显示的设置数据事务是否成功
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return success;
	}

	public boolean batchUpdate(List<String> sql,List<Object[]> args){
		boolean success = true;
		try{
			db.beginTransaction();
			for(int i=0;i<sql.size();i++){
				db.execSQL(sql.get(i),args.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
			LogUtils.i(TAG,e.getMessage());
			success = false;
		}finally{
			//显示的设置数据事务是否成功
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return success;
	}

	/**
	 * 查询数据
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @return                  返回Cursor
	 */
	public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs) {
		return db.query(table, columns, selection, selectionArgs, null, null,
				null);
	}

	/**
	 * 查询数据
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @param groupBy     		groupBy
	 * @return                  返回Cursor
	 */
	public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having, String orderBy) {
		return db.query(table, columns, selection, selectionArgs, groupBy, having,
				orderBy);
	}


	/**
	 * 查询数据
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @param orderBy           排序              如：id desc
	 * @return                  返回Cursor
	 */
	public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String orderBy) {
		return db.query(table, columns, selection, selectionArgs, null, null,
				orderBy);
	}

	/**
	 * 查询数据
	 * @param distinct          每行是唯一     true:表示唯一       false:表示不唯一
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @param orderBy           排序              如：id desc
	 * @param limit             查询的条数              如：10
	 * @return                  返回Cursor
	 */
	public Cursor query(boolean distinct, String table, String[] columns, String selection,
                        String[] selectionArgs, String orderBy, String limit){
		return db.query(distinct, table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}




	/**
	 * 查询某个字段
	 * @param classz            字节码      如：String.class
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @return                  返回Object
	 */
	public Object queryField(Class<?> classz, String table, String[] columns, String selection,
                             String[] selectionArgs){
		Object o = null;
		//查询单条记录
		Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null, "1");
		if(c.moveToFirst()){
			try {
				if(classz == Integer.TYPE) {   //int
					o = c.getInt(0);
				}else if(classz == String.class){   //String
					o = c.getString(0);
				}else if(classz == Long.TYPE){   //long
					o = c.getLong(0);
				}else if(classz == Float.TYPE){   //float
					o = c.getFloat(0);
				}else if(classz == Double.TYPE){   //double
					o = c.getDouble(0);
				}else if(classz == Short.TYPE){   //short
					o = c.getShort(0);
				}
			} catch (Exception e) {
				Log.e("queryField", e.toString());
			}
		}
		c.close();
		return o;
	}

	/**
	 * 查询数据    单个对象
	 * @param classz            字节码      如：String.class
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @return                  返回Object
	 */
	@SuppressWarnings("unchecked")
	public T queryObject(Class<?> classz, String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String order){
		//查询单条记录
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, null, order, "1");
		T t = null;
		if(c.moveToFirst()){
			try{
				//生成新的实例
				t = (T) classz.newInstance();

				//把列的值，转换成对象里属性的值
				columnToField(t, c);

			}catch(Exception e){
				Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
			}
		}
		c.close();
		return t;
	}
	/**
	 * 查询数据    单个对象
	 * @param classz            字节码      如：String.class
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @return                  返回Object
	 */
	@SuppressWarnings("unchecked")
	public T queryObject(Class<?> classz, String table, String[] columns, String selection,
                         String[] selectionArgs){
		//查询单条记录
		Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null, "1");
		T t = null;
		if(c.moveToFirst()){
			try{
				//生成新的实例
				t = (T) classz.newInstance();

				//把列的值，转换成对象里属性的值
				columnToField(t, c);

			}catch(Exception e){
				Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
			}
		}
		c.close();
		return t;
	}


	/**
	 * 查询数据    带分页功能
	 * @param classz            字节码      如：String.class
	 * @param table             表名
	 * @param columns           要查询的列名
	 * @param selection         查询条件    如：( id=?)
	 * @param selectionArgs     条件里的参数，用来替换"?"
	 * @param orderBy           排序              如：id desc
	 * @param pageNo            页码              不分页时，为null
	 * @param pageSize          每页的个数        不分页时，为null
	 * @return                  返回List
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryList(Class<?> classz, String table, String[] columns, String selection,
                             String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize,String orderType){

		if(!TextUtils.isEmpty(orderType)){
			orderBy = orderBy +" "+orderType;
		}
		//分页
		if(!(pageNo == null || pageSize ==null)){
			//分页的起始位置
			int begin = (pageNo -1)*pageSize;
			orderBy = orderBy +" limit " + begin + ", " + pageSize;
		}




		//查询数据
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, null, orderBy);

		List<T> list = new ArrayList<T>();
		T t = null;
		while (c.moveToNext()) {
			try{
				//生成新的实例
				t = (T) classz.newInstance();
			}catch(Exception e){
				Log.e("newInstance error", "生成新实例时出错 ：" + e.toString());
			}

			//把列的值，转换成对象里属性的值
			columnToField(t, c);

			list.add(t);
		}
		c.close();

		return list;
	}


	public List<T> queryList(Class<?> classz, String table, String[] columns, String selection,
							 String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize){
		return queryList(classz,table,columns,selection,selectionArgs,groupBy,orderBy,pageNo,pageSize,null);
	}


	@SuppressWarnings("unchecked")
	public List<String> queryList(String table, String[] columns, String selection,
							 String[] selectionArgs, String groupBy, String orderBy, Integer pageNo, Integer pageSize,String orderType){
		if(!TextUtils.isEmpty(orderType)){
			orderBy = orderBy+" "+orderType;
		}
		//分页
		if(!(pageNo == null || pageSize ==null)){
			//分页的起始位置
			int begin = (pageNo -1)*pageSize;
			orderBy = orderBy+" limit " + begin + ", " + pageSize;
		}

		//查询数据
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, null, orderBy);
		List<String> list = new ArrayList<>();
		if (null != c){
			while (c.moveToNext()) {
				list.add(c.getString(c.getColumnIndex(columns[0])));
			}
			c.close();
		}
		c.close();

		return list;
	}


	/**
	 * 把列的值，转换成对象里属性的值
	 */
	private void columnToField(T t, Cursor c){
		//获取T里的所有属性
		Field[] f = t.getClass().getDeclaredFields();
		for (int i = 0; i < f.length; i++) {

			int columnIndex = c.getColumnIndex(f[i].getName());
			//如果为-1，表示不存在此列
			if(columnIndex == -1){
				continue;
			}

			Class<?> classz = f[i].getType();
			//设置成可访问，否则不能set值
			f[i].setAccessible(true);

			try {
				if(classz == Integer.TYPE) {   //int
					f[i].set(t, c.getInt(columnIndex));
				}else if(classz == String.class){   //String
					f[i].set(t, c.getString(columnIndex));
				}else if(classz == Long.TYPE){   //long
					f[i].set(t, c.getLong(columnIndex));
				}else if(classz == byte[].class){   //byte
					f[i].set(t, c.getBlob(columnIndex));
				}else if(classz == Float.TYPE){   //float
					f[i].set(t, c.getFloat(columnIndex));
				}else if(classz == Double.TYPE){   //double
					f[i].set(t, c.getDouble(columnIndex));
				}else if(classz == Short.TYPE){   //short
					f[i].set(t, c.getShort(columnIndex));
				}
			} catch (Exception e) {
				Log.e("column to field error", "字段转换成对象时出错 ：" + e.toString());
			}
		}
	}

    public List<String> queryGovernWorkColList(String table, String[] columns){
        //查询数据
        Cursor c = null;
        try {
            c = db.query(table, columns, null, null, null, null, null);
        }catch (SQLiteException e){
            db.execSQL("create TABLE t_govern_work (gid TEXT UNIQUE,readtime date default (datetime('now', 'localtime')))");
        }

        List<String> list = new ArrayList<String>();
        if (null != c){
            while (c.moveToNext()) {
                list.add(c.getString(c.getColumnIndex("gid")));
            }
            c.close();
        }

        return list;
    }


    public List<String> queryRoadInfoColList(String table, String[] columns){
        //查询数据
        Cursor c = null;
        try {
            c = db.query(table, columns, null, null, null, null, null);
        }catch (SQLiteException e){
            db.execSQL("create TABLE t_road_info (rid TEXT UNIQUE,readtime date default (datetime('now', 'localtime')))");
        }

        List<String> list = new ArrayList<String>();
        if (null != c){
            while (c.moveToNext()) {
                list.add(c.getString(c.getColumnIndex("rid")));
            }
            c.close();
        }

        return list;
    }

	/**
	 * 开始事务
	 */
	protected void beginTransaction() {
		db.beginTransaction();
	}

	/**
	 * 提交事务及结束事务
	 */
	protected void commit() {
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 回滚事务
	 */
	protected void rollback() {
		db.endTransaction();
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		if (db != null && db.isOpen())
			db.close();
	}
}

package com.whisht.heatapp.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {

	private static String TAG = DBHelper.class.getSimpleName();
	private Context mContext;
	private int newVersion;

	public DBHelper(Context context, String databaseName,
                    CursorFactory factory, int version) {
		super(context, databaseName, factory, version);
		mContext = context;
		this.newVersion = version;
		File f = new File(Configuration.DB_PATH);
		if(f.exists()){
			Log.i(TAG, f.getAbsolutePath());
		}
	}

	/**
	 * 数据库第一次创建时调用
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		executeSchema(db, "heatapp.sql");
		onUpgrade(db,1,newVersion);
		Log.i(TAG, "onCreate: ==="+newVersion);
	}

	/**
	 * 数据库升级时调用
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "oldVersion:"+oldVersion+",newVersion:"+newVersion);
		//数据库不升级
		if (newVersion <= oldVersion) {
			return;
		}
		Configuration.oldVersion = oldVersion;

		int changeCnt = newVersion - oldVersion;
		for (int i = 0; i < changeCnt; i++) {
			// 依次执行updatei_i+1文件      由1更新到2 [1-2]，2更新到3 [2-3]
			String schemaName = "update" + (oldVersion + i) + "_"
					+ (oldVersion + i + 1) + ".sql";
			Log.i(TAG, "schemaName:"+schemaName);
			executeSchema(db, schemaName);
		}
	}

	/**
	 * 读取数据库文件（.sql），并执行sql语句
	 * */
	private void executeSchema(SQLiteDatabase db, String schemaName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(mContext.getAssets()
					.open(Configuration.DB_PATH + "/" + schemaName)));
			String line;
			String buffer = "";
			while ((line = in.readLine()) != null) {
				buffer += line;
				if (line.trim().endsWith(";")) {
					try {
						db.execSQL(buffer.replace(";", ""));
					}catch (Exception e){
						e.printStackTrace();
						Log.e(TAG, e.toString());
					}
					buffer = "";
				}
			}
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
	}

}
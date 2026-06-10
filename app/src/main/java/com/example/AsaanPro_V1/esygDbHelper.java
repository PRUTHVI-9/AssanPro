package com.example.AsaanPro_V1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class esygDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "asaan.db";

    public esygDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(esygDbContract.EcgMaster.SQL_CREATE_ECG_MASTER);
        db.execSQL(esygDbContract.EventMaster.SQL_CREATE_EVENT_MASTER);
        db.execSQL(esygDbContract.HealthMaster.SQL_CREATE_PATIENT_HEALTH_MASTER);
        db.execSQL(esygDbContract.PatientMaster.SQL_CREATE_PATIENT_MASTER);
        db.execSQL(esygDbContract.SearchMaster.SQL_CREATE_SEARCH_MASTER);
        db.execSQL(esygDbContract.ConfigMaster.SQL_CREATE_CONFIG_MASTER);
        db.execSQL(esygDbContract.HWMaster.SQL_CREATE_HW_MASTER);

        db.execSQL(esygDbContract.PatientSamplesDB.SQL_CREATE_PATIENT_SAMPLES_DB);
        db.execSQL(esygDbContract.EmergencySamplesDB.SQL_CREATE_EMERGENCY_SAMPLES_DB);

        db.execSQL(esygDbContract.CountryMaster.SQL_CREATE_COUNTRY_MASTER);
        db.execSQL(esygDbContract.StateMaster.SQL_CREATE_STATE_MASTER);
        db.execSQL(esygDbContract.InsuranceProviderMaster.SQL_CREATE_INSURANCEPROVIDER_MASTER);
        db.execSQL(esygDbContract.HistoryConditionsMaster.SQL_CREATE_HISTORYCONDITIONS_MASTER);
    }
    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(asaanDbContract.Order.SQL_DELETE_ORDER);
//        onCreate(db);
    }
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }
}

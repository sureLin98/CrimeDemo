package com.demo.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.criminalintent.database.CrimeCursorWrapper;
import com.demo.criminalintent.database.CrimeDbSchema.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.demo.criminalintent.database.CrimeBaseHelper;
import com.demo.criminalintent.database.CrimeDbSchema;

//创建单例

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //数据打包
    public static ContentValues getContentValues(Crime crime){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID,crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE,crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);

        return contentValues;
    }

    private CrimeLab(Context context){
        mContext=context.getApplicationContext();
        mSQLiteDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public List<Crime> getCrimes(){
        List<Crime> crimes=new ArrayList<>();
        CrimeCursorWrapper cursor=queryCrime(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor=queryCrime(
                CrimeTable.Cols.UUID+"=?",
                new String[]{id.toString()}
                );

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }

    public CrimeCursorWrapper queryCrime(String whereClause, String[] whereArgs){
        Cursor cursor=mSQLiteDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new CrimeCursorWrapper(cursor);
    }

    public void updateCrime(Crime crime){
        ContentValues values=getContentValues(crime);
        String uuid=crime.getId().toString();
        mSQLiteDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID+" = ? ",new String[]{uuid});
    }

    public void addCrime(Crime crime){
        ContentValues values=getContentValues(crime);
        mSQLiteDatabase.insert(CrimeTable.NAME,null,values);
    }

    public void deleteCrime(UUID crimeId){
        mSQLiteDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID+"=?",new String[]{crimeId.toString()});
    }

    public void deleteCrime(int index){
        mSQLiteDatabase.delete(CrimeTable.NAME,"_id=?",new String[]{String.valueOf(index)});

    }
}

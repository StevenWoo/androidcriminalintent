package com.tackable.foobar.criminalintent.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by stevenwoo on 4/24/14.
 */

public class CrimeLab {
    private ArrayList<Crime> mCrimes;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;


    private CrimeLab(Context appContext ){
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
    }

    public static CrimeLab get(Context c){
        if( sCrimeLab == null){
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public ArrayList<Crime>getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for( Crime c : mCrimes ){
            if( c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
}

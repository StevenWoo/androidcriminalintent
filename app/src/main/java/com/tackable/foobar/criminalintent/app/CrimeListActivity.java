package com.tackable.foobar.criminalintent.app;

import android.support.v4.app.Fragment;

/**
 * Created by stevenwoo on 4/25/14.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}

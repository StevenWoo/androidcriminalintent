package com.tackable.foobar.criminalintent.app;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by stevenwoo on 5/1/14.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c, String filename){
        mContext = c;
        mFilename = filename;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
    throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for( Crime c : crimes ){
            array.put(c.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if( writer != null ){
                writer.close();
            }
        }
    }
}

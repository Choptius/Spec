package com.choptius.spec;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class AstroDatabaseHelper extends SQLiteAssetHelper {

    public static final int DATA_BASE_VERSION = 1;
    public static final String DATA_BASE_NAME = "astro_database.db";

    public AstroDatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATA_BASE_VERSION);
    }


}


package com.example.octav.proiect.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ModesContentProvider extends ContentProvider {

    private static final String TAG = Constants.TAG + ModesContentProvider.class.getSimpleName();
    static final String PROVIDER_NAME = "com.example.octav.proiect";
    static final String URL = "content://" + PROVIDER_NAME + "/modes";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int MODES = 1;
    static final int MODE_ID = 2;
    static final String _ID = "_id";
    static final UriMatcher uriMatcher;
    static{

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "modes", MODES);
        uriMatcher.addURI(PROVIDER_NAME, "modes/#", MODE_ID);
    }

    // Database
    DataBase mDatabase;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");
        mDatabase = new DataBase(getContext().openOrCreateDatabase("MyDataBase", Context.MODE_PRIVATE, null));
        return (mDatabase == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query()");
        Cursor c = mDatabase.getAllModes();
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

package ga.chschtsch.milonhapashut;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SQLiteAssetHelper extends SQLiteOpenHelper {
    private static final String DATABASE_DIR_NAME = "databases";
    private final Context mContext;
    private final SQLiteDatabase.CursorFactory mFactory;
    private SQLiteDatabase mDatabase;
    private String mDatabaseName;
    private String mDatabaseAssetPath;
    private String mDatabaseDiskPath;
    private boolean mIsProcessingDatabaseCreation;

    public SQLiteAssetHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             String destinationPath, int version) {
        super(context, name, factory, version);

        mContext = context;
        mFactory = factory;
        mDatabaseName = name;
        mDatabaseAssetPath = DATABASE_DIR_NAME + "/" + name;
        if (destinationPath == null) {
            mDatabaseDiskPath = context.getApplicationInfo().dataDir + "/" + DATABASE_DIR_NAME;
        } else {
            mDatabaseDiskPath = destinationPath;
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
            return mDatabase;
        }

        if (mIsProcessingDatabaseCreation) {
            throw new IllegalStateException("getWritableDatabase is still processing");
        }

        SQLiteDatabase db = null;
        boolean isDatabaseLoaded = false;

        try {
            mIsProcessingDatabaseCreation = true;
            db = createOrOpenDatabase();
            onOpen(db);
            isDatabaseLoaded = true;
            return db;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            mIsProcessingDatabaseCreation = false;
            if (isDatabaseLoaded) {
                if (mDatabase != null) {
                    try {
                        mDatabase.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mDatabase = db;
            } else {
                if (db != null) db.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void copyDatabaseFromAssets() throws IOException {

        String dest = mDatabaseDiskPath + "/" + mDatabaseName;
        String path = mDatabaseAssetPath;

        InputStream is = mContext.getAssets().open(path);

        File databaseDestinationDir = new File(mDatabaseDiskPath + "/");
        if (!databaseDestinationDir.exists()) {
            databaseDestinationDir.mkdir();
        }
        IOUtils.copy(is, new FileOutputStream(dest));
    }

    private SQLiteDatabase createOrOpenDatabase() throws IOException {

        SQLiteDatabase db = null;
        File file = new File (mDatabaseDiskPath + "/" + mDatabaseName);
        if (file.exists()) {
            db = openDatabase();
        }

        if (db != null) {
            return db;
        } else {
            copyDatabaseFromAssets();
            db = openDatabase();
            return db;
        }
    }

    private SQLiteDatabase openDatabase() {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(
                    mDatabaseDiskPath + "/" + mDatabaseName, mFactory, SQLiteDatabase.OPEN_READWRITE);
            return db;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class IOUtils {

        private static final int BUFFER_SIZE = 1024;

        public static void copy(InputStream in, OutputStream outs) throws IOException {
            int length;
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((length = in.read(buffer)) > 0) {
                outs.write(buffer, 0, length);
            }

            outs.flush();
            outs.close();
            in.close();
        }

    };

}

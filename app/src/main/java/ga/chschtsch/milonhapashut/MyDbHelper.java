package ga.chschtsch.milonhapashut;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteAssetHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FILE_NAME = "milon.db";

    public MyDbHelper(MainActivityFragment mainActivityFragment, String dbFilepath, Context context, int i) {
        super(context, DATABASE_FILE_NAME, null, context.getFilesDir().getAbsolutePath(), DATABASE_VERSION);
    }

    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<Word>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM en_word", null);

        if (cursor.moveToFirst()) {
            do {
                Word newWord = new Word();
                //newWord.setID(Integer.parseInt(cursor.getString(0)));
                newWord.setTranslated(cursor.getString(1));
                String t = cursor.getString(1).toLowerCase();
                newWord.setTranslated2(t);
                newWord.setTranslation(cursor.getString(2));
                newWord.setPart(cursor.getString(3));
                wordList.add(newWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        Cursor cursor2 = db.rawQuery("SELECT * FROM he_word", null);

        if (cursor2.moveToFirst()) {
            do {
                Word newWord = new Word();
                //newWord.setID(Integer.parseInt(cursor2.getString(0)));
                newWord.setTranslated(cursor2.getString(1));
                String t = cursor2.getString(1).replaceAll("[\u0591-\u05C7]", "");
                newWord.setTranslated2(t);
                newWord.setTranslation(cursor2.getString(2));
                newWord.setPart(cursor2.getString(3));
                wordList.add(newWord);
            } while (cursor2.moveToNext());
        }

        cursor2.close();

        return wordList;
    }

}

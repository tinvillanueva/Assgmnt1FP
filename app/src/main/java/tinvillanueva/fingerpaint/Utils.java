package tinvillanueva.fingerpaint;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by tinvillanueva on 7/04/15.
 */
public class Utils {

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap resultImage;
        try {
            resultImage = BitmapFactory.decodeFile(imagePath);
            return resultImage;
        }
        catch (OutOfMemoryError ome){
            ome.printStackTrace();
            System.gc();
            try {
                resultImage = BitmapFactory.decodeFile(imagePath);
                return resultImage;
            }
            catch (OutOfMemoryError ome2){
                ome2.printStackTrace();
                return null;
            }
        }

    }
}

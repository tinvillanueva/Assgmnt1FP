package tinvillanueva.fingerpaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by tinvillanueva on 5/04/15.
 */
public class MainMenu extends Activity {
    private static  int RESULT_LOAD_IMAGE = 1;
    private final Context context = this;
    String imagePath;
    private Bitmap selectedImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button btnNew = (Button) findViewById(R.id.btnMenuNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GO TO MAIN ACTIVITY
                Intent mainActivityIntent = new Intent("tinvillanueva.fingerpaint.MainActivity");
                startActivity(mainActivityIntent);

            }
        });

        Button btnGallery = (Button) findViewById(R.id.btnMenuGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the gallery folder and user is able to select a saved drawing
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder exitDialog = new AlertDialog.Builder(context);
                exitDialog.setTitle("Exit App");
                exitDialog.setMessage("Do you want to exit the painting app?");
                exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        finish();
                        dialog.dismiss();
                    }
                });

                exitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                exitDialog.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     
        try {
            //when image is picked
            if (requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && null != data){
                //get the Image from data
                Uri selectedImageUri = data.getData();
                
                //selecting image
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                //get the cursor
                Cursor cursor = context.getContentResolver().query(selectedImageUri,
                        filePathColumn, null, null, null);
                //move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) findViewById(R.id.drawingView);
                //sets the image in the drawView after decoding the string
                imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }
            else {
                Toast.makeText(this, "You have not picked an image", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Something went wrong, try selecting other image",
                    Toast.LENGTH_LONG).show();
        }
    }
}

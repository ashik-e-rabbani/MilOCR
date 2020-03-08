package com.afss.milocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String galleryMode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MilOCR - EN");

        imageView = (ImageView) findViewById(R.id.ImageView);
        textView = (TextView) findViewById(R.id.textView);
    }

    public void pick_image(View view)
    {
        galleryMode = "yes";
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i,1);
    }
    public void snap_image(View view)
    {
        galleryMode = "no";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){

            Uri uri = data.getData();
            try {

                if (galleryMode=="yes")
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imageView.setImageBitmap(bitmap);

                }else{
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void detect(View view) {

        if (bitmap == null)
        {
            Toast.makeText(getApplicationContext(),"No Image",Toast.LENGTH_LONG).show();
        }else {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();

            detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText texts) {
                    process_text(texts);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed to do",Toast.LENGTH_LONG).show();
                }
            });
        }



    }

    private void process_text(FirebaseVisionText texts) {

       // ArrayList<FirebaseVisionText.TextBlock> blocks = (ArrayList<FirebaseVisionText.TextBlock>) texts.getTextBlocks();

        // If need data can be cleaned

        if (texts.getTextBlocks().size() == 0){

            Toast.makeText(getApplicationContext(),"Not Detected",Toast.LENGTH_LONG).show();
        }else
        {
            for (FirebaseVisionText.TextBlock textBlock : texts.getTextBlocks()){
                textView.append(textBlock.getText());



            }
        }

    }
}

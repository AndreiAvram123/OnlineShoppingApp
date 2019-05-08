package com.example.onlineshoopingapp.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class used to provide a way for activities to process barcodes
 * using the phone's camera
 */
public class BarcodeScanner {
    private Activity activity;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE_CODE = 1;


    public BarcodeScanner(@NonNull Activity activity) {
        this.activity = activity;
    }

    public int getRequestImageCaptureCode(){
        return REQUEST_IMAGE_CAPTURE_CODE;
    }


    /**
     * Start the cameraActivity and ask the user to take
     * a picture of a barcode
     */
    public void scan() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //the authority argument for this method is very sensitive
                //IT MUST HAVE THE EXACT SAME NAME AS DECLARED IN THE MANIFEST
                Uri photoURI = FileProvider.getUriForFile(activity.getApplicationContext(),
                        activity.getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //use start activity for result because we are expecting a result, a picture
                //put the field variable @field REQUEST_IMAGE_CAPTURE_CODE in order
                //to know if the result is the expected one
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_CODE);

            }

        }
    }

    /**
     * CALL THIS METHOD AFTER GETTING A RESPONSE FROM
     * THE CAMERA ACTIVITY
     */
    @Nullable
    public ArrayList<String> processImage() {
        ArrayList<String> results = new ArrayList<>();
        try {
            //create a firebaseVisionImage from the stored picturePath
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(activity.getApplicationContext(),
                    Uri.fromFile(new File(currentPhotoPath)));

            FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector =
                    FirebaseVision.getInstance().getVisionBarcodeDetector(getVisionConfiguration());


            Task<List<FirebaseVisionBarcode>> result = firebaseVisionBarcodeDetector.detectInImage(image)
                    .addOnSuccessListener(barcodes -> {
                        // Task completed successfully
                        results.addAll(processBarcodes(barcodes));
                    })
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                        // ...
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @SuppressLint("NewApi")
    private ArrayList<String> processBarcodes(List<FirebaseVisionBarcode> barcodes) {
        ArrayList<String> rawValues = new ArrayList<>();
        barcodes.forEach(barcode -> rawValues.add(barcode.getRawValue()));
        return rawValues;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //get getExternalFilesDir() with the argument DIRECTORY_PICTURE saves
        //the picture in the app context. IT IS NOT ACCESSIBLY BY OTHER APPS
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // save the image path in order to use it later
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private FirebaseVisionBarcodeDetectorOptions getVisionConfiguration() {
        //use the builder model in order to build the options


        return new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();


    }
}

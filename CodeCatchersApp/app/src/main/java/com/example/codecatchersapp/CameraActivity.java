/**
 * This class represents a camera activity that captures a photo using the device camera and stores it in Firebase.
 * It also resizes the captured photo and saves it to the device's internal storage.
 * The class uses the Fotoapparat library for camera functionalities and the Firebase Firestore SDK for database operations.
 * @author Kyle Karpyshyn
 * @version 1.0
 * @since 2023-04-01
 */
package com.example.codecatchersapp;

import static io.fotoapparat.selector.AspectRatioSelectorsKt.standardRatio;
import static io.fotoapparat.selector.FlashSelectorsKt.autoFlash;
import static io.fotoapparat.selector.FlashSelectorsKt.autoRedEye;
import static io.fotoapparat.selector.FlashSelectorsKt.off;
import static io.fotoapparat.selector.FlashSelectorsKt.torch;
import static io.fotoapparat.selector.FocusModeSelectorsKt.autoFocus;
import static io.fotoapparat.selector.FocusModeSelectorsKt.continuousFocusPicture;
import static io.fotoapparat.selector.FocusModeSelectorsKt.fixed;
import static io.fotoapparat.selector.LensPositionSelectorsKt.back;
import static io.fotoapparat.selector.PreviewFpsRangeSelectorsKt.highestFps;
import static io.fotoapparat.selector.ResolutionSelectorsKt.highestResolution;
import static io.fotoapparat.selector.SelectorsKt.firstAvailable;
import static io.fotoapparat.selector.SensorSensitivitySelectorsKt.highestSensorSensitivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.CameraConfiguration;
import io.fotoapparat.error.CameraErrorListener;
import io.fotoapparat.exception.camera.CameraException;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.view.CameraView;

/**
 * CameraActivity is an activity that allows the user to capture a photo using the device's camera
 * and save it to Firebase Firestore and local storage. The photo can also be resized and displayed.
 */
public class CameraActivity extends AppCompatActivity {
    private static final String LOGGING_TAG = "Fotoapparat";
    private CameraView cameraView;
    private Button capture;
    private Fotoapparat fotoapparat;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("PlayerDB/someUserID1/Monsters/someMonsterID/photo").document("local_image");

    /**
     * The camera configuration used to configure the device's camera.
     */
    private CameraConfiguration cameraConfiguration = CameraConfiguration
            .builder()
            .photoResolution(standardRatio(
                    highestResolution()
            ))
            .focusMode(firstAvailable(
                    continuousFocusPicture(),
                    autoFocus(),
                    fixed()
            ))
            .flash(firstAvailable(
                    autoRedEye(),
                    autoFlash(),
                    torch(),
                    off()
            ))
            .previewFpsRange(highestFps())
            .sensorSensitivity(highestSensorSensitivity())
            .build();

    /**
     * Called when the activity is created. Initializes the camera view and capture button, and
     * creates the Fotoapparat instance.
     *
     * @param savedInstanceState The saved instance state, if any.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view);
        Intent intent = getIntent();

        cameraView = findViewById(R.id.camera_preview);
        capture = findViewById(R.id.image_capture_button);

        fotoapparat = createFotoapparat();

        takePictureOnClick();
    }

    /**
     * Creates a new Fotoapparat instance with the desired configuration.
     */
    private Fotoapparat createFotoapparat() {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .previewScaleType(ScaleType.CenterCrop)
                .lensPosition(back())
                .cameraErrorCallback(new CameraErrorListener() {
                    @Override
                    public void onError(@NotNull CameraException e) {
                        Toast.makeText(CameraActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    /**
     * Sets the takePicture() method to be called when the capture button is clicked.
     */
    private void takePictureOnClick() {
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    /**
     * Captures a photo using the device's camera and saves it to Firebase
     */
    private void takePicture() {
        PhotoResult photoResult = fotoapparat.takePicture();

        photoResult
                .toBitmap()
                .whenDone(new WhenDoneListener<BitmapPhoto>() {
                    @Override
                    public void whenDone(@Nullable BitmapPhoto bitmapPhoto) {
                        if (bitmapPhoto == null) {
                            Log.e(LOGGING_TAG, "Couldn't capture photo.");
                            return;
                        }

                        // Saves the bitmap photo to Firestore database
                        toFirestore(bitmapPhoto.bitmap);
                        try {
                            Thread.sleep(10);
                            SwapIntent();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * SwapIntent method starts a new ScannerActivity for barcode scanning.
     * The method creates an Intent object to start a new activity and starts it.
     * @throws NullPointerException if CameraActivity context is null.
     * @return void.
     */
    private void SwapIntent() {
        Intent intent = new Intent(CameraActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the activity is starting or resuming.
     * This method starts the Fotoapparat camera and prepares it for use.
     * @see Fotoapparat#start()
     */
    @Override
    protected void onStart() {
        super.onStart();
        fotoapparat.start();
    }

    /**
     * Called when the activity is no longer visible to the user.
     * This method stops the Fotoapparat camera and releases its resources.
     * @see Fotoapparat#stop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    /**
     * Converts a Bitmap to a base64-encoded string and saves it to Firestore.
     * @param bitmap the Bitmap to be saved
     */
    private void toFirestore(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String base64Image = Base64.encodeToString(data, Base64.DEFAULT);

        HashMap<String, Object> imageMap = new HashMap<>();
        imageMap.put("base64", base64Image);

        docRef.set(imageMap);
    }

    // for testing
    private void getFireStoreImage() {
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String base64Image = documentSnapshot.getString("base64");
                        byte[] data = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        // Do something with the bitmap
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error retrieving document
                    }
                });
    }
}

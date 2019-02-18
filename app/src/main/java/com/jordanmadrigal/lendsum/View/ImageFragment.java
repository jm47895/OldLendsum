package com.jordanmadrigal.lendsum.View;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jordanmadrigal.lendsum.Adapter.ImageAdapter;
import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String LOG_TAG = ImageFragment.class.getSimpleName();

    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private GridView mGridView;
    private Button mNextBtn;
    private ImageButton mAddImageBtn, mCloseImageBtn;
    private List<Bitmap> mImageBitmaps;
    private OnActivityToFragmentListener mOnFragmentStateChange;
    private DataViewModel mDataModel;


    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGridView = view.findViewById(R.id.pictureGridView);
        mCloseImageBtn = view.findViewById(R.id.createPackImageCloseBtn);
        mNextBtn = view.findViewById(R.id.createPackNextBtn);
        mAddImageBtn = view.findViewById(R.id.createPackSelectImgBtn);

        mImageBitmaps = new ArrayList<>();


        mCloseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFragmentStateChange.setActionBarListener(R.string.app_name);
                mOnFragmentStateChange.setFragmentVisible(false);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //share images with AddPackFrag
                mDataModel.setSelectedImageArray(mImageBitmaps);

                Fragment addPackageFragment = new AddPackageFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.imageFragmentContainer, addPackageFragment).addToBackStack("imageFrag").commit();
            }
        });


        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnFragmentStateChange = (OnActivityToFragmentListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnFragmentInteraction Listener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();

        String storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();

        deleteAllPreviewFiles(storageDir);

        mOnFragmentStateChange = null;
    }

    private void deleteAllPreviewFiles(String fileDirPath){
        File fileDir = new File(fileDirPath);

        if(fileDir.isDirectory()){
            for(File file : fileDir.listFiles()){
                file.delete();
            }
        }
    }


    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;

            try{
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photoFile != null) {

                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                        "com.jordanmadrigal.lendsum.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            }else{
                Log.d(LOG_TAG, "create File failed");
            }

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }else{
            Toast.makeText(getActivity(), "No camera app present", Toast.LENGTH_SHORT).show();
        }


    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            File file = new File(mCurrentPhotoPath);

            Bitmap imageBitmap = null;
            try {

                Uri uri = Uri.fromFile(file);

                imageBitmap = handleSamplingAndRotation(getActivity(), uri);


            } catch (IOException e) {
                e.printStackTrace();
            }

            mImageBitmaps.add(imageBitmap);

            ImageAdapter adapter = new ImageAdapter(getActivity(), mImageBitmaps);

            mGridView.setAdapter(adapter);

        }
    }



    /**
     * required to handle image rotation by manufacturer when dealing with different devices
     */
    private Bitmap handleSamplingAndRotation(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


}

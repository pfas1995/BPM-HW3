package com.adc2018.bpmhw3;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adc2018.bpmhw3.adapter.BrandAdapter;
import com.adc2018.bpmhw3.utils.BPM3;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int GRANT_WRITE_EXTRNAL_STORAGE = 1;

    private ImageView picture;
    private Uri imageUri;
    private String imageEncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picture = findViewById(R.id.picture);
        Log.d(TAG, "onCreate: " + Build.BRAND);
    }

    /**
     * 调用相机demo
     * @param view
     */
    public void takePhoto(View view) {
        Log.d(TAG, "takePhoto: " + getExternalCacheDir());
        File outputImage = new File(getExternalCacheDir(), "Output_image.jpg");
        try {
            if(outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }
        catch(IOException e) {
            Log.e(TAG, "takePhoto: " + e.getMessage() );
        }
        if( Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.adc2018.bpmhw3.fileprovider", outputImage);
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024*4);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 从相册选择相片
     * 先检查有没有权限
     * @param view
     */
    public void choosePhoto(View view) {
        Log.d(TAG, "choosePhoto: " + "choose photo");
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, GRANT_WRITE_EXTRNAL_STORAGE );
        }
        else {
            openAlbum();
        }
    }

    /**
     * 打开相册
     */
    public void openAlbum() {
        Log.d(TAG, "openAlbum: " + "open album");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 获取运行时权限之后再打开相册
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GRANT_WRITE_EXTRNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            /**
             * 处理拍完照的
             * */
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = BrandAdapter.rotatePhotoAfterCamera(bitmap);
                        bitmap = BPM3.changeBitmapScale(bitmap, 1024, 768);
                        picture.setImageBitmap(bitmap);
//                        HttpRequest request = new HttpRequest();
//                        request.aliyunCardOCR(encodeImage(bitmap));
//                        request.xfyunCardOCR(encodeImage(bitmap));
                    }
                    catch (FileNotFoundException e) {
                        Log.e(TAG, "onActivityResult: " + e.getMessage() );
                    }
                }
                break;
            /**
             * 处理从相册选择照片
             */
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: " + "call album");
                    if(Build.VERSION.SDK_INT >= 19) {
                        handleImageOnkitKat(data);
                    }
                    else {
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }

    /**
     * 对图片进行 base64 编码
     * @param bitmap
     * @return
     */
    public String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bytes = baos.toByteArray();
        String encode = null;
        try {
            encode = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encodeImage: ", e);
        }
        Log.d(TAG, "encodeImage: encode size: " + encode.getBytes().length + " byte");
        Log.d(TAG, "encodeImage: " + encode);
        return encode;
    }



    /**
     * 对于系统版本大于4.4的设备获取相册图片
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnkitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d(TAG, "handleImageOnkitKat: " + uri.getAuthority() );
        if(DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document 类型的uri, 则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
            else if("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
            else if("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
        }
        else if("com.miui.gallery.open".equals(uri.getAuthority())){
            imagePath = uri.getPathSegments().get(1);
        }
        if(imagePath != null) {
            displayImage(imagePath);
        }
    }


    /**
     * 对于系统版本小于4.4的设备获取相册图片
     * @param data
     */
    public void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,  null);
        displayImage(imagePath);
    }

    /**
     * 根据uri 和 selection 获取图片的路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection,null,
                null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 展示图片
     * @param imagePath 图片路径
     */
    private void displayImage(String imagePath) {
        Log.d(TAG, "displayImage: imagePath: " +  imagePath);
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


}

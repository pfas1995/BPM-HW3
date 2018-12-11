package com.adc2018.bpmhw3.activity;

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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.adapter.device.BrandAdapter;
import com.adc2018.bpmhw3.entity.ocr.AliyunCardResult;
import com.adc2018.bpmhw3.entity.ocr.CardImage;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.ocr.OCRApi;
import com.adc2018.bpmhw3.network.api.ocr.OCRUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardIdentifyActivity extends AppCompatActivity {

    private static String TAG = CardIdentifyActivity.class.getSimpleName();

    private static final int ADJUST_ACTIVITY = 0;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int GRANT_WRITE_EXTRNAL_STORAGE = 3;

    private ImageView picture;
    private Uri imageUri;
    private String imageEncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardidentify);
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
            imageUri = FileProvider.getUriForFile(CardIdentifyActivity.this,
                    "com.adc2018.bpmhw3.fileprovider", outputImage);
        }

        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024*4);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(CardIdentifyActivity.this, "没有权限识别", Toast.LENGTH_LONG).show();
                        CardIdentifyActivity.this.setResult(RESULT_CANCELED);
                        finish();
                    }
                }).start();
    }

    /**
     * 从相册选择相片
     * 先检查有没有权限
     * @param view
     */
    public void choosePhoto(View view) {
        Log.d(TAG, "choosePhoto: " + "choose photo");
        if(ContextCompat.checkSelfPermission(CardIdentifyActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(CardIdentifyActivity.this,
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
            case ADJUST_ACTIVITY:
                Log.d(TAG, "onActivityResult: " + String.valueOf(resultCode));
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = BrandAdapter.rotatePhotoAfterCamera(bitmap);

                        picture.setImageBitmap(bitmap);
//                        callAliyunOCR(encodeImage(bitmap));
                        setContentView(R.layout.loading_layout);
                        callXfyunOCR(encodeImage(bitmap));
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
                break;
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

    public void startActivity2(View view) {
        Intent intent = new Intent(CardIdentifyActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    /**
     * 请求科大讯飞的名片识别
     * @param imageEncode
     */
    private void callXfyunOCR(String imageEncode) {
        final Retrofit retrofit = RetrofitTool.getRetrofit(OCRUtil.getXfyunOCRBaseUrl());
        final OCRApi api = retrofit.create(OCRApi.class);
        CardImage image = new CardImage();
        image.setImage(imageEncode);
        Call<ResponseBody> call = api.xfyunCard(image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                setContentView(R.layout.activity_cardidentify);
                if(response.isSuccessful()) {
                    //讯飞云识别成功
                    Card card = parseXfyunResponse(response.body());
                    if(card == null) {
                        Toast.makeText(CardIdentifyActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(CardIdentifyActivity.this, AdjustActivity.class);
                        intent.putExtra("card", card);
                        startActivityForResult(intent, ADJUST_ACTIVITY);
                    }
                }
                else {
                    Toast.makeText(CardIdentifyActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 请求阿里云的名片识别
     * @param imageEncode 图片的base64编码
     */
    private void callAliyunOCR(String imageEncode) {
        Retrofit retrofit = RetrofitTool.getRetrofit(OCRUtil.getAliyunOCRBaseUrl());
        final OCRApi api = retrofit.create(OCRApi.class);
        CardImage cardImage = new CardImage();
        cardImage.setImage(imageEncode);
        Call<AliyunCardResult> call = api.aliyunCard(cardImage);
        call.enqueue(new Callback<AliyunCardResult>() {
            @Override
            public void onResponse(Call<AliyunCardResult> call, Response<AliyunCardResult> response) {
                if(response.isSuccessful()) {
                    //阿里云识别成功
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AliyunCardResult> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public Card parseXfyunResponse(ResponseBody responseBody) {
        Card card = null;
        try {
            String response = responseBody.string();
            Log.d(TAG, "parseXfyunResponse: " + response);
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            Log.d(TAG, "parseXfyunResponse: " + code);
            if(Objects.equals(code, "0")){
                card = XfyunJsonToCard(jsonObject.getJSONObject("data"));
            }
            else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
                return card;
            }

        } catch (IOException e) {
            Log.e(TAG, "parseXfyunResponse: ", e);
        } catch (JSONException e) {
            Log.e(TAG, "parseXfyunResponse: ", e);
        }
        return card;
    }


    public Card XfyunJsonToCard(JSONObject jsonObject) {
        Card card = new Card();
        try {
            JSONArray jsonArray = null;
            if(jsonObject.has("formatted_name")) {
                jsonArray = jsonObject.getJSONArray("formatted_name");
                String name = jsonArray.getJSONObject(0).getString("item");
                card.setName(name);
            }
            if(jsonObject.has("organization")) {
                jsonArray = jsonObject.getJSONArray("organization");
                String company = jsonArray.getJSONObject(0).getJSONObject("item").getString("name");
                card.setCompany(company);
            }
            if(jsonObject.has("title")) {
                jsonArray = jsonObject.getJSONArray("title");
                String position = jsonArray.getJSONObject(0).getString("item");
                card.setPosition(position);
            }
            if(jsonObject.has("telephone")) {
                jsonArray = jsonObject.getJSONArray("telephone");
                String phone = jsonArray.getJSONObject(0).getJSONObject("item").getString("number");
                card.setPhone_number(phone);
            }
            if(jsonObject.has("label")) {
                jsonArray = jsonObject.getJSONArray("label");
                String address = jsonArray.getJSONObject(0).getJSONObject("item").getString("address");
                card.setAddress(address);
            }
            if(jsonObject.has("email")) {
                jsonArray = jsonObject.getJSONArray("email");
                String email = jsonArray.getJSONObject(0).getString("item");
                card.setEmail(email);
            }
            Log.d(TAG, "XfyunJsonToCard: " + card.toString());
        } catch (JSONException e) {
            Log.e(TAG, "XfyunJsonToCard: ", e);
        }
        return card;
    }


}

package com.dingpw.tool.camera;

import android.app.Activity;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dingpw.tool.camera.R;

public class CameraMainActivity extends Activity implements View.OnClickListener {
    private Button button = null;
    private ImageView imageView = null;
    private File sdcardTempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private AlertDialog dialog = null;
    private static final int RESULT_CODE_CAMERA = 0;
    private static final int RESULT_CODE_ALBUM = 1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_reset:
                this.imageView.setImageResource(R.drawable.ic_launcher);
                break;
            case R.id.iv_shower:
                if (this.dialog == null) {
                    this.dialog =
                            new AlertDialog.Builder(this).setItems(new String[] {"相机", "相册"},
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                // 指定调用相机拍照后照片的储存路径
                                                cameraintent
                                                        .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
                                                startActivityForResult(cameraintent, RESULT_CODE_CAMERA);
                                            } else {
                                                Intent intent = new Intent("android.intent.action.PICK");
                                                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                                        "image/*");
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
                                                startActivityForResult(intent, RESULT_CODE_ALBUM);
                                            }
                                        }
                                    }).create();
                }
                if (!this.dialog.isShowing()) {
                    this.dialog.show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        this.button = (Button) findViewById(R.id.bt_reset);
        this.imageView = (ImageView) findViewById(R.id.iv_shower);
        this.imageView.setOnClickListener(this);
        this.button.setOnClickListener(this);

        sdcardTempFile =
                new File(Environment.getExternalStorageDirectory().getPath(), "tmp_pic_"
                        + SystemClock.currentThreadTimeMillis() + ".jpg");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RESULT_CODE_CAMERA:// 当选择拍照时调用
                startPhotoZoom(Uri.fromFile(sdcardTempFile));
                break;
            case RESULT_CODE_ALBUM:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 3:// 返回的结果
                if (data != null) {
                    // setPicToView(data);
                    sentPicToNext(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(date) + ".jpg";
    }

    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo == null) {
                imageView.setImageResource(R.drawable.ic_launcher);
            } else {
                imageView.setImageBitmap(photo);
                // 设置文本内容为 图片绝对路径和名字
                // text.setText(tempFile.getAbsolutePath());
            }

            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] photodata = baos.toByteArray();
                System.out.println(photodata.toString());
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }
}

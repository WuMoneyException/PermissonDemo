package com.wzh.permissondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wzh.permissondemo.permission.IPermissionResult;
import com.wzh.permissondemo.permission.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 申请单个权限
     *
     * @param view
     */
    public void applyOne(View view) {
        PermissionHelper.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , 100, new IPermissionResult() {
                    @Override
                    public void applySuccess(int requestCode) {
                        Toast.makeText(MainActivity.this, "申请存储权限成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void applyFail(int requestCode, boolean isNeverAsk) {
                        if (isNeverAsk) {
                            //权限被拒绝
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("存储权限被拒绝了，请到设置中允许该权限")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PermissionHelper.goToAppSettings(MainActivity.this);
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                        }
                    }
                });
    }

    /**
     * 申请多个权限
     *
     * @param view
     */
    public void applyMore(View view) {
        PermissionHelper.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE}, 200, new IPermissionResult() {
            @Override
            public void applySuccess(int requestCode) {
                Toast.makeText(MainActivity.this, "申请权限成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void applyFail(int requestCode, boolean isNeverAsk) {
                if (isNeverAsk) {
                    //权限被拒绝
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("权限被拒绝了，请到设置中允许该权限")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionHelper.goToAppSettings(MainActivity.this);
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}

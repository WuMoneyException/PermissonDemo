package com.wzh.permissondemo.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员: Wzh.
 * 开发日期: 2018/4/23.
 * 开发描述: 权限申请
 */

public class PermissionHelper {

    private static IPermissionResult sIPermissionResult;

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode, IPermissionResult IPermissionResult) {
        request(activity, permissions, requestCode, IPermissionResult);
    }

    public static void requestPermissions(Fragment fragment, String[] permissions, int requestCode, IPermissionResult IPermissionResult) {
        request(fragment, permissions, requestCode, IPermissionResult);
    }

    /**
     * 请求权限
     *
     * @param o           activity或者fragment
     * @param permissions 权限
     * @param requestCode 请求码
     */
    private static void request(Object o, String[] permissions, int requestCode, IPermissionResult IPermissionResult) {
        sIPermissionResult = IPermissionResult;
        //系统是否是6.0以上的
        if (!isOverM()) {
            //允许
            if (null != IPermissionResult) {
                IPermissionResult.applySuccess(requestCode);
            }
            return;
        }
        Activity activity = null;
        if (o instanceof Activity) {
            activity = (Activity) o;
        } else if (o instanceof Fragment) {
            Fragment fragment = (Fragment) o;
            activity = fragment.getActivity();
        }
        if (null == activity) {
            //允许
            if (null != IPermissionResult)
                IPermissionResult.applySuccess(requestCode);
            return;
        }

        //查找要申请的权限是否不通过(只要有一个不通过，则执行拒绝)
        List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
        if (null == deniedPermissions || deniedPermissions.size() <= 0) {
            //允许
            if (null != IPermissionResult) {
                IPermissionResult.applySuccess(requestCode);
            }
            return;
        }

        //申请权限
        if (isOverM())
            activity.requestPermissions(permissions, requestCode);
    }

    /**
     * 是否是android m以上的系统
     */
    private static boolean isOverM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取申请未通过的集合
     *
     * @param context     context
     * @param permissions 权限
     * @return 未通过的权限集合
     */
    private static List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            //检查是否有拒绝的权限
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 进入应用的设置
     *
     * @param context 上下文
     */
    public static void goToAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }


    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 请求后的回调
     *
     * @param requestCode 请求码
     * @param permissions 权限
     */
    private static void requestResult(Object o, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Activity activity = null;
        if (o instanceof Activity) {
            activity = (Activity) o;
        } else if (o instanceof Fragment) {
            Fragment fragment = (Fragment) o;
            activity = fragment.getActivity();
        }
        int grantLength = grantResults.length;
        if (grantLength <= 0 || null == activity) return;
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantLength; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            boolean isNeverAsk = false;
            //拒绝
            if (isOverM()) {
                //在拒绝授权的情况下，shouldShowRequestPermissionRationale返回的是false，可以说明是用户选择了"不再询问"
                if (!activity.shouldShowRequestPermissionRationale(deniedPermissions.get(0))) {
                    isNeverAsk = true;
                }
            }

            if (null != sIPermissionResult)
                sIPermissionResult.applyFail(requestCode, isNeverAsk);
            return;
        }
        //允许
        if (null != sIPermissionResult)
            sIPermissionResult.applySuccess(requestCode);
    }

}

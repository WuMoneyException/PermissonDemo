package com.wzh.permissondemo.permission;

/**
 * 开发人员: Wzh.
 * 开发日期: 2018/4/23.
 * 开发描述: 权限申请结果回调
 */

public interface IPermissionResult {

    /**
     * 申请授权成功
     *
     * @param requestCode 请求码
     */
    void applySuccess(int requestCode);

    /**
     * 申请授权失败
     *
     * @param requestCode 请求码
     * @param isNeverAsk  是否不再询问
     */
    void applyFail(int requestCode, boolean isNeverAsk);
}

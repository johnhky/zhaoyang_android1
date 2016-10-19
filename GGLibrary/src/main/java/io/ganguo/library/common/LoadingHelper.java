package io.ganguo.library.common;

import android.content.Context;
import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Tony on 10/6/15.
 */
public class LoadingHelper {

    static MaterialDialog mMaterialDialog = null;

    /**
     * show loading
     */
    public static MaterialDialog showMaterLoading(Context context, String message) {
        return showMaterLoading(context, message, null);
    }

    /**
     * show loading
     */
    public static MaterialDialog showMaterLoading(final Context context, final String message, final DialogInterface.OnCancelListener listener) {
        if (context == null) {
            return null;
        }
        if (mMaterialDialog != null) {
            hideMaterLoading();
        }
        mMaterialDialog = new MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(message)
                .cancelable(listener != null)
                .cancelListener(listener)
                .show();

        return mMaterialDialog;
    }

    /**
     * hide loading
     */
    public static void hideMaterLoading() {
        try {
            if (mMaterialDialog != null && mMaterialDialog.getWindow() != null && mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
                mMaterialDialog = null;
            }
        } catch (Exception ignored) {

        }
    }

}

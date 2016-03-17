package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by aaron on 10/21/15.
 */
public class MyAdapter extends AuthorizeAdapter {

    @Override
    public void onCreate() {
        hideShareSDKLogo();
    }
}

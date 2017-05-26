package maojian.android.walnut.me;

import maojian.android.walnut.me.userdate.UserBean;

public interface MeView {

    void setPostSuccess(MyPostList myPostList,boolean likePost);
    void setUserBeanSuccess(UserBean userBean, boolean likePost);
    void setPostFail(String errInfo);
}

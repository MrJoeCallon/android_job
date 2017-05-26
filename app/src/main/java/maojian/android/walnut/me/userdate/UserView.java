package maojian.android.walnut.me.userdate;

import maojian.android.walnut.me.MyPostList;

public interface UserView {

    void setSuccess(UserBean userBean);


    void setFail(String errInfo);

    void setPostSuccess(MyPostList myPostList,boolean likePost);


    void setPostFail(String errInfo);
}

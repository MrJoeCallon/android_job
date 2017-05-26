package maojian.android.walnut.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017-05-21  17:03.
 * @version: 1.0
 */
public class LastInputEditText extends EditText {

    public LastInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastInputEditText(Context context) {
        super(context);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if(selStart==selEnd){//防止不能多选
            setSelection(getText().length());
        }

    }
}
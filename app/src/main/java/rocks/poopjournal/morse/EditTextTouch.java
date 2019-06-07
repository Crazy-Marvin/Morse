package rocks.poopjournal.morse;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class EditTextTouch extends android.support.v7.widget.AppCompatEditText {
    public EditTextTouch(Context context) {
        super(context);
    }

    public EditTextTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent rawEvent) {
            return false;
        }
    };
}

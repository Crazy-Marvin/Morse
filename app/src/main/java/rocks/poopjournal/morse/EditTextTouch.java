package rocks.poopjournal.morse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class EditTextTouch extends EditText {
    public final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent rawEvent) {
            return false;
        }
    };

    public EditTextTouch(Context context) {
        super(context);
    }

    public EditTextTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

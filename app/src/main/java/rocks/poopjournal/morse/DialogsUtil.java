package rocks.poopjournal.morse;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.LinearLayout;

public class DialogsUtil {


    public static Dialog showVerificationDialog(Activity activity){

        Dialog dialog;
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(activity.getResources().getColor(R.color.colorDialogTransclucent)));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_verification);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;

    }


}

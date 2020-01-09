package rocks.poopjournal.morse;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
import static android.hardware.Camera.Parameters.FLASH_MODE_ON;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;

public class MainActivity extends AppCompatActivity implements Camera.AutoFocusCallback {

    ImageView settings, history, flash;
    TextView buttonOne;
    TextView buttonTwo;
    RelativeLayout switchImageContainer;
    EditTextTouch input;
    RelativeLayout popularMorseSuggestionContainer;
    TextView output;
    ImageView copy;
    ImageView sound;
    RelativeLayout container, mic, fullscreen;
    int global_counter=0;
    RelativeLayout bottomNavigation;
    RelativeLayout flare_view;
    RelativeLayout morseInputContainer;
    RelativeLayout dot;
    RelativeLayout dash;
    RelativeLayout space;
    RelativeLayout makeInputVisible;
    RelativeLayout backspace;
    public static Camera camera = null;// has to be static, otherwise onDestroy() destroys it
    boolean visibilityCheck = false;
    ArrayList<String> popularMorse = new ArrayList<>();
    HashMap<String, String> popularMorseConversion = new HashMap<>();
    HashMap<String, String> popularMorseConversionText = new HashMap<>();
    String flashText = null;
    DBHelper helper;
    ImageView star;
    ArrayList<PhrasebookModel> arrayList;
    final AtomicBoolean textToMorse = new AtomicBoolean(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        settings = findViewById(R.id.settings);
        mic = findViewById(R.id.input_mic_container);
        fullscreen = findViewById(R.id.input_fullscreen_container);
        history = findViewById(R.id.history);
        buttonOne = findViewById(R.id.buttonOne);
        flare_view = findViewById(R.id.flare_view);
        buttonTwo = findViewById(R.id.buttonTwo);
        switchImageContainer = findViewById(R.id.switchImageContainer);
        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
        copy = findViewById(R.id.copyText);
        sound = findViewById(R.id.playAudio);
        flash = findViewById(R.id.flash);
        popularMorseSuggestionContainer = findViewById(R.id.bottom_suggestion_container);

        helper = new DBHelper(getApplicationContext());

        star = findViewById(R.id.star);
        popularMorse.add("...---...");
        popularMorse.add("-.-.--.--..");
        popularMorse.add(".--.....--.....--....--.----...--.-.---..---.....-");
        popularMorse.add(".-..--...");

        flare_view.setVisibility(View.GONE);
        popularMorseConversion.put("...---...","... --- ...");
        popularMorseConversion.put("-.-.--.--..","-.-. --.- -..");
        popularMorseConversion.put(".--.....--.....--....--.----...--.-.---..---.....-",".-- .... .- -   .... .- - ....   --. --- -..   .-- .-. --- ..- --. .... -");
        popularMorseConversion.put(".-..--...",".-. .- - ...");

        popularMorseConversionText.put("...---...","SOS");
        popularMorseConversionText.put("-.-.--.--..","CQD");
        popularMorseConversionText.put(".--.....--.....--....--.----...--.-.---..---.....-","What hath God wrought");
        popularMorseConversionText.put(".-..--...","rats");
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please give camera permission to use flash",Toast.LENGTH_SHORT).show();
            return;
        }
                turnOff();
                if (textToMorse.get()){
                    if (!TextUtils.isEmpty(output.getText().toString())){
                        String[] something =  TextUtils.split(output.getText().toString().trim().replaceAll("\\s+",""),"");
                        Log.d("test_string",output.getText().toString().trim().replace(" ","").replace("  ",""));
                        Log.d("test_string",".....");
                        Log.d("test_length_string",String.valueOf(something.length))
                        ;
                        for (String s: something){
                            Log.d("skkk",s);
                        }

                        int len = something.length;


                        int currentcounter =0;
                        for (String s : something) {
                            if (s.equals(".")) {
                                turnOn();
                                SystemClock.sleep(200);
                                turnOff();
                            } else if (s.equals("-")) {
                                turnOn();
                                SystemClock.sleep(600);
                                turnOff();
                            }
                        }

                    }
                }
                else {
                    if (!TextUtils.isEmpty(input.getText().toString())){
                        String[] something =  TextUtils.split(input.getText().toString().trim().replaceAll("\\s+",""),"");
                        Log.d("test_string",input.getText().toString().trim().replace(" ","").replace("  ",""));
                        Log.d("test_string",".....");
                        Log.d("test_length_string",String.valueOf(something.length))
                        ;
                        for (String s: something){
                            Log.d("skkk",s);
                        }

                        int len = something.length;


                        int currentcounter =0;
                        for (String s : something) {
                            if (s.equals(".")) {
                                turnOn();
                                SystemClock.sleep(200);
                                turnOff();
                            } else if (s.equals("-")) {
                                turnOn();
                                SystemClock.sleep(600);
                                turnOff();
                            }
                        }

                    }
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, PhraseBookActivity.class));
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"To be implemented in a future release", Toast.LENGTH_SHORT).show();
            }
        });
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"To be implemented in a future release", Toast.LENGTH_SHORT).show();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be implemented in a future release", Toast.LENGTH_SHORT).show();
            }
        });
        container = findViewById(R.id.container);
        bottomNavigation = findViewById(R.id.bottomLayout);
        morseInputContainer = findViewById(R.id.morseInputContainer);

        makeInputVisible = findViewById(R.id.input_visible_container);
        makeInputVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morseInputContainer.getVisibility()==View.GONE)
                    morseInputContainer.setVisibility(View.VISIBLE);
                else {
                    morseInputContainer.setVisibility(View.GONE);
                }
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("something", output.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Text copied",Toast.LENGTH_SHORT).show();
            }
        });

        addKeyboardVisibilityListener(container, new KeyboardVisibilityCallback() {
            @Override
            public void onChange(boolean isVisible) {
               visibilityCheck = isVisible;
            }
        });


        final ImageView flare = findViewById(R.id.flare);
        final Runnable hide = new Runnable() {
            @Override
            public void run() {
                flare_view.setVisibility(View.GONE);

                Log.d("flare","set to gone");
            }
        };


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(output.getText().toString().trim()) && !TextUtils.isEmpty(input.getText().toString().trim())){
                    if (textToMorse.get())
                        helper.addPhrase(input.getText().toString(),output.getText().toString());
                    else {
                        helper.addPhrase(output.getText().toString(),input.getText().toString());
                    }
                }

                arrayList = helper.getAllPhrases();
                checkForStarColor();
            }
        });
        flare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToMorse.get()){
                    if (!TextUtils.isEmpty(output.getText().toString())){

                      /*  new Thread() {
                            public void run() {
                                String[] something =  TextUtils.split(output.getText().toString().trim().replaceAll("\\s+",""),"");
                                for (String s : something) {

                                    try {

                                        flare_view.setVisibility(View.VISIBLE);
                                        sleep(500);
                                        flare_view.setVisibility(View.INVISIBLE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                        }.run();


*/

                      flare_view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                      flashText = output.getText().toString().trim().replace(" ","").replaceAll("\\s+","");
                      global_counter=0;
                      flash_display();





                    }
                }
                else {
                    if (!TextUtils.isEmpty(input.getText().toString())){

                      /*  new Thread() {
                            public void run() {
                                String[] something =  TextUtils.split(output.getText().toString().trim().replaceAll("\\s+",""),"");
                                for (String s : something) {

                                    try {

                                        flare_view.setVisibility(View.VISIBLE);
                                        sleep(500);
                                        flare_view.setVisibility(View.INVISIBLE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                        }.run();


*/

                        flare_view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                        flashText = input.getText().toString().trim().replace(" ","").replaceAll("\\s+","");
                        global_counter=0;
                        flash_display();





                    }
                }
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToMorse.get()){
                    if (!TextUtils.isEmpty(output.getText().toString())){
                       String[] something =  TextUtils.split(output.getText().toString().trim().replaceAll("\\s+",""),"");
                       Log.d("test_string",output.getText().toString().trim().replace(" ","").replace("  ",""));
                       Log.d("test_string",".....");
                       Log.d("test_length_string",String.valueOf(something.length))
                       ;
                       for (String s: something){
                           Log.d("skkk",s);
                       }

                       int len = something.length;
                       final int[] tracks = new int[len];
                        int counter = 0;
                       for (String s: something){
                           if (s.equals(".")){
                               tracks[counter] = R.raw.dot;
                               counter++;
                           }
                            else if (s.equals("-")){
                               tracks[counter] = R.raw.dash;
                               counter++;
                           }
                       }

                       Log.d("test_length",String.valueOf(tracks.length));
                        final int[] trackcounter = {0};
                       MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[trackcounter[0]]);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                if (trackcounter[0] < tracks.length-3) {
                                    trackcounter[0]++;
                                    mp = MediaPlayer.create(getApplicationContext(), tracks[trackcounter[0]]);
                                    mp.setOnCompletionListener(this);
                                    mp.start();
                                }
                            }
                        });
                        mediaPlayer.start();
                    }
                }
                else{
                    if (!TextUtils.isEmpty(input.getText().toString())){
                        String[] something =  TextUtils.split(input.getText().toString().trim().replaceAll("\\s+",""),"");
                        Log.d("test_string",input.getText().toString().trim().replace(" ","").replace("  ",""));
                        Log.d("test_string",".....");
                        Log.d("test_length_string",String.valueOf(something.length))
                        ;
                        for (String s: something){
                            Log.d("skkk",s);
                        }

                        int len = something.length;
                        final int[] tracks = new int[len];
                        int counter = 0;
                        for (String s: something){
                            if (s.equals(".")){
                                tracks[counter] = R.raw.dot;
                                counter++;
                            }
                            else if (s.equals("-")){
                                tracks[counter] = R.raw.dash;
                                counter++;
                            }
                        }

                        Log.d("test_length",String.valueOf(tracks.length));
                        final int[] trackcounter = {0};
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[trackcounter[0]]);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                if (trackcounter[0] < tracks.length-3) {
                                    trackcounter[0]++;
                                    mp = MediaPlayer.create(getApplicationContext(), tracks[trackcounter[0]]);
                                    mp.setOnCompletionListener(this);
                                    mp.start();
                                }
                            }
                        });
                        mediaPlayer.start();
                    }
                }
            }
        });
        switchImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToMorse.get()){
                    buttonOne.setText("MORSE");
                    buttonTwo.setText("TEXT");
                    input.setText("");
                    output.setText("");
                    textToMorse.set(false);
                    input.setOnTouchListener(otl);

                    bottomNavigation.setVisibility(View.VISIBLE);
                    morseInputContainer.setVisibility(View.VISIBLE);

                    if (visibilityCheck)
                        hideKeyboard(MainActivity.this);
                    dot = findViewById(R.id.inputDotContainer);
                    dash = findViewById(R.id.inputDashContainer);
                    space = findViewById(R.id.input_space_container);
                    backspace = findViewById(R.id.input_clear_container);

                    dot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append(".");
                            Log.d("test","clicked");
                        }
                    });
                    dash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("-");
                        }
                    });
                    space.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append(" ");
                        }
                    });
                    backspace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (input.getText().toString().length()==0)
                                return;
                            input.setText(input.getText().toString().substring(0,input.getText().toString().length()-1));
                            input.setSelection(input.getText().toString().length());

                        }
                    });
                    backspace.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            input.setText("");
                            return false;
                        }
                    });
                }
                else { input.setText("");
                    output.setText("");
                    buttonOne.setText("TEXT");
                    buttonTwo.setText("MORSE");
                    textToMorse.set(true);
                    input.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            v.setOnTouchListener(input.mOnTouchListener);
                            return false;
                        }
                    });
                    bottomNavigation.setVisibility(View.GONE);
                    morseInputContainer.setVisibility(View.GONE);


                }
            }
        });


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())){
                    output.setText("");
                    return;
                }

                if (textToMorse.get()){
                    output.setText("");
                    String text = input.getText().toString();
                    String[] letters = text.split("");
                    for (String letter : letters) {
                        output.append(morseEncode(letter)+" ");
                    }


                }
                else {
                    if (popularMorse.contains(input.getText().toString())){
                     /*   final Dialog confirm = DialogsUtil.showVerificationDialog(MainActivity.this);
                        TextView original, converted, discard, yes;
                        original = confirm.findViewById(R.id.successTV);
                        converted = confirm.findViewById(R.id.descTV);
                        discard = confirm.findViewById(R.id.discardBtnVerify);
                        yes = confirm.findViewById(R.id.import_playlist);


                        original.setText(input.getText().toString());
                        converted.setText("Do you mean " + popularMorseConversionText.get(input.getText().toString())+ " " + popularMorseConversion.get(input.getText().toString()) +"?");
                        discard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirm.dismiss();
                            }
                        });
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                input.setText(popularMorseConversion.get(input.getText().toString()));
                                Toast.makeText(getApplicationContext(),"Changed morse",Toast.LENGTH_SHORT).show();
                                confirm.dismiss();
                            }
                        });*/
                     Log.d("popular_morse","true");
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)input.getLayoutParams();
                        setMargins(input,params.leftMargin,params.topMargin,params.rightMargin,2);
                        input.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_top_suggestion));
                        popularMorseSuggestionContainer.setVisibility(View.VISIBLE);

                        TextView suggestionTV = findViewById(R.id.suggestion_text_tv);
                        TextView replace = findViewById(R.id.replace_suggestion);
                        TextView ignore = findViewById(R.id.ignore_suggestion);

                        suggestionTV.setText("Did you mean " + popularMorseConversion.get(input.getText().toString())+  " (" + popularMorseConversionText.get(input.getText().toString())  +")?");
                        change(suggestionTV.getText().toString(),popularMorseConversion.get(input.getText().toString()), suggestionTV);

                        replace.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                input.setText(popularMorseConversion.get(input.getText().toString()));
                                input.setSelection(input.getText().length());
                                Toast.makeText(getApplicationContext(),"Changed morse",Toast.LENGTH_SHORT).show();
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)input.getLayoutParams();
                                setMargins(input,params.leftMargin,params.topMargin,params.rightMargin,20);
                                input.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_morse));
                                popularMorseSuggestionContainer.setVisibility(View.GONE);
                            }
                        });

                        ignore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)input.getLayoutParams();
                                setMargins(input,params.leftMargin,params.topMargin,params.rightMargin,20);
                                input.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_morse));
                                popularMorseSuggestionContainer.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)input.getLayoutParams();
                        setMargins(input,params.leftMargin,params.topMargin,params.rightMargin,20);
                        input.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_morse));
                        popularMorseSuggestionContainer.setVisibility(View.GONE);
                    }

                    output.setText("");
                    String text = input.getText().toString();
                    String[] letters = text.split("\\s");
                    for (String morse: letters){
                        output.append(morseDecode(morse));
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                checkForStarColor();
            }
        });
    }

    private void checkForStarColor() {
        boolean isTrue = false;
        if (arrayList==null || arrayList.size()==0){
            star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_border_black_24dp));
            star.setColorFilter(Color.parseColor("#7DD3D8"));
            Log.d("debug_star","did not match new text: " + input.getText().toString() + ", setting star to off");
            return;
        }
        if (textToMorse.get()){
            for (PhrasebookModel model: arrayList){
                if (model.text.trim().equals(input.getText().toString().trim())){
                    star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_black_24dp));
                    star.setColorFilter(Color.parseColor("#F9A825"));
                    Log.d("debug_star","matched new text: " + input.getText().toString() + ", setting star to on");
                    return;
                }
                else {

                    isTrue = true;
                }

            }
            if (isTrue){
                star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_border_black_24dp));
                star.setColorFilter(Color.parseColor("#7DD3D8"));
                Log.d("debug_star","did not match new text: " + input.getText().toString() + ", setting star to on");
            }
        }
        else {
            for (PhrasebookModel model: arrayList){
                if (model.text.trim().equals(output.getText().toString().trim())){
                    star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_black_24dp));
                    star.setColorFilter(Color.parseColor("#F9A825"));
                    Log.d("debug_star","matched new text: " + output.getText().toString() + ", setting star to on");
                    return;
                }
                else {

                    isTrue = true;
                }

            }
            if (isTrue){
                star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_border_black_24dp));
                star.setColorFilter(Color.parseColor("#7DD3D8"));
                Log.d("debug_star","did not match new text: " + output.getText().toString() + ", setting star to on");
            }
        }

    }

    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };


    void flash_display() {

        flare_view.setVisibility(View.VISIBLE);
        flare_view.bringToFront();
        flare_view.setTag(flare_view.getVisibility());
        Log.d("flare","in flash dispaly");

    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            final String[] something =  flashText.trim().replaceAll("\\s+","").split("(?!^)");
            Log.d("flare","flash_text: " + flashText);
            Log.d("flare","counter: " + global_counter);
            if (global_counter==something.length){
                flare_view.setVisibility(View.GONE);
                flare_view.setTag(flare_view.getVisibility());
                flare_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);return;
            }

          /*  Log.d("flare","here");
            Log.d("flare","current character: " + something[global_counter]);
            Log.d("flare","string" + flashText);
            for (int i=0;i <something.length; i++){
                Log.d("flare","string_char_array " + something[i]);
            }*/
            if ((int) flare_view.getTag() == View.VISIBLE) {
                Log.d("flare","here in if");
                if (something[global_counter].equals(".")){
                    Log.d("flare","dot");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("flare","dot_post_delayed");
                            flare_view.setVisibility(View.GONE);
                            global_counter++;
                            flare_view.setTag(flare_view.getVisibility());
                            if (global_counter!=something.length){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        flash_display();
                                    }
                                }, 150);
                            }

                        }
                    }, 350);
                }
                else   if (something[global_counter].equals("-")) {
                    final Handler handler = new Handler();
                    Log.d("flare","dash");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {   Log.d("flare","dash_post_delayed");
                            flare_view.setVisibility(View.GONE);
                            global_counter++;
                            flare_view.setTag(flare_view.getVisibility());
                            if (global_counter!=something.length){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        flash_display();
                                    }
                                }, 150);
                            }
                        }
                    }, 1000);
                }
                else {
                    Log.d("flare","can't identifyt");
                }

            }
        }
    };
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    static String morseEncode(String x)
    {
        // refer to the Morse table
        // image attached in the article
        x = x.toLowerCase();
        switch (x)
        {
            case "a":
                return ".-";
            case "b":
                return "-...";
            case "c":
                return "-.-.";
            case "d":
                return "-..";
            case "e":
                return ".";
            case "f":
                return "..-.";
            case "g":
                return "--.";
            case "h":
                return "....";
            case "i":
                return "..";
            case "j":
                return ".---";
            case "k":
                return "-.-";
            case "l":
                return ".-..";
            case "m":
                return "--";
            case "n":
                return "-.";
            case "o":
                return "---";
            case "p":
                return ".--.";
            case "q":
                return "--.-";
            case "r":
                return ".-.";
            case "s":
                return "...";
            case "t":
                return "-";
            case "u":
                return "..-";
            case "v":
                return "...-";
            case "w":
                return ".--";
            case "x":
                return "-..-";
            case "y":
                return "-.--";
            case "  ":
                return " ";
            case "z":
                return "--..";
            case " ":
                return  " ";
        }
        return "";
    }

    static String morseDecode(String morse)
    {
        // refer to the Morse table
        // image attached in the article
        switch (morse) {
            case ".-":
                return "a";
            case "-...":
                return "b";
            case "-.-.":
                return "c";
            case "-..":
                return "d";
            case ".":
                return "e";
            case "..-.":
                return "f";
            case "--.":
                return "g";
            case "....":
                return "h";
            case "..":
                return "i";
            case ".---":
                return "j";
            case "-.-":
                return "k";
            case ".-..":
                return "l";
            case "--":
                return "m";
            case "-.":
                return "n";
            case "---":
                return "o";
            case ".--.":
                return "p";
            case "--.-":
                return "q";
            case ".-.":
                return "r";
            case "...":
                return "s";
            case "-":
                return "t";
            case "..-":
                return "u";
            case "...-":
                return "v";
            case ".--":
                return "w";
            case "-..-":
                return "x";
            case "-.--":
                return "y";
            case "--..":
                return "z";
            case "   ":
                return " ";
        }

        return "";
    }

    public void addKeyboardVisibilityListener(
            final View rootView, final KeyboardVisibilityCallback callback){
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();

                        rootView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootView.getRootView().getHeight();
                        int keypadHeight = screenHeight - r.bottom;

                        if (keypadHeight > screenHeight * 0.15) {
                            callback.onChange(true);
                        } else {
                            callback.onChange(false);
                        }
                    }
                });
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {

    }


    public interface KeyboardVisibilityCallback{
        /**
         * On change of keyboard visibility
         * @param isVisible : Make keyboard visible.
         */
        void onChange(boolean isVisible);
    }

    public void turnOn() {

        camera = Camera.open();
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(getFlashOnParameter());
            camera.setParameters(parameters);

            camera.setPreviewTexture(new SurfaceTexture(0));

            camera.startPreview();
            camera.autoFocus(this);

        } catch (Exception e) {
            // We are expecting this to happen on devices that don't support autofocus.
        }
    }
    private String getFlashOnParameter() {
        List<String> flashModes = camera.getParameters().getSupportedFlashModes();

        if (flashModes.contains(FLASH_MODE_TORCH)) {
            return FLASH_MODE_TORCH;
        } else if (flashModes.contains(FLASH_MODE_ON)) {
            return FLASH_MODE_ON;
        } else if (flashModes.contains(FLASH_MODE_AUTO)) {
            return FLASH_MODE_AUTO;
        }
        throw new RuntimeException();
    }
    public void turnOff() {
        try {
            camera.stopPreview();
            camera.release();
            camera = null;
        } catch (Exception e) {
            // This will happen if the camera fails to turn on.
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]);

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            flare_view.setVisibility(View.GONE);
        }


        @Override
        protected void onPreExecute() {
           flare_view.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
    void change(String s, String newSuggestion, TextView t){
        int i = s.indexOf(newSuggestion);
        SpannableStringBuilder sb = new SpannableStringBuilder(s);
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(),R.color.colorMorse)), i, i+newSuggestion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int k = s.indexOf("(");
        sb.setSpan(new StyleSpan(Typeface.ITALIC), k+1,  k+popularMorseConversionText.get(input.getText().toString()).length()+1, 0);
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(),R.color.colorMorse)), k+1,  k+popularMorseConversionText.get(input.getText().toString()).length() +1, 0);
        t.setText(sb);
    }


    @Override
    protected void onResume() {
        super.onResume();

        arrayList = helper.getAllPhrases();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        helper = new DBHelper(getApplicationContext());
        arrayList = helper.getAllPhrases();
        // Checks the orientation of the scree
        }

}

package rocks.poopjournal.morse;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ImageView settings;
    TextView buttonOne;
    TextView buttonTwo;
    RelativeLayout switchImageContainer;
    EditTextTouch input;
    TextView output;
    ImageView copy;
    ImageView sound;
    RelativeLayout container;

    RelativeLayout bottomNavigation;
    RelativeLayout morseInputContainer;
    RelativeLayout dot;
    RelativeLayout dash;
    RelativeLayout space;
    RelativeLayout makeInputVisible;
    RelativeLayout backspace;

    boolean visibilityCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AtomicBoolean textToMorse = new AtomicBoolean(true);
        settings = findViewById(R.id.settings);
        buttonOne = findViewById(R.id.buttonOne);
        buttonTwo = findViewById(R.id.buttonTwo);
        switchImageContainer = findViewById(R.id.switchImageContainer);
        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
        copy = findViewById(R.id.copyText);
        sound = findViewById(R.id.playAudio);

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


        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToMorse.get()){
                    if (!TextUtils.isEmpty(output.getText().toString())){
                       String[] something =  TextUtils.split(output.getText().toString().trim().replaceAll("\\s+",""),"");
                       Log.d("test_string",output.getText().toString().trim().replace(" ","").replace("  ",""));
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
                                if (trackcounter[0] < tracks.length) {
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
                if (textToMorse.get()){
                    output.setText("");
                    String text = input.getText().toString();
                    String[] letters = text.split("");
                    for (String letter : letters) {
                        output.append(morseEncode(letter)+" ");
                    }
                }
                else {
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

            }
        });
    }
    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true; // the listener has consumed the event
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



    public interface KeyboardVisibilityCallback{
        /**
         * On change of keyboard visibility
         * @param isVisible : Make keyboard visible.
         */
        void onChange(boolean isVisible);
    }

}

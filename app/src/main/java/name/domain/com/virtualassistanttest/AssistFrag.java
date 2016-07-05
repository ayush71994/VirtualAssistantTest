package name.domain.com.virtualassistanttest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
    public class AssistFrag extends Fragment implements TextToSpeech.OnInitListener,RecognitionListener{

    private TextToSpeech tts;
    private ImageView img;
    private TextView liste;
    private Bundle temp;
    Communicator communicator;
    GetContacts obj;
    Button listen;
    // Speech recognizer instance
    private SpeechRecognizer speech = null;
    public AssistFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator=(Communicator)(context);
    }

    TextToSpeech.OnUtteranceCompletedListener listener=new TextToSpeech.OnUtteranceCompletedListener() {
        @Override
        public void onUtteranceCompleted(String s) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getSpeechRecognizer().cancel();
                    startVoiceRecognitionCycle();
                }
            });
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tts = new TextToSpeech(getActivity(),this);
        temp=savedInstanceState;
        View view=inflater.inflate(R.layout.fragment_assist, container, false);
        listen=(Button)view.findViewById(R.id.listen);
        img=(ImageView)view.findViewById(R.id.imageView);
        liste=(TextView)view.findViewById(R.id.textView);
        listen.setVisibility(View.VISIBLE);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceRecognitionCycle();
                //listen.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("SpeechToText","onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.d("SpeechToText","onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        String message;
        boolean restart = true;
        listening(false);
        switch (error)
        {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                restart = false;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                restart = false;
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                //listen.setVisibility(View.VISIBLE);
                message = "No match";
                speakOut("I dont understand");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                speakOut("Please say something");
                break;
            default:
                message = "Not recognised";
                break;
        }
        Log.d("SpeechToText","onError code:" + error + " message: " + message);
/*
        if (restart) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getSpeechRecognizer().cancel();
                    startVoiceRecognitionCycle();
                }
            });
        }*/
    }

    @Override
    public void onResults(Bundle results) {
        listening(false);
        StringBuilder scores = new StringBuilder();
        for (int i = 0; i < results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES).length; i++) {
            scores.append(results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[i] + " ");
        }
        Log.d("SpeechToText","onResults: " + results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) + " scores: " + scores.toString());
        // Return to the container activity dictation results
        if (results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null) {
            for (int i = 0; i < results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size(); i++)
                Log.v("SpeechToText",results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(i));
            //mCallback.onResults(this, results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));

        }

        String com = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        com = com.toUpperCase();
        if (com.matches("(.*)CALL(.*)")||com.matches("(.*)SPEAK"))
        {
            boolean fl = true;
            //Obtain name
            int ind;
            String name;
            ind=com.indexOf("CALL");
            if(ind==-1)
            {
                ind=com.indexOf("SPEAK");
                name=com.substring(ind + 5, com.length()).trim();
            }
            else {
                name = com.substring(ind + 4, com.length()).trim();
            }
            if(name==null)
            {
                speechError(1);
                return;

            }
            else if(name.matches("TO(.*)")||name.matches("WITH(.*)"))
            {

                name=com.substring(com.indexOf("TO")+2,com.length()).trim();
                Log.i("NAME:",name);
            }
            Log.i("NAME:",name);
            String fname = (name.indexOf(" ") != -1) ? name.substring(0, name.indexOf(" ")) : name;
            fname = fname.trim();
            obj = new GetContacts(getActivity());
            ArrayList<Person> per = obj.checkIfContactPresent(name);
            if(per.size()==0)
            {
                speechError(2);
                return;
            }
            for(int i=0;i<per.size();i++) {
                Log.i("Person", per.get(i).getName() + ":" + per.get(i).getPhoneNo());
            }
            if(per.size()==1)
            {
                communicator.showDialog(per.get(0));
                return;
            }
            else
            {
                communicator.sendPersonList(per);
            }
            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+per.get(0).getPhoneNo()));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION","NOPE");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);*/
            //System.out.println(name+name.length());
        }
        else
        {
            speakOut("Please say call followed by name");
        }

    }

    private void speechError(int i) {
        switch(i)
        {
            case 1:speakOut("No name Spoken");
                    break;
            case 2:speakOut("Name not recognised");
                    break;
        }
    }

    //public ArrayList<Person> checkSurName(String sur){
    //    String surn;
    //}

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {
                        Log.i("Start:", s);
                    }

                    @Override
                    public void onDone(String s) {

                        Log.i("Done:", s);
                        //speakOut("I'm done for the day");
                        /*if (processStart) {
                            speech.startListening(intent);
                        } else {
                            ...
                        }*/
                        //flag=false;
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                getSpeechRecognizer().cancel();
                                startVoiceRecognitionCycle();
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {
                        Log.i("Start:", s);
                    }
                });


                } else {
                    //btnSpeak.setEnabled(true);
                    //speakOutWelcomeMessage();
                }
            }
            else
            {
                tts.setOnUtteranceCompletedListener(listener);
            }

        int result = tts.setLanguage(Locale.UK);

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        }
        else {
                Log.e("TTS", "Initilization Failed!");
            }

        }

    /** Class private functions */
    /*private void speakOutWelcomeMessage() {

        int i = 0;
        String text = "Welcome to the virtual assistant app";
        tts.setPitch((float) 1.5);
        tts.setSpeechRate((float) 1);

        //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,temp,"ID="+i++);
    }*/

    private void speakOut(String text) {

        /*HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");*/

/*
        String text = txtText.getText().toString();*/
        listening(false);
        StringTokenizer sent=new StringTokenizer(text,".");
        //List<String> sentence= Arrays.asList(text.split("."));
        tts.setPitch((float) 1.5);
        tts.setSpeechRate((float) 0.65);
        int i=0;
        while(sent.hasMoreTokens()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.v("TextToSpeech","Using UtteranceProgressListener");
            tts.speak(sent.nextToken(), TextToSpeech.QUEUE_FLUSH,temp,"ID="+i++);
            }
            else
            {
                tts.speak(sent.nextToken(),TextToSpeech.QUEUE_FLUSH,null);
            }
            //while(tts.isSpeaking());
        }
        //Log.i("SEnt:", sent.nextToken());
    }
    // Lazy instantiation method for getting the speech recognizer
    private SpeechRecognizer getSpeechRecognizer(){
        if (speech == null) {
            speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
            speech.setRecognitionListener(this);
        }

        return speech;
    }
    /**
     * Fire an intent to start the voice recognition process.
     */
    public void listening(boolean flag)
    {
        if(flag)
        {
            img.setImageResource(R.drawable.listen);
            liste.setText("LISTENING");
            listen.setVisibility(View.INVISIBLE);
        }
        else
        {
            img.setImageResource(R.drawable.nlisten);
            liste.setText("NOT LISTENING");
            listen.setVisibility(View.VISIBLE);
        }
    }
    public void startVoiceRecognitionCycle()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        getSpeechRecognizer().startListening(intent);
        listening(true);
    }
    /**
     * Stop the voice recognition process and destroy the recognizer.
     */
    public void stopVoiceRecognition()
    {
        //speechTimeout.cancel();
        if (speech != null) {
            speech.destroy();

            speech = null;
        }

    }


    public interface Communicator{
        public void sendPersonList(ArrayList<Person> per);
        public void showDialog(Person per);
    }
}

package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;

public class AudioFullViewActivity extends AppCompatActivity {

    TextView name, description;
    String type, link;
    SeekBar seekBar;
    Button back;
    CircleImageView image;
    private MediaPlayer mediaPlayer;
    FloatingActionButton play, pause;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_full_view);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                finish();
            }
        });

        name=findViewById(R.id.name);
        description=findViewById(R.id.description);
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setClickable(false);

        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);

        type = getIntent().getStringExtra("type");
        if (type.equals("Internal")) {
            link = Constraints.BASE_IMAGE_URL + Constraints.AUDIO + getIntent().getStringExtra("link");
        } else {
            link = getIntent().getStringExtra("link");
        }
        Log.i("link", link);

        name.setText(getIntent().getExtras().getString("name"));
        description.setText(getIntent().getExtras().getString("desc"));
//        mediaPlayer = MediaPlayer.create(this, Uri.parse("http://searchgurbani.com/audio/sggs/1.mp3"));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                playAudio();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekBar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                seekBar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);

                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void playAudio() {

//        String audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mediaPlayer.pause();
        } catch (Exception e){

        }
    }
}
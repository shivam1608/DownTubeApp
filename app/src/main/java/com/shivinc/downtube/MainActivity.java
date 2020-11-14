package com.shivinc.downtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //Declare all xml values
    private EditText inputUrl;
    private Button bttSearch;
    private TextView textTitle;
    private Button bttVideo;
    private Button bttAudio;
    private TextView textProgress;
    private Button coffee;
    private Button bttPauseplay;
    private TextView instruction;
    private TextView textCredits;
    //video streaming
    private VideoView videoView;
    private File dir;
    private VideoInfo info;
    public YoutubeDLRequest request;

    // to chk current status
    private String url;
    private String title= "Video not Found";
    private Boolean playing = false;
    private Boolean isMusicDownloading=false;
    private long currTime2;
    private long currTime3;

    //double press back logic
    private long currTime;
    private int wait=2000; //2000 mili sec = 2 seconds

    private Animation down_to_top;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check For Permission
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) {
            dir.mkdir();
        }

         down_to_top=AnimationUtils.loadAnimation(this,R.anim.down_animation);
        //call ini
        ini();
        //call iniButtons
        iniButton();
        //ini library
        try {
            YoutubeDL.getInstance().init(getApplication());
            FFmpeg.getInstance().init(getApplication());
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }
        textCredits.setAnimation(down_to_top);

    }

    private void ini() {
        //ini all xml component
        inputUrl = findViewById(R.id.inputUrl);
        bttSearch = findViewById(R.id.bttSearch);
        textTitle = findViewById(R.id.textTitle);
        bttVideo = findViewById(R.id.bttVideo);
        bttAudio = findViewById(R.id.bttAudio);
        textProgress = findViewById(R.id.textProgress);
        coffee = findViewById(R.id.button4);
        videoView = findViewById(R.id.videoView);
        instruction=findViewById(R.id.textInstructions);
        bttPauseplay = findViewById(R.id.bttPausePlay);
        textCredits=findViewById(R.id.textCredits);
        textProgress.setText("");

    }

    private void iniButton() {
        //ini all onClick Buttons
        bttSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        bttVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call download
                bttAudio.setVisibility(View.GONE);
                bttVideo.setVisibility(View.GONE);
                textProgress.setText("Downloading...");
                    downloadActivity da=new downloadActivity("video");
                    da.start();

            }
        });
        bttAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    isMusicDownloading=true;
                    bttAudio.setVisibility(View.GONE);
                    bttVideo.setVisibility(View.GONE);
                    textProgress.setText("Downloading...");
                    downloadActivity da=new downloadActivity("music");
                    da.start();

            }
        });
        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               intent=new Intent(MainActivity.this , Donation.class);
               startActivity(intent);
            }
        });
        bttPauseplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playing == true) {
                    videoView.pause();
                    playing = false;
                } else {
                    videoView.resume();
                    playing = true;
                }
            }
        });

    }

    //to search and display results
    private void search() {
            if(textTitle.getVisibility() == View.VISIBLE){
                textTitle.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                bttVideo.setVisibility(View.GONE);
                bttAudio.setVisibility(View.GONE);
                bttPauseplay.setVisibility(View.GONE);

            }
            instruction.setVisibility(View.GONE);
       if(!inputUrl.getText().toString().trim().isEmpty()){
           if(inputUrl.getText().toString().trim().contains("http")){
               textProgress.setText("Please Wait...");
               dataOverBack dob=new dataOverBack();
               dob.start();
           }
           else{
               inputUrl.setError("Enter Valid Url");
           }
       }
       else{
           inputUrl.setError("Please Enter Url");
       }

    }

    //to recive data over background and set it to display
    public class dataOverBack extends Thread {
        @Override
        public void run() {
            url = inputUrl.getText().toString().trim();

                if (inputUrl.getText().toString().trim().contains("http")) {
                    request = new YoutubeDLRequest(url);
                    request.addOption("-o", dir.getAbsolutePath() + "/%(title)s.%(ext)s");
                    request.addOption("-f", "best");
                    try {
                        info = YoutubeDL.getInstance().getInfo(request);
                        title=info.getTitle();
                    } catch (YoutubeDLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bttSearch.setClickable(false);


                            //set visibility
                            textTitle.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.VISIBLE);
                            bttVideo.setVisibility(View.VISIBLE);
                            bttAudio.setVisibility(View.VISIBLE);
                            bttPauseplay.setVisibility(View.VISIBLE);
                            textTitle.setText(title);
                            if(!title.equals("Video not Found")){
                                videoView.setVideoURI(Uri.parse(info.getUrl()));
                            }
                            else{
                                videoView.setVisibility(View.GONE);
                                bttVideo.setVisibility(View.GONE);
                                bttAudio.setVisibility(View.GONE);
                                bttPauseplay.setVisibility(View.GONE);
                            }
                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    videoView.start();
                                    playing = true;
                                }
                            });
                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    videoView.pause();
                                }
                            });
                            textProgress.setText("");
                            bttSearch.setClickable(true);
                        }

                    });
                }



        }
    }

    //start download of required type as per input
    public class downloadActivity extends Thread{
        String type;
        downloadActivity(String Type){
            type=Type;
        }
        @Override
        public void run() {
            request=new YoutubeDLRequest(url);
            if(type.equals("video")){
                request.addOption("-f","best");
            }
            else{
                request.addOption("--extract-audio");
                request.addOption("--audio-format", "mp3");
            }
            request.addOption("-o", dir.getAbsolutePath() + "/%(title)s.%(ext)s");
            try {
                YoutubeDL.getInstance().execute(request, new DownloadProgressCallback() {
                    @Override
                    public void onProgressUpdate(float progress, long etaInSeconds) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    bttSearch.setClickable(false);

                                textProgress.setText(progress+"%"+" Downloaded");
                                if(progress==100.0){
                                    if(isMusicDownloading==false){
                                        textProgress.setText("Download Completed");
                                        bttSearch.setClickable(true);
                                    }
                                    else{
                                        //calling audio working
                                        textProgress.setText("Working on Audio...");
                                        audioWorking aw=new audioWorking();
                                        aw.start();
                                    }
                                }
                            }
                        });
                    }
                });
            } catch (YoutubeDLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //complex calculations to wait until video converts into audio
    class audioWorking extends Thread{
        @Override
        public void run() {
            int videoTime=info.getDuration();
            long wait=((long)videoTime/7)*1000;
            currTime2=System.currentTimeMillis();
            while(currTime2+wait > currTime3){
                currTime3=System.currentTimeMillis();
            }
            if(currTime2+wait == currTime3){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textProgress.setText("Download Completed");
                        isMusicDownloading=false;
                        bttSearch.setClickable(true);
                    }
                });}

        }
    }

    @Override
    public void onBackPressed() {
        if(currTime+wait > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
        else {
            LayoutInflater inflater =getLayoutInflater();
            View layout=inflater.inflate(R.layout.custom_toast , (ViewGroup) findViewById(R.id.toast_root));
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,50);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
        currTime=System.currentTimeMillis();
        }
    }





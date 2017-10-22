/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sanskritbasics;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener mm  =new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            rele();
        }
    };
    private AudioManager mAudio;
    AudioManager.OnAudioFocusChangeListener asChange =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if ( i == AUDIOFOCUS_LOSS_TRANSIENT|| i == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    else if (i == AudioManager.AUDIOFOCUS_GAIN){
                        mediaPlayer.start();
                    }
                    else if (i == AUDIOFOCUS_LOSS){
                        rele();
                    }
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        mAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "रक्तः", R.drawable.color_red,R.raw.red));
        words.add(new Word("yellow", "पीत", R.drawable.color_mustard_yellow,R.raw.yellow));
        words.add(new Word("light brown", "रासभधूसर", R.drawable.color_dusty_yellow,R.raw.lightbrown));
        words.add(new Word("green", "हरितः", R.drawable.color_green,R.raw.green));
        words.add(new Word("brown", "कपिलः", R.drawable.color_brown,R.raw.brown));
        words.add(new Word("gray", "कापोत", R.drawable.color_gray,R.raw.grey));
        words.add(new Word("black", "कृष्णः अथवा श्यामः ", R.drawable.color_black,R.raw.black));
        words.add(new Word("white", "श्वेतः अथवा अर्जुनः ", R.drawable.color_white,R.raw.white));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = words.get(i);
                rele();
                int ree = mAudio.requestAudioFocus(asChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (ree == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.hasAudio());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mm);
                }
            }


        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        rele();
    }
    private void rele(){
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer=null;
        mAudio.abandonAudioFocus(asChange);


    }
}

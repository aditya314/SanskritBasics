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

public class PhrasesActivity extends AppCompatActivity {

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
        words.add(new Word("Welcome", "स्वागतम्",R.raw.welcome));
        words.add(new Word("Hello/Good Day", "नमस्ते ",R.raw.hello));
        words.add(new Word("Good morning", "शुभ प्रभातम्",R.raw.goodmorning));
        words.add(new Word("Good night", "शुभ रात्रि ",R.raw.goodnight));
        words.add(new Word("What is Your Name? ", "किं तव नाम ",R.raw.whatisyourname));
        words.add(new Word("I am Richard", "अहम् रिचर्ड",R.raw.richard));
        words.add(new Word("My name is Julia", "जूलिया अहम्/जूलिया अस्मि",R.raw.julia));
        words.add(new Word("How are you?", "कथमस्ति भवान्",R.raw.howareyou));
        words.add(new Word("Reply to 'How are you?", "अहं कुशली ",R.raw.replyhwru));
        words.add(new Word("Long time no see", "चिरान्न दृष्टम् ",R.raw.lngtymnoc));
        words.add(new Word("Where are you from?", "भवान् कुत्रत्य: ",R.raw.werrufrm));
        words.add(new Word("Pleased to meet you", "भवता सह संयोग: सन्तोषकर:",R.raw.plsdmeetu));
        words.add(new Word("Have a nice day", "सुदिनमस्तु ",R.raw.hvniceday));
        words.add(new Word("Do you speak Sanskrit?", "भवान् संस्कृतं भाषते वा?",R.raw.douspksanskrit));
        words.add(new Word("Yes, a little", "आं, स्वल्पम्",R.raw.slttl));
        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

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

                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.hasAudio());
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

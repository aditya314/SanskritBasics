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

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager mAudio;
    private MediaPlayer.OnCompletionListener mm = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            rele();
        }
    };
    AudioManager.OnAudioFocusChangeListener asChange =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if (i == AUDIOFOCUS_LOSS_TRANSIENT || i == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaPlayer.start();
                    } else if (i == AUDIOFOCUS_LOSS) {
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
        words.add(new Word("father", "पिता", R.drawable.family_father, R.raw.father));
        words.add(new Word("mother", "माता", R.drawable.family_mother, R.raw.mother));
        words.add(new Word("son", "पुत्रः", R.drawable.family_son, R.raw.son));
        words.add(new Word("daughter", "पुत्री", R.drawable.family_daughter, R.raw.daughter));
        words.add(new Word("elder brother", "ज्येष्ठभ्राता", R.drawable.family_older_brother, R.raw.elderbrother));
        words.add(new Word("younger brother", "कनिष्ठभ्राता", R.drawable.family_younger_brother, R.raw.youngerbrother));
        words.add(new Word("elder sister", "ज्येष्ठभगिनी", R.drawable.family_older_sister, R.raw.eldersister));
        words.add(new Word("younger sister", "कनिष्ठभगिनी", R.drawable.family_younger_sister, R.raw.youngersister));
        words.add(new Word("maternal grandmother", "मातामही", R.drawable.family_grandmother, R.raw.maternalgrandmother));
        words.add(new Word("maternal grandfather", "मातामहः", R.drawable.family_grandfather, R.raw.maternalgrandfather));
        words.add(new Word("paternal grandmother", "पितामही", R.drawable.family_grandmother, R.raw.paternalgrandmother));
        words.add(new Word("paternal grandfather", "पितामहः", R.drawable.family_grandfather, R.raw.paternalgrandfather));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);

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

                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.hasAudio());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mm);
                }
            }


        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        rele();
    }

    private void rele() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        mAudio.abandonAudioFocus(asChange);

    }
}

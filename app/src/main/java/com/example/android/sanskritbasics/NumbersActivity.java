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

public class NumbersActivity extends AppCompatActivity {

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
        words.add(new Word("one", "एकम्", R.drawable.number_one, R.raw.one));
        words.add(new Word("two", "द्वे", R.drawable.number_two, R.raw.two));
        words.add(new Word("three", " त्रीणि", R.drawable.number_three, R.raw.three));
        words.add(new Word("four", "चत्वारि", R.drawable.number_four, R.raw.four));
        words.add(new Word("five", "पञ्च ", R.drawable.number_five, R.raw.five));
        words.add(new Word("six", "षट्", R.drawable.number_six, R.raw.six));
        words.add(new Word("seven", "सप्त", R.drawable.number_seven, R.raw.seven));
        words.add(new Word("eight", "अष्ट ", R.drawable.number_eight, R.raw.eight));
        words.add(new Word("nine", "नव", R.drawable.number_nine, R.raw.nine));
        words.add(new Word("ten", "दश", R.drawable.number_ten, R.raw.ten));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

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

                    mediaPlayer = MediaPlayer.create(NumbersActivity.this, word.hasAudio());
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
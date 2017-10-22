package com.example.android.sanskritbasics;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation, a sanskrit translation, and an image for that word.
 */
public class Word {

    /** Default translation for the word */
    private String mDefaultTranslation;

    /** sanskrit translation for the word */
    private String msanskritTranslation;

    /** Image resource ID for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private int mAudio;

    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param sanskritTranslation is the word in the sanskrit language
     */
    public Word(String defaultTranslation, String sanskritTranslation,int aud) {
        mDefaultTranslation = defaultTranslation;
        msanskritTranslation = sanskritTranslation;
        mAudio = aud;
    }

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param sanskritTranslation is the word in the sanskrit language
     * @param imageResourceId is the drawable resource ID for the image associated with the word
     *
     */
    public Word(String defaultTranslation, String sanskritTranslation, int imageResourceId,int aud) {
        mDefaultTranslation = defaultTranslation;
        msanskritTranslation = sanskritTranslation;
        mImageResourceId = imageResourceId;
        mAudio = aud;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the sanskrit translation of the word.
     */
    public String getsanskritTranslation() {
        return msanskritTranslation;
    }

    /**
     * Return the image resource ID of the word.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
    public int hasAudio(){
        return mAudio;
    }
}
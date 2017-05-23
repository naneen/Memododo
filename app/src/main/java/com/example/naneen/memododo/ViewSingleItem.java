package com.example.naneen.memododo;

/**
 * Created by naneen on 5/24/2017 AD.
 */

public class ViewSingleItem {
    private String Word, Meaning, Image_URL;

    public ViewSingleItem(String word, String meaning, String image_URL) {
        // the same name with DB
        Word = word;
        Meaning = meaning;
        Image_URL = image_URL;
    }

    public ViewSingleItem() {
        // constructor
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getMeaning() {
        return Meaning;
    }

    public void setMeaning(String meaning) {
        Meaning = meaning;
    }
}

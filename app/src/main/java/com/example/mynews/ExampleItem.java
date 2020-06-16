package com.example.mynews;

public class ExampleItem {
    private String mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mArticleid;

    public ExampleItem(String imageResource, String text1, String text2,String text3,String article){
        mImageResource=imageResource;
        mText1=text1;
        mText2=text2;
        mText3=text3;
        mArticleid=article;
    }
    public String getImageResource(){
        return mImageResource;
    }
    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
    public String getText3(){
        return mText3;
    }
    public String getArticleid(){
        return mArticleid;
    }
}

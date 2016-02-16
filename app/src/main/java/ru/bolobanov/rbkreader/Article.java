package ru.bolobanov.rbkreader;

/**
 * Created by Bolobanov Nikolay on 11.12.15.
 */
public class Article {
    public final String mTitle;
    public final String mGuid;
    public final String mLink;

    /**
     *
     * @param pTitle
     * @param pGuid
     * @param pLink
     */
    public Article(String pTitle, String pGuid, String pLink){
        mTitle = pTitle;
        mGuid = pGuid;
        mLink = pLink;
    }
}

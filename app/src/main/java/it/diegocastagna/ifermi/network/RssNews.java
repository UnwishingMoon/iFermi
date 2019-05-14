package it.diegocastagna.ifermi.network;

public class RssNews {
    private String title;
    private String iconId;
    private String newsDescription;

    public RssNews() {
    }

    /**
     * Returns Title of the news
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns Id of the Icon
     * @return String
     */
    public String getIconId() {
        return iconId;
    }

    /**
     * Returns news description
     * @return String
     */
    public String getNewsDescription() {
        return newsDescription;
    }

    /**
     * Set the title
     * @param title Title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the icon Id
     * @param iconId Icon Id to set
     */
    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    /**
     * Set the Description
     * @param newsDescription Description to set
     */
    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }
}

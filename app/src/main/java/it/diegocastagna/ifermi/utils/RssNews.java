package it.diegocastagna.ifermi.utils;

/**
 * It is the news
 */
public class RssNews {
    private String title;
    private String iconId;
    private String longDesc;
    private String shortDesc;

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

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
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
}

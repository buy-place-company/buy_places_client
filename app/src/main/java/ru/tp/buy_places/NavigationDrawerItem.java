package ru.tp.buy_places;

/**
 * Created by home on 15.03.2015.
 */
public class NavigationDrawerItem {
    String title;
    int icon;

    NavigationDrawerItem(){
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

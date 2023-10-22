package frgp.utn.edu.ar.controllers.data.repository;

public class CustomMenuItem  {
    private int iconId;
    private String title;
    private int fragmentId;

    public CustomMenuItem (int iconId, String title, int fragmentId) {
        this.iconId = iconId;
        this.title = title;
        this.fragmentId = fragmentId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return title;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public int getDestinationId() {
        return fragmentId;
    }
}
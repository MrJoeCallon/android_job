package maojian.android.walnut.utils.eventbus;

public class AddLocationEvent {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AddLocationEvent(String title) {
        this.title = title;
    }
}

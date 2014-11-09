package planit.planit.event;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class EventItem {
    public String eventHash;
    public String title;
    public String description;
    public String time;
    public String location;
    public String creator;
    public String image;

    public EventItem(String eventHash, String title, String description, String time, String location, String creator, String image)
    {
        this.eventHash = eventHash;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
        this.creator = creator;
        this.image = image;
    }
}

package planit.planit.friends;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class FriendItem {
    public long friendID;
    public String email;
    public String friendName;
    public String imgURI;
    public FriendItem(long friendID, String email, String friendName, String imgURI)
    {
        this.friendID = friendID;
        this.email = email;
        this.imgURI = imgURI;
        this.friendName = friendName;
    }
}

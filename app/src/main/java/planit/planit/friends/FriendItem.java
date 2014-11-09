package planit.planit.friends;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class FriendItem {
    public int friendID;
    public String friendName;
    public String imgURI;
    public FriendItem(int friendID, String friendName, String imgURI)
    {
        this.friendID = friendID;
        this.imgURI = imgURI;
        this.friendName = friendName;
    }
}

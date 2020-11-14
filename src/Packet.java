import java.io.Serializable;

public class Packet implements Serializable {
    public Boolean success;
    public String message;

    public Packet(Boolean success, String message) {
        this.success = false;
        this.message = message;
    }
}

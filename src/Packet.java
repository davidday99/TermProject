import java.io.Serializable;

public class Packet implements Serializable {
    public Object message;

    public Packet(Object message) {

        this.message = message;
    }
}

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessRMI extends Remote {
    Packet Send(Packet p) throws RemoteException;
    //Packet Receive() throws RemoteException;
}

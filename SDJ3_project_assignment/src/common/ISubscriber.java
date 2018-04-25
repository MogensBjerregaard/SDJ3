package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISubscriber extends Remote{
	void updateSubscriber(String subjectList, Subject subject) throws RemoteException;
}

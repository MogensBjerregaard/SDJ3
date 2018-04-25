package tier1.station1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.ISubscriber;
import common.Subject;
import tier2.businessserver.BusinessServerController;
import tier2.businessserver.IBusinessServer;

public class Station1Controller extends UnicastRemoteObject implements ISubscriber{

	private static final long serialVersionUID = 1L;
	private Station1View view;
	private static final String registryName = "Station1";
	private IBusinessServer businessServer;
	
	public Station1Controller() throws RemoteException, MalformedURLException, NotBoundException{
		this.businessServer = BusinessServerController.getRemoteObject();
		this.view = new Station1View(this);
		this.view.setVisible(true);
		bindToRegistry();
		businessServer.subscribe(this, Subject.CARS);
		businessServer.updateView(registryName+" connected");
	}
	public void inputCar(String chassisNumber, double weight, String model) {
		try {
			businessServer.enqueueCar(chassisNumber, weight, model);
		} catch (RemoteException e) {
			view.notifyUserError("Enqueueing error\n"+e.getMessage());
		}
	}

	private void bindToRegistry(){
		try {
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error binding "+registryName+" to registry.\nCheck if the Business Server is running and restart "+registryName+"\n"+e.getMessage());
		}	
	}
	@Override
	public void updateSubscriber(String subjectList, Subject subject) throws RemoteException {
		if (subject.equals(Subject.CARS)) {
			view.updateEnqueuedCarsList(subjectList);
		}
		
	}

}



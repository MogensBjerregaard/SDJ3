package tier1.dismantlingstationclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Subject;
import tier2.businessserver.IBusinessServer;

public class DismantlingStationClientController extends UnicastRemoteObject implements ISubscriber{

	private static final long serialVersionUID = 1L;
	private DismantlingStationClientView view;
	private static final String registryName = "DismantlingStationClient";
	private IBusinessServer businessServer;
	
	public DismantlingStationClientController(IBusinessServer businessServer)
			throws RemoteException, MalformedURLException, NotBoundException {
		this.businessServer = businessServer;
		this.view = new DismantlingStationClientView(this);
		this.view.setVisible(true);
		this.bindToRegistry();
		this.businessServer.subscribe(this, Subject.CARS, Subject.CARPARTS, Subject.PALLETS);
		this.businessServer.message(registryName+" connected");
	}

	private void bindToRegistry(){
		try {
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error binding "+registryName+" to registry.\nCheck if the Business Server is running and restart "+registryName+"\n"+e.getMessage());
		}
	}

	@Override
	public void updateSubscriber(String message, Subject subject)
			throws RemoteException {
		if (subject.equals(Subject.CARS)) view.updateEnqueuedCarsList(message);
		if (subject.equals(Subject.CARPARTS)) view.updateCarPartsList(message);
		if (subject.equals(Subject.PALLETS)) view.updatePalletsList(message);
	}

	public void dequeueCar() {
		try {
			Car car = businessServer.getNextCarToBeDismantled();
			view.loadDequeuedCar(car);
		} catch (RemoteException e) {
			this.view.notifyUserError("Unable to dequeue car");
		}
	}

	public void inputCarPart(Car loadedCar, String registrationNumber, String partType, double partWeight) {
		CarPart carPart = new CarPart(registrationNumber, loadedCar, partType, partWeight);
		try {
			businessServer.registerCarPart(carPart);
		} catch (RemoteException e) {
			System.out.println("Station 2 - failed creating new carpart on business server");
		}
	}

	public void generatePallets(String carPartType) {
		try {
			businessServer.generatePallets(carPartType);
		} catch (RemoteException e) {
			view.notifyUserError("Error generating pallets on business server");
		}
	}
}

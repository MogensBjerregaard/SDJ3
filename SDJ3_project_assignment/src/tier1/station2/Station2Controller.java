package tier1.station2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import tier2.IBusinessServer;

public class Station2Controller extends UnicastRemoteObject implements ISubscriber{

	private static final long serialVersionUID = 1L;
	private Station2View view;
	private static final String registryName = "Station2";
	private IBusinessServer businessServer;
	public Station2Controller()throws RemoteException {
		this.view = new Station2View(this);
		this.view.setVisible(true);
		this.bindToRegistry();
		this.connectBusinessServer();
	}
	private void bindToRegistry(){
		try {
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error binding "+registryName+" to registry.\nCheck if the Business Server is running and restart "+registryName+"\n"+e.getMessage());
		}
	}
	private void connectBusinessServer() throws RemoteException {
		try {
			this.businessServer = (IBusinessServer) Naming.lookup("rmi://localhost/BusinessServer");
			this.businessServer.subscribeToCarQueue(this);
			this.businessServer.subscribeToCarPartsQueue(this);
			this.businessServer.subscribeToPalletsQueue(this);
			this.businessServer.updateView(registryName+" connected");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Failed connecting to BusinessServer"+e.getMessage());
		}
	}
	@Override
	public void updateEnqueuedCarList(String message) throws RemoteException {
		view.updateEnqueuedCarsList(message);
	}
	@Override
	public void updateCarPartsList(String message) throws RemoteException {
		view.updateCarPartsList(message);
	}
	@Override
	public void updatePalletsList(String message) throws RemoteException {
		view.updatePalletsList(message);
	}
	public void dequeueCar() {
		try {
			Car car = businessServer.dequeueCar();
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
	public void generatePallets(boolean wheelSelected, boolean doorSelected, boolean seatSelected, boolean engineSelected,
			boolean steeringwheelSelected) {
		try {
			businessServer.generatePallets(wheelSelected,doorSelected,seatSelected,engineSelected,steeringwheelSelected);
		} catch (RemoteException e) {
			view.notifyUserError("Error generating pallets on business server");
		}
	}

}

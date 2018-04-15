package tier2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;
import tier3.IDataServer;

public class BusinessServerController extends UnicastRemoteObject implements IBusinessServer {
	private static final long serialVersionUID = 1L;
	private BusinessServerView view;
	private IDataServer dataServer;
	private static final String registryName = "BusinessServer";
	private LinkedBlockingQueue<Car> dismantlingQueue;
	private LinkedBlockingQueue<CarPart> carPartQueue;
	private ArrayList<Pallet> palletQueue;
	private ArrayList<ISubscriber> carQueueSubscribers;
	private ArrayList<ISubscriber> carPartQueueSubscribers;
	private ArrayList<ISubscriber> palletQueueSubscribers;
	private int palletRegistrationNumberCount;

	public BusinessServerController() throws RemoteException{
		this.view = new BusinessServerView();
		this.dismantlingQueue = new LinkedBlockingQueue<>();
		this.carPartQueue = new LinkedBlockingQueue<>();
		this.carQueueSubscribers = new ArrayList<>();
		this.carPartQueueSubscribers = new ArrayList<>();
		this.palletQueueSubscribers = new ArrayList<>();
		this.palletRegistrationNumberCount = 0;
		this.palletQueue = new ArrayList<>();
		this.bindToRegistry();
		this.connectDataServer();
		this.updateView("Registry naming rebind using '"+registryName+"' successful");
		this.updateView(registryName+" is running");
	}
	@Override
	public void updateView(String message) {
		LocalDateTime timePoint = LocalDateTime.now();
		this.view.updateTextArea(timePoint+": "+message);
	}
	private void bindToRegistry(){
		try {
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error binding "+registryName+" to registry.\nCheck if the Data Server is running and restart "+registryName+"\n"+e.getMessage());
		}
	}
	private void connectDataServer() throws RemoteException {
		try {
			this.dataServer = (IDataServer) Naming.lookup("rmi://localhost/DataServer");
			this.updateView("Connected to DataServer");
			this.dataServer.updateView(registryName+" connected");
			this.palletRegistrationNumberCount = this.dataServer.getNextPalletRegistrationNumber();
			//dataServer.initiateQueues() //load data from DB to businessServer on startup
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Failed connecting to DataServer"+e.getMessage());
		}
	}
	@Override
	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException {
		Car car = new Car(chassisNumber, weight, model);
		this.dismantlingQueue.add(car);
		this.dataServer.insertCar(car);
		this.updateCarQueueSubscribers();
		this.updateView("Enqueued car: "+model+"("+chassisNumber+") "+weight+"kg");
	}
	@Override
	public Car dequeueCar() throws RemoteException {
		if (this.dismantlingQueue.isEmpty()) {
			throw new RemoteException();
		}
		Car car = this.dismantlingQueue.remove();
		this.dataServer.deleteCarFromQueue(car);
		this.updateCarQueueSubscribers();
		this.updateView("Dequeued car: "+car.getModel()+"("+car.getChassisNumber()+") "+car.getWeight()+"kg");
		return car;
	}
	@Override
	public void registerCarPart(CarPart carPart) throws RemoteException {
		this.dataServer.insertCarPart(carPart);
		this.carPartQueue.add(carPart);
		this.updateCarPartQueueSubscribers();
		this.updateView("Registered carpart: "+carPart.getType()+"("+carPart.getRegistrationNumber()+"), "+carPart.getWeight()+"kg, from "+carPart.getCar().getModel()+"("+carPart.getCar().getChassisNumber()+")");
	}
	@Override
	public void packageProduct(Product product) throws RemoteException {
		// TODO Auto-generated method stub

	}
	@Override
	public void subscribeToCarQueue(ISubscriber subscriber) throws RemoteException {
		this.carQueueSubscribers.add(subscriber);
		this.updateCarQueueSubscribers();
	}
	@Override
	public void subscribeToCarPartsQueue(ISubscriber subscriber) throws RemoteException {
		this.carPartQueueSubscribers.add(subscriber);
		this.updateCarPartQueueSubscribers();
	}
	@Override
	public void subscribeToPalletsQueue(ISubscriber subscriber) throws RemoteException {
		this.palletQueueSubscribers.add(subscriber);
		this.updatePalletQueueSubscribers();
	}
	private void updateCarQueueSubscribers() {
		String message = new String();
		for (Car car : this.dismantlingQueue) {
			message+=car.getModel()+"("+car.getChassisNumber()+")\n";
		}
		for (ISubscriber subscriber : this.carQueueSubscribers) {
			try {
				subscriber.updateEnqueuedCarList(message);
			} catch (Exception e) {
				this.carQueueSubscribers.remove(subscriber);
			}
		}
	}
	private void updateCarPartQueueSubscribers() {
		String message = new String();
		int wheelCount=0, doorCount=0, seatCount=0, engineCount=0, steeringCount=0;
		for (CarPart carPart : this.carPartQueue) {
			switch (carPart.getType()) {
			case "Wheel": wheelCount++;
			break;
			case "Door": doorCount++;
			break;
			case "Seat": seatCount++;
			break;
			case "Engine": engineCount++;
			break;
			case "Steeringwheel": steeringCount++;
			break;
			default:
				break;
			}
		}
		message = "Wheel: "+wheelCount+"pcs\nDoors: "+doorCount+"pcs\nSeat: "+seatCount+"pcs\nEngine: "+engineCount+"pcs\nStWheel: "+steeringCount+"pcs";
		for (ISubscriber subscriber : this.carPartQueueSubscribers) {
			try {
				subscriber.updateCarPartsList(message);
			} catch (Exception e) {
				this.carPartQueueSubscribers.remove(subscriber);
			}
		}
	}
	private void updatePalletQueueSubscribers() {
				String message = new String();
				for (Pallet pallet : palletQueue) {
					message+=pallet.toString()+"\n";
				}
				for (ISubscriber subscriber : palletQueueSubscribers) {
					try {
						subscriber.updatePalletsList(message);
					} catch (Exception e) {
						palletQueueSubscribers.remove(subscriber);
					}
				}
	}
	
	//takes selected car part types from car part queue and place them on pallets. fills 1 pallet until max weight or until empty of car part
	@Override
	public void generatePallets(boolean wheelSelected, boolean doorSelected, boolean seatSelected,
			boolean engineSelected, boolean steeringwheelSelected) throws RemoteException {
		double maxWeight = 500;
		double wheelPalletWeight=0, doorPalletWeight=0, seatPalletWeight=0, enginePalletWeight=0, steeringPalletWeight=0;
		Pallet wheelPallet = new Pallet("Wheels", maxWeight);
		Pallet doorPallet = new Pallet("Doors", maxWeight);
		Pallet seatPallet = new Pallet("Seats", maxWeight);
		Pallet enginePallet = new Pallet("Engines", maxWeight);
		Pallet steeringPallet = new Pallet("Steeringwheels", maxWeight);

		for (CarPart carPart : this.carPartQueue) {
			if(wheelSelected&&carPart.getType().equals("Wheel")&&(wheelPalletWeight<maxWeight)) {
				if ((wheelPalletWeight+carPart.getWeight())<=maxWeight) {
					wheelPallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
			if(doorSelected&&carPart.getType().equals("Door")&&(doorPalletWeight<maxWeight)) {
				if ((doorPalletWeight+carPart.getWeight())<=maxWeight) {
					doorPallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
			if(seatSelected&&carPart.getType().equals("Seat")&&(seatPalletWeight<maxWeight)) {
				if ((seatPalletWeight+carPart.getWeight())<=maxWeight) {
					seatPallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
			if(engineSelected&&carPart.getType().equals("Engine")&&(enginePalletWeight<maxWeight)) {
				if ((enginePalletWeight+carPart.getWeight())<=maxWeight) {
					enginePallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
			if(steeringwheelSelected&&carPart.getType().equals("Steeringwheel")&&(steeringPalletWeight<maxWeight)) {
				if ((steeringPalletWeight+carPart.getWeight())<=maxWeight) {
					steeringPallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
		}
		if(!wheelPallet.getParts().isEmpty()) {
			wheelPallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(wheelPallet);
			this.dataServer.insertPallet(wheelPallet);
		}
		if(!doorPallet.getParts().isEmpty()) {
			doorPallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(doorPallet);
			this.dataServer.insertPallet(doorPallet);
		}
		if(!seatPallet.getParts().isEmpty()) {
			seatPallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(seatPallet);
			this.dataServer.insertPallet(seatPallet);
		}
		if(!enginePallet.getParts().isEmpty()) {
			enginePallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(enginePallet);
			this.dataServer.insertPallet(enginePallet);
		}
		if(!steeringPallet.getParts().isEmpty()) {
			steeringPallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(steeringPallet);
			this.dataServer.insertPallet(steeringPallet);
		}
		updateCarPartQueueSubscribers();
		updatePalletQueueSubscribers();
	}

}




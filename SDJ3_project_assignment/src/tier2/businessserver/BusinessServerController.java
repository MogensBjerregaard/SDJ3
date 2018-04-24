package tier2.businessserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
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
	private LinkedBlockingQueue<Product> productQueue;
	private ArrayList<Pallet> palletQueue;
	private ArrayList<ISubscriber> carQueueSubscribers;
	private ArrayList<ISubscriber> carPartQueueSubscribers;
	private ArrayList<ISubscriber> palletQueueSubscribers;
	private ArrayList<ISubscriber> productQueueSubscribers;
	private int palletRegistrationNumberCount;
	private int productRegistrationNumberCount;
	
	public BusinessServerController(IDataServer dataServer) throws RemoteException{
		this.dataServer = dataServer;
		this.view = new BusinessServerView();
		this.dismantlingQueue = new LinkedBlockingQueue<>();
		this.carPartQueue = new LinkedBlockingQueue<>();
		this.productQueue = new LinkedBlockingQueue<>();
		this.carQueueSubscribers = new ArrayList<>();
		this.carPartQueueSubscribers = new ArrayList<>();
		this.palletQueueSubscribers = new ArrayList<>();
		this.productQueueSubscribers = new ArrayList<>();
		this.palletRegistrationNumberCount = 0;
		this.productRegistrationNumberCount = 0;
		this.palletQueue = new ArrayList<>();
		this.bindToRegistry();
		this.initiateSystem();
	}
	public static IBusinessServer getRemoteObject() throws MalformedURLException, RemoteException, NotBoundException {
		return (IBusinessServer) Naming.lookup("rmi://localhost/"+registryName);
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
	private void initiateSystem() throws RemoteException {
		try {
			this.updateView("Connected to DataServer");
			this.dataServer.updateView(registryName+" connected");
			this.updateView("Registry naming rebind using '"+registryName+"' successful");
			this.updateView(registryName+" is running");
			this.palletRegistrationNumberCount = this.dataServer.getNextPalletRegistrationNumber();
			this.productRegistrationNumberCount = this.dataServer.getNextProductRegistrationNumber();
			dataServer.initiateQueues(); //load data from DB to businessServer on startup
		} catch (RemoteException e) {
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
		dataServer.insertProduct(product);
		productQueue.add(product);
		updateProductQueueSubscribers();
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
	@Override
	public void subscribeToProductsQueue(ISubscriber subscriber) throws RemoteException {
		this.productQueueSubscribers.add(subscriber);
		this.updateProductQueueSubscribers();
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
	private void updateProductQueueSubscribers() {
		String message = new String();
		for (Product product : productQueue) {
			message+=product.toString()+"\n";
		}
		for (ISubscriber subscriber : productQueueSubscribers) {
			try {
				subscriber.updateProductsList(message);
			} catch (Exception e) {
				productQueueSubscribers.remove(subscriber);
			}
		}
}
	//takes selected car part types from car part queue and place them on pallets. fills 1 pallet until max weight or until empty of car part
	@Override
	public void generatePallets(String carPartType) throws RemoteException {
		double maxWeight = 500;
		double weight=0;
		Pallet pallet = new Pallet(carPartType, maxWeight);

		for (CarPart carPart : this.carPartQueue) {
			if(weight<maxWeight&&carPart.getType().equals(carPartType)) {
				if ((weight+carPart.getWeight())<=maxWeight) {
					pallet.addParts(carPart);
					this.carPartQueue.remove(carPart);
					this.dataServer.deleteFromCarPartQueue();
				}
			}
	
		}
		if(!pallet.getParts().isEmpty()) {
			pallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(pallet);
			this.dataServer.insertPallet(pallet);
		}
		updateCarPartQueueSubscribers();
		updatePalletQueueSubscribers();
	}
	@Override
	public int getCarpartTypeQuantity(Integer value, String carPartType) {
		int quantity = 0;
		for (Pallet pallet : palletQueue) {
			for (CarPart carPart : pallet.getParts()) {
				if (carPart.getType().equals(carPartType)) {
					quantity++;
				}
			}
		}
		return quantity;
	}
	@Override
	public int getNextProductRegistrationNumber() {
		return productRegistrationNumberCount++;
	}
	@Override
	public HashMap<CarPart, Pallet> getNextCarPartFromPallet(String carPartType) throws RemoteException {
		HashMap<CarPart, Pallet> result = dataServer.getNextCarPartFromPallet(carPartType);
		for (Entry<CarPart, common.Pallet> pair : result.entrySet())
		{
			Pallet pallet = pair.getValue();
			CarPart carPart = pair.getKey();
			
		 	if (pallet.getParts().size()==0) {
		 		for (Pallet queuedPallet : palletQueue) {
					if(queuedPallet.getRegistrationNumber()==pallet.getRegistrationNumber()) {
						palletQueue.remove(queuedPallet);
						break;
					}
				}
			}
		 	for (CarPart queuedCarPart : carPartQueue) {
				if(queuedCarPart.getRegistrationNumber().equals(carPart.getRegistrationNumber())) {
					carPartQueue.remove(queuedCarPart);
				}
			}
		
		}
		updateCarPartQueueSubscribers();
		updatePalletQueueSubscribers();
		return result;
	}

}




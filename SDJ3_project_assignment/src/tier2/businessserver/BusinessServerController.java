package tier2.businessserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;
import common.Subject;
import tier3.IDataServer;

public class BusinessServerController extends UnicastRemoteObject implements IBusinessServer {
	private static final long serialVersionUID = 1L;
	private BusinessServerView view;
	private IDataServer dataServer;
	private static final String registryName = "BusinessServer";
	private LinkedBlockingQueue<Car> carDismantlingQueue;
	private LinkedBlockingQueue<CarPart> carPartQueue;
	private LinkedBlockingQueue<Product> productQueue;
	private ArrayList<Pallet> palletQueue;
	private int palletRegistrationNumberCount;
	private int productRegistrationNumberCount;
	private HashMap<Subject, HashSet<ISubscriber>> subscriberMap;
	private HashMap<Subject, String> subjectToListMap;

	private List<Car> carList;
	private List<CarPart> carPartList;
	private List<Pallet> palletList;
	private List<Product> productList;

	public BusinessServerController(IDataServer dataServer) throws RemoteException {
		this.dataServer = dataServer;
		this.view = new BusinessServerView();
		this.carDismantlingQueue = new LinkedBlockingQueue<>();
		this.carPartQueue = new LinkedBlockingQueue<>();
		this.productQueue = new LinkedBlockingQueue<>();
		this.palletQueue = new ArrayList<>();
		this.palletRegistrationNumberCount = 0;
		this.productRegistrationNumberCount = 0;
		this.subscriberMap = new HashMap<>();
		this.bindToRegistry();
		this.initiateSystem();
	}

	public static IBusinessServer getRemoteObject() throws MalformedURLException, RemoteException, NotBoundException {
		return (IBusinessServer) Naming.lookup("rmi://localhost/" + registryName);
	}

	@Override
	public synchronized void message(String message) {
		LocalDateTime timePoint = LocalDateTime.now();
		this.view.updateTextArea(timePoint + ": " + message);
	}

	private void bindToRegistry() {
		try {
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error binding " + registryName + " to registry.\nCheck if the Data Server is running and restart " + registryName + "\n" + e.getMessage());
		}
	}

	private void initiateSystem() throws RemoteException {
		try {
			message("Connected to Main");
			message("Registry naming rebind using '" + registryName + "' successful");
			message(registryName + " is running");
			palletRegistrationNumberCount = dataServer.getNextPrimaryKey(Pallet.class);
			productRegistrationNumberCount = dataServer.getNextPrimaryKey(Product.class);
			loadData();
			initiateQueues();
			initiateSubjectToListMap();
		} catch (RemoteException e) {
			System.out.println("Failed connecting to Main" + e.getMessage());
		}
	}

	private void loadData() {
		try {
			carList = dataServer.readAll(Car.class);
			carPartList = dataServer.readAll(CarPart.class);
			palletList = dataServer.readAll(Pallet.class);
			productList = dataServer.readAll(Product.class);
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
	}

	private void initiateQueues() {
		try {
			initiateNondismantledCars();
			initiateAvailableCarParts();
			initiateLoadedPallets();
			initiateProductQueue();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void initiateNondismantledCars() {
		for (Car car : carList) {
			if (!car.isDismantled()) {
				carDismantlingQueue.add(car);
			}
		}
	}

	private void initiateAvailableCarParts() {
		// adds all parts and then removes those stored on a pallet
		for (CarPart carPart : carPartList) {
			carPartQueue.add(carPart);
		}
		for (Pallet pallet : palletList) {
			for (CarPart carPart : pallet.getParts()) {
				carPartQueue.removeIf(p -> p.equals(carPart));
			}
		}
	}

	private void initiateLoadedPallets() {
		for (Pallet pallet : palletList) {
			if (pallet.getParts().size() > 0) {
				palletQueue.add(pallet);
			}
		}
	}

	private void initiateProductQueue() {
		for (Product product : productList) {
			productQueue.add(product);
		}
	}

	private void initiateSubjectToListMap() {
		subjectToListMap = new HashMap<>();
		subjectToListMap.put(Subject.CARS, getCarList());
		subjectToListMap.put(Subject.CARPARTS, getCarPartList());
		subjectToListMap.put(Subject.PALLETS, getPalletList());
		subjectToListMap.put(Subject.PRODUCTS, getProductList());
	}

	@Override
	public synchronized void registerCarForDismantling(Car car) throws RemoteException {
		dataServer.create(car);
		carDismantlingQueue.add(car);
		publish(getCarList(), Subject.CARS);
		message("Enqueued car: " + car.getModel() + "(" + car.getChassisNumber() + ") " + car.getWeight() + "kg");
	}

	@Override
	public synchronized Car getNextCarToBeDismantled() throws RemoteException {
		if (carDismantlingQueue.isEmpty()) {
			throw new RemoteException("dismantling queue is empty");
		}
		Car car = carDismantlingQueue.remove();
		car.setAsDismantled();
		dataServer.update(car);
		publish(getCarList(), Subject.CARS);
		message("Dequeued car: " + car.getModel() + "(" + car.getChassisNumber() + ") " + car.getWeight() + "kg");
		return car;
	}

	@Override
	public synchronized void registerCarPart(CarPart carPart) throws RemoteException {
		dataServer.create(carPart);
		carPartQueue.add(carPart);
		publish(getCarPartList(), Subject.CARPARTS);
		message("Registered carpart: " + carPart);
	}

	@Override
	public synchronized void packageProduct(Product product) throws RemoteException {
		dataServer.create(product);
		productQueue.add(product);
		publish(getProductList(), Subject.PRODUCTS);
		message("Product packaged: " + product);
	}

	@Override
	public synchronized void subscribe(ISubscriber subscriber, Subject... subjects) throws RemoteException {
		for (Subject subject : subjects) {
			if (!subscriberMap.containsKey(subject)) {
				subscriberMap.put(subject, new HashSet<>());
			}
			subscriberMap.get(subject).add(subscriber);
			publish(subjectToListMap.get(subject), subject);
		}
	}

	private synchronized void publish(String subjectList, Subject subject) throws RemoteException {
		for (ISubscriber subscriber : subscriberMap.get(subject)) {
			subscriber.updateSubscriber(subjectList, subject);
		}

	}

	private String getCarPartList() {
		String subjectList = new String();
		int wheelCount = 0, doorCount = 0, seatCount = 0, engineCount = 0, steeringCount = 0;
		for (CarPart carPart : this.carPartQueue) {
			switch (carPart.getType()) {
				case "Wheel":
					wheelCount++;
					break;
				case "Door":
					doorCount++;
					break;
				case "Seat":
					seatCount++;
					break;
				case "Engine":
					engineCount++;
					break;
				case "Steeringwheel":
					steeringCount++;
					break;
				default:
					break;
			}
		}
		subjectList = "Wheel: " + wheelCount + "pcs\nDoors: " + doorCount + "pcs\nSeat: " + seatCount + "pcs\nEngine: " + engineCount + "pcs\nStWheel: " + steeringCount + "pcs";
		return subjectList;
	}

	private String getPalletList() {
		String subjectList = new String();
		for (Pallet pallet : palletQueue) {
			subjectList += String.format("%s, part type: %s, count: %d\n",
					pallet.getRegistrationNumber(),
					pallet.getTypeOfPart(),
					pallet.getParts().size());
		}
		return subjectList;
	}

	private String getCarList() {
		String subjectList = new String();
		for (Car car : this.carDismantlingQueue) {
			subjectList += String.format("%s, %s\n", car.getChassisNumber(), car.getModel());
		}
		return subjectList;
	}

	private String getProductList() {
		String subjectList = new String();
		for (Product product : productQueue) {
			subjectList += String.format("%s, type: %s\n", product.getRegistrationNumber(), product.getProductType());
		}
		return subjectList;
	}

	//takes selected car part types from car part queue and place them on pallets. fills 1 pallet until max weight or until empty of car part
	@Override
	public synchronized void generatePallets(String carPartType) throws RemoteException {
		Pallet pallet = new Pallet(carPartType);

		for (CarPart carPart : this.carPartQueue) {
			if (carPart.getType().equals(carPartType)) {
				try {
					pallet.addPart(carPart);
					carPartQueue.remove(carPart);
				} catch (IllegalArgumentException e) {
					break;
				}
			}
		}
		if (!pallet.getParts().isEmpty()) {
			pallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(pallet);
			this.dataServer.create(pallet);
		}
		publish(getCarPartList(), Subject.CARPARTS);
		publish(getPalletList(), Subject.PALLETS);
	}

	@Override
	public synchronized int getCarpartTypeQuantity(Integer value, String carPartType) {
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
	public synchronized CarPart pickCarPart(String carPartType) throws RemoteException {
		for (Pallet pallet : palletQueue) {
			if (pallet.getTypeOfPart().equals(carPartType)) {
					CarPart carPart = pallet.getNextCarPart();
					if (pallet.getParts().size() == 0) {
						palletQueue.remove(pallet);
					}
					dataServer.update(pallet);
					publish(getPalletList(), Subject.PALLETS);
					return carPart;
			}
		}
		throw new IllegalStateException("Not enough parts of type: " + carPartType);
	}
}




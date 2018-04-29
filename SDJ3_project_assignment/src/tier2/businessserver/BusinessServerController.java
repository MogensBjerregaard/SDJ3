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
import java.util.concurrent.LinkedBlockingQueue;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;
import common.Subject;
import tier3.IDataServer;

public class BusinessServerController extends UnicastRemoteObject implements IBusinessServer {
//	private static final long serialVersionUID = 1L;
//	private BusinessServerView view;
//	private IDataServerOLD dataServer;
//	private static final String registryName = "BusinessServer";
//	private LinkedBlockingQueue<Car> dismantlingQueue;
//	private LinkedBlockingQueue<CarPart> carPartQueue;
//	private LinkedBlockingQueue<Product> productQueue;
//	private ArrayList<Pallet> palletQueue;
//	private int palletRegistrationNumberCount;
//	private int productRegistrationNumberCount;
//	private HashMap<Subject, HashSet<ISubscriber>> subscriberMap;
//
//	public BusinessServerController(IDataServerOLD dataServer) throws RemoteException{
//		this.dataServer = dataServer;
//		this.view = new BusinessServerView();
//		this.dismantlingQueue = new LinkedBlockingQueue<>();
//		this.carPartQueue = new LinkedBlockingQueue<>();
//		this.productQueue = new LinkedBlockingQueue<>();
//		this.palletQueue = new ArrayList<>();
//		this.palletRegistrationNumberCount = 0;
//		this.productRegistrationNumberCount = 0;
//		this.subscriberMap = new HashMap<>();
//		this.bindToRegistry();
//		this.initiateSystem();
//	}
//	public static IBusinessServer getRemoteObject() throws MalformedURLException, RemoteException, NotBoundException {
//		return (IBusinessServer) Naming.lookup("rmi://localhost/"+registryName);
//	}
//	@Override
//	public void updateView(String message) {
//		LocalDateTime timePoint = LocalDateTime.now();
//		this.view.update(timePoint+": "+message);
//	}
//	private void bindToRegistry(){
//		try {
//			Naming.rebind(registryName, this);
//		} catch (RemoteException | MalformedURLException e) {
//			System.out.println("Error binding "+registryName+" to registry.\nCheck if the Data Server is running and restart "+registryName+"\n"+e.getMessage());
//		}
//	}
//	private void initiateSystem() throws RemoteException {
//		try {
//			this.updateView("Connected to Main");
//			this.dataServer.updateView(registryName+" connected");
//			this.updateView("Registry naming rebind using '"+registryName+"' successful");
//			this.updateView(registryName+" is running");
//			this.palletRegistrationNumberCount = this.dataServer.getNextPalletRegistrationNumber();
//			this.productRegistrationNumberCount = this.dataServer.getNextProductRegistrationNumber();
//			dataServer.initiateQueues(); //load data from DB to businessServer on startup
//		} catch (RemoteException e) {
//			System.out.println("Failed connecting to Main"+e.getMessage());
//		}
//	}
//	@Override
//	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException {
//		Car car = new Car(chassisNumber, weight, model);
//		this.dismantlingQueue.add(car);
//		this.dataServer.insertCar(car);
//		publish(getCarList(), Subject.CARS);
//		this.updateView("Enqueued car: "+model+"("+chassisNumber+") "+weight+"kg");
//	}
//	@Override
//	public Car dequeueCar() throws RemoteException {
//		if (this.dismantlingQueue.isEmpty()) {
//			throw new RemoteException();
//		}
//		Car car = this.dismantlingQueue.remove();
////		this.dataServer.deleteCarFromQueue(car);
//		publish(getCarList(), Subject.CARS);
//		this.updateView("Dequeued car: "+car.getModel()+"("+car.getChassisNumber()+") "+car.getWeight()+"kg");
//		return car;
//	}
//	@Override
//	public void registerCarPart(CarPart carPart) throws RemoteException {
//		this.dataServer.insertCarPart(carPart);
//		this.carPartQueue.add(carPart);
//		publish(getCarPartList(), Subject.CARPARTS);
//		this.updateView("Registered carpart: "+carPart.toString());
//	}
//	@Override
//	public void packageProduct(Product product) throws RemoteException {
//		dataServer.insertProduct(product);
//		productQueue.add(product);
//		publish(getProductList(), Subject.PRODUCTS);
//	}
//	@Override
//	public void subscribe(ISubscriber subscriber, Subject... subjects) {
//		for (Subject subject : subjects) {
//			if (!subscriberMap.containsKey(subject)) {
//				subscriberMap.put(subject, new HashSet<ISubscriber>());
//			}
//			subscriberMap.get(subject).add(subscriber);
//		}
//	}
//
//	public void publish(String subjectList, Subject subject) throws RemoteException {
//		for (ISubscriber subscriber : subscriberMap.get(subject)) {
//			subscriber.updateSubscriber(subjectList, subject);
//		}
//
//	}
//	private String getCarPartList() {
//		String subjectList = new String();
//		int wheelCount=0, doorCount=0, seatCount=0, engineCount=0, steeringCount=0;
//		for (CarPart carPart : this.carPartQueue) {
//			switch (carPart.getType()) {
//			case "Wheel": wheelCount++;
//			break;
//			case "Door": doorCount++;
//			break;
//			case "Seat": seatCount++;
//			break;
//			case "Engine": engineCount++;
//			break;
//			case "Steeringwheel": steeringCount++;
//			break;
//			default:
//				break;
//			}
//		}
//		subjectList = "Wheel: "+wheelCount+"pcs\nDoors: "+doorCount+"pcs\nSeat: "+seatCount+"pcs\nEngine: "+engineCount+"pcs\nStWheel: "+steeringCount+"pcs";
//		return subjectList;
//	}
//	private String getPalletList() {
//		String subjectList = new String();
//		for (Pallet pallet : palletQueue) {
//			subjectList+=pallet.toString()+"\n";
//		}
//		return subjectList;
//	}
//	private String getCarList() {
//		String subjectList = new String();
//		for (Car car : this.dismantlingQueue) {
//			subjectList+=car.getModel()+"("+car.getChassisNumber()+")\n";
//		}
//		return subjectList;
//	}
//	private String getProductList() {
//		String subjectList = new String();
//		for (Product product : productQueue) {
//			subjectList+=product.toString()+"\n";
//		}
//		return subjectList;
//	}
//
//	//takes selected car part types from car part queue and place them on pallets. fills 1 pallet until max weight or until empty of car part
//	@Override
//	public void generatePallets(String carPartType) throws RemoteException {
//		double maxWeight = 500;
//		double weight=0;
//		Pallet pallet = new Pallet(carPartType, maxWeight);
//
//		for (CarPart carPart : this.carPartQueue) {
//			if(weight<maxWeight&&carPart.getType().equals(carPartType)) {
//				if ((weight+carPart.getWeight())<=maxWeight) {
//					pallet.addParts(carPart);
//					this.carPartQueue.remove(carPart);
//					this.dataServer.deleteFromCarPartQueue();
//				}
//			}
//
//		}
//		if(!pallet.getParts().isEmpty()) {
//			pallet.setRegistrationNumber(palletRegistrationNumberCount++);
//			this.palletQueue.add(pallet);
//			this.dataServer.insertPallet(pallet);
//		}
//		publish(getCarPartList(), Subject.CARPARTS);
//		publish(getPalletList(), Subject.PALLETS);
//	}
//	@Override
//	public int getCarpartTypeQuantity(Integer value, String carPartType) {
//		int quantity = 0;
//		for (Pallet pallet : palletQueue) {
//			for (CarPart carPart : pallet.getParts()) {
//				if (carPart.getType().equals(carPartType)) {
//					quantity++;
//				}
//			}
//		}
//		return quantity;
//	}
//	@Override
//	public int getNextProductRegistrationNumber() {
//		return productRegistrationNumberCount++;
//	}
//	@Override
//	public HashMap<CarPart, Pallet> getNextCarPartFromPallet(String carPartType) throws RemoteException {
//		HashMap<CarPart, Pallet> result = dataServer.getNextCarPartFromPallet(carPartType);
//		for (Entry<CarPart, common.Pallet> pair : result.entrySet())
//		{
//			Pallet pallet = pair.getValue();
//			CarPart carPart = pair.getKey();
//
//		 	if (pallet.getParts().size()==0) {
//		 		for (Pallet queuedPallet : palletQueue) {
//					if(queuedPallet.getRegistrationNumber()==pallet.getRegistrationNumber()) {
//						palletQueue.remove(queuedPallet);
//						break;
//					}
//				}
//			}
//		 	for (CarPart queuedCarPart : carPartQueue) {
//				if(queuedCarPart.getRegistrationNumber().equals(carPart.getRegistrationNumber())) {
//					carPartQueue.remove(queuedCarPart);
//				}
//			}
//
//		}
//		publish(getCarPartList(), Subject.CARPARTS);
//		publish(getPalletList(), Subject.PALLETS);
//
//		return result;
//	}

	private static final long serialVersionUID = 1L;
	private BusinessServerView view;
	private IDataServer dataServer;
	private static final String registryName = "BusinessServer";
	private LinkedBlockingQueue<Car> dismantlingQueue;
	private LinkedBlockingQueue<CarPart> carPartQueue;
	private LinkedBlockingQueue<Product> productQueue;
	private ArrayList<Pallet> palletQueue;
	private int palletRegistrationNumberCount;
	private int productRegistrationNumberCount;
	private HashMap<Subject, HashSet<ISubscriber>> subscriberMap;

	public BusinessServerController(IDataServer dataServer) throws RemoteException{
		this.dataServer = dataServer;
		this.view = new BusinessServerView();
		this.dismantlingQueue = new LinkedBlockingQueue<>();
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
			updateView("Connected to Main");
//			dataServer.updateView(registryName+" connected");
			updateView("Registry naming rebind using '"+registryName+"' successful");
			updateView(registryName+" is running");
			palletRegistrationNumberCount = dataServer.getNextPrimaryKey(Pallet.class);
			productRegistrationNumberCount = dataServer.getNextPrimaryKey(Product.class);
			dataServer.initiateQueues(); //load data from DB to businessServer on startup
		} catch (RemoteException e) {
			System.out.println("Failed connecting to Main"+e.getMessage());
		}
	}
	@Override
	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException {
		Car car = new Car(chassisNumber, weight, model);
		dismantlingQueue.add(car);
		dataServer.create(car);
		publish(getCarList(), Subject.CARS);
		updateView("Enqueued car: "+model+"("+chassisNumber+") "+weight+"kg");
	}
	@Override
	public Car dequeueCar() throws RemoteException {
		if (dismantlingQueue.isEmpty()) {
			throw new RemoteException();
		}
		Car car = dismantlingQueue.remove();
		car.setAsDismantled();
		dataServer.update(car);
//		this.dataServer.deleteCarFromQueue(car);
		publish(getCarList(), Subject.CARS);
		updateView("Dequeued car: "+car.getModel()+"("+car.getChassisNumber()+") "+car.getWeight()+"kg");
		return car;
	}
	@Override
	public void registerCarPart(CarPart carPart) throws RemoteException {
		dataServer.create(carPart);
		carPartQueue.add(carPart);
		publish(getCarPartList(), Subject.CARPARTS);
		updateView("Registered carpart: "+carPart.toString());
	}
	@Override
	public void packageProduct(Product product) throws RemoteException {
		dataServer.create(product);
		productQueue.add(product);
		publish(getProductList(), Subject.PRODUCTS);
	}
	@Override
	public void subscribe(ISubscriber subscriber, Subject... subjects) {
		for (Subject subject : subjects) {
			if (!subscriberMap.containsKey(subject)) {
				subscriberMap.put(subject, new HashSet<ISubscriber>());
			}
			subscriberMap.get(subject).add(subscriber);
		}
	}

	public void publish(String subjectList, Subject subject) throws RemoteException {
		for (ISubscriber subscriber : subscriberMap.get(subject)) {
			subscriber.updateSubscriber(subjectList, subject);
		}

	}
	private String getCarPartList() {
		String subjectList = new String();
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
		subjectList = "Wheel: "+wheelCount+"pcs\nDoors: "+doorCount+"pcs\nSeat: "+seatCount+"pcs\nEngine: "+engineCount+"pcs\nStWheel: "+steeringCount+"pcs";
		return subjectList;
	}
	private String getPalletList() {
		String subjectList = new String();
		for (Pallet pallet : palletQueue) {
			subjectList+=pallet.toString()+"\n";
		}
		return subjectList;
	}
	private String getCarList() {
		String subjectList = new String();
		for (Car car : this.dismantlingQueue) {
			subjectList+=car.getModel()+"("+car.getChassisNumber()+")\n";
		}
		return subjectList;
	}
	private String getProductList() {
		String subjectList = new String();
		for (Product product : productQueue) {
			subjectList+=product.toString()+"\n";
		}
		return subjectList;
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
					carPartQueue.remove(carPart);
//					this.dataServer.deleteFromCarPartQueue();
				}
			}

		}
		if(!pallet.getParts().isEmpty()) {
			pallet.setRegistrationNumber(palletRegistrationNumberCount++);
			this.palletQueue.add(pallet);
			this.dataServer.create(pallet);
		}
		publish(getCarPartList(), Subject.CARPARTS);
		publish(getPalletList(), Subject.PALLETS);
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
//		HashMap<CarPart, Pallet> result = dataServer.getNextCarPartFromPallet(carPartType);
//		for (Entry<CarPart, common.Pallet> pair : result.entrySet())
//		{
//			Pallet pallet = pair.getValue();
//			CarPart carPart = pair.getKey();
//
//			if (pallet.getParts().size()==0) {
//				for (Pallet queuedPallet : palletQueue) {
//					if(queuedPallet.getRegistrationNumber()==pallet.getRegistrationNumber()) {
//						palletQueue.remove(queuedPallet);
//						break;
//					}
//				}
//			}
//			for (CarPart queuedCarPart : carPartQueue) {
//				if(queuedCarPart.getRegistrationNumber().equals(carPart.getRegistrationNumber())) {
//					carPartQueue.remove(queuedCarPart);
//				}
//			}
//
//		}
//		publish(getCarPartList(), Subject.CARPARTS);
//		publish(getPalletList(), Subject.PALLETS);

//		return result;
		return null;
	}


}




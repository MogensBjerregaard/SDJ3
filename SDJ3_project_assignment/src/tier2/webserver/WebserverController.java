package tier2.webserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import common.CarPart;
import common.xsd.Car;
import common.xsd.Product;
import tier3.IDataServer;

public class WebserverController {
	@SuppressWarnings("unused")
	private WebserverView view;
	private IDataServer dataServer;
	private static final String registryName = "Webserver";
	private static final String dataServerName = "DataServer";
	private WebserverController() {
		try {
			dataServer = (IDataServer) Naming.lookup("rmi://localhost/"+dataServerName);
//			dataServer.message(registryName+" connected");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("Error connecting Main to WebServiceSkeleton\n"+ e.getMessage());
		}
		this.view = WebserverView.getInstance();
		WebserverController.updateView("Connected to "+dataServerName); 
		WebserverController.updateView(registryName+" is running");
	}
	private static class Wrapper{
		static WebserverController instance = new WebserverController();
	}
	public static WebserverController getInstance() {
		return Wrapper.instance;
	}
	private static void updateView(String message) {
		LocalDateTime timePoint = LocalDateTime.now();
		WebserverView.updateTextArea(timePoint+": "+message);
	}
	public Product[] getMatchingProducts(String chassisNumber) {
		updateView("Tracking system requesting products for chassisnumber "+chassisNumber);
		ArrayList<common.Product> productsMatching;
		try {
			productsMatching = dataServer.getMatchingProducts(chassisNumber);
		} catch (RemoteException e) {
			System.out.println("WebserverController: Error getting matching products for chassisnumber from Data Server");
			return new common.xsd.Product[0];
		}
		return convertProductObjectArray(productsMatching);		
	}
	
	//converting common objects to common.xsd (Axis2 Web Service) objects - by mapping fields
	private common.xsd.Product[] convertProductObjectArray(ArrayList<common.Product> productArrayList) {
		if(productArrayList != null) {
			common.xsd.Product[] productsConverted = new common.xsd.Product[productArrayList.size()];
			int index = 0;
			for (common.Product product : productArrayList) {
				common.xsd.Product newProduct = new Product();
				newProduct.setRegistrationNumber(product.getRegistrationNumber());
				newProduct.setProductType(product.getProductType());
				HashSet<common.Pallet> pallets = product.getPalletReferences();
				for (common.Pallet pallet : pallets) {
					common.xsd.Pallet newPallet = new common.xsd.Pallet();
					newPallet.setRegistrationNumber(pallet.getRegistrationNumber());
					newProduct.addPalletReferences(newPallet);
				}
				ArrayList<CarPart> parts = product.getParts();
				for (CarPart carPart : parts) {
					common.xsd.CarPart newCarPart = new common.xsd.CarPart();
					newCarPart.setRegistrationNumber(carPart.getRegistrationNumber());
					common.xsd.Car newCar = new Car();
					newCar.setChassisNumber(carPart.getCar().getChassisNumber());
					newCar.setModel(carPart.getCar().getModel());
					newCar.setWeight(carPart.getCar().getWeight());
					newCarPart.setCar(newCar);
					newProduct.addParts(newCarPart);
				}
				productsConverted[index++] = newProduct;
			}
			return productsConverted;
		} else {
			return new common.xsd.Product[0];
 
		}
	}
}

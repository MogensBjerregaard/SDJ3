package tier1.productpackagingclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import common.*;
import tier2.businessserver.IBusinessServer;

public class ProductPackagingClientController extends UnicastRemoteObject implements ISubscriber {
	private static final long serialVersionUID = 1L;
	private ProductPackagingClientView view;
	private static final String registryName = "Main";
	private IBusinessServer businessServer;
	
	public ProductPackagingClientController(IBusinessServer businessServer) throws RemoteException, MalformedURLException, NotBoundException {
		this.businessServer = businessServer;
		this.view = new ProductPackagingClientView(this);
		this.view.setVisible(true);
		this.bindToRegistry();
		this.businessServer.subscribe(this, Subject.PALLETS, Subject.PRODUCTS);
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
	public void updateSubscriber(String message, Subject subject) throws RemoteException {
		if (subject.equals(Subject.PALLETS)) view.updatePalletsList(message);
		if (subject.equals(Subject.PRODUCTS)) view.updateProductsList(message);
	}

	public int checkCarpartTypeQuantity(Integer value, String carPartType) {
		try {
			return businessServer.getCarPartTypeQuantity(value, carPartType);
		} catch (RemoteException e) {
			view.notifyUserError("Error getting quantity from Business server.\n"+e.getMessage());
			return 0;
		}
	}
	public void generateNewProduct(HashMap<String, Integer> carPartTypeAndQuantity) throws RemoteException {
		// pallets from which the parts came
		HashSet<common.Pallet> palletReferences = new HashSet<>();

		// parts chosen for product
		ArrayList<CarPart> carParts = new ArrayList<>();

		HashSet<String> carPartTypesContained = new HashSet<>();

		// fetches necessary parts
		for (Entry<String, Integer> entry : carPartTypeAndQuantity.entrySet()) {
		    String carPartType = entry.getKey();
		    Integer quantity = entry.getValue();

		    if (quantity > 0) {

		    	carPartTypesContained.add(carPartType);

		    	for (int i = 0; i < quantity; i++) {

		    		// GET NEXT CAR PART FROM WHATEVER PALLET
					CarPart carPart = businessServer.pickCarPart(carPartType);
					carParts.add(carPart);
					palletReferences.add(carPart.getPalletReference());

//		    		HashMap<CarPart, common.Pallet> carPartAndPalletReference= businessServer.getNextCarPartFromPallet(carPartType);
//		    		for (Entry<CarPart, common.Pallet> pair : carPartAndPalletReference.entrySet())
//		    		{
//		    		  palletReferences.add(pair.getValue());
//		    		  carParts.add(pair.getKey());
//		    		}
		    	}
		    }
		}


		//Creating name from combined carpart type names
		String productType = "";
		for (String type : carPartTypesContained) {
			productType+=type+"-";
		}
		productType += "set";
		
		Product newProduct = new Product(businessServer.getNextProductRegistrationNumber(), productType);
		newProduct.setPalletReferences(palletReferences);
		newProduct.setParts(carParts);
		try {
			businessServer.packageProduct(newProduct);
		} catch (RemoteException e) {
			view.notifyUserError("Error creating new product.\n"+e.getMessage());
		}
	}

}

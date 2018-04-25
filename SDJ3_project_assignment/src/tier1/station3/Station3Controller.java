package tier1.station3;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import common.CarPart;
import common.ISubscriber;
import common.Product;
import common.Subject;
import tier2.businessserver.BusinessServerController;
import tier2.businessserver.IBusinessServer;

public class Station3Controller extends UnicastRemoteObject implements ISubscriber {
	private static final long serialVersionUID = 1L;
	private Station3View view;
	private static final String registryName = "Station3";
	private IBusinessServer businessServer;
	
	public Station3Controller()throws RemoteException, MalformedURLException, NotBoundException {
		this.businessServer = BusinessServerController.getRemoteObject();
		this.view = new Station3View(this);
		this.view.setVisible(true);
		this.bindToRegistry();
		this.businessServer.subscribe(this, Subject.PALLETS, Subject.PRODUCTS);
		this.businessServer.updateView(registryName+" connected");
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
		if (subject.equals(Subject.PALLETS)) view.updatePalletsList(subjectList);
		if (subject.equals(Subject.PRODUCTS)) view.updateProductsList(subjectList);
	}

	public int checkCarpartTypeQuantity(Integer value, String carPartType) {
		try {
			return businessServer.getCarpartTypeQuantity(value, carPartType);
		} catch (RemoteException e) {
			view.notifyUserError("Error getting quantity from Business server.\n"+e.getMessage());
			return 0;
		}
	}
	public void generateNewProduct(HashMap<String, Integer> carPartTypeAndQuantity) throws RemoteException {
		HashSet<common.Pallet> palletReferences = new HashSet<>();
		ArrayList<CarPart> carParts = new ArrayList<>();
		HashSet<String> carPartTypesContained = new HashSet<>();
		for (Entry<String, Integer> entry : carPartTypeAndQuantity.entrySet()) {
		    String carPartType = entry.getKey();
		    Integer quantity = entry.getValue();
		    if(quantity>0) {
		    	carPartTypesContained.add(carPartType);
		    	for (int i=0; i<quantity; i++) {
		    		HashMap<CarPart, common.Pallet> carPartAndPalletReference= businessServer.getNextCarPartFromPallet(carPartType);
		    		for (Entry<CarPart, common.Pallet> pair : carPartAndPalletReference.entrySet())
		    		{
		    		  palletReferences.add(pair.getValue());
		    		  carParts.add(pair.getKey());
		    		}
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

package tier1.station1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import common.Car;
import common.ISubscriber;
import common.Subject;
import tier2.businessserver.BusinessServerController;
import tier2.businessserver.IBusinessServer;

public class Station1Controller extends UnicastRemoteObject implements ISubscriber{

	private static final long serialVersionUID = 1L;
	private Station1View view;
	private static final String registryName = "Station1";
	private IBusinessServer businessServer;
	private Properties properties;
	
	public Station1Controller(IBusinessServer businessServer) throws RemoteException, MalformedURLException, NotBoundException{
		this.businessServer = businessServer;
		this.view = new Station1View(this);
		this.view.setVisible(true);
		loadProperties();
		bindToRegistry();
		businessServer.subscribe(this, Subject.CARS);
		businessServer.updateView(registryName+" connected");
	}
	public void inputCar(String chassisNumber, double weight, String model) {
		try {
			Car car = new Car(chassisNumber, weight, model);
			businessServer.enqueueCar(car);
			view.notifyUserSucces(properties.getProperty("Succes_message"));
		} catch (RemoteException e) {
			view.notifyUserError("Enqueueing error\n"+e.getLocalizedMessage());
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
	public void updateSubscriber(String message, Subject subject) throws RemoteException {
		if (subject.equals(Subject.CARS)) {
			view.updateEnqueuedCarsList(message);
		}
		
	}

	private void loadProperties(){
		try (InputStream in = new FileInputStream("C:\\ScriptsSemester4\\SDJ3\\Project\\SDJ3_project_assignment\\station1.properties")){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			System.out.println("Station 1 properties file not found!"+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading Station 1 properties file"+ e.getMessage());
		}
	}
}



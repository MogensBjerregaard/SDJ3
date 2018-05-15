package tier1.station3;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Station3View extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Properties properties;
	private JPanel contentPane;
	private JTextArea textAreaPallets;
	private JSpinner spinnerWheel;
	private JSpinner spinnerSeat;
	private JSpinner spinnerDoor;
	private JSpinner spinnerEngine;
	private JSpinner spinnerSteering;
	private JButton btnPackProduct;
	private JTextArea textAreaProducts;
	private Station3Controller controller;

	public Station3View(Station3Controller controller) {
		loadProperties();
		this.controller = controller;
		this.initComponents();
		this.createEvents();
	}

	private void initComponents() {
		this.setResizable(false);
		this.setTitle("Station 3 Tier 1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(1050, 50, 540, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		JLabel lblEnqueueCarFor = new JLabel(properties.getProperty("Headline"));
		lblEnqueueCarFor.setBounds(40, 11, 347, 31);
		lblEnqueueCarFor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		this.contentPane.add(lblEnqueueCarFor);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 75, 120, 163);
		this.contentPane.add(scrollPane);

		this.textAreaPallets = new JTextArea();
		this.textAreaPallets.setLocation(40, 0);
		scrollPane.setViewportView(this.textAreaPallets);

		JLabel lblPallets = new JLabel("Stored pallets");
		lblPallets.setBounds(40, 50, 85, 22);
		this.contentPane.add(lblPallets);

		this.spinnerWheel = new JSpinner();
		this.spinnerWheel.setBounds(179, 78, 40, 20);
		this.contentPane.add(this.spinnerWheel);

		JLabel lblWheel = new JLabel("Wheel");
		lblWheel.setBounds(229, 81, 55, 17);
		this.contentPane.add(lblWheel);

		this.spinnerSeat = new JSpinner();
		this.spinnerSeat.setBounds(179, 104, 40, 20);
		this.contentPane.add(this.spinnerSeat);

		this.spinnerDoor = new JSpinner();
		this.spinnerDoor.setBounds(179, 129, 40, 20);
		this.contentPane.add(this.spinnerDoor);

		JLabel lblSeat = new JLabel("Seat");
		lblSeat.setBounds(229, 104, 40, 20);
		this.contentPane.add(lblSeat);

		JLabel lblDoor = new JLabel("Door");
		lblDoor.setBounds(229, 129, 48, 17);
		this.contentPane.add(lblDoor);

		this.spinnerEngine = new JSpinner();
		this.spinnerEngine.setBounds(179, 153, 40, 20);
		this.contentPane.add(this.spinnerEngine);

		JLabel lblEngine = new JLabel("Engine");
		lblEngine.setBounds(229, 156, 48, 17);
		this.contentPane.add(lblEngine);

		this.spinnerSteering = new JSpinner();
		this.spinnerSteering.setBounds(179, 176, 40, 20);
		this.contentPane.add(this.spinnerSteering);

		JLabel lblSteeringwheel = new JLabel("Steeringwheel");
		lblSteeringwheel.setBounds(229, 179, 99, 17);
		this.contentPane.add(lblSteeringwheel);

		this.btnPackProduct = new JButton("Pack product");
		this.btnPackProduct.setBounds(180, 207, 126, 31);
		this.contentPane.add(this.btnPackProduct);

		JLabel lblPickCarparts = new JLabel("Pick carparts");
		lblPickCarparts.setBounds(179, 53, 105, 20);
		this.contentPane.add(lblPickCarparts);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(338, 75, 154, 163);
		this.contentPane.add(scrollPane_1);

		this.textAreaProducts = new JTextArea();
		scrollPane_1.setViewportView(this.textAreaProducts);

		JLabel lblProducts = new JLabel("Products");
		lblProducts.setBounds(338, 54, 77, 18);
		this.contentPane.add(lblProducts);


	}
	private void createEvents() {
		this.spinnerWheel.addChangeListener(arg0 -> {
			checkSpinnerValue(this.spinnerWheel, "Wheel");
		});
		this.spinnerSeat.addChangeListener(e -> {
			checkSpinnerValue(this.spinnerSeat, "Seat");
		});
		this.spinnerDoor.addChangeListener(e -> {
			checkSpinnerValue(this.spinnerDoor, "Door");
		});
		this.spinnerEngine.addChangeListener(e -> {
			checkSpinnerValue(this.spinnerEngine, "Engine");
		});
		this.spinnerSteering.addChangeListener(e -> {
			checkSpinnerValue(this.spinnerSteering, "Steeringwheel");
		});
		btnPackProduct.addActionListener(e -> {
			if (anyValuesSelected()) {
				createNewProduct();
			} else {
				notifyUserError("No carpart type quantities was selected");
			}
		});
	}

	//getting the selected quantity for every carPart type and creating a new product of it
	private void createNewProduct() {
		HashMap<String, Integer> carPartTypeAndQuantity = new HashMap<>();
		carPartTypeAndQuantity.put("Wheel", (Integer)spinnerWheel.getValue());
		carPartTypeAndQuantity.put("Door", (Integer)spinnerDoor.getValue());
		carPartTypeAndQuantity.put("Seat", (Integer)spinnerSeat.getValue());
		carPartTypeAndQuantity.put("Engine", (Integer)spinnerEngine.getValue());
		carPartTypeAndQuantity.put("Steeringwheel", (Integer)spinnerSteering.getValue());
		try {
			controller.generateNewProduct(carPartTypeAndQuantity);
			notifyUserSucces("A new product was created");
			resetValues();
		} catch (RemoteException e) {
			notifyUserError("Error creating new product.\n"+e.getMessage());
		}
	}

	private boolean anyValuesSelected() {
		if ((Integer)spinnerDoor.getValue()>0) return true;
		if ((Integer)spinnerEngine.getValue()>0) return true;
		if ((Integer)spinnerSeat.getValue()>0) return true;
		if ((Integer)spinnerSteering.getValue()>0) return true;
		if ((Integer)spinnerWheel.getValue()>0) return true;
		return false;
	}
	private void resetValues() {
		spinnerDoor.setValue(0);
		spinnerEngine.setValue(0);
		spinnerSeat.setValue(0);
		spinnerSteering.setValue(0);
		spinnerWheel.setValue(0);
	}
	public void notifyUserError(String message) {
		JOptionPane.showMessageDialog(null,
				message,
				properties.getProperty("Error"), JOptionPane.ERROR_MESSAGE);
	}
	private void notifyUserSucces(String message) {
		JOptionPane.showMessageDialog(null,
				message,
				properties.getProperty("Succes_headline"), JOptionPane.PLAIN_MESSAGE);
	}

	private void checkSpinnerValue(JSpinner jSpinner, String carPartType) {
		if((Integer)jSpinner.getValue() < 0) {
			jSpinner.setValue(0);
		}
		if((Integer)jSpinner.getValue() > 10) {
			jSpinner.setValue(10);
		}
		int carPartQuantityInStock = controller.checkCarpartTypeQuantity((Integer)jSpinner.getValue(), carPartType);
		if((Integer)jSpinner.getValue() > carPartQuantityInStock) {
			jSpinner.setValue(carPartQuantityInStock);
			if (carPartQuantityInStock == 0) {
				notifyUserError("There is no "+ carPartType+ " in stock.");
			}else {
				notifyUserError("There is only "+carPartQuantityInStock+" "+ carPartType+ " in stock.");
			}
		}
	}
	public void updatePalletsList(String message) {
		this.textAreaPallets.setText(message);
	}

	private static void loadProperties(){
		try (InputStream in = new FileInputStream("C:\\ScriptsSemester4\\SDJ3\\Project\\SDJ3_project_assignment\\station3.properties")){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			System.out.println("Station 3 properties file not found!"+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading Station 3 properties file"+ e.getMessage());
		}

	}

	public void updateProductsList(String message) {
		textAreaProducts.setText(message);
	}
}

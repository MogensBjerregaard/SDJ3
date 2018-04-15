package tier1.station2;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import common.Car;

public class Station2View extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Properties properties;
	private JPanel contentPane;

	private JButton btnDequeueCar;
	private JTextArea textAreaEnqueued;
	private JLabel lblDismantling;
	private JLabel lblCarToBeDismantled;
	private JLabel lblWheel;
	private JTextField textFieldWheel;
	private JTextField textFieldDoor;
	private JTextField textFieldSeat;
	private JTextField textFieldEngine;
	private JLabel lblDoor;
	private JLabel lblSeat;
	private JLabel lblEngine;
	private JLabel lblSteering;
	private JTextField textFieldSteering;
	private JButton btnWeigh;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaCarParts;
	private JScrollPane scrollPane_2;
	private JLabel lblCarpartsWeighed;
	private JLabel lblStoredPallets;
	private JTextArea textAreaPallets;
	private Station2Controller controller;
	private double weightWheel;
	private double weightDoor;
	private double weightSeat;
	private double weightEngine;
	private double weightSteering;
	private Car loadedCar;
	private JButton btnGeneratePallets;
	private JCheckBox chckbxWheels;
	private JCheckBox chckbxDoors;
	private JCheckBox chckbxSeats;
	private JCheckBox chckbxEngines;
	private JCheckBox chckbxSteeringwheels;

	public Station2View(Station2Controller controller) {
		loadProperties();
		this.controller = controller;
		this.initComponents();
		this.createEvents();
	}
	private void initComponents() {
		this.setResizable(false);
		this.setTitle("Station 2 Tier 1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(650, 350, 640, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		JLabel lblEnqueueCarFor = new JLabel(properties.getProperty("Headline"));
		lblEnqueueCarFor.setBounds(40, 11, 347, 31);
		lblEnqueueCarFor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		this.contentPane.add(lblEnqueueCarFor);


		this.btnDequeueCar = new JButton("Dequeue car");
		this.btnDequeueCar.setBounds(40, 193, 109, 31);
		this.contentPane.add(this.btnDequeueCar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 79, 109, 97);
		this.contentPane.add(scrollPane);

		this.textAreaEnqueued = new JTextArea();
		scrollPane.setViewportView(this.textAreaEnqueued);

		JLabel lblQueueTitle = new JLabel("Enqueued cars");
		lblQueueTitle.setBounds(40, 50, 109, 20);
		this.contentPane.add(lblQueueTitle);

		this.lblDismantling = new JLabel("Car:");
		this.lblDismantling.setBounds(167, 50, 31, 20);
		this.contentPane.add(this.lblDismantling);

		this.lblCarToBeDismantled = new JLabel("");
		this.lblCarToBeDismantled.setBounds(195, 52, 143, 17);
		this.contentPane.add(this.lblCarToBeDismantled);

		this.lblWheel = new JLabel("Wheel");
		this.lblWheel.setBounds(167, 79, 56, 14);
		this.contentPane.add(this.lblWheel);

		this.textFieldWheel = new JTextField();
		this.textFieldWheel.setBounds(239, 76, 86, 20);
		this.contentPane.add(this.textFieldWheel);
		this.textFieldWheel.setColumns(10);

		this.textFieldDoor = new JTextField();
		this.textFieldDoor.setBounds(239, 105, 86, 20);
		this.contentPane.add(this.textFieldDoor);
		this.textFieldDoor.setColumns(10);

		this.textFieldSeat = new JTextField();
		this.textFieldSeat.setBounds(239, 134, 86, 20);
		this.contentPane.add(this.textFieldSeat);
		this.textFieldSeat.setColumns(10);

		this.textFieldEngine = new JTextField();
		this.textFieldEngine.setBounds(239, 163, 86, 20);
		this.contentPane.add(this.textFieldEngine);
		this.textFieldEngine.setColumns(10);

		this.lblDoor = new JLabel("Door");
		this.lblDoor.setBounds(167, 103, 64, 17);
		this.contentPane.add(this.lblDoor);

		this.lblSeat = new JLabel("Seat");
		this.lblSeat.setBounds(167, 130, 57, 20);
		this.contentPane.add(this.lblSeat);

		this.lblEngine = new JLabel("Engine");
		this.lblEngine.setBounds(167, 160, 58, 20);
		this.contentPane.add(this.lblEngine);

		this.lblSteering = new JLabel("Steering");
		this.lblSteering.setBounds(167, 190, 56, 20);
		this.contentPane.add(this.lblSteering);

		this.textFieldSteering = new JTextField();
		this.textFieldSteering.setBounds(239, 192, 86, 20);
		this.contentPane.add(this.textFieldSteering);
		this.textFieldSteering.setColumns(10);

		this.btnWeigh = new JButton("Register");
		this.btnWeigh.setBounds(239, 221, 86, 31);
		this.contentPane.add(this.btnWeigh);
		this.btnWeigh.setEnabled(false);

		this.scrollPane_1 = new JScrollPane();
		this.scrollPane_1.setBounds(348, 79, 109, 97);
		this.contentPane.add(this.scrollPane_1);

		this.textAreaCarParts = new JTextArea();
		this.scrollPane_1.setViewportView(this.textAreaCarParts);

		this.scrollPane_2 = new JScrollPane();
		this.scrollPane_2.setBounds(485, 79, 109, 97);
		this.contentPane.add(this.scrollPane_2);

		this.textAreaPallets = new JTextArea();
		this.scrollPane_2.setViewportView(this.textAreaPallets);

		this.lblCarpartsWeighed = new JLabel("Waiting for pallet");
		this.lblCarpartsWeighed.setBounds(348, 50, 119, 20);
		this.contentPane.add(this.lblCarpartsWeighed);

		this.lblStoredPallets = new JLabel("Stored pallets");
		this.lblStoredPallets.setBounds(485, 50, 100, 20);
		this.contentPane.add(this.lblStoredPallets);

		this.chckbxWheels = new JCheckBox("Wheels");
		this.chckbxWheels.setBounds(347, 175, 70, 23);
		this.contentPane.add(this.chckbxWheels);

		this.chckbxDoors = new JCheckBox("Doors");
		this.chckbxDoors.setBounds(347, 197, 64, 23);
		this.contentPane.add(this.chckbxDoors);

		this.chckbxSeats = new JCheckBox("Seats");
		this.chckbxSeats.setBounds(348, 225, 63, 23);
		this.contentPane.add(this.chckbxSeats);

		this.chckbxEngines = new JCheckBox("Engines");
		this.chckbxEngines.setBounds(417, 175, 97, 23);
		this.contentPane.add(this.chckbxEngines);

		this.chckbxSteeringwheels = new JCheckBox("Steeringwheels");
		this.chckbxSteeringwheels.setBounds(417, 197, 119, 23);
		this.contentPane.add(this.chckbxSteeringwheels);

		this.btnGeneratePallets = new JButton("Generate pallets");
		this.btnGeneratePallets.setBounds(417, 223, 177, 27);
		this.contentPane.add(this.btnGeneratePallets);
	}
	private void createEvents() {
		this.btnDequeueCar.addActionListener(arg0 -> {
			this.controller.dequeueCar();
		});
		this.btnWeigh.addActionListener(arg0 -> {
			if (this.inputIsValid()) {
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"WH1", "Wheel", this.weightWheel);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"WH2", "Wheel", this.weightWheel);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"WH3", "Wheel", this.weightWheel);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"WH4", "Wheel", this.weightWheel);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"DO1", "Door", this.weightDoor);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"DO2", "Door", this.weightDoor);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"DO3", "Door", this.weightDoor);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"DO4", "Door", this.weightDoor);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"SE1", "Seat", this.weightSeat);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"SE2", "Seat", this.weightSeat);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"SE3", "Seat", this.weightSeat);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"SE4", "Seat", this.weightSeat);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"EN", "Engine", this.weightEngine);
				this.controller.inputCarPart(this.loadedCar, this.loadedCar.getChassisNumber()+"ST", "Steeringwheel", this.weightSteering);
				this.notifyUserSucces("All carparts was registered succesful.");
				this.textFieldWheel.setText("");
				this.textFieldDoor.setText("");
				this.textFieldSeat.setText("");
				this.textFieldEngine.setText("");
				this.textFieldSteering.setText("");
				this.lblCarToBeDismantled.setText("");
			}
		});
		this.btnGeneratePallets.addActionListener(arg0 -> {
			if(!this.chckbxWheels.isSelected()&&!this.chckbxDoors.isSelected()&&!this.chckbxSeats.isSelected()&&!this.chckbxEngines.isSelected()&&!this.chckbxSteeringwheels.isSelected()) {
				this.notifyUserError("Please tick off one or more car parts palleting");
			} else {
				this.controller.generatePallets(this.chckbxWheels.isSelected(),this.chckbxDoors.isSelected(),this.chckbxSeats.isSelected(),this.chckbxEngines.isSelected(),this.chckbxSteeringwheels.isSelected());
				this.chckbxWheels.setSelected(false);
				this.chckbxDoors.setSelected(false);
				this.chckbxSeats.setSelected(false);
				this.chckbxEngines.setSelected(false);
				this.chckbxSteeringwheels.setSelected(false);
			}
		});

	}
	private boolean inputIsValid() {
		if(this.textFieldWheel.getText().equals("")) {
			this.notifyUserError("Input wheel weight");
			return false;
		} else {
			try {
				this.weightWheel = Double.parseDouble(this.textFieldWheel.getText());
			} catch (Exception e) {
				this.notifyUserError("Error parsing wheel weight");
				this.textFieldWheel.setText("");
				return false;
			}
		}
		if(this.textFieldDoor.getText().equals("")) {
			this.notifyUserError("Input door weight");
			return false;
		} else {
			try {
				this.weightDoor = Double.parseDouble(this.textFieldDoor.getText());
			} catch (Exception e) {
				this.notifyUserError("Error parsing door weight");
				this.textFieldWheel.setText("");
				return false;
			}
		}
		if(this.textFieldSeat.getText().equals("")) {
			this.notifyUserError("Input seat weight");
			return false;
		} else {
			try {
				this.weightSeat = Double.parseDouble(this.textFieldSeat.getText());
			} catch (Exception e) {
				this.notifyUserError("Error parsing seat weight");
				this.textFieldSeat.setText("");
				return false;
			}
		}
		if(this.textFieldEngine.getText().equals("")) {
			this.notifyUserError("Input engine weight");
			return false;
		} else {
			try {
				this.weightEngine = Double.parseDouble(this.textFieldEngine.getText());
			} catch (Exception e) {
				this.notifyUserError("Error parsing engine weight");
				this.textFieldEngine.setText("");
				return false;
			}
		}
		if(this.textFieldSteering.getText().equals("")) {
			this.notifyUserError("Input steeringwheel weight");
			return false;
		} else {
			try {
				this.weightSteering = Double.parseDouble(this.textFieldSteering.getText());
			} catch (Exception e) {
				this.notifyUserError("Error parsing steeringwheel weight");
				this.textFieldSteering.setText("");
				return false;
			}
		}
		return true;
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
	public void updateEnqueuedCarsList(String message) {
		this.textAreaEnqueued.setText(message);
	}
	public void updateCarPartsList(String message) {
		this.textAreaCarParts.setText(message);
		this.btnDequeueCar.setEnabled(true);
		this.btnWeigh.setEnabled(false);
	}
	public void updatePalletsList(String message) {
		this.textAreaPallets.setText(message);
	}
	public void loadDequeuedCar(Car dequeuedCar) {
		this.lblCarToBeDismantled.setText(dequeuedCar.getModel()+", "+dequeuedCar.getChassisNumber());
		this.loadedCar = dequeuedCar;
		this.btnDequeueCar.setEnabled(false);
		this.btnWeigh.setEnabled(true);
	}
	private static void loadProperties(){
		try (InputStream in = new FileInputStream("SDJ3_project_assignment\\station2.properties")){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			System.out.println("Station 2 properties file not found!"+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading Station 2 properties file"+ e.getMessage());
		}

	}
}

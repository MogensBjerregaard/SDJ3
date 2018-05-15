package tier1.carregistrationclient;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CarRegistrationClientView extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Properties properties;
	private JPanel contentPane;
	private JTextField textFieldWeight;
	private JLabel lblEnterCarWeight;
	private JLabel lblEnterChassisNumber;
	private JTextField textFieldChassisNumber;
	private JLabel lblSelectCarType;
	private JComboBox<String> comboBoxCarModels;
	private JButton btnEnqueueCar;
	private CarRegistrationClientController controller;
	private double weight;
	private String chassisNumber;
	private String model;
	private JTextArea textArea;

	public CarRegistrationClientView(CarRegistrationClientController controller) {
		loadProperties();
		this.controller = controller;
		this.initComponents();
		this.createEvents();
	}
	private void initComponents() {
		this.setResizable(false);
		this.setTitle("Car Registration Client - Tier 1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(50, 50, 360, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		contentPane.setLayout(null);

		JLabel lblEnqueueCarFor = new JLabel(properties.getProperty("Headline"));
		lblEnqueueCarFor.setBounds(40, 11, 347, 31);
		lblEnqueueCarFor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		this.contentPane.add(lblEnqueueCarFor);

		this.textFieldWeight = new JTextField();
		textFieldWeight.setBounds(40, 82, 100, 20);
		this.contentPane.add(this.textFieldWeight);
		this.textFieldWeight.setColumns(10);

		this.lblEnterCarWeight = new JLabel(properties.getProperty("Weight_title"));
		lblEnterCarWeight.setBounds(40, 58, 136, 20);
		this.contentPane.add(this.lblEnterCarWeight);

		this.lblEnterChassisNumber = new JLabel(properties.getProperty("Chassis_title"));
		lblEnterChassisNumber.setBounds(40, 113, 142, 20);
		this.contentPane.add(this.lblEnterChassisNumber);

		this.textFieldChassisNumber = new JTextField();
		textFieldChassisNumber.setBounds(40, 135, 100, 20);
		this.contentPane.add(this.textFieldChassisNumber);
		this.textFieldChassisNumber.setColumns(10);

		this.lblSelectCarType = new JLabel(properties.getProperty("Carmodel_title"));
		lblSelectCarType.setBounds(40, 166, 121, 20);
		this.contentPane.add(this.lblSelectCarType);

		String[] carTypes = properties.getProperty("CarModels").split(";");
		this.comboBoxCarModels = new JComboBox<>(carTypes);
		comboBoxCarModels.setBounds(40, 192, 100, 20);
		this.contentPane.add(this.comboBoxCarModels);


		this.btnEnqueueCar = new JButton(properties.getProperty("Button_text"));
		btnEnqueueCar.setBounds(214, 196, 109, 31);
		this.contentPane.add(this.btnEnqueueCar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(214, 82, 109, 97);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel = new JLabel(properties.getProperty("Queue_title"));
		lblNewLabel.setBounds(214, 57, 109, 20);
		contentPane.add(lblNewLabel);
	}
	private void createEvents() {
		this.btnEnqueueCar.addActionListener(arg0 -> {
			if (this.inputIsValid()) {
				this.controller.inputCar(this.chassisNumber, this.weight, this.model);
				this.textFieldWeight.setText("");
				this.textFieldChassisNumber.setText("");
				this.comboBoxCarModels.setSelectedIndex(0);
			}
		});

	}
	private boolean inputIsValid() {
		if(this.textFieldWeight.getText().equals("")) {
			this.notifyUserError(properties.getProperty("Empty_weight"));
			return false;
		} else {
			try {
				this.weight = Double.parseDouble(this.textFieldWeight.getText());
			} catch (Exception e) {
				this.notifyUserError(properties.getProperty("Error_parsing_weight"));
				this.textFieldWeight.setText("");
				return false;
			}
		}
		if(this.textFieldChassisNumber.getText().equals("")) {
			this.notifyUserError(properties.getProperty("Empty_chassisnumber"));
			return false;
		} else {
			this.chassisNumber = this.textFieldChassisNumber.getText();
		}
		if(this.comboBoxCarModels.getSelectedItem().toString().equals("")) {
			this.notifyUserError(properties.getProperty("Empty_carmodel"));
			return false;
		} else {
			this.model = this.comboBoxCarModels.getSelectedItem().toString();
		}
		return true;
	}

	public void notifyUserError(String message) {
		JOptionPane.showMessageDialog(null,
				message,
				properties.getProperty("Error"), JOptionPane.ERROR_MESSAGE);
	}

	public void notifyUserSucces(String message) {
		JOptionPane.showMessageDialog(null,
				message,
				 properties.getProperty("Succes_headline"), JOptionPane.PLAIN_MESSAGE);
	}

	public void updateEnqueuedCarsList(String message) {
		textArea.setText(message);
	}

	private static void loadProperties(){
		try (InputStream in = new FileInputStream("C:\\ScriptsSemester4\\SDJ3\\Project\\SDJ3_project_assignment\\carregistrationclient.properties")){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			System.out.println("Station 1 properties file not found!"+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading Station 1 properties file"+ e.getMessage());
		}
	}
}

package tier1.station3;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.JButton;


public class Station3View extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Properties properties;
	private JPanel contentPane;
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
		contentPane.add(scrollPane);
		
		JTextArea textAreaPallets = new JTextArea();
		textAreaPallets.setLocation(40, 0);
		scrollPane.setViewportView(textAreaPallets);
		
		JLabel lblPallets = new JLabel("Stored pallets");
		lblPallets.setBounds(40, 50, 85, 22);
		contentPane.add(lblPallets);
		
		JSpinner spinnerWheel = new JSpinner();
		spinnerWheel.setBounds(179, 78, 40, 20);
		contentPane.add(spinnerWheel);
		
		JLabel lblWheel = new JLabel("Wheel");
		lblWheel.setBounds(229, 81, 55, 17);
		contentPane.add(lblWheel);
		
		JSpinner spinnerSeat = new JSpinner();
		spinnerSeat.setBounds(179, 104, 40, 20);
		contentPane.add(spinnerSeat);
		
		JSpinner spinnerDoor = new JSpinner();
		spinnerDoor.setBounds(179, 129, 40, 20);
		contentPane.add(spinnerDoor);
		
		JLabel lblSeat = new JLabel("Seat");
		lblSeat.setBounds(229, 104, 40, 20);
		contentPane.add(lblSeat);
		
		JLabel lblDoor = new JLabel("Door");
		lblDoor.setBounds(229, 129, 48, 17);
		contentPane.add(lblDoor);
		
		JSpinner spinnerEngine = new JSpinner();
		spinnerEngine.setBounds(179, 153, 40, 20);
		contentPane.add(spinnerEngine);
		
		JLabel lblEngine = new JLabel("Engine");
		lblEngine.setBounds(229, 156, 48, 17);
		contentPane.add(lblEngine);
		
		JSpinner spinnerSteering = new JSpinner();
		spinnerSteering.setBounds(179, 176, 40, 20);
		contentPane.add(spinnerSteering);
		
		JLabel lblSteeringwheel = new JLabel("Steeringwheel");
		lblSteeringwheel.setBounds(229, 179, 99, 17);
		contentPane.add(lblSteeringwheel);
		
		JButton btnPackProduct = new JButton("Pack product");
		btnPackProduct.setBounds(180, 207, 126, 31);
		contentPane.add(btnPackProduct);
		
		JLabel lblPickCarparts = new JLabel("Pick carparts");
		lblPickCarparts.setBounds(179, 53, 105, 20);
		contentPane.add(lblPickCarparts);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(338, 75, 154, 163);
		contentPane.add(scrollPane_1);
		
		JTextArea textAreaProducts = new JTextArea();
		scrollPane_1.setViewportView(textAreaProducts);
		
		JLabel lblProducts = new JLabel("Products");
		lblProducts.setBounds(338, 54, 77, 18);
		contentPane.add(lblProducts);


	}
	private void createEvents() {


	}
	
	private static void loadProperties(){
		try (InputStream in = new FileInputStream("./station3.properties")){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			System.out.println("Station 3 properties file not found!"+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading Station 3 properties file"+ e.getMessage());
		}

	}
}

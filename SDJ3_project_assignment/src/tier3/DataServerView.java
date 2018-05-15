package tier3;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class DataServerView extends JFrame implements IDataServerView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	
	public DataServerView() {
		setResizable(false);
		initiateComponents();
	}
	private void initiateComponents() {
		this.setVisible(true);
		setTitle("Data Server - Tier 3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 650, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 10, 570, 250);
		contentPane.add(textArea);		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		scrollPane.setBounds(10, 10, 570, 250);
		contentPane.add(scrollPane);
	}
	public void update(String message) {
		textArea.setText(textArea.getText()+message+"\n");
	}
}
package tier2.businessserver;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class BusinessServerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	
	public BusinessServerView() {
		setResizable(false);
		initiateComponents();
	}
	private void initiateComponents() {
		this.setVisible(true);
		setTitle("Business Server - Tier 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 350, 600, 300);
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
	public void updateTextArea(String message) {
		textArea.setText(textArea.getText()+message+"\n");
	}
}

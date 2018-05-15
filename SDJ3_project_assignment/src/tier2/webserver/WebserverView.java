package tier2.webserver;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class WebserverView extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextArea textArea;
	
	private WebserverView() {
		setResizable(false);
		initiateComponents();
	}
	private static class Wrapper{
		static WebserverView instance = new WebserverView();
	}
	public static WebserverView getInstance() {
		return Wrapper.instance;
	}
	private void initiateComponents() {
		this.setVisible(true);
		setTitle("Webserver - Tier 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(650, 350, 600, 300);
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
	public static void updateTextArea(String message) {
		textArea.setText(textArea.getText()+message+"\n");
	}
}

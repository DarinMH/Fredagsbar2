 package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.image.*;
import java.awt.BorderLayout;
import java.awt.Color;

public class LogInd extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldLogInd;
	private JLabel lblId;
	private JButton btnBekræft;
	private final JLabel lblLogInd = new JLabel("Log ind:");
	private JLabel lblNewLabel;
	private JLabel lblInfo;
	private KasseSystem kasseSystem;
	private LogInd login; 
	

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInd frame = new LogInd();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public LogInd() {
		
		
		init(); 
		
		
}
	
	
	
	
	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 582, 390);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 71, 0, 0, 0, 0, 0, 123, 0, 77, 0};
		gbl_contentPane.rowHeights = new int[]{50, 0, 0, 0, 0, 0, 39, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
	
		JLabel lblBillede1 = new JLabel("");
		ImageIcon originalIcon = new ImageIcon(LogInd.class.getResource("/GUI/Images/Screenshot 2025-05-06 152104.png"));
		Image image = originalIcon.getImage();
		Image scaledImage = image.getScaledInstance(400, 150, Image.SCALE_SMOOTH);
		lblBillede1.setIcon(new ImageIcon(scaledImage));

		GridBagConstraints gbc_lblBillede1 = new GridBagConstraints();
		gbc_lblBillede1.gridx = 2;
		gbc_lblBillede1.gridy = 1;
		gbc_lblBillede1.gridwidth = 7;
		gbc_lblBillede1.gridheight = 5;
		gbc_lblBillede1.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblBillede1, gbc_lblBillede1);
		
		lblInfo = new JLabel("");
		lblInfo.setForeground(Color.RED);
		lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblInfo = new GridBagConstraints();
		gbc_lblInfo.gridwidth = 7;
		gbc_lblInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInfo.gridx = 2;
		gbc_lblInfo.gridy = 6;
		contentPane.add(lblInfo, gbc_lblInfo);
		
		
		GridBagConstraints gbc_lblLogInd = new GridBagConstraints();
		gbc_lblLogInd.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogInd.gridx = 3;
		gbc_lblLogInd.gridy = 7;
		lblLogInd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblLogInd, gbc_lblLogInd);		
		
		textFieldLogInd = new JTextField();
		textFieldLogInd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_textFieldLogInd = new GridBagConstraints();
		gbc_textFieldLogInd.gridwidth = 4;
		gbc_textFieldLogInd.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldLogInd.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLogInd.gridx = 5;
		gbc_textFieldLogInd.gridy = 7;
		contentPane.add(textFieldLogInd, gbc_textFieldLogInd);
		textFieldLogInd.setColumns(10);
		
		btnBekræft = new JButton("Bekræft");
		btnBekræft.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_btnBekræft = new GridBagConstraints();
		gbc_btnBekræft.anchor = GridBagConstraints.EAST;
		gbc_btnBekræft.insets = new Insets(0, 0, 5, 5);
		gbc_btnBekræft.fill = GridBagConstraints.VERTICAL;
		gbc_btnBekræft.gridx = 8;
		gbc_btnBekræft.gridy = 8;
		contentPane.add(btnBekræft, gbc_btnBekræft);
		getRootPane().setDefaultButton(btnBekræft);

		btnBekræft.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String enteredID = textFieldLogInd.getText();
		        if (enteredID.equals("1234")) {
		            KasseSystem kasseSystem = new KasseSystem(null, login);
		            setVisible(false);
		            kasseSystem.setVisible(true);
		        } else if (enteredID.equals("4321")) {
		            KasseSystemBes kasseSystemBes = new KasseSystemBes(null, login); 
		            setVisible(false);
		            kasseSystemBes.setVisible(true);
		        } else {
		            lblInfo.setText("Ugyldig ID!");
		        }
		    }
		});
	}
	}

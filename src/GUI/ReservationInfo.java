package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Reservation;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;

public class ReservationInfo extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldResId;
	private JTextField textFieldProduct;
	private JTextField textFieldCustomer;
	private JTextField textFieldDate;
	private Reservation reservation; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ReservationInfo dialog = new ReservationInfo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ReservationInfo(Reservation reservation) {
		
		this.reservation=reservation; 
		
		
		init(); 
		
	}
	
	
	private void init() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel_3 = new JLabel("Reservation ID: ");
			GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
			gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_3.gridx = 4;
			gbc_lblNewLabel_3.gridy = 1;
			contentPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		}
		{
			textFieldResId = new JTextField(reservation.getReservationId());
			textFieldResId.setEditable(false);
			GridBagConstraints gbc_textFieldResId = new GridBagConstraints();
			gbc_textFieldResId.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldResId.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldResId.gridx = 5;
			gbc_textFieldResId.gridy = 1;
			contentPanel.add(textFieldResId, gbc_textFieldResId);
			textFieldResId.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Produkt: ");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_2.gridx = 4;
			gbc_lblNewLabel_2.gridy = 2;
			contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		}
		{
			textFieldProduct = new JTextField(reservation.getBorrowableProduct().getProductName());
			textFieldProduct.setEditable(false);
			GridBagConstraints gbc_textFieldProduct = new GridBagConstraints();
			gbc_textFieldProduct.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldProduct.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldProduct.gridx = 5;
			gbc_textFieldProduct.gridy = 2;
			contentPanel.add(textFieldProduct, gbc_textFieldProduct);
			textFieldProduct.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Kunde: ");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_1.gridx = 4;
			gbc_lblNewLabel_1.gridy = 3;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			textFieldCustomer = new JTextField(String.valueOf(reservation.getCustomer()));
			textFieldCustomer.setEditable(false);
			GridBagConstraints gbc_textFieldCustomer = new GridBagConstraints();
			gbc_textFieldCustomer.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldCustomer.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldCustomer.gridx = 5;
			gbc_textFieldCustomer.gridy = 3;
			contentPanel.add(textFieldCustomer, gbc_textFieldCustomer);
			textFieldCustomer.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("Dato:");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.gridx = 4;
			gbc_lblNewLabel.gridy = 4;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textFieldDate = new JTextField();
			textFieldDate.setEditable(false);
			GridBagConstraints gbc_textFieldDate = new GridBagConstraints();
			gbc_textFieldDate.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldDate.gridx = 5;
			gbc_textFieldDate.gridy = 4;
			contentPanel.add(textFieldDate, gbc_textFieldDate);
			textFieldDate.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	
	}
}

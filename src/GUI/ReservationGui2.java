
package GUI;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DataAccessException;
import controller.ReservationCtr;
import controller.SaleOrderCtr;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;

import java.awt.GridBagLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class ReservationGui2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<BorrowableProduct> productsModelList; 
	private JList<Reservation> reservationList; 
	private DefaultListModel<Reservation> reservationModelList; 
	private ReservationCtr reservationCtr; 
	private JButton reservationBtn;
	private JTextField textFieldCustomerSearch;
	private JButton searchCustomer;
	private JTextField customerTextField;
	private JLabel lblNewLabel;
	private JTextField reservationTextField;
	private JButton productBtn;
	private JLabel lblNewLabel_1;
	private JTextField productTF;
	private JLabel lblNewLabel_2;
	private JButton btnNewButton;
	private JScrollPane scrollPane_1;
	private JButton btnNewButton_1;
	private JScrollPane scrollPane_2;
	private JList<BorrowableProduct> list;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel_3;
	private JButton btnNewButton_2;
	private Reservation res; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservationGui2 frame = new ReservationGui2(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws DataAccessException 
	 */
	public ReservationGui2(Reservation res) throws DataAccessException {
		
		
		this.reservationCtr= new ReservationCtr(); 

		
		productsModelList = new DefaultListModel<>(); 
		reservationModelList = new DefaultListModel<>(); 
	
		
		init();
		
	
		
		list.setModel(productsModelList); 
		list.setCellRenderer(new BorrowableProductsListCellRenderer()); 
		
		reservationList.setModel(reservationModelList); 
		reservationList.setCellRenderer(new ReservationListCellRenderer()); 
		
		
		reservationCtr = new ReservationCtr();
		if(res == null) {
			this.res = reservationCtr.createReservation(generateRandomNumber(), LocalDate.now(), 0, null, false, null); 
		    reservationTextField.setText(String.valueOf(this.res.getReservationId()));

		} else {
			this.res = res; 
		}
		

		
		fillProductList(); 
		fillReservationList(); 
		
		
	}
	
	private void fillProductList() throws DataAccessException {
		
		List<BorrowableProduct> products = reservationCtr.findAllProducts(); 
		
		for(BorrowableProduct product : products) {
			if(product != null && product.getAmount() > 0)  
			 {
			productsModelList.addElement(product);
		}
		}
	}
	
	
	private void fillReservationList() throws DataAccessException {
		
		List<Reservation> reservations = reservationCtr.findAll(); 
		
		
		for(Reservation reservation : reservations) {
			if(reservation != null && reservation.getCustomer() != null && reservation.getBorrowableProduct() != null && reservation.isStatus() == true)  
			 {
			reservationModelList.addElement(reservation);
		}
		}
	}
	
	
	private boolean confirmReservation() throws DataAccessException {
		
		try {
		
			if(res.getCustomer() != null && res.getBorrowableProduct() != null) {
		reservationCtr.confirmReservation(); 
		return true; 
			}
			
	
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
	        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		
		}
		return false; 
		
		
	}
	
	
	private void createReservation() throws DataAccessException {


			try {
				
				textFieldCustomerSearch.setText("");
				customerTextField.setText("");
				productTF.setText("");

				
				reservationTextField.setText(String.valueOf(generateRandomNumber())); 
			int reservationNumber =	Integer.parseInt(reservationTextField.getText()); 

			reservationCtr.createReservation(reservationNumber, LocalDate.now(), 0, null, false, null);
			
			
		    reservationModelList.clear();
		    fillReservationList();
			   productsModelList.clear(); 
			    fillProductList(); 
			    
			    
				JOptionPane.showMessageDialog(ReservationGui2.this, "Reservation er oprettet ", "Succes", JOptionPane.INFORMATION_MESSAGE); 

		
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
		        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	
			
			}
		
	

	}
	
	
	
	
	private int generateRandomNumber() {
		return 100000 + new java.util.Random().nextInt(900000); 
	}
	
	private Customer addCustomer() throws DataAccessException {
		
		
		
	
		int studentId =	Integer.parseInt(textFieldCustomerSearch.getText());  
		
			Customer customer = reservationCtr.addCustomerToReservation(studentId);  
			
			if(customer != null) {
			
			customerTextField.setText(String.valueOf(customer)); 
			
		   
		    int orderNumber = Integer.parseInt(reservationTextField.getText()); 
		    
		    Reservation reservation = reservationCtr.findByReservationId(orderNumber); 
		    
			JOptionPane.showMessageDialog(this, customer + " er hermed tilføjet til reservation!", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			return customer; 

		} else {
			JOptionPane.showMessageDialog(null, "Kunde ikke registreret i systemet", "Error", JOptionPane.ERROR_MESSAGE);
		}
			
			
	
			return null; 
			
		
		
		}
	
	
	
	
	private BorrowableProduct addProduct() throws DataAccessException {

	
		try {
	BorrowableProduct product = list.getSelectedValue(); 
	
	if(product != null) {
		

	
	int productId = product.getProductId(); 
	
	
		
			reservationCtr.addProductToReservation(productId); 
			
			productTF.setText(String.valueOf(product));


 int orderNumber = Integer.parseInt(reservationTextField.getText()); 
		    
		    Reservation reservation = reservationCtr.findByReservationId(orderNumber); 
		    
			JOptionPane.showMessageDialog(this, product + " er hermed tilføjet til reservation!", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			
			return product; 
		    
		    
	} else {
		JOptionPane.showMessageDialog(null, "produkt ikke registreret i systemet", "Error", JOptionPane.ERROR_MESSAGE);
	}
			
	return null; 

		
			
		
			
	 } catch (IllegalArgumentException e) {
	        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        return null;
	 }
		
		}

	
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 961, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 141, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		reservationBtn = new JButton("Opret Ny Reservation");
		reservationBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createReservation();
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		lblNewLabel_3 = new JLabel("Reservations ID");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 0;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		GridBagConstraints gbc_reservationBtn = new GridBagConstraints();
		gbc_reservationBtn.insets = new Insets(0, 0, 5, 5);
		gbc_reservationBtn.gridx = 1;
		gbc_reservationBtn.gridy = 1;
		contentPane.add(reservationBtn, gbc_reservationBtn);
		
		btnNewButton_1 = new JButton("Slut Reservation");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Reservation reservation = reservationList.getSelectedValue(); 
					reservationCtr.endReservation(reservation.getReservationId());
					
					
				    reservationModelList.clear();
				    fillReservationList();
				    productsModelList.clear(); 
				    fillProductList(); 
					
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		reservationTextField = new JTextField();
		reservationTextField.setEditable(false);
		GridBagConstraints gbc_reservationTextField = new GridBagConstraints();
		gbc_reservationTextField.insets = new Insets(0, 0, 5, 5);
		gbc_reservationTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_reservationTextField.gridx = 2;
		gbc_reservationTextField.gridy = 1;
		contentPane.add(reservationTextField, gbc_reservationTextField);
		reservationTextField.setColumns(10);
		
		productBtn = new JButton("Tilføj Produkt Til Reservation");
		productBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addProduct();
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		searchCustomer = new JButton("Tilføj Kunde til Reservation");
		searchCustomer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					addCustomer();
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		GridBagConstraints gbc_searchCustomer = new GridBagConstraints();
		gbc_searchCustomer.insets = new Insets(0, 0, 5, 5);
		gbc_searchCustomer.gridx = 1;
		gbc_searchCustomer.gridy = 3;
		contentPane.add(searchCustomer, gbc_searchCustomer);
		
		textFieldCustomerSearch = new JTextField();
		textFieldCustomerSearch.setText("Indtast Studie ID På Kunde: "); 
		textFieldCustomerSearch.setForeground(new Color(153, 153, 153));
		textFieldCustomerSearch.addFocusListener(new FocusAdapter() {
		@Override 
		public void focusGained(FocusEvent e) {
			if(textFieldCustomerSearch.getText().equals("Indtast Studie ID På Kunde: ")) {
				textFieldCustomerSearch.setText(""); 
				textFieldCustomerSearch.setForeground(Color.BLACK); 
			}
		}
		@Override
		public void focusLost(FocusEvent e) {
			if(textFieldCustomerSearch.getText().equals("")) {
				textFieldCustomerSearch.setText("Indtast Studie ID På Kunde: "); 
				textFieldCustomerSearch.setForeground(new Color(153, 153, 153)); 
			}
		}
			
			
		}); 
		GridBagConstraints gbc_textFieldCustomerSearch = new GridBagConstraints();
		gbc_textFieldCustomerSearch.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCustomerSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCustomerSearch.gridx = 2;
		gbc_textFieldCustomerSearch.gridy = 3;
		contentPane.add(textFieldCustomerSearch, gbc_textFieldCustomerSearch);
		textFieldCustomerSearch.setColumns(10);
		
		
		GridBagConstraints gbc_productBtn = new GridBagConstraints();
		gbc_productBtn.insets = new Insets(0, 0, 5, 5);
		gbc_productBtn.gridx = 1;
		gbc_productBtn.gridy = 4;
		contentPane.add(productBtn, gbc_productBtn);
		
		scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 2;
		gbc_scrollPane_2.gridy = 4;
		contentPane.add(scrollPane_2, gbc_scrollPane_2);
		
		list = new JList<>();
		scrollPane_2.setViewportView(list);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 5;
		gbc_btnNewButton_1.gridy = 4;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 7;
		gbc_scrollPane_1.gridy = 4;
		gbc_scrollPane_1.gridwidth = 1; 
		gbc_scrollPane_1.gridheight = 8; 
		gbc_scrollPane_1.weightx = 2.0; 
		gbc_scrollPane_1.weighty = 1.0; 
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		reservationList = new JList<>();
		scrollPane_1.setViewportView(reservationList);
		
		lblNewLabel_2 = new JLabel("Reservation Oplysninger: ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 7;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		lblNewLabel = new JLabel("Kunde: ");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 8;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		customerTextField = new JTextField(); 
		customerTextField.setEditable(false);
		GridBagConstraints gbc_customerTextField = new GridBagConstraints();
		gbc_customerTextField.insets = new Insets(0, 0, 5, 5);
		gbc_customerTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_customerTextField.gridx = 2;
		gbc_customerTextField.gridy = 8;
		contentPane.add(customerTextField, gbc_customerTextField);
		customerTextField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Produkt: ");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 9;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		productTF = new JTextField();
		productTF.setEditable(false);
		GridBagConstraints gbc_productTF = new GridBagConstraints();
		gbc_productTF.insets = new Insets(0, 0, 5, 5);
		gbc_productTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_productTF.gridx = 2;
		gbc_productTF.gridy = 9;
		contentPane.add(productTF, gbc_productTF);
		productTF.setColumns(10);
		
		btnNewButton = new JButton("Bekræft Reservation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					
					boolean reservation = confirmReservation();
					if(reservation) {
					createReservation(); 				
				
			        textFieldCustomerSearch.setText("Indtast Studie ID På Kunde: "); 
					textFieldCustomerSearch.setForeground(new Color(153, 153, 153)); 
					
					}
	
					
					
					
				
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		btnNewButton_2 = new JButton("Tilbage");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 11;
		contentPane.add(btnNewButton_2, gbc_btnNewButton_2);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 11;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
		list = new JList<>();
		scrollPane_2.setViewportView(list);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 7;
		gbc_scrollPane.gridy = 11;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		pack(); 
	}

}

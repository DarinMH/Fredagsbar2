package GUI;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DataAccessException;
import controller.ReservationCtr;
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
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class ReservationGui2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<BorrowableProduct> productsList; 
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
	private Reservation currentReservation; 
	private JButton productBtn;
	private JLabel lblNewLabel_1;
	private JTextField productTF;
	private JLabel lblNewLabel_2;
	private JButton btnNewButton;
	private JScrollPane scrollPane_1;
	private JButton btnNewButton_1;
	private JLabel lblReservation;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservationGui2 frame = new ReservationGui2();
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
	public ReservationGui2() throws DataAccessException {
		
		
		this.reservationCtr= new ReservationCtr(); 
		
		
    currentReservation = this.reservationCtr.getCurrentReservation();  
		
		productsModelList = new DefaultListModel<>(); 
		reservationModelList = new DefaultListModel<>(); 
	
		
		init();
		
		productsList.setModel(productsModelList); 
		productsList.setCellRenderer(new BorrowableProductsListCellRenderer()); 
		
		reservationList.setModel(reservationModelList); 
		reservationList.setCellRenderer(new ReservationListCellRenderer()); 
		
		
		

		
	
		
	
		

		
//		productsModelList = new DefaultListModel<>(); 
//		productsList = new JList<BorrowableProduct>(); 
//		this.productsList.setCellRenderer(new BorrowableProductsListCellRenderer());
		
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
	
	
	private void createReservation() throws DataAccessException {

		try {
			try {

				
				
				reservationTextField.setText(String.valueOf(generateRandomNumber())); 
			int reservationNumber =	Integer.parseInt(reservationTextField.getText()); 
			
//			Customer customer = addCustomer(); 
			
			 // Create a new sale order, where the the date attribute is being set to the current time. 
			reservationCtr.createReservation(reservationNumber, LocalDate.now(), 0, null, false, null);
			
			
		    reservationModelList.clear();
		    fillReservationList();
			   productsModelList.clear(); 
			    fillProductList(); 

		
				
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(ReservationGui2.this, "Reservation er oprettet ", "Succes", JOptionPane.INFORMATION_MESSAGE); 
			
			
			// Reset and update UI elements for the new order
				

		} catch(IllegalArgumentException e) {
			e.printStackTrace(); 
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Order kunne ikke oprettes", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	
	
	
	private int generateRandomNumber() {
		return 100000 + new java.util.Random().nextInt(900000); 
	}
	
	private Customer addCustomer() throws DataAccessException {
		
		int studentId =	Integer.parseInt(textFieldCustomerSearch.getText());  
		
			Customer customer = reservationCtr.addCustomerToReservation(studentId);  
			
			
			customerTextField.setText(String.valueOf(customer)); 
			
		    reservationModelList.clear();
		    fillReservationList();
		   

			
//			System.out.println(currentReservation.getCustomer() + " er kunden tilføjet til reservation med reservation nummer: " + 
//			currentReservation.getReservationId()); 
			
			return customer; 
		
		}
	
	
	
	
	private BorrowableProduct addProduct() throws DataAccessException {
		
	BorrowableProduct product = productsList.getSelectedValue(); 
	
	int productId = product.getProductId(); 
		
			reservationCtr.addProductToReservation(productId); 
			
			productTF.setText(String.valueOf(product));
			
		    reservationModelList.clear();
		    fillReservationList();
		    productsModelList.clear(); 
		    fillProductList(); 

			
			
			return product; 
		
		}
	
	

	
		
	
	
	private void init() {
		
		
		GridBagConstraints gbc_productBtn = new GridBagConstraints();
		gbc_productBtn.insets = new Insets(0, 0, 5, 5);
		gbc_productBtn.gridx = 1;
		gbc_productBtn.gridy = 4;
		contentPane.add(productBtn, gbc_productBtn);
		
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
		
		
	
		

		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		productsList = new JList<>();
		scrollPane.setViewportView(productsList);
		
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
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 5;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		

		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 5;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		reservationList = new JList<>();
		scrollPane_1.setViewportView(reservationList);
		
		lblNewLabel_2 = new JLabel("Reservation Oplysninger: ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
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
		
		btnNewButton = new JButton("Bekræft Reservation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					reservationCtr.confirmReservation();
					
					
				
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 8;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 961, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 16, 0, 0, 25, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblReservation = new JLabel("Reservation");
		lblReservation.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblReservation = new GridBagConstraints();
		gbc_lblReservation.anchor = GridBagConstraints.WEST;
		gbc_lblReservation.gridwidth = 5;
		gbc_lblReservation.insets = new Insets(0, 0, 5, 5);
		gbc_lblReservation.gridx = 1;
		gbc_lblReservation.gridy = 0;
		contentPane.add(lblReservation, gbc_lblReservation);
		
		reservationBtn = new JButton("Opret Reservation");
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
		GridBagConstraints gbc_reservationBtn = new GridBagConstraints();
		gbc_reservationBtn.insets = new Insets(0, 0, 5, 5);
		gbc_reservationBtn.gridx = 1;
		gbc_reservationBtn.gridy = 2;
		contentPane.add(reservationBtn, gbc_reservationBtn);
		
		reservationTextField = new JTextField();
		reservationTextField.setEditable(false);
		GridBagConstraints gbc_reservationTextField = new GridBagConstraints();
		gbc_reservationTextField.insets = new Insets(0, 0, 5, 5);
		gbc_reservationTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_reservationTextField.gridx = 2;
		gbc_reservationTextField.gridy = 2;
		contentPane.add(reservationTextField, gbc_reservationTextField);
		reservationTextField.setColumns(10);
		
		searchCustomer = new JButton("Søg Efter Kunde");
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
		GridBagConstraints gbc_textFieldCustomerSearch = new GridBagConstraints();
		gbc_textFieldCustomerSearch.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCustomerSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCustomerSearch.gridx = 2;
		gbc_textFieldCustomerSearch.gridy = 3;
		contentPane.add(textFieldCustomerSearch, gbc_textFieldCustomerSearch);
		textFieldCustomerSearch.setColumns(10);
	}

}

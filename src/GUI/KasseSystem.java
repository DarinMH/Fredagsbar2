
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import DB.DBConnection;
import DB.DataAccessException;
import controller.InventoryCtr;
import controller.SaleOrderCtr;
import model.Customer;
import model.Drink;
import model.Inventory;
import model.InventoryProduct;
import model.Miscellaneous;
import model.Product;
import model.SaleOrder;
import model.SaleOrderLine;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class KasseSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	private LogInd logInd;
	private JTextField totalField;
	private JPanel productPanel;
	private JPanel panelMisc; 
//	private double total = 0.0; 

	private JPanel orderItemsPanel;
	private InventoryCtr inventoryCtr; 
	private Map<String, Integer> cartItems = new HashMap<>();
	private Map<String, Double> itemPrices = new HashMap<>();
	private JTextField textFieldOrderNr;
	private SaleOrderCtr saleOrderCtr; 
	private SaleOrder currentOrder;
	private double total;
	private JTextField textFieldCustomer;
	private JTextField textFieldCustomerSearch;
	private LogInd login; 
 

	/*
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KasseSystem kasseSystemBes = new KasseSystem(null, null);
					kasseSystemBes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//constructor For the KasseSystem GUI and initializes the controllers so a new saleorder can be made
	
	public KasseSystem(SaleOrder saleOrder, LogInd login)  {
			
	try { //tries to create a connection to database with an instance of inventoryCtr
		inventoryCtr = new InventoryCtr();
	} catch (DataAccessException e) { //Catches it if a problem occurs with the connection
		// TODO Auto-generated catch block
		e.printStackTrace(); 
	} 
	
		try {
			saleOrderCtr = new SaleOrderCtr();
			if(currentOrder == null) {
				this.currentOrder = saleOrderCtr.createSaleOrder(generateRandomNumber(), LocalDate.now(), false, null, null, 0, 0); 
			} else {
//			this.currentOrder=currentOrder; 
			
			}
			saleOrderCtr.setCurrentOrder(this.currentOrder);
			total=currentOrder.getTotalPrice(); 
			init(); 
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	
		
	
	}

	
	//Initializes The main GUI layout and all the components on the JFrame for the Kassesystem
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Kasse System");

		GridBagLayout gridBagLayout = new GridBagLayout();
		setSize(953, 541);

		gridBagLayout.columnWidths = new int[] {25, 10, 25, 120, 200, 25, 140, 140, 10, 25 };
		gridBagLayout.rowHeights = new int[] { 74, 30, 31, 0, 31, 0, 31, 12, 0, 0, 0, 0, 18, 18, 60, 0, 38, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				1.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		// Logo setup
		ImageIcon originalIcon = new ImageIcon(
				KasseSystem.class.getResource("/GUI/Images/Screenshot 2025-05-08 153004.png"));
		Image image = originalIcon.getImage();
		Image newimg = image.getScaledInstance(100, 80, java.awt.Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(newimg);

		JLabel lblSofies = new JLabel("");
		lblSofies.setIcon(scaledIcon);
		GridBagConstraints gbc_lblSofies = new GridBagConstraints();
		gbc_lblSofies.insets = new Insets(0, 0, 5, 5);
		gbc_lblSofies.gridx = 1;
		gbc_lblSofies.gridy = 0;
		getContentPane().add(lblSofies, gbc_lblSofies);

		JLabel lblNewLabel = new JLabel("Salg");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.gridwidth = 6;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		// Category panel
		JPanel panelCategory = new JPanel();
		JScrollPane scrollCategory = new JScrollPane(panelCategory);
		GridBagConstraints gbc_scrollCategory = new GridBagConstraints();
		gbc_scrollCategory.gridheight = 13;
		gbc_scrollCategory.insets = new Insets(0, 0, 5, 5);
		gbc_scrollCategory.fill = GridBagConstraints.BOTH;
		gbc_scrollCategory.gridx = 3;
		gbc_scrollCategory.gridy = 1;
		getContentPane().add(scrollCategory, gbc_scrollCategory);

		// Product panel
		productPanel = new JPanel();
		JScrollPane productScroll = new JScrollPane(productPanel);
		GridBagConstraints gbc_productPanel = new GridBagConstraints();
		gbc_productPanel.gridheight = 13;
		gbc_productPanel.insets = new Insets(0, 0, 5, 5);
		gbc_productPanel.fill = GridBagConstraints.BOTH;
		gbc_productPanel.gridx = 4;
		gbc_productPanel.gridy = 1;
		getContentPane().add(productScroll, gbc_productPanel);
		
		JButton btnNewButton_2 = new JButton("Miscellaneous");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadMisc(); 
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 3;
		gbc_btnNewButton_2.gridy = 14;
		getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);		
		
		panelMisc = new JPanel();
		JScrollPane scrollPaneMisc = new JScrollPane(panelMisc);
		GridBagConstraints gbc_panelMisc = new GridBagConstraints();
		gbc_panelMisc.insets = new Insets(0, 0, 5, 5);
		gbc_panelMisc.fill = GridBagConstraints.BOTH;
		gbc_panelMisc.gridx = 4;
		gbc_panelMisc.gridy = 14;
		getContentPane().add(scrollPaneMisc, gbc_panelMisc);
		
		
		scrollPaneMisc.setViewportView(panelMisc);

		// Order items panel
		orderItemsPanel = new JPanel();
		orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(orderItemsPanel);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridheight = 12;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 6;
		gbc_scrollPane.gridy = 1;
		getContentPane().add(scrollPane, gbc_scrollPane);

		JButton btnIndstillinger = new JButton("Indstillinger");
		btnIndstillinger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add settings functionality
			}
		});

		// Buttons
		JButton btnAnnuller = new JButton("Annuller");
		btnAnnuller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cartItems.clear();
				itemPrices.clear();
				orderItemsPanel.removeAll();
				orderItemsPanel.revalidate();
				orderItemsPanel.repaint();
				total = 0.0;
				totalField.setText("0 kr");
			}
		});

		GridBagConstraints gbc_btnAnnullere = new GridBagConstraints();
		gbc_btnAnnullere.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAnnullere.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnnullere.gridx = 8;
		gbc_btnAnnullere.gridy = 1;
		getContentPane().add(btnAnnuller, gbc_btnAnnullere);
		GridBagConstraints gbc_btnIndstillinger = new GridBagConstraints();
		gbc_btnIndstillinger.fill = GridBagConstraints.BOTH;
		gbc_btnIndstillinger.insets = new Insets(0, 0, 5, 5);
		gbc_btnIndstillinger.gridx = 1;
		gbc_btnIndstillinger.gridy = 2;
		getContentPane().add(btnIndstillinger, gbc_btnIndstillinger);

		JButton btnLager = new JButton("Lager");
		btnLager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InventoryGui dialog = new InventoryGui(); 
				dialog.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnLager = new GridBagConstraints();
		gbc_btnLager.fill = GridBagConstraints.BOTH;
		gbc_btnLager.insets = new Insets(0, 0, 5, 5);
		gbc_btnLager.gridx = 1;
		gbc_btnLager.gridy = 6;
		getContentPane().add(btnLager, gbc_btnLager);

		JButton mobilePayBtn = new JButton("MobilePay");
		mobilePayBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        processPayment("MobilePay");
		    }
		});
		
		JButton btnNewButton = new JButton("Bekræft Ordre");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					confirmOrder();
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 8;
		gbc_btnNewButton.gridy = 7;
		getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Ordre Nummer: ");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 10;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		
		textFieldOrderNr = new JTextField(String.valueOf(currentOrder.getOrderNumber()));
		textFieldOrderNr.setEditable(false);
		GridBagConstraints gbc_textFieldOrderNr = new GridBagConstraints();
		gbc_textFieldOrderNr.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldOrderNr.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOrderNr.gridx = 1;
		gbc_textFieldOrderNr.gridy = 10;
		getContentPane().add(textFieldOrderNr, gbc_textFieldOrderNr);
		textFieldOrderNr.setColumns(10);
		
		JButton customerBtn = new JButton("Søg efter Kunde");
		customerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					findByStudentId();
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		GridBagConstraints gbc_customerBtn = new GridBagConstraints();
		gbc_customerBtn.insets = new Insets(0, 0, 5, 5);
		gbc_customerBtn.gridx = 0;
		gbc_customerBtn.gridy = 12;
		getContentPane().add(customerBtn, gbc_customerBtn);
		
		textFieldCustomerSearch = new JTextField();
		textFieldCustomerSearch.setText("Studie ID: "); 
		textFieldCustomerSearch.setForeground(new Color(153, 153, 153));
		textFieldCustomerSearch.addFocusListener(new FocusAdapter() {
		@Override 
		public void focusGained(FocusEvent e) {
			if(textFieldCustomerSearch.getText().equals("Studie ID: ")) {
				textFieldCustomerSearch.setText(""); 
				textFieldCustomerSearch.setForeground(Color.BLACK); 
			}
		}
		@Override
		public void focusLost(FocusEvent e) {
			if(textFieldCustomerSearch.getText().equals("")) {
				textFieldCustomerSearch.setText("Studie ID: "); 
				textFieldCustomerSearch.setForeground(new Color(153, 153, 153)); 
			}
		}
			
			
		}); 
		GridBagConstraints gbc_textFieldCustomerSearch = new GridBagConstraints();
		gbc_textFieldCustomerSearch.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCustomerSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCustomerSearch.gridx = 1;
		gbc_textFieldCustomerSearch.gridy = 12;
		getContentPane().add(textFieldCustomerSearch, gbc_textFieldCustomerSearch);
		textFieldCustomerSearch.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Kunde: ");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 13;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		String name = "Standard Kunde"; 
		if(this.currentOrder.getCustomer() != null) {
				name = String.valueOf(this.currentOrder.getCustomer()); 
		}
		textFieldCustomer = new JTextField(name); 
		
		
		textFieldCustomer.setEditable(false);
		GridBagConstraints gbc_textFieldCustomer = new GridBagConstraints();
		gbc_textFieldCustomer.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCustomer.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCustomer.gridx = 1;
		gbc_textFieldCustomer.gridy = 13;
		getContentPane().add(textFieldCustomer, gbc_textFieldCustomer);
		textFieldCustomer.setColumns(10);
		

		


		GridBagConstraints gbc_mobilePayBtn = new GridBagConstraints();
		gbc_mobilePayBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_mobilePayBtn.insets = new Insets(0, 0, 5, 5);
		gbc_mobilePayBtn.gridx = 6;
		gbc_mobilePayBtn.gridy = 14;
		getContentPane().add(mobilePayBtn, gbc_mobilePayBtn);

		JButton cashBtn = new JButton("Kontant");
		cashBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        processPayment("Kontanter");
	
	
		    }
		});
		
		GridBagConstraints gbc_cashBtn = new GridBagConstraints();
		gbc_cashBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_cashBtn.insets = new Insets(0, 0, 5, 5);
		gbc_cashBtn.gridx = 7;
		gbc_cashBtn.gridy = 14;
		getContentPane().add(cashBtn, gbc_cashBtn);

		JButton cardPaymentBtn = new JButton("Kortbetaling");
		cardPaymentBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        processPayment("Kort");
		    }
		});
				
				JButton btnNewButton_1 = new JButton("Print Kvittering");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("===================="); 
			            System.out.println("Kvittering: "); 
			            System.out.println("Ordre Nummer: " + currentOrder.getOrderNumber());
			            System.out.println("Dato: " + currentOrder.getDate());   
			    		List<SaleOrderLine> orderLines = currentOrder.getOrderLines(); 
			    		for(SaleOrderLine orderLine: orderLines) {
			    			System.out.println(orderLine.getQuantity() + "x " + orderLine.getProduct() + ": " + orderLine.getPrice() + "kr"); 
			    		}
			    		
			    		
			    		String name = "Standard Kunde"; 
						if(currentOrder.getCustomer() != null) {
							name = String.valueOf(currentOrder.getCustomer()); 
						}
						
						System.out.println("Kunde: " +name); 



			            System.out.println("***********************"); 
			             System.out.println("Pris: " + currentOrder.getTotalPrice()); 
			             System.out.println("====================");
					}
				});
			
				
				GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
				gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
				gbc_btnNewButton_1.gridx = 8;
				gbc_btnNewButton_1.gridy = 14;
				getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
		
				JButton btnlogOut = new JButton("Log ud");
				GridBagConstraints gbc_btnlogOut = new GridBagConstraints();
				gbc_btnlogOut.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnlogOut.insets = new Insets(0, 0, 5, 5);
				gbc_btnlogOut.gridx = 1;
				gbc_btnlogOut.gridy = 15;
				getContentPane().add(btnlogOut, gbc_btnlogOut);
				
						btnlogOut.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								dispose();
									LogInd login = new LogInd(); 
									login.setVisible(true);
								
							}

						});
		
		GridBagConstraints gbc_cardPaymentBtn = new GridBagConstraints();
		gbc_cardPaymentBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_cardPaymentBtn.gridwidth = 2;
		gbc_cardPaymentBtn.insets = new Insets(0, 0, 5, 5);
		gbc_cardPaymentBtn.gridx = 6;
		gbc_cardPaymentBtn.gridy = 15;
		getContentPane().add(cardPaymentBtn, gbc_cardPaymentBtn);

		// Total panel
		JPanel totalPanel = new JPanel(new BorderLayout());
		GridBagConstraints gbc_totalPanel = new GridBagConstraints();
		gbc_totalPanel.gridwidth = 2;
		gbc_totalPanel.insets = new Insets(0, 0, 5, 5);
		gbc_totalPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_totalPanel.gridx = 6;
		gbc_totalPanel.gridy = 13;
		getContentPane().add(totalPanel, gbc_totalPanel);

		JLabel totalLabel = new JLabel("I alt:");
		totalField = new JTextField(String.valueOf(currentOrder.getTotalPrice()));
		totalField.setEditable(false);
		totalField.setHorizontalAlignment(JTextField.RIGHT);
		totalPanel.add(totalLabel, BorderLayout.WEST);
		totalPanel.add(totalField, BorderLayout.CENTER);

		JButton orderButton = new JButton("Opret Ordre");
		orderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			try {
				createOrder();
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
	        textFieldCustomerSearch.setText("Studie ID: "); 
				textFieldCustomerSearch.setForeground(new Color(153, 153, 153)); 
			}
		});
		GridBagConstraints gbc_orderButton = new GridBagConstraints();
		gbc_orderButton.insets = new Insets(0, 0, 5, 5);
		gbc_orderButton.gridx = 1;
		gbc_orderButton.gridy = 8;
		getContentPane().add(orderButton, gbc_orderButton);

		// Initialize UI components
		addCategoryButtons(panelCategory);
		
		pack(); 
			}
			
		

	private void addCategoryButtons(JPanel panelCategory) {
		panelCategory.setLayout(new GridLayout(0, 1));

		try {
			Connection conn = DB.DBConnection.getInstance().getConnection();
			String sql = "SELECT DISTINCT category FROM Drink";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			String productSql = "SELECT DISTINCT ProductName FROM Product";
			PreparedStatement productPs = conn.prepareStatement(productSql);
			ResultSet productRs = productPs.executeQuery();

			while (rs.next()) {
				String category = rs.getString("category");
				JButton catButton = new JButton(category);

				catButton.addActionListener(e -> {
					loadDrinksForCategory(category);
				});
				panelCategory.add(catButton);
			}
			panelCategory.revalidate();
			panelCategory.repaint();

		} catch (SQLException e) {
			panelCategory.revalidate();
		}
	}
	
	
	private void loadMisc() {
	panelMisc.removeAll();
	
	   panelMisc.setLayout(new BoxLayout(panelMisc, BoxLayout.Y_AXIS));
	
	try {
		List<Miscellaneous> miscellaneous = saleOrderCtr.getProductCtr().findAllMiscellaneous(); 
		
		for(Miscellaneous misc : miscellaneous) {
			JButton miscButton = new JButton(misc.getProductName() + " - " + misc.getSalePrice() + " kr"); 
			miscButton.addActionListener(e -> {
				try {
					addProductToOrder(misc.getProductName(), misc.getSalePrice()); 
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error adding product:" + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE); 
				ex.printStackTrace(); 
			}
			});
			panelMisc.add(miscButton); 
		}
	}catch (DataAccessException e) {
		JOptionPane.showMessageDialog(this,
	            "Error loading products: " + e.getMessage(),
	            "Error",
	            JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
	
	panelMisc.revalidate();  
	panelMisc.repaint(); 
	
	
	}

	private void loadDrinksForCategory(String category) {
		productPanel.removeAll();
		productPanel.setLayout(new GridLayout(0, 1));

		try {
			
			List<Drink> drinks = saleOrderCtr.getProductCtr().findByCategory(category); 
			
			for(Drink drink : drinks) {
				JButton productButton = new JButton(drink.getProductName() + " - " + 
			drink.getSalePrice() + " kr"); 
				productButton.addActionListener(e -> {
					try { 
						addProductToOrder(drink.getProductName(), 	drink.getSalePrice());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this, "Error adding product:" + ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE); 
						ex.printStackTrace(); 
					}
				}); 
					
				productPanel.add(productButton); 
			}
			

		} catch (DataAccessException e) {
			JOptionPane.showMessageDialog(this,
		            "Error loading drinks: " + e.getMessage(),
		            "Error",
		            JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		productPanel.revalidate();
		productPanel.repaint();
	}
	
	
	private void addProductToOrder(String productName, double price) throws DataAccessException {
		
		 System.out.println("[DEBUG] Current order: " + this.currentOrder);
		    System.out.println("[DEBUG] Current order number: " + (this.currentOrder != null ? this.currentOrder.getOrderNumber() : "null"));
		Product product = saleOrderCtr.getProductCtr().findByProductName(productName); 
		
		
		if(product != null) {
			int quantity = cartItems.getOrDefault(productName, 0) + 1; 
			
			
		    int barStock = inventoryCtr.getInventoryStockForProduct(1, product.getProductId());
		    

	
		    int totalStock = inventoryCtr.getTotalStock(product.getProductId());
		    
		  if(quantity > barStock) {
			JOptionPane.showMessageDialog(this, "Der er ikke flere produkter af " + "ved baren. Du bedes venligst hente noget mere fra lageret", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return; 
		  }

	
		    else if(quantity > totalStock) {
				JOptionPane.showMessageDialog(this, productName + " er udsolgt", "Error", JOptionPane.ERROR_MESSAGE);
				return; 
		

		
		} else {
			cartItems.put(productName, quantity); 
			itemPrices.put(productName, price); 
			
			updateTotal();
		saleOrderCtr.addProductToOrder(product, quantity); 
		saleOrderCtr.updateOrder(currentOrder); 
			
		}
		
//		currentOrder.setTotalPrice(total);
		
		
		
		
		updateOrderItemsUI();
		
	 
		} else {
			JOptionPane.showMessageDialog(this, "Produkt ikke at finde: " + productName, "Error", JOptionPane.ERROR_MESSAGE); 
		}
		

		

	}

	

	


	private void updateOrderItemsUI() {
		orderItemsPanel.removeAll();

		for (String item : cartItems.keySet()) {
			int quantity = cartItems.get(item);
			double price = itemPrices.get(item);
 
			JPanel itemPanel = new JPanel();
			itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			JLabel itemLabel = new JLabel(item + " - " + quantity + " x " + String.format("%.2f", price) + " kr");
			itemPanel.add(itemLabel);

			// "+" button to increase quantity
			JButton plusButton = new JButton("+");
			plusButton.addActionListener(e -> updateItemQuantity(item, quantity + 1));
			itemPanel.add(plusButton);

			// "-" button to decrease quantity
			JButton minusButton = new JButton("-");
			minusButton.addActionListener(e -> updateItemQuantity(item, quantity - 1));
			itemPanel.add(minusButton);

			orderItemsPanel.add(itemPanel);
		}
		orderItemsPanel.revalidate();
		orderItemsPanel.repaint();
	}
	
	
	
	private void updateItemQuantity(String item, int newQuantity) {
		try {
			Product product = saleOrderCtr.getProductCtr().findByProductName(item); 
		
			
			if(newQuantity <= inventoryCtr.getTotalStock(product.getProductId())) {
			
		
		if (newQuantity <= 0) {
			cartItems.remove(item);
			
		}else if(inventoryCtr.getInventoryStockForProduct(1, product.getProductId()) < newQuantity) {
			JOptionPane.showMessageDialog(this, "Der er ikke flere produkter af " + "ved baren. Du bedes venligst hente noget mere fra lageret", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return; 
			
		} else {
			cartItems.put(item, newQuantity);
		}
		
		for(SaleOrderLine orderLine: currentOrder.getOrderLines()) {
			
			if(orderLine.getProduct().getProductId() == product.getProductId()) {
				orderLine.setQuantity(newQuantity);
				break;
				
				
			}
		}
		
		updateTotal(); 
		
		saleOrderCtr.updateOrder(currentOrder);
		
//		saleOrderCtr.addProductToOrder(product, newQuantity); 
	

		updateOrderItemsUI();
		
//		
		currentOrder.setTotalPrice(total);
		
			} else {
				JOptionPane.showMessageDialog(this, product.getProductName() + " er udsolgt", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} catch (DataAccessException e) {
	        JOptionPane.showMessageDialog(this,
	            "Error updating quantity: " + e.getMessage(),
	            "Error",
	            JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
		}
	}
	
	
	
	
	
	private boolean confirmOrder() throws DataAccessException { 
		
		saleOrderCtr.confirmOrder(); 
	
		
		if(currentOrder.isStatus() == true) {
			JOptionPane.showMessageDialog(KasseSystem.this, "Ordre er afsluttet succesfuldt!", "Succes", JOptionPane.INFORMATION_MESSAGE); 
			Product product = null; 
			int quantity = 0; 
			Inventory inventory = inventoryCtr.findByInventoryId(1); 
			
			
			for(SaleOrderLine orderLine : currentOrder.getOrderLines()) {
				product = orderLine.getProduct(); 
				quantity = orderLine.getQuantity();
				inventoryCtr.removeStock(inventory, product, quantity); 
			}
	
			return true; 
		} else {
			JOptionPane.showMessageDialog(this, "Ordre er ikke afsluttet", "Fejl", JOptionPane.ERROR_MESSAGE); 
			return false; 
		}
		
		
	}

	private void updateTotal() {
		total = 0.0;
		for (String item : cartItems.keySet()) {
			int quantity = cartItems.get(item);
			double price = itemPrices.get(item);
			total += quantity * price;
		}
		currentOrder.setTotalPrice(total);
		System.out.println("[DEBUG] Updated total to: " + total);
		totalField.setText(String.valueOf(currentOrder.getTotalPrice())); 

		totalField.setText(String.format("%.2f kr", total));
	}
	
	private void processPayment(String method) {
		

	    if (total > 0 && currentOrder.isStatus() == true) {
	        JOptionPane.showMessageDialog(this, 
	            "Betaling på " + String.format("%.2f", total) + " kr via " + method + " gennemført.",
	            "Betaling Gennemført",
	            JOptionPane.INFORMATION_MESSAGE);
	        cartItems.clear();
	        itemPrices.clear();
	        orderItemsPanel.removeAll();
	        orderItemsPanel.revalidate();
	        orderItemsPanel.repaint();
	        total = 0.0;
	        totalField.setText(String.valueOf(currentOrder.getTotalPrice()));
	    } else if (currentOrder.isStatus() == false) {
	    	JOptionPane.showMessageDialog(this, "Hov hov makker. Du skal altså lige bekræfte ordren!", 
	    			"Fejl", JOptionPane.WARNING_MESSAGE); 
	    }
	    
	    else {
	        JOptionPane.showMessageDialog(this,
	            "Ingen varer i kurven.",
	            "Fejl",
	            JOptionPane.WARNING_MESSAGE);
	    }

	}
	
	private int generateRandomNumber() {
		return 100000 + new java.util.Random().nextInt(900000); 
	}

	private void findByStudentId() throws DataAccessException {
		
	int studentId =	Integer.parseInt(textFieldCustomerSearch.getText());  
	
	//Initializies a swingWorker to do background tasks without the GUI freezing or having long loading times
	SwingWorker<Customer, Void>  worker = new SwingWorker<Customer, Void>() {
		
		
		@Override
		protected Customer doInBackground() throws Exception {
//			Thread.sleep(10000);
			return saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
		}
		@Override
		protected void done() {
			try {
		
			Customer customer = get(); 
			
			
			if(customer == null) {
				JOptionPane.showMessageDialog(null, "Kunde ikke registreret i systemet", "Error", JOptionPane.ERROR_MESSAGE);
			       textFieldCustomerSearch.setText("Studie ID: "); 
							textFieldCustomerSearch.setForeground(new Color(153, 153, 153));
			return; 
			
			}
			
			try {
				saleOrderCtr.addCustomerToOrder(customer);
				textFieldCustomer.setText(String.valueOf(customer.getFirstName() + " " + customer.getLastName()));
				JOptionPane.showMessageDialog(null, customer.getFirstName() + " " + customer.getLastName() + " er hermed tilføjet til ordre", "Succes", JOptionPane.INFORMATION_MESSAGE);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	
			
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		} catch (ExecutionException e) {          
            e.printStackTrace();
        }
			}
		
	 
	 	
	 
	};

	
	

	
	
	worker.execute(); 
	

	
	}
	
	
	
private void createOrder() throws DataAccessException {

		try {
			try {
				
				textFieldCustomer.setText("Standard Kunde");
				
				textFieldOrderNr.setText(String.valueOf(generateRandomNumber())); 
			int orderNumber =	Integer.parseInt(textFieldOrderNr.getText()); 
			
				currentOrder = saleOrderCtr.createSaleOrder(orderNumber, LocalDate.now(), false, null, null, 0, 0);
		
				
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(KasseSystem.this, "Ordre er oprettet ", "Succes", JOptionPane.INFORMATION_MESSAGE); 
			
				textFieldOrderNr.setText(""); 
				textFieldOrderNr.setText(String.valueOf(currentOrder.getOrderNumber())); 
				cartItems.clear();
				itemPrices.clear();
		        orderItemsPanel.removeAll();
		        orderItemsPanel.revalidate();
		        orderItemsPanel.repaint();
		        total = 0.0;
		        totalField.setText("0 kr");
				

		} catch(IllegalArgumentException e) {
			e.printStackTrace(); 
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Order kunne ikke oprettes", JOptionPane.ERROR_MESSAGE);
			
		}
	

	
	


				}
	
}

package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.InventoryCtr;
import DB.DataAccessException;
import model.Inventory;
import model.InventoryProduct;
import model.Product;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JRadioButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class InventoryGui extends JDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox<Inventory> inventoryBox; 
	private InventoryCtr inventoryCtr; 
	private DefaultListModel<InventoryProduct> modelInventoryProduct; 
	private JTextField textField;
	private JTextField addStockTF;
	private JTextField removeStockTF;
	private JTextField maxCapacityTF;
	private JScrollPane scrollPane_1;
	private JList<Product> Category;
	private DefaultListModel<Product> categoryModel;
	private JTextField textSøgprodukt;
	private List<Product> allProducts;
	private Product selectedProduct;
	private JTextField kasseTextField; 
	private LogInd login; 
	
	// Added components for product transfer
	private JComboBox<Inventory> comboBoxFraLager;
	private JComboBox<Inventory> comboBoxTilLager;
	private JTextField flytMængdeTF;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JTextField ipText;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			InventoryGui dialog = new InventoryGui(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


	/**
	 * Create the dialog.
	 */
	public InventoryGui(JFrame parent) {
		try {
			inventoryCtr = new InventoryCtr();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	// initialization of the list for the inventory Product objects
		
		modelInventoryProduct = new DefaultListModel<>();
		
		categoryModel = new DefaultListModel<>();
		allProducts = new ArrayList<>();
		
		init(); 
		
		
		try {
			loadProducts();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			loadInventory();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	




	// method that loads all the inventories in the system: 
	private void loadInventory() throws DataAccessException {
		
		// Finds all the inventory objects via. the method from the controller. 
		List<Inventory> in = inventoryCtr.findAll(); 
		
// Combo DefaultComboBoxModels that drops down all the inventory objects
		DefaultComboBoxModel<Inventory> inventory = new DefaultComboBoxModel<Inventory>(); 
		DefaultComboBoxModel<Inventory> modelFrom = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<Inventory> modelTo = new DefaultComboBoxModel<>();
		
	
		for(int i = 0; i < in.size(); i++) {
			inventory.addElement(in.get(i));
			modelFrom.addElement(in.get(i));
			modelTo.addElement(in.get(i));
		}
		
		
		
		this.inventoryBox.setModel(inventory);
		comboBoxFraLager.setModel(modelFrom);
		comboBoxTilLager.setModel(modelTo);
		
		comboBoxFraLager.addActionListener(e -> {
			Inventory inventoryFrom = (Inventory) comboBoxFraLager.getSelectedItem();
			Inventory inventoryTo = (Inventory) comboBoxTilLager.getSelectedItem();
			if (inventoryFrom != null && inventoryFrom.equals(inventoryTo)) {
				comboBoxTilLager.setSelectedItem(null);
			}
		});

	}
	
	// Method that loads all the products from the system
	private void loadProducts() throws DataAccessException {
		// finds all the products
		allProducts = inventoryCtr.getProductCtr().findAll(); 
		categoryModel.clear();
		
		// Adds all the product objects to the categoyModel list 
		for(Product p : allProducts) {
			categoryModel.addElement(p);
		}
		Category.setModel(categoryModel);
	}

	
	// Finds the specific object of inventory product
	private void findInventoryProduct() throws DataAccessException {
		
		// Based on the inventory object and product object, the object of inventory product will show up in the JList
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem();
		
		if (inventory != null && selectedProduct != null) {
			InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(selectedProduct.getProductId(), inventory.getInventoryId()); 
			
			
			
			modelInventoryProduct.clear(); 
			if (inventoryProduct != null) {
				modelInventoryProduct.addElement(inventoryProduct);
			}
			updateStockInfo(selectedProduct, inventory);
			ipText.setText(String.valueOf(inventoryProduct.getQuantityInStock()));
		}
	}
	
	
	// Filters the product list so only products matching the search text appear in the JList.
	private void filterProducts(String searchText) {
		categoryModel.clear();
		
		if (searchText == null || searchText.trim().isEmpty()) {
			for (Product p : allProducts) {
				if(p != null) {
					categoryModel.addElement(p); 
				}
			}
		} else {
			searchText = searchText.toLowerCase();
			for (Product p : allProducts) {
				if(p != null && p.getProductName() != null && p.getProductName().toLowerCase().contains(searchText)) {
					categoryModel.addElement(p);
				}
			}
		}
	}
	
	// method that adds products to an inventory
	public void addStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		if (inventory == null || selectedProduct == null || addStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			// The quantity of stock the user wants to add is decided here
			int quantity = Integer.parseInt(addStockTF.getText()); 
			// to make sure that the max capacity of the inventory is not exceeded
			int max = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId()); 
			
			if(quantity + max > inventory.getCapacity()) {
				JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				// adds the stock to the inventory by calling the method in the inventory controller
				inventoryCtr.addStock(inventory, selectedProduct, quantity); 
				findInventoryProduct();
				// calling the updateStockInfo method in the gui, to visualize the updated values instantly. 
				updateStockInfo(selectedProduct, inventory);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	// Method that transfers stock from one inventory to another. 
	private void transferStock() throws DataAccessException {
		
		// Objects are based on the selected value from the combo boxes
	    Product product = (Product) Category.getSelectedValue();
	    Inventory from = (Inventory) comboBoxFraLager.getSelectedItem();
	    Inventory to = (Inventory) comboBoxTilLager.getSelectedItem();

	    if (product == null || from == null || to == null || from.equals(to) || flytMængdeTF.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vælg forskellige lagre, produkt og mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
	    	
	    	
	    	// Quantity is based on the input from the user in the text box
	        int quantity = Integer.parseInt(flytMængdeTF.getText());


	        
	        int fromInventory = inventoryCtr.getInventoryStockForProduct(from.getInventoryId(), product.getProductId()); 

	        if (quantity > fromInventory) {
	            JOptionPane.showMessageDialog(this, "Der er ikke nok produkter i 'fra'-lageret", "Fejl", JOptionPane.ERROR_MESSAGE);
	            return;
	        } 

	        
	        if(inventoryCtr.getTotalInventoryStock(to.getInventoryId()) + quantity > to.getCapacity() ) {
	        	 JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Fejl", JOptionPane.ERROR_MESSAGE);
	        	 return; 
	        } 
	    
	        // transferring stock from one inventory to another by calling the method in the inventory controller. 
	        inventoryCtr.transferStock(to, from, product, quantity); 
	        
	     // calling the updateStockInfo method in the gui, to visualize the updated values instantly. 
	        updateStockInfo(product, (Inventory) inventoryBox.getSelectedItem()); 
	        // empties the text field, after the action is done. 
	        flytMængdeTF.setText("");

	        Inventory inventoryView = (Inventory) inventoryBox.getSelectedItem();
	        InventoryProduct updated = inventoryCtr.findInventoryProduct(product.getProductId(), inventoryView.getInventoryId());
	        modelInventoryProduct.clear();
	        if (updated != null) {
	            modelInventoryProduct.addElement(updated);
	        }
    

	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Ugyldig mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
	    }

	}
	
	// method to remove stock from inventory
	public void removeStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		
		if (inventory == null || selectedProduct == null || removeStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			
			// quantity of the stock to be removed is decided in this text field: 
			int quantity = Integer.parseInt(removeStockTF.getText()); 
			
			
			// To make sure that the user does not remove more products from the inventory than the stock value for the product
			if(quantity > inventoryCtr.getInventoryStockForProduct(inventory.getInventoryId(), selectedProduct.getProductId())) {
				JOptionPane.showMessageDialog(this, "Så stor mængde af dette produkt findes slet ikke på lageret!", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				// Removing product from inventory by calling the method in the inventory controller
				inventoryCtr.removeStock(inventory, selectedProduct, quantity); 
				findInventoryProduct();
				updateStockInfo(selectedProduct, inventory);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Method that updates the information about the inventory. 
	private void updateStockInfo(Product product, Inventory inventory) throws DataAccessException {
		if (product != null) {
			int totalStock = inventoryCtr.getTotalStock(product.getProductId());
			InventoryProduct ip = inventoryCtr.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 
			textField.setText(String.valueOf(totalStock));
			ipText.setText(String.valueOf(ip.getQuantityInStock()));
		}
		
		if (inventory != null) {
			int totalStock = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId());  
			maxCapacityTF.setText(String.valueOf(totalStock + "/" + String.valueOf(inventory.getCapacity())));
			InventoryProduct ip = inventoryCtr.findInventoryProduct(product.getProductId(), inventory.getInventoryId());
			ipText.setText(String.valueOf(ip.getQuantityInStock()));
		}
	}
	
	
	
	// Sets up the graphical user interface for the inventory management screen.
	public void init() {
		setBounds(100, 100, 804, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 25, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 38, 0, 31, 0, 0, 0, 0, 0, 56, 0, 0, 0, 0, 41, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Lager");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblProduktnavn = new JLabel("Søg Efter Produkt: ");
		GridBagConstraints gbc_lblProduktnavn = new GridBagConstraints();
		gbc_lblProduktnavn.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduktnavn.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblProduktnavn.gridx = 2;
		gbc_lblProduktnavn.gridy = 2;
		getContentPane().add(lblProduktnavn, gbc_lblProduktnavn);
		
		textSøgprodukt = new JTextField();
		textSøgprodukt.setText("Indtast Produktnavn: "); 
		textSøgprodukt.setForeground(new Color(153, 153, 153));
		textSøgprodukt.addFocusListener(new FocusAdapter() {
		@Override 
		public void focusGained(FocusEvent e) {
			if(textSøgprodukt.getText().equals("Indtast Produktnavn: ")) {
				textSøgprodukt.setText(""); 
				textSøgprodukt.setForeground(Color.BLACK); 
			}
		}
		@Override
		public void focusLost(FocusEvent e) {
			if(textSøgprodukt.getText().equals("")) {
				textSøgprodukt.setText("Indtast Produktnavn: "); 
				textSøgprodukt.setForeground(new Color(153, 153, 153)); 
			}
		}
			
			
		}); 
		textSøgprodukt.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				filterProducts(textSøgprodukt.getText());
			}
		});
		
		lblNewLabel_6 = new JLabel("Vælg Lager:");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 10;
		gbc_lblNewLabel_6.gridy = 2;
		getContentPane().add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		lblNewLabel_2 = new JLabel("Angiv mængde i tal");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 18;
		gbc_lblNewLabel_2.gridy = 2;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		GridBagConstraints gbc_textSøgprodukt = new GridBagConstraints();
		gbc_textSøgprodukt.insets = new Insets(0, 0, 5, 5);
		gbc_textSøgprodukt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSøgprodukt.gridx = 2;
		gbc_textSøgprodukt.gridy = 4;
		getContentPane().add(textSøgprodukt, gbc_textSøgprodukt);
		textSøgprodukt.setColumns(10);
		
		JButton addProductBtn = new JButton("Tilføj produkt");
		addProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addStock();
					addStockTF.setText(""); 
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				} 
			}
		});
		
		inventoryBox = new JComboBox<Inventory>();
		// Implements action listener for when a certain value in the JComboBox is selected
		inventoryBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (selectedProduct != null) {
						findInventoryProduct();
					}
					Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
					if (inventory != null) {
						int totalStock = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId());  
						maxCapacityTF.setText(String.valueOf(totalStock + "/" + String.valueOf(inventory.getCapacity()))); 
					}
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				} 
			}
		});
		GridBagConstraints gbc_inventoryBox = new GridBagConstraints();
		gbc_inventoryBox.gridwidth = 3;
		gbc_inventoryBox.insets = new Insets(0, 0, 5, 5);
		gbc_inventoryBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_inventoryBox.gridx = 9;
		gbc_inventoryBox.gridy = 4;
		getContentPane().add(inventoryBox, gbc_inventoryBox);
		GridBagConstraints gbc_addProductBtn = new GridBagConstraints();
		gbc_addProductBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_addProductBtn.insets = new Insets(0, 0, 5, 5);
		gbc_addProductBtn.gridx = 17;
		gbc_addProductBtn.gridy = 4;
		getContentPane().add(addProductBtn, gbc_addProductBtn);
		
		addStockTF = new JTextField();
		GridBagConstraints gbc_addStockTF = new GridBagConstraints();
		gbc_addStockTF.insets = new Insets(0, 0, 5, 0);
		gbc_addStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_addStockTF.gridx = 18;
		gbc_addStockTF.gridy = 4;
		getContentPane().add(addStockTF, gbc_addStockTF);
		addStockTF.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 5;
		gbc_scrollPane_1.anchor = GridBagConstraints.BASELINE;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH; 
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 5;
		gbc_scrollPane_1.gridwidth = 3; 

		getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		Category = new JList<>();
		Category.setModel(categoryModel);
		Category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Category.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedProduct = Category.getSelectedValue();
					if (selectedProduct != null) {
						try {
							findInventoryProduct();
						} catch (DataAccessException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		scrollPane_1.setViewportView(Category);
		

		JButton removeProductBtn = new JButton("Fjern produkt");
		removeProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeStock();
					removeStockTF.setText("");
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				} 
			}
		});
		
		lblNewLabel_5 = new JLabel("Lagerkapacitet for valgte lager:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 8;
		gbc_lblNewLabel_5.gridy = 5;
		getContentPane().add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		maxCapacityTF = new JTextField();
		maxCapacityTF.setEditable(false);
		GridBagConstraints gbc_maxCapacityTF = new GridBagConstraints();
		gbc_maxCapacityTF.gridwidth = 4;
		gbc_maxCapacityTF.insets = new Insets(0, 0, 5, 5);
		gbc_maxCapacityTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxCapacityTF.gridx = 9;
		gbc_maxCapacityTF.gridy = 5;
		getContentPane().add(maxCapacityTF, gbc_maxCapacityTF);
		maxCapacityTF.setColumns(10);
		GridBagConstraints gbc_removeProductBtn = new GridBagConstraints();
		gbc_removeProductBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeProductBtn.insets = new Insets(0, 0, 5, 5);
		gbc_removeProductBtn.gridx = 17;
		gbc_removeProductBtn.gridy = 5;
		getContentPane().add(removeProductBtn, gbc_removeProductBtn);
		
		removeStockTF = new JTextField();
		GridBagConstraints gbc_removeStockTF = new GridBagConstraints();
		gbc_removeStockTF.insets = new Insets(0, 0, 5, 0);
		gbc_removeStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeStockTF.gridx = 18;
		gbc_removeStockTF.gridy = 5;
		getContentPane().add(removeStockTF, gbc_removeStockTF);
		removeStockTF.setColumns(10);
		
		JButton btnTilfjNyProdukt = new JButton("Tilføj nyt produkt");
		GridBagConstraints gbc_btnTilfjNyProdukt = new GridBagConstraints();
		gbc_btnTilfjNyProdukt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTilfjNyProdukt.insets = new Insets(0, 0, 5, 5);
		gbc_btnTilfjNyProdukt.gridx = 17;
		gbc_btnTilfjNyProdukt.gridy = 7;
		getContentPane().add(btnTilfjNyProdukt, gbc_btnTilfjNyProdukt);
		
		JLabel lblFra = new JLabel("Flyt fra: ");
		lblFra.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblFra = new GridBagConstraints();
		gbc_lblFra.anchor = GridBagConstraints.EAST;
		gbc_lblFra.insets = new Insets(0, 0, 5, 5);
		gbc_lblFra.gridx = 17;
		gbc_lblFra.gridy = 8;
		getContentPane().add(lblFra, gbc_lblFra);

		comboBoxFraLager = new JComboBox<>();
		GridBagConstraints gbc_comboBoxFraLager = new GridBagConstraints();
		gbc_comboBoxFraLager.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFraLager.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFraLager.gridx = 18;
		gbc_comboBoxFraLager.gridy = 8;
		getContentPane().add(comboBoxFraLager, gbc_comboBoxFraLager);

		JLabel lblTil = new JLabel("Flyt til:");
		lblTil.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblTil = new GridBagConstraints();
		gbc_lblTil.anchor = GridBagConstraints.EAST;
		gbc_lblTil.insets = new Insets(0, 0, 5, 5);
		gbc_lblTil.gridx = 17;
		gbc_lblTil.gridy = 10;
		getContentPane().add(lblTil, gbc_lblTil);

		comboBoxTilLager = new JComboBox<>();
		GridBagConstraints gbc_comboBoxTilLager = new GridBagConstraints();
		gbc_comboBoxTilLager.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTilLager.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTilLager.gridx = 18;
		gbc_comboBoxTilLager.gridy = 10;
		getContentPane().add(comboBoxTilLager, gbc_comboBoxTilLager);
		
		lblNewLabel_7 = new JLabel("Produktets Lager Status: ");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 2;
		gbc_lblNewLabel_7.gridy = 11;
		getContentPane().add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		lblNewLabel_3 = new JLabel("Angiv Mængde i Tal");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_3.gridx = 18;
		gbc_lblNewLabel_3.gridy = 11;
		getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("Antal på pågældende lager");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 12;
		getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		ipText = new JTextField();
		ipText.setEditable(false);
		GridBagConstraints gbc_ipText = new GridBagConstraints();
		gbc_ipText.insets = new Insets(0, 0, 5, 5);
		gbc_ipText.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipText.gridx = 2;
		gbc_ipText.gridy = 12;
		getContentPane().add(ipText, gbc_ipText);
		ipText.setColumns(10);
		
		JLabel lblMængde = new JLabel("Mængde");
		lblMængde.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblMængde = new GridBagConstraints();
		gbc_lblMængde.anchor = GridBagConstraints.EAST;
		gbc_lblMængde.insets = new Insets(0, 0, 5, 5);
		gbc_lblMængde.gridx = 17;
		gbc_lblMængde.gridy = 12;
		getContentPane().add(lblMængde, gbc_lblMængde);
		
		flytMængdeTF = new JTextField();
		GridBagConstraints gbc_flytMængdeTF = new GridBagConstraints();
		gbc_flytMængdeTF.insets = new Insets(0, 0, 5, 0);
		gbc_flytMængdeTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_flytMængdeTF.gridx = 18;
		gbc_flytMængdeTF.gridy = 12;
		getContentPane().add(flytMængdeTF, gbc_flytMængdeTF);
		flytMængdeTF.setColumns(10);
		
		JButton flytBtn = new JButton("Flyt vare");
		flytBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					transferStock();
				} catch (DataAccessException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Antal på tværs af alle lagere");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 13;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 13;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		GridBagConstraints gbc_flytBtn = new GridBagConstraints();
		gbc_flytBtn.anchor = GridBagConstraints.NORTH;
		gbc_flytBtn.insets = new Insets(0, 0, 5, 0);
		gbc_flytBtn.gridx = 18;
		gbc_flytBtn.gridy = 13;
		getContentPane().add(flytBtn, gbc_flytBtn);
		
		JButton btnKassesystem = new JButton("Tilbage");
		btnKassesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});
		
		GridBagConstraints gbc_btnKassesystem = new GridBagConstraints();
		gbc_btnKassesystem.anchor = GridBagConstraints.WEST;
		gbc_btnKassesystem.insets = new Insets(0, 0, 0, 5);
		gbc_btnKassesystem.fill = GridBagConstraints.VERTICAL;
		gbc_btnKassesystem.gridx = 0;
		gbc_btnKassesystem.gridy = 19;
		getContentPane().add(btnKassesystem, gbc_btnKassesystem);
		
		pack(); 
	}
}
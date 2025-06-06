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
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
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
	private JList<InventoryProduct> inventoryProductList;  
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
		inventoryProductList = new JList<>(modelInventoryProduct); 
		inventoryProductList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		
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
			textField.setText(String.valueOf(totalStock));
		}
		
		if (inventory != null) {
			int totalStock = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId());  
			maxCapacityTF.setText(String.valueOf(totalStock + "/" + String.valueOf(inventory.getCapacity())));
		}
	}
	
	
	
	// Sets up the graphical user interface for the inventory management screen.
	public void init() {
		setBounds(100, 100, 804, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 25, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 38, 0, 31, 0, 0, 0, 0, 0, 56, 0, 0, 0, 0, 41, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		
		JLabel lblProduktnavn = new JLabel("Produkt navn:");
		GridBagConstraints gbc_lblProduktnavn = new GridBagConstraints();
		gbc_lblProduktnavn.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduktnavn.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblProduktnavn.gridx = 2;
		gbc_lblProduktnavn.gridy = 2;
		getContentPane().add(lblProduktnavn, gbc_lblProduktnavn);
		GridBagConstraints gbc_inventoryBox = new GridBagConstraints();
		gbc_inventoryBox.gridwidth = 3;
		gbc_inventoryBox.insets = new Insets(0, 0, 5, 5);
		gbc_inventoryBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_inventoryBox.gridx = 3;
		gbc_inventoryBox.gridy = 2;
		getContentPane().add(inventoryBox, gbc_inventoryBox);
		
		textSøgprodukt = new JTextField();
		textSøgprodukt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterProducts(textSøgprodukt.getText());
			}
		});
		
		maxCapacityTF = new JTextField();
		maxCapacityTF.setEditable(false);
		GridBagConstraints gbc_maxCapacityTF = new GridBagConstraints();
		gbc_maxCapacityTF.gridwidth = 4;
		gbc_maxCapacityTF.insets = new Insets(0, 0, 5, 5);
		gbc_maxCapacityTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxCapacityTF.gridx = 13;
		gbc_maxCapacityTF.gridy = 2;
		getContentPane().add(maxCapacityTF, gbc_maxCapacityTF);
		maxCapacityTF.setColumns(10);
		
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
		gbc_textSøgprodukt.gridy = 3;
		getContentPane().add(textSøgprodukt, gbc_textSøgprodukt);
		textSøgprodukt.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 13;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 14;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		inventoryProductList.setModel(modelInventoryProduct);
		scrollPane.setViewportView(inventoryProductList);
		
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
		GridBagConstraints gbc_addProductBtn = new GridBagConstraints();
		gbc_addProductBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_addProductBtn.insets = new Insets(0, 0, 5, 5);
		gbc_addProductBtn.gridx = 17;
		gbc_addProductBtn.gridy = 3;
		getContentPane().add(addProductBtn, gbc_addProductBtn);
		
		addStockTF = new JTextField();
		GridBagConstraints gbc_addStockTF = new GridBagConstraints();
		gbc_addStockTF.insets = new Insets(0, 0, 5, 0);
		gbc_addStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_addStockTF.gridx = 18;
		gbc_addStockTF.gridy = 3;
		getContentPane().add(addStockTF, gbc_addStockTF);
		addStockTF.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 5;
		gbc_scrollPane_1.anchor = GridBagConstraints.BASELINE;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 4;
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
		GridBagConstraints gbc_removeProductBtn = new GridBagConstraints();
		gbc_removeProductBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeProductBtn.insets = new Insets(0, 0, 5, 5);
		gbc_removeProductBtn.gridx = 17;
		gbc_removeProductBtn.gridy = 4;
		getContentPane().add(removeProductBtn, gbc_removeProductBtn);
		
		removeStockTF = new JTextField();
		GridBagConstraints gbc_removeStockTF = new GridBagConstraints();
		gbc_removeStockTF.insets = new Insets(0, 0, 5, 0);
		gbc_removeStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeStockTF.gridx = 18;
		gbc_removeStockTF.gridy = 4;
		getContentPane().add(removeStockTF, gbc_removeStockTF);
		removeStockTF.setColumns(10);
		
		JButton btnTilfjNyProdukt = new JButton("Tilføj nyt produkt");
		GridBagConstraints gbc_btnTilfjNyProdukt = new GridBagConstraints();
		gbc_btnTilfjNyProdukt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTilfjNyProdukt.insets = new Insets(0, 0, 5, 5);
		gbc_btnTilfjNyProdukt.gridx = 17;
		gbc_btnTilfjNyProdukt.gridy = 6;
		getContentPane().add(btnTilfjNyProdukt, gbc_btnTilfjNyProdukt);
		
		JLabel lblFra = new JLabel("Flyt fra Lager");
		lblFra.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblFra = new GridBagConstraints();
		gbc_lblFra.anchor = GridBagConstraints.EAST;
		gbc_lblFra.insets = new Insets(0, 0, 5, 5);
		gbc_lblFra.gridx = 17;
		gbc_lblFra.gridy = 7;
		getContentPane().add(lblFra, gbc_lblFra);

		comboBoxFraLager = new JComboBox<>();
		GridBagConstraints gbc_comboBoxFraLager = new GridBagConstraints();
		gbc_comboBoxFraLager.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxFraLager.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFraLager.gridx = 18;
		gbc_comboBoxFraLager.gridy = 7;
		getContentPane().add(comboBoxFraLager, gbc_comboBoxFraLager);

		JLabel lblTil = new JLabel("Flyt til Lager");
		lblTil.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblTil = new GridBagConstraints();
		gbc_lblTil.anchor = GridBagConstraints.EAST;
		gbc_lblTil.insets = new Insets(0, 0, 5, 5);
		gbc_lblTil.gridx = 17;
		gbc_lblTil.gridy = 9;
		getContentPane().add(lblTil, gbc_lblTil);

		comboBoxTilLager = new JComboBox<>();
		GridBagConstraints gbc_comboBoxTilLager = new GridBagConstraints();
		gbc_comboBoxTilLager.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTilLager.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTilLager.gridx = 18;
		gbc_comboBoxTilLager.gridy = 9;
		getContentPane().add(comboBoxTilLager, gbc_comboBoxTilLager);
		
		lblNewLabel_3 = new JLabel("Angiv Mængde i Tal");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_3.gridx = 18;
		gbc_lblNewLabel_3.gridy = 10;
		getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JLabel lblMængde = new JLabel("Mængde");
		lblMængde.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblMængde = new GridBagConstraints();
		gbc_lblMængde.anchor = GridBagConstraints.EAST;
		gbc_lblMængde.insets = new Insets(0, 0, 5, 5);
		gbc_lblMængde.gridx = 17;
		gbc_lblMængde.gridy = 11;
		getContentPane().add(lblMængde, gbc_lblMængde);
		
		flytMængdeTF = new JTextField();
		GridBagConstraints gbc_flytMængdeTF = new GridBagConstraints();
		gbc_flytMængdeTF.insets = new Insets(0, 0, 5, 0);
		gbc_flytMængdeTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_flytMængdeTF.gridx = 18;
		gbc_flytMængdeTF.gridy = 11;
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
		GridBagConstraints gbc_flytBtn = new GridBagConstraints();
		gbc_flytBtn.anchor = GridBagConstraints.NORTH;
		gbc_flytBtn.insets = new Insets(0, 0, 5, 0);
		gbc_flytBtn.gridx = 18;
		gbc_flytBtn.gridy = 12;
		getContentPane().add(flytBtn, gbc_flytBtn);
		
		JLabel lblNewLabel_1 = new JLabel("I alt på lager:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 16;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 16;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnKassesystem = new JButton("Tilbage");
		btnKassesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KasseSystem frame = new KasseSystem(null, null); 
				frame.setVisible(true); 
			}
		});
		
		GridBagConstraints gbc_btnKassesystem = new GridBagConstraints();
		gbc_btnKassesystem.anchor = GridBagConstraints.WEST;
		gbc_btnKassesystem.insets = new Insets(0, 0, 5, 5);
		gbc_btnKassesystem.fill = GridBagConstraints.VERTICAL;
		gbc_btnKassesystem.gridx = 2;
		gbc_btnKassesystem.gridy = 15;
		getContentPane().add(btnKassesystem, gbc_btnKassesystem);
	}
}
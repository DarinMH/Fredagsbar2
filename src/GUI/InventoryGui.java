package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
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
	
	// Added components for product transfer
	private JComboBox<Inventory> comboBoxFraLager;
	private JComboBox<Inventory> comboBoxTilLager;
	private JTextField flytMængdeTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InventoryGui dialog = new InventoryGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


	/**
	 * Create the dialog.
	 */
	public InventoryGui() {
		try {
			inventoryCtr = new InventoryCtr();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
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


	private void loadInventory() throws DataAccessException {
		List<Inventory> in = inventoryCtr.findAll(); 
		
		DefaultComboBoxModel<Inventory> inventory = new DefaultComboBoxModel<Inventory>(); 
		DefaultComboBoxModel<Inventory> modelFra = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<Inventory> modelTil = new DefaultComboBoxModel<>();
		
		for(int i = 0; i < in.size(); i++) {
			inventory.addElement(in.get(i));
			modelFra.addElement(in.get(i));
			modelTil.addElement(in.get(i));
		}
		
		this.inventoryBox.setModel(inventory);
		comboBoxFraLager.setModel(modelFra);
		comboBoxTilLager.setModel(modelTil);
		
		comboBoxFraLager.addActionListener(e -> {
			Inventory fra = (Inventory) comboBoxFraLager.getSelectedItem();
			Inventory til = (Inventory) comboBoxTilLager.getSelectedItem();
			if (fra != null && fra.equals(til)) {
				comboBoxTilLager.setSelectedItem(null);
			}
		});

		comboBoxTilLager.addActionListener(e -> {
			Inventory fra = (Inventory) comboBoxFraLager.getSelectedItem();
			Inventory til = (Inventory) comboBoxTilLager.getSelectedItem();
			if (til != null && til.equals(fra)) {
				comboBoxFraLager.setSelectedItem(null);
			}
		});
	}
	
	
	private void loadProducts() throws DataAccessException {
		allProducts = inventoryCtr.getProductCtr().findAll(); 
		categoryModel.clear();
		for(Product p : allProducts) {
			categoryModel.addElement(p);
		}
		Category.setModel(categoryModel);
	}

	
	private void findInventoryProduct() throws DataAccessException {
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
	
	private void filterProducts(String searchText) {
		categoryModel.clear();
		
		if (searchText == null || searchText.trim().isEmpty()) {
			for (Product p : allProducts) {
				categoryModel.addElement(p);
			}
		} else {
			searchText = searchText.toLowerCase();
			for (Product p : allProducts) {
				if (p.getProductName().toLowerCase().contains(searchText)) {
					categoryModel.addElement(p);
				}
			}
		}
	}
	
	
	public void addStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		
		if (inventory == null || selectedProduct == null || addStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int quantity = Integer.parseInt(addStockTF.getText()); 
			
			int max = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId()); 
			
			if(quantity + max > inventory.getCapacity()) {
				JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				inventoryCtr.addStock(inventory, selectedProduct, quantity); 
				findInventoryProduct();
				updateStockInfo(selectedProduct, inventory);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
		}

		}
	
	
	
	private void transferStock() throws DataAccessException {
//	    Product product = (Product) productBox.getSelectedItem();
		Product product = (Product) Category.getSelectedValue();
	    Inventory from = (Inventory) comboBoxFraLager.getSelectedItem();
	    Inventory to = (Inventory) comboBoxTilLager.getSelectedItem();

	    if (product == null || from == null || to == null || from.equals(to) || flytMængdeTF.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vælg forskellige lagre, produkt og mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
	        int quantity = Integer.parseInt(flytMængdeTF.getText());

	        int fromInventory = inventoryCtr.getInventoryStockForProduct(from.getInventoryId(), product.getProductId());
	        
	        

	        if (quantity > fromInventory) {
	            JOptionPane.showMessageDialog(this, "Der er ikke nok produkter i 'fra'-lageret", "Fejl", JOptionPane.ERROR_MESSAGE);
	            return;
	        } 
	        
	        
	        int toInventory = inventoryCtr.getTotalInventoryStock(to.getCapacity()); 
	        
	        if(inventoryCtr.getTotalInventoryStock(to.getInventoryId()) + quantity > to.getCapacity() ) {
	        	 JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Fejl", JOptionPane.ERROR_MESSAGE);
	        	 return; 
	        } 
	    
	        	
	        

	        inventoryCtr.removeStock(from, product, quantity);
	        inventoryCtr.addStock(to, product, quantity);

	        updateStockInfo(product, (Inventory) inventoryBox.getSelectedItem()); // behold aktiv visning
	        flytMængdeTF.setText("");

	        Inventory inventoryView = (Inventory) inventoryBox.getSelectedItem();
	        InventoryProduct updated = inventoryCtr.findInventoryProduct(product.getProductId(), inventoryView.getInventoryId());
	        modelInventoryProduct.clear();
	        if (updated != null) {
	            modelInventoryProduct.addElement(updated);
	        }

	        System.out.println("Flyttede " + quantity + " fra " + from.getLocation() + " til " + to.getLocation());
	        System.out.println("Fra-lager: " + inventoryCtr.getInventoryStockForProduct(from.getInventoryId(), product.getProductId()));
	        System.out.println("Til-lager: " + inventoryCtr.getInventoryStockForProduct(to.getInventoryId(), product.getProductId()));
	        
	        

	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Ugyldig mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
	    }

	}
	
	
	public void removeStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		
		if (inventory == null || selectedProduct == null || removeStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int quantity = Integer.parseInt(removeStockTF.getText()); 
			
			if(quantity > inventoryCtr.getTotalStock(selectedProduct.getProductId())) {
				JOptionPane.showMessageDialog(this, "Så stor mængde af dette produkt findes slet ikke på lageret!", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				inventoryCtr.removeStock(inventory, selectedProduct, quantity); 
				findInventoryProduct();
				updateStockInfo(selectedProduct, inventory);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void flytVare() throws DataAccessException {
		Inventory from = (Inventory) comboBoxFraLager.getSelectedItem();
		Inventory to = (Inventory) comboBoxTilLager.getSelectedItem();
		
		if (selectedProduct == null || from == null || to == null || from.equals(to) || flytMængdeTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg forskellige lagre, produkt og mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int quantity = Integer.parseInt(flytMængdeTF.getText());
			
			int fromInventory = inventoryCtr.getInventoryStockForProduct(from.getInventoryId(), selectedProduct.getProductId());
			
			if (quantity > fromInventory) {
				JOptionPane.showMessageDialog(this, "Der er ikke nok produkter i 'fra'-lageret", "Fejl", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int toInventory = inventoryCtr.getTotalInventoryStock(to.getInventoryId());
			
			if (toInventory + quantity > to.getCapacity()) {
				JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Fejl", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			inventoryCtr.removeStock(from, selectedProduct, quantity);
			inventoryCtr.addStock(to, selectedProduct, quantity);
			
			updateStockInfo(selectedProduct, (Inventory) inventoryBox.getSelectedItem()); // behold aktiv visning
			flytMængdeTF.setText("");
			
			Inventory inventoryView = (Inventory) inventoryBox.getSelectedItem();
			if (inventoryView != null) {
				InventoryProduct updated = inventoryCtr.findInventoryProduct(selectedProduct.getProductId(), inventoryView.getInventoryId());
				modelInventoryProduct.clear();
				if (updated != null) {
					modelInventoryProduct.addElement(updated);
				}
			}
			
			System.out.println("Flyttede " + quantity + " fra " + from.getLocation() + " til " + to.getLocation());
			System.out.println("Fra-lager: " + inventoryCtr.getInventoryStockForProduct(from.getInventoryId(), selectedProduct.getProductId()));
			System.out.println("Til-lager: " + inventoryCtr.getInventoryStockForProduct(to.getInventoryId(), selectedProduct.getProductId()));
			
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
		}
	}
	
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
	
	public void init() {
		setBounds(100, 100, 800, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{53, 145, 0, 0, 0, 0, 0, 0, 0, 0, 130, 0, 0, 32};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 38, 0, 31, 0, 0, 0, 0, 0, 56, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Lager");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		inventoryBox = new JComboBox<Inventory>();
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
		gbc_inventoryBox.gridx = 2;
		gbc_inventoryBox.gridy = 1;
		getContentPane().add(inventoryBox, gbc_inventoryBox);
		
		maxCapacityTF = new JTextField();
		maxCapacityTF.setEditable(false);
		GridBagConstraints gbc_maxCapacityTF = new GridBagConstraints();
		gbc_maxCapacityTF.gridwidth = 4;
		gbc_maxCapacityTF.insets = new Insets(0, 0, 5, 5);
		gbc_maxCapacityTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxCapacityTF.gridx = 6;
		gbc_maxCapacityTF.gridy = 1;
		getContentPane().add(maxCapacityTF, gbc_maxCapacityTF);
		maxCapacityTF.setColumns(10);
		
		JLabel lblProduktnavn = new JLabel("Produkt navn:");
		GridBagConstraints gbc_lblProduktnavn = new GridBagConstraints();
		gbc_lblProduktnavn.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduktnavn.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblProduktnavn.gridx = 1;
		gbc_lblProduktnavn.gridy = 2;
		getContentPane().add(lblProduktnavn, gbc_lblProduktnavn);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 13;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 2;
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
		gbc_addProductBtn.gridx = 10;
		gbc_addProductBtn.gridy = 2;
		getContentPane().add(addProductBtn, gbc_addProductBtn);
		
		textSøgprodukt = new JTextField();
		textSøgprodukt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterProducts(textSøgprodukt.getText());
			}
		});
		
		addStockTF = new JTextField();
		GridBagConstraints gbc_addStockTF = new GridBagConstraints();
		gbc_addStockTF.insets = new Insets(0, 0, 5, 5);
		gbc_addStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_addStockTF.gridx = 11;
		gbc_addStockTF.gridy = 2;
		getContentPane().add(addStockTF, gbc_addStockTF);
		addStockTF.setColumns(10);
		GridBagConstraints gbc_textSøgprodukt = new GridBagConstraints();
		gbc_textSøgprodukt.insets = new Insets(0, 0, 5, 5);
		gbc_textSøgprodukt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSøgprodukt.gridx = 1;
		gbc_textSøgprodukt.gridy = 3;
		getContentPane().add(textSøgprodukt, gbc_textSøgprodukt);
		textSøgprodukt.setColumns(10);
		

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
		gbc_removeProductBtn.gridx = 10;
		gbc_removeProductBtn.gridy = 3;
		getContentPane().add(removeProductBtn, gbc_removeProductBtn);
		
		removeStockTF = new JTextField();
		GridBagConstraints gbc_removeStockTF = new GridBagConstraints();
		gbc_removeStockTF.insets = new Insets(0, 0, 5, 5);
		gbc_removeStockTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_removeStockTF.gridx = 11;
		gbc_removeStockTF.gridy = 3;
		getContentPane().add(removeStockTF, gbc_removeStockTF);
		removeStockTF.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 5;
		gbc_scrollPane_1.anchor = GridBagConstraints.BASELINE;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrollPane_1.gridx = 1;
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
		
		JButton btnTilfjNyProdukt = new JButton("Tilføj ny produkt");
		GridBagConstraints gbc_btnTilfjNyProdukt = new GridBagConstraints();
		gbc_btnTilfjNyProdukt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTilfjNyProdukt.insets = new Insets(0, 0, 5, 5);
		gbc_btnTilfjNyProdukt.gridx = 10;
		gbc_btnTilfjNyProdukt.gridy = 4;
		getContentPane().add(btnTilfjNyProdukt, gbc_btnTilfjNyProdukt);
		
		// Add components for product transfer
		JLabel lblFra = new JLabel("Flyt fra Lager:");
		lblFra.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblFra = new GridBagConstraints();
		gbc_lblFra.anchor = GridBagConstraints.WEST;
		gbc_lblFra.insets = new Insets(0, 0, 5, 5);
		gbc_lblFra.gridx = 10;
		gbc_lblFra.gridy = 6;
		getContentPane().add(lblFra, gbc_lblFra);
						
								comboBoxFraLager = new JComboBox<>();
								GridBagConstraints gbc_comboBoxFraLager = new GridBagConstraints();
								gbc_comboBoxFraLager.insets = new Insets(0, 0, 5, 5);
								gbc_comboBoxFraLager.fill = GridBagConstraints.HORIZONTAL;
								gbc_comboBoxFraLager.gridx = 11;
								gbc_comboBoxFraLager.gridy = 6;
								getContentPane().add(comboBoxFraLager, gbc_comboBoxFraLager);
				
						JLabel lblTil = new JLabel("Flyt til Lager:");
						lblTil.setFont(new Font("Tahoma", Font.BOLD, 12));
						GridBagConstraints gbc_lblTil = new GridBagConstraints();
						gbc_lblTil.anchor = GridBagConstraints.WEST;
						gbc_lblTil.insets = new Insets(0, 0, 5, 5);
						gbc_lblTil.gridx = 10;
						gbc_lblTil.gridy = 7;
						getContentPane().add(lblTil, gbc_lblTil);
		
				comboBoxTilLager = new JComboBox<>();
				GridBagConstraints gbc_comboBoxTilLager = new GridBagConstraints();
				gbc_comboBoxTilLager.insets = new Insets(0, 0, 5, 5);
				gbc_comboBoxTilLager.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBoxTilLager.gridx = 11;
				gbc_comboBoxTilLager.gridy = 7;
				getContentPane().add(comboBoxTilLager, gbc_comboBoxTilLager);
		
		JLabel lblMængde = new JLabel("Mængde:");
		lblMængde.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblMængde = new GridBagConstraints();
		gbc_lblMængde.anchor = GridBagConstraints.WEST;
		gbc_lblMængde.insets = new Insets(0, 0, 5, 5);
		gbc_lblMængde.gridx = 10;
		gbc_lblMængde.gridy = 8;
		getContentPane().add(lblMængde, gbc_lblMængde);
		
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
		
		flytMængdeTF = new JTextField();
		GridBagConstraints gbc_flytMængdeTF = new GridBagConstraints();
		gbc_flytMængdeTF.insets = new Insets(0, 0, 5, 5);
		gbc_flytMængdeTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_flytMængdeTF.gridx = 11;
		gbc_flytMængdeTF.gridy = 8;
		getContentPane().add(flytMængdeTF, gbc_flytMængdeTF);
		flytMængdeTF.setColumns(10);
		GridBagConstraints gbc_flytBtn = new GridBagConstraints();
		gbc_flytBtn.anchor = GridBagConstraints.NORTH;
		gbc_flytBtn.insets = new Insets(0, 0, 5, 5);
		gbc_flytBtn.gridx = 11;
		gbc_flytBtn.gridy = 9;
		getContentPane().add(flytBtn, gbc_flytBtn);
		
		JLabel lblNewLabel_1 = new JLabel("I alt på lager:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 15;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 15;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnKassesystem = new JButton("Tilbage");
		btnKassesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		GridBagConstraints gbc_btnKassesystem = new GridBagConstraints();
		gbc_btnKassesystem.anchor = GridBagConstraints.WEST;
		gbc_btnKassesystem.insets = new Insets(0, 0, 5, 5);
		gbc_btnKassesystem.fill = GridBagConstraints.VERTICAL;
		gbc_btnKassesystem.gridx = 1;
		gbc_btnKassesystem.gridy = 14;
		getContentPane().add(btnKassesystem, gbc_btnKassesystem);
	}
}
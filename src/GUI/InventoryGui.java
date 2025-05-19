
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

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
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
	private JComboBox<Product> productBox; 
	private JList<InventoryProduct> inventoryProductList;  
	private DefaultListModel<InventoryProduct> modelInventoryProduct; 
	private JTextField textField;
	private JTextField addStockTF;
	private JTextField removeStockTF;
	private JTextField maxCapacityTF;
	private JScrollPane scrollPane_1;
	private JList<Product> Category;
	private JComboBox<Inventory> comboBoxFraLager;
	private JComboBox<Inventory> comboBoxTilLager;
	private JTextField flytMængdeTF;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InventoryGui dialog = new InventoryGui(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


	/**
	 * Create the dialog.
	 */
	public InventoryGui(KasseSystem kasseSystem, KasseSystemBes kasseSystemBes) {
	
		
		try {
			inventoryCtr = new InventoryCtr();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		modelInventoryProduct = new DefaultListModel<>(); 
		inventoryProductList = new JList<>(modelInventoryProduct); 
		inventoryProductList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		
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

		try {
			findInventoryProduct();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}


	private void loadInventory() throws DataAccessException {
		List<Inventory> in = inventoryCtr.findAll(); 
		
		DefaultComboBoxModel<Inventory> inventory = new DefaultComboBoxModel<Inventory>(); 
		for(int i = 0; i < in.size(); i++) {
			inventory.addElement(in.get(i));
		}
		this.inventoryBox.setModel(inventory);
		comboBoxFraLager.setModel(inventoryBox.getModel());
		comboBoxTilLager.setModel(inventoryBox.getModel());

	}
	
	
	private void loadProducts() throws DataAccessException {
		List<Product> product = inventoryCtr.getProductCtr().findAll(); 
		
		DefaultComboBoxModel<Product> products = new DefaultComboBoxModel<Product>(); 
		for(int i = 0; i < product.size(); i++) {
			products.addElement(product.get(i));
		}
		this.productBox.setModel(products); 
	}

	
	private void findInventoryProduct() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		Product product = (Product) productBox.getSelectedItem(); 	
		
		if (inventory != null && product != null) {
			InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 
			
			modelInventoryProduct.clear(); 
			if (inventoryProduct != null) {
				modelInventoryProduct.addElement(inventoryProduct);
			}
		}
	}
	
	
	public void addStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		Product product = (Product) productBox.getSelectedItem(); 
		
		if (inventory == null || product == null || addStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int quantity = Integer.parseInt(addStockTF.getText()); 
			
			int max = inventoryCtr.getTotalInventoryStock(inventory.getInventoryId()); 
			
			if(quantity + max > inventory.getCapacity()) {
				JOptionPane.showMessageDialog(this, "Du har overskredet maximum væriden for lager", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				inventoryCtr.addStock(inventory, product, quantity); 
				findInventoryProduct();
				updateStockInfo(product, inventory);
			}


		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
		}
		}
		private void flytVare() throws DataAccessException {
			Product produkt = (Product) productBox.getSelectedItem();
			Inventory fra = (Inventory) comboBoxFraLager.getSelectedItem();
			Inventory til = (Inventory) comboBoxTilLager.getSelectedItem();

			if (produkt == null || fra == null || til == null || fra.equals(til) || flytMængdeTF.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vælg forskellige lagre, produkt og mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int mængde = Integer.parseInt(flytMængdeTF.getText());

				int fraBeholdning = inventoryCtr.getInventoryStockForProduct(fra.getInventoryId(), produkt.getProductId());
				int tilBeholdning = inventoryCtr.getTotalInventoryStock(til.getInventoryId());

				if (mængde > fraBeholdning) {
					JOptionPane.showMessageDialog(this, "Der er ikke nok produkter i 'fra'-lageret", "Fejl", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (tilBeholdning + mængde > til.getCapacity()) {
					JOptionPane.showMessageDialog(this, "'Til'-lageret har ikke nok kapacitet", "Fejl", JOptionPane.ERROR_MESSAGE);
					return;
				}

				inventoryCtr.removeStock(fra, produkt, mængde);
				inventoryCtr.addStock(til, produkt, mængde);

				findInventoryProduct(); // opdater visning
				updateStockInfo(produkt, fra);
				updateStockInfo(produkt, til);
				flytMængdeTF.setText("");
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Ugyldig mængde", "Fejl", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	
	
	public void removeStock() throws DataAccessException {
		Inventory inventory = (Inventory) inventoryBox.getSelectedItem(); 
		Product product = (Product) productBox.getSelectedItem(); 
		
		if (inventory == null || product == null || removeStockTF.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vælg produkt og lager, og angiv en gyldig mængde", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			int quantity = Integer.parseInt(removeStockTF.getText()); 
			
			if(quantity > inventoryCtr.getTotalStock(product.getProductId())) {
				JOptionPane.showMessageDialog(this, "Så stor mængde af dette produkt findes slet ikke på lageret!", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				inventoryCtr.removeStock(inventory, product, quantity); 
				findInventoryProduct();
				updateStockInfo(product, inventory);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Ugyldig mængde angivet", "Error", JOptionPane.ERROR_MESSAGE);
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
		setBounds(100, 100, 804, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 25, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 38, 0, 31, 0, 0, 0, 0, 0, 56, 0, 0, 0, 0, 41, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Inventory");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		inventoryBox = new JComboBox<Inventory>();
		inventoryBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					findInventoryProduct();
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
		gbc_inventoryBox.gridx = 3;
		gbc_inventoryBox.gridy = 2;
		getContentPane().add(inventoryBox, gbc_inventoryBox);
		
		maxCapacityTF = new JTextField();
		maxCapacityTF.setEditable(false);
		GridBagConstraints gbc_maxCapacityTF = new GridBagConstraints();
		gbc_maxCapacityTF.insets = new Insets(0, 0, 5, 5);
		gbc_maxCapacityTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxCapacityTF.gridx = 10;
		gbc_maxCapacityTF.gridy = 2;
		getContentPane().add(maxCapacityTF, gbc_maxCapacityTF);
		maxCapacityTF.setColumns(10);
		
		JLabel lblProduktnavn = new JLabel("Produkt navn:");
		GridBagConstraints gbc_lblProduktnavn = new GridBagConstraints();
		gbc_lblProduktnavn.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduktnavn.anchor = GridBagConstraints.EAST;
		gbc_lblProduktnavn.gridx = 1;
		gbc_lblProduktnavn.gridy = 3;
		getContentPane().add(lblProduktnavn, gbc_lblProduktnavn);
		
		productBox = new JComboBox<Product>();
		productBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					findInventoryProduct();
					Product product = (Product) productBox.getSelectedItem(); 
					if (product != null) {
						try {
							int totalStock = inventoryCtr.getTotalStock(product.getProductId());
							textField.setText(String.valueOf(totalStock)); 	
						} catch (DataAccessException e1) {
							e1.printStackTrace();
						}
					}
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				} 
			}
		});
		
		GridBagConstraints gbc_productBox = new GridBagConstraints();
		gbc_productBox.insets = new Insets(0, 0, 5, 5);
		gbc_productBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_productBox.gridx = 2;
		gbc_productBox.gridy = 3;
		getContentPane().add(productBox, gbc_productBox);
		
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
		gbc_addProductBtn.fill = GridBagConstraints.BOTH;
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
		
		Category = new JList<Product>();
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
		
		JLabel lblFra = new JLabel("Flyt fra Lager");
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
		getContentPane().add(comboBoxFraLager, gbc_comboBoxFraLager);;
		
		JLabel lblTil = new JLabel("Flyt ti Lager");
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
					flytVare();
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
		
		JLabel lblNewLabel_2 = new JLabel("Kasse i alt på lager:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 17;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JTextField textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 17;
		getContentPane().add(textField_1, gbc_textField_1);
		
		JButton btnKassesystem = new JButton("Kassesystem");
		btnKassesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add kassesystem logic here
			}
		});
		
		GridBagConstraints gbc_btnKassesystem = new GridBagConstraints();
		gbc_btnKassesystem.insets = new Insets(0, 0, 5, 5);
		gbc_btnKassesystem.fill = GridBagConstraints.BOTH;
		gbc_btnKassesystem.gridx = 2;
		gbc_btnKassesystem.gridy = 15;
		getContentPane().add(btnKassesystem, gbc_btnKassesystem);
	}
}

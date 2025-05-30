package GUI;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import model.BorrowableProduct;


/**
 * 
 * @author knol
 * @version 2018-08-30
 */

public class BorrowableProductsListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	private DefaultListCellRenderer dlcr = new DefaultListCellRenderer();

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
//		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	
		String representation = "Unknown Product"; 
		if (value != null) {
			BorrowableProduct product = (BorrowableProduct) value;
			representation = product.getProductName(); 
		}
		return dlcr.getListCellRendererComponent(list, representation, index, isSelected,
				cellHasFocus);
	
	}
}



package GUI;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import model.BorrowableProduct;
import model.Reservation;


/**
 * 
 * @author knol
 * @version 2018-08-30
 */

public class ReservationListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	private DefaultListCellRenderer dlcr = new DefaultListCellRenderer();

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
//		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	
		String representation = "Unknown Reservation"; 
		if (value != null) {
			Reservation reservation = (Reservation) value;
			representation = String.valueOf(reservation.getCustomer()) + " " + String.valueOf(reservation.getDate() + " (" + String.valueOf(reservation.getReservationId() + ")")); 
		}
		return dlcr.getListCellRendererComponent(list, representation, index, isSelected,
				cellHasFocus);
	
	}
}



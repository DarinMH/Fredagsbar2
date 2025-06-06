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
 super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	

		if (value != null) {
			Reservation reservation = (Reservation) value;
			 String representation = String.format(
		                "<html>" +
		                 "<b>Kunde:</b> %s<br>" +
			 "<b>Produkt:</b> %s<br>" +
		                "<b>Dato:</b> %s<br>" +
		                "<b>ID:</b> %d<br>" +  
		                "<br>" +  
		                "</html>",
		                reservation.getCustomer(),  
		                reservation.getBorrowableProduct(),
		                reservation.getDate(),
		                reservation.getReservationId()
		            );

		            setText(representation);
		}else {
            setText("<html>Unknown Reservation</html>");
        }

        return this;
	
	}
}



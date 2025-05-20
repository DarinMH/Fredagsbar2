package test;

import controller.SaleOrderCtr;
import model.Customer;
import model.SaleOrder;
import model.Staff;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SaleOrderCtrTest {

    @Test
    public void testCreateSaleOrder() throws Exception {
        // Arrange
        SaleOrderCtr saleOrderCtr = new SaleOrderCtr();

        int orderNumber = 1;
        LocalDate date = LocalDate.now();
        boolean status = false;
        Customer customer = new Customer(123, "Test", "Tester", "test@example.com", 100);
        customer.setStudentId(123);
        Staff staff = new Staff("bartender1", "1234", "Benny", "Bar", "Bartender");;
        staff.setUsername("bartender1");
        double totalPrice = 50.0;
        double discountPercentage = 15.0;

        // Act
        SaleOrder order = saleOrderCtr.createSaleOrder(orderNumber, date, status, customer, staff, totalPrice, discountPercentage);

        // Assert
        assertNotNull(order);
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(date, order.getDate());
        assertEquals(status, order.isStatus());
        assertEquals(customer, order.getCustomer());
        assertEquals(staff, order.getStaff());
        assertEquals(totalPrice, order.getTotalPrice(), 0.1);
        assertEquals(discountPercentage, order.getDiscountPercentage());
    }
}
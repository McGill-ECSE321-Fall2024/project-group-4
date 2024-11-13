package ca.mcgill.ecse321.gameshop.dto;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse321.gameshop.model.Customer;

public class CustomerDTOConverter {
    public static List<CustomerDTO> convertToDto(List<Customer> customers) {
        List<CustomerDTO> customerDtoList = new ArrayList<>(customers.size());
        for (Customer customer : customers) {
            customerDtoList.add(new CustomerDTO(customer));
        }
        return customerDtoList;
    }
}
package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Address;
import ca.mcgill.ecse321.gameshop.model.Customer;

import java.io.Serializable;

public record AddressDTO(
        int id,
        String street,
        String city,
        String province,
        String country,
        String postalCode,
        CustomerDTO customer
) implements Serializable {

    public AddressDTO(Address address) {
        this(address.getId(), address.getStreet(), address.getCity(), address.getProvince(), address.getCountry(), address.getPostalCode(), new CustomerDTO(address.getCustomer()));
    }
}

package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Address;

import java.io.Serializable;

public record AddressResponseDTO(
        int id,
        String street,
        String city,
        String province,
        String country,
        String postalCode
) implements Serializable {

    public AddressResponseDTO(Address address) {
        this(address.getId(), address.getStreet(), address.getCity(), address.getProvince(), address.getCountry(), address.getPostalCode());
    }





}
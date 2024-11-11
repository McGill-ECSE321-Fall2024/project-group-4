package ca.mcgill.ecse321.gameshop.dto;

import java.io.Serializable;
import java.util.List;

public record ErrorDTO(List<String> errorMessages) implements Serializable {
}

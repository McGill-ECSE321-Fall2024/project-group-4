package ca.mcgill.ecse321.gameshop.dto;

import java.io.Serializable;

public record ChangePasswordDTO (String oldPassword, String newPassword) implements Serializable {
}

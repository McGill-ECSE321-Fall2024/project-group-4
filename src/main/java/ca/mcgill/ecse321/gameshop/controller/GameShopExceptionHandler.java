package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.ErrorDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GameShopExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException exception){
        return new ResponseEntity<>(new ErrorDTO(List.of(exception.getMessage())), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorDTO> handleEntityExistsException(EntityExistsException exception){
        return new ResponseEntity<>(new ErrorDTO(List.of(exception.getMessage())), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleEntityIllegalArgException(IllegalArgumentException exception){
        return new ResponseEntity<>(new ErrorDTO(List.of(exception.getMessage())), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDTO> handleEntityIllegalStateException(IllegalStateException exception){
        return new ResponseEntity<>(new ErrorDTO(List.of(exception.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

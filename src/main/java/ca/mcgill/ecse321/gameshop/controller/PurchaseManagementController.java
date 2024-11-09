package ca.mcgill.ecse321.gameshop.controller;


import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseManagementController {

    @Autowired
    PurchaseManagementService purchaseManagementService;
}

package restaurant.controller;


import core.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import restaurant.respository.CustomerRepository;
import restaurant.util.RestaurantUtil;

@RestController(value = "customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer){
restaurant.entities.Customer customerEntity = RestaurantUtil.convertCustomerModelToAddressCustomer(customer);
        customerRepository.saveAndFlush(customerEntity);
        return customer;
    }
}

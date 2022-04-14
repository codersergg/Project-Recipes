package recipes.com.codersergg.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.com.codersergg.model.Customer;
import recipes.com.codersergg.repository.CustomerRepository;

import javax.validation.Valid;


@Slf4j
@RestController
@ResponseStatus(code = HttpStatus.OK)
@Data
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;

    @PostMapping(value = "/api/register")
    public Customer register(@Valid @RequestBody Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        log.info("Post /api/register with Customer: " + customer);

        if (customerRepository.existsCustomerByEmailIgnoreCase(customer.getEmail())) {
            log.info("Customer: " + customer + " is already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (!customer.getEmail().matches(regexPattern)) {
            log.info("Email format is incorrect: " + customer.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return customerRepository.save(customer);
    }

}

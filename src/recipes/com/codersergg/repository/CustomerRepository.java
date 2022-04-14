package recipes.com.codersergg.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.com.codersergg.model.Customer;


@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findCustomerByEmailIgnoreCase(String email);

    Boolean existsCustomerByEmailIgnoreCase(String email);
}

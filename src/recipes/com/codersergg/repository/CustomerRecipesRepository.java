package recipes.com.codersergg.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.com.codersergg.model.CustomerRecipes;

@Repository
public interface CustomerRecipesRepository extends CrudRepository<CustomerRecipes, Long> {

    Boolean existsCustomerRecipesByCustomer_IdAndRecipe_Id(long customerId, long recipesId);

    void deleteCustomerRecipesByCustomer_IdAndRecipe_Id(long customerId, long recipesId);

}

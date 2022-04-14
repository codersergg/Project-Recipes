package recipes.com.codersergg.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.com.codersergg.model.BaseEntity;
import recipes.com.codersergg.model.Customer;
import recipes.com.codersergg.model.CustomerRecipes;
import recipes.com.codersergg.model.Recipe;
import recipes.com.codersergg.repository.CustomerRecipesRepository;
import recipes.com.codersergg.repository.CustomerRepository;
import recipes.com.codersergg.repository.RecipeRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@ResponseStatus(code = HttpStatus.OK)
@Data
@Transactional(readOnly = true)
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final CustomerRecipesRepository customerRecipesRepository;
    private final CustomerRepository customerRepository;

    @PostMapping(value = "/api/recipe/new")
    @Transactional
    public BaseEntity addRecipe(@AuthenticationPrincipal UserDetails details,
                                @Valid @RequestBody Recipe recipe) {
        log.info("Post recipe: " + recipe);
        if (recipe.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Recipe newRecipe = new Recipe(
                recipe.getName(),
                recipe.getCategory(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections()
        );
        Recipe save = recipeRepository.save(newRecipe);

        Customer customer = customerRepository.findCustomerByEmailIgnoreCase(details.getUsername());

        CustomerRecipes customerRecipes = new CustomerRecipes();
        customerRecipes.setCustomer(customer);
        customerRecipes.setRecipe(save);
        customerRecipesRepository.save(customerRecipes);

        return new BaseEntity(save.getId());
    }

    @PutMapping(value = "/api/recipe/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRecipe(
            @AuthenticationPrincipal UserDetails details,
            @PathVariable long id,
            @Valid @RequestBody Recipe recipe) {
        log.info("Put recipe: " + recipe + " with id: " + id);

        if (recipe.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Customer customer = checkingCustomer(details, id);

        Recipe newRecipe = new Recipe(
                id,
                recipe.getName(),
                recipe.getCategory(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections()
        );

        CustomerRecipes customerRecipes = new CustomerRecipes();
        customerRecipes.setCustomer(customer);
        customerRecipes.setRecipe(newRecipe);

        customerRecipesRepository.deleteCustomerRecipesByCustomer_IdAndRecipe_Id(customer.getId(), id);
        recipeRepository.save(newRecipe);
        customerRecipesRepository.save(customerRecipes);
    }

    private Customer checkingCustomer(@AuthenticationPrincipal UserDetails details, @PathVariable long id) {
        Customer customer = customerRepository.findCustomerByEmailIgnoreCase(details.getUsername());
        log.info("Customer with email: " + details.getUsername() + " and id: " + customer.getId() + " is found");

        if (!recipeRepository.existsById(id)) {
            log.info("Recipe with id: " + id + " not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!customerRecipesRepository.existsCustomerRecipesByCustomer_IdAndRecipe_Id(customer.getId(), id)) {
            log.info("CustomerRecipes with customer_id: " + customer.getId() + " and recipe_id: " + id + " not found");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return customer;
    }

    @GetMapping(value = "/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        log.info("Get recipe with id: " + id);
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("recipe with id " + id + " is " + recipe);
        return recipe;
    }

    @GetMapping(value = "/api/recipe/search")
    public List<Recipe> getRecipe(@Valid @RequestParam Map<String, String> request) {
        log.info("Get search with parameter: " + request);
        List<Recipe> response;
        if (request.size() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (request.containsKey("name")) {
            response = recipeRepository.findByNameIsContainingIgnoreCaseOrderByDateDesc(request.get("name"));
        } else if (request.containsKey("category")) {
            response = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(request.get("category"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @DeleteMapping(value = "/api/recipe/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(
            @AuthenticationPrincipal UserDetails details,
            @PathVariable long id) {
        log.info("Delete recipe with id: " + id);

        Customer customer = checkingCustomer(details, id);

        customerRecipesRepository.deleteCustomerRecipesByCustomer_IdAndRecipe_Id(customer.getId(), id);
        recipeRepository.deleteById(id);
    }

}
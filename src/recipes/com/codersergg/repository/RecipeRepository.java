package recipes.com.codersergg.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.com.codersergg.model.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameIsContainingIgnoreCaseOrderByDateDesc(String name);

    Recipe findByNameIgnoreCase(String name);

}

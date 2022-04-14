package recipes.com.codersergg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@ToString
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "date")
    @Length(min = 8)
    private String date;

    @Column(name = "description")
    @NotBlank
    private String description;

    @NotEmpty
    @Column(name = "ingredients")
    @ElementCollection
    private List<String> ingredients;

    @NotEmpty
    @Column(name = "directions")
    @ElementCollection
    private List<String> directions;

    public Recipe() {
    }

    public Recipe(String name, String category, String description, List<String> ingredients, List<String> directions) {
        this.name = name;
        this.category = category;
        this.date = LocalDateTime.now().toString();
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }
    public Recipe(long id, String name, String category, String description, List<String> ingredients, List<String> directions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = LocalDateTime.now().toString();
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
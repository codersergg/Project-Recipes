package recipes.com.codersergg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "customerRecipes")
@Data
public class CustomerRecipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerRecipes_id", nullable = false)
    @JsonIgnore
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne
    private Customer customer;

    @JoinColumn(name = "recipes_id", nullable = false)
    @ManyToOne
    private Recipe recipe;

}

package hiberspring.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "branches")
public class Branch extends BaseEntity{

    @Column(unique = true)
    @NotNull
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    @NotNull
    private Town town;

    @OneToMany(mappedBy = "branch")
    private List<Employee> employees;

    @OneToMany(mappedBy = "branch")
    private List<Product> products;
}

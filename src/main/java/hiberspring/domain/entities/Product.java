package hiberspring.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity{

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private int clients;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;
}

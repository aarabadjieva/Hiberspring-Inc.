package hiberspring.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity{

    @Column
    @NotNull
    private String firstName;

    @Column
    @NotNull
    private String lastName;

    @Column
    @NotNull
    private String position;

    @OneToOne(optional = false)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private EmployeeCard card;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;
}

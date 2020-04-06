package hiberspring.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "employee_cards")
public class EmployeeCard extends BaseEntity{

    @Column(unique = true)
    @NotNull
    private String number;

    @OneToOne(mappedBy = "card")
    private Employee employee;
}

package hiberspring.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private int population;

    @OneToMany(mappedBy = "town")
    private List<Branch> branches;
}

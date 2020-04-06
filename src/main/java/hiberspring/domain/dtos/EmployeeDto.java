package hiberspring.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeDto {

    @XmlAttribute(name = "first-name")
    @NotNull
    private String firstName;

    @XmlAttribute(name = "last-name")
    @NotNull
    private String lastName;

    @XmlAttribute
    @NotNull
    private String position;

    @XmlElement
    @NotNull
    private String card;

    @XmlElement
    @NotNull
    private String branch;
}

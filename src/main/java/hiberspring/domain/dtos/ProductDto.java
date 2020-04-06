package hiberspring.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDto {

    @XmlAttribute
    @NotNull
    private String name;

    @XmlAttribute
    @NotNull
    private int clients;

    @XmlElement
    @NotNull
    private String branch;
}

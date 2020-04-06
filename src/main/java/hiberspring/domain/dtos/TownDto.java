package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TownDto {

    @Expose
    private String name;

    @Expose
    private int population;
}

package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CardDto {

    @Expose
    @NotNull
    private String number;
}

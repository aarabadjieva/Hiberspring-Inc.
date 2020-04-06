package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BranchDto {

    @Expose
    @NotNull
    private String name;

    @Expose
    @NotNull
    private String town;
}

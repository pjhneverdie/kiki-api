package corp.pjh.kiki.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ApiResponse<T> {

    @Setter
    private String codeName = "OK";

    private final T value;

    public ApiResponse(T value) {
        this.value = value;
    }

}

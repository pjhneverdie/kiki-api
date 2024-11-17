package corp.pjh.kiki.for_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import corp.pjh.kiki.common.dto.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

@Import({SecurityTestConfig.class})
@Configurable
public class ControllerTestBase {

    @Autowired
    private ObjectMapper objectMapper;

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected <T> ApiResponse<T> createApiResponse(T value) {
        return new ApiResponse<>(value);
    }

}

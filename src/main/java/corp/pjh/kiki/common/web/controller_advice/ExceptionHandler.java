package corp.pjh.kiki.common.web.controller_advice;

import corp.pjh.kiki.common.dto.ExceptionCode;
import corp.pjh.kiki.common.dto.CustomException;
import corp.pjh.kiki.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionHandler {

    @RequiredArgsConstructor
    public enum GlobalExceptionCode implements ExceptionCode {

        INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatusCode.valueOf(500)),

        BAD_REQUEST("BAD_REQUEST", HttpStatusCode.valueOf(400)),

        NOT_FOUND("NOT_FOUND", HttpStatusCode.valueOf(404));

        @Override
        public String codeName() {
            return this.codeName;
        }

        @Override
        public HttpStatusCode httpStatusCode() {
            return this.httpStatusCode;
        }

        private final String codeName;

        private final HttpStatusCode httpStatusCode;

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleServerException() {
        ApiResponse<Void> apiResponse = createApiResponse();

        apiResponse.setCodeName(GlobalExceptionCode.INTERNAL_SERVER_ERROR.codeName());

        return new ResponseEntity<>(apiResponse, GlobalExceptionCode.INTERNAL_SERVER_ERROR.httpStatusCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException() {
        ApiResponse<Void> apiResponse = createApiResponse();

        apiResponse.setCodeName(GlobalExceptionCode.NOT_FOUND.codeName());

        return new ResponseEntity<>(apiResponse, GlobalExceptionCode.NOT_FOUND.httpStatusCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException() {
        ApiResponse<Void> apiResponse = createApiResponse();

        apiResponse.setCodeName(GlobalExceptionCode.BAD_REQUEST.codeName());

        return new ResponseEntity<>(apiResponse, GlobalExceptionCode.BAD_REQUEST.httpStatusCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        ApiResponse<Void> apiResponse = createApiResponse();

        apiResponse.setCodeName(e.getExceptionCode().codeName());

        return new ResponseEntity<>(apiResponse, e.getExceptionCode().httpStatusCode());
    }

    private ApiResponse<Void> createApiResponse() {
        return new ApiResponse<>(null);
    }

}

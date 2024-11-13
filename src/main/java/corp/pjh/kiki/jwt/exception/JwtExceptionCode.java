package corp.pjh.kiki.jwt.exception;

import corp.pjh.kiki.common.dto.ExceptionCode;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
public enum JwtExceptionCode implements ExceptionCode {

    INVALID_TOKEN("INVALID_TOKEN", HttpStatusCode.valueOf(401));

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

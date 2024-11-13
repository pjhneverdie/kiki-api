package corp.pjh.kiki.common.dto;

import org.springframework.http.HttpStatusCode;

/**
 * codeName: 미리 정의한 codeName(프론트에서 codeName에 따라 예외 처리)
 * httpStatusCode: httpStatusCode
 */
public interface ExceptionCode {

    String codeName();

    HttpStatusCode httpStatusCode();

}

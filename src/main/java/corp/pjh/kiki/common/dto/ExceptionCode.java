package corp.pjh.kiki.common.dto;

import org.springframework.http.HttpStatusCode;

public interface ExceptionCode {

    String codeName();

    HttpStatusCode httpStatusCode();

}

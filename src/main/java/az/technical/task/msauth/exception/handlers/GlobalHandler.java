package az.technical.task.msauth.exception.handlers;

import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalHandler {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(WrongDataException.class)
    public ErrorInfo wrongDataExceptionHandler(HttpServletRequest request, WrongDataException exception) {
        String url = request.getRequestURL().toString();
        String message = exception.getLocalizedMessage();

        return ErrorInfo.builder()
                .url(url)
                .message(message)
                .build();
    }
}

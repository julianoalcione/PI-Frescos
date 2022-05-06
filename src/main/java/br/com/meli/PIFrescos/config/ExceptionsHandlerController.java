package br.com.meli.PIFrescos.config;

import br.com.meli.PIFrescos.config.handler.ProductCartException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe ExceptionsHandlerController para tratar mensagens de erro
 * @author Juliano Alcione de Souza
 * Refactor:
 * @author Ana Preis
 * Refactor:
 * @author Julio César Gama
 */

@RestControllerAdvice
public class ExceptionsHandlerController {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorFormsDto> formsError(MethodArgumentNotValidException exception) {
        List<ErrorFormsDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErrorFormsDto erro = new ErrorFormsDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleErrorNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductCartException.class)
    public ResponseEntity<List<ErrorFormsDto>> handleProductError(ProductCartException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ex.getErrorFormsDtoList(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> handleProductError(RuntimeException ex, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MPApiException.class)
    public ResponseEntity<ExceptionDTO> handleProductError(MPApiException ex, HttpServletRequest request) {

        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getApiResponse().getContent())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MPException.class)
    public ResponseEntity<ExceptionDTO> handleProductError(MPException ex, HttpServletRequest request) {

        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleError(Exception ex, HttpServletRequest request) {
        ExceptionDTO exceptionDTO = ExceptionDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}

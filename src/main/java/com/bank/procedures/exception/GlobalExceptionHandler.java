package com.bank.procedures.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(
            DataAccessException exception,
            HttpServletRequest request
    ) {
        Throwable cause = findCause(exception, SQLException.class);

        if (cause instanceof PSQLException postgresException
                && "P0001".equals(postgresException.getSQLState())) {
            return build(
                    HttpStatus.BAD_REQUEST,
                    postgresException.getServerErrorMessage() != null
                            ? postgresException.getServerErrorMessage().getMessage()
                            : postgresException.getMessage(),
                    request
            );
        }

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno ao acessar o banco de dados",
                request
        );
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    private Throwable findCause(Throwable exception, Class<? extends Throwable> type) {
        Throwable current = exception;

        while (current != null) {
            if (type.isInstance(current)) {
                return current;
            }

            current = current.getCause();
        }

        return null;
    }
}

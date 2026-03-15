package com.ecommerce.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(BusinessException.class)
	    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {

		 String correlationId = MDC.get("traceId");
				 
	        ApiError error = new ApiError(
	                HttpStatus.BAD_REQUEST.value(),
	                ex.getMessage(),
	                ex.getErrorCode().name(),
	                correlationId,
	                LocalDateTime.now()
	        );

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	    @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {

	    	String correlationId = MDC.get("traceId");
	    			
	        ApiError error = new ApiError(
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                ex.getMessage(),
	                ErrorCode.INTERNAL_SERVER_ERROR.name(),
	                correlationId,
	                LocalDateTime.now()
	        );

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	    }
	    
	    @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class})
	    public ResponseEntity<ApiError> handleValidationException(
	            org.springframework.web.bind.MethodArgumentNotValidException ex) {

	    	String correlationId = MDC.get("traceId");
	    			
	        String message = ex.getBindingResult()
	                .getFieldErrors()
	                .get(0)
	                .getDefaultMessage();

	        ApiError error = new ApiError(
	                HttpStatus.BAD_REQUEST.value(),
	                message,
	                ErrorCode.VALIDATION_FAILED.name(),
	                correlationId,
	                LocalDateTime.now()
	        );

	        return ResponseEntity.badRequest().body(error);
	    }
}

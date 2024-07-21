package com.Core_Service.ExceptionHandler;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.custom_exceptions.SessionExpiredException;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphQLException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class CommonExceptionHandler {
    @GraphQlExceptionHandler({ConstraintViolationException.class})
    public GraphQLError ConstraintViolationExceptionHandler(ConstraintViolationException ex){
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public GraphQLError SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
        return GraphQLError.newError().message(ex.getLocalizedMessage()).build();
    }

    @GraphQlExceptionHandler(NoUserFoundException.class)
    public GraphQLError NoUserFound(NoUserFoundException ex){
        return GraphQLError.newError().message(ex.getLocalizedMessage()).build();
    }

    @GraphQlExceptionHandler(SessionExpiredException.class)
    public GraphQLError SessionExpiredException(SessionExpiredException ex) {
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(StringIndexOutOfBoundsException.class)
    public GraphQLError StringIndexOutOfBoundsException(StringIndexOutOfBoundsException ex) {
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(BindException.class)
    public GraphQLError BindException(BindException ex) {
        return GraphQLError.newError().message("Something wrong with input please recheck the entered values !!!").build();
    }
}

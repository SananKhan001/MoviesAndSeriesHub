package com.Core_Service.ExceptionHandler;

import com.Core_Service.custom_exceptions.NoUserFound;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphQLException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GraphQlExceptionHandler(NoUserFound.class)
    public GraphQLError NoUserFound(NoUserFound ex){
        return GraphQLError.newError().message(ex.getLocalizedMessage()).build();
    }
}

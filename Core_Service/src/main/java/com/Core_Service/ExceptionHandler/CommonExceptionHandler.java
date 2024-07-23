package com.Core_Service.ExceptionHandler;

import com.Core_Service.custom_exceptions.*;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CommonExceptionHandler {
//    @GraphQlExceptionHandler({ConstraintViolationException.class})
//    public GraphQLError ConstraintViolationExceptionHandler(ConstraintViolationException ex){
//        return GraphQLError.newError().message(ex.getMessage()).build();
//    }

//    @GraphQlExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public GraphQLError SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
//        return GraphQLError.newError().message(ex.getLocalizedMessage()).build();
//    }

    @GraphQlExceptionHandler(NoUserFoundException.class)
    public GraphQLError NoUserFound(NoUserFoundException ex){
        return GraphQLError.newError().message(ex.getLocalizedMessage()).build();
    }

    @GraphQlExceptionHandler(SessionExpiredException.class)
    public GraphQLError SessionExpiredExceptionHandler(SessionExpiredException ex) {
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(StringIndexOutOfBoundsException.class)
    public GraphQLError StringIndexOutOfBoundsExceptionHandler(StringIndexOutOfBoundsException ex) {
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(BindException.class)
    public GraphQLError BindExceptionHandler(BindException ex) {
        return GraphQLError.newError().message("Something wrong with input please recheck the entered values !!!").build();
    }

    @GraphQlExceptionHandler(NoMovieFoundException.class)
    public GraphQLError NoMovieFoundExceptionHandler(NoMovieFoundException ex) {
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(NoEpisodeFoundException.class)
    public GraphQLError NoEpisodeFoundExceptionHandler(NoEpisodeFoundException ex){
        return GraphQLError.newError().message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(NoSeriesFoundException.class)
    public GraphQLError NoSeriesFoundExceptionHandler(NoSeriesFoundException ex){
        return GraphQLError.newError().message(ex.getMessage()).build();
    }
}

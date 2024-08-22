package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.model_response.TransactionResponse;
import com.Core_Service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasAuthority('VIEWER')")
    @QueryMapping(name = "getAllTransactions")
    public List<TransactionResponse> getAllTransactions() throws NoUserFoundException {
        return transactionService.getAllTransactions();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @QueryMapping(name = "getAllTransactionsByUserId")
    public List<TransactionResponse> getAllTransactionsByUserId(@Argument Long userId) throws NoUserFoundException {
        return transactionService.getAllTransactionsByUserId(userId);
    }

}

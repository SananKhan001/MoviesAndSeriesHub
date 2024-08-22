package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.model.Transaction;
import com.Core_Service.model.User;
import com.Core_Service.model.Viewer;
import com.Core_Service.model_response.TransactionResponse;
import com.Core_Service.repository.cache_repository.ViewerServiceCacheRepository;
import com.Core_Service.repository.db_repository.TransactionRepository;
import com.Core_Service.repository.db_repository.UserRepository;
import com.Core_Service.repository.db_repository.ViewerRepository;
import org.commonDTO.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViewerRepository viewerRepository;

    @Autowired
    private ViewerServiceCacheRepository cacheRepository;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private MovieService movieService;
    
    public void save(TransactionDetails transactionDetails) {
        Viewer viewer = null;
        try {
            viewer = userRepository.findById(transactionDetails.getUserId())
                    .orElseThrow(() -> new NoUserFoundException("User not found !!!")).getViewer();
            if(viewer == null) throw new NoUserFoundException("User is not Viewer !!!");
        } catch (NoUserFoundException ex) {
            throw new RuntimeException(ex);
        }

        try {
            if(transactionDetails.getContentType().equals("MOVIE")) {
                movieService.assignMovieToViewer(viewer, transactionDetails.getContentId());
            } else {
                seriesService.assignSeriesToViewer(viewer, transactionDetails.getContentId());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        viewer.setTotalPurchasedAmount(viewer.getTotalPurchasedAmount() + transactionDetails.getPaidAmount());
        viewer = viewerRepository.save(viewer);
        cacheRepository.clearCacheViewerList();

        Transaction transaction = Transaction.builder()
                .transactionId(transactionDetails.getTransactionId())
                .contentId(transactionDetails.getContentId())
                .contentType(transactionDetails.getContentType())
                .contentName(transactionDetails.getContentName())
                .paidAmount(transactionDetails.getPaidAmount())
                .viewer(viewer).build();
        
        transactionRepository.save(transaction);
    }

    public List<TransactionResponse> getAllTransactions() throws NoUserFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Viewer viewer = user.getViewer();
        if(viewer == null) throw new NoUserFoundException("This is not an appropriate user !!!");
        
        return transactionRepository.findAllByViewerId(viewer.getId())
                .stream()
                .map(Transaction::to)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getAllTransactionsByUserId(Long userId) throws NoUserFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFoundException("No user found with given id !!!"));
        Viewer viewer = user.getViewer();
        if(viewer == null) throw new NoUserFoundException("User is not a viewer !!!");

        return transactionRepository.findAllByViewerId(viewer.getId())
                .stream()
                .map(Transaction::to)
                .collect(Collectors.toList());
    }
}

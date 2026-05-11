package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.RecommendationPurchaseRequest;
import com.gamesUP.gamesUP.dto.RecommendationRequest;
import com.gamesUP.gamesUP.dto.RecommendationResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.AvisRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
/**
 * Builds recommendation requests and delegates scoring to the Python API.
 */
public class RecommendationService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final AvisRepository avisRepository;

    @Value("${gamesup.recommendation-api.url}")
    private String recommendationApiUrl;

    /**
     * Creates the service with HTTP and persistence dependencies.
     *
     * @param restTemplate HTTP client used to call the Python API
     * @param userRepository repository for users
     * @param purchaseRepository repository for purchases
     * @param avisRepository repository for reviews
     */
    public RecommendationService(
            RestTemplate restTemplate,
            UserRepository userRepository,
            PurchaseRepository purchaseRepository,
            AvisRepository avisRepository
    ) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.avisRepository = avisRepository;
    }

    /**
     * Builds signals from a stored user and requests recommendations.
     *
     * @param userId user identifier
     * @return recommendations returned by the Python API
     */
    public List<RecommendationResponse> recommendForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        RecommendationRequest request = new RecommendationRequest(userId, buildSignals(userId));
        return recommend(request);
    }

    /**
     * Sends an explicit recommendation request to the Python API.
     *
     * @param request recommendation payload
     * @return recommendations returned by the Python API
     */
    public List<RecommendationResponse> recommend(RecommendationRequest request) {
        return restTemplate.exchange(
                recommendationApiUrl + "/recommendations",
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<List<RecommendationResponse>>() {
                }
        ).getBody();
    }

    private List<RecommendationPurchaseRequest> buildSignals(Long userId) {
        List<RecommendationPurchaseRequest> signals = new ArrayList<>();
        for (Purchase purchase : purchaseRepository.findByUserId(userId)) {
            for (PurchaseLine line : purchase.getLines()) {
                signals.add(new RecommendationPurchaseRequest(line.getGame().getId(), 4.0));
            }
        }
        for (Avis avis : avisRepository.findByUserId(userId)) {
            signals.add(new RecommendationPurchaseRequest(avis.getGame().getId(), avis.getRating()));
        }
        return signals;
    }
}

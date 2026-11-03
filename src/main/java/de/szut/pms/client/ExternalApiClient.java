package de.szut.pms.client;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Component
public class ExternalApiClient {

    private final WebClient webClient;

    public ExternalApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8089")
                .build();
    }

    private String getCurrentToken() {
        var authentication = (JwtAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getToken().getTokenValue();
    }

    /*public String getEmployees() {
        return webClient.get()
                .uri("/employees")
                .headers(h -> h.setBearerAuth(getCurrentToken()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }*/

    public EmployeeDto getEmployeeById(Long id) {
        return webClient.get()
                .uri("/employees/{id}", id)
                .headers(h -> h.setBearerAuth(getCurrentToken()))
                .retrieve()
                .bodyToMono(EmployeeDto.class)
                .block();
    }

    public String getQualification(Long employeeId, Long qualificationId) {
        return webClient.get()
                .uri("/employees/{id}/qualifications/{qid}", employeeId, qualificationId)
                .headers(h -> h.setBearerAuth(getCurrentToken()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<QualificationDto> getAllQualifications() {
        return webClient.get()
                .uri("/qualifications")
                .headers(h -> h.setBearerAuth(getCurrentToken()))
                .retrieve()
                .bodyToFlux(QualificationDto.class)
                .collectList()
                .block();
    }
}

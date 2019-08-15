package com.madkroll.inteview.evsessions.web.summary;

import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring MVC REST controller providing endpoint to show session storage summary.
 * */
@RestController
@Log4j2
public class SummaryController {

    private final SessionStatefulService sessionService;

    public SummaryController(final SessionStatefulService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Provides summary of stored sessions.
     * @return successful response entity containing summary data.
     * */
    @GetMapping(path = "/chargingSessions/summary", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SummaryResponse> summary() {
        final SummaryResponse summary = sessionService.calculateSummary();
        log.info("Last minute summary: {}", summary);
        return ResponseEntity
                .ok()
                .body(summary);
    }
}
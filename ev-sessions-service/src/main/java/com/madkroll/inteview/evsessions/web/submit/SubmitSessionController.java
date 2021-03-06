package com.madkroll.inteview.evsessions.web.submit;

import com.madkroll.inteview.evsessions.service.ChargingSession;
import com.madkroll.inteview.evsessions.service.SessionStatefulService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Spring MVC REST controller providing endpoint to submit new charging session.
 * */
@RestController
@Log4j2
public class SubmitSessionController {

    private final SessionStatefulService sessionService;

    public SubmitSessionController(final SessionStatefulService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Submits new charging session in target station.
     * @param submitSessionRequest object representing request body required to submit new charging session
     * @return successful response entity containing data of submitted charging session.
     * */
    @PostMapping(
            path = "/chargingSessions",
            consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<SubmitSessionResponse> submit(@Valid @RequestBody final SubmitSessionRequest submitSessionRequest) {
        final ChargingSession session = sessionService.submit(submitSessionRequest.getStationId());

        log.info("Submitted new session {} at station {}", session.getId(), session.getStationId());

        return ResponseEntity
                .ok()
                .body(new SubmitSessionResponse(
                        session.getId().toString(),
                        session.getStationId(),
                        session.getUpdatedAt().toString()
                ));
    }
}
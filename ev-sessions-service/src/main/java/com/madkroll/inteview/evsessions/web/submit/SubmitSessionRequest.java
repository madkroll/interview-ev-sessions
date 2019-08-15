package com.madkroll.inteview.evsessions.web.submit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

/**
 * Data object describing all fields available in request submitting new charging session.
 * Defines validation criteria per field basis.
 * */
@Getter
@AllArgsConstructor
public class SubmitSessionRequest {

    /**
     * Station ID to submit new session in.
     * */
    @NotEmpty
    private final String stationId;

}
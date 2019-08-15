package com.madkroll.inteview.evsessions.web.submit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data object describing response with data of submitted session.
 * */
@Getter
@AllArgsConstructor
public class SubmitSessionResponse {

    private final String id;
    private final String stationId;
    private final String updatedAt;
}
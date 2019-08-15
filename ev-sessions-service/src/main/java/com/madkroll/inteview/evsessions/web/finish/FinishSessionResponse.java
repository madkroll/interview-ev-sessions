package com.madkroll.inteview.evsessions.web.finish;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data object describing finishing session operation response.
 * */
@Getter
@AllArgsConstructor
public class FinishSessionResponse {

    private final String id;
    private final String stationId;
    private final String updatedAt;
    private final String status;
}
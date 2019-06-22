package com.madkroll.inteview.evsessions.web.submit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmitSessionResponse {

    private final String id;
    private final String stationId;
    private final String updatedAt;
}
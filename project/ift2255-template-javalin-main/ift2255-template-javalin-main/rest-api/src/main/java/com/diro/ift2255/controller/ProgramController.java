package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.util.HttpClientApi;
import com.diro.ift2255.util.ResponseUtil;
import java.util.Map;

public class ProgramController {

    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1/programs";

    public void getProgramCourses(Context ctx) {
        String programId = ctx.queryParam("programs_list");

        if (programId == null || programId.isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre programs_list est requis."));
            return;
        }

        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append("?programs_list=").append(programId);

            String includeDetail = ctx.queryParam("include_courses_detail");
            if ("true".equalsIgnoreCase(includeDetail)) {
                urlBuilder.append("&include_courses_detail=true");
            }

            var response = new HttpClientApi().get(java.net.URI.create(urlBuilder.toString()));
            ctx.contentType("application/json").result(response.getBody());

        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }
}
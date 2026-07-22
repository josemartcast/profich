package com.josemartcast.profich.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public StatusResponse getStatus() {
        return new StatusResponse("profich-backend", "UP");
    }
}

package com.example.nosqllab1.clientLeads;

import com.example.nosqllab1.riakservices.RiakCounterService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LeadsService {

    private final RiakCounterService riakCounterService;

    public LeadsService(RiakCounterService riakCounterService) {
        this.riakCounterService = riakCounterService;
    }

    public long createLead(){
        try {
            return riakCounterService.generateNextId(LeadsService.class);
        } catch (ExecutionException |InterruptedException e) {
            throw new RuntimeException("Ошибка при создании заявки");
        }
    }

}

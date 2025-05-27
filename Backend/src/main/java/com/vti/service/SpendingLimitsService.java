package com.vti.service;

import com.vti.repository.ISpendingLimitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpendingLimitsService implements ISpendingLimitsService {

    @Autowired
    private ISpendingLimitsRepository spendingLimitsRepository;

}

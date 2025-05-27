package com.vti.service;

import com.vti.entity.MoneySources;
import com.vti.repository.IMoneySourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneySourceService implements IMoneySourceService{

    @Autowired
    private IMoneySourceRepository MoneySourceRepository;

    @Override
    public void updateCurrentBalance(Integer id, Double amount) {
        MoneySources moneySources = MoneySourceRepository.findById(id).get();
        moneySources.setCurrentBalance(moneySources.getCurrentBalance() + amount);
    }
}

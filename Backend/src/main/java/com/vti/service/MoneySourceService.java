package com.vti.service;

import com.vti.entity.MoneySources;
import com.vti.repository.IMoneySourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneySourceService implements IMoneySourceService{

    @Autowired
    private IMoneySourceRepository moneySourceRepository;

    @Override
    public void updateCurrentBalance(Integer id, Double amount) {
        MoneySources moneySources = moneySourceRepository.findById(id).get();
        moneySources.setCurrentBalance(moneySources.getCurrentBalance() + amount);
        moneySourceRepository.save(moneySources);
    }

    @Override
    public MoneySources findById(Integer id) {
        return moneySourceRepository.findById(id).orElse(null);
    }
}

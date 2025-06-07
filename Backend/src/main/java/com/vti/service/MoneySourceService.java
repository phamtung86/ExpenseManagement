package com.vti.service;

import com.vti.dto.MoneySourcesDTO;
import com.vti.entity.MoneySources;
import com.vti.form.MoneySourceForm;
import com.vti.repository.IMoneySourceRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MoneySourceService implements IMoneySourceService {

    @Autowired
    private IMoneySourceRepository moneySourceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ITransactionService transactionService;

    @Override
    public void updateCurrentBalance(Integer id, Double amount) {
        MoneySources moneySources = moneySourceRepository.findById(id).get();
        moneySources.setCurrentBalance(moneySources.getCurrentBalance() + amount);
        moneySourceRepository.save(moneySources);
    }

    @Override
    public List<MoneySourcesDTO> getAllMoneySources(Integer userId) {
        List<MoneySources> moneySources = moneySourceRepository.findByUserId(userId);
        return modelMapper.map(moneySources, new TypeToken<List<MoneySourcesDTO>>() {
        }.getType());
    }

    @Override
    public MoneySources findById(Integer id) {
        return moneySourceRepository.findById(id).orElse(null);
    }

    @Override
    public MoneySources createNewMoneySource(MoneySourceForm createMoneySourceForm) {
        MoneySources moneySources = modelMapper.map(createMoneySourceForm, MoneySources.class);
        moneySources.setId(null);
        moneySources.setCreatedAt(new Date());
        moneySources.setActive(true);
        return moneySourceRepository.save(moneySources);
    }

    @Override
    public boolean updateMoneySource(Integer id, MoneySourceForm updateMoneySourceForm) {
        MoneySources existingMoneySource = findById(id);
        if (existingMoneySource == null) {
            return false;
        }

        double totalExpenses = transactionService.getAllTotalExpensesByMoneySources(id);
        System.out.println(totalExpenses);

        MoneySources updatedMoneySource = modelMapper.map(updateMoneySourceForm, MoneySources.class);

        if (totalExpenses > updateMoneySourceForm.getCurrentBalance()) {
            updatedMoneySource.setCurrentBalance(updateMoneySourceForm.getCurrentBalance() - totalExpenses);
        }

        updatedMoneySource.setId(id);
        updatedMoneySource.setCreatedAt(existingMoneySource.getCreatedAt());
        updatedMoneySource.setUser(existingMoneySource.getUser());
        updatedMoneySource.setActive(updateMoneySourceForm.isActive());
        updatedMoneySource.setUpdateAt(new Date());

//        moneySourceRepository.save(updatedMoneySource);
        return true;
    }


    @Override
    public boolean deleteMoneySource(Integer id) {
        if (!moneySourceRepository.existsById(id)) {
            return false;
        }
        moneySourceRepository.deleteById(id);
        return true;
    }

    @Override
    public double getTotalCurrentBalance(Integer userId) {
        return moneySourceRepository.getTotalCurrentBalance(userId);
    }
}

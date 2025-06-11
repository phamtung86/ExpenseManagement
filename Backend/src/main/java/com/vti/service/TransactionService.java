package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Categories;
import com.vti.entity.MoneySources;
import com.vti.entity.SpendingLimits;
import com.vti.entity.Transactions;
import com.vti.form.CreateTransactionForm;
import com.vti.form.UpdateTransactionForm;
import com.vti.repository.ITransactionRepository;
import com.vti.specification.TransactionSpecificationBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Lazy
    @Autowired
    private IMoneySourceService moneySourceService;
    @Autowired
    private ICategoriesService categoriesService;
    @Autowired
    private ISpendingLimitsService spendingLimitsService;

    public List<TransactionsDTO> filterTransactions(TransactionFilter filter) {
        TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder(filter);

        List<Transactions> entities = transactionRepository.findAll(builder.build());

        // Map từ entity sang DTO
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransactionsDTO toDTO(Transactions entity) {
        return new TransactionsDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getAction().name(),
                entity.getTransactionDate(),
                entity.getUpdateAt(),
                entity.getDescription(),
                new UserDTO(null, entity.getUser().getFullName(), null, null, null, null)
                ,
                entity.getCategories().getId(),
                entity.getCategories().getName(),
                entity.getTransactionTypes().getId().toString(),
                entity.getTransactionTypes().getName(),
                entity.getTransactionTypes().getType().toString(),
                entity.getMoneySources().getId(),
                entity.getMoneySources().getName()
        );
    }

    @Transactional
    @Override
    public Transactions createTransaction(CreateTransactionForm createTransactionForm) {
        Transactions transactions = modelMapper.map(createTransactionForm, Transactions.class);
        transactions.setAction(Transactions.Action.CREATED);

        double amount;

        if ("INCOME".equals(createTransactionForm.getTransactionTypeType())) {
            amount = createTransactionForm.getAmount();
        } else {
            Categories category = categoriesService.findById(createTransactionForm.getCategoriesId());
            if (categoriesService.isParentCategories(createTransactionForm.getCategoriesId())) {
                SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(category.getId(), createTransactionForm.getMoneySourcesId(), createTransactionForm.getUserId());
                if (spendingLimits != null && spendingLimits.isActive()) {
                    double newActual = spendingLimits.getActualSpent() + createTransactionForm.getAmount();
                    spendingLimitsService.updateActualSpent(spendingLimits.getId(), newActual);
                }
            } else {
                SpendingLimits spl = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(category.getParentId(), createTransactionForm.getMoneySourcesId(), createTransactionForm.getUserId());
                if (spl != null && spl.isActive()) {
                    double newActual = spl.getActualSpent() + createTransactionForm.getAmount();
                    spendingLimitsService.updateActualSpent(spl.getId(), newActual);
                }
                SpendingLimits childLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(category.getId(), createTransactionForm.getMoneySourcesId(), createTransactionForm.getUserId());
                if (childLimits != null && childLimits.isActive()) {
                    double childActual = childLimits.getActualSpent() + createTransactionForm.getAmount();
                    childLimits.setActualSpent(childActual);
                }
            }
            amount = -createTransactionForm.getAmount();
        }
        moneySourceService.updateCurrentBalance(createTransactionForm.getMoneySourcesId(), amount);
        return transactionRepository.save(transactions);
    }


    @Transactional
    @Override
    public boolean updateTransaction(Integer transactionID, UpdateTransactionForm updateTransactionForm) {
        Categories categories;
        MoneySources moneySources;
        Transactions transaction = transactionRepository.findById(transactionID).orElse(null);


        if (transaction == null) {
            return false;
        }
        if (!transaction.getCategories().getId().equals(updateTransactionForm.getCategoriesId())) {
            categories = categoriesService.findById(updateTransactionForm.getCategoriesId());
            transaction.setCategories(categories);
        }
        if (!transaction.getMoneySources().getId().equals(updateTransactionForm.getMoneySourcesId())) {
            moneySources = moneySourceService.findById(updateTransactionForm.getMoneySourcesId());
            transaction.setMoneySources(moneySources);
        }
        Double oldAmount = transaction.getAmount();
        transaction.setDescription(updateTransactionForm.getDescription());
        if (!transaction.getTransactionDate().equals(updateTransactionForm.getTransactionDate())) {
            transaction.setTransactionDate(updateTransactionForm.getTransactionDate());
        }
        transaction.setUpdateAt(new Date());
        if (updateTransactionForm.getAmount() != transaction.getAmount()) {
            transaction.setAmount(updateTransactionForm.getAmount());
            double amountUpdate = oldAmount - updateTransactionForm.getAmount();
            moneySourceService.updateCurrentBalance(updateTransactionForm.getMoneySourcesId(), amountUpdate);
            transaction.setAmount(updateTransactionForm.getAmount());
            Categories c = categoriesService.findById(updateTransactionForm.getCategoriesId());
            if (categoriesService.isParentCategories(updateTransactionForm.getCategoriesId())) {
                SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(c.getId(), updateTransactionForm.getMoneySourcesId(), updateTransactionForm.getUserId());
                if (spendingLimits != null) {
                    if (spendingLimits.isActive()) {
                        spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() + amountUpdate);
                    }
                }
            } else {
                SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(c.getParentId(), updateTransactionForm.getMoneySourcesId(), updateTransactionForm.getUserId());
                if (spendingLimits != null) {
                    if (spendingLimits.isActive()) {
                        spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() + amountUpdate);
                    }
                }

                SpendingLimits childLimit = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(c.getId(), updateTransactionForm.getMoneySourcesId(), updateTransactionForm.getUserId());
                if (childLimit != null) {
                    if (childLimit.isActive()) {
                        childLimit.setActualSpent(childLimit.getActualSpent() + amountUpdate);
                    }
                }
            }

        }
        transaction.setAction(Transactions.Action.UPDATED);
        transactionRepository.save(transaction);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteTransaction(List<Integer> transactionID) {
        int count = 0;
        for (Integer i : transactionID) {
            Transactions transaction = transactionRepository.findById(i).orElse(null);
            assert transaction != null;
            MoneySources moneySources = moneySourceService.findById(transaction.getMoneySources().getId());
            moneySourceService.updateCurrentBalance(transaction.getMoneySources().getId(), transaction.getAmount());
            SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(transaction.getCategories().getId(), transaction.getMoneySources().getId(), transaction.getUser().getId());
            if (spendingLimits != null) {
                if (spendingLimits.isActive()) {
                    spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() - transaction.getAmount());
                }
            }
            transactionRepository.deleteById(i);
            count++;
        }

        return count > 0;
    }

    @Override
    public List<TransactionsDTO> getAllTransactions(Integer userID) {
        List<Transactions> transactions = transactionRepository.findAllByUserId(userID);
        List<TransactionsDTO> transactionsDTOS = new ArrayList<>();
        for (Transactions transaction : transactions) {
            transactionsDTOS.add(toDTO(transaction));
        }
        return transactionsDTOS;
    }

    @Override
    public Page<TransactionsDTO> getTransactions(Pageable pageable, Integer userID) {
        Page<Transactions> transactions = transactionRepository.findAllByUserId(pageable, userID);
        List<TransactionsDTO> transactionsDTOS = modelMapper.map(transactions.getContent(), new TypeToken<List<TransactionsDTO>>() {
        }.getType());
        Page<TransactionsDTO> transactionsDTOPage = new PageImpl<>(transactionsDTOS, pageable, transactions.getTotalElements());
        return transactionsDTOPage;
    }

    @Override
    public double getAllTotalExpensesByMoneySources(Integer moneySourceID) {
        LocalDate now = LocalDate.now();
        return transactionRepository.getAllTotalExpensesByMoneySources(moneySourceID, now.getMonthValue());
    }

    @Override
    public double getAllTotalExpensesByTime(String type, Integer userId) {
        LocalDate now = LocalDate.now();
        switch (type.toUpperCase()) {
            case "DAY":
                return transactionRepository.getTotalExpenseByDay(now.getDayOfMonth(), now.getMonthValue(), now.getYear(), userId);
            case "MONTH":
                return transactionRepository.getTotalExpenseByMonth(now.getMonthValue(), now.getYear(), userId);
            case "YEAR":
                return transactionRepository.getTotalExpenseByYear(now.getYear(), userId);
            default:
                throw new IllegalArgumentException("Type phải là: DAY, MONTH, hoặc YEAR");
        }

    }

    @Override
    public double getAllTotalIncomesByTime(String type, Integer userId) {
        LocalDate now = LocalDate.now();
        switch (type.toUpperCase()) {
            case "DAY":
                return transactionRepository.getTotalIncomeByDay(now.getDayOfMonth(), now.getMonthValue(), now.getYear(), userId);
            case "MONTH":
                return transactionRepository.getTotalIncomeByMonth(now.getMonthValue(), now.getYear(), userId);
            case "YEAR":
                return transactionRepository.getTotalIncomeByYear(now.getYear(), userId);
            default:
                throw new IllegalArgumentException("Type phải là: DAY, MONTH, hoặc YEAR");
        }
    }

    @Override
    public List<TransactionsDTO> getRecentTransactions(Integer userID, int limit) {
        List<Transactions> transactions = transactionRepository.findRecentTransactionsByUserId(userID, limit);
        return modelMapper.map(transactions, new TypeToken<List<TransactionsDTO>>() {
        }.getType());
    }

}

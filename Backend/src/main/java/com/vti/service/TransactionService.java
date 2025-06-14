package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Categories;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public Page<TransactionsDTO> filterTransactions(TransactionFilter filter, Integer userId, Pageable pageable) {
        filter.setUserId(userId);
        TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder(filter);
        Page<Transactions> transactionsPage = transactionRepository.findAll(builder.build(), pageable);
        List<TransactionsDTO> transactionsDTOS = modelMapper.map(transactionsPage.getContent(), new TypeToken<List<TransactionsDTO>>() {
        }.getType());
        return new PageImpl<>(transactionsDTOS, pageable, transactionsPage.getTotalElements());
    }

    private TransactionsDTO toDTO(Transactions entity) {
        return new TransactionsDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getAction().name(),
                entity.getTransactionDate(),
                entity.getUpdateAt(),
                entity.getDescription(),
                new UserDTO(null, entity.getUser().getFullName(), null, null, null, null),
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
        Transactions transaction = modelMapper.map(createTransactionForm, Transactions.class);
        transaction.setAction(Transactions.Action.CREATED);
        double amount = calculateTransactionAmount(createTransactionForm);
        moneySourceService.updateCurrentBalance(createTransactionForm.getMoneySourcesId(), amount);
        return transactionRepository.save(transaction);
    }

    private double calculateTransactionAmount(CreateTransactionForm form) {
        if ("INCOME".equals(form.getTransactionTypeType())) {
            return form.getAmount();
        } else {
            updateSpendingLimits(form);
            return -form.getAmount();
        }
    }

    private void updateSpendingLimits(CreateTransactionForm form) {
        Categories category = categoriesService.findById(form.getCategoriesId());
        if (categoriesService.isParentCategories(form.getCategoriesId())) {
            updateSpendingLimitForParentCategory(form, category);
        } else {
            updateSpendingLimitForChildCategory(form, category);
        }
    }

    private void updateSpendingLimitForParentCategory(CreateTransactionForm form, Categories category) {
        SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), form.getMoneySourcesId(), form.getUserId());
        if (spendingLimits != null && spendingLimits.isActive()) {
            double newActual = spendingLimits.getActualSpent() + form.getAmount();
            spendingLimitsService.updateActualSpent(spendingLimits.getId(), newActual);
        }
    }

    private void updateSpendingLimitForChildCategory(CreateTransactionForm form, Categories category) {
        SpendingLimits parentLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getParentId(), form.getMoneySourcesId(), form.getUserId());
        if (parentLimits != null && parentLimits.isActive()) {
            double newActual = parentLimits.getActualSpent() + form.getAmount();
            spendingLimitsService.updateActualSpent(parentLimits.getId(), newActual);
        }
        SpendingLimits childLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), form.getMoneySourcesId(), form.getUserId());
        if (childLimits != null && childLimits.isActive()) {
            double childActual = childLimits.getActualSpent() + form.getAmount();
            childLimits.setActualSpent(childActual);
        }
    }

    @Transactional
    @Override
    public boolean updateTransaction(Integer transactionID, UpdateTransactionForm updateTransactionForm) {
        Transactions transaction = transactionRepository.findById(transactionID).orElse(null);
        if (transaction == null) {
            return false;
        }
        updateTransactionDetails(transaction, updateTransactionForm);
        transaction.setAction(Transactions.Action.UPDATED);
        transactionRepository.save(transaction);
        return true;
    }

    private void updateTransactionDetails(Transactions transaction, UpdateTransactionForm form) {
        if (!transaction.getCategories().getId().equals(form.getCategoriesId())) {
            transaction.setCategories(categoriesService.findById(form.getCategoriesId()));
        }
        if (!transaction.getMoneySources().getId().equals(form.getMoneySourcesId())) {
            transaction.setMoneySources(moneySourceService.findById(form.getMoneySourcesId()));
        }
        transaction.setDescription(form.getDescription());
        if (!transaction.getTransactionDate().equals(form.getTransactionDate())) {
            transaction.setTransactionDate(form.getTransactionDate());
        }
        transaction.setUpdateAt(new Date());
        updateTransactionAmount(transaction, form);
    }

    private void updateTransactionAmount(Transactions transaction, UpdateTransactionForm form) {
        double oldAmount = transaction.getAmount();
        if (form.getAmount() != oldAmount) {
            transaction.setAmount(form.getAmount());
            double amountUpdate = oldAmount - form.getAmount();
            moneySourceService.updateCurrentBalance(form.getMoneySourcesId(), amountUpdate);
            updateSpendingLimitsOnUpdate(form, amountUpdate);
        }
    }

    private void updateSpendingLimitsOnUpdate(UpdateTransactionForm form, double amountUpdate) {
        Categories category = categoriesService.findById(form.getCategoriesId());
        if (categoriesService.isParentCategories(form.getCategoriesId())) {
            updateSpendingLimitForParentCategoryOnUpdate(form, category, amountUpdate);
        } else {
            updateSpendingLimitForChildCategoryOnUpdate(form, category, amountUpdate);
        }
    }

    private void updateSpendingLimitForParentCategoryOnUpdate(UpdateTransactionForm form, Categories category, double amountUpdate) {
        SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), form.getMoneySourcesId(), form.getUserId());
        if (spendingLimits != null && spendingLimits.isActive()) {
            spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() + amountUpdate);
        }
    }

    private void updateSpendingLimitForChildCategoryOnUpdate(UpdateTransactionForm form, Categories category, double amountUpdate) {
        SpendingLimits parentLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getParentId(), form.getMoneySourcesId(), form.getUserId());
        if (parentLimits != null && parentLimits.isActive()) {
            spendingLimitsService.updateActualSpent(parentLimits.getId(), parentLimits.getActualSpent() + amountUpdate);
        }
        SpendingLimits childLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), form.getMoneySourcesId(), form.getUserId());
        if (childLimits != null && childLimits.isActive()) {
            childLimits.setActualSpent(childLimits.getActualSpent() + amountUpdate);
        }
    }

    @Transactional
    @Override
    public boolean deleteTransaction(List<Integer> transactionIDs) {
        int count = 0;
        for (Integer id : transactionIDs) {
            Optional<Transactions> transactionOpt = transactionRepository.findById(id);
            if (transactionOpt.isPresent()) {
                Transactions transaction = transactionOpt.get();
                moneySourceService.updateCurrentBalance(transaction.getMoneySources().getId(), transaction.getAmount());
                updateSpendingLimitsOnDelete(transaction);
                transactionRepository.deleteById(id);
                count++;
            }
        }
        return count > 0;
    }

    private void updateSpendingLimitsOnDelete(Transactions transaction) {
        SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                transaction.getCategories().getId(), transaction.getMoneySources().getId(), transaction.getUser().getId());
        if (spendingLimits != null && spendingLimits.isActive()) {
            spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() - transaction.getAmount());
        }
    }

    private void updateSpendingLimitsForParentCategoryOnUpdate(Transactions transactions) {

    }

    @Override
    public List<TransactionsDTO> getAllTransactions(Integer userID) {
        List<Transactions> transactions = transactionRepository.findAllByUserId(userID);
        return transactions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<TransactionsDTO> getTransactions(Pageable pageable, Integer userID) {
        Page<Transactions> transactions = transactionRepository.findAllByUserId(pageable, userID);
        List<TransactionsDTO> transactionsDTOS = modelMapper.map(transactions.getContent(), new TypeToken<List<TransactionsDTO>>() {
        }.getType());
        return new PageImpl<>(transactionsDTOS, pageable, transactions.getTotalElements());
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
                throw new IllegalArgumentException("Type must be: DAY, MONTH, or YEAR");
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
                throw new IllegalArgumentException("Type must be: DAY, MONTH, or YEAR");
        }
    }

    @Override
    public List<TransactionsDTO> getRecentTransactions(Integer userID, int limit) {
        List<Transactions> transactions = transactionRepository.findRecentTransactionsByUserId(userID, limit);
        return modelMapper.map(transactions, new TypeToken<List<TransactionsDTO>>() {
        }.getType());
    }
}

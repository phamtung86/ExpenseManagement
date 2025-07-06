package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Categories;
import com.vti.entity.SpendingLimits;
import com.vti.entity.Transactions;
import com.vti.form.CreateTransactionForm;
import com.vti.form.UpdateTransactionForm;
import com.vti.models.ReportModel;
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
import java.util.*;
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
                new UserDTO(null, entity.getUser().getFullName(), null, null, null, null, entity.getUser().isNotice()),
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
        double oldAmount = transaction.getAmount();
        double newAmount = form.getAmount();
        Integer oldMoneySourceId = transaction.getMoneySources().getId();
        Integer newMoneySourceId = form.getMoneySourcesId();
        boolean isMoneySourceChanged = !Objects.equals(oldMoneySourceId, newMoneySourceId);
        boolean isAmountChanged = oldAmount != newAmount;

        // Cập nhật danh mục
        if (!transaction.getCategories().getId().equals(form.getCategoriesId())) {
            transaction.setCategories(categoriesService.findById(form.getCategoriesId()));
        }

        // Nếu nguồn tiền thay đổi
        if (isMoneySourceChanged) {
            // Hoàn lại tiền cũ cho nguồn cũ
            moneySourceService.updateCurrentBalance(oldMoneySourceId, oldAmount);
            // Trừ tiền mới khỏi nguồn mới
            moneySourceService.updateCurrentBalance(newMoneySourceId, -newAmount);

            // Cập nhật SpendingLimits tương ứng
            updateSpendingLimitsOnUpdate(buildFormWith(form, oldMoneySourceId), -oldAmount);
            updateSpendingLimitsOnUpdate(form, newAmount);

            // Cập nhật lại source mới
            transaction.setMoneySources(moneySourceService.findById(newMoneySourceId));
        } else if (isAmountChanged) {
            // Nếu chỉ đổi số tiền
            double diff = newAmount - oldAmount;
            moneySourceService.updateCurrentBalance(newMoneySourceId, -diff);
            updateSpendingLimitsOnUpdate(form, diff);
        }

        transaction.setDescription(form.getDescription());
        if (!transaction.getTransactionDate().equals(form.getTransactionDate())) {
            transaction.setTransactionDate(form.getTransactionDate());
        }
        transaction.setUpdateAt(new Date());

        if (isAmountChanged) {
            transaction.setAmount(newAmount);
        }
    }

    private UpdateTransactionForm buildFormWith(UpdateTransactionForm form, Integer moneySourceId) {
        UpdateTransactionForm clone = new UpdateTransactionForm();
        clone.setAmount(form.getAmount());
        clone.setCategoriesId(form.getCategoriesId());
        clone.setUserId(form.getUserId());
        clone.setMoneySourcesId(moneySourceId);
        return clone;
    }

    private void updateTransactionAmount(Transactions transaction, UpdateTransactionForm form) {
        double oldAmount = transaction.getAmount();
//        if (form.getAmount() != oldAmount) {
        double amountUpdate = form.getAmount() - oldAmount;
        transaction.setAmount(form.getAmount());
        moneySourceService.updateCurrentBalance(form.getMoneySourcesId(), -amountUpdate);
        updateSpendingLimitsOnUpdate(form, amountUpdate);
//        }
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
        if (transactionIDs == null || transactionIDs.isEmpty()) {
            return false;
        }
        int count = 0;
        for (Integer id : transactionIDs) {
            if (id == null) {
                continue;
            }
            try {
                boolean exists = transactionRepository.existsById(id);
                if (!exists) {
                    continue;
                }
                Optional<Transactions> transactionOpt = transactionRepository.findById(id);
                if (transactionOpt.isPresent()) {
                    Transactions transaction = transactionOpt.get();
                    Integer moneySourceId = transaction.getMoneySources().getId();
                    Double amount = transaction.getAmount();
                    moneySourceService.updateCurrentBalance(moneySourceId, amount);
                    updateSpendingLimitsOnDelete(transaction, amount);
                    transactionRepository.hardDelete(id);
                    transactionRepository.flush();
                    boolean stillExists = transactionRepository.existsById(id);
                    if (!stillExists) {
                        count++;
                    } else {
                        System.out.println("Xóa thất bại Transaction ID: " + id + " - vẫn tồn tại trong DB");
                    }
                } else {
                    System.out.println("Không tìm thấy Transaction ID: " + id);
                }
            } catch (StackOverflowError e) {
                System.out.println(" StackOverflowError khi xử lý Transaction ID: " + id);
                System.out.println("Có thể do circular reference trong entity relationships");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Lỗi khi xóa Transaction ID: " + id + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return count > 0;
    }

    private void updateSpendingLimitsOnDelete(Transactions transaction, double amountUpdate) {
        Categories category = categoriesService.findById(transaction.getCategories().getId());
        if (categoriesService.isParentCategories(transaction.getCategories().getId())) {
            updateSpendingLimitsForParentCategoryOnDelete(transaction, category, amountUpdate);
        } else {
            updateSpendingLimitForChildCategoryOnDelete(transaction, category, amountUpdate);
        }

    }

    private void updateSpendingLimitsForParentCategoryOnDelete(Transactions transaction, Categories category, double amountUpdate) {
        SpendingLimits spendingLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), transaction.getMoneySources().getId(), transaction.getUser().getId());
        if (spendingLimits != null && spendingLimits.isActive()) {
            spendingLimitsService.updateActualSpent(spendingLimits.getId(), spendingLimits.getActualSpent() - amountUpdate);
        }
    }

    private void updateSpendingLimitForChildCategoryOnDelete(Transactions transaction, Categories category, double amountUpdate) {
        SpendingLimits parentLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getParentId(), transaction.getMoneySources().getId(), transaction.getUser().getId());
        if (parentLimits != null && parentLimits.isActive()) {
            spendingLimitsService.updateActualSpent(parentLimits.getId(), parentLimits.getActualSpent() - amountUpdate);
        }
        SpendingLimits childLimits = spendingLimitsService.findByCategoriesIdAndMoneySourcesIdAndUserId(
                category.getId(), transaction.getMoneySources().getId(), transaction.getUser().getId());
        if (childLimits != null && childLimits.isActive()) {
            childLimits.setActualSpent(childLimits.getActualSpent() - amountUpdate);
        }
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

    @Transactional
    @Override
    public Map<Integer, ReportModel> getReport(Integer userId) {
        LocalDate now = LocalDate.now();
        List<Transactions> transactions = transactionRepository.getReportToday(
                userId, now.getDayOfMonth(), now.getMonthValue(), now.getYear()
        );
        Map<Integer, ReportModel> result = new HashMap<>();

        List<TransactionsDTO> transactionsDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionsDTO>>() {
        }.getType());
        for (TransactionsDTO t : transactionsDTOS) {
            if (result.containsKey(t.getCategoriesId())) {
                ReportModel r = result.get(t.getCategoriesId());
                r.setAmount(r.getAmount() + t.getAmount());
            } else {
                ReportModel reportModel = new ReportModel();
                reportModel.setName(t.getCategoriesName());
                reportModel.setAmount(t.getAmount());
                reportModel.setType(t.getTransactionTypeType());
                result.put(t.getCategoriesId(), reportModel);

            }
        }
        return result.entrySet().stream()
                .filter(entry -> entry.getValue().getAmount() > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }


}

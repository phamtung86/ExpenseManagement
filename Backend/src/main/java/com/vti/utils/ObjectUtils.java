package com.vti.utils;

import com.vti.dto.CategoriesDTO;
import com.vti.dto.TransactionsDTO;
import com.vti.entity.Categories;
import com.vti.entity.MoneySources;
import com.vti.entity.TransactionTypes;
import com.vti.entity.Transactions;
import org.springframework.beans.BeanUtils;

public class ObjectUtils {

    public static TransactionsDTO convertToDto(Transactions transactions) {
        TransactionsDTO transactionsDTO = new TransactionsDTO();
        BeanUtils.copyProperties(transactions, transactionsDTO);
        TransactionTypes transactionTypes = transactions.getTransactionTypes();
        transactionsDTO.setTransactionTypesId(String.valueOf(transactionTypes.getId()));
        transactionsDTO.setTransactionTypesName(transactionTypes.getName());
        transactionsDTO.setTransactionTypeType(transactionTypes.getType().name());
        transactionsDTO.setAction(transactions.getAction().name());


        MoneySources moneySources = transactions.getMoneySources();
        transactionsDTO.setMoneySourcesId(moneySources.getId());
        transactionsDTO.setMoneySourcesName(moneySources.getName());

        Categories categories = transactions.getCategories();
//        transactionsDTO.setCategoriesId(categories.getId());
        transactionsDTO.setCategoriesName(categories.getName());

        return transactionsDTO;
    }

    public static CategoriesDTO convertToDto(Categories categories) {
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        BeanUtils.copyProperties(categories, categoriesDTO);
        TransactionTypes transactionTypes  = categories.getTransactionTypes() ;
        categoriesDTO.setTransactionTypesId(String.valueOf(categories.getTransactionTypes().getId()));
        categoriesDTO.setTransactionTypesName(transactionTypes.getName());
        return categoriesDTO;
    }

//    public static MoneySourcesDTO convertToDto(MoneySources moneySources) {
//        MoneySourcesDTO moneySourcesDTO = new MoneySourcesDTO();
//        BeanUtils.copyProperties(moneySources, moneySourcesDTO);
//        TransactionTypes transactionTypes  = moneySources.getTransactionTypes() ;
//        categoriesDTO.setTransactionTypesId(String.valueOf(categories.getTransactionTypes().getId()));
//        categoriesDTO.setTransactionTypesName(transactionTypes.getName());
//        return categoriesDTO;
//    }
}

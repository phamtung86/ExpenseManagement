package com.vti.dto;

import com.vti.dto.SpendingLimits.SpendingLimitsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO {
    private Integer id;

    private String name;

    private Integer parentId;

    private String icon;

    private String transactionTypesId;

    private String transactionTypesName;

    private String transactionTypeType;

    private List<TransactionsDTO> transactions;

    private List<SpendingLimitsDTO> spendingLimits;

    private List<CategoriesDTO> children;

    private UserDTO user;

//    public CategoriesDTO(Integer id, String name, Integer parentId, String icon,
//                         String transactionTypesId, String transactionTypesName,
//                         List<CategoriesDTO> children, List<TransactionsDTO> transactions) {
//        this.id = id;
//        this.name = name;
//        this.parentId = parentId;
//        this.icon = icon;
//        this.transactionTypesId = transactionTypesId;
//        this.transactionTypesName = transactionTypesName;
//        this.children = children;
//        this.transactions = transactions;
//    }
}


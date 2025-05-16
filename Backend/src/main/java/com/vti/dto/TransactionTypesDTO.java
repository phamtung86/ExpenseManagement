package com.vti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTypesDTO {
    private Integer id;

    private String name;

    private String type;

    private List<CategoriesDTO> categories;

    private List<TransactionsDTO> transactions;

}

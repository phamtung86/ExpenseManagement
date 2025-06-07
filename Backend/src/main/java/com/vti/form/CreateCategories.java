package com.vti.form;

import lombok.Data;

@Data
public class CreateCategories {

    private Integer id;
    private String name;
    private Integer parentId;
    private String icon;
    private Integer transactionTypesId;
    private Integer userId;
}
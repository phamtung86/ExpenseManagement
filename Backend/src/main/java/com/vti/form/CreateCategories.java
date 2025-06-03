package com.vti.form;

import lombok.Data;

@Data
public class CreateCategories {
    private String name;
    private Integer parentId;
    private String icon;
}
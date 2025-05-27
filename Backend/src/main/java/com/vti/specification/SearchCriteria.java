package com.vti.specification;

public class SearchCriteria {
    private String key;       // Field name (e.g., "categories.name")
    private String operation; // Operator: "=", ">=", "<=", "Like"
    private Object value;     // Giá trị so sánh

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    // Getters
    public String getKey() { return key; }
    public String getOperation() { return operation; }
    public Object getValue() { return value; }
}

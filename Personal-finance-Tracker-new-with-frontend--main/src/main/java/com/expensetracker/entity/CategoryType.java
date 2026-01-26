package com.expensetracker.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryType {
	 GROCERIES,
	    ENTERTAINMENT,
	    TRANSPORTATION,
	    SALARY,
	    MEDICAL,  
	    RENT,
	    OTHER;
	    
	
	@JsonCreator
	public static CategoryType fromString(String value) {
	    if (value == null || value.trim().isEmpty()) {
	        throw new IllegalArgumentException("Category type cannot be null or empty");
	    }

	    for (CategoryType type : CategoryType.values()) {
	        if (type.name().equalsIgnoreCase(value)) { // Case insensitive match
	            return type;
	        }
	    }

	    throw new IllegalArgumentException("Invalid category type: " + value);
	}

	    @JsonValue
	    public String toJson() {
	        return this.name();
	    }
}

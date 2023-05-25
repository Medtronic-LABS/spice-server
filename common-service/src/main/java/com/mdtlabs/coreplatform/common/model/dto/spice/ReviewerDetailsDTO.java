package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The ReviewerDetailsDTO class is a data transfer object that contains information about a reviewer's
 * first name, last name, and username.
 * </p>
 */
@Data
public class ReviewerDetailsDTO {

    private String firstName;

    private String lastName;

    private String userName;


    public ReviewerDetailsDTO(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public ReviewerDetailsDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ReviewerDetailsDTO() {
    }


}

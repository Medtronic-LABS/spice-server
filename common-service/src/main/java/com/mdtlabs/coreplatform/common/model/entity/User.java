package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mdtlabs.coreplatform.common.CustomAuthorityDeserializer;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.util.spice.UserInfo;

/**
 * <p>
 * User is a Java class representing a User entity with various fields and relationships to other entities.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_USER)
public class User extends TenantBaseEntity implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = ErrorConstants.FIRST_NAME_NOT_NULL)
    @Column(name = FieldConstants.FIRST_NAME)
    private String firstName;

    @Column(name = FieldConstants.MIDDLE_NAME)
    private String middleName;

    @NotEmpty(message = ErrorConstants.LAST_NAME_NOT_NULL)
    @Column(name = FieldConstants.LAST_NAME)
    private String lastName;

    @Column(name = FieldConstants.GENDER)
    private String gender;

    @NotEmpty(message = ErrorConstants.PHONE_NUMBER_NOT_NULL)
    @Column(name = FieldConstants.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = FieldConstants.ADDRESS)
    private String address;

    @NotEmpty(message = ErrorConstants.EMAIL_NOT_NULL)
    @Column(name = FieldConstants.USERNAME)
    private String username;

    @ColumnTransformer(forColumn = FieldConstants.PASSWORD, read = "public.pgp_sym_decrypt(password::bytea, " + "'"
            + FieldConstants.SPICE_CORE + "'"
            + ")", write = "public.pgp_sym_encrypt(?, " + "'" + FieldConstants.SPICE_CORE + "'" + ")")
    @Column(name = FieldConstants.PASSWORD, columnDefinition = "bytea", nullable = false)
    private String password;

    @Column(name = FieldConstants.COUNTRY_CODE)
    private String countryCode;

    @Column(name = FieldConstants.IS_BLOCKED)
    private Boolean isBlocked = false;

    @Column(name = FieldConstants.BLOCKED_DATE, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockedDate;

    @Column(name = FieldConstants.FORGET_PASSWORD_TOKEN)
    private String forgetPasswordToken;

    @Column(name = FieldConstants.FORGET_PASSWORD_TIME, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date forgetPasswordTime;

    @Column(name = FieldConstants.FORGET_PASSWORD_COUNT)
    private int forgetPasswordCount;

    @Column(name = FieldConstants.INVALID_LOGIN_TIME, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidLoginTime;

    @Column(name = FieldConstants.INVALID_LOGIN_ATTEMPTS)
    private int invalidLoginAttempts;

    @Column(name = FieldConstants.INVALID_RESET_TIME, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidResetTime;

    @Column(name = FieldConstants.IS_PASSWORD_RESET_ENABLED)
    private Boolean isPasswordResetEnabled = false;

    @Column(name = FieldConstants.PASSWORD_RESET_ATTEMPTS)
    private int passwordResetAttempts;

    @Column(name = FieldConstants.IS_LICENSE_ACCEPTANCE)
    private boolean isLicenseAcceptance = false;

    @Column(name = FieldConstants.LAST_LOGGED_IN)
    private Date lastLoggedIn;

    @Column(name = FieldConstants.LAST_LOGGED_OUT)
    private Date lastLoggedOut;

    @Column(name = FieldConstants.CULTURE_ID)
    private Long cultureId;

    @ManyToOne
    @JoinColumn(name = FieldConstants.COUNTRY_ID)
    private Country country;

    @NotEmpty(message = ErrorConstants.TIMEZONE_NOT_NULL, groups = {UserInfo.class})
    @ManyToOne
    @JoinColumn(name = FieldConstants.TIMEZONE_ID)
    private Timezone timezone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = TableConstants.TABLE_USER_ROLE, joinColumns = {
            @JoinColumn(name = FieldConstants.USER_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.ROLE_ID)})
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = TableConstants.TABLE_USER_ORGANIZATION, joinColumns = {
            @JoinColumn(name = FieldConstants.USER_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.ORGANIZATION_ID)})
    private Set<Organization> organizations = new HashSet<>();

    public User() {

    }

    public User(Long id) {
        super(id);
    }

    /**
     * <p>
     * This method is used to set for the "password" field.
     * </p>
     *
     * @param password {@link String} The password to set is given
     */
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <p>
     * This function sets the active status of an object to a specified boolean value.
     * </p>
     *
     * @param isActive A boolean parameter that indicates whether the component should be enabled or disabled is given
     */
    public void setEnabled(boolean isActive) {
        super.setActive(isActive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return super.isActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.addAll(roles);
        return authorities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }
}

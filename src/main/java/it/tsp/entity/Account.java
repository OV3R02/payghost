package it.tsp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


@NamedQueries({
    @NamedQuery(name = Account.FIND_ALL, query = "select e from Account e")
})

@Entity
@Table (name = "account")
public class Account extends BaseEntity implements Serializable {

    public static final String FIND_ALL = "Account.findAll";

    @JsonbProperty("first-name")
    private String fname;
    @JsonbProperty("last-name")
    private String lname;
    
    @NotBlank
    @Size(min = 4, message = "The password is blank or less than four letters, please set it right!")
    @Column(nullable = false, unique = false)
    private String pwd;

    @NotBlank
    @Email(message = "The email adress is not valid!")
    @Column(nullable = false, unique = true)
    private String email;

    @Transient
    private String confirmPwd;

    @PositiveOrZero(message = "The credit should be more or equals to 0.")
    @Column(precision = 6, scale = 2)
    private BigDecimal credit;

    public Account(){}

    public Account(String pwd, String email) {
        this.pwd = pwd;
        this.email = email;
    }

    public Account(String fname, String lname,
            @NotBlank @Size(min = 4, message = "The password is blank or less than four letters, please set it right!") String pwd,
            @Email(message = "The email adress is not valid!") @NotBlank String email) {
        this.fname = fname;
        this.lname = lname;
        this.pwd = pwd;
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFullName(){
        return fname+" "+lname;
    }

    @JsonbTransient
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
    

    

    @Override
    public String toString() {
        return "Account [ID ="+ID+", fname=" + fname + ", lname=" + lname + ", pwd=" + pwd + ", email=" + email
                + ", credit=" + credit + "]";
    }

    public void increaseCredit(BigDecimal amount) {
        this.credit = this.credit.add(amount);
    }

    public void decreaseCredit(BigDecimal amount) {
        this.credit = this.credit.subtract(amount);
    }

    public boolean hasSufficientCredit(BigDecimal amount) {
        return this.credit.compareTo(amount) > 0;
    }
    


}

package it.tsp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "recharge")

public class Recharge extends BaseEntity implements Serializable{
    
    @ManyToOne(optional = false)
    private Account account;

    @Positive(message = "The amount value should be major than 0 to work properly.")
    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal amount;

    private LocalDate performedOn = LocalDate.now();

    public Recharge() {
    }

    public Recharge(Account account,
            @Positive(message = "The amount value should be major than 0 to work properly.") BigDecimal amount) {
        this.account = account;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPerformedOn() {
        return performedOn;
    }

    public void setPerformedOn(LocalDate performedOn) {
        this.performedOn = performedOn;
    }

    

    

    

    


}

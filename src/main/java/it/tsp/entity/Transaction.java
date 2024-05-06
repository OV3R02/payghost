package it.tsp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")

public class Transaction extends BaseEntity implements Serializable {

    public Transaction() {
    }

    public Transaction(Account senderAccount, Account receiverAccount, BigDecimal amount) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    @ManyToOne(optional = false)
    private Account senderAccount;

    @ManyToOne(optional = false)
    private Account receiverAccount;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal amount;

    private LocalDate sendingAT = LocalDate.now();

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getSendingAT() {
        return sendingAT;
    }

    public void setSendingAT(LocalDate sendingAT) {
        this.sendingAT = sendingAT;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((senderAccount == null) ? 0 : senderAccount.hashCode());
        result = prime * result + ((receiverAccount == null) ? 0 : receiverAccount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (senderAccount == null) {
            if (other.senderAccount != null)
                return false;
        } else if (!senderAccount.equals(other.senderAccount))
            return false;
        if (receiverAccount == null) {
            if (other.receiverAccount != null)
                return false;
        } else if (!receiverAccount.equals(other.receiverAccount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaction [senderAccount=" + senderAccount + ", receiverAccount=" + receiverAccount + ", amount="
                + amount + ", sendingAT=" + sendingAT + "]";
    }

}

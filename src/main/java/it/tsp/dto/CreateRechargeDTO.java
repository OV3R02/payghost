package it.tsp.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRechargeDTO(@NotNull @Positive  BigDecimal amounth) {
    
}

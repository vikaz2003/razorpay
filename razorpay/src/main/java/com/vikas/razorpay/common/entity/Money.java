package com.vikas.razorpay.common.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class Money {

    private int amountUnits;

    private String currency;

    private Money of(int amountUnits,String currency){
        return new Money(amountUnits,currency);
    }

    private Money inr(int amountUnits){
        return new Money(amountUnits,"INR");
    }


    public Money add(Money other){
        if(!this.currency.equals(other.currency)){
           throw new IllegalArgumentException("Cannot add Money with different currencies");
        }

        return new Money(this.amountUnits+ other.amountUnits,this.currency);
    }
}

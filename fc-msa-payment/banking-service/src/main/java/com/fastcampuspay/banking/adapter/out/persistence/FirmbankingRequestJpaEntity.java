package com.fastcampuspay.banking.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "request_firmbanking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirmbankingRequestJpaEntity {
    @Id
    @GeneratedValue
    private Long requestFirmbankingId;

    private String fromBankName;

    private  String fromBankAccountNumber;

    private  String toBankName;

    private  String toBankAccountNumber;

    private int moneyAmount; // only won

    private int firmbankingStatus; // 0: 요청, 1: 완료, 2: 실패

    private String uuid;

    private String aggregateIdentifier;

    public FirmbankingRequestJpaEntity(String fromBankName, String fromBankAccountNumber, String toBankName, String toBankAccountNumber, int moneyAmount, int firmbankingStatus, UUID uuid, String aggregateIdentifier) {
        this.fromBankName = fromBankName;
        this.fromBankAccountNumber = fromBankAccountNumber;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.moneyAmount = moneyAmount;
        this.firmbankingStatus = firmbankingStatus;
        this.uuid = uuid.toString();
        this.aggregateIdentifier = aggregateIdentifier;
    }

    @Override
    public String toString() {
        return "FirmbankingRequestJpaEntity{" +
                "requestFirmbankingId=" + requestFirmbankingId +
                ", fromBankName='" + fromBankName + '\'' +
                ", fromBankAccountNumber='" + fromBankAccountNumber + '\'' +
                ", toBankName='" + toBankName + '\'' +
                ", toBankAccountNumber='" + toBankAccountNumber + '\'' +
                ", moneyAmount=" + moneyAmount +
                ", firmbankingStatus=" + firmbankingStatus +
                ", uuid='" + uuid + '\'' +
                ", aggregateIdentifier='" + aggregateIdentifier + '\'' +
                '}';
    }
}

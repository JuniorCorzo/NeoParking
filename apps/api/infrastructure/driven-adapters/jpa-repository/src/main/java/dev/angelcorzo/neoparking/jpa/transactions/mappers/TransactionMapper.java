package dev.angelcorzo.neoparking.jpa.transactions.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.mappers.JacksonConverter;
import dev.angelcorzo.neoparking.jpa.payments.mapper.PaymentMapper;
import dev.angelcorzo.neoparking.jpa.transactions.TransactionsData;
import dev.angelcorzo.neoparking.model.transactions.Transactions;
import org.mapstruct.Mapper;

@Mapper(
    config = MapperStructConfig.class,
    uses = {JacksonConverter.class, PaymentMapper.class })
public interface TransactionMapper extends BaseMapper<Transactions, TransactionsData> {}

package com.selfxdsd.core.contracts.invoices;

import com.selfxdsd.api.Contract;
import com.selfxdsd.api.Invoice;
import com.selfxdsd.api.storage.Storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A Invoice stored in self.
 * @author criske
 * @version $Id$
 * @since 0.0.3
 */
public final class StoredInvoice implements Invoice {

    /**
     * Invoice id.
     */
    private final int id;
    /**
     * Self storage context.
     */
    private final Storage storage;

    /**
     * Contract's id of this invoice.
     */
    private final Contract.Id contractId;

    /**
     * Time when this Invoice has been paid.
     */
    private final LocalDateTime paymentTime;

    /**
     * The payment's transaction ID.
     */
    private final String transactionId;

    /**
     * Ctor.
     * @param id Invoice id.
     * @param storage Self storage context.
     * @param contractId Contract's id of this invoice
     */
    public StoredInvoice(
        final int id,
        final Storage storage,
        final Contract.Id contractId
    ) {
        this(id, storage, contractId, null, null);
    }

    /**
     * Ctor.
     * @param id Invoice id.
     * @param storage Self storage context.
     * @param contractId Contract's id of this invoice
     * @param paymentTime Time when this Invoice has been paid.
     * @param transactionId The payment's transaction ID.
     */
    public StoredInvoice(
        final int id,
        final Storage storage,
        final Contract.Id contractId,
        final LocalDateTime paymentTime,
        final String transactionId
    ) {
        this.id = id;
        this.storage = storage;
        this.contractId = contractId;
        this.paymentTime = paymentTime;
        this.transactionId = transactionId;
    }

    @Override
    public int invoiceId() {
        return this.id;
    }

    @Override
    public Contract.Id contractId() {
        return this.contractId;
    }

    @Override
    public boolean isPaid() {
        return this.paymentTime != null && this.transactionId != null;
    }

    @Override
    public BigDecimal totalAmount() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof Invoice
            && this.id == ((Invoice) obj).invoiceId());
    }
}
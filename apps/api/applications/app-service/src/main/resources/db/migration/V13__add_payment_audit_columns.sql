ALTER TABLE "neo-parking_db".neo_parking.payments
    ADD COLUMN provider            VARCHAR(150),
    ADD COLUMN external_payment_id VARCHAR(100),
    ADD COLUMN checkout_session_id VARCHAR(100),
    ADD COLUMN checkout_url        TEXT,
    ADD COLUMN checkout_expires_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN completed_at        TIMESTAMP WITH TIME ZONE;

ALTER TABLE "neo-parking_db".neo_parking.payments
    RENAME COLUMN provider_metadata TO provider_create_response;

CREATE UNIQUE INDEX ux_payment_one_completed_per_ticket
    ON "neo-parking_db".neo_parking.payments (tenant_id, parking_ticket_id)
    WHERE status = 'PAID';

CREATE INDEX idx_payment_status
    ON "neo-parking_db".neo_parking.payments (status);

CREATE INDEX idx_checkout_session_id
    ON "neo-parking_db".neo_parking.payments (checkout_session_id)
    WHERE checkout_session_id IS NOT NULL;

CREATE INDEX idx_checkout_expires_at
    ON "neo-parking_db".neo_parking.payments (checkout_expires_at)
    WHERE status = 'PENDING_PAYMENT';


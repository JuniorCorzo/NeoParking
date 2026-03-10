ALTER TABLE "neo-parking_db".neo_parking.payments
    ALTER COLUMN status
        SET DEFAULT 'PENDING';


ALTER TABLE "neo-parking_db".neo_parking.payments
    DROP CONSTRAINT payments_status_check;


ALTER TABLE "neo-parking_db".neo_parking.payments
    DROP CONSTRAINT payments_payment_method_check;


ALTER TABLE "neo-parking_db".neo_parking.payments
    ADD CONSTRAINT payments_status_check CHECK (status in ('PENDING_CHECKOUT',
                                                           'PENDING_PAYMENT',
                                                           'PAID',
                                                           'FAILED',
                                                           'EXPIRED',
                                                           'CANCELLED',
                                                           'REFUNDED'));


ALTER TABLE "neo-parking_db".neo_parking.payments
    ADD CONSTRAINT payment_method_check CHECK (payment_method in ('PAY_LINK',
                                                                  'EFFECTIVE'));
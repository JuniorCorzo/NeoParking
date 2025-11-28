CREATE TABLE slots (
    id UUID PRIMARY KEY,
    parking_lot_id UUID NOT NULL REFERENCES parking_lots(id),
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    slot_number VARCHAR(20) NOT NULL,
    type VARCHAR(50) DEFAULT 'car', -- car, motorcycle, ev, disabled
    zone VARCHAR(50),
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    UNIQUE (slot_number)
);

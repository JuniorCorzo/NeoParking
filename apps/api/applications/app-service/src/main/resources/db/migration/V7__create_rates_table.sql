CREATE TABLE IF NOT EXISTS rates (
    id UUID PRIMARY KEY,
    parking_lot_id UUID NOT NULL REFERENCES parking_lots(id),
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    name TEXT NOT NULL,
    description VARCHAR(255) NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    time_unit VARCHAR(20) NOT NULL CHECK (time_unit IN ('MINUTES', 'HOURS', 'DAYS')),
    min_charge_time_minutes INTEGER DEFAULT 0,
    vehicle_type VARCHAR(50) NOT NULL CHECK(vehicle_type IN ('CART', 'MOTORCYCLE')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);
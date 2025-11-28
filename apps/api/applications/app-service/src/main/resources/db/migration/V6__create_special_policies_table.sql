CREATE TABLE IF NOT EXISTS special_policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    name TEXT NOT NULL,
    modifies VARCHAR(10) CHECK (modifies IN ('PRICE', 'TIME', 'DISCOUNT', 'SURCHARGE')),
    operation VARCHAR(11) CHECK (operation IN ( 'SUBTRACT', 'PERCENTAGE', 'SET')),
    value_to_modify NUMERIC(10,2) CHECK(value_to_modify >= 0),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_percentage CHECK (operation = 'PERCENTAGE' AND (value_to_modify > 0 AND value_to_modify <= 100) )
);

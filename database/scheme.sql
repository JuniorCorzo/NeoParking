CREATE SCHEMA IF NOT EXISTS neo_parking; 
SET search_path TO neo_parking;



-- Tabla para gestionar los inquilinos (propietarios de parqueaderos)
CREATE TABLE IF NOT EXISTS tenants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de usuarios con roles y referencia al inquilino
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('SUPERADMIN', 'OWNER', 'MANAGER', 'OPERATOR', 'DRIVER', 'AUDITOR')),
    tenant_id INTEGER REFERENCES tenants(id) ON DELETE SET NULL, -- Un usuario puede no pertenecer a un inquilino (ej. Superadmin, Driver)
    contact_info TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'operating_hours_t') THEN
        CREATE TYPE operating_hours_t AS (
            open_time TIME,
            close_time TIME
        );
    END IF;
END $$;

-- Tabla de parqueaderos
CREATE TABLE IF NOT EXISTS parking_lots (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    total_spots INTEGER NOT NULL,
    owner_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    timezone VARCHAR(50) DEFAULT 'UTC',
    currency VARCHAR(10) DEFAULT 'COP',
    operating_hours operating_hours_t
);

-- Tabla de plazas (slots) de cada parqueadero
CREATE TABLE slots (
    id SERIAL PRIMARY KEY,
    parking_lot_id INTEGER NOT NULL REFERENCES parking_lots(id) ON DELETE CASCADE,
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    slot_number VARCHAR(20) NOT NULL,
    type VARCHAR(50) DEFAULT 'car', -- car, motorcycle, ev, disabled
    zone VARCHAR(50),
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE')),
    UNIQUE (parking_lot_id, slot_number)
);

--- type fer special policies
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'special_policies_t') THEN
        CREATE TYPE special_policies_t AS (
            name TEXT,
            modifies VARCHAR(6),
            operation VARCHAR(11),
            value_to_modify INTEGER
        );
    END IF;
END $$;

-- Tabla de tarifas
CREATE TABLE IF NOT EXISTS rates (
    id SERIAL PRIMARY KEY,
    parking_lot_id INTEGER NOT NULL REFERENCES parking_lots(id) ON DELETE CASCADE,
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    description VARCHAR(255) NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    time_unit VARCHAR(20) NOT NULL CHECK (time_unit IN ('MINUTE', 'HOUR', 'DAY')),
    min_charge_time_minutes INTEGER DEFAULT 0,
    vehicle_type VARCHAR(50) NOT NULL CHECK(vehicle_type IN ('CART', 'MOTORCYCLE')),
    special_policies special_policies_t
);

-- Tabla de reservas
CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    slot_id INTEGER NOT NULL REFERENCES slots(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED')),
    payment_method VARCHAR(50),
    reservation_code VARCHAR(50) UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tickets de estacionamiento
CREATE TABLE IF NOT EXISTS parking_tickets (
    id SERIAL PRIMARY KEY,
    slot_id INTEGER NOT NULL REFERENCES slots(id) ON DELETE CASCADE,
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
    license_plate VARCHAR(20),
    entry_time TIMESTAMP WITH TIME ZONE NOT NULL,
    exit_time TIMESTAMP WITH TIME ZONE,
    reservation_id INTEGER REFERENCES reservations(id) ON DELETE SET NULL,
    rate_id INTEGER REFERENCES rates(id) ON DELETE SET NULL,
    total_to_charge DECIMAL(10, 2),
    status VARCHAR(20) DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'CLOSED', 'LOST')),
    payment_method VARCHAR(50),
    transaction_reference VARCHAR(255)
);

-- Tabla de pagos/transacciones
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    ticket_id INTEGER REFERENCES parking_tickets(id) ON DELETE CASCADE,
    reservation_id INTEGER REFERENCES reservations(id) ON DELETE SET NULL, -- Para pagos de reserva
    tenant_id INTEGER NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL CHECK(payment_method IN ('CARD', 'EFFECTIVE')), 
    status VARCHAR(20) DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    provider_details TEXT -- Ej. ID de transacción de Stripe/PayPal
);
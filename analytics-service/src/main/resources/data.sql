CREATE TABLE IF NOT EXISTS registration_stats (
    id UUID PRIMARY KEY,
    role VARCHAR(14) NOT NULL,
    total_count INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS daily_meeting_stats (
    id UUID PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    booked_count INTEGER NOT NULL DEFAULT (0),
    completed_count INTEGER NOT NULL DEFAULT (0),
    cancelled_count INTEGER NOT NULL DEFAULT (0)
);

CREATE TABLE IF NOT EXISTS doctor_meeting_stats(
    id UUID PRIMARY KEY,
    doctor_id UUID NOT NULL UNIQUE,
    booked_count INTEGER NOT NULL DEFAULT (0),
    completed_count INTEGER NOT NULL DEFAULT (0),
    cancelled_count INTEGER NOT NULL DEFAULT (0)
);
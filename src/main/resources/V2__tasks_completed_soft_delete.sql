-- Ajuste de status padrão: ACTIVE
ALTER TABLE tasks
  ALTER COLUMN status TYPE VARCHAR(20),
  ALTER COLUMN status SET DEFAULT 'ACTIVE';

-- Se tiver registros com status nulo ou 'PENDING', ajustar:
UPDATE tasks SET status = 'ACTIVE' WHERE status IS NULL OR status = 'PENDING';

-- Índices (se não existirem ainda)
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_tasks_completed_at ON tasks(completed_at);
CREATE INDEX IF NOT EXISTS idx_tasks_deleted_at ON tasks(deleted_at);

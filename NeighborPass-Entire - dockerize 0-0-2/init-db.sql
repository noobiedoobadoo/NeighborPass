-- Create databases if they don't exist
CREATE DATABASE IF NOT EXISTS security_directory;
CREATE DATABASE IF NOT EXISTS gate_log_directory;

-- Grant all privileges to root user (no password reset)
GRANT ALL PRIVILEGES ON security_directory.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON gate_log_directory.* TO 'root'@'%';
FLUSH PRIVILEGES;

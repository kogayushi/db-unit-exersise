package test_utils;

public enum DbMode {
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    ORACLE("Oracle"),
    NONE(null);
    
    private String mode;
    
    private DbMode(String mode) {
        this.mode = mode;
    }
    
    String getMode() {
        return this.mode;
    }
    
    static DbMode modeOf(String mode) {
        if(mode == null) {
            return NONE;
        }

        for(DbMode dbMode : values()) {
            if(dbMode.getMode().toUpperCase().equals(mode.toUpperCase())) {
                return dbMode;
            }
        }
        throw new IllegalArgumentException("no such as mode : " + mode);
    }

}

package hallytech.phoenix.util;

public interface Constant {
    /**
     * STATE FOR ALL ENTITIES
     * */
    enum State {ACTIVE, INACTIVE, DELETE}
    /**
     * USER
     * */
    enum TypeUser {ADMIN, ADMIN_COMPANY, SUPERVISOR, SUPPORT, INSPECTOR, CLIENT}
    /**
     * INSPECTION
     * */
    enum PriorityInspection {HIGH, NORMAL, LOW}
    enum TypeInspection {SCL, TEMPERATURE, CORRECTIVE, GEAR, AIR}
    enum StatusInspection {PROCESS, OBSERVED, FINALIZED}
}

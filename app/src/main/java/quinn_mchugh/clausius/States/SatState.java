package quinn_mchugh.clausius.States;

/**
 * Created by Quinn McHugh on 10/20/2018.
 */
public class SatState {
    private double v_f;     // [m^3/kg]     Specific volume of the saturated liquid
    private double v_g;     // [m^3/kg]     Specific volume of the saturated vapor
    private double u_f;     // [kJ/kg]      Internal energy of the saturated liquid
    private double u_g;     // [kJ/kg]      Internal energy of the saturated vapor
    private double h_f;     // [kJ/kg]      Enthalpy of the saturated liquid
    private double h_g;     // [kJ/kg]      Enthalpy of the saturated vapor
    private double s_f;     // [kJ/kg/K]    Entropy of the saturated liquid
    private double s_g;     // [kJ/kg/K]    Entropy of the saturated vapor

    private double T;   // [°C] Temperature
    private double P;   // [kPa] Pressure

    /**
     * Required public constructor.
     */
    public SatState() {

    }

    public double getV_f() {
        return v_f;
    }

    public void setV_f(double v_f) {
        this.v_f = v_f;
    }

    public double getV_g() {
        return v_g;
    }

    public void setV_g(double v_g) {
        this.v_g = v_g;
    }

    public double getU_f() {
        return u_f;
    }

    public void setU_f(double u_f) {
        this.u_f = u_f;
    }

    public double getU_g() {
        return u_g;
    }

    public void setU_g(double u_g) {
        this.u_g = u_g;
    }

    public double getH_f() {
        return h_f;
    }

    public void setH_f(double h_f) {
        this.h_f = h_f;
    }

    public double getH_g() {
        return h_g;
    }

    public void setH_g(double h_g) {
        this.h_g = h_g;
    }

    public double getS_f() {
        return s_f;
    }

    public void setS_f(double s_f) {
        this.s_f = s_f;
    }

    public double getS_g() {
        return s_g;
    }

    public void setS_g(double s_g) {
        this.s_g = s_g;
    }

    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }
}

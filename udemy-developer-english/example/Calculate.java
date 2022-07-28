
public class Calculate implements {
    /**
     *
     * @param r : the hourly pay rate
     * @param h : the work hours
     * @param ovt : the overtime pay rate
     * @param ovh : the overtime work hours
     * @param e : indicate if employee is eligible for overtime pay
     * @return the total amount of pay
     */
    public Integer calculatePay(int r, int h, int ovt, int ovh, int e) {
        int t = r * h; // calculate pay for normal working hours

        if (e) {
            t = t + ovt * ovh; // if yes, appli overtime rate
        } else {
            t = t + r * ovh; // if no, apply normal rate
        }

        return t; // return total pay
    }

    public Integer calculatePay(int hourlyPayRate, int workHours, int overtimePayRate, int overtimeWorkHours, boolean isEligibleOvertimeRate) {
        int totalPay = 0;
        totalPay = hourlyPayRate * workHours;

        if (isEligibleOvertimeRate) {
            totalPay = totalPay + overtimePayRate * overtimeWorkHours;
        } else {
            totalPay = totalPay + hourlyPayRate * overtimePayRate;
        }

        return totalPay;
    }
}
package util;

import clinic.*;

/**
 * Class to sort the appointments and providers based on different criteria.
 * @author Pranav Sudheer
 */
public class Sort {
    /**
     * Sorts the list of appointments based on the key.
     * @param list the list of appointments
     * @param key the key to sort the list
     */
    public static void appointment(List<Appointment> list, char key){
        if(key == '1') {
            PP(list);
        }
        else if(key == '2') {
            PA(list);
        }
        else if(key == '3') {
            PL(list);
        }
        else if(key == '4') {
            PO(list);
        }
        else if(key == '5') {
            PI(list);
        }
    }

    /**
     * Sorts the list of providers based on their profile.
     * @param list the list of providers
     */
    public static void provider(List<Provider> list){
        for(int i = 0; i < list.size() - 1; i++){
            for(int j = i + 1; j < list.size(); j++){
                if(list.get(i).getProfile().compareTo(list.get(j).getProfile()) < 0){
                    Provider temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
    /**
     * Sorts the list of appointments based on the patient name, date and time in that order.
     * @param list the list of appointments
     */
    public static void PP(List<Appointment> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getPatient().getProfile().compareTo(list.get(j).getPatient().getProfile()) < 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                else if(list.get(i).getPatient().getProfile().compareTo(list.get(j).getPatient().getProfile()) == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) < 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                else if(list.get(i).getPatient().getProfile().compareTo(list.get(j).getPatient().getProfile()) == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) == 0 && list.get(i).compareTo(list.get(j)) < 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
    /**
     * Sorts the list of appointments based on the date, time, and provider in that order.
     * @param list the list of appointments
     */
    public static void PA(List <Appointment> list){
        for(int i = 0; i < list.size() - 1; i++){
            for(int j = i + 1; j < list.size(); j++){
                if(list.get(i).getDate().compareTo(list.get(j).getDate()) < 0){
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                else if(list.get(i).getDate().compareTo(list.get(j).getDate()) == 0 && list.get(i).compareTo(list.get(j)) < 0){
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
    /**
     * Sorts the list of appointments based on the location (county), date and time.
     * @param list the list of appointments
     */
    public static void PL(List <Appointment> list){
        for(int i = 0; i < list.size() - 1; i++) {
            for(int j = i + 1; j < list.size(); j++) {
                Provider temp1 = (Provider) list.get(i).getProvider();
                Provider temp2 = (Provider) list.get(j).getProvider();
                int countyComp = temp1.getLocation().getCounty().compareTo(temp2.getLocation().getCounty());
                if(countyComp > 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                else if(countyComp == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) < 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                else if(countyComp == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) == 0 && list.get(i).compareTo(list.get(j)) < 0) {
                    Appointment temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
    /**
     * Sorts the list of patients based on profile.
     * @param patients the list of patients
     */
    public static void PS(List <Patient> patients){
        for(int i = 0; i < patients.size() - 1; i++){
            for(int j = i + 1; j < patients.size(); j++){
                if(patients.get(i).getProfile().compareTo(patients.get(j).getProfile()) < 0){
                    Patient temp = patients.get(i);
                    patients.set(i, patients.get(j));
                    patients.set(j, temp);
                }
            }
        }
    }
    /**
     * Sorts the list of doctor appointments based on county, date and time .
     * @param list the list of appointments
     */
    public static void PO(List <Appointment> list){
        for(int i = 0; i < list.size(); i++) {
            for(int j = i + 1; j < list.size(); j++) {
                if(list.get(i).getProvider() instanceof Doctor && list.get(j).getProvider() instanceof Doctor) {
                    Doctor temp1 = (Doctor) list.get(i).getProvider();
                    Doctor temp2 = (Doctor) list.get(j).getProvider();
                    int countyCompare = temp1.getLocation().getCounty().compareTo(temp2.getLocation().getCounty());
                    if(countyCompare > 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                    else if(countyCompare == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) < 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                    else if(countyCompare == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) == 0 && list.get(i).compareTo(list.get(j)) < 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }
    }
    /**
     * Sorts the list of technician appointments based on county, date and time.
     * @param list the list of appointments
     */
    public static void PI(List <Appointment> list){
        for(int i = 0; i < list.size(); i++) {
            for(int j = i + 1; j < list.size(); j++) {
                if(list.get(i).getProvider() instanceof Technician && list.get(j).getProvider() instanceof Technician) {
                    Technician temp1 = (Technician) list.get(i).getProvider();
                    Technician temp2 = (Technician) list.get(j).getProvider();
                    int countyCompare = temp1.getLocation().getCounty().compareTo(temp2.getLocation().getCounty());
                    if(countyCompare > 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                    else if(countyCompare == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) < 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                    else if(countyCompare == 0 && list.get(i).getDate().compareTo(list.get(j).getDate()) == 0 && list.get(i).compareTo(list.get(j)) < 0) {
                        Appointment temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }
    }
    /**
     * Sorts the list of doctor credit accounts based on their profile.
     * @param list the list of providers
     */
    public static void PC(List <Provider> list){
        for(int i = 0; i < list.size() - 1; i++){
            for(int j = i + 1; j < list.size(); j++){
                if(list.get(i).getProfile().compareTo(list.get(j).getProfile()) < 0){
                    Provider temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
}

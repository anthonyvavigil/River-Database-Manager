package riverDataIA;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anthony Vigil
 */
public class VariableNameSet {
    String v1;
    String v2;
    public VariableNameSet(String a1, String a2) {
    v1 = a1;
    v2 = a2;
    }
    public void setV1(String a){
        v1 = a;
    }
    public void setV2(String a){
        v2 = a;
    }
    public String getJavaName() {
        return v1;
    }
    public String getSQLName() {
        return v2;
    }
    
}

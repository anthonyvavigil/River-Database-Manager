/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riverDataIA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Anthony Vigil
 */
public class dataImporter extends javax.swing.JFrame {

    Connection con;
    Statement stmt;
    ResultSet rs;
    ArrayList<River> allData = new ArrayList();
    int elementsInList = 0;
    
    /**
     * Creates new form dataImporter
     */
    public dataImporter() {
        initComponents();
        DoConnect();
        
    }
    public void DoConnect() {
        String host = "jdbc:derby://localhost:1527/IA_unstable";
        String passphrase = "root";
        String user = "root";
        
              
        try {
        Connection con = DriverManager.getConnection(host, user, passphrase);
        
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        rs = stmt.executeQuery("SELECT * FROM RIVERSET2");
        }
        catch (SQLException e) {
            System.out.println("could not establish connnection to \"" + host + "\" with username \"" + user + "\" and password \"" + passphrase + "\"");
            System.out.println(e.getMessage());
        }
    }
    
    public void writeToDatabaseFromFile(File inFile){
       Scanner scn = null;
       try {
           scn = new Scanner(inFile);
       } catch (Exception e){
           System.out.println("Scanner could not be established");
           e.printStackTrace();
       }
       
       int count = 0;
       while(scn.hasNextLine()){
           count++;
           String currentLine = scn.nextLine();
           String[] curLineSplit = currentLine.split("\t");
           
           /*
           System.out.println("------------------------");
           for(int i = 0; i < curLineSplit.length; i++){
               System.out.println(curLineSplit[i]);
           }
           System.out.println("------------------------");
           */
           int a =  Integer.valueOf(curLineSplit[2]);
           String dateString = curLineSplit[3];
           String timeString = curLineSplit[4];
           
           Date dateHHMMSS = null;
           DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
           try {dateHHMMSS = formatter.parse(timeString);} catch(Exception e){ e.printStackTrace();}
           
           Time t = new Time(dateHHMMSS.getTime());
           
           String[] splitter = dateString.split("/");
           String month = splitter[0];
           String d = splitter[1];
 
           if(month.length() == 1){
               month = "0" + month;
           }
           if(d.length() == 1){
               d = "0" + d;
           }
           dateString = splitter[2] +  "/" + month + "/" + d;
           Calendar c = Calendar.getInstance();
           Date dateCalendar = new Date();
           dateCalendar.setYear(Integer.valueOf(splitter[2])-1900);
           dateCalendar.setMonth(Integer.valueOf(month)-1);
           dateCalendar.setDate(Integer.valueOf(d));
           
           
           /*
           DateFormat frm = new SimpleDateFormat("yyyy/MM/dd");
           try{dateCalendar = formatter.parse(dateString);} catch(Exception e){ e.printStackTrace();}
           */
           System.out.println(dateCalendar.toString());
           System.out.println(t.toString());
           System.out.println("------------------------------");
                   
           
           River temp = new River();
           temp.setID(count);
           temp.setDate(dateCalendar);
           temp.setTime(t);
           temp.setLocation(a);
           temp.setDate(dateCalendar);
           if(curLineSplit[1].isEmpty()){
               temp.setMonitorNames("NO DATA!");
           } else {
               temp.setMonitorNames(curLineSplit[1]);
           }
           temp.setObservations(curLineSplit[5]);
           temp.setEColi1(Integer.valueOf(curLineSplit[11]));
           temp.setEColi2(Integer.valueOf(curLineSplit[13]));
                     
           
           String fir3 = curLineSplit[12].substring(0, 3);
           String fir3_2 = curLineSplit[14].substring(0, 3);
           if(fir3.equals("1-9")){
               temp.setAbundance("Rare");
           } else if (fir3.equals("10-")){
               temp.setAbundance("Common");
           } else if(fir3.equals("101")){
               temp.setAbundance("Abundant");
           } else {
               temp.setAbundance("Too abundant to count");
           }
           if(fir3_2.equals("1-9")){
               temp.setAbundance2("Rare");
           } else if (fir3_2.equals("10-")){
               temp.setAbundance2("Common");
           } else if(fir3_2.equals("101")){
               temp.setAbundance2("Abundant");
           } else {
               temp.setAbundance2("Too abundant to count");
           }
           allData.add(temp);     
       }
       sendRiversToDatabase(allData);
       try {
           rs.close();
           stmt.close();
       } catch (Exception e){
           System.out.println("resultSet or statement could not be closed");
           e.printStackTrace();
       }
       
    }
    
    public void sendRiversToDatabase(ArrayList<River> data) {
      try {
       if(rs.next()){
          rs.first();
           while(rs.next()) { 
            elementsInList++;
        }
       }
       rs.first();
      } catch (Exception e){ e.printStackTrace(); }
      System.out.println(elementsInList);
        for(int i = 0; i < data.size(); i++){
                try {
                    rs.moveToInsertRow();
                    
                    River temp = data.get(i);
                    
                    elementsInList++;
                    rs.updateInt("ID", elementsInList);
                    rs.updateInt("LOCATION_ID", temp.getLocation());
                    rs.updateObject("DATE_COLLECTED", temp.getDate());
                    rs.updateString("MONITOR_NAMES", temp.getMonitorNames());
                    rs.updateTime("TIME_COLLECTED", temp.getTime());
                    rs.updateString("OBSERVATIONS", temp.getObservations());
                    rs.updateInt("E_COLI_SAMPLE_1", temp.getEColi1());
                    rs.updateInt("E_COLI_SAMPLE_2", temp.getEColi2());
                    rs.updateString("ABUNDANCE", temp.getAbundance());
                    rs.updateString("ABUNDANCE_2", temp.getAbundance2());
                    
                    rs.insertRow();
                    
                        }
                catch (Exception e) { 
                    System.out.println("Error when moving ArrayList to SQL database");
                    e.printStackTrace();
                }
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGo = new javax.swing.JButton();
        labelFileLoc = new javax.swing.JLabel();
        textFieldFileLoc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGo.setText("GO!");
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });

        labelFileLoc.setText("File Location: ");

        textFieldFileLoc.setText("RiverData.txt");
        textFieldFileLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldFileLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelFileLoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldFileLoc, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGo))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGo)
                    .addComponent(labelFileLoc)
                    .addComponent(textFieldFileLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoActionPerformed
        // TODO add your handling code here:
        System.out.println(textFieldFileLoc.getText());
        File file = null;
        try {
            file = new File(textFieldFileLoc.getText());
        } catch (Exception e) {
            System.out.println("No file could be found at location: \"" + textFieldFileLoc.getText() + "\"");
            e.printStackTrace();
        }
        writeToDatabaseFromFile(file);
    }//GEN-LAST:event_btnGoActionPerformed

    private void textFieldFileLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldFileLocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldFileLocActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
               java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataImporter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelFileLoc;
    private javax.swing.JTextField textFieldFileLoc;
    // End of variables declaration//GEN-END:variables
}

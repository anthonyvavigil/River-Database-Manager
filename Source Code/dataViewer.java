/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riverDataIA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.table.TableColumn;
import javax.swing.table.*;
import java.util.Collections;
import java.text.DecimalFormat;


/**
 *
 * @author Anthony Vigil
 */
public class dataViewer extends javax.swing.JFrame {

    DefaultTableModel model;
    ArrayList<Connection> cons = new ArrayList(); ArrayList<Statement> stmts = new ArrayList(); ArrayList<ResultSet> rses = new ArrayList(); 
    ArrayList<River> allData = new ArrayList(); ArrayList<VariableNameSet> vars = new ArrayList(); ArrayList<VariableNameSet> queriedColumns = new ArrayList();
    Boolean mean = false, varSt = false, chID = false, chLocID = false, chObserv = false, chMon = false, chDate = false, chTime = false, chEColi1 = false, chEColi2 = false, chAbundance = false, chAbundance2 = false, chObservations = false;
    int numColumns = 0;
    
    String currentQuery = "SELECT * FROM RIVERSET2";
    String host = "jdbc:derby://localhost:1527/IA";
    String passphrase = "root";
    String user = "root";
   
    
    
    /**
     * Creates new form dataViewer
     */
    public dataViewer() {
        
        initTranslator();
        connectToDatabase(currentQuery);
        initComponents();
        
        
        //sets up drop downs
        
        for(int i = 0; i < vars.size(); i++){
            choice1.add(vars.get(i).getJavaName());
            choice2.add(vars.get(i).getJavaName());
            choice3.add(vars.get(i).getJavaName());
        }
       
        model = new DefaultTableModel();
        table1.setModel(model);
    }
    
    
    
    public void connectToDatabase(String query) {
        
              
        try {
            //prints data about query
            printConnectionData(query);
            
            //closes old connections, statements, and resultsets
            for (int i = 0; i < cons.size(); i++) {
                cons.get(i).close();
                stmts.get(i).close();
                rses.get(i).close();
                
                cons.remove(i);
                stmts.remove(i);
                rses.remove(i);
            }
            
            //opens new connections, statements, and resultsets
            cons.add(DriverManager.getConnection(host, user, passphrase));
            stmts.add(cons.get(cons.size()-1).createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
            rses.add(stmts.get(stmts.size()-1).executeQuery("SELECT * FROM RIVERSET2"));
            
            //prints if query is successful - mainly for testing
            System.out.println("query was successful");
        }
        catch (SQLException e) {
            //System.out.println("could not establish connnection to \"" + host + "\" with username \"" + user + "\" and password \"" + passphrase + "\"");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void printConnectionData(String query) {
        //for bugtesting, only effects the console
        for(int i = 0; i < cons.size(); i++) {
            System.out.println("CONNECTION " + (i+1) + "-------------------\n\n");
            System.out.println("host:\t" + host + "\nuser:\t" + user + "\npassphrase:\t" + passphrase);
            System.out.println("query:\t" + query + "\n\n");
        }
    }
    
    public void initTranslator() {
        //object with multiple names is created to make end user experience easier - they will see a more polished title for each column
        vars.add(new VariableNameSet("Element ID", "ID"));
        vars.add(new VariableNameSet("Location ID", "LOCATION_ID"));
        vars.add(new VariableNameSet("Date Collected", "DATE_COLLECTED"));
        vars.add(new VariableNameSet("Monitor Names", "MONITOR_NAMES"));
        vars.add(new VariableNameSet("Observations", "OBSERVATIONS"));
        vars.add(new VariableNameSet("Time Collected", "TIME_COLLECTED"));
        vars.add(new VariableNameSet("E. Coli Sample 1", "E_COLI_SAMPLE_1"));
        vars.add(new VariableNameSet("E. Coli Sample 2", "E_COLI_SAMPLE_2"));
        vars.add(new VariableNameSet("Abundance 1", "ABUNDANCE"));
        vars.add(new VariableNameSet("Abundance 2", "ABUNDANCE_2"));
    }
    
    
    public void createQuery() {
        
        currentQuery = "SELECT ";
        queriedColumns = new ArrayList();
        
        // GENERATES AN SQL QUERY
        
        if (chID){
            currentQuery = currentQuery + "ID, ";
            queriedColumns.add(new VariableNameSet("Element ID", "ID"));
        } if (chLocID){
            currentQuery = currentQuery + "LOCATION_ID, ";
            queriedColumns.add(new VariableNameSet("Location ID", "LOCATION_ID"));
        } if (chMon){
            currentQuery = currentQuery + "MONITOR_NAMES, ";
            queriedColumns.add(new VariableNameSet("Monitors", "MONITOR_NAMES"));
        } if(chObserv){
            currentQuery = currentQuery + "OBSERVATIONS, ";
            queriedColumns.add(new VariableNameSet("Observations", "OBSERVATIONS"));
        } if (chDate){
            currentQuery = currentQuery + "DATE_COLLECTED, ";
            queriedColumns.add(new VariableNameSet("Date Collected", "DATE_COLLECTED"));
        } if (chTime){
            currentQuery = currentQuery + "TIME_COLLECTED, ";
            queriedColumns.add(new VariableNameSet("Time Collected", "TIME_COLLECTED"));
        } if (chEColi1){
            currentQuery = currentQuery + "E_COLI_SAMPLE_1, ";
            queriedColumns.add(new VariableNameSet("E. Coli Sample 1", "E_COLI_SAMPLE_1"));
        } if (chEColi2){
            currentQuery = currentQuery + "E_COLI_SAMPLE_2, ";
            queriedColumns.add(new VariableNameSet("E. Coli Sample 2", "E_COLI_SAMPLE_2"));
        } if (chAbundance) {
            currentQuery = currentQuery + "ABUNDANCE, ";
            queriedColumns.add(new VariableNameSet("Abundance 1", "ABUNDANCE"));
        } if(chAbundance2) {
            currentQuery = currentQuery + "ABUNDANCE_2, ";
            queriedColumns.add(new VariableNameSet("Abundance 2", "ABUNDANCE_2"));
        }
        if(currentQuery.equals("SELECT ")){
            JOptionPane.showMessageDialog(null, "No Columns Selected!");
        } else {
        currentQuery = currentQuery.substring(0, currentQuery.length()-2) + " FROM RIVERSET2 ";
        
        
        String grouper = choice1.getSelectedItem();
        String sorter = choice3.getSelectedItem();
        for(int i = 0; i < vars.size(); i++) {
            if(grouper.equals(vars.get(i).getJavaName())){
                grouper = vars.get(i).getSQLName();
            }
            if(sorter.equals(vars.get(i).getJavaName())){
                sorter = vars.get(i).getSQLName();
            }
        }
        
        currentQuery = currentQuery + "GROUP BY " + grouper + " ORDER BY " + sorter;
        
                     
    }
        
        connectToDatabase(currentQuery);
        updateTable();
        if(varSt || mean){
            arrayListSetUp();
        }
    }
    
    //stats methods
    
    public void arrayListSetUp() {
        ArrayList<Integer> dataToAnalyze = new ArrayList();
        String column = choice2.getSelectedItem();
        if(column.equals("Time Collected") || column.equals("Date Collected") || column.equals("Monitor Names") || column.equals("Observations") || column.equals("Abundance 1") || column.equals("Abundance 2")){
             JOptionPane.showMessageDialog(null, "Column selected is not of type integer");
        } else {
            if(column.equals("Element ID")){
                for(int i = 0; i < allData.size(); i++) {
                    dataToAnalyze.add(allData.get(i).getID());
                }
            }
            if(column.equals("Location ID")){
                for(int i = 0; i < allData.size(); i++) {
                    dataToAnalyze.add(allData.get(i).getLocation());
                }
            }            
            if(column.equals("E. Coli Sample 1")){
                for(int i = 0; i < allData.size(); i++) {
                    dataToAnalyze.add(allData.get(i).getEColi1());
                }
            }          
            if(column.equals("E. Coli Sample 2")){
                for(int i = 0; i < allData.size(); i++) {
                    dataToAnalyze.add(allData.get(i).getEColi2());
                }
            }
            Collections.sort(dataToAnalyze);
            if(varSt) {
                varStats(dataToAnalyze);
            } if(mean) {
                mean(dataToAnalyze);
            }
        }
    }
    public void mean (ArrayList<Integer> dataToAnalyze) {
            double mean;
            int total = 0;
            for(int i = 0; i < dataToAnalyze.size(); i++){
                total += dataToAnalyze.get(i);
            }
            mean = ((double) total)/((double) dataToAnalyze.size());
            
            double totalStDev = 0;
            for(int j = 0; j < dataToAnalyze.size(); j++){
                double a = Math.abs((dataToAnalyze.get(j)));
                totalStDev += a/mean;
            }
            totalStDev = totalStDev/(double) dataToAnalyze.size();
            
            String meanSt = "";
            String stDevSt = "";
            DecimalFormat decform = new DecimalFormat("#.00"); 
            meanSt = decform.format(mean);
            stDevSt = decform.format(totalStDev);
            
            JOptionPane.showMessageDialog(null, "mean: " + meanSt + "\nst. dev: " + stDevSt);
    }
    
    public void varStats(ArrayList<Integer> dataToAnalyze) {
            int min = dataToAnalyze.get(0);
            int quartile1 = dataToAnalyze.get((dataToAnalyze.size()/4));
            int med = dataToAnalyze.get((dataToAnalyze.size()/2));
            int quartile3 = dataToAnalyze.get((3*(dataToAnalyze.size()/4)));
            int max = dataToAnalyze.get(dataToAnalyze.size()-1);
            JOptionPane.showMessageDialog(null, "min: \t" +  min + "\nquartile1: \t" + quartile1 + "\nmed: \t" + med + "\nquartile3: \t" + quartile3 + "\nmax: \t" + max);
  
    }
    
    public void updateTable() {
        
        //contacts database for new data
        
        try {                        
            
        //migrates data into an ArrayList of river objects

            while(rses.get(rses.size()-1).next()) {
                River temp = new River();
                for(int i = 0; i < queriedColumns.size(); i++) {
                    if(queriedColumns.get(i).getJavaName().equals("Element ID")){
                        temp.setID(rses.get(rses.size()-1).getInt("ID"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Location ID")){
                        temp.setLocation(rses.get(rses.size()-1).getInt("LOCATION_ID"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Observations")) {
                        temp.setObservations(rses.get(rses.size()-1).getString("OBSERVATIONS"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Date Collected")) {
                        temp.setDate(rses.get(rses.size()-1).getDate("DATE_COLLECTED"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Time Collected")) {
                        temp.setTime(rses.get(rses.size()-1).getTime("TIME_COLLECTED"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Monitor Names")) {
                        temp.setMonitorNames(rses.get(rses.size()-1).getString("MONITOR_NAMES"));
                    }   if(queriedColumns.get(i).getJavaName().equals("E. Coli Sample 1")){
                        temp.setEColi1(rses.get(rses.size()-1).getInt("E_COLI_SAMPLE_1"));
                    }   if(queriedColumns.get(i).getJavaName().equals("E. Coli Sample 2")){
                        temp.setEColi2(rses.get(rses.size()-1).getInt("E_COLI_SAMPLE_2"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Abundance 1")) {
                        temp.setAbundance(rses.get(rses.size()-1).getString("ABUNDANCE"));
                    }   if(queriedColumns.get(i).getJavaName().equals("Abundance 2")) {
                        temp.setAbundance(rses.get(rses.size()-1).getString("ABUNDANCE_2"));
                    }
                }
                allData.add(temp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Query could not be executed");
            System.out.println(currentQuery);
            e.printStackTrace();
        }
       
    //completely resets the table and columns

    
    DefaultTableModel model = new DefaultTableModel();
    table1.setModel(model);
    for(int i = 0; i < model.getRowCount(); i++) {
        model.removeRow(i);
      //  table1.remove(table1.get);
    }
    model.setColumnCount(0);
    
    
    //adds the columns specified by user
    
    String[] colNames = new String[queriedColumns.size()];
    for(int i = 0; i < queriedColumns.size(); i++){
         TableColumn temp = new TableColumn();
         colNames[i] = queriedColumns.get(i).getJavaName();
         model.addColumn(temp);
    }
    model.setColumnIdentifiers(colNames);    
    
    
    //adds data to respective columns
    for(int i = 0; i < allData.size(); i++){
        River temp = allData.get(i);
        ArrayList<Object> toAdd = new ArrayList();
        
        if(chID){
            toAdd.add(temp.getID());
        } if(chLocID){
            toAdd.add(temp.getLocation());
        } if(chMon) {
            toAdd.add(temp.getMonitorNames());
        } if(chObserv) {
            toAdd.add(temp.getObservations());
        } if(chDate) {
            toAdd.add(temp.getDate());
        }  if(chTime) {
            toAdd.add(temp.getTime());
        }  if(chEColi1) {
            toAdd.add(temp.getEColi1());
        } if(chEColi2) {
            toAdd.add(temp.getEColi2());
        } if(chAbundance) {
            toAdd.add(temp.getAbundance());
        } if(chAbundance2) {
            toAdd.add(temp.getAbundance2());
        }
        Object[] toAddList = new Object[toAdd.size()];
        for(int j = 0; j < toAdd.size(); j++){
            toAddList[j] = toAdd.get(j);
           // System.out.println(toAddList[j]);
        }
        
        model.addRow(toAddList);
       
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cBoxLocID = new javax.swing.JCheckBox();
        cBoxID = new javax.swing.JCheckBox();
        cBoxDate = new javax.swing.JCheckBox();
        cBoxObservations = new javax.swing.JCheckBox();
        cBoxTime = new javax.swing.JCheckBox();
        cBoxMonitors = new javax.swing.JCheckBox();
        cBoxAbundance2 = new javax.swing.JCheckBox();
        cBoxAbundance = new javax.swing.JCheckBox();
        cBox5Var = new javax.swing.JCheckBox();
        cBoxEColi1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        choice1 = new java.awt.Choice();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        choice3 = new java.awt.Choice();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        choice2 = new java.awt.Choice();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cBoxEColi2 = new javax.swing.JCheckBox();
        cBoxMean = new javax.swing.JCheckBox();
        btnUpdate = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table1.setSurrendersFocusOnKeystroke(true);
        jScrollPane1.setViewportView(table1);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel1.setText("Data Analyzer");

        cBoxLocID.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxLocID.setText("Location ID");
        cBoxLocID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxLocIDActionPerformed(evt);
            }
        });

        cBoxID.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxID.setText("Element ID");
        cBoxID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxIDActionPerformed(evt);
            }
        });

        cBoxDate.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxDate.setText("Date Collected");
        cBoxDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxDateActionPerformed(evt);
            }
        });

        cBoxObservations.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxObservations.setText("Observations");
        cBoxObservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxObservationsActionPerformed(evt);
            }
        });

        cBoxTime.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxTime.setText("Time Collected");
        cBoxTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxTimeActionPerformed(evt);
            }
        });

        cBoxMonitors.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxMonitors.setText("Monitor Names");
        cBoxMonitors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxMonitorsActionPerformed(evt);
            }
        });

        cBoxAbundance2.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxAbundance2.setText("Abundance 2");
        cBoxAbundance2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxAbundance2ActionPerformed(evt);
            }
        });

        cBoxAbundance.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxAbundance.setText("Abundance 1");
        cBoxAbundance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxAbundanceActionPerformed(evt);
            }
        });

        cBox5Var.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBox5Var.setText("5-var stats");
        cBox5Var.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBox5VarActionPerformed(evt);
            }
        });

        cBoxEColi1.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxEColi1.setText("E. Coli Sample 1");
        cBoxEColi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxEColi1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setText("Select columns to be included in table");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel3.setText("Select column to group by (must be checked)");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel4.setText("Select column to sort by (must be checked)");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel5.setText("Advanced Commands");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel7.setText("Statistical Calculations");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel9.setText("Column");

        cBoxEColi2.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxEColi2.setText("E. Coli Sample 2");
        cBoxEColi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxEColi2ActionPerformed(evt);
            }
        });

        cBoxMean.setFont(new java.awt.Font("Lucida Sans Unicode", 1, 11)); // NOI18N
        cBoxMean.setText("Mean, St. dev");
        cBoxMean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxMeanActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update Table");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                        .addGap(200, 200, 200)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                    .addComponent(jLabel2)
                    .addComponent(cBoxLocID)
                    .addComponent(cBoxID)
                    .addComponent(cBoxObservations)
                    .addComponent(cBoxTime)
                    .addComponent(cBoxMonitors)
                    .addComponent(cBoxAbundance)
                    .addComponent(cBoxEColi1)
                    .addComponent(cBoxAbundance2)
                    .addComponent(jLabel3)
                    .addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(choice3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cBoxEColi2)
                    .addComponent(btnUpdate)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(choice2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cBox5Var)
                            .addComponent(cBoxMean)))
                    .addComponent(cBoxDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1081, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxLocID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxMonitors)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxObservations)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxEColi1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxEColi2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxAbundance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cBoxAbundance2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(choice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(choice3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(choice2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(cBox5Var))
                            .addComponent(cBoxMean))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addComponent(btnUpdate)
                .addContainerGap())
            .addComponent(jScrollPane1)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cBoxLocIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxLocIDActionPerformed
        // TODO add your handling code here:
        chLocID = !chLocID;
    }//GEN-LAST:event_cBoxLocIDActionPerformed

    private void cBoxIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxIDActionPerformed
        // TODO add your handling code here:
        chID = !chID;
    }//GEN-LAST:event_cBoxIDActionPerformed

    private void cBoxDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxDateActionPerformed
        // TODO add your handling code here:
        chDate = !chDate;
    }//GEN-LAST:event_cBoxDateActionPerformed

    private void cBoxObservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxObservationsActionPerformed
        // TODO add your handling code here:
        chObserv = !chObserv;
    }//GEN-LAST:event_cBoxObservationsActionPerformed

    private void cBoxTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxTimeActionPerformed
        // TODO add your handling code here:
        chTime = !chTime;
    }//GEN-LAST:event_cBoxTimeActionPerformed

    private void cBoxMonitorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxMonitorsActionPerformed
        // TODO add your handling code here:
        chMon = !chMon;
    }//GEN-LAST:event_cBoxMonitorsActionPerformed

    private void cBoxAbundance2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxAbundance2ActionPerformed
        // TODO add your handling code here:
        chAbundance2 = !chAbundance2;
    }//GEN-LAST:event_cBoxAbundance2ActionPerformed

    private void cBoxAbundanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxAbundanceActionPerformed
        // TODO add your handling code here:
        chAbundance = !chAbundance;
    }//GEN-LAST:event_cBoxAbundanceActionPerformed

    private void cBox5VarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBox5VarActionPerformed
        // TODO add your handling code here:
        varSt = !varSt;
    }//GEN-LAST:event_cBox5VarActionPerformed

    private void cBoxEColi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxEColi1ActionPerformed
        // TODO add your handling code here:
        chEColi1 = !chEColi1;
    }//GEN-LAST:event_cBoxEColi1ActionPerformed

    private void cBoxEColi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxEColi2ActionPerformed
        // TODO add your handling code here:
        chEColi2 = !chEColi2;
    }//GEN-LAST:event_cBoxEColi2ActionPerformed

    private void cBoxMeanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxMeanActionPerformed
        // TODO add your handling code here:
        mean = !mean;
    }//GEN-LAST:event_cBoxMeanActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        createQuery();
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataViewer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JCheckBox cBox5Var;
    private javax.swing.JCheckBox cBoxAbundance;
    private javax.swing.JCheckBox cBoxAbundance2;
    private javax.swing.JCheckBox cBoxDate;
    private javax.swing.JCheckBox cBoxEColi1;
    private javax.swing.JCheckBox cBoxEColi2;
    private javax.swing.JCheckBox cBoxID;
    private javax.swing.JCheckBox cBoxLocID;
    private javax.swing.JCheckBox cBoxMean;
    private javax.swing.JCheckBox cBoxMonitors;
    private javax.swing.JCheckBox cBoxObservations;
    private javax.swing.JCheckBox cBoxTime;
    private java.awt.Choice choice1;
    private java.awt.Choice choice2;
    private java.awt.Choice choice3;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable table1;
    // End of variables declaration//GEN-END:variables
}

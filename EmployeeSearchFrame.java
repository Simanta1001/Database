import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.io.FileInputStream;
import java.util.Properties;


public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCustomDatabase;
    private JList<String> lstCustomDepartment;
    private DefaultListModel<String> customDepartmentModel = new DefaultListModel<>();
    private JList<String> lstCustomProject;
    private DefaultListModel<String> customProjectModel = new DefaultListModel<>();
    private JTextArea textAreaCustomEmployee;
    //private String customDatabaseName = "";
    private JCheckBox chckbxNotCustomDept;
    private JCheckBox chckbxNotCustomProject;
    private String allowedDatabaseName = "COMPANY"; // Specify the allowed database name

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmployeeSearchFrame frame = new EmployeeSearchFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmployeeSearchFrame() {
        setTitle("Custom Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 347);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 23, 59, 14);
        contentPane.add(lblNewLabel);

        txtCustomDatabase = new JTextField();
        txtCustomDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtCustomDatabase);
        txtCustomDatabase.setColumns(10);

        JButton btnCustomDBFill = new JButton("Fill");
        btnCustomDBFill.addActionListener(e -> fillCustomDBLists());
        btnCustomDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnCustomDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnCustomDBFill);

        lstCustomProject = new JList<String>(customProjectModel);
        lstCustomProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstCustomProject.setBounds(225, 84, 150, 42);
        contentPane.add(lstCustomProject);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(255, 63, 47, 14);
        contentPane.add(lblProject);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        JScrollPane scrollPaneProject = new JScrollPane();
        scrollPaneProject.setBounds(225, 84, 150, 42);
        contentPane.add(scrollPaneProject);
        scrollPaneProject.setViewportView(lstCustomProject);

        chckbxNotCustomDept = new JCheckBox("Not");
        chckbxNotCustomDept.setBounds(71, 133, 59, 23);
        contentPane.add(chckbxNotCustomDept);

        chckbxNotCustomProject = new JCheckBox("Not");
        chckbxNotCustomProject.setBounds(270, 133, 59, 23);
        contentPane.add(chckbxNotCustomProject);

        lstCustomDepartment = new JList<String>(customDepartmentModel);
        lstCustomDepartment.setBounds(36, 84, 172, 40);
        contentPane.add(lstCustomDepartment);
        lstCustomDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JScrollPane scrollPaneDepartment = new JScrollPane();
        scrollPaneDepartment.setBounds(36, 84, 172, 40);
        contentPane.add(scrollPaneDepartment);
        scrollPaneDepartment.setViewportView(lstCustomDepartment);

        JLabel lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(52, 179, 89, 14);
        contentPane.add(lblEmployee);

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("database.props")) {
            prop.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            

            
            public void actionPerformed(ActionEvent e) {
                /*String enteredDatabaseName = txtCustomDatabase.getText().trim();
                
             if (!allowedDatabaseName.equalsIgnoreCase(enteredDatabaseName)) {

            // Show an alert if the entered database name is not allowed
            JOptionPane.showMessageDialog(EmployeeSearchFrame.this, "Error: Database access is restricted to 'COMPANY'.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop execution if the entered database name is not allowed*/
   // }
                try {
                    // Use the properties for database connection
                    Connection conn = DriverManager.getConnection(url, user, password);
                    Statement statementPrint = conn.createStatement();
                    List<String> selectedProjects = lstCustomProject.getSelectedValuesList();
                    List<String> selectedDepartment = lstCustomDepartment.getSelectedValuesList();
                    String projectName = "";
                    String departmentName = "";
                    String notDepartment = "";
                    String notProject = "";
                    if (!selectedProjects.isEmpty()) {
                        for (String project : selectedProjects) {
                            projectName = projectName + "'" + project + "'" + ",";
                        }
                        projectName = projectName.substring(0, projectName.length() - 1);
                    }
                    if (!selectedDepartment.isEmpty()) {
                        for (String department : selectedDepartment) {
                            departmentName = departmentName + "'" + department + "',";
                        }
                        departmentName = departmentName.substring(0, departmentName.length() - 1);
                    }
                    if (chckbxNotCustomDept.isSelected()) {
                        notDepartment = "NOT";
                    }
                    if (chckbxNotCustomProject.isSelected()) {
                        notProject = "NOT";
                    }
                    String queryString = "";
                    if (selectedProjects.isEmpty() && selectedDepartment.isEmpty()) {
                        queryString = "SELECT Fname, Minit, Lname FROM EMPLOYEE";
                    } else if (selectedDepartment.isEmpty()) {
                        queryString = "SELECT DISTINCT E.Fname, E.Minit, E.Lname FROM EMPLOYEE E LEFT JOIN WORKS_ON WO ON E.Ssn = WO.Essn LEFT JOIN PROJECT P ON WO.Pno = P.Pnumber WHERE "
                                + " E.Ssn " + notProject
                                + " IN (SELECT Essn FROM WORKS_ON LEFT JOIN PROJECT ON Pno = Pnumber WHERE Pname IN ("
                                + projectName + "));";
                    } else if (selectedProjects.isEmpty()) {
                        queryString = "SELECT DISTINCT Fname, Minit, Lname FROM EMPLOYEE, DEPARTMENT WHERE DEPARTMENT.Dnumber = EMPLOYEE.Dno AND DEPARTMENT.Dname "
                                + notDepartment + " IN (" + departmentName + ");";
                    } else {
                        queryString = "SELECT DISTINCT E.Fname, E.Minit, E.Lname FROM EMPLOYEE E INNER JOIN DEPARTMENT D ON D.Dnumber = E.Dno INNER JOIN WORKS_ON W ON E.Ssn = W.Essn WHERE D.Dname "
                                + notDepartment + " IN (" + departmentName + ") AND " + " E.Ssn " + notProject
                                + " IN (SELECT Essn FROM WORKS_ON LEFT JOIN PROJECT ON Pno = Pnumber WHERE Pname IN ("
                                + projectName + "));";
                    }
                    ResultSet employees = statementPrint.executeQuery(queryString);
                    while (employees.next()) {
                        String employeeName = employees.getString("Fname") + " " + employees.getString("Minit") + " "
                                + employees.getString("Lname");
                        textAreaCustomEmployee.append(employeeName + "\n");
                    }
                    employees.close();
                    statementPrint.close();
                    conn.close();
                } catch (Exception exep) {
                    exep.printStackTrace();
                }
            }
        });
        btnSearch.setBounds(80, 276, 89, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaCustomEmployee.setText("");
                lstCustomDepartment.clearSelection();
                lstCustomProject.clearSelection();
                chckbxNotCustomDept.setSelected(false);
                chckbxNotCustomProject.setSelected(false);
            }
        });
        btnClear.setBounds(236, 276, 89, 23);
        contentPane.add(btnClear);

        textAreaCustomEmployee = new JTextArea();
        textAreaCustomEmployee.setBounds(36, 197, 339, 68);

        JScrollPane scrollPaneEmployee = new JScrollPane();
        scrollPaneEmployee.setBounds(36, 197, 339, 68);
        scrollPaneEmployee.setViewportView(textAreaCustomEmployee);
        contentPane.add(scrollPaneEmployee);
    }

    private void fillCustomDBLists() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("database.props")) {
            prop.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

        String enteredDatabaseName = txtCustomDatabase.getText().trim();
        //System.out.println(enteredDatabaseName);
        if (!allowedDatabaseName.equalsIgnoreCase(enteredDatabaseName)) {
        // Show an alert if the entered database name is not allowed
        JOptionPane.showMessageDialog(EmployeeSearchFrame.this, "Error: Database access is restricted to 'COMPANY'.", "Database Error", JOptionPane.ERROR_MESSAGE);
        return; // Stop execution if the entered database name is not allowed
    }

        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");

        try {
            //customDatabaseName = txtCustomDatabase.getText();
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statementDept = conn.createStatement();
            Statement statementProj = conn.createStatement();
            ResultSet rsDepartment = statementDept.executeQuery("SELECT Dname FROM DEPARTMENT");
            ResultSet rsProject = statementProj.executeQuery("SELECT Pname FROM PROJECT");
            customDepartmentModel.clear();
            customProjectModel.clear();
            while (rsDepartment.next()) {
                String departmentName = rsDepartment.getString("Dname");
                customDepartmentModel.addElement(departmentName);
            }
            while (rsProject.next()) {
                String projectName = rsProject.getString("Pname");
                customProjectModel.addElement(projectName);
            }
            rsProject.close();
            rsDepartment.close();
            statementDept.close();
            statementProj.close();
            conn.close();
        } catch (Exception exep) {
            exep.printStackTrace();
        }
    }
}

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCustomDatabase;
    private JList<String> lstCustomDepartment;
    private DefaultListModel<String> customDepartmentModel = new DefaultListModel<>();
    private JList<String> lstCustomProject;
    private DefaultListModel<String> customProjectModel = new DefaultListModel<>();
    private JTextArea textAreaCustomEmployee;
    private String customDatabaseName = "";
    private JCheckBox notCheckBoxCustomDept;
    private JCheckBox notCheckBoxCustomProject;
    private Properties prop;

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

        lstCustomProject = new JList<>(customProjectModel);
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

        notCheckBoxCustomDept = new JCheckBox("Not");
        notCheckBoxCustomDept.setBounds(71, 133, 59, 23);
        contentPane.add(notCheckBoxCustomDept);

        notCheckBoxCustomProject = new JCheckBox("Not");
        notCheckBoxCustomProject.setBounds(270, 133, 59, 23);
        contentPane.add(notCheckBoxCustomProject);

        lstCustomDepartment = new JList<>(customDepartmentModel);
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

        textAreaCustomEmployee = new JTextArea();
        textAreaCustomEmployee.setBounds(36, 197, 339, 68);

        JScrollPane scrollPaneEmployee = new JScrollPane();
        scrollPaneEmployee.setBounds(36, 197, 339, 68);
        scrollPaneEmployee.setViewportView(textAreaCustomEmployee);
        contentPane.add(scrollPaneEmployee);

        prop = new Properties();
        try (FileInputStream input = new FileInputStream("database.props")) {
            prop.load(input);
            String dbdriver = prop.getProperty("db.driver");
            Class.forName(dbdriver);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchCustomDatabase());
        btnSearch.setBounds(80, 276, 89, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearFields());
        btnClear.setBounds(236, 276, 89, 23);
        contentPane.add(btnClear);
    }

    private void fillCustomDBLists() {
        clearFields();
        customDatabaseName = txtCustomDatabase.getText().trim();
        String dbURL = prop.getProperty("db.url");
        String url = String.format(dbURL, customDatabaseName);
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");
    
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Attempt to connect to the database
    
            if (!databaseExists(conn, customDatabaseName)) {
                // Show an error message using a popup dialog
                JOptionPane.showMessageDialog(
                        this,
                        "Error: Database name not found!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
    
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
        } catch (Exception ex) {
            // Handle any other exceptions
            System.out.println("");
    
            // Show an error message using a popup dialog
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to find the name of the database.","Error! " ,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    private boolean databaseExists(Connection conn, String dbName) {
        try {
            String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, dbName);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void searchCustomDatabase() {
        textAreaCustomEmployee.setText("");
        customDatabaseName = txtCustomDatabase.getText().trim();
        String dbURL = prop.getProperty("db.url");
        String url = String.format(dbURL, customDatabaseName);
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");
        String dbdriver = prop.getProperty("db.driver");
//connector
        try {
            Class.forName(dbdriver);
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                List<String> selectedProjects = lstCustomProject.getSelectedValuesList();
                List<String> selectedDepartment = lstCustomDepartment.getSelectedValuesList();
                String notinDept = notCheckBoxCustomDept.isSelected() ? "NOT" : "";
                String notProject = notCheckBoxCustomProject.isSelected() ? "NOT" : "";

                String query = buildQuery(selectedProjects, selectedDepartment, notinDept, notProject);

                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    setQueryParameters(preparedStatement, selectedProjects, selectedDepartment);
                    
                    ResultSet employees = preparedStatement.executeQuery();

                    while (employees.next()) {
                        String employeeName = employees.getString("Fname") + " " + employees.getString("Minit") + " "
                                + employees.getString("Lname");
                        textAreaCustomEmployee.append(employeeName + "\n");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String buildQuery(List<String> selectedProjects, List<String> selectedDepartment, String notinDept, String notProject) {
        if (selectedProjects.isEmpty() && selectedDepartment.isEmpty()) {
            return "SELECT Fname, Minit, Lname FROM EMPLOYEE";
        } else if (selectedProjects.isEmpty()) {
            return "SELECT DISTINCT Fname, Minit, Lname FROM EMPLOYEE, DEPARTMENT WHERE DEPARTMENT.Dnumber = EMPLOYEE.Dno AND DEPARTMENT.Dname "
                    + notinDept + " IN (?)";
        } else if (selectedDepartment.isEmpty()) {
            return "SELECT DISTINCT E.Fname, E.Minit, E.Lname FROM EMPLOYEE E LEFT JOIN WORKS_ON WO ON E.Ssn = WO.Essn LEFT JOIN PROJECT P ON WO.Pno = P.Pnumber WHERE "
                    + " E.Ssn " + notProject
                    + " IN (SELECT Essn FROM WORKS_ON LEFT JOIN PROJECT ON Pno = Pnumber WHERE Pname IN (?))";
        } else {
            return "SELECT DISTINCT E.Fname, E.Minit, E.Lname FROM EMPLOYEE E INNER JOIN DEPARTMENT D ON D.Dnumber = E.Dno INNER JOIN WORKS_ON W ON E.Ssn = W.Essn WHERE D.Dname "
                    + notinDept + " IN (?) AND " + " E.Ssn " + notProject
                    + " IN (SELECT Essn FROM WORKS_ON LEFT JOIN PROJECT ON Pno = Pnumber WHERE Pname IN (?))";
        }
    }

    private void setQueryParameters(PreparedStatement preparedStatement, List<String> selectedProjects, List<String> selectedDepartment) throws SQLException {
        int parameterIndex = 1;

        for (String department : selectedDepartment) {
            preparedStatement.setString(parameterIndex++, department);
        }

        for (String project : selectedProjects) {
            preparedStatement.setString(parameterIndex++, project);
        }
    }

    private void clearFields() {
        textAreaCustomEmployee.setText("");
        lstCustomDepartment.clearSelection();
        lstCustomProject.clearSelection();
        notCheckBoxCustomDept.setSelected(false);
        notCheckBoxCustomProject.setSelected(false);
    }
}

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneDepartment;
    private JScrollPane scrollPaneProject;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EmployeeSearchFrame() {
        setTitle("Employee Search");
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

        txtDatabase = new JTextField();
        txtDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtDatabase);
        txtDatabase.setColumns(10);

        JButton btnDBFill = new JButton("Fill");
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] dept = {"Headquarters", "Reorganization"};
                for (int i = 0; i < dept.length; i++) {
                    department.addElement(dept[i]);
                }
                String[] prj = {"ProductX", "ProductY", "ProductZ"};
                for (int j = 0; j < prj.length; j++) {
                    project.addElement(prj[j]);
                }
            }
        });

        btnDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnDBFill);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        // Initialize lstDepartment
        lstDepartment = new JList<String>(new DefaultListModel<String>());
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setModel(department);
        lstDepartment.setBounds(36, 84, 172, 40);
        contentPane.add(lstDepartment);

        // Add JScrollPane for lstDepartment
        scrollPaneDepartment = new JScrollPane(lstDepartment);
        scrollPaneDepartment.setBounds(36, 84, 172, 40);
        contentPane.add(scrollPaneDepartment);

        textAreaEmployee = new JTextArea();
        scrollPane = new JScrollPane(textAreaEmployee);
        scrollPane.setBounds(36, 197, 339, 68);
        contentPane.add(scrollPane);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(255, 63, 47, 14);
        contentPane.add(lblProject);

        // Initialize lstProject
        lstProject = new JList<String>(new DefaultListModel<String>());
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setModel(project);
        lstProject.setBounds(225, 84, 150, 42);
        contentPane.add(lstProject);

        // Add JScrollPane for lstProject
        scrollPaneProject = new JScrollPane(lstProject);
        scrollPaneProject.setBounds(225, 84, 150, 42);
        contentPane.add(scrollPaneProject);

        JCheckBox chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(71, 133, 59, 23);
        contentPane.add(chckbxNotDept);

        JCheckBox chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(270, 133, 59, 23);
        contentPane.add(chckbxNotProject);

        JLabel lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(52, 179, 89, 14);
        contentPane.add(lblEmployee);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaEmployee.setText("John Smith\nFranklin Wong \nAlicia Zelaya\nJennifer Wallace\nRamesh Narayan\nJoyce English\nAhmad Jabbar\nJames Borg\n");
            }
        });
        btnSearch.setBounds(80, 276, 89, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaEmployee.setText("");
            }
        });
        btnClear.setBounds(236, 276, 89, 23);
        contentPane.add(btnClear);
    }
}

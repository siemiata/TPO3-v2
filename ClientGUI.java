import javax.swing.*;

public class ClientGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("NewsClient");

        // Utworzenie listy rozwijanej
        String[] options = {"A", "B", "C", "D", "E"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setBounds(50, 50, 100, 30);

        // Utworzenie przycisku
        JButton button = new JButton("LOAD");
        button.setBounds(170, 50, 80, 30);

        // Utworzenie pola tekstowego
        JTextField textField = new JTextField();
        textField.setBounds(10, 100, 250, 60);

        // Dodanie akcji do przycisku
        button.addActionListener(e -> {
            String selectedOption = comboBox.getSelectedItem().toString();
            textField.setText("Wybrano w polu tekstowym opcję " + selectedOption);
        });

        // Dodanie elementów do ramki
        frame.add(comboBox);
        frame.add(button);
        frame.add(textField);

        // Ustawienie wielkości ramki i wyświetlenie jej
        frame.setSize(300, 200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

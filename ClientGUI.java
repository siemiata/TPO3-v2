import javax.swing.*;

public class ClientGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Moja aplikacja");

        // Utworzenie pola tekstowego
        JTextField textField = new JTextField();
        textField.setBounds(50, 50, 200, 30);

        // Utworzenie listy rozwijanej
        String[] options = {"A", "B", "C", "D", "E"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setBounds(50, 100, 100, 30);

        // Utworzenie przycisku
        JButton button = new JButton("LOAD");
        button.setBounds(50, 150, 100, 30);
        button.addActionListener(e -> {
            // Pobranie wybranej opcji z listy rozwijanej
            String selectedOption = comboBox.getSelectedItem().toString();
            // Wyświetlenie tekstu w polu tekstowym
            textField.setText("Wybrano w polu tekstowym opcję " + selectedOption);
        });

        // Dodanie elementów do ramki
        frame.add(textField);
        frame.add(comboBox);
        frame.add(button);

        // Ustawienie wielkości ramki i wyświetlenie jej
        frame.setSize(300, 250);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

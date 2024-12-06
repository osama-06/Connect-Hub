
package Frontend;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SuccessPanel extends JPanel {
    public SuccessPanel(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());

        JLabel successLabel = new JLabel("Signup successful!", JLabel.CENTER);
        successLabel.setForeground(Color.GREEN);
        add(successLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            // Go back to Main Menu after clicking OK
            cardLayout.show(cardPanel, "MainMenu");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}


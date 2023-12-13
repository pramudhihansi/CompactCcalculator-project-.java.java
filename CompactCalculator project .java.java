import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CompactCalculator extends JFrame {
    private final JTextField display;

    /**
     *
     */
    public CompactCalculator() {
        // Set up the frame
        setTitle("Compact Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        // Create the display
        display = new JTextField();
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Create the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8,6 ));

        String[] buttonLabels = {
                "9", "8", "7", "+",
                "-", "6", "5", "4",
                "*", "/", "3", "2",
                "1", "sqrt", "x^2", ".",
                "0", "=", "x^n", "log",
                "antilog", "sin", "cos", "tan",
                "asin", "acos", "atan",
                "Dec->Bin", "Bin->Dec", "Dec->Oct", "Oct->Dec",
                "Dec->Hex", "Hex->Dec", "C", "<-"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();

            if (buttonText.equals("=")) {
                try {
                    calculateResult();
                } catch (ScriptException ex) {
                    Logger.getLogger(CompactCalculator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (buttonText.equals("sqrt") || buttonText.equals("x^2") || buttonText.equals("x^n") || buttonText.equals("log") || buttonText.equals("antilog")) {
                performUnaryOperation(buttonText);
            } else if (buttonText.equals("sin") || buttonText.equals("cos") || buttonText.equals("tan")
                    || buttonText.equals("asin") || buttonText.equals("acos") || buttonText.equals("atan")) {
                performTrigFunction(buttonText);
            } else if (buttonText.equals("Dec->Bin") || buttonText.equals("Bin->Dec") ||
                    buttonText.equals("Dec->Oct") || buttonText.equals("Oct->Dec") ||
                    buttonText.equals("Dec->Hex") || buttonText.equals("Hex->Dec")) {
                performConversion(buttonText);
            } else if (buttonText.equals("C")) {
                clearDisplay();
            } else if (buttonText.equals("<-")) {
                backspace();
            } else {
                display.setText(display.getText() + buttonText);
            }
        }

        private void calculateResult() throws ScriptException {
            String expression = display.getText();

            // Evaluate the expression using JavaScript engine
            Object result = new ScriptEngineManager().getEngineByName("JavaScript").eval(expression);
            // Display the result
            display.setText(result.toString());
        }

        private void performUnaryOperation(String operation) {
            double input = Double.parseDouble(display.getText());

            switch (operation) {
                case "sqrt":
                    display.setText(String.valueOf(Math.sqrt(input)));
                    break;
                case "x^2":
                    display.setText(String.valueOf(Math.pow(input, 2)));
                    break;
                case "x^n":
                    display.setText(display.getText() + "^");
                    break;
                case "log":
                    display.setText(display.getText() + "log(");
                    break;
                case "antilog":
                    display.setText(display.getText() + "10^(");
                    break;
            }
        }

        private void performTrigFunction(String function) {
            double input = Double.parseDouble(display.getText());

            switch (function) {
                case "sin":
                    display.setText(String.valueOf(Math.sin(Math.toRadians(input))));
                    break;
                case "cos":
                    display.setText(String.valueOf(Math.cos(Math.toRadians(input))));
                    break;
                case "tan":
                    display.setText(String.valueOf(Math.tan(Math.toRadians(input))));
                    break;
                case "asin":
                    display.setText(String.valueOf(Math.toDegrees(Math.asin(input))));
                    break;
                case "acos":
                    display.setText(String.valueOf(Math.toDegrees(Math.acos(input))));
                    break;
                case "atan":
                    display.setText(String.valueOf(Math.toDegrees(Math.atan(input))));
                    break;
            }
        }

        private void performConversion(String conversionType) {
            String inputValue = display.getText();
            try {
                switch (conversionType) {
                    case "Dec->Bin":
                        display.setText(Integer.toBinaryString(Integer.parseInt(inputValue)));
                        break;
                    case "Bin->Dec":
                        display.setText(String.valueOf(Integer.parseInt(inputValue, 2)));
                        break;
                    case "Dec->Oct":
                        display.setText(Integer.toOctalString(Integer.parseInt(inputValue)));
                        break;
                    case "Oct->Dec":
                        display.setText(String.valueOf(Integer.parseInt(inputValue, 8)));
                        break;
                    case "Dec->Hex":
                        display.setText(Integer.toHexString(Integer.parseInt(inputValue)));
                        break;
                    case "Hex->Dec":
                        display.setText(String.valueOf(Integer.parseInt(inputValue, 16)));
                        break;
                }
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }

        private void clearDisplay() {
            display.setText("");
        }

        private void backspace() {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CompactCalculator calculator = new CompactCalculator();
            calculator.setVisible(true);
        });
    }
}

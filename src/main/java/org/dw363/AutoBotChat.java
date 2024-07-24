package org.dw363;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class AutoBotChat extends JFrame {
    private JButton startButton;
    private JTextField intervalField;
    private boolean running;
    private Timer timer;

    public AutoBotChat() {
        setTitle("Нажималка"); // Устанавливаем название окна
        setSize(400, 200); // Размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрытие приложения при закрытии окна
        setLocationRelativeTo(null); // Центрируем окно по экрану
        setResizable(false); // Окно нельзя изменить по размеру

        JPanel panel = new JPanel(); // Панель для элементов
        panel.setLayout(new GridBagLayout()); // Используем GridBagLayout
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Отступы
        panel.setBackground(new Color(40, 40, 60)); // Цвет фона панели

        GridBagConstraints gbc = new GridBagConstraints(); // Настройки для GridBagLayout
        gbc.insets = new Insets(10, 10, 10, 10); // Отступы вокруг элементов
        gbc.fill = GridBagConstraints.HORIZONTAL; // Элементы растягиваются по горизонтали
        gbc.gridx = 0; // Начальная позиция по горизонтали
        gbc.gridy = 0; // Начальная позиция по вертикали

        intervalField = new JTextField("120"); // Поле для интервала
        intervalField.setFont(new Font("Arial", Font.PLAIN, 18)); // Шрифт
        intervalField.setHorizontalAlignment(JTextField.CENTER); // Выравнивание по центру
        intervalField.setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 2)); // Рамка вокруг
        intervalField.setBackground(new Color(70, 70, 100)); // Цвет фона
        intervalField.setForeground(Color.WHITE); // Цвет текста

        JLabel intervalLabel = new JLabel("Интервал (сек):"); // Метка
        intervalLabel.setForeground(Color.WHITE); // Цвет текста метки
        intervalLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Шрифт
        intervalLabel.setHorizontalAlignment(SwingConstants.CENTER); // Выравнивание по центру

        startButton = new JButton("Начать"); // Кнопка для начала и остановки
        startButton.setFont(new Font("Arial", Font.BOLD, 18)); // Шрифт
        startButton.setBackground(new Color(138, 43, 226)); // Цвет фона
        startButton.setForeground(Color.WHITE); // Цвет текста
        startButton.setFocusPainted(false); // Убираем фокусную обводку
        startButton.addActionListener(new StartStopAction()); // Добавляем обработчик нажатий

        // Добавляем эффект наведения
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(148, 0, 211)); // Меняем цвет при наведении
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(138, 43, 226)); // Возвращаем цвет при уходе курсора
            }
        });

        panel.add(intervalLabel, gbc); // Добавляем метку на панель
        gbc.gridy++; // Переходим на следующую строку
        panel.add(intervalField, gbc); // Добавляем поле ввода на панель
        gbc.gridy++; // Переходим на следующую строку
        panel.add(startButton, gbc); // Добавляем кнопку на панель

        add(panel); // Добавляем панель в окно

        running = false; // Изначально автоматизация не запущена
        timer = new Timer(); // Создаем новый таймер

        setFocusable(true); // Окно может принимать фокус
    }

    private class StartStopAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                stopAutoKeyPressing(); // Останавливаем автоматизацию
            } else {
                startAutoKeyPressing(); // Запускаем автоматизацию
            }
        }
    }

    private void startAutoKeyPressing() {
        running = true; // Устанавливаем флаг, что автоматизация запущена
        startButton.setText("Стоп"); // Меняем текст кнопки
        setState(Frame.ICONIFIED); // Сворачиваем окно

        int interval;
        try {
            // Получаем интервал из поля и переводим в миллисекунды
            interval = Integer.parseInt(intervalField.getText()) * 1000;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректное число.", "Ошибка", JOptionPane.ERROR_MESSAGE); // Сообщение об ошибке
            stopAutoKeyPressing(); // Останавливаем автоматизацию
            return;
        }

        // Планируем задачу
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Robot robot = new Robot(); // Создаем объект Robot
                    robot.keyPress(KeyEvent.VK_T); // Нажимаем клавишу T
                    robot.keyRelease(KeyEvent.VK_T); // Отпускаем клавишу T
                    Thread.sleep(100); // Задержка
                    robot.keyPress(KeyEvent.VK_UP); // Нажимаем клавишу вверх
                    robot.keyRelease(KeyEvent.VK_UP); // Отпускаем клавишу вверх
                    Thread.sleep(100); // Задержка
                    robot.keyPress(KeyEvent.VK_ENTER); // Нажимаем клавишу Enter
                    robot.keyRelease(KeyEvent.VK_ENTER); // Отпускаем клавишу Enter
                } catch (Exception ex) {
                    ex.printStackTrace(); // Выводим ошибку
                }
            }
        }, 0, interval); // Запускаем задачу через интервал
    }

    private void stopAutoKeyPressing() {
        running = false; // Устанавливаем флаг, что автоматизация остановлена
        startButton.setText("Начать"); // Меняем текст кнопки
        timer.cancel(); // Останавливаем таймер
        timer = new Timer(); // Создаем новый таймер
        setState(Frame.NORMAL); // Восстанавливаем окно
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Устанавливаем системный Look and Feel
            } catch (Exception e) {
                e.printStackTrace(); // Выводим ошибку
            }

            AutoBotChat app = new AutoBotChat(); // Создаем приложение
            app.setVisible(true); // Делаем окно видимым
        });
    }
}
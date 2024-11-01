import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Frame extends JFrame {
    private final ImagePanel imagePanel;
    private static final JButton openButton = new JButton("Открыть");
    private static final JButton erosionButton = new JButton("Эрозия");
    private static final JButton dilationButton = new JButton("Дилатация");
    private static final JButton openingButton = new JButton("Открытие");
    private static final JButton closingButton = new JButton("Закртыие");
    private static final JButton lowPassFilterButton = new JButton("Сглаживание");

    public Frame() {
        setSize(1400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        imagePanel = new ImagePanel();
        add(imagePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton, BorderLayout.SOUTH);
        buttonPanel.add(erosionButton, BorderLayout.SOUTH);
        buttonPanel.add(dilationButton);
        buttonPanel.add(openingButton);
        buttonPanel.add(closingButton);
        buttonPanel.add(lowPassFilterButton);

        add(buttonPanel, BorderLayout.SOUTH);

        openButton.addActionListener(new LoadImageAction());
        erosionButton.addActionListener(new MorphologyAction("erosion"));
        dilationButton.addActionListener(new MorphologyAction("dilation"));
        openingButton.addActionListener(new MorphologyAction("opening"));
        closingButton.addActionListener(new MorphologyAction("closing"));
        lowPassFilterButton.addActionListener(new MorphologyAction("low_pass"));
    }

    private class LoadImageAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                imagePanel.loadImage(filePath);
                BufferedImage img = imagePanel.getImage();
                ;
                imagePanel.setSize(img.getWidth(), img.getHeight());
                setSize(img.getWidth(), img.getHeight() + 15);
            }
        }
    }

    private class MorphologyAction implements ActionListener {
        private String operationType;

        public MorphologyAction(String operationType) {
            this.operationType = operationType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            BufferedImage processedImage = MorphologyProcessor.applyOperation(imagePanel.getImage(), operationType);
            imagePanel.setImage(processedImage);
        }
    }
}
